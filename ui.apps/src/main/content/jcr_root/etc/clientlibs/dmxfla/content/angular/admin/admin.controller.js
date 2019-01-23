(function() {
    angular.module('fittanyUiApp')
        .controller('AdminController', ['$scope', '$rootScope', '$state', 'AdminService', '$q', '$timeout', '$uibModal', 'Auth', '$window',
            function($scope, $rootScope, $state, AdminService, $q, $timeout, $uibModal, Auth, $window) {
                console.log("initializing admin controller");
                $scope.state = $state;
                var scope = $scope;
                $scope.success = Auth.getAdminAuth();
                $scope.isloggedIn = false;
                $rootScope.$on('adminloggedin', function(event,data){
            		$scope.success  = Auth.getAdminAuth();
        		});

                    $scope.submitAdmin = function(user) {
                        var formData = {};
                        var reset = {};
                        $scope.error = false;
                        $scope.dataLoading = true;
                        if (user.username == "admin" && user.password == "admin") {
                            $scope.error = false;
                            $scope.success = true;
                            $scope.isloggedIn = true;
                            $scope.dataLoading = false;
                            Auth.setAdminAuth(true);
                            $rootScope.$broadcast("adminloggedin", true);
                             $state.go("adminquestions"); // you wanna go to profile, will change later
                        } else {
                            $scope.error = true;
                            $scope.success = false;
                            $scope.message = "username or password is not correct";
                            $scope.dataLoading = false;
                        }


                    };


            }
        ]);


})();