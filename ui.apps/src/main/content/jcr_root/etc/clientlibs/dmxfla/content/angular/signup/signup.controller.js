(function() {


angular.module('fittanyUiApp')
       .controller('SignupController',
    	['$scope', '$state','SignupService','Base64',
    		function ($scope, $state, SignupService,Base64) {
        		console.log("This is signup controller");
        	$scope.state = $state;
        	$scope.radioAge = function(age) {
                    if (age == "25below") {
                        $scope.below25 = "inputcheckboxactive";
                        $scope.above25 = "";
                        $scope.above65 = "";
                    } else if (age == "2564") {
                        $scope.below25 = "";
                        $scope.above25 = "inputcheckboxactive";
                        $scope.above65 = "";
                    } else if (age == "65above") {
                        $scope.below25 = "";
                        $scope.above25 = "";
                        $scope.above65 = "inputcheckboxactive";
                    }
                };

                $scope.pennAlum = function(alum) {
                    if (alum == "y") {
                        $scope.palum = "inputcheckboxactive";
                        $scope.alumActive = "radiocircleactive"
                        $scope.npalum = "";
                        $scope.noalumActive = ""
                    } else if (alum == "n") {
                        $scope.palum = "";
                        $scope.npalum = "inputcheckboxactive";
                        $scope.alumActive = ""
                        $scope.noalumActive = "radiocircleactive"
                    }
                };

       	 	var resetClass = function(){
                $scope.below25 = "";
                $scope.above25 = "";
                $scope.above65 = "";
                $scope.palum = "";
                $scope.alumActive = "";
                $scope.npalum = "";
                $scope.noalumActive = "";
        	};

        $scope.submitFittanySignup = function(user){
        		var formData = {};
                var reset = {};
                $scope.error = false;
                $scope.success = false;

				if (user) {


                        formData = {

                            "custFirstName": user.firstname,
                            "custLastName": user.lastname,
                            "custEmailAddress": user.email,
                            "custPassword": Base64.encode(user.password),
                            "custZip": user.zip,
                            "custDOBRangeDesc": user.age,
                            "custPennStateUnivAlumniIN": user.pennalum,
                          //  "custRecordMntdID": custRecordMntdID

                        };
                        var registerUser = SignupService.registerUser(JSON.stringify(formData));
                        registerUser.then(function(response) {
                            if (response.data.statusCode == 200) {
                                console.log("user registered ");
                                $scope.error = false;
                                $scope.success = true;
                                $scope.message = response.statusText;
                            } else {
                                console.log("registration failed");
                                $scope.error = true;
                                $scope.success = false;
                                $scope.message = "error occured while posting, please try again later";

                            }
                        }, function(err) {
                            console.log("error occurred while registrating");
                            $scope.error = true;
                            $scope.success = false;
                            $scope.message = "error occured while posting, please try again later";
                        });


                    }


        };


    }]);
})();