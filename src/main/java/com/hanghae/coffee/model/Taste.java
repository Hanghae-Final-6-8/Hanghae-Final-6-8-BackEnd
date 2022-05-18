package com.hanghae.coffee.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Taste extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taste_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bean_id")
    private Beans beans;

    public static Taste createTaste(Users users, Beans beans) {
        Taste taste = new Taste();
        taste.setUsers(users);
        taste.setBeans(beans);
        return taste;
    }

    public Taste updateTaste(Taste taste, Users users, Beans beans) {
        this.setUsers(users);
        this.setBeans(beans);
        return this;
    }


}