package org.ozsoft.taskman.web.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.ozsoft.taskman.domain.Task;

/**
 * Backing bean for editing a task.
 * 
 * @author Oscar Stigter
 */
@ManagedBean
@SessionScoped
public class EditTaskBean implements Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = -6860027446038205685L;

	/** User bean. */
	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;
	
	/** Task being edited. */
	private Task task;
	
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	
	public Task getTask() {
		return task;
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
	
	/**
	 * Action for editing a task.
	 * 
	 * @return The navigate result.
	 */
	public String editTask() {
		userBean.editTask(task);
		return "list.xhtml";
	}

}
