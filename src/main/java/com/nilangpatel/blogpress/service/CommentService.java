package com.nilangpatel.blogpress.service;

import com.nilangpatel.blogpress.model.Blog;
import com.nilangpatel.blogpress.model.Comment;
import com.nilangpatel.blogpress.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CommentService {

    private Logger logger =  LoggerFactory.getLogger(BlogService.class);

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        super();
        this.commentRepository = commentRepository;
    }

    public void addUpdateComment(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> getAllComments(Long blogId) {
        List<Comment> commentList = new ArrayList<Comment>();
        Iterable<Comment> commentIterable =  commentRepository.findAllByBlogId(blogId);
        Iterator<Comment> commentIterator =  commentIterable.iterator();
        while(commentIterator.hasNext()) {
            commentList.add(commentIterator.next());
        }

        return commentList;
    }

}
