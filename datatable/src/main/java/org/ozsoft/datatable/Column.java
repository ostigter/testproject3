package org.ozsoft.datatable;

public class Column {

    private final String name;

    private final String tooltip;

    private final ColumnRenderer renderer;

    public Column(String name, String tooltip) {
        this(name, tooltip, null);
    }

    public Column(String name, String tooltip, ColumnRenderer renderer) {
        this.name = name;
        this.tooltip = tooltip;
        this.renderer = renderer;
    }

    public String getName() {
        return name;
    }

    public String getTooltip() {
        return tooltip;
    }

    public ColumnRenderer getRenderer() {
        return renderer;
    }

    public Column getFooterColumn() {
        Column footerColumn = new Column(name, tooltip, renderer);
        return footerColumn;
    }
}
