This program was written in November-December 2013 in support of the CBIIT Science Management group's 
initiative to adopt Life Data Systems' product called FormBuilder for use in maintaining Greensheets 
"forms" (questionnaires). Specifically, it is meant to help with loading the content of "legacy" or 
"current Production" definitions of questions and forms into FormBuilder's library.  Besides that goal, 
this program's output will also help with NCI OGA's effort to review and redefine the current-state 
greensheet forms.

The program takes as inputs the main 6 question-definition XML files in "legacy Greensheets" format 
(i.e., PC_Questions.xml, SNC_Questions.xml, etc.). It also needs the plain-text files listing which 
type/mechanism combinations comprise which groups of forms (i.e., files like qlPC.txt and qlSNC.txt).

It essentially "transcodes" the information found in those input files and "writes" the same 
information presented in a different format - it writes a separate XML file for each "distinct form".
You can say that it transcodes the "question-centric" 6 files in "legacy Greensheets" format into as
many as necessary "form-centric" XML files, each of which defines a form as it would be experienced 
by an end user filling out the questionnaire. Within each XML file defining a form, the list of type/
mechanism combinations to which the form applies will be stated. Besides, the form's XML file will 
state how frequently the form is used (the number of times instances of such form were filled out 
during fiscal years 2009-2013 plus roughly the 1st 3 months of FY 2014).

Additionally, the program writes "summary files" (plain-text) for each category, listing each distinct
form discovered, its usage count, and applicability in terms of type/mechanism combinations. These 
summary files are called PC_FORMLIST.TXT, SNC_FORMLIST.TXT, etc.

It generates "transient artifacts" of the form PC_5_R01_qstns.xml - one for each combination of staff
role (program/specialist/DM), type and mechanism that are defined in qlPC.txt-style files. It generates 
them by applying the same XSLT transformation stylesheet to the same question-definition source file 
with different parameters in a loop. This results in a large number of files representing the form for
that exact combination of staff role, type, and mechanism, but many of those forms are identical as 
far as their question content is concerned.  So the system then does byte-by-byte comparison of these 
generated transient form files, and builds a library of "consolidated" forms - all identical forms are 
merged into one, whose applicability is made to reflect all type/mechs covered. 

Manual post-processing was performed at the end, upon form-definition XML files copied from 'output'
directory to 'post-processed'. It consisted of manually typing in the best-guess approximate description 
of grant category probably covered by a form with this applicability. It was added as <ApproxCategory>
XML elements. 

One more feature was added at a later time: if the program is run with command-line argument 
'-postProcess', it will go through the XML files in the 'post-processed' directory and write one tab-
separated summary file listing all the forms, including the manually typed in applicability. During 
that same loop the system also rewrites the post-processed files to make XML declaration appear on its
own line.  

Technical note... Effort was made to use the implementations of Java XML APIs supplied out of the box 
with JDK 7 - i.e, the intent was to have no dependencies on any 3rd-party libraries for XML processing 
- to depend strictly on what comes with the JDK.  
It worked in almost all respects - except pretty-printing the output (making nicely indented XML 
files). For the sake of making pretty-printing/indentation work, three JARs from the distribution of 
Apache Xerces 2 were added (xercesImpl.jar, serializer.jar, and xml-apis.jar). As a result, they seem 
to be picked up from the classpath over JDK-provided ones, but warnings are written to syserr about 
one property not being recognized by Xerces' parser. This warning seems harmless and can be ignored. 

It also uses Spring-core (and its dependencies), Spring-jdbc, and Oracle's JDBC drivers.

- Anatoli Kouznetsov 
