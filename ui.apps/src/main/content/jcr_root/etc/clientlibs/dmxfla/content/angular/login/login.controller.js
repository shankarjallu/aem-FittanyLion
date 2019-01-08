
(function() {
angular.module('fittanyUiApp')
 
.controller('LoginController',
    ['$scope','AuthService', '$state','$rootScope','$window','Auth','User',
    function ($scope,AuthService,$state, $rootScope,$window, Auth, User) {
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
                    $window.localStorage.removeItem("username");
                }

                //production ready code
                 if(user){
                     AuthService.Login(user.email,user.password,function(response){
                        if(response.data.StatusCode == 200){
                           console.log("authorized..");
                             $scope.loginLoading = false;
                             User.setUser(response.data);
                             Auth.setAuth(true); //tell everyone that you succesfully logged in
                             $state.go("tasks"); // go to task/profile
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
