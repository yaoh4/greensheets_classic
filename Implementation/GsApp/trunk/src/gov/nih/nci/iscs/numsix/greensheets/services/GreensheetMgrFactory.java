/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */


package gov.nih.nci.iscs.numsix.greensheets.services;


import gov.nih.nci.iscs.i2e.greensheets.*;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetresourcemgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*;


/**
 * Factory class used to generate application services managers.
 * These managers are used to provides specific services to calling clients. The
 * factory returns and interface for each Manager. A type is required to determine 
 * which interface implementation to return.
 * 
 *  @author kpuscas, Number Six Software
 */
public class GreensheetMgrFactory {
    
    

    
    public static final int TEST = 1;
    public static final int PROD = 2;
    
    
     
    private GreensheetMgrFactory(){
    }
    
    
    public static GrantMgr createGrantMgr(int type){
        
        if(type == TEST){
            return new GrantMgrTestImpl();
        }else{
            return new GrantMgrImpl();
        }
        
    }
       
     public static GreensheetFormMgr createGreensheetFormMgr(int type){
        
        if(type == TEST){
            return new GreensheetFormMgrTestImpl();
        }else{
            return new GreensheetFormMgrImpl();
        }
        
    }   
    
    
    public static GreensheetUserMgr createGreensheetUserMgr(int type) {
        
        if(type == TEST){
            return new GreensheetUserMgrTestImpl();
        }else{
            return new GreensheetUserMgrImpl();
        }
        
        
    }    
    
    public static GreensheetResourceManager createGreenSheetResourceMgr(int type) {
        
        if(type == TEST){
            return new GreensheetResourceManagerTestImpl();
        }else{
            return new GreensheetResourceManagerImpl();
        }
        
        
    }    
    
    
    
}

