$(document).ready(function(){
     $(".chosen-select").chosen(); //for cancer activities dropdown
   

   //customization of Data Tables

  $( ".no-sort" ).removeClass( ".sorting" );

var table = $('#example').dataTable( {

    "bSort": true,
    "bFilter": false,

  "dom": '<"top"lip>rt<"bottom"lip>',
    "buttons": [
        'copy', 'excel', 'pdf'
    ],

   "order": [[ 4, "asc" ]],

    "language": {
                "info": "&nbsp; (Displaying _END_ out of _TOTAL_ )  ",
              },
   
       columnDefs: [
         { targets: ['status'], type: 'alt-string'},
 
         { targets: 'no-sort', orderable: false }] 



    } );






 

$('[data-toggle="tooltip"]').tooltip();    //tooltip on icons    



 


 });  

 








