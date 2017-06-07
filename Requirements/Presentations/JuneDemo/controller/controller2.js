
$( window ).load(function() {
 
   $("#1sub, #2sub, #3sub, #4sub, #5sub, #6sub, #7sub, #8sub,  #10sub, #sub11-1, #12sub, #sub12-2, #12-1-1sub, #12-1-1-1sub, #sub12-2-1-1").attr("style", "display:none");
    
  
  

});

$(document).ready(function(){

   
     


  $('#comment1').change(function(){
   if($.trim($('#comment1').val()) != ''){
     $("#comment1image").attr("src", "images/comment.gif");
    
   }
});


    $('#comment3').change(function(){
   if($.trim($('#comment3').val()) != ''){
     $("#comment3image").attr("src", "images/comment.gif");
    
   }
});

        $('#comment11').change(function(){
   if($.trim($('#comment11').val()) != ''){
     $("#comment11image").attr("src", "images/comment.gif");
    
   }
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

   $("#gsTable").treeFy({
            expanderExpandedClass: 'fa fa-angle-double-down',
            expanderCollapsedClass: 'fa fa-angle-double-right',
            treeColumn: 0

         });


//Submit & Validate Function 

$(".submit").click(function(){


 $("#error").attr("style", "inline");
 $('#complete').attr('style', 'display:none;');
 $('html, body').animate({ scrollTop: 0 }, 0);


    $("#1, #8-1, #Textarea1,  #Textarea8, #12-2-1-1-1, #Textarea12-2-1-1-1, #12-2-1-1-2, #radio12-2-1-1-1, #sub12-2-1-1-3, #date, .warning1, #1-1text").addClass("has-error");
    $(".warning1").attr('style', 'display: block');
    $("#success").attr("style", "display: none");
    $("#main1,  #main8, #main12, #sub12-2, #sub12-2-1, #sub12-2-1-1").removeClass("treetable-collapsed preview");
     $("#main1,  #main8, #main12, #sub12-2, #sub12-2-1, #sub12-2-1-1" ).addClass("treetable-expanded");
     $("#1sub,  #8sub, #sub12-2, #sub12-2-1, #sub12-2-1-1, #sub12-2-1-1-1, #sub12-2-1-1-2, #sub12-2-1-1-3, #sub12-2-2, #sub12-2-3, #sub12-2-4, #sub12-2-5, #sub12-2-6").attr('style','display: table-row');
    $("#1-1text, #Textarea8, #12-2-1-1-2-Yes, #12-2-1-1-2-No, #calendar").attr("disabled", false);
       
 });


//Modal add attachments view previously saved

    $("#showAttach").click(function(){
      if ($(this).text() == "Show previously saved attachment(s)") 
  { 
     $(this).text("Hide previously saved attachment(s)"); 


 $("#attachmentTable").attr("style", "display:inline");
}

else {
  $("#attachmentTable").attr("style", "display:none;");
  $(this).text("Show previously saved attachment(s)"); 
}
  });


    //modal Add Attachments copy function

 $(".copy").click(function(){
   $("#table1").attr("style", "display:none;");
   $("#table2").attr("style", "display:block;");
   $("#fileList").attr("style", "display:none;");
   $("#fileList2").attr("style", "display:block;");
   });

 //modal Add Attachments save function
 $(".saveAttach").click(function(){
   $("#savedAttach").attr("src", "images/attachments.gif");
   
   });


//Save Alert Function
$("#saveButton").click(function(){
  $("#success").attr("style", "inline");
  $("#NSstatus").attr("style", "display:none")
  $("#Sstatus").attr("style", "display:inline")
});


$("#completed").click(function () {
     $('#complete').attr('style', 'display:block;');
      $('#error').attr('style', 'display:none;');
   
 });

$('.datepicker').datepicker();

    $(".allNotes").click(function(){
        $(".hiddenNotes").toggle();

        if ($(this).text() == "View All Comments") 
  { 
     $(".allNotes").text("Hide All Comments"); 

      $("#main11, #sub11-1").removeClass("treetable-collapsed");
     $("#main11, #sub11-1" ).addClass("treetable-expanded");
    
       
 

  } 
  else 
  { 
     $(".allNotes").text("View All Comments"); 
     $("#main11, #sub11-1").removeClass("treetable-expanded");
     $("#main11, #sub11-1" ).addClass("treetable-collapsed");
    
      
  }; 
    });


    //Comment box counter

$(function(){
var text_max = 4000;
    $('.exampleTextarea').on('keyup', function(){
        var wordsLength = $(this).val().length;
        var text_remaining = text_max - wordsLength;
        $(this).next().find('.count_message2').html(text_remaining);
    });
});


    //text box counter

$(function(){
var text_max = 2000;
    $('.exampleTextarea').on('keyup', function(){
        var wordsLength = $(this).val().length;
        var text_remaining = text_max - wordsLength;
        $(this).next().find('.count_message').html(text_remaining);
    });
});

//Expand All Function


$(".allSubs").click(function(){


   

  if ($(this).text() == "Exit Preview" )
  { 
  

    $('tr').removeClass("treetable-expanded");
     $('tr').addClass("treetable-collapsed");
      $('#grantBox').removeClass("treetable-collapsed");
      $('#grantBox').removeClass("treetable-expanded");
   $(".lastSub, .sub").attr('style', 'display:none');
           $('.answered').attr("style", "display:table-row; color: #999"); 
 $('#gsTable tr, #gsTable select, #gsTable input').removeClass("preview");
     $(".allSubs").text("Preview All Sub Questions"); 
 $(".form-control, .allNotes, #print, #rs, .submit, #submit, #saveButton, .save, .rs, .print, .form-check-input").attr('disabled', false);
$(".comms img").attr('src',"images/nocomment.gif");
$(".notes img").attr('src',"images/nocomment.gif");
$(".attachs img").attr('src',"images/attachment.gif");
  if($.trim($('#comment1, #comment3, #comment11').val()) != ''){
     $("#comment1image, #comment3image, #comment11image").attr("src", "images/comment.gif");
    
   }
    
    } 


  else 
  { 
   
 $(".allSubs").text("Exit Preview"); 
     $(".expanded").attr('style', 'display:table-row');

        $('tr').removeClass("treetable-collapsed");
          $('#gsTable tr, #gsTable select, #gsTable input').addClass("preview");
     $('tr').addClass("treetable-expanded");
      $('#grantBox, #collapse1').removeClass("treetable-collapsed");
      $('#grantBox, #collapse1').removeClass("treetable-expanded");
     $(".treetable-expanded").attr('style', 'display:table-row');
$(".comms img, .notes img").attr('src',"images/commentDisabled.gif");
$(".attachs img").attr('src',"images/Noattachment.gif");
$(".form-control, .allNotes, #print, #rs, .rs, .print, .submit, #submit, #saveButton, .save, .form-check-input").attr('disabled', true);
return false;

  }


    });

$('.notes').click(function(e){
     $(this).closest('tr').find('.hiddenNotes').toggle(); 
       $("#sub11-1 span").removeClass("fa fa-angle-double-right fa-angle-double-down");
     

      e.preventDefault();
});



$('#infoBox').click(function(){
     $(this).find('i').toggleClass("fa-minus-circle fa-plus-circle"); 
});

//controller for Q1
 $('#select1').change(function(){


    if($(this).val() == 'Changed and Not Approved'){
     $("#main1 .fa-angle-double-right").attr("style", "color: #CCC");
      $("#1sub").attr("style", "display:table-row");
      $('#1sub').addClass("expanded answered");
       $('#1sub').removeClass("preview");
        $("#1-1text").attr("disabled", false);
        $( "#main1 span").removeClass( "fa-angle-double-right" );
        $( "#main1 span").addClass( "fa-angle-double-down" );
         $("#main1 span").closest(".treetable-expander").off('click');
$("#main1 span").css('cursor', 'not-allowed')
  }
  
   

 else if ($(this).val() == 'Changed and Approved'){

  $("#1sub").attr("style", "display:none");
         $('#1sub').removeClass("expanded answered"); 

   $( "#main1 span").removeClass( "fa-angle-double-right" );
$( "#main1 span").addClass( "fa-angle-double-down" );
$("#main1 .fa-angle-double-down").attr("style", "color: #CCC");
$("#main1 span").closest(".treetable-expander").off('click');
$("#main1 span").css('cursor', 'not-allowed')  

 }

  else if ($(this).val() == 'Select an Option')
  {
  

       $( "#main1 span").closest(".treetable-expander").removeClass( "fa-angle-double-down" );
$( "#main1 span").closest(".treetable-expander").addClass( "fa-angle-double-right" );
  $("#main1 .fa-angle-double-right").removeAttr("style", "color: #CCC");
$("#1sub").attr("style", "display:none");
$('#1sub').addClass("preview");
      $('#1sub').removeClass("expanded answered"); 
      $("#1-1text").attr("disabled", true); 

   
    $('#main1 span').on('click', function (e) {
        e.preventDefault();
        var elem = $("#1sub")
        elem.toggle('fast');
        $( "#main1 span").toggleClass( "fa-angle-double-right  fa-angle-double-down" );
    });



  }       

    else {
     $("#main1 .fa-angle-double-right").removeAttr("style", "color: #CCC");
     
       $("#1sub").attr("style", "display:none");
      $('#1sub').addClass("preview");
      $('#1sub').removeClass("expanded answered"); 
      $("#1-1text").attr("disabled", true); 

       
    
  

    }

});

//controller for Q2
  $("input[name$='optionsRadios2']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#2sub").attr("style", "display:none");
   $('#2sub').removeClass("expanded answered ");
    $("#2-2text").attr("disabled", true); 
       $( "#main2 span").removeClass( "fa-angle-double-right" );
$( "#main2 span").addClass( "fa-angle-double-down" );
$("#main2  .fa-angle-double-down").attr("style", "color: #CCC");
     $("#main2 span").closest(".treetable-expander").off('click');
$("#main2 span").css('cursor', 'not-allowed')
    


 
  }
  else {
    $('#2sub').addClass("expanded answered");
    $('#2sub').removeClass("preview");
    $("#2sub").attr("style", "display:table-row");
    
   $( "#main2 span").removeClass( "fa-angle-double-right" );
$( "#main2 span").addClass( "fa-angle-double-down" );
$("#main2  .fa-angle-double-down").attr("style", "color: #CCC");
$("#2-2text").attr("disabled", false); 
     $("#main2 span").closest(".treetable-expander").off('click');
$("#main2 span").css('cursor', 'not-allowed'); 
    


  }
  
  });

//controller for Q3

  $("input[name$='optionsRadios3']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {

   $("#3sub").attr("style", "display:none");
 $("#3-1sub").attr("style", "display:none");
  $('#3sub').removeClass("expanded answered"); 
 $( "#main3 span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
$( "#main3 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#main3 .fa-angle-double-down").attr("style", "color: #CCC");
$("#main3 span").off('click');
$("#main3 span").css('cursor', 'not-allowed')
     
 
  }




  else {

  $("#3sub").attr("style", "display:table-row");
   $("#3-1sub").attr("style", "display:none");
$( "#3sub span").closest(".treetable-expander").removeClass( "fa-angle-double-down" );
$( "#3sub span").closest(".treetable-expander").addClass( "fa-angle-double-right" );
$("#3sub .fa-angle-double-right").removeAttr("style", "color: #CCC");

    
    $('#3sub').addClass("expanded answered");
    $('#3sub').removeClass("preview");
    $("#3-1-No").attr("disabled", false); 
    $("#3-1-Yes").attr("disabled", false); 
    $
   $( "#main3 span").removeClass( "fa-angle-double-right" );
$( "#main3 span").addClass( "fa-angle-double-down" );
$("#main3  .fa-angle-double-down").attr("style", "color: #CCC");
$("#3-1-1Text").attr("disabled", false);  
$("#main3 span").closest(".treetable-expander").off('click');
$("#main3 span").css('cursor', 'not-allowed')

$('#3sub span').on('click', function () {
     if(radio_value=='Yes') {
   $("#3sub").attr("style", "display:table-row"); };
   $("#3-1sub").removeAttr("style", "display:none")
 $("#3-1sub").toggleClass("treetable-expanded treetable-collapsed");
 $("#3-1sub").toggleClass("hideRow showRow");

});



  }
  
  });


  //controller for Q3-1




  $("input[name$='optionsRadios3-1']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#3-1sub").attr("style", "display:none");
   $('#3-1sub').removeClass("expanded answered"); 
   
          $( "#3sub span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#3sub span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#3sub  .fa-angle-double-down").attr("style", "color: #CCC");  
      $("#3sub  span").closest(".treetable-expander").off('click');
