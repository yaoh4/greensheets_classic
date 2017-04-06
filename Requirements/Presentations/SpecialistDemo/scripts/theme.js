

/*
 * Image preview script 
 * powered by jQuery (http://www.jquery.com)
 * 
 * written by Alen Grakalic (http://cssglobe.com)
 * 
 * for more info visit http://cssglobe.com/post/1695/easiest-tooltip-and-image-preview-using-jquery
 *
 */
 



// starting the script on page load
	$(document).ready(function(){
    
//for all pages when user doesn't save on form//
		$('form.dirty-check').areYouSure();
		/*
	    formmodified=0;
	    $('form *').change(function(){
	        formmodified=1;
	    });
	    window.onbeforeunload = confirmExit;
	    function confirmExit() {
	        if (formmodified == 1) {
	            return "New information not saved. Do you wish to leave the page?";
	        }
	    }
	    $("input[name='commit']").click(function() {
	        formmodified = 0;
	    });
	   */ 

//pop up new window
        $('.js-newWindow').click(function (event) {
            event.preventDefault();

            var $this = $(this);

            var url = $this.attr("href");
            var windowName = "popUp";
            var windowSize = $this.data("popup");


            window.open(url, windowName, windowSize);
        });
        
        // We can attach the `fileselect` event to all file inputs on the page
        $(document).on('change', ':file', function() {
          var input = $(this),
              numFiles = input.get(0).files ? input.get(0).files.length : 1,
              label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
          input.trigger('fileselect', [numFiles, label]);
        });
        
        // We can watch for our custom `fileselect` event like this
        $(document).on('fileselect', ':file', function(event, numFiles, label) {

            var input = $(this).parents('.input-group').find(':text'),
                log = numFiles > 1 ? numFiles + ' files selected' : label;

            if( input.length ) {
                input.val(log);
            } else {
                if( log ) alert(log);
            }

        });
        


//back to top button located in footer.jsp

//Check to see if the window is top if not then display button
    $(window).scroll(function(){
        if ($(this).scrollTop() > 100) {
            $('.back-to-top').fadeIn();
        } else {
            $('.back-to-top').fadeOut();
        }
    });
    
    //Click event to scroll to top
    $('.back-to-top').click(function(){
        $('html, body').animate({scrollTop : 0},800);
        return false;
    });
    
});


//sticky navigation

$(document).ready(function() {
var stickyNavTop = $('.stickyDiv').offset().top;
 
var stickyNav = function(){
var scrollTop1 = $(window).scrollTop();
      
if (scrollTop1 > stickyNavTop) { 
    $('.stickyDiv').addClass('sticky');
} else {
    $('.stickyDiv').removeClass('sticky'); 
}
};
 
stickyNav();
 
$(window).scroll(function() {
  stickyNav();
});
});

$('#submissionTable').dataTable( {
  "autoWidth": false
} );




 