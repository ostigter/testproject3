package customui;

public abstract class AbstractComponent implements Component {

    private AbstractComponent parent;

    private int x = 0;

    private int y = 0;

    private int width = -1;

    private int height = -1;

    private boolean isValid = false;

    public AbstractComponent getParent() {
        return parent;
    }

    /* package */void setParent(AbstractComponent parent) {
        this.parent = parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /* package */void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /* package */void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /* package */boolean isValid() {
        return isValid;
    }

    /* package */void setValid(boolean isValid) {
        this.isValid = isValid;
    }

}
