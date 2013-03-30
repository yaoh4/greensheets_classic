package gov.nih.nci.iscs.numsix.greensheets.fwrk;

/**
 * An Exception that is thrown if, when trying to save a brand-new greensheet for the
 * first time, it turns out that it has been saved already (from another window? by 
 * another person?).
 * We catch it in the Struts action, and if it is this specific type of Exception, we 
 * show the user a custom, specific message and don't send a notification e-mail. 
 * @author Anatoli Kouznetsov
 */
public class GsDuplicateFormsException extends GreensheetBaseException {

	private static final long serialVersionUID = -2347521874791565961L;

	public GsDuplicateFormsException() {
		super();
	}

	public GsDuplicateFormsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public GsDuplicateFormsException(String arg0) {
		super(arg0);
	}

	public GsDuplicateFormsException(Throwable arg0) {
		super(arg0);
	}
}
