/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.jsp.PageContext;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

/**
 * Decorator for date display in the MM/dd/yyyy format
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class DateColumnDecorator implements DisplaytagColumnDecorator {
	public Object decorate(Object date, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			return formatter.format((Date) date);
		} else {
			return null;
		}

	}

}
