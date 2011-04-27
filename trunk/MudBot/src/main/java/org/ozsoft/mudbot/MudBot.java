package org.ozsoft.mudbot;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import org.ozsoft.telnet.TelnetClient;
import org.ozsoft.telnet.TelnetListener;

/**
 * MUD robot automating the slow, repetitive process of killing countless
 * monsters in order to gain experience points. <br />
 * <br />
 * 
 * Implemented as a Telnet client.
 * 
 * @todo Fleeing
 * @todo ANSI colors
 * 
 * @author Oscar Stigter
 */
public class MudBot implements TelnetListener {
    
    /** Robot state. */
    private enum State {
        
        /** Idle (safe). */
        IDLE,
        
        /** Travelling to or from an area. */
        TRAVELING,
        
        /** Hunting for monsters in an area. */
        HUNTING,

        /** In combat with a monster. */
        FIGHTING,

        /** Claiming a trophy (get the head of a killed monster). */
        CLAIMING_TROPHY,

        /** Resting to regenerate HP and CP. */
        RESTING,
        
        /** Camping to regenerate HP and CP faster. */
        CAMPING,
        
        /** Selling loot and offering heads. */
        DUMPING_STUFF,
    };

    /** Minimum HP % for hunting. */
    private static final double MIN_HP_PERC = 0.4;

    /** Minimum HP % to resume after resting. */
    private static final double SAFE_HP_PERC = 0.9;

    /** Minimum CP for hunting. */
    private static final int MIN_CP = 220;
    
    /** Delay between commands. */
    private static final long DELAY = 1000;
    
    /** Time in ms for area to reset. */
    private static final long AREA_RESET_TIME = 600000; // 10 min

    /** Regex to parse healthbar. */
    private static final Pattern healthBarPattern =
            Pattern.compile("HP: \\[(\\d+)/(\\d+)\\]  CONC: \\[(\\d+)/(\\d+)\\]");

    /** NewLine character. */
    private static final String NEWLINE = "\r\n";

    /** Telnet client. */
    private final TelnetClient telnetClient;
    
    private final StringBuilder receivedText;
    
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
    private boolean active = false;

    /** Current robot state. */
    private State state = State.IDLE;

    /** Main frame. */
    private JFrame frame;
    
    /** Text pane. */
    private StyledTextPane textPane;
    
    /** Input text with command line to send. */
    private JTextField commandLine;
    
    /** Send button. */
    private JButton sendButton;

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
     * Constructs the MUD robot.
     */
    public MudBot() {
        telnetClient = new TelnetClient();
        telnetClient.addTelnetListener(this);
        
        receivedText = new StringBuilder();
        
        createMacros();
        
        areas = new ArrayList<Area>();
        createAreas();

        createUI();

        telnetClient.connect("mud.agesofdespair.net", 5000);
    }

    @Override
    public void connected() {
        clearIncomingText();
        appendText(">>> Connected\n");
    }

    @Override
    public void disconnected() {
        clearIncomingText();
        appendText("\n>>> Disconnected\n");
    }

    @Override
    public void textReceived(String text) {
        appendText(text);
        if (active) {
            synchronized (receivedText) {
                receivedText.append(text);
            }
            processIncomingText();
        }
    }

    @Override
    public void ansiCodeReceived(String code) {
        // Not implemented.
    }

    @Override
    public void telnetExceptionCaught(Throwable t) {
        stop();
        appendText(String.format(">>> ERROR: %s\n", t.getMessage()));
    }

