package com.nilangpatel.blogpress.model;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nilangpatel.blogpress.util.BlogpressCommentComparator;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nilangpatel.blogpress.util.BlogpressUtil;


@Data
@Entity
@Table(name="blog")
public class Blog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "title")
	private String title;
	@Column(name = "body")
	private String body;
	@Column(name = "status")
	private String status;
	@Column(name = "created_by")
	private String createdBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy'T'HH:mm:ss")
	@Column(name = "created_date")
	private Date createdDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy'T'HH:mm:ss")
	@Column(name = "published_date")
	private Date publishDate;

	@OneToMany(targetEntity=Comment.class, mappedBy="blog", cascade = CascadeType.ALL, orphanRemoval = true)
	//@Column(nullable=true)
	@JsonIgnoreProperties("blog")
	private Set<Comment> comments = new HashSet<Comment>();

	/**
	 * @return the comments
	 */
	public Set<Comment> getComments() {
		if(comments !=null && !comments.isEmpty()) {
			Collections.sort(new ArrayList<>(comments), new BlogpressCommentComparator());
		}
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public String getPublishDateForDisplay() {
		String returnDateStr="";
		if(this.getCreatedDate() !=null) {
			returnDateStr = BlogpressUtil.getFormattedDateForDisplayOnPage(createdDate);
		}
		return returnDateStr;
	}

	public int getCommentSize() {
		if(this.comments !=null) {
			return this.comments.size();
		}else {
			return 0;
		}
	}

	@Override
    public String toString() {
        return "blog {" +
                "\"title\":" + title+ "\"" +
                "\"body\":" + body+ "\"" +
                "\"status\":" + status+ "\"" +
                "\"createdBy\":" + createdBy+ "\"" +
                "\"createdDate\":" + BlogpressUtil.getFormattedDateForSearch(createdDate)+ "\"" +
                "\"publishDate\":" + BlogpressUtil.getFormattedDateForSearch(publishDate)+ "\"" +
                "\"comments\":"+getComments()+
                "})";
    }
	
}
