package org.warehouse.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Users {
    private int userID;
    private String loginHash;
    private String password;
    private LocalDateTime creationDate;
    private int nationalityID;
    private boolean isAdmin;
    
    public Users(int userID, String loginHash, LocalDateTime creationDate, int nationalityID, boolean isAdmin) {
        this.userID = userID;
        this.loginHash = loginHash;
        this.creationDate = creationDate;
        this.nationalityID = nationalityID;
        this.isAdmin = isAdmin;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getLoginHash() {
        return loginHash;
    }

    public void setLoginHash(String loginHash) {
        this.loginHash = loginHash;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getNationalityID() {
        return nationalityID;
    }

    public void setNationalityID(int nationalityID) {
        this.nationalityID = nationalityID;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
