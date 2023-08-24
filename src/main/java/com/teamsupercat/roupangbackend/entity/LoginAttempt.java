package com.teamsupercat.roupangbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "loginAttempt", schema = "supercat")
public class LoginAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "domain", nullable = false)
    private String domain;

    @Column(name = "trial", nullable = false)
    private Integer trial;

    @Column(name = "allowed_login_time", nullable = false)
    private Instant allowedLoginTime;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public void init(){
        this.trial = -1;
        this.allowedLoginTime = Instant.now();
    }
}