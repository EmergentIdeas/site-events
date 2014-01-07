package com.emergentideas.base.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class AllPermissionsRequest extends HttpServletRequestWrapper {
	
	public AllPermissionsRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public boolean isUserInRole(String role) {
		return true;
	}

	
}
