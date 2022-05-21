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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Taste extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taste_id")
    private Long id;

    // OneToOne 관계를 맺었을때 디폴트 설정으로 FetchType 이 EAGER 로 설정되어 있어 자식 Entity 를 조회 했을때 자동으로 부모Entity 를 조회해 옵니다.
    // 이때 바로 부모 Entity 를 사용할 필요가 없다면 속도를 위해 FetchType 을 LAZY 로 설정할 수 있습니다.

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
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