package com.jbground.batch.model;

import java.util.UUID;

public class Account {

    private final String id = UUID.randomUUID().toString();
    private String eName;
    private long balance;

    private long before;
    private long after;
    private double tax;

    private String charge;

    public String getId() {
        return id;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getBefore() {
        return before;
    }

    public void setBefore(long before) {
        this.before = before;
    }

    public long getAfter() {
        return after;
    }

    public void setAfter(long after) {
        this.after = after;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Account{");
        sb.append("id='").append(id).append('\'');
        sb.append(", before=").append(before);
        sb.append(", after=").append(after);
        sb.append(", tax=").append(tax);
        sb.append('}');
        return sb.toString();
    }
}
