package com.hanghae.coffee.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Posts extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posts_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content; // text로 자료형 변경

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @JsonIgnore
    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostsImage> postsImages = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostsTags> postsTags = new ArrayList<>();

    @Builder
    public Posts(String title, String content, Users users){
        this.title = title;
        this.content = content;
        this.users = users;
    }

    public Posts(Long post_id){
        this.id = post_id;
    }

 
    public void update(String title, String content, Users users) {
        this.title = title;
        this.content = content;
        this.users = users;
    }

    //==생성 메서드==//
    public static Posts createPosts(Posts posts,List<PostsImage> postsImages,List<PostsTags> postsTags) {
        posts.setPostsImages(postsImages);
        posts.setPostsTags(postsTags);

        return posts;
    }

}
