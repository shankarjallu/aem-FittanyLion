var app = angular.module('fittanyUiApp', []);
app.controller('myCtrl', function($scope, $http) {

  $scope.myVar = false;

     $scope.loginNotify = function() {

        //   console.log("The email" + $scope.emailnotify);
         var etemail = $scope.email;
          $scope.myVar = true;


         var ETUrl = "/bin/getFittanyExactTargetStaus?email=" + etemail;

  $http({
    method : "GET",
    url : ETUrl
  }).then(function mySuccess(response) {
      $scope.myWelcome = response.data;
      $scope.statuscode = response.status;

      if($scope.statuscode == '200'){

     //   alert("this is sattus nsuccess");

           $scope.myVar = false;
      }


    }, function myError(response) {
         $scope.myVar = false;
      $scope.myWelcome = response.statusText;
  });
     }
});