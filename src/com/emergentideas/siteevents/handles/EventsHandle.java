package com.emergentideas.siteevents.handles;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.siteevents.data.Event;
import com.emergentideas.siteevents.services.EventService;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.apps.oak.crud.CRUDHandle;
import com.emergentideas.webhandle.assumptions.oak.RequestMessages;
import com.emergentideas.webhandle.assumptions.oak.dob.tables.TableDataModel;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;
import com.emergentideas.webhandle.handlers.Handle;
import com.emergentideas.webhandle.handlers.HttpMethod;
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
	public Object list(InvocationContext context, User user, Location location, HttpServletRequest request) {
		
		List<Event> all = super.findEntitiesToShow(context, user, request);
		all = sortEntities(all);
		location.put("all", all);
		
		TableDataModel table = createDataTable(all);
	
		location.put("table", table);

		return getTemplatePrefix() + "list";
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
