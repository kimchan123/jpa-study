package com.example.jpaselfstudy.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "team_id")
    private Long teamId;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<OrderItem> orders = new ArrayList<>();

    protected Member() {
    }

    public Member(final Long id, final String name, final Long teamId, final List<OrderItem> orders) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
        this.orders = orders;
    }

    public Member(final String name) {
        this.name = name;
    }
}
