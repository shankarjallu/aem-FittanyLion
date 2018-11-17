(function() {
    
    function RewardsController ($scope, User, $state) {
        // Get 
        // resolve promise - assign resolved data to $scope.user in a $promise.then

        User.get({id: 1}).$promise.then(function(user){
            $scope.user = user; 
            console.log($scope.user);
        }); 

    };

    RewardsController.$inject = ['$scope', 'User', '$state'];
    angular.module('fittanyUiApp')
        .controller('RewardsController', RewardsController);

})();