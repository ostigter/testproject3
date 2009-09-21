package org.ozsoft.mediacenter;

public class MediaFile extends MediaItem {
	
//	private boolean isSeen = false;
	
	public MediaFile(String name, String path, MediaFolder parent) {
		super(name, path, parent);
	}
	
//	public boolean isSeen() {
//		return isSeen;
//	}
//	
//	public void setSeen(boolean isSeen) {
//		this.isSeen = isSeen;
//	}

	@Override
	public String toString() {
		return name;
	}
	
}
