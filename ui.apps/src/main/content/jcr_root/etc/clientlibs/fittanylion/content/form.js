function formSubmit(){
    var formval = "yes";
    setCookie("form-submit",formval,365);
    $('#download').removeClass('isDisable');

}
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = 'expires=' + d.toUTCString();
    document.cookie = cname + '=' + cvalue + '; ' + expires + '; path=/';
}




function getCookie(name) {
   var re = new RegExp(name + '=([^;]+)');
        var value = re.exec(document.cookie);
        return value !== null ? unescape(value[1]) : null;
}


(function(window, document, $, undefined) {
    $(document).ready(function() {

		var formValue =  getCookie("form-submit");
        if(formValue === "null"|| formValue === null || formValue === "undefined" || formValue === undefined || formValue === ''){
            $('#download').addClass('isDisable');
        }
        var generator = new IDGenerator();
        var test = generator.generate();

        function IDGenerator() {
	 
		 this.length = 8;
		 this.timestamp = +new Date;
		 
		 var _getRandomInt = function( min, max ) {
			return Math.floor( Math.random() * ( max - min + 1 ) ) + min;
		 }
		 
		 this.generate = function() {
			 var ts = this.timestamp.toString();
			 var parts = ts.split( "" ).reverse();
			 var id = "";
			 
			 for( var i = 0; i < this.length; ++i ) {
				var index = _getRandomInt( 0, parts.length - 1 );
				id += parts[index];	 
			 }

          var formId =  getCookie("form-id");
          var pageUrl = window.location.href;
          var pagetitle = $('h1').text();


               $('#00NC0000007CihR').attr('value', formId);
               $('#lead_source').attr('value', pageUrl);
               $('#00NC0000007CdED').attr('value', pagetitle);

             if(formId === null || formId === "undefined" || formId === ""){

                  setCookie("form-id",id,365);

                   $('#00NC0000007CihR').attr('value', id);


             }

			 return id;

		 }

		 
	 }

         });

})(window, document, jQuery);
