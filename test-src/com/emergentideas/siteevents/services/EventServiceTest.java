package com.emergentideas.siteevents.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.emergentideas.base.test.FullEnvTestCase;
import com.emergentideas.siteevents.data.Event;
import com.emergentideas.siteevents.services.EventService;
import com.emergentideas.siteevents.wobs.DayBox;
import com.emergentideas.utils.DateUtils;

import static org.junit.Assert.*;

public class EventServiceTest extends FullEnvTestCase {

	
	@Test
	public void testBasicServiceCalls() throws Exception {
		init();
		EventService es = webAppLocation.getServiceByType(EventService.class);
		
		setupTransaction();
		
		Event e = new Event();
		e.setTitle("event one");
		es.save(e);
		
		commit();
		
		setupTransaction();

		List<Event> events = es.all();
		assertEquals(1, events.size());
		
		assertEquals("event one", events.get(0).getTitle());
		
		Event laterToday = new Event();
		laterToday.setTitle("later today");
		laterToday.setStartDate(new Date());
		
		es.save(laterToday);
		
		events = es.allFromTodayOnward();
		assertEquals(1, events.size());
		assertEquals("later today", events.get(0).getTitle());
		es.deleteAll();
		commit();
		
	}
	
	@Test
	public void testEventSetCalls() throws Exception {
		init();
		EventService es = webAppLocation.getServiceByType(EventService.class);
		
		Date now = new Date();
		
		setupTransaction();
		
		Calendar c = Calendar.getInstance();
		
		Event e = new Event();
		e.setTitle("this");
		e.setStartDate(c.getTime());
		es.save(e);
		
		e = new Event();
		e.setTitle("next");
		c.add(Calendar.MONTH, 1);
		e.setStartDate(c.getTime());
		es.save(e);
		
		e = new Event();
		e.setTitle("next2");
		c.add(Calendar.MONTH, 1);
		e.setStartDate(c.getTime());
		es.save(e);

		assertEquals(3, es.getAllFromTodayOnward(null).size());
		
		assertEquals(2, es.getAllFromTodayOnward(2).size());
		
		assertEquals(1, es.getAllThisMonth().size());

		List<Event> next2List = es.getAllInMonth(DateUtils.html5MonthFormat().format(c.getTime()));
		assertEquals(1, next2List.size());
		
		assertEquals("next2", next2List.get(0).getTitle());
		
		List<Event> thisMonth = es.getAllThisMonth();
		List<DayBox> dayBoxes = es.createDayBoxes(DateUtils.html5MonthFormat().format(now), thisMonth);
		
		int today = es.getDayOfTheMonth(now);
		
		DayBox db = null;
		
		for(DayBox current : dayBoxes) {
			if(("" + today).equals(current.getDayNumber())) {
				db = current;
				break;
			}
		}
		
		assertNotNull(db);
		
		assertEquals(1, db.getEvents().size());
		assertEquals("this", db.getEvents().get(0).getTitle());
		
		
		rollback();
	}
}
