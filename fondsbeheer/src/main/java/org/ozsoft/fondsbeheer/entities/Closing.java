package org.ozsoft.fondsbeheer.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "findClosings", query = "SELECT c FROM Closing c"),
    @NamedQuery(name = "findClosingsByFund", query = "SELECT c FROM Closing c WHERE c.fundId = :fundId")
})
public class Closing {
    
    @Id
    private long id;
    
    private String fundId;
    
    private Date date;
    
    private double price;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getFundId() {
        return fundId;
    }
    
    public void setFundId(String fundId) {
        this.fundId = fundId;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

}
