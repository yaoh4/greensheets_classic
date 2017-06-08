
$( window ).load(function() {
 
   $("#2sub, #3sub, #4sub, #5sub, #6sub, #7sub, #8sub,  #10sub, #sub11-1, #12sub, #sub12-2, #12-1-1sub, #12-1-1-1sub, #sub12-2-1-1").attr("style", "display:none");

  $("#sub1, #main1, #sub3-1, #main3, #sub4-1, #main4, #main6, #sub6, #sub8, #main8, #sub10, #sub10-1-1, #main12, #12sub, #sub12-2, #sub12-1, #sub12-2-1, #sub12-2-1-1, #sub12-2-1-2, #sub12-2-1-3, #sub12-2-1-1-1, #sub12-2-1-1-2, #sub12-2-1-1-3, #sub12-2-2, #sub12-2-3, #sub12-2-4, #sub12-2-3-1, #sub12-2-3-1-1, #sub12-2-5, #sub12-2-6, #sub12-2-6-1, #sub12-2-6-1-1, #3sub, #4sub").attr("style", "display:table-row");
    $("#main1, #main3, #main4, #main6, #main8, #sub10, #main11, #sub11-1, #main12, #sub12-1, #sub12-2-1, #12sub, #sub12-2-3-1, #sub12-2-6, #sub12-2-6-1").removeClass("treetable-collapsed");
     $("#main1, #main3, #main4, #main6, #main8, #sub10, #main11, #sub11-1, #main12, #sub12-1, #sub12-2-1, #12sub, #sub12-2-3-1, #sub12-2-6, #sub12-2-6-1").addClass("treetable-expanded");
  

});

