
$( window ).load(function() {
 
   $("#2sub, #3sub, #4sub, #5sub, #6sub, #7sub, #8sub,  #10sub, #sub11-1, #12sub, #sub12-2, #12-1-1sub, #12-1-1-1sub, #sub12-2-1-1").attr("style", "display:none");

  $("#sub1, #main1, #sub3-1, #main3, #sub4-1, #main4, #main6, #sub6, #sub8, #main8, #sub10, #sub10-1-1,  #main12, #sub12-1, #sub12-2, #sub12-2-1, #sub12-2-1-1, #sub12-2-1-2, #sub12-2-1-3, #sub12-2-1-1-1, #sub12-2-1-1-2, #sub12-2-1-1-3, #sub12-2-2, #sub12-2-3, #sub12-2-4, #sub12-2-3-1, #sub12-2-3-1-1, #sub12-2-5, #sub12-2-6, #sub12-2-6-1, #sub12-2-6-1-1, #3sub, #4sub").attr("style", "display:table-row");
    $("#main1, #main3, #main4, #main6, #main8, #sub10,  #main12, #sub12-1, #sub12-2-1, #sub12-2-3-1, #sub12-2-6, #sub12-2-6-1").removeClass("treetable-collapsed");
     $("#main1, #main3, #main4, #main6, #main8, #sub10,  #main12, #sub12-1, #sub12-2-1, #sub12-2-3-1, #sub12-2-6, #sub12-2-6-1").addClass("treetable-expanded");
  

});

$(document).ready(function(){

   
   $('.answered').attr("style", "display:table-row; color: #000");    


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


    $("#1, #6-1, #8-1, #Textarea1, #Textarea6, #Textarea8, #12-2-1-1-1, #Textarea12-2-1-1-1, #12-2-1-1-2, #radio12-2-1-1-1, #sub12-2-1-1-3, #date, .warning1, #1-1text").addClass("has-error");
    $(".warning1").attr('style', 'display: block');
    $("#success").attr("style", "display: none");
    $("#main1, #main6, #main8, #main12, #sub12-2, #sub12-2-1, #sub12-2-1-1").removeClass("treetable-collapsed");
     $("#main1, #main6, #main8, #main12, #sub12-2, #sub12-2-1, #sub12-2-1-1" ).addClass("treetable-expanded");
     $("#1sub, #6sub, #8sub, #sub12-2, #sub12-2-1, #sub12-2-1-1, #sub12-2-1-1-1, #sub12-2-1-1-2, #sub12-2-1-1-3, #sub12-2-2, #sub12-2-3, #sub12-2-4, #sub12-2-5, #sub12-2-6").attr('style','display: table-row');
       $("#main1, #main6, #main8, #main12").find('span').toggleClass("fa-minus-circle fa-plus-circle"); 
 $("#sub12-2, #sub12-2-1, #sub12-2-1-1").find('.treetable-expander').toggleClass("fa-minus-circle fa-plus-circle"); 
       
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
     $("#sub11-1, #sub11-1-1").attr('style','display: table-row');
       
 

  } 
  else 
  { 
     $(".allNotes").text("View All Comments"); 
     $("#main11, #sub11-1").removeClass("treetable-expanded");
     $("#main11, #sub11-1" ).addClass("treetable-collapsed");
     $("#sub11-1, #sub11-1-1").attr('style','display: none');
      
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
 $(".form-control, .allNotes, .print, .rs, .submit, #submit, .save, .form-check-input").attr('disabled', false);
$(".comms img").attr('src',"images/nocomment.gif");
$(".notes img").attr('src',"images/nocomment.gif");
$(".attachs img").attr('src',"images/attachment.gif");
$("#savedAttach").attr('src',"images/attachments.gif");
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
$(".attachs img, #savedAttach").attr('src',"images/Noattachment.gif");
$(".form-control, .allNotes, .print, .rs, .submit, #submit, .save, .form-check-input").attr('disabled', true);
return false;

  }


    });

$('.notes').click(function(e){
     $(this).closest('tr').find('.hiddenNotes').toggle(); 
       $("#sub11-1 span").removeClass("fa fa-minus-circle fa-plus-circle");
     $("#main11 span").removeClass("fa fa-minus-circle fa-plus-circle");

      e.preventDefault();
});



$('#infoBox').click(function(){
     $(this).find('i').toggleClass("fa-minus-circle fa-plus-circle"); 
});




//controller for Q1
   $("#main1 span").off('click');
   
   $( "#main1 span").removeClass( "fa fa-angle-double-right" );
        $( "#main1 span").addClass( "fa fa-angle-double-down" );
 $("#main1 .fa-angle-double-down").attr("style", "color: #CCC");


//controller for Q2
   $("#main2 span").off('click');
   
   $( "#main2 span").removeClass( "fa-angle-double-right" );
        $( "#main2 span").addClass( "fa-angle-double-down" );
 $("#main2 .fa-angle-double-down").attr("style", "color: #CCC");

//controller for Q3
   $("#main3 span").off('click');
   
   $( "#main3 span").removeClass( "fa-angle-double-right" );
        $( "#main3 span").addClass( "fa-angle-double-down" );
 $("#main3 .fa-angle-double-down").attr("style", "color: #CCC");

//controller for Q3-1
   $("#3sub span").off('click');
   
   $( "#3sub span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#3sub span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#3sub .fa-angle-double-down").attr("style", "color: #CCC");
  

