



$(document).ready(function(){

  $(".chosen-select").chosen();

  $( ".no-sort" ).removeClass( ".sorting" );

$('#example').dataTable( {
       columnDefs: [
         { targets: ['status'], type: 'alt-string'},
 
         { targets: 'no-sort', orderable: false }], 
         "oLanguage": {
"sSearch": "Filter Results: "
}

    } );

 
   

      

$('[data-toggle="tooltip"]').tooltip();   





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

  $("input:radio[name=options]").on( "change", function() {
         if($(this).val() == 'PI Name'){
       $('#searchCriteria').attr('placeholder', 'Enter PI Name');
    }

    else {
      $('#searchCriteria').attr('placeholder', 'Enter Grant Number');
    }
});

 });   






