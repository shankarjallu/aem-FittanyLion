(function (window, document, $, undefined) {
	$(document).ready(function () {


		var emailVal, validemail;

		var IsEmail = function (email) {
			var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			return regex.test(email);
		};


         var z = '<div class="container">';
                z += '<div class="content">';
                z += '<h3 class = "main_header">' + "Thank you page" +'</h3>';

                z += '</div>';
                z += '</div>';

          var y = '<div class="container">';
                y += '<div class="content">';
                y += '<h3 class = "main_header">' + "OOp's Something went wrong" +'</h3>';

                y += '</div>';
                y += '</div>';


		$('input[type="submit"]').click(function (event) {

			event.preventDefault();

			$('.red').css('display', 'none');

			   
			emailVal = $('input[name="email"]').val();   


			if (IsEmail(emailVal)) {

				validemail = true;


			} else {
				// console.log("has no email value");
				validemail = false;
                $('input[name="email"]').after("<p  class='red'>PLEASE ENTER THE CORRECT EMAIL ID</p>");

			}

			if (validemail == true) {

				var ETUrl = "/bin/getFittanyExactTargetStaus?email=" + emailVal;
				$.ajax({
					type: 'GET',

					contentType: 'application/json',

					url: ETUrl,

					dataType: 'json',


					success: function (data,xhr) {

						//Hide the Input field component and display Thank you



                         $("div").remove("#fittanylandingpage");
                          $("#mainwrapper").append(z);

					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						//Hide the Input component and show the Error Message


					}
				});

			}


		});


	});

})(window, document, jQuery);