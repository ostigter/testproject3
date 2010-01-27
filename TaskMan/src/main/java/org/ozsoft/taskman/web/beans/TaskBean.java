package org.ozsoft.taskman.web.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;
import org.ozsoft.taskman.domain.Status;
import org.ozsoft.taskman.domain.Task;

/**
 * Backing bean handling task management.
 * 
 * @author Oscar Stigter
 */
@ManagedBean
@SessionScoped
public class TaskBean implements Serializable {

	/** Log. */
	private static final Logger LOG = Logger.getLogger(TaskBean.class);
	
	/** Serial version UID. */
	private static final long serialVersionUID = -6860027446038205685L;

	/** User bean. */
	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;

	/** Task summary. */
	private String summary;

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * Action for creating a task.
	 * 
	 * @return The navigate result.
	 */
	public String doCreateTask() {
		Task task = new Task();
		task.setSummary(summary);
		task.setStatus(Status.OPEN);
		userBean.createTask(task);
		LOG.debug(String.format("Created task '%s' for user '%s'", summary, userBean.getUsername()));
		return "list.xhtml";
	}

}
