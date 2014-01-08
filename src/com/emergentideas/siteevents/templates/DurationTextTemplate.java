package com.emergentideas.siteevents.templates;

import java.util.Date;

import com.emergentideas.siteevents.data.Event;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateDef;
import com.emergentideas.webhandle.templates.TemplateInstance;

@TemplateDef("site-events/duration-text")
public class DurationTextTemplate extends SimpleTimeTemplate {

	@Override
	public void render(SegmentedOutput output, Location location,
			String elementSourceName, String... processingHints) {
		Object o = location.get("$this");
		if(o != null && (o instanceof Event)) {
			Event e = (Event)o;
			output.getStream(elementSourceName).append(formatDate(e.getEndDate()));
		}
	}

}
