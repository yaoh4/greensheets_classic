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


//var $regexname=/^([a-zA-Z]{3,16})$/;
  //  $('.name').on('keypress keydown keyup',function(){
          //   if (!$(this).val().match($regexname)) {
              // there is a mismatch, hence show the error message
                 // $('.emsg').removeClass('hidden');
                 // $('.emsg').show();
             // }
           // else{
                // else, do not display message
                // $('.emsg').addClass('hidden');
               // }
         // });



//function for making year blank when serial number is entered

$('#serial').on('keypress change', function() {
if($('#serial').val().length > 0) {
  $('#year').removeAttr('placeholder');
}
else {
 $('#year').attr('placeholder', '01');
}

});



$('[data-toggle="tooltip"]').tooltip();    //tooltip on icons    

//function for copy and paste Grant Number
var $inputs = $("#GrantNumber input"); // grab inputs from grant number input boxes //
    maxlengthArray = []; // array to store max lengths of all input boxes in grant row //


    


    $($inputs).each(function(index) { // loop through each input and push max length of input to maxLengthArray //
        maxlengthArray.push($inputs[index].maxLength)
    });

    $("#GrantNumber input#first").on("paste", function() { 
        $inputs.attr("maxlength", 19);
        var $this = $(this);
      

        $this.one("input.fromPaste", function(){
        
            $currentInputBox = $(this); // grab pasted value //
            pastedValue = $currentInputBox.val().replace("-","");
     
            // set regular expression here //
            if ((pastedValue.length >=14 && pastedValue.length <= 19 && pastedValue.indexOf("CA") >= 0)) {
                console.log(pastedValue.length)
                setMaxValues();
                var counter = 0;
                $(maxlengthArray).each(function(index) {
                   $inputs[index].value = pastedValue.slice(counter,counter+maxlengthArray[index]);
                   counter += maxlengthArray[index];
                    
                });
            }
            else {
                $(maxlengthArray).each(function(index) {
                    $inputs[index].value = "";
                });
            };

            function setMaxValues() {
                $(maxlengthArray).each(function(index) {
                    $inputs[index].maxLength = maxlengthArray[index];
                });
            };


        });


    });
    
 







 });  

 








