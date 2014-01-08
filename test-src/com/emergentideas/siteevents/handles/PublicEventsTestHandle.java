package com.emergentideas.siteevents.handles;

import java.util.Date;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.emergentideas.siteevents.services.EventService;
import com.emergentideas.utils.DateUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;

public class PublicEventsTestHandle {
	
	@Resource
	protected EventService eventService;
	
	@Path("/small")
	@GET
	@Template
	@Wrap("app_page")
	public Object testSmallCalendar(Location location) throws Exception {
		String month = DateUtils.html5MonthFormat().format(new Date());
		location.put("dayBoxes", eventService.createDayBoxes(month, true));
		
		return "small-test";
	}

}
