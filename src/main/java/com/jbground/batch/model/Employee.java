package com.jbground.batch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "EMP")
public class Employee {

    @Id
    @Column(name = "EMPNO")
    private int empNo;

    @Column(name = "ENAME")
    private String eName;

    @Column(name = "JOB")
    private String job;

    @Column(name = "MGR")
    private int mgr;

    @Column(name = "HIREDATE")
    private LocalDateTime hireDate;

    @Column(name = "SAL")
    private int sal;

    @Column(name = "COMM")
    private int comm;

    @Column(name = "DEPTNO")
    private int deptNo;

    public int getEmpNo() {
        return empNo;
    }

    public void setEmpNo(int empNo) {
        this.empNo = empNo;
    }

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

    public int getMgr() {
        return mgr;
    }

    public void setMgr(int mgr) {
        this.mgr = mgr;
    }

    public LocalDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
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

    public int getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(int deptNo) {
        this.deptNo = deptNo;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Employee{");
        sb.append("empNo=").append(empNo);
        sb.append(", eName='").append(eName).append('\'');
        sb.append(", job='").append(job).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
