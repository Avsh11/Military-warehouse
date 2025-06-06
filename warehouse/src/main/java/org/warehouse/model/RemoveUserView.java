package org.warehouse.model;

import java.time.LocalDateTime;

public class RemoveUserView {
    private int userID;
    private String loginHash;
    private LocalDateTime creationDate;
    private String country;

    public RemoveUserView(int userID, String loginHash, LocalDateTime creationDate, String country) {
        this.userID = userID;
        this.loginHash = loginHash;
        this.creationDate = creationDate;
        this.country = country;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
