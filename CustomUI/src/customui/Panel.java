package customui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Panel extends AbstractComponent {

    private static final AbstractComponent[] EMPTY_ARRAY = new AbstractComponent[0];

    private List<AbstractComponent> children;

    public Panel() {
        children = new ArrayList<AbstractComponent>();
    }

    public void addComponent(AbstractComponent component) {
        children.add(component);
        component.setParent(this);
    }

    public AbstractComponent[] getComponents() {
        return children.toArray(EMPTY_ARRAY);
    }

    public void doLayout() {
        int width = getWidth();
        int height = getHeight();

        for (AbstractComponent c : children) {
            c.doLayout();
        }

        if (width == 0) {
            for (Component c : children) {
                width += c.getWidth();
            }

        }
        if (height == 0) {
            for (Component c : children) {
                height += c.getHeight();
            }

        }

        // int dx = 0;
        int dy = 0;
        for (AbstractComponent c : children) {
            c.setY(dy);
            dy += c.getHeight();
        }

    }

    public void paintComponent(Graphics g) {
        for (AbstractComponent c : children) {
            c.paintComponent(g);
        }
    }

}
