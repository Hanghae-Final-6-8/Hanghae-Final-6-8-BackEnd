package com.hanghae.coffee.model;


import static javax.persistence.FetchType.LAZY;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comments extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comments_id")
    private Long id;

    @Column(nullable = false)
    private String content; // text로 자료형 변경

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    public Comments(String content,Posts posts,Users users){
        this.content = content;
        this.posts = posts;
        this.users = users;
    }

}
