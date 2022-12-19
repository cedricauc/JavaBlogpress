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
}