$("#3sub  span").css('cursor', 'not-allowed')   


 
  }
  else {
       $( "#3sub span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#3sub span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#3sub  .fa-angle-double-down").attr("style", "color: #CCC");
$('#3-1sub').removeClass("preview"); 
    $("#3-1sub").attr("style", "display:table-row");
     $('#3-1sub').addClass("expanded answered"); 
     $("#3sub span").closest(".treetable-expander").off('click');
         $("#3sub  span").closest(".treetable-expander").off('click');
$("#3sub  span").css('cursor', 'not-allowed') 

  }
  
  });

//controller for Q4
$("input[name$='optionsRadios4']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#4sub").attr("style", "display:none");

  $('#4sub').removeClass("expanded answered");   
          $( "#main4 span").removeClass( "fa-angle-double-right" );
$( "#main4 span").addClass( "fa-angle-double-down" );
$("#main4  .fa-angle-double-down").attr("style", "color: #CCC");  
 $("#main4 span").closest(".treetable-expander").off('click');
$("#main4 span").css('cursor', 'not-allowed')

  }




  else {
    $("#4-1-No").attr("disabled", false); 
    $("#4-1-Yes").attr("disabled", false); 

  $("#4sub").attr("style", "display:table-row");
   

$("#4sub .fa-angle-double-right").removeAttr("style", "color: #CCC");

    
    $('#4sub').addClass("expanded answered");
    $('#4sub').removeClass("preview");
  
   $( "#main4 span").removeClass( "fa-angle-double-right" );
$( "#main4 span").addClass( "fa-angle-double-down" );
$("#main4  .fa-angle-double-down").attr("style", "color: #CCC");
 $("#main4 span").closest(".treetable-expander").off('click');
$("#main4 span").css('cursor', 'not-allowed');

  }
  
  });


    //controller for Q5
  $("input[name$='optionsRadios5']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#5sub").attr("style", "display:none");
   $('#5sub').removeClass("expanded answered");
     
    

 
  }
  else {
    $('#5sub').addClass("expanded answered");
    $("#5sub").attr("style", "display:table-row");
    


  }
  
  });


  //controller for Q9
  $("input[name$='optionsRadios16']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#9sub").attr("style", "display:none");
   $('#9sub').removeClass("expanded answered");
     
     

 
  }
  else {
    $('#9sub').addClass("expanded answered");
    $("#9sub").attr("style", "display:table-row");
    


  }
  
  });

