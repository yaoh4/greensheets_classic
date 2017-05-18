
$( window ).load(function() {
 
   $("#2sub, #3sub, #4sub, #5sub, #6sub, #7sub, #8sub,  #10sub, #sub11-1, #12sub, #sub12-2, #12-1-1sub, #12-1-1-1sub, #sub12-2-1-1").attr("style", "display:none");
     $("#main1 span, #main2 span, #main3 span, #3sub span, #main4 span, #main5 span, #main6 span, #main7 span, #main8 span, #main9 span, #main10 span, #sub10 span, #main11 span,  #sub11-1 span, #main12 span, #12sub span, #sub12-2 span, #12-1-1sub span, #sub12-2-1-1 span, #sub12-2-6 span, #sub12-2-3 span, #sub12-2-2 span, #sub12-2-1 span, #sub12-2-3-1 span, #sub12-2-6-1 span, #10sub span").removeClass("fa-plus-circle");
  $("#sub1, #main1, #sub3-1, #main3, #sub4-1, #main4, #main6, #sub6, #sub8, #main8, #sub10, #sub10-1-1,  #main12, #sub12-1, #sub12-2, #sub12-2-1, #sub12-2-1-1, #sub12-2-1-2, #sub12-2-1-3, #sub12-2-1-1-1, #sub12-2-1-1-2, #sub12-2-1-1-3, #sub12-2-2, #sub12-2-3, #sub12-2-4, #sub12-2-3-1, #sub12-2-3-1-1, #sub12-2-5, #sub12-2-6, #sub12-2-6-1, #sub12-2-6-1-1, #3sub, #4sub").attr("style", "display:table-row");
    $("#main1, #main3, #main4, #main6, #main8, #sub10,  #main12, #sub12-1, #sub12-2-1, #sub12-2-3-1, #sub12-2-6, #sub12-2-6-1").removeClass("treetable-collapsed");
     $("#main1, #main3, #main4, #main6, #main8, #sub10,  #main12, #sub12-1, #sub12-2-1, #sub12-2-3-1, #sub12-2-6, #sub12-2-6-1").addClass("treetable-expanded");

    $(".question-row span").removeClass("fa-plus-circle");
    window.console&&console.log('foo');

});

function showNode(childId) {
    var nodeId = "#" + childId;
    window.console&&console.log("show Node = " + nodeId);
    $("#" + childId).attr("style", "display:table-row");
    $("#" + childId).addClass("expanded answered");

}

function hideNode(childId) {
    var nodeId = "#" + childId;
    window.console&&console.log("hide Node = " + nodeId);
    $(nodeId).attr("style", "display:none");
    $(nodeId).removeClass("expanded answered");

}

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
            expanderExpandedClass: 'fa fa-minus-circle',
            expanderCollapsedClass: 'fa fa-plus-circle',
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
 $(".form-control, .allNotes, .print, .rs, .submit, #submit, .save").attr('disabled', false);
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
$(".form-control, .allNotes, .print, .rs, .submit, #submit, .save").attr('disabled', true);
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




//controller for Q2
  $("input[name$='optionsRadios2']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#2sub").attr("style", "display:none");
   $('#2sub').removeClass("expanded answered");
     


 
  }
  else {
    $('#2sub').addClass("expanded answered");
    $("#2sub").attr("style", "display:table-row");
    


  }
  
  });

//controller for Q3
  $("input[name$='optionsRadios3']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#3sub").attr("style", "display:none");
 $("#3-1sub").attr("style", "display:none");
  $('#3sub').removeClass("expanded answered");   
     
 
  }
  else {
    $("#3sub").attr("style", "display:table-row");
    $('#3sub').addClass("expanded answered");
    

  }
  
  });


  //controller for Q3-1
  $("input[name$='optionsRadios3-1']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#3-1sub").attr("style", "display:none");
   $('#3-1sub').removeClass("expanded answered"); 
     
    


 
  }
  else {
    $("#3-1sub").attr("style", "display:table-row");
     $('#3-1sub').addClass("expanded answered"); 
     


  }
  
  });

