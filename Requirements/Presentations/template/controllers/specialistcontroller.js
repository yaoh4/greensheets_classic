$(document).ready(function(){
    $(".chosen-select").chosen(); // calles chosen plugin that is for the multiselect drop downs 
   

// All related to Data Tables plugin //
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

// For ability to paste into Grant Number box  -- I still need to add the regular expression to check the pattern //

    var $inputs = $("#GrantNumber input"); // grab inputs from grant number input boxes //
    maxlengthArray = []; // array to store max lengths of all input boxes in grant row //
 
    $($inputs).each(function(index) { // loop through each input and push max length of input to maxLengthArray //
        maxlengthArray.push($inputs[index].maxLength)
    });
 
    $("#GrantNumber input#Type").on("paste", function() { 
        $inputs.attr("maxlength", 19);
        var $this = $(this);
 
        $this.one("input.fromPaste", function(){
            $currentInputBox = $(this); // grab pasted value //
            pastedValue = $currentInputBox.val().replace("-","");
     
            // set regular expression here //
            if ((pastedValue.length >=15 && pastedValue.length <= 19)) {
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


  // alllows for tooltips //     

$('[data-toggle="tooltip"]').tooltip();  

// after search results are displayed -- jumps down to results //

  $("#search-btn").on( "click", function() {
  
        $("#spSearch").attr('style', 'display: block'); // this line is specific for demo -- deltete once you get search working //
        $("body, html").animate({ 
        scrollTop: $("#searchResults").offset().top -30
    }, 600);
 
});



// These functions are specific for the demo and you will not need any of these once you get the pages working //

 $("#editButton").click(function(){
  
     $('.panel-body').attr('style', 'background-color: rgba(253, 245, 154, 0.64)');
     $('#searchDiv').attr('style','display: none');
      $('#editDiv').attr('style','display: block');
  
    });

 $("#restore").click(function(){
  
     $('.panel-body').attr('style', 'background-color: none');
     $('#searchDiv').attr('style','display: block');
      $('#editDiv').attr('style','display: none');
  
    });


  $("#editCancel").click(function(){
  
     $('.panel-body').attr('style', 'background-color: none');
     $('#searchDiv').attr('style','display: block');
      $('#editDiv').attr('style','display: none');
  
    });

    $("#editSave").click(function(){
  
     $('.panel-body').attr('style', 'background-color: none');
     $('#searchDiv').attr('style','display: block');
      $('#editDiv').attr('style','display: none');
  
    });







 });  

 








