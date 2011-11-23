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
        if (selectedPanel != null) {
            selectedPanel.setLocation(getX(), getY() + 50);
            selectedPanel.doLayout(g);
            setSize(selectedPanel.getWidth(), selectedPanel.getHeight() + 50);
        }
        setValid(true);
    }

    @Override
    public void paint(Graphics2D g) {
        if (!isValid()) {
            doLayout(g);
        }

        g.setColor(Color.RED);
        g.drawRect(0, 0, getWidth(), 49);

        if (selectedPanel != null) {
            selectedPanel.paint(g);
        }
    }

}
