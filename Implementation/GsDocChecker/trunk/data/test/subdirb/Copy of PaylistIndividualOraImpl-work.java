package gov.nih.nci.numsix.grits.paylist.domain;

import gov.nih.nci.numsix.grits.appmechs.database.DbConnectionHelper;
import gov.nih.nci.numsix.grits.appmechs.excpthandling.GritsException;
import gov.nih.nci.numsix.grits.util.Constants;

import java.sql.*;
import java.util.*;

import oracle.jdbc.driver.OracleTypes;

import org.apache.log4j.Logger;

/**
 * @author David Fado
 * 
 * This class provides the Oracle implementation for storing and retrieving
 * paylist information.
 * 
 *  
 */
/*
 * 
 * 
 * @author pnagaraj
 *
 * TODO - This class has extensive refactoring. Logic is scattered all over.
 */
public class CopyOfPaylistIndividualOraImpl implements PaylistIndividualDac {

    Logger logger = Logger.getLogger(CopyOfPaylistIndividualOraImpl.class.getName());
   

    /**
     * This implementation that will return a Paylist and a set of Paylist Line
     * Items
     */

    public PaylistIndividualDo retrievePaylistIndividual(String paylistId,
            PaylistIndividualDo paylistIndividual, String currentFy, String currentRound) throws GritsException {

        logger.debug("Start retrievePaylistIndividual -----------------------");
        logger.debug("current fy="+currentFy+" current round="+currentRound);

        DbConnectionHelper connHelper = null;
        Connection conn = null;
        Statement stmt = null;
        String sqlPaylistGeneral = null;
        String sql = null;
        int i = 1000;
        Double tcObject = new Double(0.0);
        double totalCost = 0.0;
        double fundPlanValue = 0.0;
        boolean isCaPaylist = false;
        boolean isDefaultCost = false; //indicates if this can take a default
        int withinPaylineCount=0;
        Double workingPercentage = new Double(0.0);
        int fundPlanInt = 0;
        Double fundPlanDoub = new Double(0.0);
        String sort = null;
        String ecost = null;
        String fundPlanString = "";//basic string to use for translating
                                   // funding plan value to 100.

        //logger.debug("In Oracle Implementation for paylist details.");
         int allNullWithinPaylineCount = 0;
        Map map = new TreeMap(); //get ready to work with a collection
        ArrayList payList = new ArrayList(); //
        double payline = 0;
        double rscore = 0;
        long runningTotal = 0;
        long runningTotalEstimated = 0;
        ResultSet rs = null;
        //  ********************** SQL ********************

        //SQL for the Paylist

        sqlPaylistGeneral = "select * from nci.pat_vw where ID = " + paylistId;

        //SQL for the Individual Line items.
        sql = "select * from nci.plm_vw where PAT_ID = " + paylistId + " order by ranking_score";

        try {
            // Get an instance of the connection helper (the class is a
            // singleton)
            connHelper = connHelper.getInstance();
            // Get a connection from the pool....
            conn = connHelper.getConnection();

            // Create a statement....

            stmt = conn.createStatement();

            logger.debug("sending this sQL HERE " + sqlPaylistGeneral);
            stmt.execute(sqlPaylistGeneral);
            rs = stmt.getResultSet();

            //logger.debug("we should have this something back");

            int count = 0;
            while (rs.next()) {

                //First, do the general Paylist Information

                logger.debug("getting  paylist " + ++count);
                paylistIndividual.setPaylistType(rs.getString("PAYLIST_TYPE_DISPLAY_NAME"));
                paylistIndividual.setPaylistStatus(rs.getString("Paylist_status"));
                paylistIndividual.setFiscalYear(rs.getString("Fiscal_Year"));
                paylistIndividual.setPaylistId(paylistId);
                paylistIndividual.setDivision(rs.getString("div_non_description"));
                paylistIndividual.setCancerActivity(rs.getString("CAY_description"));
                paylistIndividual.setBudgetMech(rs.getString("BMM_Description"));
                paylistIndividual.setCostCenter(rs.getString("bmm_ccr_code"));
                
                paylistIndividual.setPercentileScore(rs.getDouble("PAYLINE_PERCENTILE"));
                paylistIndividual.setPriorityScore(rs.getDouble("PAYLINE_PRIORITY_SCORE"));
                paylistIndividual.setPayline(rs.getDouble("payline"));
                paylistIndividual.setRound(rs.getString("round"));
                paylistIndividual.setRunDate(rs.getDate("run_date"));
                
                paylistIndividual.setSubclass(rs.getString("BMM_SCN_CODE"));
                paylistIndividual.setRfapa(rs.getString("rfn_code"));
                paylistIndividual.setPaylistType(rs.getString("PAYLIST_TYPE_DISPLAY_NAME"));
                paylistIndividual.setMaxValueLarge(rs.getString("MAX_DIRECT_COST_AMT"));
                paylistIndividual.setMinValueLarge(rs.getString("MIN_DIRECT_COST_AMT"));

                //Set whether this is RFA/PA or CA based on the RFN_Code.
                if (paylistIndividual.getRfapa() == null) {
                    isCaPaylist = true;
                }
                logger.debug("what is the Rfapa?  " + paylistIndividual.getRfapa());

                logger.debug("we have set the paylist it is CA " + isCaPaylist);

                //if RFA PA Paylist, then go to the separate method to get
                // total from stored procedure.

                if (!isCaPaylist) {

                    paylistIndividual = this.retrieveTotalRfa(paylistIndividual);

                }//End Add total for RFAPA paylist

                //Determine if Default Values

                //SET Defaults
                //If coming from paylist Details, will take defaults,
                //Otherwise, it will not.
                logger.debug("$$$$$$$$$$$$$ isDefaultCost = "+paylistIndividual.getIsDefaultCost());
                if (paylistIndividual.getIsDefaultCost()) {
                    isDefaultCost = true; //we will do default cost here.

                }

                //If RFA PA, don't do default.
                if (!isCaPaylist) {
                    logger.debug("not in a CA paylist");
                    isDefaultCost = false;
                    paylistIndividual.setIsDefaultCost(false);
                }
                logger.debug("this is our default cost instructions " + isDefaultCost);

            }
            //END BUILDING GENERAL PAYLIST INFORMATION

            //Start Paylist LIne Items
            logger.debug("sending this sQL " + sql);
            stmt.execute(sql);
            //logger.debug("we have a real line item");
            rs = stmt.getResultSet();

            while (rs.next()) {
                logger.debug("\n\n\n~~~~~~~~\n");

                // Test initial for putting item on object.
                PaylistLineItemDo paylistLineItem = new PaylistLineItemDo();

                paylistLineItem.setCodingStatus("comp");
                paylistLineItem.setComment("No Comment Yet!");
                paylistLineItem.setApplId(rs.getString("appl_id"));
                paylistLineItem.setInvestigator(rs.getString("LAST_NAME"));
                paylistLineItem.setRank(rs.getDouble("ranking_score"));
                paylistLineItem.setComment(rs.getString("plm_comments"));
                logger.debug("Comment is = "+paylistLineItem.getComment());
                String tdc = rs.getString("Total_direct_cost");

                //paylistLineItem.setDirectCost(rs.getInt("Total_direct_cost"));
                paylistLineItem.setDirectCost(tdc == null ? (int) Constants.gritsNull : Integer
                        .parseInt(tdc));

                //paylistLineItem.setTotalCostPolicy(rs.getInt("total_cost_with_policy"));
                String tcwp = rs.getString("total_cost_with_policy");
                paylistLineItem.setTotalCostPolicy(tcwp == null
                        ? (int) Constants.gritsNull
                        : Integer.parseInt(tcwp));

                paylistLineItem.setEstIndirectCost(rs.getDouble("Estimated_IDC_rate"));
                //paylistLineItem.setTotalCostPolicy(rs.getInt("total_cost_with_policy"));

                paylistLineItem.setExceptionStatus(rs.getString("Exception_Requested_Flag"));

                //*****Set the Funding Policy.
                //logger.debug("set the general information, now the
                // celebrity.");

                if (rs.getString("funding_policy_pct") != null) {

                    //Translate the data values, multiplying by 100 for
                    // percent.

                    fundPlanValue = rs.getDouble("funding_policy_pct") * 100;

                    logger.debug("Translating the value of the string" + fundPlanValue);

                    Double fundDouble = new Double(fundPlanValue);

                    //get rid of the zeros
                    fundPlanInt = fundDouble.intValue();

                    //Turn to a String
                    fundPlanString = fundPlanString.valueOf(fundPlanInt);

                    //reset to the string
                    // logger.debug("translated the string to int");

                    //Set on the bean.
                    paylistLineItem.setFundPolicy(fundPlanString);

                    logger.debug("here is the funding plan " + fundPlanString);

                } else {
                    //Do we want to indicate we are setting the default here on
                    // the page?

                    paylistLineItem.setFundPolicy("100");

                }
                String fyrs = rs.getString("total_future_years");
                logger.debug("total_future_years = " + fyrs);

                //paylistLineItem.setFutureYears(rs.getInt("total_future_years"));
                paylistLineItem.setFutureYears(fyrs == null ? (int) Constants.gritsNull : Integer
                        .parseInt(fyrs));
                paylistLineItem.setGrantNumber(rs.getString("Full_grant_num"));

                //Set BOOLEANS
                // *************************************************************

                //Is Comment Existing

                paylistLineItem.setIsComment(false);
                if (rs.getString("PLM_COMMENTS_FLAG").equalsIgnoreCase("Y")) {

                    paylistLineItem.setIsComment(true);
                } else {
                    paylistLineItem.setIsComment(false);
                }
                //logger.debug("set Comment flag");

                //Official Pay Status
                if ((rs.getString("Official_Pay_Status") != null)
                        && (rs.getString("Official_Pay_Status").equalsIgnoreCase("Y"))) {
                    paylistLineItem.setIsOfficialSelectforPay(true);
                } else {
                    paylistLineItem.setIsOfficialSelectforPay(false);
                }
                //logger.debug("set Pay flag");
                //Setting is within Payline Here. @toDo, put in the business
                // services layer.
                rscore = rs.getDouble("Ranking_score");
              //  if ((rscore <= paylistIndividual.getPayline())&&(rscore > 0) || paylistIndividual.getPaylistType().equalsIgnoreCase("Non Percentile"))
                 if(paylistIndividual.getPayline()<=0 || (rscore >0 && rscore <=paylistIndividual.getPayline()))       {

                    paylistLineItem.setIsWithinPayline(true);
                } else {
                    paylistLineItem.setIsWithinPayline(false);
                }

                logger.debug("---------------------");
                logger.debug("ranking score = "+rscore+" payline=" + paylistIndividual.getPayline() + " withinpayline="+ paylistLineItem.getIsWithinPayline());
                logger.debug("---------------------");
                //Set for RFA Paylist
                if (!(isCaPaylist)) {
                    paylistLineItem.setIsWithinPayline(true);
                }
                //end if CA paylist (RFA PAYLIST are within payline.
                //logger.debug("Ready to derive cost");

                //SETTING THE TOTAL COST WITH POLICY (DERIVED)
                //will change to come from the database.

                workingPercentage = workingPercentage.valueOf(paylistLineItem.getFundPolicy());
                
                
                        String estpgmcost = rs.getString("est_pgm_tot_cost_rcmd");
                        logger.debug("estpgmcost = " + estpgmcost);
                        if(estpgmcost==null){
                        paylistLineItem.setEstimatedProgramRecommendedCost((long) Constants.gritsNull);
                        }else{
                          paylistLineItem.setEstimatedProgramRecommendedCost(Long.parseLong(estpgmcost));
                        }
                        String payCode = rs.getString("PAYSELECT_DESCRIPTION");
                        
                        paylistLineItem.setPayCode(payCode);
                        

                // Policy
                //ADD THE TOTAL TO THE RUNNING TOTAL ON THE GENERAL BEAN, only
                // if WITHIN PAYLINE
                //DEFAULT ESTIMATED COST IF TOTAL COST IS 0

                // SET ESTIMATED COST ACCORDING TO DEFAULT IF WITHIN Payline AND
                // Is taking a default
                //These two procedures ONLY for things within the payline
                if (paylistLineItem.getIsWithinPayline()) {
                     if(paylistLineItem.getTotalCostPolicy()!=Constants.gritsNull){
                    runningTotal = runningTotal + paylistLineItem.getTotalCostPolicy();
                        logger.debug("runningTotal = "+runningTotal);
                     }
                   // runningTotalEstimated = runningTotalEstimated + paylistLineItem.getEstimatedProgramRecommendedCost();

                    //Now set default values.

                    //This is a variable to do the was null record below.

                    long y = rs.getLong("est_pgm_tot_cost_rcmd");

                    logger.debug("out estimated cost is " + y);

                    //If the program cost was null, get the default Cost.
                    //before if, reset individual to false

                    paylistIndividual.setIsDefaultCost(false);
                    if ((rs.wasNull()) && (isDefaultCost)) {
//                      If editable page the default the Estimated Recommened cost   -else leave it as it is from database. //Bug 3310 fix
                        
                        if ((paylistIndividual.getPaylistStatus().equalsIgnoreCase("No Approvals")) || (paylistIndividual.getPaylistStatus().equalsIgnoreCase("Program Approved"))){
                            
                        paylistLineItem.setEstimatedProgramRecommendedCost(paylistLineItem
                                .getTotalCostPolicy());
                        }
                        paylistLineItem.setIsChanged(true);

                        paylistLineItem.setIsDefaultEstProg(true); //indicate
                                                                   // this is a
                                                                   // derived
                                                                   // value
                        paylistIndividual.setIsDefaultCost(true); //if any of
                                                                  // the line
                                                                  // items are
                                                                  // default,
                                                                  // the whole
                                                                  // paylist has
                                                                  // the default
                                                                  // flag true.
                        withinPaylineCount++;
                        //this is also why we have a local variable
                        // "isDefaultCost for work flow here.
                    } /*else {
                        String estpgmcost = rs.getString("est_pgm_tot_cost_rcmd");
                        logger.debug("estpgmcost = " + estpgmcost);
                        if(estpgmcost==null){
                        paylistLineItem.setEstimatedProgramRecommendedCost((int) Constants.gritsNull);
                        }else{
                          paylistLineItem.setEstimatedProgramRecommendedCost(Integer.parseInt(estpgmcost));
                        }
                        //rs.getInt("est_pgm_tot_cost_rcmd"));

                    }*/
                    

                    //SET PAYCODE TO PAY IF WITHIN PAYLINE AS DEFAULT for CA
                    // ONLY only for isDefaultCost

                   
                    logger.debug("estimated program rec cost set to = "
                            + paylistLineItem.getEstimatedProgramRecommendedCost());

                    //END SET ESTIMATED COST

                    //Add to the total if within payline - only if EstimatedProgramRecommendedCost is not gritsNull (= -9999999999999)
                    if(paylistLineItem.getEstimatedProgramRecommendedCost()!=Constants.gritsNull&&
                        (paylistLineItem.getPayCode()!=null && paylistLineItem.getPayCode().equalsIgnoreCase("Pay"))){
                    
                    runningTotalEstimated = runningTotalEstimated
                            + paylistLineItem.getEstimatedProgramRecommendedCost();
                    }
                            
                
                }//end if things are within Payline manipulation.

                //Commented the following code - Jims Bug - Pals April 26th
                // 2004
                /*
                 * if ((paylistLineItem.getPayCode() == null) &&
                 * (isDefaultCost)) {
                 * 
                 */
                
               
//                 if (isCaPaylist && paylistLineItem.getIsWithinPayline() && 
//                         (paylistLineItem.getPayCode() == null)) {
//                     paylistLineItem.setPayCode("Pay");
//                 }
                
              if (isCaPaylist && paylistLineItem.getIsWithinPayline() && 
              (paylistLineItem.getPayCode() == null)&& (paylistIndividual.getIsDefaultCost())) {
          paylistLineItem.setPayCode("Pay");
      }                
                 

                //FIGURE out PROGRAM REC FUTURE YEARS - Removed this logic
                // after Jims Bug-Pals April 26th. 2004
                /*
                 * if ((rs.getInt("pgm_rcmd_future_years") == 0) &&
                 * (isDefaultCost)) { paylistLineItem.setProgramRecFutureYears(
                 * rs.getInt("Total_Future_Years")); } else {
                 * paylistLineItem.setProgramRecFutureYears(
                 * rs.getInt("pgm_rcmd_future_years")); }
                 */
                //set up sorting to return in the current order.
                String pgmFuYrs = rs.getString("pgm_rcmd_future_years");
                
                
                if(pgmFuYrs != null){
                        
                    paylistLineItem.setProgramRecFutureYears(Integer.parseInt(pgmFuYrs));
                    
                }else{
                    paylistLineItem.setProgramRecFutureYears((int)Constants.gritsNull);
                }
//                logger.debug("pgm_rcmd_future_years = " + pgmFuYrs);
//                int pgmFuYrsInt = pgmFuYrs == null ? (int) Constants.gritsNull : Integer
//                        .parseInt(pgmFuYrs);
//                paylistLineItem.setProgramRecFutureYears(pgmFuYrsInt);
//                paylistLineItem.setPaylistSortNumber(i);
//                Abdul - commented the following code
                if (isCaPaylist && paylistLineItem.getIsWithinPayline() && 
                        (paylistLineItem.getPayCode().equalsIgnoreCase("Pay"))&& (pgmFuYrs == null)) {
                    //If editable page the default the future years  -else leave it as it is from database. bug 3310 fix.
                    if ((paylistIndividual.getPaylistStatus().equalsIgnoreCase("No Approvals")) || (paylistIndividual.getPaylistStatus().equalsIgnoreCase("Program Approved"))){
                        
                    paylistLineItem.setProgramRecFutureYears(paylistLineItem.getFutureYears());
                    }
                }               
                
//                 if (paylistIndividual.getIsDefaultCost()) {
//                    paylistLineItem.setProgramRecFutureYears(paylistLineItem.getFutureYears());
//                }
 
                // Abdul - code ends here
                //if pay code is not 'Pay' then set the EstimatedProgram Rec
                // cost & progrem Rec futures years to Null; = Pals Jun 2 04
                this.checkPayCode(paylistLineItem);
                logger.debug("Abdul --- Estimated PGM Rec="+paylistLineItem.getEstimatedProgramRecommendedCost()+" paycode="+paylistLineItem.getPayCode()+" pggFuYrs="+paylistLineItem.getProgramRecFutureYears());
                //set the isDefaultCost  - Pals Jan 14th. Bug 3342 fix.  Done here at the end to keep the logic intact. Needs refactoring.
               if(isCaPaylist && paylistLineItem.isWithinPayline()){
                   logger.debug("Estimated PGM Rec="+estpgmcost+" paycode="+payCode+" pggFuYrs="+pgmFuYrs);
                    if(estpgmcost==null && payCode==null && pgmFuYrs==null){ //If all three values are null & isDefaultCost is not set to true...
                     
                    allNullWithinPaylineCount++;                                                        //...set isDefaultCost to true - Pals Jan 14th 
                    }
                    }
                
                
                sort = sort.valueOf(i);
               
                i++; //update i for the input ranking number.

                map.put(sort, paylistLineItem); //build the collection.
                logger.debug("~~~~~~~~\n");
                //Show Assign button only if the paylist fy/round == currentFy/currentRound and has NO Approval status.
                 if(currentFy.equals(paylistIndividual.getFiscalYear()) && 
                            currentRound.equals(paylistIndividual.getRound()) && 
                            paylistIndividual.getPaylistStatus().equalsIgnoreCase("No Approvals")){
                    paylistLineItem.setAssignable(true);
                }else{
                    paylistLineItem.setAssignable(false);
                }
                 logger.debug("cfy=fy="+currentFy.equals(paylistIndividual.getFiscalYear()));
                 logger.debug("cround=round="+currentRound.equals(paylistIndividual.getRound()));
                 logger.debug("No Approvals="+paylistIndividual.getPaylistStatus().equalsIgnoreCase("No Approvals"));
                 logger.debug("cfy="+currentFy+" cround="+currentRound+" fy="+paylistIndividual.getFiscalYear()+" round="+paylistIndividual.getRound()+" assignable="+paylistLineItem.isAssignable()+
                           " paylist status = "+paylistIndividual.getPaylistStatus());
                 
            }//While loop end

            //Update the paylist metadata: SET PAYLIST GENERAL NUMBERS

            //IF RFA/PA, total cost is dervied and taken from database

            if (!isCaPaylist) {
                //set running total to equal database value
                runningTotal = paylistIndividual.getTotalCost();

            }
            if(isCaPaylist && allNullWithinPaylineCount>0){
                paylistIndividual.setIsDefaultCost(true);
            }else{
                paylistIndividual.setIsDefaultCost(false);
            }
            
            logger.debug("Default cost set to ="+paylistIndividual.getIsDefaultCost());
            //Set Total Cost
            
            logger.debug("final runningTotal ="+runningTotal);
            paylistIndividual.setTotalCost(runningTotal);
            //Set Estimated Total
            paylistIndividual.setTotalCostEst(runningTotalEstimated);

            //Set the Balance
            paylistIndividual.setBalance(runningTotal - runningTotalEstimated);
 
            //Set the number of grants

            paylistIndividual.setTotalGrants(map.size());
            logger.debug("total grants = " + map.size() + " " + paylistIndividual.getTotalGrants());
            //*****End Setting Paylist General NUMBERS

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            logger.debug("we have a SQL Exception that is " + sqle.getMessage());
            //stmt.close();//adding to check
            throw new GritsException("error in retrievePaylistIndividual", sqle);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error during retrieval", e);
            throw new GritsException("error in retrievePaylistIndividual",e);
        }

        finally {

            try {

                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();

                connHelper.freeConnection(conn);

            } catch (Exception e) {

                logger.debug(e.getMessage());
                throw new GritsException("error in retrievePaylistIndividual",e);

            }

        } //end finally

        //mocked up way to generate additional paylists. @kluge, not permanent.
        Integer numberPaylists = new Integer(paylistId);
        int pItems = numberPaylists.intValue();

        payline = paylistIndividual.getPayline();

        paylistIndividual.setPaylistLineItems(map);

        logger.debug("End retrievePaylistIndividual -----------------------");

        return paylistIndividual;

    }

