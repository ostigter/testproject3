package org.ozsoft.taskman.web.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.ozsoft.taskman.domain.Status;
import org.ozsoft.taskman.domain.Task;
import org.ozsoft.taskman.domain.User;

/**
 * Backing bean handling task management.
 * 
 * @author Oscar Stigter
 */
@ManagedBean
@SessionScoped
public class TaskBean implements Serializable {
    
    private static final long serialVersionUID = -6860027446038205685L;
    
    @ManagedProperty(value = "#{userBean}")
    private UserBean userBean;
    
    private String summary;
    
    public String getSummary() {
	return summary;
    }
    
    public void setSummary(String summary) {
	this.summary = summary;
    }
    
    public String doCreateTask() {
	Task task = new Task();
	task.setSummary(summary);
	task.setStatus(Status.OPEN);
	
	User user = userBean.getUser();
	if (user != null) {
	    user.addTask(task);
	}
	
        return "list.xhtml";
    }
    
}
