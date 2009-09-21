package org.ozsoft.mediacenter;

import java.util.ArrayList;
import java.util.List;

public class MediaFolder extends MediaItem {
	
	private final List<MediaFolder> folders;
	private final List<MediaFile> files;
	
	public MediaFolder(String name, String path, MediaFolder parent) {
		super(name, path, parent);
		folders = new ArrayList<MediaFolder>();
		files = new ArrayList<MediaFile>();
	}
	
	public boolean isEmpty() {
	    return (folders.size() == 0 && files.size() == 0);
	}
	
	public List<MediaFolder> getFolders() {
		return folders;
	}
	
	public List<MediaFile> getFiles() {
		return files;
	}
	
	public void addFolder(MediaFolder folder) {
		folders.add(folder);
	}
	
	public void addFile(MediaFile file) {
		files.add(file);
	}
	
	public void deleteFile(MediaFile file) {
	    if (!files.remove(file)) {
    	    for (MediaFolder folder : folders) {
    	        folder.deleteFile(file);
    	    }
	    }
	}
	
	public void clear() {
		folders.clear();
		files.clear();
	}
	
	@Override
	public String toString() {
		return String.format("%s/", name);
	}
	
//	public void print() {
//		print(0);
//	}
//	
//	private void print(int indent) {
//		for (int i = 0; i < indent; i++) {
//			System.out.print(' ');
//		}
//		System.out.format("%s/\n", name);
//		for (MediaFolder folder : folders) {
//			folder.print(indent + 2);
//		}
//		for (MediaFile file : files) {
//			for (int i = 0; i < (indent + 2); i++) {
//				System.out.print(' ');
//			}
//			System.out.println(file.getName());
//		}
//	}
	
}
