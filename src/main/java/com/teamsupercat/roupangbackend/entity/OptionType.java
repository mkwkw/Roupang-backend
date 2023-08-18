package com.teamsupercat.roupangbackend.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "option_type", schema = "supercat")
public class OptionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "product_idx", nullable = false)
    private Integer productIdx;

    @Column(name = "option_type_name", nullable = false)
    private String optionTypeName;

    @Column(name = "option_detail_idx")
    private String optionDetailIdx;

}
