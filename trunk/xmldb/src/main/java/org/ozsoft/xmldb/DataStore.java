package org.ozsoft.xmldb;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class DataStore {
    
    private final Map<Long, NodeRecord> nodes;
    
    private RandomAccessFile nodeFile;
    
    private RandomAccessFile documentFile;
    
    private boolean isOpen = false;
    
    public DataStore() {
        nodes = new HashMap<Long, NodeRecord>();
    }
    
    public void open() {
        if (!isOpen) {
            try {
                nodeFile = new RandomAccessFile("nodes.dbx", "rw");
                documentFile = new RandomAccessFile("documents.dbx", "rw");
                readNodes();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
    
    public void close() {
        if (isOpen) {
            try {
                writeNodes();
                nodeFile.close();
                documentFile.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
    
    public void storeDocument(Document doc) throws IOException {
        long id = doc.getId();
        NodeRecord nodeRecord = nodes.get(id);
        if (nodeRecord == null) {
            nodeRecord = new NodeRecord(id, NodeRecord.DOCUMENT);
            nodeRecord.setOffset(documentFile.length());
        }
        documentFile.seek(nodeRecord.getOffset());
        documentFile.writeUTF(doc.getName());
        Element rootElement = doc.getRootElement();
        if (rootElement != null) {
            documentFile.writeLong(rootElement.getId());
        } else {
            documentFile.writeLong(-1L);
        }
    }
    
    public void storeElement(Element el) {
    }
    
    public void storeAttribute(Attribute attr) {
    }
    
    public void storeText(Text text) {
    }
    
    public Document retrieveDocument(long id) {
        return null;
    }
    
    private void readNodes() throws IOException {
        nodes.clear();
        nodeFile.seek(0L);
        final long fileLength = nodeFile.length();
        while (nodeFile.getFilePointer() < fileLength) {
            long id = nodeFile.readLong();
            int type = nodeFile.readByte();
            long offset = nodeFile.readLong();
            long length = nodeFile.readLong();
            nodes.put(id, new NodeRecord(id, type, offset, length));
        }
    }
    
    private void writeNodes() throws IOException {
        nodeFile.setLength(0L);
        for (NodeRecord node : nodes.values()) {
            nodeFile.writeLong(node.getId());
            nodeFile.writeByte(node.getType());
            nodeFile.writeLong(node.getOffset());
            nodeFile.writeLong(node.getLength());
        }
    }
    
}
