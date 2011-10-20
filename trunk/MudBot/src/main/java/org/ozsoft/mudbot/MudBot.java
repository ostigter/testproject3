package org.ozsoft.mudbot;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.ozsoft.telnet.TelnetClient;
import org.ozsoft.telnet.TelnetListener;

/**
 * MUD robot automating the slow, repetitive process of killing countless
 * monsters in order to gain experience points. <br />
 * <br />
 * 
 * Implemented as a Telnet client.
 * 
 * @author Oscar Stigter
 */
public class MudBot implements TelnetListener {

    /** Robot state. */
    private enum State {

        /** Idle (home). */
        IDLE,

        /** Traveling to an area. */
        TRAVELING_TO_AREA,

        /** Hunting for monsters in an area. */
        HUNTING,

        /** In combat with a monster. */
        IN_COMBAT,

        /** Looting a monster's corpse. */
        LOOTING,

        /** Resting to regenerate HP and CP. */
        RESTING,

        /** Sleeping (camping) to regenerate HP and CP even faster. */
        SLEEPING,

        /** Going home. */
        GOING_HOME,

        /** Selling loot and offering heads. */
        DUMPING,
    };
    
    /** Default window width. */
    private static final int DEFAULT_WIDTH = 800;

    /** Default window height. */
    private static final int DEFAULT_HEIGHT = 600;
    
    /** Font used by text components. */
    private static final Font FONT = new Font("Monospaced", Font.PLAIN, 12);

    /** Minimum HP % being safe for hunting. */
    private static final double MIN_HP_PERC = 0.4;

    /** Minimum HP % to resume after resting. */
    private static final double SAFE_HP_PERC = 0.9;

    /** Minimum CP for hunting. */
    private static final int MIN_CP = 220;

    /** Delay between commands. */
    private static final long DELAY = 1000L;

    /** Time in ms for area to reset. */
    private static final long AREA_RESET_TIME = 600000L; // 10 min

    /** Regex to parse healthbar. */
    private static final Pattern healthBarPattern = Pattern.compile("HP: \\[(\\d+)/(\\d+)\\]  CONC: \\[(\\d+)/(\\d+)\\]");

    /** NewLine character. */
    private static final String NEWLINE = "\r\n";

    /** Telnet client. */
    private final TelnetClient telnetClient;

    /** Buffer for received messages from the MUD to be processed. */
    private final StringBuilder messageBuffer;

    /** Current HP percentage. */
    private double hpPerc;

    /** Current CP. */
    private int cp;

    /** Macro's. */
    private Map<String, String[]> macros;

    /** Known areas. */
    private List<Area> areas;

    /** Next area index to travel to. */
    private int nextArea;

    /** Current area. */
    private Area currentArea;

    /** Current monster the robot is in combat with. */
    private Monster monster;

    /** Whether the robot is active. */
    private boolean robotStarted = false;

    /** Current robot state. */
    private State state;

    /** Main frame. */
    private JFrame frame;
    
    /** Start/Stop button. */
    private JButton startStopButton;
    
    /** Status text. */
    private JTextField statusText;
    
    /** Location text. */
    private JTextField locationText;
    
    /** HP text. */
    private JTextField hpText;
    
    /** CP text. */
    private JTextField cpText;
    
    /** Messages text. */
    private StyledTextPane messagesText;

    /** Clear messages button. */
    private JButton clearButton;
  
    /** Command text. */
    private JTextField commandText;

    /**
     * Application's entry point.
     * 
     * @param args
     *            Command line arguments.
     */
    public static void main(String[] args) {
        new MudBot();
    }

