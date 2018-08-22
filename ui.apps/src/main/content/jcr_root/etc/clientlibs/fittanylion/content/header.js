(function(window, document, $, undefined) {
    $(document).ready(function() {
       var btns = $("#header a.buttonheader");
        $(btns).removeClass("active");
        for(var i = 0; i < btns.length; i++){
			var current = btns[i];
            console.log("looping ...");
            if(current.href == window.location.href){
                $(current).addClass("active");
                return;
            }
        }

    });

})(window, document, jQuery);

