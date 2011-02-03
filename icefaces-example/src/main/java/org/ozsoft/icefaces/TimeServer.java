package org.ozsoft.icefaces;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.application.PushRenderer;

@ManagedBean(name = "timeServer", eager = true)
@ApplicationScoped
public class TimeServer implements Runnable {
    
    private static final long ONE_SECOND = 1000L;
    
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    
    private final Thread thread;
    
    private boolean running = true;
    
    private String time = "";
    
    public TimeServer() {
        thread = new Thread(this);
        PushRenderer.addCurrentSession("TimeServer");
    }
    
    @PostConstruct
    public void postConstruct() {
        thread.start();
    }
    
    @PreDestroy
    public void preDestroy() {
        running = false;
        thread.interrupt();
    }
    
    public void run() {
        while (running) {
            updateTime();
            try {
                Thread.sleep(ONE_SECOND);
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
    }
    
    public String getTime() {
        return time;
    }
    
    private void updateTime() {
        time = TIME_FORMAT.format(new Date());
    }
    
}
