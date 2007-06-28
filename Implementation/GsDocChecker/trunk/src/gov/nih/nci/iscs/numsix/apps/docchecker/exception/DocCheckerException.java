package gov.nih.nci.iscs.numsix.apps.docchecker.exception;

public class DocCheckerException extends Exception {
	private static final long serialVersionUID = 1L;

	public DocCheckerException() {
		super();
	}

	public DocCheckerException(String s) {
		super(s);
	}

	public DocCheckerException(Throwable arg0) {
		super(arg0);
	}

	public DocCheckerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
