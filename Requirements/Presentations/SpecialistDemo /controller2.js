
$( window ).load(function() {
 
   $("#1sub, #2sub, #3sub, #4sub, #5sub, #6sub, #7sub, #8sub,  #10sub, #sub11-1, #12sub, #sub12-2, #12-1-1sub, #12-1-1-1sub, #sub12-2-1-1").attr("style", "display:none");
     $("#main1 span, #main2 span, #main3 span, #3sub span, #main4 span, #main5 span, #main6 span, #main7 span, #main8 span, #main9 span, #main10 span, #sub10 span, #main11 span, #sub11-1 span, #main12 span, #12-1-1sub span, #sub12-2-1-1 span, #sub12-2-6 span, #sub12-2-3 span, #sub12-2-2 span").removeClass("fa-plus-circle");


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
     $(this).text("Hide All Comments"); 

      $("#main11, #sub11-1").removeClass("treetable-collapsed");
     $("#main11, #sub11-1" ).addClass("treetable-expanded");
     $("#sub11-1, #sub11-1-1").attr('style','display: table-row');
       $("#main11").find('span').toggleClass("fa-minus-circle fa-plus-circle"); 
       $("#sub11-1").find('.treetable-expander').toggleClass("fa-minus-circle fa-plus-circle"); 
 

  } 
  else 
  { 
     $(this).text("View All Comments"); 
     $("#main11, #sub11-1").removeClass("treetable-expanded");
     $("#main11, #sub11-1" ).addClass("treetable-collapsed");
     $("#sub11-1, #sub11-1-1").attr('style','display: none');
       $("#main11").find('span').toggleClass("fa-plus-circle fa-minus-circle"); 
       $("#sub11-1").find('.treetable-expander').toggleClass("fa-plus-circle fa-minus-circle"); 
  }; 
    });


    //Comment box counter

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


   

  if ($(this).text() == "Expand All" )
  { 
  

  $(this).text("Collapse All"); 
    $(".expanded").attr('style', 'display:table-row');

    if ($('.lastSub', '.sub').is('style', 'display:table-row')) {
        $('tr').removeClass("treetable-collapsed");
     $('tr').addClass("treetable-expanded");
    
      $('.lastSub', '.sub').attr('style', 'display:table-row');
    
    } 


    else  {
        $('tr').removeClass("treetable-expanded");
     $('tr').addClass("treetable-collapsed");
     $('.lastSub', '.sub').attr('style', 'display:none');
     console.log("test");

    }

   
       $('.fa-plus-circle:not(.details)').toggleClass("fa-plus-circle fa-minus-circle"); 

  } 

  else 
  { 
   
   $('tr').removeClass("treetable-expanded");
     $('tr').addClass("treetable-collapsed");
      $('.sub').attr('style','display: none');
      $('.lastSub').attr('style','display: none');
      $('.fa-minus-circle').toggleClass("fa-minus-circle fa-plus-circle"); 

     $(this).text("Expand All"); 
  }


    });

$('.notes').click(function(e){
     $(this).closest('tr').find('.hiddenNotes').toggle(); 
      e.preventDefault();
});



$('#infoBox').click(function(){
     $(this).find('i').toggleClass("fa-minus-circle fa-plus-circle"); 
});


  //controller for Q1
 $('#select1').change(function(){ 
    if($(this).val() == 'Changed and Not Approved'){
      $("#1sub").attr("style", "display:table-row");
      $('#1sub').addClass("expanded");
     $("#main1").find('span').removeClass("fa-plus-circle");
      $("#main1").find('span').addClass("fa");
      $("#main1").find('span').addClass("fa-minus-circle");
    }


    else {

      $("#1sub").attr("style", "display:none");
      $('#1sub').removeClass("expanded");
     
     $("#main1").find('span').removeClass("fa");
     $("#main1").find('span').removeClass("fa-plus-circle");
      $("#main1").find('span').removeClass("fa-minus-circle");

    }

});

