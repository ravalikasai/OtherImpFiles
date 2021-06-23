/**
 * This is an unpublished work containing 3M confidential and proprietary
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2018 - 3M. All rights reserved
 */

package com.mmm.cdms.distribute.sds.utils;

/**
 * information. Disclosure or reproduction without the written authorization of
 * 3M is prohibited. If publication occurs, the following notice applies:
 * 
 * Copyright 2011 - 3M. All rights reserved
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Locale.Builder;

import org.apache.log4j.Logger;

import com.mmm.cdms.distribute.sds.constants.DistributeSDSConstants;
import com.mmm.cdms.exceptions.DistributionException;

/**
 * Abstract class that has the utility methods for xml file read/DB initialize operations/ Logging
 * operations
 * 
 * @author Auraavamoudhan S
 * @version 1.0  21-Jan-2015
 */
public class DistributeSDSUtils {

	/**
	 * logger element for this object
	 */
	private static final Logger LOGGER = Logger.getLogger(DistributeSDSUtils.class);
	
	/**
	 * Load Property File
	 */
	public static String databaseConnectionURL;
	protected static Properties properties;
	public static String environment;
	
	/**
	 * Database connection
	 */
	private static Connection connection;
	
    /**
     * Database connection parameters
     */
    private String dbUserName;
    private String dbPassword;
    private String serverPort;
    private String databaseName;
    private String serverName;
	
