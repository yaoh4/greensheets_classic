/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.fwrk;

import org.apache.commons.lang.exception.*;

/**
 *  Base Nestable Exception class
 * 
 *  @see org.apache.commons.lang.exception
 * 
 *  @author kpuscas, Number Six Software
 */
public class GreensheetBaseException extends NestableException {

    /**
     * Constructor for GreensheetBaseException.
     */
    public GreensheetBaseException() {
        super();
    }

    /**
     * Constructor for GreensheetBaseException.
     * @param arg0
     */
    public GreensheetBaseException(String arg0) {
        super(arg0);
    }

    /**
     * Constructor for GreensheetBaseException.
     * @param arg0
     */
    public GreensheetBaseException(Throwable arg0) {
        super(arg0);
    }

    /**
     * Constructor for GreensheetBaseException.
     * @param arg0
     * @param arg1
     */
    public GreensheetBaseException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
