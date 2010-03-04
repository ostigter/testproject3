package xquery;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

/**
 * File filter for XQuery modules.
 * 
 * Uses a set of commonly used file extentions for XQuery modules.
 * 
 * @author Oscar Stigter
 */
public class XQueryModuleFileFilter implements FileFilter {

	/** Supported XQuery module file extentions. */
	private final Set<String> supportedExtension;

	/**
	 * Constructor.
	 * 
	 * Populates the list of known XQuery module file extentions.
	 */
	public XQueryModuleFileFilter() {
		supportedExtension = new HashSet<String>();
		supportedExtension.add(".xqy");
		supportedExtension.add(".xquery");
	}

	/**
	 * Returns true if the specified file is recognized as an XQuery module,
	 * otherwise false.
	 * 
	 * @param file
	 *            The file.
	 * 
	 * @return True if an XQuery mode, otherwise false.
	 */
	public boolean accept(File file) {
		boolean accept = false;
		if (file.isDirectory()) {
			accept = true;
		} else {
			int pos = file.getName().lastIndexOf(".");
			if (pos != -1) {
				String ext = file.getName().substring(pos).toLowerCase();
				accept = supportedExtension.contains(ext);
			}
		}
		return accept;
	}

}
