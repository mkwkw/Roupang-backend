package com.teamsupercat.roupangbackend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

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

    @Column(name = "option_type_name_idx", nullable = false)
    private Integer optionTypeNameIdx;

    @Column(name = "option_detail_idx")
    private String optionDetailIdx;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    //TODO. 필요 없는데 왜 생기는지 모르겠음. - 나중에 ddl-auto를 none으로 바꿀까싶기도
    @Builder.Default
    @Column(name = "option_type_name")
    private String optionTypeName = "null";

    public OptionType(Integer productIdx, Integer optionTypeNameIdx, String optionDetailIdx){
        this.productIdx = productIdx;
        this.optionTypeNameIdx = optionTypeNameIdx;
        this.optionDetailIdx = optionDetailIdx;
        this.optionTypeName = "null";
    }


}
