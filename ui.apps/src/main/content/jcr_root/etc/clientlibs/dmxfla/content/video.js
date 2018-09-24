$(document).ready(function() {
    var allow = $(".video-container iframe").attr("allow");
    var webkitallowfullscreen  = $(".video-container iframe").attr("webkitallowfullscreen");
    var mozallowfullscreen  = $(".video-container iframe").attr("mozallowfullscreen");
    var allowfullscreen  = $(".video-container iframe").attr("allowfullscreen");

    if(!allow){
         $(".video-container iframe").attr("allow", "autoplay;encrypted-media");
    } if(!webkitallowfullscreen){
         $(".video-container iframe").attr("webkitallowfullscreen", "");
    } if(!mozallowfullscreen){
         $(".video-container iframe").attr("mozallowfullscreen", "");
    } if(!allowfullscreen){
         $(".video-container iframe").attr("allowfullscreen", "");
    }

    $(".video-container #open-trans a").click(function(){
        $(this).parents(".row").children("div").removeClass("col col-sm-12");
         $(this).parents(".row").children("div").addClass("col col-sm-6")
         $(this).parents(".row").children(".transcript-container").css("display","block");
         $(this).parent().css("display","none");
         $(this).parent().siblings("#close-trans").css("display","block");

    });

    $(".video-container #close-trans a").click(function(){
        $(this).parents(".row").children("div").removeClass("col col-sm-6");
         $(this).parents(".row").children("div").addClass("col col-sm-12")
         $(this).parents(".row").children(".transcript-container").css("display","none");
         $(this).parent().css("display","none");
         $(this).parent().siblings("#open-trans").css("display","block");

    });


});