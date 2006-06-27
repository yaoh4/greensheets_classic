/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetresourcemgr;

/**
 * Wraps information related to a greensheet template.
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetTemplateWrapper {

	private int id;

	private String type;

	private String mech;

	private String template;

	private String group;

	/**
	 * Constructor for GreensheetTemplateWrapper.
	 */
	public GreensheetTemplateWrapper() {
	}

	public GreensheetTemplateWrapper(int id, String template) {
		this.id = id;
		this.template = template;
	}

	public GreensheetTemplateWrapper(int id, String type, String mech,
			String template) {
		this.id = id;
		this.type = type;
		this.mech = mech;
		this.template = template;
	}

	/**
	 * Returns the id for this template
	 * 
	 * @return String
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the mech.
	 * 
	 * @return String
	 */
	public String getMech() {
		return mech;
	}

	/**
	 * Returns the String that contains the template.
	 * 
	 * @return String
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Returns the type.
	 * 
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            The id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the mech.
	 * 
	 * @param mech
	 *            The mech to set
	 */
	public void setMech(String mech) {
		this.mech = mech;
	}

	/**
	 * Sets the template.
	 * 
	 * @param template
	 *            The template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            The type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