//controller for Q6
 $("input[name$='optionsRadios6']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No'){
      $("#6sub").attr("style", "display:table-row");
      $('#6sub').addClass("expanded answered");
         $('#6sub-1, #6sub-2').removeClass("expanded answered");
      $("#6sub-1, #6sub-2").attr("style", "display:none");
           $("#6sub .helperText").attr("style", "display:none")

      $("#6sub-1 .helperText, #6sub-2 .helperText").attr("style", "display:block");
     
    }
    else if(radio_value=='Yes'){
      $('#6sub').removeClass("expanded answered");
      $("#6sub").attr("style", "display:none");
       $("#6sub-1, #6sub-2").attr("style", "display:table-row");
      $('#6sub-1, #6sub-2').addClass("expanded answered");
       $("#6sub-1 .helperText, #6sub-2 .helperText").attr("style", "display:none")

      $("#6sub .helperText").attr("style", "display:block");
    
    }

    else {

      $("#6sub, #6sub-1, #6sub-2").attr("style", "display:none");

      $("#6sub .helperText, #6sub-1 .helperText, #6sub-2 .helperText").attr("style", "display:block");
      $('#6sub, #6sub-1, #6sub-2').removeClass("expanded answered");
     
     

    }

});





 //controller for Q7
 $('#select7').change(function(){ 
    if($(this).val() == 'Not Approved' || $(this).val() == 'Yes Approved' || $(this).val() == 'Not Approved No Impact'){
      $("#7sub").attr("style", "display:table-row");
      $('#7sub').addClass("expanded answered");
     
    }
    

    else {

      $("#7sub").attr("style", "display:none");
      $('#7sub').removeClass("expanded answered");
     
     

    }

});

  //controller for Q8
 $('#select8').change(function(){


    if($(this).val() == 'Changed and Not Approved'){
     $("#main8 .fa-angle-double-right").attr("style", "color: #CCC");
      $("#8sub").attr("style", "display:table-row");
      $('#8sub').addClass("expanded answered");
       $('#8sub').removeClass("preview");
        $("#Textarea8").attr("disabled", false);
        $( "#main8 span").removeClass( "fa-angle-double-right" );
        $( "#main8 span").addClass( "fa-angle-double-down" );
             $("#main8 span").closest(".treetable-expander").off('click');
$("#main8 span").css('cursor', 'not-allowed')
  }
  
   

 else if ($(this).val() == 'Changed and Approved'){

  $("#8sub").attr("style", "display:none");
         $('#8sub').removeClass("expanded answered"); 
  
   $( "#main8 span").removeClass( "fa-angle-double-down" );
$( "#main8 span").addClass( "fa-angle-double-right" );
$("#main8 .fa-angle-double-right").attr("style", "color: #CCC");
      $("#main8 span").closest(".treetable-expander").off('click');
$("#main8 span").css('cursor', 'not-allowed')  

 }

  else if ($(this).val() == 'Select an Option')
  {
  

       $( "#main8 span").removeClass( "fa-angle-double-down" );
$( "#main8 span").addClass( "fa-angle-double-right" );
  $("#main8 .fa-angle-double-right").removeAttr("style", "color: #CCC");
$("#8sub").attr("style", "display:none");
$('#8sub').addClass("preview");
      $('#8sub').removeClass("expanded answered"); 
      $("#Textarea8").attr("disabled", true); 

   
    $('#main8 span').on('click', function (e) {
        e.preventDefault();
        var elem = $("#8sub")
        elem.toggle('fast');
        $( "#main8 span").toggleClass( "fa-angle-double-right  fa-angle-double-down" );
    });



  }       

    else {
     $("#main8 .fa-angle-double-right").removeAttr("style", "color: #CCC");
     
       $("#8sub").attr("style", "display:none");
      $('#8sub').addClass("preview");
      $('#8sub').removeClass("expanded answered"); 
      $("#Textarea8").attr("disabled", true); 

       
    
  

    }

});




 //controller for Q10
  $("input[name$='optionsRadios10']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#10sub").attr("style", "display:none");
   $('#10sub').removeClass("expanded answered");
    $("#10sub1").attr("style", "display:none");
     
    

 
  }
  else {
    $("#10sub").attr("style", "display:table-row");
    $('#10sub').addClass("expanded answered");

      

  }
  
  });


  //controller for Q10-1
  $("input[name$='optionsRadios10-1']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#10sub1").attr("style", "display:none");

     $('#10sub1').removeClass("expanded answered");
     

 
  }
  else {
    $("#10sub1").attr("style", "display:table-row");
     $('#10sub1').addClass("expanded answered");
     

  }
  
  });


  //controller for Q11
  $("input[name$='optionsRadios11']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#sub11-1").attr("style", "display:none");
    $('#sub11-1').removeClass("expanded answered");
    $("#sub11-1-1").attr("style", "display:none");

     
     
  }
  else {
    $("#sub11-1").attr("style", "display:table-row");
     $('#sub11-1').addClass("expanded answered");
   
     


  }
  
  });


  //controller for Q11-1
  $("input[name$='optionsRadios11-1']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#sub11-1-1").attr("style", "display:none");
    $('#sub11-1-1').removeClass("expanded answered");
     
    


 
  }
  else {
    $("#sub11-1-1").attr("style", "display:table-row");
      $('#sub11-1-1').addClass("expanded answered");
    

  }
  
  });



  //controller for Q12
  $("input[name$='optionsRadios12']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#12sub").attr("style", "display:none");
 $("#12-1sub").attr("style", "display:none");
