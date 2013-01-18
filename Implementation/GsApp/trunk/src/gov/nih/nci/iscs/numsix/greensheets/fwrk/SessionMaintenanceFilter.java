package gov.nih.nci.iscs.numsix.greensheets.fwrk;

import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * The purpose of this filter is, for most requests that are mapped to Struts 
 * actions, to check for "signs" that objects that need to be present in the 
 * HTTP session as session attributes are indeed there, and if they are not, 
 * to redirect the user to a page saying their session appears to have expired.
 * But action that PLACES those objects into the HTTP session can be allowed 
 * through regardless.
 * @author kouzneta
 *
 */
public class SessionMaintenanceFilter implements Filter {

	Logger logger = Logger.getLogger(SessionMaintenanceFilter.class);
	
	public void doFilter(ServletRequest servletRequest, ServletResponse resp,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = null;
		boolean matchFound = false;
		logger.debug("=============== Session Maintenance Filter is running! ===================");
		if (servletRequest instanceof HttpServletRequest) {
			request = (HttpServletRequest)servletRequest;   // just so we don't need to cast every time
			if( (request.getContextPath()+"/").equals(request.getRequestURI()) ) {
				matchFound = true;  // requests for just /greensheets/ can never expect for user 
					// info to be already in the session - this can always be serviced without checking
			}
			HttpSession session = request.getSession();
			Object initialResourceNames = session.getServletContext()
					.getAttribute(GreensheetsKeys.KEY_SESSION_INITIATING_RESOURCE_NAMES);
			if (initialResourceNames!=null && initialResourceNames instanceof List<?>) {
				StringBuffer requestedURL = request.getRequestURL();
				Iterator<String> i = ((List<String>)initialResourceNames).iterator();
				while (i.hasNext() && !matchFound) {
					String resourceName = i.next();
					if (requestedURL.indexOf(resourceName) != -1) {
						matchFound = true;
					}
				}
				if (!matchFound) {   // requested resource is not one of those that don't care 
									 // about having the user-representing object in the session     
					logger.debug("Checking whether we have the user-representing object in the session.");
					Object gus = session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
					if (gus!=null) {
						logger.debug("  Session attribute with user data exists - processing will continue...");
						filterChain.doFilter(servletRequest, resp);
					}
					else {
						logger.warn("  ****** No user-representing object in session - REDIRECTING TO 'EXPIRED' PAGE!!! ");
						// logger.debug("  . . . . . . but for now, we will let it slide...");
						((HttpServletResponse)resp).sendRedirect(request.getContextPath() +
								Constants.SESSION_EXPIRED_PAGE);
					}
				}
				else {
					logger.debug("The requeseted URL can be serviced fine even if " +
							"we don't have user information in the session");
					filterChain.doFilter(servletRequest, resp);
				}
			}
		}
		logger.debug("=========== Back in Session Maintenance Filter ==============");
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		/*
		 * Taking the filter initialization parameter - one string, breaking it down into 
		 * pieces separated by the delimiter (;), and saving each in application context.
		 * (Yes, I suppose we could have made it the context initialization parameter and
		 * handle in application's init processor... but...)
		 */
		Enumeration<String> initParamNames = (Enumeration<String>)filterConfig.getInitParameterNames();
		while(initParamNames.hasMoreElements()) {
			String paramName = initParamNames.nextElement();
			if (paramName!=null 
					&& Constants.SESS_MAINT_FLTR_INIT_PARAM_NAME.equals(paramName)) {
				String paramValue = filterConfig.getInitParameter(
						Constants.SESS_MAINT_FLTR_INIT_PARAM_NAME);
				filterConfig.getServletContext().removeAttribute(GreensheetsKeys.KEY_SESSION_INITIATING_RESOURCE_NAMES);
				StringTokenizer tokenizer = new StringTokenizer(paramValue, ";");
				List<String> initialResourceNames = new LinkedList<String>();
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					if (token!=null && !"".equals(token)) {
						initialResourceNames.add(token);
					}
				}
				filterConfig.getServletContext().setAttribute(
						GreensheetsKeys.KEY_SESSION_INITIATING_RESOURCE_NAMES, initialResourceNames);
			}
		}
	}

	public void destroy() {
		
	}

}
