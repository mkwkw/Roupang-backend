package com.teamsupercat.roupangbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "option_detail", schema = "supercat")
public class OptionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "product_idx", nullable = false)
    private Integer productIdx;

    @Column(name = "option_detail_name", nullable = false)
    private String optionDetailName;

    @Column(name = "option_type_idx", nullable = false)
    private Integer optionTypeIdx;

}