$("#sub12-2").attr("style", "display:none");
  $('#12sub').removeClass("expanded answered"); 
        $("#main12  span").closest(".treetable-expander").off('click');
$("#main12  span").css('cursor', 'not-allowed')   
     
 
  }

  else {
// show 12.1 row
  $("#12sub").attr("style", "display:table-row");
   $("#12-1sub").attr("style", "display:none");
    
    $('#12sub').addClass("expanded answered");
    $('#12sub').removeClass("preview");


    $("#12-1-No").attr("disabled", false); 
    $("#12-1-Yes").attr("disabled", false); 
     
   $( "#main12 span").removeClass( "fa-angle-double-right" );
$( "#main12 span").addClass( "fa-angle-double-down" );
$("#main12  .fa-angle-double-down").attr("style", "color: #CCC");
$("#select12-1-1").attr("disabled", false);  
    $("#main12  span").closest(".treetable-expander").off('click');
$("#main12  span").css('cursor', 'not-allowed') 

$('#12sub span').on('click', function () {
   if(radio_value=='Yes') {
   $("#12sub").attr("style", "display:table-row"); };
   $("#12-1-1sub").removeAttr("style", "display:none")
 $("#12-1-1sub").toggleClass("treetable-expanded treetable-collapsed");
 $("#12-1-1sub").toggleClass("hideRow showRow");

 });

//show 12.2

  $("#sub12-2").attr("style", "display:table-row");
   $("#sub12-2-1").attr("style", "display:none");
    
    $('#sub12-2').addClass("expanded answered");
    $('#sub12-2').removeClass("preview");


    $("#12-2-No").attr("disabled", false); 
    $("#12-2-Yes").attr("disabled", false); 
$("#select12-2-1").attr("disabled", false);  


$('#sub12-2 span').on('click', function () {
   if(radio_value=='No') {
   $("#sub12-2").attr("style", "display:table-row"); };
   $("#sub12-2-1").removeAttr("style", "display:none")
 $("#sub12-2-1").toggleClass("treetable-expanded treetable-collapsed");
 $("#sub12-2-1").toggleClass("hideRow showRow");


});

  }
  
  });


  //controller for Q12-1




  $("input[name$='optionsRadios12-1']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#12-1-1sub").attr("style", "display:none");
   $('#12-1-1sub').removeClass("expanded answered"); 
   
    $( "#12sub span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#12sub span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#12sub  .fa-angle-double-down").attr("style", "color: #CCC");    
 $("#12sub span").closest(".treetable-expander").off('click');   
