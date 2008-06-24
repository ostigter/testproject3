package xen.filestore;


/**
 * Administrative entry of a single, stored file.
 * 
 * Each entry has a name, offset and length.
 * 
 * @author Oscar Stigter
 */
public class FileEntry implements Comparable<FileEntry> {
	

	private String name;
	
	private long offset;
	
	private long length;
	

	public FileEntry(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public long getOffset() {
		return offset;
	}


	public void setOffset(long offset) {
		this.offset = offset;
	}


	public long getLength() {
		return length;
	}


	public void setLength(long length) {
		this.length = length;
	}


	@Override
	public String toString() {
		return "{'" + name + "', " + offset + ", " + length + "}";
	}


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileEntry) {
			FileEntry pos = (FileEntry) obj;
			return pos.offset == offset;
		} else {
			return false;
		}
	}


	public int compareTo(FileEntry pos) {
		long otherOffset = pos.getOffset();
		if (offset > otherOffset) {
			return 1;
		} else if (offset < otherOffset) {
			return -1;
		} else {
			return 0;
		}
	}


}
