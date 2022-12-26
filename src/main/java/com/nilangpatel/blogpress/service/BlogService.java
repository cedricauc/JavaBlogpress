package com.nilangpatel.blogpress.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nilangpatel.blogpress.model.Blog;
import com.nilangpatel.blogpress.model.Comment;
import com.nilangpatel.blogpress.repository.BlogRepository;
import org.springframework.stereotype.Service;


@Service
public class BlogService {

	private Logger logger =  LoggerFactory.getLogger(BlogService.class);

	private BlogRepository blogRepository;

	public BlogService(BlogRepository blogRepository) {
		super();
		this.blogRepository = blogRepository;
	}

	public void addUpdateBlog(Blog blog) {
		blogRepository.save(blog);
	}

	public List<Blog> getAllBlogs() {
		List<Blog> blogList = new ArrayList<Blog>();
		Iterable<Blog> blogIterable =  blogRepository.findAll();
		Iterator<Blog> blogIterator =  blogIterable.iterator();
		while(blogIterator.hasNext()) {
			blogList.add(blogIterator.next());
		}

		return blogList;
	}

	public Blog getBlog(Long blogId) {
		Optional<Blog> blogObj = blogRepository.findById(blogId);
		if(blogObj.isPresent()) {
			return blogObj.get();
		}else {
			return null;
		}
	}

	public int getCurrentChildSequence(Long blogId,Long parentCommentId) {
		return blogRepository.getCurrentChildSequence(blogId,parentCommentId);
	}

	public void updateCommentStatus(Long blogId, String commentId, Set<Comment> commentList, String updatedStatus) {
		if(commentList !=null) {
			for(Comment comment: commentList) {
				if(comment.getCommentId().equals(commentId)) {
					comment.setStatus(updatedStatus);
					break;
				}
			}
			Blog blog = this.getBlog(blogId);
			blog.setComments(commentList);
			blogRepository.save(blog);
		}

	}

	public List<Blog> search(String searchTxt){
		return this.blogRepository.search(searchTxt);
	}

	public void deleteBlog(Long blogId) {
		Optional<Blog> blogObj = this.blogRepository.findById(blogId);
		if(blogObj.isPresent()) {
			this.blogRepository.delete(blogObj.get());
		}
	}


}
