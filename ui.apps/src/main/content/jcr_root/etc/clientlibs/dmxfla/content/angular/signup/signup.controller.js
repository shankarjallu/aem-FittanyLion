(function() {


angular.module('fittanyUiApp')
       .controller('SignupController',
    	['$scope', '$state','SignupService','Base64',
    		function ($scope, $state, SignupService, Base64) {
        		console.log("This is signup controller");
        	$scope.state = $state;
        	$scope.radioAge = function(age){
        	if(age == "25below"){
        		$scope.below25 = "inputcheckboxactive";
        		$scope.above25 = "";
        		$scope.above65 = "";
        	}else if(age == "2564") {
        		$scope.below25 = "";
        		$scope.above25 = "inputcheckboxactive";
        		$scope.above65 = "";
        	}else if(age == "65above"){
        		$scope.below25 = "";
        		$scope.above25 = "";
        		$scope.above65 = "inputcheckboxactive";
        	}
        };

        $scope.pennAlum = function(pennalum){
        	if(pennalum == "pennAlum"){
        		$scope.palum = "inputcheckboxactive";
        		$scope.alumActive = "radiocircleactive"
        		$scope.npalum = "";
        		$scope.noalumActive = ""
        	}else if(pennalum == "noalum") {
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
                
        		if(user){
                    var newuser = new SignupService();
                    
                    var custIdentifier = 9989892;
                    formData = {
             //CustomerIdentifer should be removed in Prod, just for dev testing.
                            "custIdentifier":  custIdentifier,

                             "custFirstName": user.firstname,
                           "custLastName": user.lastname,
                           "custEmailAddress": user.email,
                           "custPassword": Base64.encode(user.password),
                          "custZip" : user.zip,
                          "custAge" : user.age,
                          "custPennStateUnivAlumniIN": user.pennalum

                      };


//                    formData = 'custIdentifier='+ custIdentifier +'&custFirstName='+ user.firstname + '&custLastName='+user.lastname + '&custEmailAddress=' + user.email +
//'&custPassword='+ Base64.encode(user.password) + '&custZip=' + user.zip + '&custAge=' + user.age +
//'&custPennStateUnivAlumniIN=' + user.pennalum;
                         
                         
                     
                    
                    newuser.data = formData;
                    var promise = newuser.$save();
                    promise.then(function(res){
                        $scope.error = false;
                        $scope.success = true;
                        $scope.message = res.status;
                        console.log(res);
                        $state.go("login");
                        
                    },function(err){
                        console.log("error occured while posting");
                        console.log(err);
                        $scope.error = true;
                        $scope.success = false;
                        $scope.message = err.status;
                        $scope.fittanySignupForm.$setPristine();
                        $scope.fittanySignupForm.$setUntouched();
                        $scope.user = angular.copy(reset);
                        resetClass();  
                    });
                         
        			    //console.log("signup form value: " + Object.keys(formData));
        			
        		}
        };


    }]);
})();