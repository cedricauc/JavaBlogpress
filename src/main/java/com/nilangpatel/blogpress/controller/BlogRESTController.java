package com.nilangpatel.blogpress.controller;

import com.nilangpatel.blogpress.model.Blog;
import com.nilangpatel.blogpress.model.Comment;
import com.nilangpatel.blogpress.service.BlogService;
import com.nilangpatel.blogpress.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class BlogRESTController {

    private Logger logger =  LoggerFactory.getLogger(BlogRESTController.class);

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/listBlogs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Blog>> getAllBlogJSON() {
        logger.info("getting all blog data in json format ");
        List<Blog> allBlogs = blogService.getAllBlogs();

        for(Blog blog: allBlogs) {
            List<Comment> commentLst = commentService.getAllComments(blog.getId());
            if(commentLst != null && !commentLst.isEmpty()) {
                commentLst = commentLst.stream().
                        filter(comment->comment.getStatus().equalsIgnoreCase("A"))
                        .collect(Collectors.toList());
                if(commentLst !=null) {
                    blog.setComments(new HashSet<Comment>(commentLst));
                }else {
                    blog.setComments(new HashSet<Comment>());
                }
            }
            if(commentLst == null || commentLst.isEmpty()) {
                blog.setComments(new HashSet<Comment>());
            }
        }

        return new ResponseEntity<List<Blog>>(allBlogs, HttpStatus.OK);
    }

    @RequestMapping(value = "/listAllComments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Comment>> getAllCommentJSON() {
        logger.info("getting all blog data in json format ");
        List<Comment> allComments = (List<Comment>) blogService.getAllComments(0, 100);

        return new ResponseEntity<List<Comment>>(allComments, HttpStatus.OK);
    }
}
