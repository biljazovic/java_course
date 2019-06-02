package hr.fer.zemris.java.hw15.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents one clog comment. Comment has information about the email fo the
 * user who posted it and time of posting.
 * Anonymus user can post comments but given that he provided the email first.
 * 
 * @author Bruno Iljazović
 */
@Entity
@Table(name="blog_comments")
public class BlogComment {

	/** The id. */
	@Id @GeneratedValue
	private Long id;

	/** The blog entry that this comment is posted on. */
	@ManyToOne
	@JoinColumn(nullable=false)
	private BlogEntry blogEntry;

	/** The user email. */
	@Column(length=100,nullable=false)
	private String userEmail;

	/** The message. */
	@Column(length=4096,nullable=false)
	private String message;

	/** The posted on date. */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date postedOn;
	
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
	 * Gets the user email.
	 *
	 * @return the user email
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * Sets the user email.
	 *
	 * @param userEmail the new user email
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	/**
	 * Gets the blog entry.
	 *
	 * @return the blog entry
	 */
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	
	/**
	 * Sets the blog entry.
	 *
	 * @param blogEntry the new blog entry
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the posted on.
	 *
	 * @return the posted on
	 */
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Sets the posted on.
	 *
	 * @param postedOn the new posted on
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
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
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}