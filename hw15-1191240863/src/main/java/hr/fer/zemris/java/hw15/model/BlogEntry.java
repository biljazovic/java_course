package hr.fer.zemris.java.hw15.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents one blog entry. It has the information about when it was created
 * and when it was last modifed.
 * 
 * @author Bruno Iljazović
 */
@Entity
@Table(name="blog_entries")
public class BlogEntry {

	/** The id. */
	@Id @GeneratedValue
	private Long id;

	/** The comments. */
	@OneToMany(mappedBy="blogEntry",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("postedOn DESC")
	private List<BlogComment> comments = new ArrayList<>();
	
	/** The blog user. */
	@ManyToOne
	@JoinColumn(nullable=false)
	private BlogUser blogUser;

	/** created at. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date createdAt;

	/** last modified at. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	private Date lastModifiedAt;

	/** The title. */
	@Column(length=200,nullable=false)
	private String title;

	/** The text. */
	@Column(length=4096,nullable=false)
	private String text;

	/**
	 * Gets the blog user.
	 *
	 * @return the blog user
	 */
	public BlogUser getBlogUser() {
		return blogUser;
	}

	/**
	 * Sets the blog user.
	 *
	 * @param blogUser the new blog user
	 */
	public void setBlogUser(BlogUser blogUser) {
		this.blogUser = blogUser;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the comments.
	 *
	 * @return the comments
	 */
	public List<BlogComment> getComments() {
		return comments;
	}
	
	/**
	 * Sets the comments.
	 *
	 * @param comments the new comments
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Gets the created time.
	 *
	 * @return the created
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the created time.
	 *
	 * @param createdAt the new created
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets the last modified time.
	 *
	 * @return the last modified time
	 */
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Sets the last modified time.
	 *
	 * @param lastModifiedAt the new last modified time
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}