package org.ozsoft.projectbase.web;

import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class DateBean {
	
	private final TimeZone timeZone;
	
	public DateBean() {
		timeZone = new GregorianCalendar().getTimeZone();
	}
	
	public TimeZone getTimeZone() {
		return timeZone;
	}

}
