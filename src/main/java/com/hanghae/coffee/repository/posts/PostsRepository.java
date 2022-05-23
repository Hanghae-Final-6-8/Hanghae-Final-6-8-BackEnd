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
        + "(SELECT count(l.posts.id) FROM Likes l where l.posts.id = p.id group by l.posts.id) as likes_count, "
        + "(SELECT l.users.id FROM Likes l where p.id = l.posts.id and l.users.id = :id) as isLikes, "
        + "(select function('group_concat',t.tagName) FROM PostsTags pt inner join Tags t on t.id = pt.tags.id where p.id = pt.posts.id group by p.id) as tag_name "
        + "FROM Posts p "
        + "Left JOIN fetch PostsImage pi ON p.id = pi.posts.id "
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
        + "(SELECT count(l.posts.id) FROM Likes l where l.posts.id = p.id group by l.posts.id) as likes_count, "
        + "(SELECT l.users.id FROM Likes l where p.id = l.posts.id and l.users.id = :id) as isLikes, "
        + "(select function('group_concat',t.tagName) FROM PostsTags pt inner join Tags t on t.id = pt.tags.id where p.id = pt.posts.id group by p.id) as tag_name "
        + "FROM Posts p "
        + "Left JOIN fetch PostsImage pi ON p.id = pi.posts.id "
        + "order by p.modifiedAt "
        + "desc" )

    Slice<PostsInterfaceJoinVO> findAllWithPostImages(Long id, Pageable pageable);




    // 세부 게시물 내용 반환
    @Query("SELECT p.id as posts_id, "
        + "p.title as title, "
        + "p.content as content, "
        + "p.createdAt as created_at, "
        + "p.modifiedAt as modified_at, "
        + "p.users.nickname as nickname, "
        + "pi.imageUrl as posts_image, "
        + "(SELECT count(l.posts.id) FROM Likes l where l.posts.id = :id group by l.posts.id) as likes_count, "
        + "(SELECT l.users.id FROM Likes l where p.id = l.posts.id and l.users.id = :user_id) as isLikes, "
        + "(select function('group_concat',t.tagName) FROM PostsTags pt inner join Tags t on t.id = pt.tags.id where p.id = pt.posts.id group by p.id) as tag_name "
        + "FROM Posts p "
        + "Left JOIN fetch PostsImage pi ON p.id = pi.posts.id "
        + "where p.id = :id")
    PostsInterfaceJoinVO findPostsByIdWithPostImages(Long id, Long user_id);





}