$("#12sub span").closest(".treetable-expander").css('cursor', 'not-allowed') 

 
  }
  else {

       $( "#12sub span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#12sub span").closest(".treetable-expander").addClass( "fa-angle-double-down " );
$("#12sub  .fa-angle-double-down").attr("style", "color: #CCC");
$('#12-1-1sub').removeClass("preview"); 
    $("#12-1-1sub").attr("style", "display:table-row");
     $('#12-1-1sub').addClass("expanded answered"); 
     
     $("12-1sub span").closest(".treetable-expander").off('click');
   $("#select12-1-1").attr("disabled", false); 

$("#12sub span").closest(".treetable-expander").off('click');   
$("#12sub span").closest(".treetable-expander").css('cursor', 'not-allowed') 


  }
  
  });


  //controller for Q12-2




  $("input[name$='optionsRadios12-2']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#sub12-2-1").attr("style", "display:none");
   $('#sub12-2-1').removeClass("expanded answered"); 
   $("#sub12-2 span").closest(".treetable-expander").off('click');
    $( "#sub12-2 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#sub12-2  .fa-angle-double-down").attr("style", "color: #CCC");    
 $("#sub12-2 span").closest(".treetable-expander").off('click');   
$("#sub12-2 span").closest(".treetable-expander").css('cursor', 'not-allowed')   


 
  }
  else {

       $( "#sub12-2 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2 span").closest(".treetable-expander").addClass( "fa-angle-double-down " );
$("#sub12-2  .fa-angle-double-down").attr("style", "color: #CCC");
$('#sub12-2-1, #sub12-2-2, #sub12-2-3, #sub12-2-4, #sub12-2-5, #sub12-2-6').removeClass("preview"); 
    $("#sub12-2-1, #sub12-2-2, #sub12-2-3, #sub12-2-4, #sub12-2-5, #sub12-2-6").attr("style", "display:table-row");
     $('#sub12-2-1, #sub12-2-2, #sub12-2-3, #sub12-2-4, #sub12-2-5, #sub12-2-6').addClass("expanded answered"); 
     
     $("#sub12-2 span").closest(".treetable-expander").off('click');   
$("#sub12-2 span").closest(".treetable-expander").css('cursor', 'not-allowed') 
   $("#select12-2-1, #select12-2-2, #12-2-3-Yes, #12-2-3-No, #12-2-4-Yes, #12-2-4-No, #12-2-5-Yes, #12-2-5-No, #12-2-6-Yes, #12-2-6-No").attr("disabled", false); 



  }
  
  });


