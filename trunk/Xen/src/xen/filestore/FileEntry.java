package xen.filestore;


/**
 * Administrative entry of a single, stored file.
 * 
 * Each entry has a name, offset and length.
 * 
 * @author Oscar Stigter
 */
public class FileEntry implements Comparable<FileEntry> {
	

	private int id;
	
	private int offset;
	
	private int length;
	

	public FileEntry(int id) {
		this.id = id;
	}


	public int getId() {
		return id;
	}


	public int getOffset() {
		return offset;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}


	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}


	@Override
	public String toString() {
		return "{'" + id + "', " + offset + ", " + length + "}";
	}


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileEntry) {
			FileEntry entry = (FileEntry) obj;
			return entry.getId() == id;
		} else {
			return false;
		}
	}


	public int compareTo(FileEntry entry) {
		int otherOffset = entry.getOffset();
		if (offset > otherOffset) {
			return 1;
		} else if (offset < otherOffset) {
			return -1;
		} else {
			return 0;
		}
	}


}
