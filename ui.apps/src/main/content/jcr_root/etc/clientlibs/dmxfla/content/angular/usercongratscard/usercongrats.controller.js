(function() {
    angular.module('fittanyUiApp')
        .controller("CongratsController", ['$scope', 'User',
            function($scope, User) {
                console.log("This is in congrats controller");
                var icon = document.getElementById("congratsToggle").childNodes[0];
                var collapsIn = document.getElementById("congrats-toggle-container");
                var congratsToggle = document.getElementById("congratsToggle");
                var congratsEl = document.getElementById("congrats");
                $scope.today = new Date();
                
                $scope.toggled = false;

                $scope.toggleCongrats = function(){
                    $scope.toggled = true;
                    icon.classList.toggle("glyphicon-chevron-down");
                    icon.classList.toggle("glyphicon-chevron-up");
                    collapsIn.classList.toggle("show");
                    congratsToggle.classList.toggle("congrats-toggle-icon");
                };
                $scope.removeBanner = function(){
                    congratsEl.classList.toggle("close");
                };

            }
        ]);
}());
