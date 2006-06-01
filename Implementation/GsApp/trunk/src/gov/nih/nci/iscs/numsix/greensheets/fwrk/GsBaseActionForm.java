package gov.nih.nci.iscs.numsix.greensheets.fwrk;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import org.apache.struts.action.ActionForm;

public class GsBaseActionForm extends ActionForm {

   /** isNull */
	public boolean isNull() {
		Class clazz = this.getClass();
		Field[] fields = clazz.getDeclaredFields();
		boolean result = true;
		try {
			AccessibleObject.setAccessible(fields, true);
			for (int i = 0; i < fields.length; i++) {
				String value = fields[i].get(this).toString();
				if (value != null) {
					result = false;
					break;
				}
			}
		} catch (Exception e) {
			// Don't bother reporting this
		}
		return result;
	}

	/** toString */
	public String toString() {
		StringBuffer results = new StringBuffer();
		Class clazz = getClass();
		results.append(getClass().getName() + "\n");
		Field[] fields = clazz.getDeclaredFields();
		try {
			AccessibleObject.setAccessible(fields, true);
			for (int i = 0; i < fields.length; i++) {
				results.append("\t" + fields[i].getName() + "="
						+ fields[i].get(this) + "\n");
			}
		} catch (Exception e) {
			// Don't bother reporting this
		}
		return results.toString();
	}
	
	/** clear */
	public void clear() {
		Class clazz = this.getClass();
		Field[] fields = clazz.getDeclaredFields();
		try {
			AccessibleObject.setAccessible(fields, true);
			for (int i = 0; i < fields.length; i++) {
			   fields[i].set(this,null);
			}
		} catch (Exception e) {
			// Don't bother reporting this
		}
	}

}
