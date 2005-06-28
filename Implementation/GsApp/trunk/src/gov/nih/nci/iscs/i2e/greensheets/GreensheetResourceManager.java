package gov.nih.nci.iscs.i2e.greensheets;

/**
 * Created by IntelliJ IDEA.
 * User: Jed
 * Date: Sep 7, 2003
 * Time: 12:56:19 PM
 * To change this template use Options | File Templates.
 */
public interface GreensheetResourceManager
{
  public static final int VMTEMPLATE_RESOURCE = 1;

  public String getResource(String id, int type) throws GreensheetsResourceException;
  
}
