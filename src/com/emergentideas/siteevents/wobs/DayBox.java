package com.emergentideas.siteevents.wobs;

import java.util.ArrayList;
import java.util.List;

import com.emergentideas.siteevents.data.Event;

public class DayBox {
	
	protected String dayNumber = "";
	
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
	
	public boolean hasEvents() {
		return !events.isEmpty();
	}

	public String getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(String dayNumber) {
		this.dayNumber = dayNumber;
	}
	
	

}
