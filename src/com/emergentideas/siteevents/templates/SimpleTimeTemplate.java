package com.emergentideas.siteevents.templates;

import java.text.DateFormat;
import java.util.Date;

import com.emergentideas.utils.DateUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateDef;
import com.emergentideas.webhandle.templates.TemplateInstance;

@TemplateDef("site-events/simple-time")
public class SimpleTimeTemplate implements TemplateInstance {

	protected String timeFormat = "h:mm a";
	
	protected DateFormat dateFormat;
	
	
	@Override
	public void render(SegmentedOutput output, Location location,
			String elementSourceName, String... processingHints) {
		Object o = location.get("$this");
		if(o != null && (o instanceof Date)) {
			Date d = (Date)o;
			output.getStream(elementSourceName).append(formatDate(d));
		}

	}
	
	protected String formatDate(Date d) {
		return getDateFormat().format(d).replace("AM", "a").replace("PM", "p");
	}

	protected DateFormat getDateFormat() {
		if(dateFormat == null) {
			dateFormat = DateUtils.newDateFormat(timeFormat);
		}
		
		return dateFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
		dateFormat = null;
	}
	
	
}
