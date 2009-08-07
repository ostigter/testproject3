package org.ozsoft.texasholdem.actions;

public class RaiseAction implements Action {
    
    private int amount;
    
    public RaiseAction(int amount) {
        this.amount = amount;
    }
    
    public int getAmount() {
        return amount;
    }
    
    @Override
    public String getVerb() {
        return "raises with $ " + amount;
    }
    
    @Override
    public String toString() {
        return "Raise";
    }
    
}
