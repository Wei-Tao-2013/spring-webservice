package com.lp.connector.utils;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
//import com.sap.security.api.umap.system.ISystemLandscape;
import com.sap.security.api.umap.system.ISystemLandscapeObject;
import com.sap.tc.logging.Location;
import com.sap.tc.logging.Severity;
import com.sap.tc.logging.SimpleLogger;


public class ServiceLocator {
	private static final Location location = Location
			.getLocation(ServiceLocator.class);
	private static ServiceLocator instance = null;
	private static String SYSTEM_REF = "SAP_CRM";
	private static String JCO_REF = "SS_CRM_DATA";
	//private static IUserMapping userMapping; 
	private static ISystemLandscapeObject systemLandscapeObject;
	private static JCoDestination jCoDestination;
	
   protected ServiceLocator() {
      // Exists only to defeat instantiation.
   }
   
   public static ServiceLocator getInstance() {
     if(instance == null) {
         instance = new ServiceLocator();
         //userMapping = UMFactory.getUserMapping();	
 		// systemLandscapeObject = getSystemObject();
 		 jCoDestination = getJCODestination();		
     }
      return instance;
   }
   
  // public ISystemLandscapeObject getSystemLandscapeObject() {
	//   return(systemLandscapeObject);
   //}
   
   public JCoDestination getJavaConnectorObject() {
	   return(jCoDestination);
   }
   
  /* private static ISystemLandscapeObject getSystemObject() {
		String method = "getSystemObject()";
		//trace.entering(method);
		
		ISystemLandscape portalLandscape = UMFactory.getSystemLandscapeFactory().getLandscape(ISystemLandscape.TYPE_ENTERPRISE_PORTAL);
		if(portalLandscape == null) {
			trace.errorT("It seems like no Enterprise Portal is installed.");
			throw new ZLPCustomAuthException(Constants.SYSTEM_ERROR_RESPONSE_MSG, Constants.SYSTEM_ERROR);
			
			System.err.println("PCMAPPROVALERROR:Error during portal landscape setup.");
		}
		 
		ISystemLandscapeObject systemObject = null;
		 try {
			 systemObject = portalLandscape.getSystemByAlias(SYSTEM_REF);
			 System.err.println("PCMAPPROVALERROR:system object is retrieved successfully.");
		 }
		 catch(ExceptionInImplementationException e) {
			 // TODO log the stacktrace in the log
			 trace.errorT("An error occurred while retrieving the test system object.");
	 		 throw new ZLPCustomAuthException(Constants.SYSTEM_ERROR_RESPONSE_MSG, Constants.SYSTEM_ERROR);
			 System.err.println("PCMAPPROVALERROR:" + e.getMessage());
		 }
		 if(systemObject == null) {
		     trace.errorT("Mapping reference system does not exist in Enterprise Portal system landscape.");
	 		 throw new ZLPCustomAuthException(Constants.SYSTEM_ERROR_RESPONSE_MSG, Constants.SYSTEM_ERROR);
			 System.err.println("PCMAPPROVALERROR:System object is null");
		 }
		 
		 //trace.exiting(method);
		 return(systemObject);
	}*/
   
   /**
    * RFC user/password in CRC : XPA_JCO_RFC/Welcome1
    * @return
    */
   private static JCoDestination getJCODestination() {
   	JCoDestination jCoDestination = null;
   	try {
   
   		jCoDestination = JCoDestinationManager.getDestination(JCO_REF);
   		//System.out.println("PCMAPPROVALERROR:jcoDestination object is retrieved successfully.");
   	} catch(JCoException jCoException) {
   		// Log error message
   		SimpleLogger.traceThrowable(Severity.ERROR, location, "",
				jCoException);
   		
   	}
   	return(jCoDestination);
   }
}