//controller for Q12-1-1

$('#select12-1-1').change(function(){

if ($(this).val() == 'Yes, in scope and Approved'){

  $("#12-1-1-1sub").attr("style", "display:none");
         $('#12-1-1-1sub').removeClass("expanded answered"); 
   $("#12-1-1sub span").closest(".treetable-expander").off('click');
   $( "#12-1-1sub span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
$( "#12-1-1sub span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#12-1-1sub .fa-angle-double-down").attr("style", "color: #CCC");
  $("#12-1-1sub span").closest(".treetable-expander").off('click');   
$("#12-1-1sub span").closest(".treetable-expander").css('cursor', 'not-allowed')   

 }

  else if ($(this).val() == 'Select an Option')
  {
  

       $( "#12-1-1sub span").closest(".treetable-expander").removeClass( "fa-angle-double-down" );
$( "#12-1-1sub span").closest(".treetable-expander").addClass( "fa-angle-double-right" );
  $("#12-1-1sub .fa-angle-double-right").removeAttr("style", "color: #CCC");
$("#12-1-1-1sub").attr("style", "display:none");
$('#12-1-1-1sub').addClass("preview");
      $('#12-1-1-1sub').removeClass("expanded answered"); 
      $("#12-1-1-1-text").attr("disabled", true); 

   
    $('#12-1-1sub span').on('click', function (e) {
        e.preventDefault();
        var elem = $("#1sub")
        elem.toggle('fast');
        $( "#12-1-1sub span").closest(".treetable-expander").toggleClass( "fa-angle-double-right  fa-angle-double-down" );
    });



  }       

    else {
   $("#12-1-1sub .fa-angle-double-right").attr("style", "color: #CCC");
      $("#12-1-1-1sub").attr("style", "display:table-row");
      $('#12-1-1-1sub').addClass("expanded answered");
       $('#12-1-1-1sub').removeClass("preview");
        $("#12-1-1-1-text").attr("disabled", false);
        $( "#12-1-1sub span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#12-1-1sub span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
          $("#12-1-1sub span").closest(".treetable-expander").off('click');   
        $("#12-1-1sub span").closest(".treetable-expander").css('cursor', 'not-allowed'); 

       
    
  

    }

});



  //controller for Q12-2-3




  $("input[name$='optionsRadios12-2-3']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#sub12-2-3-1").attr("style", "display:none");
   $('#sub12-2-3-1').removeClass("expanded answered"); 
   $("#sub12-2-3 span").closest(".treetable-expander").off('click');
    $( "#sub12-2-3 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2-3 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#sub12-2-3  .fa-angle-double-down").attr("style", "color: #CCC");    
 $("#sub12-2-3 span").closest(".treetable-expander").off('click');   
$("#sub12-2-3 span").closest(".treetable-expander").css('cursor', 'not-allowed')   


 
  }
  else {

       $( "#sub12-2-3 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2-3 span").closest(".treetable-expander").addClass( "fa-angle-double-down " );
$( "#sub12-2-3-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
$("#sub12-2-3  .fa-angle-double-down").attr("style", "color: #CCC");
$('#sub12-2-3-1').removeClass("preview"); 
    $("#sub12-2-3-1").attr("style", "display:table-row");
     $('#sub12-2-3-1').addClass("expanded answered"); 
  

     $("#sub12-2-3 span").closest(".treetable-expander").off('click');   
$("#sub12-2-3 span").closest(".treetable-expander").css('cursor', 'not-allowed') 
   $("#12-2-3-1-Yes, #12-2-3-1-No").attr("disabled", false); 



  }
  
  });


  //controller for Q12-2-3-1




  $("input[name$='optionsRadios12-2-3-1']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#sub12-2-3-1-1").attr("style", "display:none");
   $('#sub12-2-3-1-1').removeClass("expanded answered"); 
 
    $( "#sub12-2-3-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2-3-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#sub12-2-3-1  .fa-angle-double-down").attr("style", "color: #CCC");    
 $("#sub12-2-3-1 span").closest(".treetable-expander").off('click');   
