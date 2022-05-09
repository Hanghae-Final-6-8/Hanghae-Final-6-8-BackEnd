package com.hanghae.coffee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String authId;

    private String email;

    private String nickname;

    private String profileUrl;

    private String name;

    @Column(nullable = false)
    private String requestToken;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OauthType oauthType;

    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<Posts> posts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<Comments> comments = new ArrayList<>();

    //==생성 메서드==//
    public static Users createUser(String nickname,String email,String authId, OauthType oauth,String requestToken) {
        Users users = new Users();
        users.setAuthId(authId);
        users.setNickname(nickname);
        users.setEmail(email);
        users.setOauthType(oauth);
        users.setRequestToken(requestToken);
        return users;
    }

}