$(document).ready(function(){

    $(".rs").click(function(){
        $("#clearModal").modal();
    });   
   
   
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

    //modal Add Attachments copy function

 $(".add").click(function(){
   $("#table1-1").attr("style", "display:none;");
   $("#table2-2").attr("style", "display:block;");
   $("#fileList1").attr("style", "display:none;");
   $("#fileList2-1").attr("style", "display:block;");
   });


 //modal Add Attachments save function
 $(".saveAttach2").click(function(){
   $("#savedAttach2").attr("src", "images/attachments.gif");
   
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


   

  if ($(this).text() == "View All Sub Questions" )
  { 
  

  $(".allSubs").text("Close All Sub Questions"); 
    $(".expanded").attr('style', 'display:table-row');

        $('tr').removeClass("treetable-collapsed");
     $('tr').addClass("treetable-expanded");
        $('#grantBox, #collapse1').removeClass("treetable-collapsed");
      $('#grantBox, #collapse1').removeClass("treetable-expanded");
     $(".treetable-expanded").attr('style', 'display:table-row');
        $(".lastSub, .sub").attr('style', 'color:#999');
          $('.answered').attr("style", "color: #000"); 
    
    } 


  else 
  { 
   
   $('tr').removeClass("treetable-expanded");
     $('tr').addClass("treetable-collapsed");
     $('#grantBox').removeClass("treetable-collapsed");
      $('#grantBox').removeClass("treetable-expanded");
   $(".lastSub, .sub").attr('style', 'display:none');
           $('.answered').attr("style", "display:table-row; color: #000"); 

     $(".allSubs").text("View All Sub Questions"); 
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
    
   
   $( "#main1 span").removeClass( "fa fa-angle-double-right" );
        $( "#main1 span").addClass( "fa fa-angle-double-down" );
 $("#main1 .fa-angle-double-down").attr("style", "color: #CCC");
 $("#main1 span").closest(".treetable-expander").off('click');
   $("#main1 span").closest(".treetable-expander").css('cursor', 'not-allowed');

///controller for Q2
 
   
   $( "#main2 span").removeClass( "fa-angle-double-right" );
        $( "#main2 span").addClass( "fa-angle-double-down" );
 $("#main2 .fa-angle-double-down").attr("style", "color: #CCC");
 $("#main2 span").closest(".treetable-expander").off('click');
        $("#main2 span").closest(".treetable-expander").css('cursor', 'not-allowed');

//controller for Q3
  
   
   $( "#main3 span").removeClass( "fa-angle-double-right" );
        $( "#main3 span").addClass( "fa-angle-double-down" );
 $("#main3 .fa-angle-double-down").attr("style", "color: #CCC");
 $("#main3 span").closest(".treetable-expander").off('click');
        $("#main3 span").closest(".treetable-expander").css('cursor', 'not-allowed');


//controller for Q3-1
 
   
   $( "#3sub span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#3sub span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#3sub .fa-angle-double-down").attr("style", "color: #CCC");
 $("3sub  span").closest(".treetable-expander").off('click');
        $("#3sub  span").closest(".treetable-expander").css('cursor', 'not-allowed');
 

//controller for Q4


  
   
   $( "#main4 span").removeClass( "fa-angle-double-right" );
        $( "#main4 span").addClass( "fa-angle-double-down" );
 $("#main4 .fa-angle-double-down").attr("style", "color: #CCC");

  $("#main4 span").closest(".treetable-expander").off('click');
        $("#main4 span").closest(".treetable-expander").css('cursor', 'not-allowed');

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


  //controller for Q11-1

   $( "#sub11-1 span").removeClass( "fa-angle-double-right" );
        $( "#sub11-1 span").addClass( "fa-angle-double-down" );
 $("#sub11-1 .fa-angle-double-down").attr("style", "color: #CCC");
$("#sub11-1 span").closest(".treetable-expander").off('click');
        $("#sub11-1 span").closest(".treetable-expander").css('cursor', 'not-allowed');


  //controller for Q12
   
   $( "#main12 span").removeClass( "fa-angle-double-right" );
        $( "#main12 span").addClass( "fa-angle-double-down" );
 $("#main12 .fa-angle-double-down").attr("style", "color: #CCC");
$("#main12 span").closest(".treetable-expander").off('click');
        $("#main12 span").closest(".treetable-expander").css('cursor', 'not-allowed');


//controller for Q12sub

   
   $( "#12sub span").removeClass( "fa-angle-double-right" );
        $( "#12sub span").addClass( "fa fa-angle-double-down" );
 $("#12sub .fa-angle-double-down").attr("style", "color: #CCC");
$("#12sub span").closest(".treetable-expander").off('click');
        $("#12sub span").closest(".treetable-expander").css('cursor', 'not-allowed');
  //controller for Q12-1

   
   $( "#sub12-1 span").removeClass( "fa-angle-double-right" );
        $( "#sub12-1 span").addClass( "fa fa-angle-double-down" );
 $("#sub12-1 .fa-angle-double-down").attr("style", "color: #CCC");
$("#sub12-1 span").closest(".treetable-expander").off('click');
        $("#sub12-1 span").closest(".treetable-expander").css('cursor', 'not-allowed');


   //controller for Q12-1-1
 $('#select12-1-1').change(function(){ 
  $("#sub12-1-1-1 span").closest(".treetable-expander").off('click');
        $("#sub12-1-1-1 span").closest(".treetable-expander").css('cursor', 'not-allowed');

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

 
   
   $( "#sub12-2 span").removeClass( "fa-angle-double-right" );
        $( "#sub12-2 span").addClass( "fa fa-angle-double-down" );
 $("#sub12-2 .fa-angle-double-down").attr("style", "color: #CCC");
$("#sub12-2 span").closest(".treetable-expander").off('click');
        $("#sub12-2 span").closest(".treetable-expander").css('cursor', 'not-allowed');


     //controller for Q12-2-1

  
   
   $( "#sub12-2-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-1 .fa-angle-double-down").attr("style", "color: #CCC");

 $("#sub12-2-1 span").closest(".treetable-expander").off('click');
        $("#sub12-2-1 span").closest(".treetable-expander").css('cursor', 'not-allowed');


    //controller for Q12-2-1-1

   
   $( "#sub12-2-1-1 span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-1-1 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-1-1 .fa-angle-double-down").attr("style", "color: #CCC");
$("#sub12-2-1-1 span").closest(".treetable-expander").off('click');
        $("#sub12-2-1-1 span").closest(".treetable-expander").css('cursor', 'not-allowed');
     
      //controller for Q12-2-3
   
   $( "#sub12-2-3  span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-3  span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-3  .fa-angle-double-down").attr("style", "color: #CCC");
$("#sub12-2-3 span").closest(".treetable-expander").off('click');
        $("#sub12-2-3 span").closest(".treetable-expander").css('cursor', 'not-allowed');
  
  //controller for Q12-2-3-1
   
   $( "#sub12-2-3-1  span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-3-1  span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-3-1  .fa-angle-double-down").attr("style", "color: #CCC");
$("#sub12-2-3-1 span").closest(".treetable-expander").off('click');
        $("#sub12-2-3-1 span").closest(".treetable-expander").css('cursor', 'not-allowed');
        

        //controller for Q12-2-6
   $( "#sub12-2-6 span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-6 span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-6 .fa-angle-double-down").attr("style", "color: #CCC");
$("#sub12-2-6 span").closest(".treetable-expander").off('click');
        $("#sub12-2-6 span").closest(".treetable-expander").css('cursor', 'not-allowed');

          //controller for Q12-2-6-1
   
   $( "#sub12-2-6-1  span").closest(".treetable-expander").removeClass( "fa-angle-double-right" );
        $( "#sub12-2-6-1  span").closest(".treetable-expander").addClass( "fa-angle-double-down" );
 $("#sub12-2-6-1  .fa-angle-double-down").attr("style", "color: #CCC");
$("#sub12-2-6-1 span").closest(".treetable-expander").off('click');
        $("#sub12-2-6-1 span").closest(".treetable-expander").css('cursor', 'not-allowed');





     $('[data-toggle="tooltip"]').tooltip();   



    
});














