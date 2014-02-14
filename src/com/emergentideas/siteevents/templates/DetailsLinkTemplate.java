package com.emergentideas.siteevents.templates;

import java.util.Date;

import javax.annotation.Resource;

import com.emergentideas.siteevents.services.EventService;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateDef;
import com.emergentideas.webhandle.templates.TemplateInstance;

@TemplateDef("site-events/details-link")
public class DetailsLinkTemplate implements TemplateInstance {

	@Resource
	protected EventService eventService;
	
	@Override
	public void render(SegmentedOutput output, Location location,
			String elementSourceName, String... processingHints) {
		Object o = location.get("$this");
		if(o != null && o instanceof Date) {
			Date d = (Date)o;
			String link = eventService.createDetailsLink(null, -1, null, d);
			output.getStream(elementSourceName).append(link);
		}

	}

}
