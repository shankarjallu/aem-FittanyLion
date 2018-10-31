var app = angular.module('fittanyApp', []);
app.controller('landingPageEmail',['$scope','$http',function($scope, $http) {

    $scope.loadingclass = false;


    $scope.emailFormat = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;

    $scope.loginNotify = function() {

        var etemail = $scope.email;
        $scope.loadingclass = true;


        var ETUrl = "/bin/getFittanyExactTargetStatus?email=" + etemail;

        $http({
            method: "GET",
            url: ETUrl
        }).then(function mySuccess(response) {
            $scope.loadingclass = false;
            $scope.myWelcome = response.data;
            $scope.statuscode = response.status;



            if ($scope.statuscode == '200' && $scope.myWelcome.errorcode != '10006') {

                // Thank you success

                console.log("Please route to thanku");


            } else {

                //  Thank you fail
                console.log("Please route to thanku failure");

            }


        }, function myError(response) {
            console.log("their is a error");
            $scope.loadingclass = false;
            $scope.myWelcome = response.statusText;
        });
    }
}]);



