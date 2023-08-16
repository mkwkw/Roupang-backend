package com.teamsupercat.roupangbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "Refresh_token", schema = "supercat")
public class RefreshToken {
    @Id
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "member_idx", nullable = false)
    private Integer memberIdx;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}