    private void createUI() {
        frame = new JFrame("MUD Robot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        frame.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem menuItem = new JMenuItem("Start Robot");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startRobot();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Stop Robot");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopRobot();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        textPane = new StyledTextPane();
        textPane.setFocusable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        frame.getContentPane().add(textPane, gbc);

        commandLine = new JTextField();
        commandLine.setFont(new Font("Monospaced", Font.PLAIN, 12));
        commandLine.setBackground(Color.BLACK);
        commandLine.setForeground(Color.WHITE);
        commandLine.setCaretColor(Color.WHITE);
        commandLine.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                commandLine.selectAll();
            }
        });
        commandLine.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendCommand(commandLine.getText().trim());
                    commandLine.selectAll();
                    commandLine.requestFocusInWindow();
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        frame.getContentPane().add(commandLine, gbc);

        frame.getRootPane().setDefaultButton(sendButton);

        frame.pack();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        commandLine.requestFocusInWindow();
    }

    private void appendText(String text) {
        textPane.append(text);
    }

    private void startRobot() {
        start();
    }

    private void stopRobot() {
        stop();
    }

    private void exit() {
        stopRobot();
        telnetClient.disconnect();
        frame.dispose();
    }

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
    
    private void sendCommands(String[] commands) {
        for (String command : commands) {
            sendCommand(command);
        }
    }

    private void start() {
        if (!active) {
            clearIncomingText();
            nextArea = -1;
            active = true;
            appendText(">>> Robot started\n");
            sendCommand("hp");
            huntNextArea();
        }
    }
    
    private void stop() {
        if (active) {
            state = State.IDLE;
            active = false;
            clearIncomingText();
            appendText(">>> Robot stopped\n");
        }
    }

    private void processIncomingText() {
        String text = receivedText.toString();
        
        checkHealth(text);

        if (active) {
            if (text.contains("You can't go that way!")) {
                appendText(">>> Oops, we got lost!\n");
                clearIncomingText();
                stop();
            } else {
                if (state == State.IDLE) {
                    huntNextArea();
                } else if (state == State.TRAVELING) {
                    travel(text);
                } else if (state == State.HUNTING) {
                    hunt(text);
                } else if (state == State.FIGHTING) {
                    fight(text);
                } else if (state == State.CLAIMING_TROPHY) {
                    claimTrophy(text);
                } else if (state == State.RESTING) {
                    rest(text);
                } else if (state == State.CAMPING) {
                    camp(text);
                } else if (state == State.DUMPING_STUFF) {
                    dumpStuff(text);
                } else {
                    // This should never happen.
                    System.err.println("ERROR: Invalid state: " + state);
                }
            }
            
            if (state != State.IDLE && state != State.TRAVELING) {
                sleep();
            }
        }
    }
    
    private void clearIncomingText() {
        synchronized (receivedText) {
            receivedText.delete(0, receivedText.length());
        }
    }

    private void huntNextArea() {
        // Determine next area;
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
            appendText(String.format(">>> Going to area '%s'\n", currentArea));
            state = State.TRAVELING;
            sendCommands(currentArea.toPath);
        } else {
            appendText(">>> No more areas to hunt\n");
            stop();
        }
    }

    private void travel(String text) {
        if (text.contains("You stand at the massive eastern gates of the city of Loriah.")) {
            appendText(">>> Arrived home\n");
            clearIncomingText();
            state = State.DUMPING_STUFF;
            appendText(">>> Selling loot and offering heads\n");
            sleep();
            sendCommand("sell");
            sleep();
            sendCommand("bar");
            sleep();
            sendCommand("offer all");
            sleep();
            sendCommand("_bar");
            sleep();
        } else if (text.contains(currentArea.roomDescription)) {
            appendText(String.format(">>> Arrived at area '%s'\n", currentArea));
            clearIncomingText();
            currentArea.reset();
            state = State.HUNTING;
            hunt(text);
        }
    }
    
    private void dumpStuff(String text) {
        if (text.contains("You stand at the massive eastern gates of the city of Loriah.")) {
            appendText(">>> Arrived home\n");
            clearIncomingText();
            state = State.IDLE;
            //FIXME: Fix DUMPING_STUFF
            stopRobot();
        }
    }

    private void hunt(String text) {
        if (hpPerc >= MIN_HP_PERC && cp >= MIN_CP) {
            appendText(">>> Looking for monsters\n");
            scanForMonster(text);
            if (monster != null) {
                appendText(String.format(">>> Attacking %s\n", monster));
                state = State.FIGHTING;
                sendCommand("rm " + monster.getAlias());
            } else {
                String direction = currentArea.getNextDirection();
                if (direction != null) {
                    appendText(String.format(">>> Nothing here, proceeding '%s'\n", direction));
                    sendCommand(direction);
                } else {
                    appendText(">>> Area finished, going home\n");
                    currentArea.updateLastVisisted();
                    state = State.TRAVELING;
                    sendCommands(currentArea.homePath);
                }
            }
        } else {
            appendText(">>> HP/CP getting low, resting\n");
            state = State.RESTING;
            sendCommand("camping");
            rest(text);
        }
        clearIncomingText();
    }

    private void fight(String text) {
        if (text.contains(" falls lifeless to the ground.")) {
            appendText(String.format(">>> Killed %s\n", monster));
            clearIncomingText();
            sendCommand("loot");
            state = State.CLAIMING_TROPHY;
            sendCommand("ct");
        } else if (text.contains("You have no target for the skill.")) {
            appendText(">>> Oops, lost track of monster, resuming the hunt\n");
            clearIncomingText();
            state = State.HUNTING;
            sendCommand("l");
        } else if (text.contains("You have finished concentrating on the skill.")) {
            clearIncomingText();
            if (cp >= 200) {
                appendText(">>> Preparing next attack\n");
                sendCommand("rm " + monster.getAlias());
            }
        } else {
            appendText(String.format(">>> In combat with %s\n", monster));
        }
    }

    private void claimTrophy(String text) {
        if (text.contains("You grab hold of the corpse and lift it's neck up a bit.")) {
            clearIncomingText();
            state = State.HUNTING;
            sendCommand("l");
        } else {
            appendText(">>> Claiming trophy\n");
        }
    }

    private void rest(String text) {
        if (text.contains("You lie down, praying for the spirits of nature to guard your sleep.")) {
            clearIncomingText();
            state = State.CAMPING;
        } else if (text.contains("You can't camp so soon again.")) {
            clearIncomingText();
            appendText(">>> Too soon to camp agin, resting instead\n");
        } else if (hpPerc >= SAFE_HP_PERC && cp >= MIN_CP) {
            clearIncomingText();
            appendText(">>> Done resting, resuming the hunt\n");
            state = State.HUNTING;
            sendCommand("l");
        } else {
            appendText(">>> Resting\n");
        }
    }

    private void camp(String text) {
        if (text.contains("You wake from your rest and feel much better.")) {
            appendText(">>> Done camping, resting until fully healed\n");
            clearIncomingText();
            state = State.RESTING;
            rest(text);
        } else {
            appendText(">>> Camping\n");
        }
    }
    
    private void checkHealth(String text) {
        Matcher m = healthBarPattern.matcher(text);
        if (m.find()) {
            try {
                int hp = Integer.parseInt(m.group(1));
                int maxHp = Integer.parseInt(m.group(2));
                hpPerc = (double) hp / (double) maxHp;
                cp = Integer.parseInt(m.group(3));
            } catch (NumberFormatException e) {
                System.err.println("ERROR: " + e);
            }
        }
    }

    private void scanForMonster(String text) {
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
                    // client.echo(">>> Line: '" + line + "'\n");
                    if (line.length() > 0 && line.charAt(0) != '>') {
                        String name = line.trim();
                        if (!name.startsWith("The corpse of ")) {
                            Monster m = currentArea.getMonster(name);
                            if (m != null) {
                                appendText(String.format(">>> Monster found: '%s'\n", m));
                                if (monster == null) {
                                    monster = m;
                                }
                            } else if (currentArea.isItem(name)) {
                                appendText(String.format(">>> Found item '%s'\n", name));
                                hasLoot = true;
                            } else if (name.contains("(flagged by ")) {
                                appendText(">>> Another player is active here, moving on\n");
                                isSafe = false;
                                monster = null;
                                break;
                            } else {
                                // Unknown object, do NOT attack anything here!
                                appendText(String.format(">>> Unknown object found: '%s', moving on\n",  name));
                                isSafe = false;
                                monster = null;
                                break;
                            }
                        }
                    }
                }
                
                if (isSafe && hasLoot) {
                    appendText(">>> Collecting dropped items\n");
                    sendCommand("get all");
                }
            }
        }
    }

    private void createMacros() {
        macros = new HashMap<String, String[]>();
        macros.put("start", new String[]{"brief", "house", "valenthos", "get_eq", "hall", "login",
                "3 n", "14 e", "brief", "bstance lion", "party create", "l"});
        macros.put("end", new String[]{"brief", "w", "house", "valenthos", "store_eq", "brief", "l", "l me"});
        macros.put("get_eq", new String[]{"open valenthos1", "get all from valenthos1", "close valenthos1", "open valenthos2",
                "get all from valenthos2", "close valenthos2", "get pass", "keep all", "wear all", "wield axe", "wield axe 2 in left hand"});
        macros.put("store_eq", new String[]{"unkeep all", "drop all", "remove all", "unkeep all", "open valenthos1", "put all in valenthos1",
                "close valenthos1", "open valenthos2", "put all in valenthos2", "close valenthos2"});
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
        macros.put("unicorns", new String[]{"brief", "6 e", "3 se", "enter opening", "brief", "l"});
        macros.put("_unicorns", new String[]{"brief", "s", "3 nw", "6 w", "brief", "l"});
        macros.put("treants", new String[]{"brief", "5 e", "7 ne", "4 e", "n", "6 ne", "5 e", "enter path", "brief", "l"});
        macros.put("_treants", new String[]{"brief", "out", "5 w", "6 sw", "s", "4 w", "7 sw", "5 w", "brief", "l"});
    }

    private void createAreas() {
        Area area = null;

        // Treetown
        area = new Area("treetown");
        area.toPath = new String[] {"treetown"};
        area.homePath = new String[] {"_treetown"};
        area.roomDescription =
        "This is the southern part of a small treetown.";
        area.directions = new String[] {"e", "w", "n", "w", "e", "ne", "sw", "n", "w", "e", "n", "u",
                                        "d", "n", "e", "e", "w", "s", "u", "d", "w", "s", "s", "s"};
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

        // Oz'kiel Forest.
        area = new Area("orcs");
        area.toPath = new String[]{"orcs"};
        area.homePath = new String[]{"_orcs"};
        area.roomDescription = "A path through the magical forest of Oz'ikel.";
        area.directions = new String[]{"s", "s", "s", "s", "e", "n", "n", "n", "e",
                                       "s", "s", "e", "n", "n", "se", "nw", "3 w", "n"};
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
    }

    private static void sleep() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            // Ignore.
        }
    }

}
