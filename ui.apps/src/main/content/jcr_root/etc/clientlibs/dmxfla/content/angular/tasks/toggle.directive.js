(function() {

    var customToggle = function() {
        return {
            restrict: 'A',
            scope: {},
            link: function(scope, elem, attrs, ctrl) {
                elem.on("click", function(){
                    scope.$apply(function() {
                        console.log("attrs arg: " + attrs.target);
                        var el = elem;
                        var targetValue = attrs.target.replace("#","");
                        console.log("targetValue " + targetValue);
                        var showEle = document.getElementById(targetValue);
                        console.log(showEle);
                        showEle.classList.toggle("show");

                    });
                });
            }
        };
    };

    angular
        .module('fittanyUiApp')
        .directive('customToggle', customToggle);
}());
