package com.emergentideas.siteevents.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Event implements Cloneable {

	@Id
	@GeneratedValue
	protected Integer id;
	
	protected String title;
	protected Date startDate;
	protected Date endDate;
	protected String location;
	protected String description;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Event e = new Event();
		e.description = description;
		e.endDate = endDate;
		e.location = location;
		e.startDate = startDate;
		e.title = title;
		return e;
	}
	
	
}
