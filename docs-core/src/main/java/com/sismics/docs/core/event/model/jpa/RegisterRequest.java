package com.sismics.docs.core.event.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * Register request from a guest user.
 */
@Entity
@Table(name = "T_REGISTER_REQUEST")
public class RegisterRequest {

    @Id
    @Column(name = "RRQ_ID_C", length = 36)
    private String id;

    @Column(name = "RRQ_USERNAME_C", nullable = false, length = 50)
    private String username;

    @Column(name = "RRQ_EMAIL_C", nullable = false, length = 100)
    private String email;

    @Column(name = "RRQ_PASSWORD_C", nullable = false, length = 100)
    private String password;

    @Column(name = "RRQ_STATUS_C", nullable = false, length = 20)
    private String status; // pending, approved, rejected

    @Column(name = "RRQ_CREATEDATE_D", nullable = false)
    private Date createDate;

    public RegisterRequest() {
        this.id = UUID.randomUUID().toString();
        this.status = "pending";
        this.createDate = new Date();
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public RegisterRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RegisterRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public RegisterRequest setStatus(String status) {
        this.status = status;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public RegisterRequest setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }
}
