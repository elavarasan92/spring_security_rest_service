package com.questionbank.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "secondary_question")
@EntityListeners(AuditingEntityListener.class)
public class SecondaryQuestion {

	@Id
	@GeneratedValue
	@Column(name = "PRIMARY_QUESTION_ID")
	private long secondaryQuestionId;
	@Column(name = "PRIMARY_QUESTION_DESCRIPTION")
	@NotNull(message = "secondary question description should not be null")
	@Size(min = 3, max = 100, message = "secondary question must be between 3 and 100 characters")
	private String secondaryQuestionDescription;

	@Column(name = "CREATED_BY")
	@CreatedBy
	private String createdBy;

	@Column(name = "CREATED_DATE")
	@CreatedDate
	private Timestamp createdDate;

	@Column(name = "UPDATED_BY")
	@LastModifiedBy
	private String updatedBy;

	@Column(name = "UPDATED_DATE")
	@LastModifiedDate
	private Timestamp updatedDate;

	public long getSecondaryQuestionId() {
		return secondaryQuestionId;
	}

	public void setSecondaryQuestionId(long secondaryQuestionId) {
		this.secondaryQuestionId = secondaryQuestionId;
	}

	public String getSecondaryQuestionDescription() {
		return secondaryQuestionDescription;
	}

	public void setSecondaryQuestionDescription(String secondaryQuestionDescription) {
		this.secondaryQuestionDescription = secondaryQuestionDescription;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "User [secondaryQuestionId=" + secondaryQuestionId + ", secondaryQuestionDescription="
				+ secondaryQuestionDescription + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + "]";
	}

}

