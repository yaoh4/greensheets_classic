$(document).ready(function(){
  
   $('.chosen-select', "#mech").chosen();
$("#mech").chosen({width: '100%'});
 
 $(".chosen-select2").chosen({width: '100%'});


  $( ".no-sort" ).removeClass( ".sorting" );

$('#searchResults').dataTable( {
  "bSort": true,
  "bFilter": false,
  "dom": '<"top"lip>rt<"bottom"lip>',

   "order": [[ 0, "asc" ]],

   "language": {
                "info": "&nbsp; (Displaying _END_ out of _TOTAL_ )  ",
              },

       columnDefs: [
         { targets: ['status'], type: 'alt-string'},
 
         { targets: 'no-sort', orderable: false },

         {targets: 'no-sort', aaSorting: false },

        
         ]
    } );


   



//Search Function

 $("#search-btn").click(function(){
      $("#pSearch").attr('style', 'display: block');

           $("body, html").animate({ 
        scrollTop: $("#pSearch").offset().top -10
    }, 700);
    });

 
 //tooltip
$('[data-toggle="tooltip"]').tooltip();   





// collapse/expand search panel

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




 });   