$("#sub12-2-3-1 span").closest(".treetable-expander").css('cursor', 'not-allowed')   


 
  }
  else {

       $( "#sub12-2-3-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2-3-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down " );
$("#sub12-2-3-1  .fa-angle-double-down").attr("style", "color: #CCC");
$('#sub12-2-3-1-1').removeClass("preview"); 
    $("#sub12-2-3-1-1").attr("style", "display:table-row");
     $('#sub12-2-3-1-1').addClass("expanded answered"); 
     

   $("#12-2-3-1-1-Yes, #12-2-3-1-1-No").attr("disabled", false); 



  }
  
  });


  //controller for Q12-2-6




  $("input[name$='optionsRadios12-2-6']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#sub12-2-6-1").attr("style", "display:none");
   $('#sub12-2-6-1').removeClass("expanded answered"); 
    $( "#sub12-2-6 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2-6 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#sub12-2-6  .fa-angle-double-down").attr("style", "color: #CCC");    
 $("#sub12-2-6 span").closest(".treetable-expander").off('click');   
$("#sub12-2-6 span").closest(".treetable-expander").css('cursor', 'not-allowed')   


 
  }
  else {

       $( "#sub12-2-6 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2-6 span").closest(".treetable-expander").addClass( "fa-angle-double-down " );
$("#sub12-2-6  .fa-angle-double-down").attr("style", "color: #CCC");
$('#sub12-2-6-1').removeClass("preview"); 
    $("#sub12-2-6-1").attr("style", "display:table-row");
     $('#sub12-2-6-1').addClass("expanded answered"); 
     
     $("#sub12-2 span").closest(".treetable-expander").off('click');   
$("#sub12-2 span").closest(".treetable-expander").css('cursor', 'not-allowed') 
   $("#12-2-6-1-Yes, #12-2-6-1-No").attr("disabled", false); 
 $("#sub12-2-6 span").closest(".treetable-expander").off('click');   
$("#sub12-2-6 span").closest(".treetable-expander").css('cursor', 'not-allowed') 


  }
  
  });


  //controller for Q12-2-6-1




  $("input[name$='optionsRadios12-2-6-1']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#sub12-2-6-1-1").attr("style", "display:none");
   $('#sub12-2-6-1-1').removeClass("expanded answered"); 
    $( "#sub12-2-6-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2-6-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#sub12-2-6-1  .fa-angle-double-down").attr("style", "color: #CCC");    
 $("#sub12-2-6-1 span").closest(".treetable-expander").off('click');   
