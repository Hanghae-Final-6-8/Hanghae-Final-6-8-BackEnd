package com.hanghae.coffee.repository.posts;

import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.model.PostsInterfaceJoinVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface PostsRepository extends JpaRepository<Posts,Long> {


    List<Posts> findAllByOrderByModifiedAtDesc();
    
    // 내가 쓴 게시물 전체 반환
    @Query("SELECT p.id as posts_id, "
        + "p.title as title, "
        + "p.content as content,"
        + "p.createdAt as created_at, "
        + "p.modifiedAt as modified_at, "
        + "p.users.nickname as nickname, "
        + "pi.imageUrl as posts_image, "
        + "pt.tags.tagName as tag_name "
        + "FROM Posts p "
        + "JOIN fetch PostsImage pi ON p.id = pi.posts.id "
        + "JOIN fetch PostsTags pt ON p.id = pt.posts.id "
        + "JOIN fetch Tags t ON pt.tags.id = t.id "
        + "where p.users.id = :id")
    List<PostsInterfaceJoinVO> findAllByUsers_Id(Long id);

    // 전체 게시물 반환
    @Query("SELECT p.id as posts_id, "
        + "p.title as title, "
        + "p.content as content, "
        + "p.createdAt as created_at,"
        + "p.modifiedAt as modified_at,"
        + "p.users.nickname as nickname, "
        + "pi.imageUrl as posts_image, "
        + "pt.tags.tagName as tag_name "
        + "FROM Posts p "
        + "JOIN fetch PostsImage pi ON p.id = pi.posts.id "
        + "JOIN fetch PostsTags pt ON p.id = pt.posts.id "
        + "JOIN fetch Tags t ON pt.tags.id = t.id")
    List<PostsInterfaceJoinVO> findAllWithPostImages();

    // 세부 게시물 내용 반환
    @Query("SELECT p.id as posts_id, "
        + "p.title as title, "
        + "p.content as content, "
        + "p.createdAt as created_at, "
        + "p.modifiedAt as modified_at, "
        + "p.users.nickname as nickname, "
        + "pi.imageUrl as posts_image, "
        + "pt.tags.tagName as tag_name "
        + "FROM Posts p "
        + "JOIN fetch PostsImage pi ON p.id = pi.posts.id "
        + "JOIN fetch PostsTags pt ON p.id = pt.posts.id "
        + "JOIN fetch Tags t ON pt.tags.id = t.id "
        + "where p.id = :id")
    PostsInterfaceJoinVO findPostsByIdWithPostImages(Long id);





}
