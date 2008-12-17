The point of this directory is to be the place where files DC_Questions.xml and DNC_Questions.xml are worked on.
They are worked on manually in several steps.
Once their creation is completed in this directory, these new versions are supposed to be manually copied to their "final" locations that correspond to: 
 - https://subversion.nci.nih.gov/svn/iscs/greensheets/Implementation/GsQuestions/trunk/xml
 and
 - https://subversion.nci.nih.gov/svn/iscs/greensheets/Implementation/GsApp/trunk/appconfig/xmlxsltsrc
.

The file Q_TypeMechs.xml is the product of the fisrt step - running a VBA routine in the Excel spreadsheet one level above (DM Greensheets Questions.xls).
After that, <ResponseDefs> are filled in manually using text editor, and the file is saved as DM_ResponseDefs.xml
Then, two copies of DM_ResponseDefs.xml are made - one as "DC_Questions before nesting.xml" and the other as "DNC_Questions before nesting.xml".
In the latter one, "DNC_Questions before nesting.xml", QuestionDef IDs and ResponseDef IDs are manually changed (via global search-and replace) from DMCQ_ to DMNCQ_, and SelectionDef IDs are changed from SEL_80xx to SEL_85xx.
Then copies of those are made as DC_Questions.xml and DNC_Questions.xml (which will be the final file names), and sub-questions are nested into <SelectionDef> elemensts of their parent questions using XMLspy (see the document "Nesting questions using XML Spy.doc") in the directory one level above.
