package com.emergentideas.siteevents.handles;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.emergentideas.siteevents.data.Event;
import com.emergentideas.siteevents.services.EventService;
import com.emergentideas.webhandle.InvocationContext;
import com.emergentideas.webhandle.apps.oak.crud.CRUDHandle;
import com.emergentideas.webhandle.assumptions.oak.RequestMessages;
import com.emergentideas.webhandle.assumptions.oak.interfaces.User;

@Path("/events")
public class EventsHandle extends CRUDHandle<Event> {
	
	@Resource
	protected EventService eventService;

	@Override
	public String getTemplatePrefix() {
		return "site-events/";
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
