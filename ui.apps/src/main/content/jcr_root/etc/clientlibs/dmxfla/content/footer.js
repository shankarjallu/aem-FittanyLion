$(document).ready(function() {
    if($( window ).width() < 768){
        $('.hideinMobile').remove();
    } else {
        $('.hideinDesk').remove();
    }

});