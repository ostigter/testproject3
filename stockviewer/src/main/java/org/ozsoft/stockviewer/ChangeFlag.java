package org.ozsoft.stockviewer;

public enum ChangeFlag {

    UP("+"),

    DOWN("-"),

    UNCHANGED(""),

    ;

    private String marker;

    ChangeFlag(String marker) {
        this.marker = marker;
    }

    public String getMarker() {
        return marker;
    }

}
