
//check all function
$(document).ready(function(){


  
          $('button.has-spinner').addClass('active');


 $("#checkAll").click(function () {
     $('input:checkbox').not(this).prop('checked', this.checked);
 });

  $("#checkAll2").click(function () {
     $('.checkItem').not(this).prop('checked', this.checked);
 });


  $("#rejectProgram").click(function () {
     $('#rejected').attr('style', 'display:block; margin-top: 15px;');
     $('#promoted').attr('style', 'display:none;');
     $('#programCompeting').attr('style', 'display:none;');
     $('#1').attr('style', 'display:none;');
     $('#content2').addClass('active');
     $('#2').addClass('active');
     $('#rejectModal').modal('toggle');
 });

  $("#promoteProgram").click(function () {
   

    
      $('#programCompeting').attr('style', 'display:none;');
     $('#1').attr('style', 'display:none;');
      $('#content2').addClass('active');
     $('#2').addClass('active');
     $('#promoted').attr('style', 'display:block; margin-top: 15px;');
      $('#rejected').attr('style', 'display:none;');
     $('#promoteModal').modal('toggle');
 });

    


  function closeWindow() { 
window.open('','_parent',''); 
window.close(); 
};


 });
//collapsable panels

$(document).on('click', '.panel-heading span.clickable', function(e){
    var $this = $(this);
  if(!$this.hasClass('panel-collapsed')) {
    $this.parents('.panel').find('.panel-body').slideUp();
    $this.addClass('panel-collapsed');
    $this.find('i').removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
  } else {
    $this.parents('.panel').find('.panel-body').slideDown();
    $this.removeClass('panel-collapsed');
    $this.find('i').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
  }
});




  