	/**
	 * @param propertyFile
	 * @throws DistributionException 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static boolean initializeProperties(final String propertyFile) throws DistributionException {
		LOGGER.debug("initializeProperties - BEGIN");
		boolean arePropsInitialized = true;
		DistributeSDSUtils utils;
		try {
			utils = new DistributeSDSUtils();
			properties = loadProperties(propertyFile);
			if (properties != null && properties.size() != 0) {
	            String iniLocation = properties.getProperty("ini.file.location");
	            if (iniLocation != null && iniLocation.length() != 0) {
	            	utils.initializeDBProperties(iniLocation);
	            	initializeDB();
	            } else {
	                throw new FileNotFoundException("Could not locate **CDMSBatch.INI** file");
	            }
	        } else {
	            throw new DistributionException("Error while reading the properties file");
	        }
		} catch (IOException |ClassNotFoundException | SQLException allEx) {
			LOGGER.error("Properties Initialization Failed. Please check the properties file" + propertyFile);
			LOGGER.error(allEx.getMessage());
			arePropsInitialized = false;
		}
		LOGGER.debug("arePropsInitialized = " + arePropsInitialized);
		LOGGER.debug("initializeProperties - END");
		return arePropsInitialized;
	}

	/**
	 * Gets the database connection object
	 * 
	 * @return java.sql.Connection
	 * @throws SQLException 
	 */
	public static Connection getDBConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			try {
				initializeDB();
			} catch (ClassNotFoundException e) {
				LOGGER.error("DistributionException occured in getDBConnection", e);
			} catch (SQLException e) {
				LOGGER.error("DistributionException occured in getDBConnection", e);
			}
		}
		return connection;
	}

	/**
	 * retrieves the SQL statement from the meta data file
	 * 
	 * @param sqlID
	 *            Pass the sql id
	 * @return the corresponding sql statement
	 */
	public static String getPropertyValue(String name) {
		return properties.getProperty(name);
	}

	/**
	 * Initialize the database connection using the data configured in XML file 
	 * 
	 * @param dbElem  database metadata Element 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws DistributionException Any exception occurred 
	 */
	private static void initializeDB() throws SQLException, ClassNotFoundException {
		LOGGER.debug("initializing DB - BEGIN");
		String dbDriver = getPropertyValue(DistributeSDSConstants.DATABASE_DRIVER);
		try {
			Class.forName(dbDriver);
			connection = DriverManager.getConnection(databaseConnectionURL);
			LOGGER.debug("Database Initialized Successfully");
		} catch (SQLException sqlEx) {
			LOGGER.error("Database Initialization Failed. Initialization string: " + databaseConnectionURL);
			LOGGER.error(sqlEx.getMessage());
			throw sqlEx;
		} catch (ClassNotFoundException cnfEx) {
			LOGGER.error("Unable to open the Database Driver: " + dbDriver);
			LOGGER.error(cnfEx.getMessage());
			throw cnfEx;
		}
		LOGGER.debug("initializing DB - END");

	}
	
	/**
     * Method initializes the database properties taken from the ini
     * configuration file on the server. The path of the ini has to be mentioned
     * in the properties file
     * 
     * @throws DistributionException
     */
	public void initializeDBProperties(final String iniLocation) throws DistributionException{
		LOGGER.debug("Setting up the properties from ini file");
		try {
			Properties iniProps = loadProperties(iniLocation);
			environment = iniProps.getProperty("Environment");
			dbUserName = iniProps.getProperty("UserName");
			dbPassword = iniProps.getProperty("Password");
			serverName = iniProps.getProperty("SQLServerName");
			serverPort = iniProps.getProperty("SQLServerPort");
			databaseName = iniProps.getProperty("DistDB");
			if (dbUserName != null && dbPassword != null && serverName != null && serverPort != null
					&& databaseName != null) {
				databaseConnectionURL = "jdbc:sqlserver://" + serverName + ":" + serverPort + ";databaseName="
						+ databaseName + ";user=" + dbUserName + ";password=" + dbPassword
						+ ";encoding=utf-8;characterEncoding=UTF8;useUnicode=true";
				LOGGER.debug("Connection string good..moving ahead");
				LOGGER.debug("Using " + serverName + " Server and " + databaseName + "DB");
			} else {
				throw new DistributionException("Invalid connectionString. Parameters missing in the ini file");
			}
		} catch (DistributionException genericException) {
			LOGGER.error("DistributionException occurred while loading dbproperties from ini file : " + genericException.getMessage());
			LOGGER.error("Unable to obtain dbproperties", genericException);
			throw genericException;
		}
	}
	
	/**
	 * This method loads the properties for the application
	 * 	 
	 * @param filepath
	 * @return
	 */
	public static Properties loadProperties(String filepath) {
	    LOGGER.debug("Start - loading values from propertyfile");
		FileInputStream fileInputStream = null;
		Properties properties = new Properties();
		try {
			fileInputStream = new FileInputStream(filepath);
			properties.load(fileInputStream);
			fileInputStream.close();
		} catch (FileNotFoundException fileExc) {
			LOGGER.error("config.properties File not found " + fileExc.getMessage());
		} catch (IOException ioExc) {
			LOGGER.error("IO DistributionException occurred." + ioExc.getMessage());
		}
		LOGGER.debug("End - loading values from propertyfile");
		return properties;
	}	
	/**
	 * checks if the passed string is not null and not empty
	 * 
	 * @param checkString
	 * @return true if not null and not empty , false if not
	 */
	public static String trimString( String originalString){	
		if( null != originalString && originalString.length() > 0 ) {
			return originalString.trim();
		}
		return null;
	}	
	
	/**
	 * Builds a list of comma-separated values in a single String
	 * 
	 * @param list
	 * @return commasSeparatedList of Strings
	 */
	public static String convertIntoString(List<String> list) {
		StringBuilder commaSepValueBuilder = new StringBuilder();
		for ( int i = 0; i< list.size(); i++) {
			commaSepValueBuilder.append(list.get(i));
			if ( i != list.size()-1) {
				commaSepValueBuilder.append(", ");
			}
		}
		return commaSepValueBuilder.toString();	
	}

	/**
	 * Retrieves the current date time
	 * @return
	 */
	public static String getCurrentDateTime() {		
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        Date date = new Date();        
        return dateFormat.format(date);
    }

	/**
	 * Date formatter to display correct date on the document 
	 * cover letter
	 * 
	 * @param countryCode
	 * @param langCode
	 * @return
	 */
	public static String docDateFormatter(String countryCode, String langCode) {
		DateFormat dateFormat = null;
		Locale locale = new Builder().setLanguage(langCode).setRegion(countryCode).build();
		if (countryCode.equals("LV")) {
			dateFormat = new SimpleDateFormat(DistributeSDSConstants.DATE_FORMAT_01);
		} else {
			dateFormat = DateFormat.getDateInstance(DateFormat.DATE_FIELD, locale);
		}
		return dateFormat.format(new Date());
	}

}
