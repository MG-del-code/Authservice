package com.authcentral.authservice.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "user_application_roles",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "application_id"})
    }
)
public class UserApplicationRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private ClientApplication application;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* =======================
       Constructeurs
       ======================= */

    protected UserApplicationRole() {}

    public UserApplicationRole(User user, ClientApplication application, Role role) {
        this.user = user;
        this.application = application;
        this.role = role;
    }

    /* =======================
       Hooks JPA
       ======================= */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /* =======================
       Getters
       ======================= */

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
    
    public ClientApplication getApplication() {
        return application;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setApplication(ClientApplication application) {
        this.application = application;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
