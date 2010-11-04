package org.ozsoft.taskman.web.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.apache.log4j.Logger;
import org.ozsoft.taskman.domain.Status;
import org.ozsoft.taskman.domain.Task;

/**
 * Backing bean for creating a new task.
 * 
 * @author Oscar Stigter
 */
@ManagedBean
@RequestScoped
public class CreateTaskBean implements Serializable {

    /** Log. */
    private static final Logger LOG = Logger.getLogger(CreateTaskBean.class);

    /** Serial version UID. */
    private static final long serialVersionUID = -6860027446038205685L;

    /** User bean. */
    @ManagedProperty(value = "#{userBean}")
    private UserBean userBean;

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
    public String createTask() {
        Task task = new Task();
        task.setSummary(summary);
        task.setStatus(Status.OPEN);
        userBean.createTask(task);
        LOG.debug(String.format("Created task '%s' for user '%s'", summary, userBean.getUsername()));
        return "list.xhtml";
    }

}
