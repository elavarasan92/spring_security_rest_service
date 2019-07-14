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
@Table(name = "primary_question")
@EntityListeners(AuditingEntityListener.class)
public class PrimaryQuestion {

	@Id
	@GeneratedValue
	@Column(name = "PRIMARY_QUESTION_ID")
	private long primaryQuestionId;
	@Column(name = "PRIMARY_QUESTION_DESCRIPTION")
	@NotNull(message = "primary question description should not be null")
	@Size(min = 3, max = 100, message = "primary question must be between 3 and 100 characters")
	private String primaryQuestionDescription;

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

	public long getPrimaryQuestionId() {
		return primaryQuestionId;
	}

	public void setPrimaryQuestionId(long primaryQuestionId) {
		this.primaryQuestionId = primaryQuestionId;
	}

	public String getPrimaryQuestionDescription() {
		return primaryQuestionDescription;
	}

	public void setPrimaryQuestionDescription(String primaryQuestionDescription) {
		this.primaryQuestionDescription = primaryQuestionDescription;
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
		return "User [primaryQuestionId=" + primaryQuestionId + ", primaryQuestionDescription="
				+ primaryQuestionDescription + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + "]";
	}

}

