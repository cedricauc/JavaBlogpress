package com.nilangpatel.blogpress.controller;

import com.nilangpatel.blogpress.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nilangpatel.blogpress.constants.BlogStatus;
import com.nilangpatel.blogpress.constants.BlogpressConstants;
import com.nilangpatel.blogpress.constants.CommentStatus;
import com.nilangpatel.blogpress.model.Blog;
import com.nilangpatel.blogpress.model.Comment;
import com.nilangpatel.blogpress.service.BlogService;
import com.nilangpatel.blogpress.util.BlogpressUtil;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BlogController {
    private Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;


    @GetMapping("/")
    public String showHomePage(Model model) {
        logger.info("This is show home page method ");
        setProcessingData(model, BlogpressConstants.TITLE_HOME_PAGE);
        return "home";
    }

    @GetMapping("/controlPage")
    public String showControlPage(Model model) {
        logger.info("This is control page ");
        setProcessingData(model, BlogpressConstants.TITLE_LANDING_CONTROL_PAGE);
        model.addAttribute("pagename", "controlPage");
        return "control-page";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error",required = false) String error,
                                @RequestParam(value = "logout",	required = false) String logout,Model model) {
        logger.info("This is login page URL   ");

        if (error != null) {
            model.addAttribute("error", "Invalid Credentials provided.");
        }

        if (logout != null) {
            model.addAttribute("message", "Logged out");
        }

        setProcessingData(model, BlogpressConstants.TITLE_LOGIN_PAGE);

        return "login";
    }

    @GetMapping("/showAddNew")
    public String showAddNew(Model model) {
        logger.info("This is addNew page URL   ");
        setProcessingData(model, BlogpressConstants.TITLE_NEW_BLOG_PAGE);
        return "add-new";
    }

    @PostMapping("/addNewBlog")
    public String addNewBlog(@RequestParam(value = "title",required = true) String title,
                             @RequestParam(value = "body",required = true) String body,Model model) {
        logger.info("Adding new blog with title :"+title );
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setBody(body);
        blog.setCreatedBy(getCurrentUserName());
        blog.setCreatedDate(new Date());
        blog.setPublishDate(new Date());
        blog.setStatus(BlogStatus.PUBLISHED.getStatus());

        blogService.addUpdateBlog(blog);
        return "home";
    }

    @GetMapping("/manageBlogs")
    public String showManageBlogPage(Model model) {
        logger.info("This is manage blog page method ");
        setProcessingData(model, BlogpressConstants.TITLE_MANAGE_BLOG_PAGE);

        List<Blog> blogSearchResultLst = blogService.getAllBlogs();
        for(Blog blog: blogSearchResultLst) {
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

        model.addAttribute("blogs",blogSearchResultLst);
        model.addAttribute("pagename", "manageBlogs");
        return "manage-blogs";
    }

    @PostMapping("/showUpdateBlogPage")
    public String showUpdateBlogPage(@RequestParam(value = "blogId",required = true) Long blogId,
                                     Model model) {
        Blog blog = blogService.getBlog(blogId);
        model.addAttribute("blog",blog);
        return "edit-blog";
    }

    @PostMapping("/updateBlog")
    public String updateBlog(@RequestParam(value = "blogId",required = true) Long blogId,
                             @RequestParam(value = "title",required = true) String title,
                             @RequestParam(value = "body",required = true) String body,
                             Model model) {
        Blog blog = blogService.getBlog(blogId);
        if(blog !=null) {
            blog.setTitle(title);
            blog.setBody(body);
            blogService.addUpdateBlog(blog);
        }
        model.addAttribute("blog",blog);
        return "redirect:/manageBlogs";
    }

    @PostMapping("/deleteBlog")
    public String deleteBlog(@RequestParam(value = "blogId",required = true) Long blogId,
                             Model model) {
        blogService.deleteBlog(blogId);
        return "redirect:/manageBlogs";
    }

    @GetMapping("/viewBlog")
    public String viewBlog(@RequestParam(value = "blogId",required = true) Long blogId, Model model) {
        logger.info("showing view blog page");
        if(blogId !=null) {
            Blog blog = blogService.getBlog(blogId);
            List<Comment> approvedCommentLst = commentService.getAllComments(blogId);
            if(approvedCommentLst !=null && !approvedCommentLst.isEmpty()) {
                approvedCommentLst = approvedCommentLst.stream().
                        filter(comment->comment.getStatus().equalsIgnoreCase("A"))
                        .collect(Collectors.toList());
                if(approvedCommentLst !=null) {
                    blog.setComments(new HashSet<Comment>(approvedCommentLst));
                }else {
                    blog.setComments(new HashSet<Comment>());
                }
            }
            if(approvedCommentLst == null || approvedCommentLst.isEmpty()) {
                blog.setComments(new HashSet<Comment>());
            }
            model.addAttribute("blog", blog);
        }
        setProcessingData(model, BlogpressConstants.TITLE_VIEW_BLOG_PAGE);
        return "view-blog";
    }

    @PostMapping("/search")
    public String searchBlog(@RequestParam(value="searchText") String searchText,Model model){

        List<Blog> blogSearchResultLst = blogService.search(searchText);
        for(Blog blog: blogSearchResultLst) {
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

        model.addAttribute("blogSearchResultLst",blogSearchResultLst);
        model.addAttribute("searchText", searchText);
        return "search";
    }

    @PostMapping("/updateCommentStatus")
    public String updateCommentStatus(@RequestParam(value = "blogId",required = true) Long blogId,
                                      @RequestParam(value = "commentId",required = true) Long commentId,
                                      @RequestParam(value = "commentStatus",required = true) String commentStatus,
                                      Model model) {
        logger.info("Approve comment");

        if(blogId !=null) {
            Blog blog = blogService.getBlog(blogId);
            List<Comment> commentLst = commentService.getAllComments(blogId);
            if(blog !=null) {
                commentService.updateCommentStatus(commentId, commentLst, commentStatus);
            }
        }
        return "manage-comments";
    }

    @GetMapping("/showComments")
    public String showManageComments(Model model) {
        model.addAttribute("pagename", "showComments");
        return "manage-comments";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam(value = "blogId",required = true) Long blogId,
                             @RequestParam(value = "name",required = true) String name,
                             @RequestParam(value = "email",required = true) String email,
                             @RequestParam(value = "comment",required = true) String comment,
                             @RequestParam(value = "currentLevel",required = false,defaultValue="0") Integer currentLevel,
                             @RequestParam(value = "parentId",required = false,defaultValue="0") Long parentId,
                             @RequestParam(value = "parentPosition",required = false) String parentPosition,
                             Model model) {
        logger.info("Add comment page");

        if(blogId !=null) {
            StringBuffer currentPositionStr= new StringBuffer();
            int childSequence = blogService.getCurrentChildSequence(blogId,parentId);

            if(parentPosition !=null) {
                currentPositionStr.append(parentPosition).append(".");
            }
            currentPositionStr.append(currentLevel+1).append(".").append(childSequence);

            Blog blog = blogService.getBlog(blogId);
            if(blog !=null) {
                Set<Comment> blogComments = blog.getComments();
                if(blogComments == null) {
                    blogComments = new HashSet<Comment>();
                }

                Date currentDate = new Date();

                Comment blogComment = new Comment();
                blogComment.setCommentId(BlogpressUtil.RandonmNumber(currentDate));
                blogComment.setBlog(blog);
                blogComment.setParentId(parentId);
                blogComment.setChildSequence(childSequence);
                blogComment.setPosition(currentPositionStr.toString());
                blogComment.setStatus(CommentStatus.MODERATE.getStatus());
                blogComment.setLevel(currentLevel+1);
                blogComment.setUser(name);
                blogComment.setEmailAddress(email);
                blogComment.setCommentText(comment);
                blogComment.setCreatedDate(currentDate);
                commentService.addUpdateComment(blogComment);
                model.addAttribute("blog", blog);
            }
        }

        setProcessingData(model, BlogpressConstants.TITLE_VIEW_BLOG_PAGE);
        return "redirect:viewBlog?blogId="+blogId;
    }

    /**
     * This method will check if current logged in user has given role
     * @param roleName
     * @return true or false - if user has given role
     */
    private boolean checkIfUserHasRole(String roleName) {
        boolean hasUserRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(roleName));

        return hasUserRole;
    }

    /**
     * This method will check if valid user is logged in.
     * @return boolean if user is logged In
     */
    @ModelAttribute("validUserLogin")
    public boolean isUserLoggedIn() {
        return SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    @ModelAttribute("hasAdminRole")
    public boolean checkIfUserHasAdminRole(){
        return checkIfUserHasRole(BlogpressConstants.ROLE_ADMIN);
    }

    @ModelAttribute("hasUserRole")
    public boolean checkIfUserHasUserRole(){
        return checkIfUserHasRole(BlogpressConstants.ROLE_USER);
    }

    /**
     * This method will return current user name
     * @return
     */
    @ModelAttribute("currentUserName")
    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();

    }

    /**
     * This method stores various data which are required on presentation layer.
     * @param model
     * @param pageTitle
     */
    private void setProcessingData(Model model,String pageTitle) {
        model.addAttribute(BlogpressConstants.PAGE_TITLE, pageTitle);
    }
}

