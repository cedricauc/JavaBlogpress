package com.nilangpatel.blogpress.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nilangpatel.blogpress.util.BlogpressUtil;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="comment")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long commentId;
	@Column(name = "parent_id")
	private Long parentId;
	@Column(name = "child_sequence")
	private int childSequence;
	@Column(name = "position")
	private String position;
	@Column(name = "status")
	private String status;
	@Column(name = "level")
	private int level;
	@Column(name = "user")
	private String user;
	@Column(name = "email_address")
	private String emailAddress;
	@Column(name = "comment_text")
	private String commentText;
	@Column(name = "created_date")
	private Date createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="blogId", nullable=false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Blog blog;

	public String getCreatedDateForDisplay() {
		String returnDateStr="";
		if(this.getCreatedDate() !=null) {
			returnDateStr = BlogpressUtil.getFormattedDateForDisplayOnPage(createdDate);
		}
		return returnDateStr;
	}

	@Override
	public String toString() {
		return "Comment {" +
				"\"position\":" + position+ "\"" +
				"\"user\":" + user+ "\"" +
				"\"emailAddress\":" + emailAddress+ "\"" +
				"\"commentText\":" + commentText+ "\"" +
				"\"createdDate\":" + BlogpressUtil.getFormattedDateForSearch(createdDate)+ "\"" +
				"})";
	}
}
