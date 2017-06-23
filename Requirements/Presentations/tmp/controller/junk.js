$(function(){
var text_max = 2000;
    $('.exampleTextarea').on('keyup', function(){
        var wordsLength = $(this).val().length;
        var text_remaining = text_max - wordsLength;
        $(this).next().find('.count_message').html(text_remaining);
         if (text_remaining === 0 ) {
         console.log('max');
         $( ".exampleTextarea" ).after("<div style='display: inline-block' class='warning1 has-error'>Explaination is Required</div>");
        }
        else {
          $(this).next().find('.count_message').html(text_remaining);
           $( ".exampleTextarea" ).remove("<div style='display: inline-block' class='warning1 has-error'>Explaination is Required</div>");
        }
    });

});


    //Comment box counter
var text_max = 4000;
$(".exampleTextarea").bind("input", function() {
  var $this = $(this);
  var showMessage = false;
  var text_remaining = text_max-$this.val().length;
  if (text_remaining<0) { text_remaining = 0 }

  var message = "<div style='display: inline-block' class='warning2 has-error'>Maximum characters reached!</div>"
  
  $(this).next().find('.count_message2').html(text_remaining);
  if ($this.val().length>text_max) {
    if (!$(".warning2.has-error").length) {
          $( ".exampleTextarea" ).after(message);
          $($this).attr("style", "border-color:#a94442");
    };
  }
  else {
    $(".warning2").remove();
    $($this).removeAttr("style", "border-color:#a94442");
  };
});



var text_max = 2000;
$(".exampleTextarea").bind("input", function() {
  var $this = $(this);
  var showMessage = false;
  var text_remaining = text_max-$this.val().length;
  if (text_remaining<0) { text_remaining = 0 }

  var message = "<div style='display: inline-block' class='warning1 has-error'>Maximum characters reached!</div>";
  
  $(this).next().find('.count_message').html(text_remaining);
  if ($this.val().length==text_max) {
    if (!$(".warning1.has-error").length) {
          $( ".exampleTextarea" ).after(message);
          $($this).attr("style", "border-color:#a94442");
    };
  }
  else {
    $(".warning1").remove();
    $($this).removeAttr("style", "border-color:#a94442");
  }
})