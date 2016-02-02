package org.ozsoft.portfoliomanager.util;

public class HtmlGrabber {

    private String content;

    public HtmlGrabber(String content) {
        this.content = content;
    }

    public String search(String id) {
        String text = null;
        int p = content.indexOf(id);
        if (p >= 0) {
            content = content.substring(p + 1);
            p = content.indexOf('>');
            if (p >= 0) {
                content = content.substring(p + 1);
                p = content.indexOf('<');
                while (p == 0) {
                    p = content.indexOf('>');
                    if (p >= 0) {
                        content = content.substring(p + 1);
                        p = content.indexOf('<');
                    }
                }
                if (p >= 0) {
                    text = content.substring(0, p).trim();
                    content = content.substring(p);
                }
            }
        }
        return text;
    }
}
