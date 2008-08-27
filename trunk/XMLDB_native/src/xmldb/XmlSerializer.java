package xmldb;


public class XmlSerializer {
    

	private static final String INDENTATION  = "  ";
    
    private static final char START_TAG  = '<';
    private static final char SLASH      = '/';
    private static final char END_TAG    = '>';
    private static final char SPACE      = ' ';
    private static final char EQUALS     = '=';
    private static final char QUOTES     = '\"';
    private static final char LINE_FEED  = '\n';
    

    public static String serialize(Document document, int level) {
        StringBuilder sb = new StringBuilder();
        for (int child : document.getChildren()) {
            Node node = Database.getInstance().getNode(child);
            Element element = (Element) node;
            sb.append(serialize(element, level));
        }
        return sb.toString();
    }
    

    public static String serialize(Element element, int level) {
        StringBuilder sb = new StringBuilder();
        String name = element.getName();
        for (int i = 0; i < level; i++) {
            sb.append(INDENTATION);
        }
        sb.append(START_TAG);
        sb.append(name);
        Attribute[] attributes = element.getAttributes();
        if (attributes.length > 0) {
            for (Attribute attr : attributes) {
                sb.append(SPACE);
                sb.append(attr.getName());
                sb.append(EQUALS);
                sb.append(QUOTES);
                sb.append(attr.getValue());
                sb.append(QUOTES);
            }
        }
        Integer[] children = element.getChildren();
        if (children.length > 0) {
            sb.append(END_TAG);
            boolean lastIsElement = false;
            for (int i = 0; i < children.length; i++) {
                Node child = Database.getInstance().getNode(children[i]);
                if (child instanceof Element) {
                    sb.append(LINE_FEED);
                    sb.append(serialize((Element) child, level + 1));
                    if (i == (children.length - 1)) {
                        sb.append(LINE_FEED);
                    }
                    lastIsElement = true;
                } else if (child instanceof Text) {
                    sb.append(child.getName());
                    lastIsElement = false;
                } else {
                    System.err.println("*** ERROR: Unknown node type");
                }
            }
            if (lastIsElement) {
                for (int i = 0; i < level; i++) {
                    sb.append(INDENTATION);
                }
            }
            sb.append(START_TAG);
            sb.append(SLASH);
            sb.append(name);
            sb.append(END_TAG);
        } else {
            sb.append(SLASH);
            sb.append(END_TAG);
        }
        return sb.toString();
    }
    
}
