package com.emergentideas.siteevents.wobs;

import java.util.ArrayList;
import java.util.List;

import com.emergentideas.siteevents.data.Event;

public class DayBox {
	
	protected String dayNumber = "";
	protected String dateString;
	protected String detailsLink;
	
	protected List<Event> events = new ArrayList<Event>();
	
	public DayBox() {}
	
	public DayBox(String dayNumber) {
		this.dayNumber = dayNumber;
	}
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	public List<Event> getEvents() {
		return events;
	}
	
	public boolean getHasEvents() {
		return !events.isEmpty();
	}

	public String getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(String dayNumber) {
		this.dayNumber = dayNumber;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getDetailsLink() {
		return detailsLink;
	}

	public void setDetailsLink(String detailsLink) {
		this.detailsLink = detailsLink;
	}
	
	

}