//controller for Q4
  $("input[name$='optionsRadios4']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#4sub").attr("style", "display:none");
    $('#4sub').removeClass("expanded answered");
     
    

 
  }
  else {
    $('#4sub').addClass("expanded answered");
    $("#4sub").attr("style", "display:table-row");
     

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
 $('#select6').change(function(){ 
    if($(this).val() == 'Not Approved'){
      $("#6sub").attr("style", "display:table-row");
      $('#6sub').addClass("expanded answered");
     
    }
    else if($(this).val() == 'Yes Approved'){
      $('#6sub').addClass("expanded answered");
      $("#6sub").attr("style", "display:table-row");
    
    }

    else {

      $("#6sub").attr("style", "display:none");
      $('#6sub').removeClass("expanded answered");
     
     

    }

});

 $('.form-select').change( function () {
     window.console&&console.log("Selected " + $(this).val());
     var cval = $(this).val();
     for (i=0; i< qdata.length; i++) {
         if (qdata[i].answers) {
             for (j=0; j < qdata[i].answers.length; j++) {
                 if (cval === qdata[i].answers[j].aId) {
                     if (qdata[i].answers[j].children) {
                         for (k=0; k < qdata[i].answers[j].children.length; k++) {
                             showNode(qdata[i].answers[j].children[k]);
                         }
                     }
                 } else {
                     if (qdata[i].answers[j].children) {
                         for (k = 0; k < qdata[i].answers[j].children.length; k++) {
                             hideNode(qdata[i].answers[j].children[k]);
                         }
                     }
                 }

             }
         }

     }
     window.console&&console.log("QDATA size=" + qdata.length);
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
    if($(this).val() == 'Not Approved'){
      $("#8sub").attr("style", "display:table-row");
      $('#8sub').addClass("expanded answered");
    
    }


    else {

      $("#8sub").attr("style", "display:none");
      $('#8sub').removeClass("expanded answered");
     
    

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
     $('#12sub').removeClass("expanded answered");
    $("#sub12-2").attr("style", "display:none");
      $("#12-1-1sub").attr("style", "display:none");
        $("#12-1-1-1sub").attr("style", "display:none");
      $("#sub12-2-1").attr("style", "display:none");
   $("#sub12-2-2").attr("style", "display:none");
     $("#sub12-2-3").attr("style", "display:none"); 
      $("#sub12-2-4").attr("style", "display:none");
       $("#sub12-2-5").attr("style", "display:none");
        $("#sub12-2-6").attr("style", "display:none");
        $("#sub12-2-1-1").attr("style", "display:none");
          $("#sub12-2-1-1-1").attr("style", "display:none");
   $("#sub12-2-1-1-2").attr("style", "display:none");
    $("#sub12-2-1-1-3").attr("style", "display:none");
       $("#sub12-2-3-1").attr("style", "display:none");
     
      $("input[name$='optionsRadios12-2']").prop('checked', false);
         
      $("input[name$='optionsRadios12-1']").prop('checked', false);

 
  }
  else {
    $("#12sub").attr("style", "display:table-row");
    $('#12sub').addClass("expanded answered");
      $("#sub12-2").attr("style", "display:table-row");
    


  }
  
  });


  //controller for Q12-1
  $("input[name$='optionsRadios12-1']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#12-1-1sub").attr("style", "display:none");
   $('#12-1-1sub').removeClass("expanded answered");
     
    


 
  }
  else {
    $("#12-1-1sub").attr("style", "display:table-row");
      $('#12-1-1sub').addClass("expanded answered");
     

  }
  
  });



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
  $("input[name$='optionsRadios12-2']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#sub12-2-1").attr("style", "display:none");
   $('#sub12-2-1').removeClass("expanded answered");
   $("#sub12-2-2").attr("style", "display:none");
     $("#sub12-2-3").attr("style", "display:none"); 
      $("#sub12-2-4").attr("style", "display:none");
       $("#sub12-2-5").attr("style", "display:none");
        $("#sub12-2-6").attr("style", "display:none");
          $("#sub12-2-3-1").attr("style", "display:none");
           $("#sub12-2-6-1").attr("style", "display:none");
             $("#sub12-2-6-1-1").attr("style", "display:none");
       
     
      $("input[name$='optionsRadios12-2-3']").prop('checked', false);
      $("input[name$='optionsRadios12-2-6']").prop('checked', false);


 
  }
  else {
    $("#sub12-2-1").attr("style", "display:table-row");
    $('#sub12-2-1').addClass("expanded answered");
     $("#sub12-2-2").attr("style", "display:table-row");
      $("#sub12-2-3").attr("style", "display:table-row");
      $("#sub12-2-4").attr("style", "display:table-row");
      $("#sub12-2-5").attr("style", "display:table-row");
      $("#sub12-2-6").attr("style", "display:table-row");


  }
  
  });



     //controller for Q12-2-1
 $('#select12-2-1').change(function(){ 
    if($(this).val() == 'Not Exempt'){
      $("#sub12-2-1-1").attr("style", "display:table-row");
      $('#sub12-2-1-1').addClass("expanded answered");
     
 
    }


    else {

      $("#sub12-2-1-1").attr("style", "display:none");
      $('#sub12-2-1-1').removeClass("expanded answered");
     
    

           
    }

});


    //controller for Q12-2-1-1
  $("input[name$='optionsRadios12-2-1-1']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#sub12-2-1-1-1").attr("style", "display:none");
   $('#sub12-2-1-1-1').removeClass("expanded answered");
   $("#sub12-2-1-1-2").attr("style", "display:none");
    $("#sub12-2-1-1-3").attr("style", "display:none"); 
     


 
  }
  else {
    $("#sub12-2-1-1-1").attr("style", "display:table-row");
     $('#sub12-2-1-1-1').addClass("expanded answered");
    $("#sub12-2-1-1-2").attr("style", "display:table-row");
     $("#sub12-2-1-1-3").attr("style", "display:table-row");
     

  }
  
  });


      //controller for Q12-2-3
  $("input[name$='optionsRadios12-2-3']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
    $("#sub12-2-3-1").attr("style", "display:table-row");
     $('#sub12-2-3-1').addClass("expanded answered");
     


 
  }
  else {
   

      $("#sub12-2-3-1").attr("style", "display:none");
        $('#sub12-2-3-1').removeClass("expanded answered");
     $("#sub12-2-3-1").attr("style", "display:none");
    

  }
  
  });

        //controller for Q12-2-6
  $("input[name$='optionsRadios12-2-6']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
    $("#sub12-2-6-1").attr("style", "display:table-row");
      $('#sub12-2-6-1').addClass("expanded answered");
     
        


 
  }
  else {
   

      $("#sub12-2-6-1").attr("style", "display:none");
       $('#sub12-2-6-1').removeClass("expanded answered");
     $("#sub12-2-6-1").attr("style", "display:none");
    

  }
  
  });

          //controller for Q12-2-6-1
  $("input[name$='optionsRadios12-2-6-1']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
    $("#sub12-2-6-1-1").attr("style", "display:table-row");
     $('#sub12-2-6-1-1').addClass("expanded answered");
     

 
  }
  else {
   

      $("#sub12-2-6-1-1").attr("style", "display:none");
       $('#sub12-2-6-1-1').removeClass("expanded answered");
     $("#sub12-2-6-1-1").attr("style", "display:none");
     

  }
  
  });




     $('[data-toggle="tooltip"]').tooltip();   



    
});










