



$(document).ready(function(){

  $(".chosen-select").chosen();

  $( ".no-sort" ).removeClass( ".sorting" );

$('#example').dataTable( {
       columnDefs: [
         { targets: ['status'], type: 'alt-string'},
 
         { targets: 'no-sort', orderable: false },

         {targets: 'no-sort', aaSorting: false }]

    } );

 
   
//Save Alert Function
$("#saveButton").click(function(){
  $("#success").attr("style", "inline");
  $("#defaultS").attr("style", "display:none;");
  $("#preferredS").attr("style", "display:inline;");
 
});

//Load Preferences 
$("#loadSP").click(function(){
 
 $('#gType option:selected').removeAttr('selected');
$('#gType option:nth-child(0)').attr('selected','selected');

 $('#selectFrom option:selected').removeAttr('selected');
$('#selectFrom option:nth-child(2)').attr('selected','selected');

$('#grantNumber radio:checked').removeAttr('checked');
$('#PIname').attr('checked','checked');
$('#searchCriteria').attr('value','Smith');


  $("#defaultS").attr("style", "display:none;");
  $("#preferredS").attr("style", "display:inline;");
   $("#success").attr('style', 'display: none');
 
});


//Search Function

 $("#search-btn").click(function(){
      $("#pSearch").attr('style', 'display: block');
      $("#success").attr('style', 'display: none');
    });

 //tooltip
      

$('[data-toggle="tooltip"]').tooltip();   





 $("#restore").click(function(){
  
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

  $("input:radio[name=options]").on( "change", function() {
         if($(this).val() == 'PI Name'){
       $('#searchCriteria').attr('placeholder', 'Enter PI Name');
    }

    else {
      $('#searchCriteria').attr('placeholder', 'Enter Grant Number');
    }
});

 });   






