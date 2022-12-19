package com.nilangpatel.blogpress.repository;

import java.util.List;
import java.util.Set;

import com.nilangpatel.blogpress.model.Blog;
import com.nilangpatel.blogpress.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>{
    @Query(
            value = "SELECT count(*) FROM blog",
            nativeQuery=true)
    int getCurrentChildSequence(Long blogId, Long parentCommentId);

    @Query(
            value = "SELECT * FROM blog WHERE title LIKE %:searchTxt% OR body LIKE %:searchTxt%",
            nativeQuery=true)
    List<Blog> search(@Param("searchTxt") String searchTxt);

    @Query(
            value = "SELECT * FROM comment LIMIT :size OFFSET :from",
            nativeQuery=true)
    Set<Comment> getComments(@Param("from") int from, @Param("size") int size);

    @Query(
            value = "SELECT * FROM comment WHERE status = :status LIMIT :size OFFSET :from ",
            nativeQuery=true)
    Set<Comment> getCommentsForStatus(@Param("status") String status, @Param("from") int from, @Param("size") int size);

    @Query(
            value = "SELECT * FROM blog AS b",
            nativeQuery=true)
    Iterable<Blog> findAllWithComments();
}
