package com.emergentideas.siteevents.services;

import java.util.Comparator;

import com.emergentideas.siteevents.data.Event;

public class EventDateComparator implements Comparator<Event> {

	@Override
	public int compare(Event o1, Event o2) {
		if(o1 == null && o2 == null) {
			return 0;
		}
		
		if(o1 == null) {
			return -1;
		}
		
		if(o2 == null) {
			return 1;
		}
		
		if(o1.getStartDate() == null) {
			return -1;
		}
		
		if(o2.getStartDate() == null) {
			return 1;
		}
		
		return o1.getStartDate().compareTo(o2.getStartDate());
	}

	
}