//controller for Q4


   $("#main4 span").off('click');
   
   $( "#main4 span").removeClass( "fa-angle-double-right" );
        $( "#main4 span").addClass( "fa-angle-double-down" );
 $("#main4 .fa-angle-double-down").attr("style", "color: #CCC");

  

    //controller for Q5
   $("#main5 span").off('click');
   
   $( "#main5 span").removeClass( "fa-angle-double-right" );
        $( "#main5 span").addClass( "fa-angle-double-down" );
 $("#main5 .fa-angle-double-down").attr("style", "color: #CCC");


  //controller for Q6
   $("#main6 span").off('click');
   
   $( "#main6 span").removeClass( "fa-angle-double-right" );
        $( "#main6 span").addClass( "fa-angle-double-down" );
 $("#main6 .fa-angle-double-down").attr("style", "color: #CCC");

//controller for Q7
   $("#main7 span").off('click');
   
   $( "#main7 span").removeClass( "fa-angle-double-right" );
        $( "#main7 span").addClass( "fa fa-angle-double-down" );
 $("#main7 .fa-angle-double-down").attr("style", "color: #CCC");



  //controller for Q9
   $("#main9 span").off('click');
   
   $( "#main9 span").removeClass( "fa-angle-double-right" );
        $( "#main9 span").addClass( "fa fa-angle-double-down" );
 $("#main9 .fa-angle-double-down").attr("style", "color: #CCC");





  //controller for Q8
 
   $("#main8 span").off('click');
   
   $( "#main8 span").removeClass( "fa-angle-double-right" );
        $( "#main8 span").addClass( "fa-angle-double-down" );
 $("#main8 .fa-angle-double-down").attr("style", "color: #CCC");







 //controller for Q10
   $("#main10 span").off('click');
   
   $( "#main10 span").removeClass( "fa-angle-double-right" );
        $( "#main10 span").addClass( "fa-angle-double-down" );
 $("#main10 .fa-angle-double-down").attr("style", "color: #CCC");


  //controller for Q10-1


     $('#sub10').removeClass("expanded answered");
   $("#sub10 span").off('click');
   
   $( "#sub10 span").removeClass( "fa-angle-double-right" );
        $( "#sub10 span").addClass( "fa-angle-double-down" );
 $("#sub10 .fa-angle-double-down").attr("style", "color: #CCC");


  //controller for Q11
   $("#main11 span").off('click');
   
   $( "#main11 span").removeClass( "fa-angle-double-right" );
        $( "#main11 span").addClass( "fa-angle-double-down" );
 $("#main11 .fa-angle-double-down").attr("style", "color: #CCC");


  //controller for Q11-1
   $("#sub11-1 span").off('click');
   
   $( "#sub11-1 span").removeClass( "fa-angle-double-right" );
        $( "#sub11-1 span").addClass( "fa-angle-double-down" );
 $("#sub11-1 .fa-angle-double-down").attr("style", "color: #CCC");



  //controller for Q12
   $("#main12 span").off('click');
   
   $( "#main12 span").removeClass( "fa-angle-double-right" );
        $( "#main12 span").addClass( "fa-angle-double-down" );
 $("#main12 .fa-angle-double-down").attr("style", "color: #CCC");


  //controller for Q12-1
   $("#sub12-1 span").off('click');
   
   $( "#sub12-1 span").removeClass( "fa-angle-double-right" );
        $( "#sub12-1 span").addClass( "fa fa-angle-double-down" );
 $("#sub12-1 .fa-angle-double-down").attr("style", "color: #CCC");



   //controller for Q12-1-1
 $('#select12-1-1').change(function(){ 
    if($(this).val() == 'Not Approved' || $(this).val() == 'No'){
      $("#12-1-1-1sub").attr("style", "display:table-row");
        $('#12-1-1-1sub').addClass("expanded answered");
     
    }


    else {
      $('#12-1-1-1sub').removeClass("expanded answered");
      $("#12-1-1-1sub").attr("style", "display:none");

     
     
    

    }

});



   //controller for Q12-2

   $("#sub12-2 span").off('click');
   
   $( "#sub12-2 span").removeClass( "fa-angle-double-right" );
        $( "#sub12-2 span").addClass( "fa fa-angle-double-down" );
 $("#sub12-2 .fa-angle-double-down").attr("style", "color: #CCC");



     //controller for Q12-2-1

   $("#sub12-2-1 span").off('click');
   
   $( "#sub12-2-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-1 .fa-angle-double-down").attr("style", "color: #CCC");

 


    //controller for Q12-2-1-1
  
$("#sub12-2-1-1 span").off('click');
   
   $( "#sub12-2-1-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-1-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-1-1 .fa-angle-double-down").attr("style", "color: #CCC");

      //controller for Q12-2-3
  $("#sub12-2-3 span").off('click');
   
   $( "#sub12-2-3  span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-3  span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-3  .fa-angle-double-down").attr("style", "color: #CCC");

  //controller for Q12-2-3-1
  $("#sub12-2-3-1 span").off('click');
   
   $( "#sub12-2-3-1  span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-3-1  span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-3-1  .fa-angle-double-down").attr("style", "color: #CCC");

        //controller for Q12-2-6
 $("#sub12-2-6 span").off('click');
   
   $( "#sub12-2-6 span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-6 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-6 .fa-angle-double-down").attr("style", "color: #CCC");

          //controller for Q12-2-6-1
    $("#sub12-2-6-1 span").off('click');
   
   $( "#sub12-2-6-1  span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-6-1  span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-6-1  .fa-angle-double-down").attr("style", "color: #CCC");








     $('[data-toggle="tooltip"]').tooltip();   



    
});










