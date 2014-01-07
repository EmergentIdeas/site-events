package com.emergentideas.siteevents.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.time.DateUtils;

import com.emergentideas.siteevents.data.Event;
import com.emergentideas.siteevents.wobs.DayBox;

@Resource(type = EventService.class, name = "eventService")
public class EventService {
	
	@Resource
	protected EntityManager entityManager;

	
	public List<Event> all() {
		return entityManager.createQuery("select e from " + getEventClassName() + " e", Event.class).getResultList();
	}
	
	public List<Event> allFromTodayOnward() {
		
		return getAllFromTodayOnward(null);
	}
	
	public List<Event> getAllFromTodayOnward(Integer limit) {
		
		Date d = setTimeToMidnight(new Date());
		
		Query query = entityManager
				.createQuery("select e from " + getEventClassName() + " e where e.startDate >= :midnight", Event.class)
				.setParameter("midnight", d);
		if(limit != null) {
			query.setMaxResults(limit);
		}
		
		return query.getResultList();
	}

	/**
	 * Returns all the events for a month where the month string is like 2013-01
	 * @param month
	 * @return
	 */
	public List<Event> getAllInMonth(String month) throws ParseException {
		Date monthDate = parseMonthDate(month);
		return getAllBetween(getFirstMomentOfMonth(monthDate), getLastMomentOfMonth(monthDate));
	}
	
	/**
	 * Turns a <code>month</code> like 2013-01 into a {@link Date}.
	 * @param month
	 * @return
	 */
	protected Date parseMonthDate(String month) throws ParseException {
		Date monthDate = com.emergentideas.utils.DateUtils.html5MonthFormat().parse(month);
		return monthDate;
	}

	public List<Event> getAllThisMonth() {
		Date now = new Date();
		return getAllBetween(getFirstMomentOfMonth(now), getLastMomentOfMonth(now));
		
	}
	
	public Date getFirstMomentOfMonth(Date d) {
		d = setTimeToMidnight(d);
		d = DateUtils.setDays(d, 1);
		return d;
	}

	public Date getLastMomentOfMonth(Date d) {
		d = getFirstMomentOfMonth(d);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.SECOND, -1);
		
		return c.getTime();
	}
	
	public List<Event> getAllBetween(Date start, Date end) {
		Query query = entityManager
				.createQuery("select e from " + getEventClassName() + " e where e.startDate between :start and :end", Event.class)
				.setParameter("start", start)
				.setParameter("end", end);
		return query.getResultList();
	}
	
	public List<DayBox> createDayBoxes(String month, List<Event> events) throws ParseException {
		List<DayBox> result = new ArrayList<DayBox>();
		Date first = getFirstMomentOfMonth(parseMonthDate(month));
		
		Calendar c = Calendar.getInstance();
		c.setTime(first);
		
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
		
		// add padding days so that the first doesn't always start on sunday
		while(dayOfWeek-- > 0) {
			result.add(new DayBox());
		}
		
		sort(events);
		
		int lastDay = getDayOfTheMonth(getLastMomentOfMonth(first));
		
		for(int day = 1; day <= lastDay; day++) {
			DayBox dayBox = new DayBox("" + day);
			result.add(dayBox);
			while(!events.isEmpty() && day == getDayOfTheMonth(events.get(0).getStartDate())) {
				dayBox.addEvent(events.remove(0));
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the day of the month, 1 if the date is null
	 * @param d
	 * @return
	 */
	protected int getDayOfTheMonth(Date d) {
		if(d == null) {
			return 1;
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Sorts the <code>events</code> list and returns it.
	 * @param events
	 * @return
	 */
	public List<Event> sort(List<Event> events) {
		Collections.sort(events, new EventDateComparator());
		return events;
	}
	
	public void deleteAll() {
		entityManager.createQuery("delete from " + getEventClassName());
	}
	
	protected Date setTimeToMidnight(Date d) {
		d = DateUtils.setHours(d, 0);
		d = DateUtils.setMinutes(d, 0);
		d = DateUtils.setSeconds(d, 0);
		d = DateUtils.setMilliseconds(d, 0);
		return d;
	}
	
	public void save(Event e) {
		entityManager.persist(e);
	}
	
	protected String getEventClassName() {
		return Event.class.getName();
	}
}
