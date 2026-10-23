package com.employee.employee_management.Persitance.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "employee_db")
public class User {
    public User(){}
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column (name = "password")
    private String password;

    @Column (name = "status")
    private String status;

    @Column (name = "created_date")
    private Date dtmcrt;

    @Column (name = "create_user")
    private Date usercrt;

    @Column (name = "approved_date")
    private Date dtmapp;

    @Column (name = "approved_user")
    private String userapp;

    @Column (name = "update_date")
    private Date dtmupd;

    @Column (name = "update_user")
    private String usrupd;

    public User(long id, String username, String email, String password, String status, Date dtmcrt, Date usercrt,
            Date dtmapp, String userapp, Date dtmupd, String usrupd) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        this.dtmcrt = dtmcrt;
        this.usercrt = usercrt;
        this.dtmapp = dtmapp;
        this.userapp = userapp;
        this.dtmupd = dtmupd;
        this.usrupd = usrupd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDtmcrt() {
        return dtmcrt;
    }

    public void setDtmcrt(Date dtmcrt) {
        this.dtmcrt = dtmcrt;
    }

    public Date getUsercrt() {
        return usercrt;
    }

    public void setUsercrt(Date usercrt) {
        this.usercrt = usercrt;
    }

    public Date getDtmapp() {
        return dtmapp;
    }

    public void setDtmapp(Date dtmapp) {
        this.dtmapp = dtmapp;
    }

    public String getUserapp() {
        return userapp;
    }

    public void setUserapp(String userapp) {
        this.userapp = userapp;
    }

    public Date getDtmupd() {
        return dtmupd;
    }

    public void setDtmupd(Date dtmupd) {
        this.dtmupd = dtmupd;
    }

    public String getUsrupd() {
        return usrupd;
    }

    public void setUsrupd(String usrupd) {
        this.usrupd = usrupd;
    }
    
    
}