$("#sub12-2-6-1 span").closest(".treetable-expander").css('cursor', 'not-allowed')   


 
  }
  else {

       $( "#sub12-2-6-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2-6-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down " );
$("#sub12-2-6-1  .fa-angle-double-down").attr("style", "color: #CCC");
$('#sub12-2-6-1-1').removeClass("preview"); 
    $("#sub12-2-6-1-1").attr("style", "display:table-row");
     $('#sub12-2-6-1-1').addClass("expanded answered"); 
     
     $("#sub12-2-6 span").closest(".treetable-expander").off('click');   
$("#sub12-2-6 span").closest(".treetable-expander").css('cursor', 'not-allowed') 
   $("#12-2-6-1-1-check").attr("disabled", false); 
 $("#sub12-2-6-1 span").closest(".treetable-expander").off('click');   
$("#sub12-2-6-1 span").closest(".treetable-expander").css('cursor', 'not-allowed') 


  }
  
  });


//controller for Q12-2-1

$('#select12-2-1').change(function(){

if ($(this).val() == 'Not Exempt'){

  $("#sub12-2-1 .fa-angle-double-right").attr("style", "color: #CCC");
      $("#sub12-2-1-1").attr("style", "display:table-row");
      $('#sub12-2-1-1').addClass("expanded answered");
       $('#sub12-2-1-1').removeClass("preview");
        $("#12-2-1-1-Yes").attr("disabled", false);
        $("#12-2-1-1-No").attr("disabled", false);
        $( "#sub12-2-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
        $("#sub12-2-1 span").closest(".treetable-expander").off('click');
        $("#sub12-2-1 span").closest(".treetable-expander").css('cursor', 'not-allowed');

 }

  else if ($(this).val() == 'Select an Option')
  {
  

       $( "#sub12-2-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-down" );
$( "#sub12-2-1 span").closest(".treetable-expander").addClass( "fa-angle-double-right" );
  $("#sub12-2-1 .fa-angle-double-right").removeAttr("style", "color: #CCC");
$("#sub12-2-1-1").attr("style", "display:none");
$('#sub12-2-1-1').addClass("preview");
      $('#sub12-2-1-1').removeClass("expanded answered"); 
         $("#12-2-1-1-Yes").attr("disabled", true);
        $("#12-2-1-1-No").attr("disabled", true);

   
    $('#sub12-2-1 span').on('click', function (e) {
        e.preventDefault();
        var elem = $("#sub12-2-1")
        elem.toggle('fast');
        $( "#sub12-2-1 span").closest(".treetable-expander").toggleClass( "fa-angle-double-right  fa-angle-double-down" );
    });



  }       

    else {

       
    
    $("sub12-2-1-1").attr("style", "display:none");
         $('#sub12-2-1-1').removeClass("expanded answered"); 
   $("#sub12-2-1 span").closest(".treetable-expander").off('click');
   $( "#sub12-2-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
$( "#sub12-2-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#sub12-2-1 .fa-angle-double-down").attr("style", "color: #CCC");
$("#sub12-2-1 span").closest(".treetable-expander").off('click');
        $("#sub12-2-1 span").closest(".treetable-expander").css('cursor', 'not-allowed');

    }

});


//controller for Q12-2-1-1


  $("input[name$='optionsRadios12-2-1-1']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#sub12-2-1-1-1").attr("style", "display:none");
   $('#sub12-2-1-1-1').removeClass("expanded answered"); 
    $( "#sub12-2-1-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2-1-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
$("#sub12-2-1-1  .fa-angle-double-down").attr("style", "color: #CCC");    
 $("#sub12-2-1-1 span").closest(".treetable-expander").off('click');
        $("#sub12-2-1-1 span").closest(".treetable-expander").css('cursor', 'not-allowed');   


 
  }
  else {

       $( "#sub12-2-1-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right");
$( "#sub12-2-1-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down " );
$("#sub12-2-1-1  .fa-angle-double-down").attr("style", "color: #CCC");
$('#sub12-2-1-1-1, #sub12-2-1-1-2, #sub12-2-1-1-3').removeClass("preview"); 
    $("#sub12-2-1-1-1, #sub12-2-1-1-2, #sub12-2-1-1-3").attr("style", "display:table-row");
     $('#sub12-2-1-1-1, #sub12-2-1-1-2, #sub12-2-1-1-3').addClass("expanded answered"); 
     
     $("#sub12-2-1-1 span").closest(".treetable-expander").off('click');
   $("#Textarea12-2-1-1-1, #12-2-1-1-2-Yes, #12-2-1-1-2-No, #calendar").attr("disabled", false); 

$("#sub12-2-1-1 span").closest(".treetable-expander").off('click');
        $("#sub12-2-1-1 span").closest(".treetable-expander").css('cursor', 'not-allowed');

  }
  
  });



     $('[data-toggle="tooltip"]').tooltip();   



    
});