//controller for Q2
  $("input[name$='optionsRadios2']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#2sub").attr("style", "display:none");
   $('#2sub').removeClass("expanded");
     
     $("#main2").find('span').removeClass("fa");
     $("#main2").find('span').removeClass("fa-plus-circle");
      $("#main2").find('span').removeClass("fa-minus-circle");

 
  }
  else {
    $('#2sub').addClass("expanded");
    $("#2sub").attr("style", "display:table-row");
     $("#main2").find('span').removeClass("fa-plus-circle");
      $("#main2").find('span').addClass("fa");
      $("#main2").find('span').addClass("fa-minus-circle");


  }
  
  });

//controller for Q3
  $("input[name$='optionsRadios3']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#3sub").attr("style", "display:none");
 $("#3-1sub").attr("style", "display:none");
  $('#3sub').removeClass("expanded");   
     $("#main3").find('span').removeClass("fa");
     $("#main3").find('span').removeClass("fa-plus-circle");
      $("#main3").find('span').removeClass("fa-minus-circle");

 
  }
  else {
    $("#3sub").attr("style", "display:table-row");
    $('#3sub').addClass("expanded");
     $("#main3").find('span').removeClass("fa-plus-circle");
      $("#main3").find('span').addClass("fa");
      $("#main3").find('span').addClass("fa-minus-circle");


  }
  
  });


  //controller for Q3-1
  $("input[name$='optionsRadios3-1']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#3-1sub").attr("style", "display:none");
   $('#3-1sub').removeClass("expanded"); 
     
     $("#3sub").find('span').removeClass("fa");
     $("#3sub").find('span').removeClass("fa-plus-circle");
      $("#3sub").find('span').removeClass("fa-minus-circle");


 
  }
  else {
    $("#3-1sub").attr("style", "display:table-row");
     $('#3-1sub').addClass("expanded"); 
     $("#3sub").find('span').removeClass("fa-plus-circle");
      $("#3sub").find('span').addClass("fa");
      $("#3sub").find('span').addClass("fa-minus-circle");
       $("#3sub").find('.treetable-indent').removeClass("fa-minus-circle");


  }
  
  });

//controller for Q4
  $("input[name$='optionsRadios4']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#4sub").attr("style", "display:none");
    $('#4sub').removeClass("expanded");
     
     $("#main4").find('span').removeClass("fa");
     $("#main4").find('span').removeClass("fa-plus-circle");
      $("#main4").find('span').removeClass("fa-minus-circle");

 
  }
  else {
    $('#4sub').addClass("expanded");
    $("#4sub").attr("style", "display:table-row");
     $("#main4").find('span').removeClass("fa-plus-circle");
      $("#main4").find('span').addClass("fa");
      $("#main4").find('span').addClass("fa-minus-circle");


  }
  
  });

    //controller for Q5
  $("input[name$='optionsRadios5']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#5sub").attr("style", "display:none");
   $('#5sub').removeClass("expanded");
     
     $("#main5").find('span').removeClass("fa");
     $("#main5").find('span').removeClass("fa-plus-circle");
      $("#main5").find('span').removeClass("fa-minus-circle");

 
  }
  else {
    $('#5sub').addClass("expanded");
    $("#5sub").attr("style", "display:table-row");
     $("#main5").find('span').removeClass("fa-plus-circle");
      $("#main5").find('span').addClass("fa");
      $("#main5").find('span').addClass("fa-minus-circle");


  }
  
  });


  //controller for Q9
  $("input[name$='optionsRadios16']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#9sub").attr("style", "display:none");
   $('#9sub').removeClass("expanded");
     
     $("#main9").find('span').removeClass("fa");
     $("#main9").find('span').removeClass("fa-plus-circle");
      $("#main9").find('span').removeClass("fa-minus-circle");

 
  }
  else {
    $('#9sub').addClass("expanded");
    $("#9sub").attr("style", "display:table-row");
     $("#main9").find('span').removeClass("fa-plus-circle");
      $("#main9").find('span').addClass("fa");
      $("#main9").find('span').addClass("fa-minus-circle");


  }
  
  });

