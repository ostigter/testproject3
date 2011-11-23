package org.ozsoft.customui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class TabPane extends AbstractComponent {

    private static final long serialVersionUID = 8104646596178421453L;

    private final List<String> titles;

    private final List<Panel> panels;

    private int selectedIndex = -1;

    private Panel selectedPanel;

    public TabPane() {
        titles = new ArrayList<String>();
        panels = new ArrayList<Panel>();
    }

    public void addTab(String title, Panel panel) {
        titles.add(title);
        panels.add(panel);
        panel.setParent(this);
        if (selectedIndex == -1) {
            setSelectedIndex(0);
        }
        setValid(false);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        selectedPanel = panels.get(selectedIndex);
        setValid(false);
    }

    @Override
    public void doLayout(Graphics2D g) {
        if (!isValid()) {
            String title = (selectedIndex != -1) ? titles.get(selectedIndex) : "(No title)";
            Dimension titleDimension = Utils.getTextDimension(title, getFont(), g);
            if (selectedPanel != null) {
                selectedPanel.doLayout(g);
                selectedPanel.setLocation(getX(), getY() + titleDimension.getHeight());
                int width = Math.max(titleDimension.getWidth(), selectedPanel.getWidth());
                int height = titleDimension.getHeight() + selectedPanel.getHeight();
                setSize(width, height);
            }
            setValid(true);
        }
    }

    @Override
    public void paint(Graphics2D g) {
        doLayout(g);
        
        String title = (selectedIndex != -1) ? titles.get(selectedIndex) : "(No title)";
        Dimension titleDimension = Utils.getTextDimension(title, getFont(), g);
        g.setColor(Color.RED);
        g.drawRect(getX(), getY(), getX() + titleDimension.getWidth(), getY() + titleDimension.getHeight());
        g.setColor(Color.BLACK);
        g.drawString(title, getX(), getY());
        
        if (selectedPanel != null) {
            selectedPanel.paint(g);
        }
    }

}
