package org.ozsoft.fondsbeheer.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "getClosings", query = "SELECT c FROM Closing c WHERE c.fundId = :fundId")
})
public class Closing {
    
    @Id
    @GeneratedValue
    private long id;
    
    @Column(nullable = false)
    private String fundId;
    
    @Column(nullable = false)
    private Date date;
    
    @Column(nullable = false)
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

    @Override
    public String toString() {
        return String.format("{'%s', %s, %.2f}", fundId, date, price); 
    }
    
}
