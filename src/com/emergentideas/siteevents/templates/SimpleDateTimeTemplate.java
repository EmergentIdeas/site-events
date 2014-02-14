package com.emergentideas.siteevents.templates;

import java.util.Date;

import com.emergentideas.webhandle.templates.TemplateDef;

@TemplateDef("site-events/simple-date-time")
public class SimpleDateTimeTemplate extends SimpleTimeTemplate {

	public SimpleDateTimeTemplate() {
		timeFormat = "EEE, MMM dd h:mm a";
	}
	
	protected String formatDate(Date d) {
		return getDateFormat().format(d).replace("AM", "a").replace("PM", "p");
	}

}