    /**
     * Method retrieveTotalRfa.
     * 
     * @param paylistIndividual
     * @return PaylistIndividualDo
     */
    private PaylistIndividualDo retrieveTotalRfa(PaylistIndividualDo paylistIndividual)
            throws SQLException, GritsException {

        logger.debug("Start retrieveTotalRfa -----------------------");

        DbConnectionHelper connHelper = null;
        Connection conn = null;
        CallableStatement cstmt = null;
        String rfnCode = null;
        String fiscalYear = null;
        boolean isStored = false;

        //Set the Paylist ID for this Paylist

        rfnCode = paylistIndividual.getRfapa();
        fiscalYear = paylistIndividual.getFiscalYear();

        //******************CREATE PL SQL*******************

        String callableStore = "begin ? := paylist_pkg.get_rfapa_paylist_target(?,?); end;";

        try {
            // Get an instance of the connection helper (the class is a
            // singleton)
            connHelper = connHelper.getInstance();
            // Get a connection from the pool....
            conn = connHelper.getConnection();

            //prepare callable Statement
            cstmt = conn.prepareCall(callableStore);
            cstmt.registerOutParameter(1, OracleTypes.NUMBER);
            cstmt.setString(2, rfnCode);
            cstmt.setString(3, fiscalYear);

            logger.debug("Set Parameters, Ready to Execute");

            cstmt.execute();

            logger.debug("executed");

            //Put the data onto the bean.

            paylistIndividual.setTotalCost(cstmt.getInt(1));

            logger.debug("leslie says this will work!");

            conn.commit();

            logger.debug("Commited " + paylistIndividual.getTotalCost());

        } //end try
        catch (SQLException sqle) {

            logger.debug("we have a SQL Exception that is " + sqle.getMessage());
            cstmt.close();//adding to check
            conn.rollback();//adding to check
            throw new GritsException("error in retrieveTotalRfa",sqle);

        } catch (Exception e) {
            String debugMsg = "Some Exception here: " + e.getMessage();

            throw new GritsException("error in retrieveTotalRfa",e);
        } finally {

            cstmt.close();
            //conn.rollback();
            logger.debug("looks closed to me");

            connHelper.freeConnection(conn);

        } //end finally

        logger.debug("End retrieveTotalRfa -----------------------");
        return paylistIndividual;
    }

