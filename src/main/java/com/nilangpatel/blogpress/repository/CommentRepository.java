package com.nilangpatel.blogpress.repository;

import com.nilangpatel.blogpress.model.Blog;
import com.nilangpatel.blogpress.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(
            value = "SELECT * FROM comment WHERE blog_id = :blogId",
            nativeQuery=true)
    List<Comment> findAllByBlogId(@Param("blogId") Long blogId);

    @Query(
            value = "SELECT * FROM comment LIMIT :size OFFSET :from",
            nativeQuery=true)
    List<Comment> getComments(@Param("from") int from, @Param("size") int size);

    @Query(
            value = "SELECT * FROM comment LEFT JOIN blog ON comment.blog_id = blog.id WHERE status = :status LIMIT :size OFFSET :from ",
            nativeQuery=true)
    List<Comment> getCommentsForStatus(@Param("status") String status, @Param("from") int from, @Param("size") int size);

}
