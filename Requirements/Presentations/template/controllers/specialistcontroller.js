$(document).ready(function(){
    $(".chosen-select").chosen();
   

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


      

$('[data-toggle="tooltip"]').tooltip();   



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



  $('.panel-heading span.clickable').click (function(){
    var $this = $(this);
  if(!$this.hasClass('panel-collapsed')) {
    $this.parents('.panel').find('.panel-body').slideUp();

    $this.addClass('panel-collapsed');
    $this.find('i').removeClass('fa-minus-circle').addClass('fa-plus-circle');
  } else {
    $this.parents('.panel').find('.panel-body').slideDown();
    $this.removeClass('panel-collapsed');
    $this.find('i').removeClass('fa-plus-circle').addClass('fa-minus-circle');
  }
});


  $("#search-btn").on( "click", function() {
  
        $("#spSearch").attr('style', 'display: block');
        $("body, html").animate({ 
        scrollTop: $("#searchResults").offset().top -30
    }, 600);
 
});

 });  

 








