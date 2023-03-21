package com.jbground.batch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BONUS")
public class Bonus {

    @Id
    @Column(name = "ENAME")
    private String eName;

    @Column(name = "JOB")
    private String job;

    @Column(name = "SAL")
    private int sal;

    @Column(name = "COMM")
    private int comm;

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getSal() {
        return sal;
    }

    public void setSal(int sal) {
        this.sal = sal;
    }

    public int getComm() {
        return comm;
    }

    public void setComm(int comm) {
        this.comm = comm;
    }
}
