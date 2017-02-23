package gov.nih.nci.iscs.numsix.greensheets.utils;

import java.util.Properties;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class FilesysUtils {

	static final String REL_PATH_TO_PROPERTIES_FILE = "appconfig";
	
	// We don't need this class instantiated - its point is to have its static method used.
	private FilesysUtils(){}
	
	/**
	 * Get the base directory in which (in the sub-directories representing package levels)
	 * the physical class file is located (of the class whose name you pass as a parameter).
	 * @param className  name of the class for which we need to determine the "base"
	 * directory
	 * @return  the {@code File} object representing the directory.
	 */
	public static File getBaseDirectory(String className) {
		
		File theDirectory = null;
		
		/* Getting the path of the physical class file of the class whose name is passed */
		/* as a parameter. */
		String resourceName = className.replace('.', '/') + ".class";
		URL classURL = ClassLoader.getSystemClassLoader().getResource(resourceName);
		if (classURL != null) {
			try {
				URI classURI = classURL.toURI();
				// System.out.println("The main class is loaded from: " + classURI);
				if (classURI.toString().substring(0, 4).equals(new String("jar:"))) {
					// System.out.println("We determined that the program is being run from a JAR file.");
					int n = classURI.toString().toUpperCase().indexOf(".JAR!");
					String interimString = classURI.toString().substring(4, n);
					String jarPath = interimString.substring(0, interimString.lastIndexOf('/')+1);
					// System.out.println("JAR's directory is: " + jarPath);
					URI dirURI = new URI(jarPath);
					theDirectory = new File(dirURI);
					// propURI = URI.create(jarPath + REL_PATH_TO_PROPERTIES_FILE + propsFileName);
					// System.out.println("We will look for the properties file in: " + propURI);
					dirURI = null;
				}
				else {    // we are NOT being run from a JAR
					int n = classURI.toString().indexOf(className.replace('.', '/')+".class");
					// System.out.println("So the path up to the base folder is " + classURI.toString().substring(0, n));
					String dirString = classURI.toString().substring(0, n);
					URI dirURI = new URI(dirString);
					theDirectory = new File(dirURI);
					// propURI = URI.create(classURI.toString().substring(0, n) + REL_PATH_TO_PROPERTIES_FILE + propsFileName);
					// System.out.println("We will look for the properties file in: " + propURI);
					dirURI = null;
				}
			}
			catch (URISyntaxException e) {
				System.err.println("Determining base directory location for class " + className + " failed.");
				System.err.println("The error message is: " + e.getMessage());
			}
		}
		return theDirectory;
		/* End of the section of getting and manipulating base path to the class file. */
		
	}

	/**
	 * Loads properties from a file with specified file name, looking for the physical
	 * location of the file in the file system based on the name of the class that 
	 * should be already loaded from some physical file.
	 * @param className - name of the class that is going to try load the properties 
	 * from a file as a part of its execution. Typically in the code that calls this 
	 * method, you should pass <code>this.getClass().getName()</code> as this parameter.
	 * @param propsFileName - name of the file containing the properties (just the file
	 * name - the path is assumed to be {@code "setup"}
	 * @return the Properties object containing the properties loaded from the file.
	 */
	public static Properties loadProperties (String className, String propsFileName) {
		Properties appProps = new Properties();
		BufferedReader propFile;
		
		File baseDir = getBaseDirectory(className);
		File parentDir = baseDir.getParentFile();
		String propsFilePath = null;
		if (parentDir!=null) {
			propsFilePath = parentDir.getPath(); 
			if ( !propsFilePath.endsWith(System.getProperty("file.separator")) ) {
				propsFilePath += System.getProperty("file.separator");
			}
			propsFilePath += REL_PATH_TO_PROPERTIES_FILE;
			if ( !propsFilePath.endsWith(System.getProperty("file.separator")) ) {
				propsFilePath += System.getProperty("file.separator");
			}
			propsFilePath += propsFileName;

			/* Reading properties from properties file */
			File propsFileHandle = new File(propsFilePath);
			if ( propsFileHandle != null  &&  propsFileHandle.exists() ) {
				try {
					propFile = new BufferedReader(new FileReader(propsFileHandle));
					appProps.load(propFile);
					propFile.close();
					propFile = null;
				}
				catch (IOException e){
					System.err.println("Unable to read properties from file. " + e.getMessage());
				}
			}
			/* Done reading properties from the file */
		}
		
		return appProps;
	}
	
	public static String getRoleCodeFromModuleName (String moduleName) {	

		String roleCode = null;
		if (moduleName.equalsIgnoreCase("Program Competing")){
			roleCode = "PC";
		}
		if (moduleName.equalsIgnoreCase("Program Non Competing")){
			roleCode = "PNC";
		}
		if (moduleName.equalsIgnoreCase("Specialist Competing")){
			roleCode = "SC";
		}
		if (moduleName.equalsIgnoreCase("Specialist Non Competing")){
			roleCode = "SNC";
		}
		return roleCode;
	}
}
