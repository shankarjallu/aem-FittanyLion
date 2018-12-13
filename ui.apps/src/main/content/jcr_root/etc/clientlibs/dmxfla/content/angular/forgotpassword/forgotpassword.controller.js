(function() {


angular.module('fittanyUiApp')
       .controller('ForgotPassword',
    	['$scope','$http','$state','Base64', 'ForgotPasswordService', '$filter',
    		function ($scope, $state,Base64, $http, ForgotPasswordService, $filter) {
        		console.log("This is  ForgotPassword  controller");

$scope.submitForgotPassword = function(user){


   // var userEmail = {{user.email | lowercase }};



        var userEmail  = $filter('lowercase')(user.email);

    alert(userEmail);

//var forgotpassword = ForgotPassword.emailCheck(user.email);

    //production ready code
                 if(user){
                     ForgotPasswordService.Login(userEmail,function(response){
                        if(response.data.statusCode == 200){
                             alert("Email Sent");
                            
                        }else{

                             alert("Email Not Registered");
                        }
                     });
                 }




}

    }]);
})();