package com.emergentideas.siteevents.templates;

import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.SegmentedOutput;
import com.emergentideas.webhandle.templates.TemplateDef;
import com.emergentideas.webhandle.templates.TemplateInstance;

@TemplateDef("site-events/no-esc")
public class NoEsc implements TemplateInstance {

	@Override
	public void render(SegmentedOutput output, Location location,
			String elementSourceName, String... processingHints) {
		Object o = location.get("$this");
		if(o != null) {
			output.getStream(elementSourceName).append(o.toString());
		}
	}

}
