package com.ticketing_project.Ticketing.Project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Feedback")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "feedback_id", nullable = false)
	@Getter
	@Setter
	private int feedback_id;

	@Column(name = "statusId", nullable = false)
	@Getter
	@Setter
	private int statusId;

	@Column(name = "feedback_message", nullable = false)
	@Getter
	@Setter
	private String feedback_message;

	@Column(name = "feedback_date_time", nullable = false)
	@Getter
	@Setter
	private String feedback_date_time;

}
