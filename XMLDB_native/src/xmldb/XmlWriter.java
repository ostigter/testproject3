package xmldb;


import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class XmlWriter {
    

	private static final byte TYPE_ELEMENT    = 0x00;
    private static final byte TYPE_ATTRIBUTE  = 0x01;
    private static final byte TYPE_TEXT       = 0x02;
    
    private DataOutputStream os;
    

    public void write(Document doc) throws XmldbException {
        try {
            String fileName = String.format("%08d.dbx", doc.getId());
            File file = new File("db/" + fileName);
            os = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(file)));
            for (int id : doc.getChildren()) {
                Node node = Database.getInstance().getNode(id);
                writeNode(node);
            }
            os.close();
        } catch (IOException e) {
            String msg = "Could not store document: " + e.getMessage();
            throw new XmldbException(msg);
        }
    }
    

    private void writeNode(Node node) throws IOException {
        if (node instanceof Element) {
            writeElement((Element) node);
        } else if (node instanceof Text) {
            os.writeByte(TYPE_TEXT);
            os.writeUTF(node.getName());
        } else {
            System.err.println("*** ERROR: Unknown node type!");
        }
    }
    
    
    private void writeElement(Element element) throws IOException {
        os.writeByte(TYPE_ELEMENT);
        os.writeUTF(element.getName());
        os.writeByte(element.getNoOfAttributes());
        for (Attribute attribute : element.getAttributes()) {
            os.writeByte(TYPE_ATTRIBUTE);
            os.writeUTF(attribute.getName());
            os.writeUTF(attribute.getValue());
        }
        os.writeShort(element.getNoOfChildren());
        for (int id : element.getChildren()) {
            Node child = Database.getInstance().getNode(id);
            writeNode(child);
        }
    }
    
}
