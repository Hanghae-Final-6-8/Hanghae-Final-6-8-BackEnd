package com.hanghae.coffee.repository.posts;

import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.dto.posts.PostsInterfaceJoinVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
        + "pt.tags.tagName as tag_name, "
        + "(SELECT count(p.id) FROM Likes l where l.posts.id = p.id group by p.id) as likes_count "
        + "FROM Posts p "
        + "JOIN fetch PostsImage pi ON p.id = pi.posts.id "
        + "JOIN fetch PostsTags pt ON p.id = pt.posts.id "
        + "JOIN fetch Tags t ON pt.tags.id = t.id "
        + "where p.users.id = :id")
    Slice<PostsInterfaceJoinVO> findAllByUsers_Id(Long id, Pageable pageable);

    // 전체 게시물 반환
    @Query("SELECT p.id as posts_id, "
        + "p.title as title, "
        + "p.content as content, "
        + "p.createdAt as created_at,"
        + "p.modifiedAt as modified_at,"
        + "p.users.nickname as nickname, "
        + "pi.imageUrl as posts_image, "
        + "pt.tags.tagName as tag_name, "
        + "(SELECT count(p.id) FROM Likes l where l.posts.id = p.id group by p.id) as likes_count "
        + "FROM Posts p "
        + "JOIN fetch PostsImage pi ON p.id = pi.posts.id "
        + "JOIN fetch PostsTags pt ON p.id = pt.posts.id "
        + "JOIN fetch Tags t ON pt.tags.id = t.id" )
    Slice<PostsInterfaceJoinVO> findAllWithPostImages();

    // 전체 게시물 반환
    @Query("SELECT "
        + "p.id as posts_id, "
        + "p.title as title, "
        + "p.content as content, "
        + "p.createdAt as created_at,"
        + "p.modifiedAt as modified_at,"
        + "p.users.nickname as nickname, "
        + "pi.imageUrl as posts_image, "
        + "pt.tags.tagName as tag_name, "
        + "(SELECT count(p.id) FROM Likes l where l.posts.id = p.id group by p.id) as likes_count "
        + "FROM Posts p "
        + "Left JOIN fetch PostsImage pi ON p.id = pi.posts.id "
        + "Left JOIN fetch PostsTags pt ON p.id = pt.posts.id "
        + "Left JOIN fetch Tags t ON pt.tags.id = t.id "
        + "order by p.createdAt "
        + "asc" )
    Slice<PostsInterfaceJoinVO> findAllWithPostImagesPageing(Pageable pageable);



    // 세부 게시물 내용 반환
    @Query("SELECT p.id as posts_id, "
        + "p.title as title, "
        + "p.content as content, "
        + "p.createdAt as created_at, "
        + "p.modifiedAt as modified_at, "
        + "p.users.nickname as nickname, "
        + "pi.imageUrl as posts_image, "
        + "pt.tags.tagName as tag_name, "
        + "(SELECT count(p.id) FROM Likes l where l.posts.id = p.id group by p.id) as likes_count "
        + "FROM Posts p "
        + "JOIN fetch PostsImage pi ON p.id = pi.posts.id "
        + "JOIN fetch PostsTags pt ON p.id = pt.posts.id "
        + "JOIN fetch Tags t ON pt.tags.id = t.id "
        + "where p.id = :id")
    PostsInterfaceJoinVO findPostsByIdWithPostImages(Long id);



}