    /** @modelguid {DC93CE0F-01A5-4577-98D2-EB9558B69CEE} */
    public boolean storePaylistIndividual(PaylistDone paylistDone, String appUser) throws GritsException {

        logger.debug("\n\n\n------Start storePaylistIndividual-----------------");

        DbConnectionHelper connHelper = null;
        Connection conn = null;
        CallableStatement cstmt = null;
        String paylistId = null;
        String userId = "fadod";
        boolean isStored = false;

        //Set the Paylist ID for this Paylist

        paylistId = paylistDone.getPat_id();

        //******************CREATE PL SQL*******************

        String callableStore = "begin nci.Paylist_PKG.upd_paylist_grant(?,?,?,?,?,?,?,?,?); end;";

        try {
            // Get an instance of the connection helper (the class is a
            // singleton)
            connHelper = connHelper.getInstance();
            // Get a connection from the pool....
            conn = connHelper.getConnection(appUser);
            conn.setAutoCommit(false);

            //prepare callable Statement
            cstmt = conn.prepareCall(callableStore);

            //Get the Map
            Map paylistLineItems = new TreeMap();
            paylistLineItems = paylistDone.getPaylistLineItems();

            //GET THE ITERATOR FOR THE DONE MAP AND SET ON CSTM THEN SEND

            /*******************************************************************
             * p_appl_id paylist_line_items_t.appl_id%TYPE ,p_pat_id
             * paylist_line_items_t.pat_id%TYPE ,p_est_pgm_tot_cost_rcmd
             * paylist_line_items_t.est_pgm_tot_cost_rcmd%TYPE
             * ,p_pgm_rcmd_future_years
             * paylist_line_items_t.pgm_rcmd_future_years%TYPE DEFAULT NULL
             * ,p_payselect_flag paylist_line_items_t.payselect_flag%TYPE
             * ,p_comments paylist_line_items_t.comments%TYPE ,p_por_flag
             * nci_appl_elements_t.por_flag%TYPE DEFAULT NULL
             * ,p_basic_percentage nci_appl_elements_t.basic_percentage%TYPE
             * DEFAULT NULL ,p_applied_percentage
             * nci_appl_elements_t.applied_percentage%TYPE DEFAULT NULL);
             *  
             ******************************************************************/

            Collection paylistItems = paylistLineItems.values();
            Iterator it = paylistItems.iterator();
            
            while (it.hasNext()) {

                PaylistLineItemDone paylistLineItemDone = new PaylistLineItemDone();
               
                paylistLineItemDone = (PaylistLineItemDone) it.next();

                cstmt.setString(1, paylistLineItemDone.getAppl_id());
                logger.debug("PAT ID " + paylistId + "est Prog Cost"
                        + paylistLineItemDone.getEst_pgm_tot_cost_rcmd());
                cstmt.setString(2, paylistId);
                // cstmt.setInt(3,100000);
                logger.debug("Estimated Program Cost = "
                        + paylistLineItemDone.getEst_pgm_tot_cost_rcmd() + " pgm rcmd f yrs "
                        + paylistLineItemDone.getPgm_rcmd_future_years());

                logger.debug("Pay code = " + paylistLineItemDone.getPayselect_flag());

                if (!(paylistLineItemDone.getPayselect_flag().equalsIgnoreCase("Pay"))) {
                    cstmt.setNull(3, Types.INTEGER);
                   
                } else if (paylistLineItemDone.getEst_pgm_tot_cost_rcmd() == Constants.gritsNull) {
                    cstmt.setNull(3, Types.INTEGER);
                    

                } else {
                    cstmt.setLong(3, paylistLineItemDone.getEst_pgm_tot_cost_rcmd());
                    logger.debug("Est Pgm Tot Rec Cost set to = "
                            + paylistLineItemDone.getEst_pgm_tot_cost_rcmd());

                }

                if (!(paylistLineItemDone.getPayselect_flag().equalsIgnoreCase("Pay"))) {
                    cstmt.setNull(4, Types.INTEGER);
                } else if (paylistLineItemDone.getPgm_rcmd_future_years() == Constants.gritsNull) {
                    cstmt.setNull(4, Types.INTEGER);
                } else {
                    cstmt.setInt(4, paylistLineItemDone.getPgm_rcmd_future_years());
                }

                //translate to one character for the database.

                if (paylistLineItemDone.getPayselect_flag().equalsIgnoreCase("Pay")) {
                    cstmt.setString(5, "Y");
                }
                if (paylistLineItemDone.getPayselect_flag().equalsIgnoreCase("Skip")) {
                    cstmt.setString(5, "N");
                }
                if (paylistLineItemDone.getPayselect_flag().equalsIgnoreCase("Defer")) {
                    cstmt.setString(5, "D");
                }
                if (paylistLineItemDone.getPayselect_flag().equalsIgnoreCase("")) {
                    cstmt.setString(5, "");
                } //end setting Pay select flag as one character.
                logger.debug("\n\n\n\n comments = "+paylistLineItemDone.getComments());
                cstmt.setString(6, paylistLineItemDone.getComments());
                cstmt.setString(7, "");
                cstmt.setString(8, "");
                cstmt.setString(9, "");
                //   cstmt.setString(10, "");

                //cstmt.setString(11, ""); 

                cstmt.execute();

            } // end while

            conn.commit();

        } //end try
        catch (SQLException sqle) {

           
            throw new GritsException("error in storePaylistIndividual",sqle);

        } catch (Exception e) {
            String debugMsg = "Some Exception here: " + e.getMessage();

            throw new GritsException(e);
        } finally {

            try {
                cstmt.close();//adding to check
               // conn.rollback();//adding to check

            } catch (SQLException e1) {
                throw new GritsException("error in storePaylistIndividual",e1);
            }  
            
            connHelper.freeConnection(conn);

        } //end finally

        isStored = true;

        logger.debug("------------  End storePaylistIndividual-----------------");

        return isStored;
    }

    /*This method check if the paycode value is not "Pay" ("Skip" or "Defer") and sets null values for Estimated Program Recommended cost 
     * & program recommended future years.
     */
    void checkPayCode(PaylistLineItemDo paylistLineItem) {
        //paylistLineItem.getPayCode() may return null - in case of paylist outside payline. So check if it is null.
        logger.debug("paycode = " + paylistLineItem.getPayCode());
        logger.debug("Estimated P Rec Cost = "
                + paylistLineItem.getEstimatedProgramRecommendedCost());
        if (((paylistLineItem.getPayCode() != null) && !(paylistLineItem.getPayCode()
                .equalsIgnoreCase("Pay")))) {

            paylistLineItem.setEstimatedProgramRecommendedCost((int) Constants.gritsNull);
            paylistLineItem.setProgramRecFutureYears((int) Constants.gritsNull);
            
        }
         
      paylistLineItem.setIsEprcValid(true);
      paylistLineItem.setIsprfyValid(true);
    }

}
