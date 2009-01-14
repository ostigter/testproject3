package hellojsf;

import java.util.Locale;

import javax.faces.context.FacesContext;

/**
 * Changes the current locale.
 * 
 * @author Oscar Stigter
 */
public class LocaleChanger {

	public String englishAction() {
		setLocale(Locale.ENGLISH);
		return null;
	}

	public String germanAction() {
		setLocale(Locale.GERMAN);
		return null;
	}
	
	public String dutchAction() {
		setLocale(new Locale("nl"));
		return null;
	}
	
	private void setLocale(Locale locale) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getViewRoot().setLocale(locale);
	}
	

}
