



$(document).ready(function(){

  $(document).ready(function() {
    $('#example').DataTable();
} );


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

    $("#gsTable").treeFy({
            expanderExpandedClass: 'fa fa-minus-circle',
            expanderCollapsedClass: 'fa fa-plus-circle',
            treeColumn: 0

        });

$("#submit").click(function(){

     BootstrapDialog.show({
            title: 'Warning!',
            message: 'Errors indicated on the form must be corrected before submission.',
            type: 'type-danger',
            buttons: [{
              
                label: 'Close',
                action: function(dialogItself){
                    dialogItself.close();
                }
            }],


        });

    $("#1, #6-1, #8-1, #Textarea1, #Textarea6, #Textarea8, #12-2-1-1-1, #Textarea12-2-1-1-1, #12-2-1-1-2, #radio12-2-1-1-1, #sub12-2-1-1-3, #date, .warning1").addClass("has-error");
    $(".warning1").attr('style', 'display: block');
    $("#main1, #main6, #main8, #main12, #sub12-2, #sub12-2-1, #sub12-2-1-1").removeClass("treetable-collapsed");
     $("#main1, #main6, #main8, #main12, #sub12-2, #sub12-2-1, #sub12-2-1-1" ).addClass("treetable-expanded");
     $("#sub1, #sub6, #sub8, #sub12-2, #sub12-2-1, #sub12-2-1-1, #sub12-2-1-1-1, #sub12-2-1-1-2, #sub12-2-1-1-3, #sub12-2-2, #sub12-2-3, #sub12-2-4, #sub12-2-5, #sub12-2-6").attr('style','display: table-row');
       $("#main1, #main6, #main8, #main12").find('span').toggleClass("fa-minus-circle fa-plus-circle"); 
 $("#sub12-2, #sub12-2-1, #sub12-2-1-1").find('.treetable-expander').toggleClass("fa-minus-circle fa-plus-circle"); 
       
 });


$('.datepicker').datepicker();

    $(".allNotes").click(function(){
        $(".hiddenNotes").toggle();

        if ($(this).text() == "View All Comments") 
  { 
     $(this).text("Hide All Comments"); 
  } 
  else 
  { 
     $(this).text("View All Comments"); 
  }; 
    });



 $(".allSubs").click(function(){
   
   

  if ($(this).text() == "Expand All") 
  { 
  

     $(this).text("CollapseAll"); 
     $('tr').removeClass("treetable-collapsed");
     $('tr').addClass("treetable-expanded");
     $('.sub').attr('style','display: table-row');
      $('.lastSub').attr('style','display: table-row');
       $('.fa-plus-circle').toggleClass("fa-plus-circle fa-minus-circle"); 


  } 
  else 
  { 
   
   $('tr').removeClass("treetable-expanded");
     $('tr').addClass("treetable-collapsed");
      $('.sub').attr('style','display: none');
      $('.lastSub').attr('style','display: none');
      $('.fa-minus-circle').toggleClass("fa-minus-circle fa-plus-circle"); 

      
    
     $(this).text("Expand All"); 


  }; 
    });

$('.notes').click(function(){
     $(this).closest('tr').find('.hiddenNotes').toggle(); 
});



$('#infoBox').click(function(){
     $(this).find('i').toggleClass("fa-minus-circle fa-plus-circle"); 
});


  $("input[name$='optionsRadios10']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#2sub").attr("style", "display:none");

     
     $("#main2").find('span').removeClass("fa");
     $("#main2").find('span').removeClass("fa-plus-circle");
      $("#main2").find('span').removeClass("fa-minus-circle");

 
  }
  else {
    $("#2sub").attr("style", "display:table-row");
     $("#main2").find('span').removeClass("fa-plus-circle");
      $("#main2").find('span').addClass("fa");
      $("#main2").find('span').addClass("fa-minus-circle");


  }
  
  });

     $('[data-toggle="tooltip"]').tooltip();   



    
});