//controller for Q6
 $('#select6').change(function(){ 
    if($(this).val() == 'Not Approved'){
      $("#6sub").attr("style", "display:table-row");
      $('#6sub').addClass("expanded");
     $("#main6").find('span').removeClass("fa-plus-circle");
      $("#main6").find('span').addClass("fa");
      $("#main6").find('span').addClass("fa-minus-circle");
    }
    else if($(this).val() == 'Yes Approved'){
      $('#6sub').addClass("expanded");
      $("#6sub").attr("style", "display:table-row");
     $("#main6").find('span').removeClass("fa-plus-circle");
      $("#main6").find('span').addClass("fa");
      $("#main6").find('span').addClass("fa-minus-circle");
    }

    else {

      $("#6sub").attr("style", "display:none");
      $('#6sub').removeClass("expanded");
     
     $("#main6").find('span').removeClass("fa");
     $("#main6").find('span').removeClass("fa-plus-circle");
      $("#main6").find('span').removeClass("fa-minus-circle");

    }

});



 //controller for Q7
 $('#select7').change(function(){ 
    if($(this).val() == 'Not Approved' || $(this).val() == 'Yes Approved' || $(this).val() == 'Not Approved No Impact'){
      $("#7sub").attr("style", "display:table-row");
      $('#7sub').addClass("expanded");
     $("#main7").find('span').removeClass("fa-plus-circle");
      $("#main7").find('span').addClass("fa");
      $("#main7").find('span').addClass("fa-minus-circle");
    }
    

    else {

      $("#7sub").attr("style", "display:none");
      $('#7sub').removeClass("expanded");
     
     $("#main7").find('span').removeClass("fa");
     $("#main7").find('span').removeClass("fa-plus-circle");
      $("#main7").find('span').removeClass("fa-minus-circle");

    }

});

  //controller for Q8
 $('#select8').change(function(){ 
    if($(this).val() == 'Not Approved'){
      $("#8sub").attr("style", "display:table-row");
      $('#8sub').addClass("expanded");
     $("#main8").find('span').removeClass("fa-plus-circle");
      $("#main8").find('span').addClass("fa");
      $("#main8").find('span').addClass("fa-minus-circle");
    }


    else {

      $("#8sub").attr("style", "display:none");
      $('#8sub').removeClass("expanded");
     
     $("#main8").find('span').removeClass("fa");
     $("#main8").find('span').removeClass("fa-plus-circle");
      $("#main8").find('span').removeClass("fa-minus-circle");

    }

});



 //controller for Q10
  $("input[name$='optionsRadios10']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#10sub").attr("style", "display:none");
   $('#10sub').removeClass("expanded");
    $("#10sub1").attr("style", "display:none");
     
     $("#main10").find('span').removeClass("fa");
     $("#main10").find('span').removeClass("fa-plus-circle");
      $("#main10").find('span').removeClass("fa-minus-circle");

 
  }
  else {
    $("#10sub").attr("style", "display:table-row");
    $('#10sub').addClass("expanded");
     $("#main10").find('span').removeClass("fa-plus-circle");
      $("#main10").find('span').addClass("fa");
      $("#main10").find('span').addClass("fa-minus-circle");
       $("#10sub").find('.treetable-expander').removeClass("fa-plus-circle");


  }
  
  });


  //controller for Q10-1
  $("input[name$='optionsRadios10-1']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#10sub1").attr("style", "display:none");

     $('#10sub1').removeClass("expanded");
     $("#10sub").find('span').removeClass("fa");
     $("#10sub").find('span').removeClass("fa-plus-circle");
      $("#10sub").find('span').removeClass("fa-minus-circle");


 
  }
  else {
    $("#10sub1").attr("style", "display:table-row");
     $('#10sub1').addClass("expanded");
     $("#10sub").find('span').removeClass("fa-plus-circle");
      $("#10sub").find('span').addClass("fa");
      $("#10sub").find('span').addClass("fa-minus-circle");
     $("#10sub").find('.treetable-indent').removeClass("fa-minus-circle");


  }
  
  });


  //controller for Q11
  $("input[name$='optionsRadios11']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#sub11-1").attr("style", "display:none");
    $('#sub11-1').removeClass("expanded");
    $("#sub11-1-1").attr("style", "display:none");
     
     $("#main11").find('span').removeClass("fa");
     $("#main11").find('span').removeClass("fa-plus-circle");
      $("#main11").find('span').removeClass("fa-minus-circle");

 
  }
  else {
    $("#sub11-1").attr("style", "display:table-row");
     $('#sub11-1').addClass("expanded");
     $("#main11").find('span').removeClass("fa-plus-circle");
      $("#main11").find('span').addClass("fa");
      $("#main11").find('span').addClass("fa-minus-circle");
       $("#sub11-1").find('.treetable-expander').removeClass("fa-plus-circle");


  }
  
  });


  //controller for Q11-1
  $("input[name$='optionsRadios11-1']").click(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#sub11-1-1").attr("style", "display:none");
    $('#sub11-1-1').removeClass("expanded");
     
     $("#sub11-1").find('span').removeClass("fa");
     $("#sub11-1").find('span').removeClass("fa-plus-circle");
      $("#sub11-1").find('span').removeClass("fa-minus-circle");


 
  }
  else {
    $("#sub11-1-1").attr("style", "display:table-row");
      $('#sub11-1-1').addClass("expanded");
     $("#sub11-1").find('span').removeClass("fa-plus-circle");
      $("#sub11-1").find('span').addClass("fa");
      $("#sub11-1").find('span').addClass("fa-minus-circle");
     $("#sub11-1").find('.treetable-indent').removeClass("fa-minus-circle");


  }
  
  });



  //controller for Q12
  $("input[name$='optionsRadios12']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#12sub").attr("style", "display:none");
     $('#12sub').removeClass("expanded");
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
     $("#main12").find('span').removeClass("fa");
     $("#main12").find('span').removeClass("fa-plus-circle");
      $("#main12").find('span').removeClass("fa-minus-circle");
       $("#sub12-2").find('span').removeClass("fa-minus-circle");
      $("input[name$='optionsRadios12-2']").prop('checked', false);
         $("#12sub").find('span').removeClass("fa-minus-circle");
      $("input[name$='optionsRadios12-1']").prop('checked', false);

 
  }
  else {
    $("#12sub").attr("style", "display:table-row");
    $('#12sub').addClass("expanded");
      $("#sub12-2").attr("style", "display:table-row");
     $("#main12").find('span').removeClass("fa-plus-circle");
      $("#main12").find('span').addClass("fa");
      $("#main12").find('span').addClass("fa-minus-circle");
      $("#sub12-2").find('.treetable-expander').removeClass("fa-plus-circle");
       $("#12sub").find('.treetable-expander').removeClass("fa-plus-circle");


  }
  
  });


  //controller for Q12-1
  $("input[name$='optionsRadios12-1']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
   $("#12-1-1sub").attr("style", "display:none");
   $('#12-1-1sub').removeClass("expanded");
     
     $("#12sub").find('span').removeClass("fa");
     $("#12sub").find('span').removeClass("fa-plus-circle");
      $("#12sub").find('span').removeClass("fa-minus-circle");


 
  }
  else {
    $("#12-1-1sub").attr("style", "display:table-row");
      $('#12-1-1sub').addClass("expanded");
     $("#12sub").find('span').removeClass("fa-plus-circle");
      $("#12sub").find('span').addClass("fa");
      $("#12sub").find('span').addClass("fa-minus-circle");
     $("#12sub").find('.treetable-indent').removeClass("fa-minus-circle");
      $("#12-1-1sub").find('.treetable-expander').removeClass("fa-plus-circle");

  }
  
  });



   //controller for Q12-1-1
 $('#select12-1-1').change(function(){ 
    if($(this).val() == 'Not Approved' || $(this).val() == 'No'){
      $("#12-1-1-1sub").attr("style", "display:table-row");
        $('#12-1-1-1sub').addClass("expanded");
     $("#12-1-1sub").find('span').removeClass("fa-plus-circle");
    
        $("#12-1-1-1sub").find('.treetable-indent').removeClass("fa-minus-circle");
      $("#12-1-1-1sub").find('.treetable-expander').removeClass("fa-minus-circle");
    }


    else {
      $('#12-1-1-1sub').removeClass("expanded");
      $("#12-1-1-1sub").attr("style", "display:none");

     
     $("#12-1-1sub").find('span').removeClass("fa");
     $("#12-1-1sub").find('span').removeClass("fa-plus-circle");
      $("#12-1-1sub").find('span').removeClass("fa-minus-circle");
    

    }

});



   //controller for Q12-2
  $("input[name$='optionsRadios12-2']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#sub12-2-1").attr("style", "display:none");
   $('#sub12-2-1').removeClass("expanded");
   $("#sub12-2-2").attr("style", "display:none");
     $("#sub12-2-3").attr("style", "display:none"); 
      $("#sub12-2-4").attr("style", "display:none");
       $("#sub12-2-5").attr("style", "display:none");
        $("#sub12-2-6").attr("style", "display:none");
          $("#sub12-2-3-1").attr("style", "display:none");
           $("#sub12-2-6-1").attr("style", "display:none");
             $("#sub12-2-6-1-1").attr("style", "display:none");
       
     $("#sub12-2").find('span').removeClass("fa");
     $("#sub12-2").find('span').removeClass("fa-plus-circle");
      $("#sub12-2").find('span').removeClass("fa-minus-circle");
      $("#sub12-2-3").find('span').removeClass("fa-minus-circle");
      $("#sub12-2-6").find('span').removeClass("fa-minus-circle");
           $("#sub12-2-3").find('span').removeClass("fa-minus-circle");
      $("input[name$='optionsRadios12-2-3']").prop('checked', false);
      $("input[name$='optionsRadios12-2-6']").prop('checked', false);


 
  }
  else {
    $("#sub12-2-1").attr("style", "display:table-row");
    $('#sub12-2-1').addClass("expanded");
     $("#sub12-2-2").attr("style", "display:table-row");
      $("#sub12-2-3").attr("style", "display:table-row");
      $("#sub12-2-4").attr("style", "display:table-row");
      $("#sub12-2-5").attr("style", "display:table-row");
      $("#sub12-2-6").attr("style", "display:table-row");

     $("#sub12-2").find('span').removeClass("fa-plus-circle");
      $("#sub12-2").find('span').addClass("fa");
      $("#sub12-2").find('span').addClass("fa-minus-circle");
     $("#sub12-2").find('.treetable-indent').removeClass("fa-minus-circle");
      $("#sub12-2-1").find('.treetable-expander').removeClass("fa-plus-circle");

  }
  
  });



     //controller for Q12-2-1
 $('#select12-2-1').change(function(){ 
    if($(this).val() == 'Not Exempt'){
      $("#sub12-2-1-1").attr("style", "display:table-row");
      $('#sub12-2-1-1').addClass("expanded");
     $("#sub12-2-1").find('span').removeClass("fa-plus-circle");
     $("#sub12-2-1").find('.treetable-expander').addClass("fa");
       $("#sub12-2-1").find('.treetable-expander').addClass("fa-minus-circle");
 
    }


    else {

      $("#sub12-2-1-1").attr("style", "display:none");
      $('#sub12-2-1-1').removeClass("expanded");
     
     $("#sub12-2-1").find('span').removeClass("fa");
     $("#sub12-2-1").find('span').removeClass("fa-plus-circle");
      $("#sub12-2-1").find('span').removeClass("fa-minus-circle");

             $("#sub12-2-1").find('.treetable-indent').removeClass("fa-minus-circle");
      $("#sub12-2-1").find('.treetable-expander').removeClass("fa-minus-circle");
    

    }

});


    //controller for Q12-2-1-1
  $("input[name$='optionsRadios12-2-1-1']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
   $("#sub12-2-1-1-1").attr("style", "display:none");
   $('#sub12-2-1-1-1').removeClass("expanded");
   $("#sub12-2-1-1-2").attr("style", "display:none");
    $("#sub12-2-1-1-3").attr("style", "display:none"); 
     $("#sub12-2-1-1").find('span').removeClass("fa");
     $("#sub12-2-1-1").find('span').removeClass("fa-plus-circle");
      $("#sub12-2-1-1").find('span').removeClass("fa-minus-circle");


 
  }
  else {
    $("#sub12-2-1-1-1").attr("style", "display:table-row");
     $('#sub12-2-1-1-1').addClass("expanded");
    $("#sub12-2-1-1-2").attr("style", "display:table-row");
     $("#sub12-2-1-1-3").attr("style", "display:table-row");
     $("#sub12-2-1-1").find('span').removeClass("fa-plus-circle");
      $("#sub12-2-1-1").find('span').addClass("fa");
      $("#sub12-2-1-1").find('span').addClass("fa-minus-circle");
     $("#sub12-2-1-1").find('.treetable-indent').removeClass("fa-minus-circle");
      $("#sub12-2-1-1").find('.treetable-expander').removeClass("fa-plus-circle");

  }
  
  });


      //controller for Q12-2-3
  $("input[name$='optionsRadios12-2-3']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='No') {
    $("#sub12-2-3-1").attr("style", "display:table-row");
     $('#sub12-2-3-1').addClass("expanded");
     $("#sub12-2-3").find('span').removeClass("fa-plus-circle");
      $("#sub12-2-3").find('span').addClass("fa");
      $("#sub12-2-3").find('span').addClass("fa-minus-circle");
         $("#sub12-2-3").find('.treetable-indent').removeClass("fa-minus-circle");
      $("#sub12-2-3").find('.treetable-expander').removeClass("fa-plus-circle");
     $("#sub12-2-3-1").find('.treetable-indent').removeClass("fa-minus-circle");
      $("#sub12-2-3-1").find('.treetable-expander').removeClass("fa-plus-circle");


 
  }
  else {
   

      $("#sub12-2-3-1").attr("style", "display:none");
        $('#sub12-2-3-1').removeClass("expanded");
     $("#sub12-2-3-1").attr("style", "display:none");
     $("#sub12-2-3").find('span').removeClass("fa");
     $("#sub12-2-3").find('span').removeClass("fa-plus-circle");
      $("#sub12-2-3").find('span').removeClass("fa-minus-circle");

  }
  
  });

        //controller for Q12-2-6
  $("input[name$='optionsRadios12-2-6']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
    $("#sub12-2-6-1").attr("style", "display:table-row");
      $('#sub12-2-6-1').addClass("expanded");
     $("#sub12-2-6").find('span').removeClass("fa-plus-circle");
      $("#sub12-2-6").find('span').addClass("fa");
      $("#sub12-2-6").find('span').addClass("fa-minus-circle");
         $("#sub12-2-6").find('.treetable-indent').removeClass("fa-minus-circle");
      $("#sub12-2-6").find('.treetable-expander').removeClass("fa-plus-circle");
     $("#sub12-2-6-1").find('.treetable-indent').removeClass("fa-minus-circle");
      $("#sub12-2-6-1").find('.treetable-expander').removeClass("fa-plus-circle");


 
  }
  else {
   

      $("#sub12-2-6-1").attr("style", "display:none");
       $('#sub12-2-6-1').removeClass("expanded");
     $("#sub12-2-6-1").attr("style", "display:none");
     $("#sub12-2-6").find('span').removeClass("fa");
     $("#sub12-2-6").find('span').removeClass("fa-plus-circle");
      $("#sub12-2-6").find('span').removeClass("fa-minus-circle");

  }
  
  });

          //controller for Q12-2-6-1
  $("input[name$='optionsRadios12-2-6-1']").change(function(){
  var radio_value = $(this).val();
  if(radio_value=='Yes') {
    $("#sub12-2-6-1-1").attr("style", "display:table-row");
     $('#sub12-2-6-1-1').addClass("expanded");
     $("#sub12-2-6-1").find('span').removeClass("fa-plus-circle");
      $("#sub12-2-6-1").find('span').addClass("fa");
      $("#sub12-2-6-1").find('span').addClass("fa-minus-circle");
         $("#sub12-2-6-1").find('.treetable-indent').removeClass("fa-minus-circle");
      $("#sub12-2-6-1").find('.treetable-expander').removeClass("fa-plus-circle");
     $("#sub12-2-6-1-1").find('.treetable-indent').removeClass("fa-minus-circle");
      $("#sub12-2-6-1-1").find('.treetable-expander').removeClass("fa-plus-circle");


 
  }
  else {
   

      $("#sub12-2-6-1-1").attr("style", "display:none");
       $('#sub12-2-6-1-1').removeClass("expanded");
     $("#sub12-2-6-1-1").attr("style", "display:none");
     $("#sub12-2-6-1").find('span').removeClass("fa");
     $("#sub12-2-6-1").find('span').removeClass("fa-plus-circle");
      $("#sub12-2-6-1").find('span').removeClass("fa-minus-circle");

  }
  
  });




     $('[data-toggle="tooltip"]').tooltip();   



    
});










