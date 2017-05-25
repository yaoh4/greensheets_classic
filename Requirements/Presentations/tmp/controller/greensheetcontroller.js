
$( window ).load(function() {
 



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

  
//code for expand/collapse Grant Number box

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



//initiate treeFy jsjlk

    $("#gsTable").treeFy({
            expanderExpandedClass: 'fa fa-minus-circle',
            expanderCollapsedClass: 'fa fa-plus-circle',
            treeColumn: 0

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

//initiate Datepicker (calendar in form) js

$('.datepicker').datepicker();


//Show & Hide Comment boxes 

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

//Show Comment box when click on icon -- demo only
$('.notes').click(function(e){
     $(this).closest('tr').find('.hiddenNotes').toggle(); 
       $("#sub11-1 span").removeClass("fa fa-minus-circle fa-plus-circle");
     $("#main11 span").removeClass("fa fa-minus-circle fa-plus-circle");

      e.preventDefault();
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

//Preview Mode that expands everything and makes everything gray


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




$('.form-select').change( function () {
     var cval = $(this).val();
     var match = false;
     for (i=0; i< qdata.length; i++) {
         if (qdata[i].answers) {
             for (j=0; j < qdata[i].answers.length; j++) {
                 if (cval === qdata[i].answers[j].aId) {
                     if (qdata[i].answers[j].children) {
                        match = true;
                         for (k=0; k < qdata[i].answers[j].children.length; k++) {
                             showNode(qdata[i].answers[j].children[k]);
                         }
                     }
                 } else {
                     if (qdata[i].answers[j].children) {
                      if (!match) {
                         for (k = 0; k < qdata[i].answers[j].children.length; k++) {
                             hideNode(qdata[i].answers[j].children[k]);
                         }                       
                      }
 
                     }
                 }
 
             }
         }
 
     }
});
 











// initiate tooltip js


     $('[data-toggle="tooltip"]').tooltip();   



    
});










