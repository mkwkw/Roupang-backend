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
@Table(name = "option_type_name", schema = "supercat")
public class OptionTypeName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Integer id;

    @Column(name = "option_name")
    private String optionName;

    public OptionTypeName(String optionName){
        this.optionName = optionName;
    }

}
