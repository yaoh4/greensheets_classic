/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Singleton class that contains key-value pairs of the application
 * configuration properties.
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class AppConfigProperties {

	private static AppConfigProperties instance = new AppConfigProperties();

	private Map props = new HashMap();

	private AppConfigProperties() {
	}

	public static AppConfigProperties getInstance() {
		return instance;
	}

	public void addProperties(Map m) {
		props.putAll(m);
	}

	public void addProperty(String key, Object value) {
		props.put(key, value);
	}

	public Object getProperty(String key) {
		return props.get(key);
	}

	public Object findPropertyValue(String key) {

		Object result = null;

		if (props.containsKey(key)) {
			result = props.get(key);
		} else {
			Iterator iter = props.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry me = (Map.Entry) iter.next();
				if (me.getValue() instanceof java.util.Properties) {
					Properties p = (Properties) me.getValue();
					if (p.containsKey(key)) {
						result = p.get(key);
						break;
					}
				}
			}

		}
		return result;
	}
}
