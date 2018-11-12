
(function() {
angular.module('fittanyUiApp')
 
.controller('LoginController',
    ['$scope','AuthService', '$state','$rootScope','$window','Auth',
    function ($scope,AuthService,$state, $rootScope,$window, Auth) {
        console.log("Inside login controller");
        $scope.state = $state;

         console.log($scope.state.current.name);
         var email = $window.localStorage.getItem("username") || "";
         if(email){
            $scope.user = {email: email, rememberme: true};
         }
        $scope.login = function (user) {          
            //code for spinner comes here
                $scope.error = false;
                $scope.success = false;
                $scope.loginLoading = true;
                var userObj;
                if(user.rememberme){ // put on localstorage 
                    $window.localStorage.setItem("username", user.email);
                }else { //put on session 
                    console.log("don't rememberme");
                    $window.sessionStorage.setItem("username", user.email);
                }

                //production ready code
                 if(user){
                     AuthService.Login(user.email,user.password,function(response){
                        if(response.status == 200){
                           console.log("authorized..");
                             $scope.loginLoading = false;
                            $state.go("signup"); // you wanna go to profile, will change later
                                Auth.setAuth(true); //tell everyone that you succesfully logged in
            					$rootScope.$broadcast("loggedin", true);
                            
                        }else{
                            console.log("error occured while posting");
                        	console.log(response);
                            $scope.error = true;
                            $scope.message = response.statusText; // to display error message in html
                            $scope.loginLoading = false;

                        }
                     });
                 }


    
        };
        
    }]);



})();