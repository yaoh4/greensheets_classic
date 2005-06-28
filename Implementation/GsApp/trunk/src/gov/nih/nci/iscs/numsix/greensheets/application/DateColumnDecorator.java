/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import java.text.*;
import java.util.*;

import org.displaytag.decorator.*;
import org.displaytag.exception.*;

/**
 * Decorator for date display in the MM/dd/yyyy format
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class DateColumnDecorator implements ColumnDecorator {

    /**
     * @see org.displaytag.decorator.ColumnDecorator#decorate(Object)
     */
    public String decorate(Object date) throws DecoratorException {
        if(date != null){     
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            return formatter.format((Date)date);
        }else{
            return null;
        }

    }

}
