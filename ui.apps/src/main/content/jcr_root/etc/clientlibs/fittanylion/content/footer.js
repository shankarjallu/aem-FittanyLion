$(document).ready(function() {
    if($(window).width() < 768 ) {
        $(".footer-div").parent().css({"width": "100%", "display": "block", "margin":"auto"});
        $(".footer-div h5").attr("data-toggle", "collapse");
    } else {
         $(".footer-div").parent().removeAttr("style");
         $(".footer-div h5").removeAttr("data-toggle");
    }
    $(window).resize(function(){
		if($(window).width() < 768 ) {
            $(".footer-div").parent().css({"width": "100%", "display": "block", "margin":"auto"});
             $(".footer-div h5").attr("data-toggle", "collapse");
    	} else {
         $(".footer-div").parent().removeAttr("style");
         $(".footer-div h5").removeAttr("data-toggle");
    }
    });

});