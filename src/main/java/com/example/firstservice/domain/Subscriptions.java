package com.example.firstservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE subscriptions SET active = false WHERE id=?")
@FilterDef(name = "deletedUserFilter", parameters = @ParamDef(name = "isActive", type = Boolean.class))
@Filter(name = "deletedUserFilter", condition = "active = :isActive")
public class Subscriptions {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subs_generator")
    @SequenceGenerator(name="subs_generator", sequenceName = "subs_seq", allocationSize=1)
    Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follower_id")
    private User follower;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "followed_id")
    private User followed;
    private boolean active = Boolean.TRUE;
}
