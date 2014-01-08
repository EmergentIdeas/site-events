package com.emergentideas.siteevents.handles;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.emergentideas.siteevents.data.Event;
import com.emergentideas.siteevents.services.EventService;
import com.emergentideas.siteevents.wobs.EventRepeatPeriod;
import com.emergentideas.webhandle.Inject;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.NotNull;
import com.emergentideas.webhandle.apps.oak.crud.CRUDHandle;
import com.emergentideas.webhandle.assumptions.oak.RequestMessages;
import com.emergentideas.webhandle.assumptions.oak.dob.tables.TableDataModel;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;
import com.emergentideas.webhandle.handlers.Handle;
import com.emergentideas.webhandle.handlers.HttpMethod;
import com.emergentideas.webhandle.output.Show;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;

@Path("/events")
@RolesAllowed({ "page-editors", "administrators"})
public class EventsHandle extends CRUDHandle<Event> {
	
	@Resource
	protected EventService eventService;

	@Override
	public String getTemplatePrefix() {
		return "site-events/";
	}
	
	@Handle(value = {"/all"}, method = HttpMethod.GET)
	@Template
	@Wrap("app_page")
	public Object listAll(InvocationContext context, User user, Location location, HttpServletRequest request) {
		
		List<Event> all = super.findEntitiesToShow(context, user, request);
		all = sortEntities(all);
		location.put("all", all);
		
		TableDataModel table = createDataTable(all);
	
		location.put("table", table);

		return getTemplatePrefix() + "list-all";
	}

	@Handle(value = "/create", method = HttpMethod.POST)
	@Template
	@Wrap("app_page")
	public Object createPost(InvocationContext context, @NotNull @Inject Event focus, Location location, RequestMessages messages,
			EventRepeatPeriod repeatPeriod, Date repeatsUntil) throws Exception {

		if(validateCreate(focus, messages)) {
			entityManager.persist(focus);
			
			if(repeatsUntil != null && repeatPeriod != null && repeatPeriod != EventRepeatPeriod.NONE) {
				
				Long diff = getDifferenceInSeconds(focus);
				
				Date mostRecent = advanceToNext(repeatPeriod, focus.getStartDate());
				while(mostRecent != null && (mostRecent.before(repeatsUntil) || DateUtils.isSameDay(repeatsUntil, mostRecent))) {
					Event e = (Event)focus.clone();
					e.setStartDate(mostRecent);
					if(diff != null) {
						e.setEndDate(addSecondsToDate(mostRecent, diff));
					}
					entityManager.persist(e);
					mostRecent = advanceToNext(repeatPeriod, mostRecent);
				}
			}
			
			return new Show(getPostCreateURL(context, focus, location, messages));
		}
		
		location.add(focus);
		addAssociatedData(context, focus, location);
		return getTemplatePrefix() + "create";
	}
	
	protected Date addSecondsToDate(Date d, Long seconds) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.SECOND, (int)seconds.longValue());
		return c.getTime();
	}
	
	/**
	 * Gets the difference in seconds between the startDate and endDate of the event
	 * or returns null if one of the events is not set.
	 * @param e
	 * @return
	 */
	protected Long getDifferenceInSeconds(Event e) {
		if(e.getStartDate() == null || e.getEndDate() == null) {
			return null;
		}
		
		return (e.getEndDate().getTime() - e.getStartDate().getTime()) / 1000;
	}

	/**
	 * Advances to the next date given the starting date and period.
	 * @param period
	 * @param starting
	 * @return
	 */
	protected Date advanceToNext(EventRepeatPeriod period, Date starting) {
		Calendar c = Calendar.getInstance();
		c.setTime(starting);
		if(period == EventRepeatPeriod.DAILY) {
			c.add(Calendar.DAY_OF_YEAR, 1);
		}
		else if(period == EventRepeatPeriod.WEEKDAY) {
			do {
				c.add(Calendar.DAY_OF_YEAR, 1);
			}
			while(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
		}
		else if(period == EventRepeatPeriod.WEEKENDDAY) {
			do {
				c.add(Calendar.DAY_OF_YEAR, 1);
			}
			while(!(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY));
		}
		else if(period == EventRepeatPeriod.WEEKLY) {
			c.add(Calendar.DAY_OF_YEAR, 7);
		}
		else {
			return null;
		}
		
		return c.getTime();
	}
	

	@Override
	public List<Event> sortEntities(List<Event> all) {
		return eventService.sort(all);
	}

	@Override
	public List<Event> findEntitiesToShow(InvocationContext context, User user,
			HttpServletRequest request) {
		return eventService.allFromTodayOnward();
	}



	@Override
	public boolean validateEdit(Event focus, RequestMessages messages) {
		
		super.validateEdit(focus, messages);
		return validate(focus, messages);
	}

	@Override
	public boolean validateCreate(Event focus, RequestMessages messages) {
		super.validateCreate(focus, messages);
		return validate(focus, messages);
	}

	
	protected boolean validate(Event focus, RequestMessages messages) {
		if(focus.getStartDate() == null) {
			messages.getErrorMessages().add("Start must not be blank.");
		}
		if(StringUtils.isBlank(focus.getTitle())) {
			messages.getErrorMessages().add("Title must not be blank.");
		}
		
		return messages.getErrorMessages().isEmpty();
	}
	
	
}
