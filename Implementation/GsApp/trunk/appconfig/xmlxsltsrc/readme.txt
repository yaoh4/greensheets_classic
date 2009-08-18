This note explains the absence of certain files that "used to be" in this directory, 
in case a very astute observer notices their "disappearance" and becomes alarmed.

Specifically - prior to Aug. 18, 2009, the following 6 files used to exist in this 
directory:

PC_Questions.xml
PNC_Questions.xml
SC_Questions.xml
SNC_Questions.xml
DC_Questions.xml
DNC_Questions.xml

We are removing these files today because we determined, by analyzing the application's 
source code and by thorough testing, that they are not required to be within the WAR 
file that gets deployed to the J2EE application server. 
Previously, we assumed that these files had to be updated any time files with the same 
names were updated in the respective directory of the Swing (rather than J2EE) 
GsQuestions application.  Because the content of these files changes when questions and/
or answer options are added or changed on greensheet forms, we used to believe that we 
had to build and deploy a new WAR file any time a new question was added, even though the 
functionality of the application did not change at all.  This was causing many unnecessary
deployments of the WAR file to the server when in fact only changes in database records 
would suffice. See issue GREENSHEETS-298 in NCI's Jira issue tracking system for more 
information.

- Anatoli Kouznetsov, 
  Abdul Latheef Jamal Mohamed