    /**
     * Constructs the application.
     */
    public MudBot() {
        messageBuffer = new StringBuilder();

        createMacros();

        areas = new ArrayList<Area>();
        createAreas();

        createUI();

        telnetClient = new TelnetClient();
        telnetClient.addTelnetListener(this);
        telnetClient.connect("mud.agesofdespair.net", 5000);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.telnet.TelnetListener#connected()
     */
    @Override
    public void connected() {
        clearMessageBuffer();
        clearMessages();
        setStatus("Connected");
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.telnet.TelnetListener#disconnected()
     */
    @Override
    public void disconnected() {
        clearMessageBuffer();
        setStatus("Disconnected");
        locationText.setText("");
        hpText.setText("");
        cpText.setText("");
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.telnet.TelnetListener#textReceived(java.lang.String)
     */
    @Override
    public void textReceived(String text) {
        appendText(text);
        checkHealth(text);
        if (robotStarted) {
            synchronized (messageBuffer) {
                messageBuffer.append(text);
                processMessages();
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.telnet.TelnetListener#ansiCodeReceived(java.lang.String)
     */
    @Override
    public void ansiCodeReceived(String code) {
        // Not implemented yet.
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.telnet.TelnetListener#telnetExceptionCaught(java.lang.Throwable)
     */
    @Override
    public void telnetExceptionCaught(Throwable t) {
        logError("Connection error", t);
        stopRobot();
    }

    /**
     * Creates the application's GUI.
     */
    private void createUI() {
        frame = new JFrame("MUD Robot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
            @Override
            public void windowActivated(WindowEvent e) {
                commandText.selectAll();
                commandText.requestFocusInWindow();
            }
        });
        frame.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(new TitledBorder("Control"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        frame.getContentPane().add(controlPanel, gbc);
        
        startStopButton = new JButton("Start");
        startStopButton.setFocusable(false);
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startStopButton.setEnabled(false);
                if (!robotStarted) {
                    startRobot();
                } else {
                    stopRobot();
                }
            } 
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 5, 5);
        controlPanel.add(startStopButton, gbc);
        
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(new TitledBorder("Information"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        frame.getContentPane().add(infoPanel, gbc);
        
        JLabel label = new JLabel("Status:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 5, 5);
        infoPanel.add(label, gbc);
        
        statusText = new JTextField();
        statusText.setFont(FONT);
        statusText.setEditable(false);
        statusText.setFocusable(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 0, 5, 5);
        infoPanel.add(statusText, gbc);
 
        label = new JLabel("Location:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        infoPanel.add(label, gbc);
        
        locationText = new JTextField();
        locationText.setFont(FONT);
        locationText.setEditable(false);
        locationText.setFocusable(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 5, 5);
        infoPanel.add(locationText, gbc);
 
        label = new JLabel("HP:");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        infoPanel.add(label, gbc);
        
        hpText = new JTextField();
        hpText.setFont(FONT);
        hpText.setEditable(false);
        hpText.setFocusable(false);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 5, 5);
        infoPanel.add(hpText, gbc);
 
        label = new JLabel("CP:");
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        infoPanel.add(label, gbc);
        
        cpText = new JTextField();
        cpText.setFont(FONT);
        cpText.setEditable(false);
        cpText.setFocusable(false);
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 5, 5);
        infoPanel.add(cpText, gbc);
 
        JPanel messagePanel = new JPanel(new GridBagLayout());
        messagePanel.setBorder(new TitledBorder("Messages"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        frame.getContentPane().add(messagePanel, gbc);
        
        messagesText = new StyledTextPane();
        messagesText.setFocusable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 5, 5, 5);
        messagePanel.add(messagesText, gbc);
        
        clearButton = new JButton("Clear");
        clearButton.setFocusable(false);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearMessages();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 5, 5);
        messagePanel.add(clearButton, gbc);

        JPanel commandPanel = new JPanel(new GridBagLayout());
        commandPanel.setBorder(new TitledBorder("Send Command"));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        frame.getContentPane().add(commandPanel, gbc);
        
        commandText = new JTextField();
        commandText.setFont(FONT);
        commandText.setBackground(Color.BLACK);
        commandText.setForeground(Color.WHITE);
        commandText.setCaretColor(Color.WHITE);
        commandText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                commandText.selectAll();
            }
        });
        commandText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendCommand(commandText.getText().trim());
                    commandText.selectAll();
                    commandText.requestFocusInWindow();
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 5, 5);
        commandPanel.add(commandText, gbc);

        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    
    /**
     * Append message text to the messages log.
     * 
     * @param text
     *            The message text.
     */
    private void appendText(String text) {
        messagesText.append(text);
    }
    
    /**
     * Clears the message log.
     */
    private void clearMessages() {
        messagesText.clear();
    }

    /**
     * Sends a single command to the MUD.
     * 
     * @param command
     *            The command.
     */
    private void sendCommand(String command) {
        String[] macro = macros.get(command);
        if (macro != null) {
            sendCommands(macro);
        } else {
            String text = command + NEWLINE;
            telnetClient.sendText(text);
            appendText(text);
        }
    }

    /**
     * Sends multiple commands as a single compound command.
     * 
     * @param commands
     *            The commands.
     */
    private void sendCommands(String[] commands) {
        for (String command : commands) {
            sendCommand(command);
        }
    }

    /**
     * Starts the robot.
     */
    private void startRobot() {
        if (!robotStarted) {
            clearMessageBuffer();
            state = State.IDLE;
            nextArea = -1;
            robotStarted = true;
            appendText("[Bot] Started\n");
            startStopButton.setText("Stop");
            startStopButton.setEnabled(true);
            sendCommand("hp");
        }
    }

    /**
     * Stops the robot immediately.
     */
    private void stopRobot() {
        if (robotStarted) {
            robotStarted = false;
            clearMessageBuffer();
            appendText("[Bot] Stopped\n");
            setStatus("Stopped");
            locationText.setText("");
            startStopButton.setText("Start");
            startStopButton.setEnabled(true);
        }
    }

    /**
     * Processes incoming text from the MUD.
     */
    private void processMessages() {
        String text = messageBuffer.toString();

        if (robotStarted) {
            if (text.contains("You can't go that way!")) {
                clearMessageBuffer();
                appendText(String.format("[Alert] Got lost in area '%s'\n", currentArea));
                locationText.setText(String.format("%s (LOST!)", currentArea));
                stopRobot();
            } else {
                if (state == State.IDLE) {
                    huntNextArea();
                } else if (state == State.TRAVELING_TO_AREA) {
                    travelToArea(text);
                } else if (state == State.HUNTING) {
                    hunt(text);
                } else if (state == State.IN_COMBAT) {
                    fight(text);
                } else if (state == State.LOOTING) {
                    loot(text);
                } else if (state == State.RESTING) {
                    rest(text);
                } else if (state == State.SLEEPING) {
                    sleep(text);
                } else if (state == State.GOING_HOME) {
                    goHome(text);
                } else if (state == State.DUMPING) {
                    dump(text);
                } else {
                    // This should never happen.
                    logError("Invalid bot status: " + state);
                }
            }

            if (state != State.IDLE && state != State.TRAVELING_TO_AREA) {
                delay();
            }
        }
    }

    /**
     * Scan incoming messages for an HP/CP update.
     * 
     * @param text
     *            The message text.
     */
    private void checkHealth(String text) {
        Matcher m = healthBarPattern.matcher(text);
        if (m.find()) {
            try {
                int hp = Integer.parseInt(m.group(1));
                int maxHp = Integer.parseInt(m.group(2));
                hpPerc = (double) hp / (double) maxHp;
                cp = Integer.parseInt(m.group(3));
                int maxCp = Integer.parseInt(m.group(4));
                double cpPerc = (double) cp / (double) maxCp;
                hpText.setText(String.format("%d / %d (%.0f%%)", hp, maxHp, hpPerc * 100));
                cpText.setText(String.format("%d / %d (%.0f%%)", cp, maxCp, cpPerc * 100));
            } catch (NumberFormatException e) {
                logError("Could not update health", e);
            }
        }
    }
    
    private void logError(String message, Throwable ... cause) {
        String errorText = null;
        if (cause != null) {
            errorText = String.format("ERROR: %s; exception: %s" , message, cause);
        } else {
            errorText = String.format("ERROR: %s");
        }
        appendText("[Alert] >>> " + errorText);
        System.err.println(errorText);
    }

    /**
     * Updates the robot's status.
     * 
     * @param status The status.
     */
    private void setStatus(String status) {
        appendText(String.format("[Bot] %s\n", status));
        statusText.setText(status);
    }

    /**
     * Let the robot hunt the next area.
     */
    private void huntNextArea() {
        // Determine next area;
        appendText("[Bot] Looking for the next area to hunt\n");
        currentArea = null;
        for (int i = 0; i < areas.size(); i++) {
            nextArea = (nextArea + 1) % areas.size();
            Area area = areas.get(nextArea);
            if (area.getLastVisitedSince() > AREA_RESET_TIME) {
                currentArea = area;
                break;
            }
        }

        if (currentArea != null) {
            setStatus(String.format("Traveling to %s", currentArea));
            state = State.TRAVELING_TO_AREA;
            sendCommands(currentArea.toPath);
        } else {
            appendText("[Bot] No more areas to hunt\n");
            stopRobot();
        }
    }
    
    /**
     * Handles incoming messages while traveling.
     * 
     * @param text
     *            The message text.
     */
    private void travelToArea(String text) {
        if (text.contains(currentArea.roomDescription)) {
            clearMessageBuffer();
            currentArea.reset();
            state = State.HUNTING;
            appendText(String.format("[Bot] Arrived at %s\n", currentArea));
            setStatus("Hunting");
            locationText.setText(currentArea.getName());
            sendCommand("l");
        }
    }

    /**
     * Handles incoming messages while hunting.
     * 
     * @param text
     *            The message text.
     */
    private void hunt(String text) {
        if (hpPerc >= MIN_HP_PERC && cp >= MIN_CP) {
            lookForMonster(text);
            if (monster != null) {
                state = State.IN_COMBAT;
                setStatus(String.format("In combat with %s", monster));
                sendCommand("rm " + monster.getAlias());
            } else {
                String direction = currentArea.getNextDirection();
                if (direction != null) {
                    appendText(String.format("[Bot] Nothing here; proceeding '%s'\n", direction));
                    sendCommand(direction);
                } else {
                    currentArea.updateLastVisisted();
                    state = State.GOING_HOME;
                    setStatus("Area finished, traveling home");
                    locationText.setText("");
                    sendCommands(currentArea.homePath);
                }
            }
        } else {
            state = State.RESTING;
            setStatus("Health getting low, resting");
            sendCommand("camping");
            rest(text);
        }
        clearMessageBuffer();
    }

    /**
     * Handles incoming messages while in combat.
     * 
     * @param text
     *            The message text.
     */
    private void fight(String text) {
        if (text.contains(" falls lifeless to the ground.")) {
            clearMessageBuffer();
            state = State.LOOTING;
            setStatus(String.format("Looting corpse of %s\n", monster));
            sendCommand("loot");
            sendCommand("ct");
        } else if (text.contains("You have no target for the skill.")) {
            appendText("[Alert] Lost track of monster; resuming the hunt\n");
            clearMessageBuffer();
            state = State.HUNTING;
            sendCommand("l");
        } else if (text.contains("You have finished concentrating on the skill.")) {
            clearMessageBuffer();
            if (cp >= MIN_CP) {
                // Attack again.
                sendCommand("rm " + monster.getAlias());
            }
        }
    }

    /**
     * Handles incoming messages while looting.
     * 
     * @param text
     *            The message text.
     */
    private void loot(String text) {
        if (text.contains("You grab hold of the corpse and lift it's neck up a bit.")) {
            clearMessageBuffer();
            state = State.HUNTING;
            setStatus("Hunting");
            sendCommand("l");
        }
    }

    /**
     * Handles incoming messages while resting.
     * 
     * @param text
     *            The message text.
     */
    private void rest(String text) {
        if (text.contains(" lie down, praying for the spirits of nature to guard your sleep.")) {
            clearMessageBuffer();
            state = State.SLEEPING;
            setStatus("Sleeping");
        } else if (text.contains("You can't camp so soon again.")) {
            clearMessageBuffer();
        } else if (hpPerc >= SAFE_HP_PERC && cp >= MIN_CP) {
            clearMessageBuffer();
            appendText("[Bot] Done resting; resuming the hunt\n");
            setStatus("Hunting");
            state = State.HUNTING;
            sendCommand("l");
        }
    }

    /**
     * Handles incoming messages while sleeping.
     * 
     * @param text
     *            The message text.
     */
    private void sleep(String text) {
        if (text.contains("You wake from your rest and feel much better.")) {
            appendText("[Bot] Done camping; resting until fully healed\n");
            clearMessageBuffer();
            state = State.RESTING;
            setStatus("Resting");
            rest(text);
        }
    }
    
    private void goHome(String text) {
        if (text.contains("You stand at the massive eastern gates of the city of Loriah.")) {
            appendText("[Bot] Arrived home\n");
            clearMessageBuffer();
            state = State.DUMPING;
            setStatus("Dumping items");
            delay();
            sendCommand("sell");
            delay();
            sendCommand("bar");
            delay();
            sendCommand("offer all");
            delay();
            sendCommand("_bar");
            delay();
            sendCommand("l");
        }
    }

    /**
     * Handles incoming messages while dumping items.
     * 
     * @param text
     *            The message text.
     */
    private void dump(String text) {
        if (text.contains("You stand at the massive eastern gates of the city of Loriah.")) {
            clearMessageBuffer();
            state = State.IDLE;
            setStatus("Home");
            sendCommand("l");
        }
    }

    /**
     * Clears the buffer for incoming messages.
     */
    private void clearMessageBuffer() {
        synchronized (messageBuffer) {
            messageBuffer.delete(0, messageBuffer.length());
        }
    }

    /**
     * Parse the room description, looking for monsters to kill.
     * 
     * @param text
     *            The room description.
     */
    private void lookForMonster(String text) {
        monster = null;
        int pos = text.lastIndexOf("Obvious exit");
        if (pos != -1) {
            pos = text.indexOf('.', pos);
            if (pos != -1) {
                text = text.substring(pos + 1);
                String[] lines = text.split(NEWLINE);
                boolean isSafe = true;
                boolean hasLoot = false;
                for (String line : lines) {
                    if (line.length() > 0 && line.charAt(0) != '>') {
                        String name = line.trim();
                        if (!name.startsWith("The corpse of ")) {
                            Monster m = currentArea.getMonster(name);
                            if (m != null) {
                                if (monster == null) {
                                    monster = m;
                                }
                            } else if (currentArea.isItem(name)) {
                                hasLoot = true;
                            } else if (name.contains("(flagged by ")) {
                                appendText("[Bot] Another player is active here; moving on\n");
                                isSafe = false;
                                monster = null;
                                break;
                            } else {
                                // Unknown object, do NOT attack anything here!
                                appendText(String.format("[Bot] Unknown object found: '%s'; moving on\n", name));
                                isSafe = false;
                                monster = null;
                                break;
                            }
                        }
                    }
                }

                if (isSafe && hasLoot) {
                    appendText("[Bot] Collecting found items\n");
                    sendCommand("get all");
                }
            }
        }
    }

    /**
     * Creates the MUD-specific macros.
     */
    private void createMacros() {
        macros = new HashMap<String, String[]>();
        macros.put("start", new String[]{"brief", "house", "valenthos", "get_eq", "hall", "login", "3 n", "14 e", "brief", "bstance lion", "party create", "l", "hp"});
        macros.put("end", new String[]{"brief", "w", "house", "valenthos", "store_eq", "brief", "l", "l me"});
        macros.put("get_eq", new String[]{"open valenthos1", "get all from valenthos1", "close valenthos1", "open valenthos2", "get all from valenthos2", "close valenthos2", "get all", "keep all", "wear all", "wield axe", "wield axe 2 in left hand"});
        macros.put("store_eq", new String[]{"remove all", "unkeep all", "open valenthos1", "put all in valenthos1", "close valenthos1", "open valenthos2", "put all in valenthos2", "close valenthos2", "drop all"});
        macros.put("sell", new String[]{"brief", "13 w", "3 n", "sell all", "2 s", "e", "deposit all", "w", "s", "13 e", "brief", "l", "money"});
        macros.put("bar", new String[]{"brief", "5 e", "5 ne", "7 n", "7 nw", "enter path", "n", "brief", "l"});
        macros.put("_bar", new String[]{"brief", "2 s", "7 se", "7 s", "5 sw", "5 w", "brief", "l"});
        macros.put("heights", new String[]{"brief", "3 e", "s", "climb", "brief", "l"});
        macros.put("_heights", new String[]{"brief", "down", "n", "3 w", "brief", "l"});
        macros.put("farm", new String[]{"brief", "e", "ne", "n", "path", "brief", "l"});
        macros.put("_farm", new String[]{"brief", "leave", "s", "sw", "w", "brief", "l"});
        macros.put("library", new String[]{"brief", "e", "2 se", "2 s", "enter path", "n", "ne", "n", "enter", "brief", "l"});
        macros.put("_library", new String[]{"brief", "out", "s", "sw", "s", "leave", "2 n", "2 nw", "brief", "l"});
        macros.put("treetown", new String[]{"brief", "e", "13 se", "4 e", "3 s", "se", "path", "n", "climb tree", "brief", "l"});
        macros.put("_treetown", new String[]{"brief", "d", "s", "s", "nw", "3 n", "4 w", "13 nw", "w", "brief", "l"});
        macros.put("raja", new String[]{"brief", "5 e", "13 ne", "enter village", "brief", "l"});
        macros.put("_raja", new String[]{"brief", "leave", "13 sw", "5 w", "brief", "l"});
        macros.put("orcs", new String[]{"brief", "e", "2 se", "s", "7 sw", "16 w", "sw", "s", "path", "brief", "l"});
        macros.put("_orcs", new String[]{"brief", "w", "n", "ne", "16 e", "7 ne", "n", "2 nw", "w", "brief", "l"});
        macros.put("unicorns", new String[]{"brief", "6 e", "3 se", "enter opening", "3 n", "brief", "l"});
        macros.put("_unicorns", new String[]{"brief", "3 s", "s", "3 nw", "6 w", "brief", "l"});
        macros.put("treants", new String[]{"brief", "5 e", "7 ne", "4 e", "n", "6 ne", "5 e", "enter path", "brief", "l"});
        macros.put("_treants", new String[]{"brief", "out", "5 w", "6 sw", "s", "4 w", "7 sw", "5 w", "brief", "l"});
    }

    /**
     * Creates the MUD-specific areas.
     */
    private void createAreas() {
        Area area = null;

        // Treetown
        area = new Area("Treetown");
        area.toPath = new String[]{"treetown"};
        area.homePath = new String[]{"_treetown"};
        area.roomDescription = "This is the southern part of a small treetown.";
        area.directions = new String[] {"e", "w", "n", "w", "e", "ne", "sw", "n", "w", "e", "n", "u", "d", "n", "e", "e", "w", "s", "u", "d", "w", "s", "s", "s"};
        area.addMonster(new Monster("An apeman worker", "worker"));
        area.addMonster(new Monster("A friendly nurse", "nurse"));
        area.addMonster(new Monster("An apewoman", "apewoman"));
        area.addMonster(new Monster("A salesman cleaning his stand", "salesman"));
        area.addItem(" gold coins");
        area.addItem("The decapitated head of ");
        area.addItem("A sturdy leather belt");
        area.addItem("Worker's helmet");
        area.addItem("A white collar");
        area.addItem("A dress");
        area.addItem("Information about our theatre");
        areas.add(area);

        // Oz'ikel Forest.
        area = new Area("Oz'ikel Forest");
        area.toPath = new String[]{"orcs"};
        area.homePath = new String[]{"_orcs"};
        area.roomDescription = "A path through the magical forest of Oz'ikel.";
        area.directions = new String[]{"s", "s", "s", "s", "e", "n", "n", "n", "e", "s", "s", "e", "n", "n", "se", "nw", "3 w", "n"};
        area.addMonster(new Monster("An ugly orc", "orc"));
        area.addMonster(new Monster("A bloodthirsty orc", "orc"));
        area.addMonster(new Monster("A horny orc", "orc"));
        area.addMonster(new Monster("A nasty orc", "orc"));
        area.addMonster(new Monster("A strong orc", "orc"));
        area.addMonster(new Monster("A slimy orc", "orc"));
        area.addMonster(new Monster("An evil orc", "orc"));
        area.addMonster(new Monster("A sick orc", "orc"));
        area.addMonster(new Monster("A happy orc", "orc"));
        area.addMonster(new Monster("A disgusting orc", "orc"));
        area.addMonster(new Monster("A fat orc", "orc"));
        area.addItem(" gold coins");
        area.addItem("The decapitated head of ");
        area.addItem("An orcish axe");
        area.addItem("An orcish club");
        area.addItem("An orcish spear");
        area.addItem("An orcish dagger");
        area.addItem("An orcish throwing dagger");
        areas.add(area);
        
//        area = new Area("Glade of the Unicorns");
//        area.toPath = new String[]{"unicorns"};
//        area.homePath = new String[]{"_unicorns"};
//        area.roomDescription = "This is the southernmost part of the glade.";
//        area.directions = new String[]{"n", "e", "e", "n", "w", "w", "n", "s", "w", "w", "s", "e", "e", "s"};
//        area.addMonster(new Monster("A magnificent unicorn", "unicorn"));
//        area.addMonster(new Monster("A small unicorn", "unicorn"));
//        areas.add(area);
    }

    /**
     * Closes the application.
     */
    private void close() {
        stopRobot();
        telnetClient.shutdown();
        frame.dispose();
    }

    /**
     * Delays the current thread.
     */
    private static void delay() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            // Ignore.
        }
    }

}
