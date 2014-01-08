package com.emergentideas.siteevents.handles;

import java.text.DateFormat;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.emergentideas.siteevents.services.EventService;
import com.emergentideas.utils.DateUtils;
import com.emergentideas.webhandle.Location;
import com.emergentideas.webhandle.output.Template;
import com.emergentideas.webhandle.output.Wrap;


public class PublicEventsHandle {

	@Resource
	protected EventService eventService;
	
	protected DateFormat monthFormat = DateUtils.newDateFormat("MMMM");
	
	@Path("/event-details/{year:\\d+}-{month:\\d+}")
	@GET
	@Template
	@Wrap("public_page")
	public Object getMonthDetail(Location location, String year, String month) throws Exception {
		setupInfo(location, year, month, false);
		
		return "site-events/month-detail";
	}
	
	@Path("/big-calendar/{year:\\d+}-{month:\\d+}")
	@GET
	@Template
	@Wrap("public_page")
	public Object getBigCalendar(Location location, String year, String month) throws Exception {
		
		setupInfo(location, year, month, true);
		return "site-events/big-calendar";
	}
	
	protected void setupInfo(Location location, String year, String month, boolean usePaddedDays) throws Exception {
		if(month.length() < 2) {
			month = "0" + month;
		}
		String wholeMonth = year + "-" + month;
		
		location.put("dayBoxes", eventService.createDayBoxes(wholeMonth, usePaddedDays));
		location.put("monthName", monthFormat.format(DateUtils.html5MonthFormat().parse(wholeMonth)));
		location.put("yearName", year);
		location.put("nextPeriod", getNextPeriod(year, month));
		location.put("previousPeriod", getPreviousPeriod(year, month));
		location.put("currentPeriod", wholeMonth);
	}
	
	protected String getNextPeriod(String year, String month) {
		int iYear = Integer.parseInt(year);
		int iMonth = Integer.parseInt(month);
		
		if(iMonth == 12) {
			iMonth = 1;
			iYear++;
		}
		else {
			iMonth++;
		}
		
		year = "" + iYear;
		month = formatMonth(iMonth);
		
		return year + "-" + month;
	}

	protected String getPreviousPeriod(String year, String month) {
		int iYear = Integer.parseInt(year);
		int iMonth = Integer.parseInt(month);
		
		if(iMonth == 1) {
			iMonth = 12;
			iYear--;
		}
		else {
			iMonth--;
		}
		
		year = "" + iYear;
		month = formatMonth(iMonth);
		
		return year + "-" + month;
	}

	protected String formatMonth(int iMonth) {
		String month = "" + iMonth;
		if(month.length() == 1) {
			month = "0" + month;
		}
		
		return month;
	}
}
