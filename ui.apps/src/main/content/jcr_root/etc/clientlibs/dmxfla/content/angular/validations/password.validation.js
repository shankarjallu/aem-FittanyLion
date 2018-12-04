(function() {
        angular
            .module('fittanyUiApp')
            .directive('passwordCompare', passwordCompare);
        //This is a password validation directive to compare two password fields
        //use it inside confirmation password input like this:
        //<input type="password" id="pwd" name="pwd" ng-model="user.password">
        // attribure value for password-check="?" should match ng-model value from password field
        // <input type="password" id="cpwd" name="confirmpwd" password-compare="user.password">
        function passwordCompare() {

            return {
                require: 'ngModel',
                scope: {
                    firstPassword: "=passwordCompare"
                },
                link: function(scope, elem, attrs, ctrl) {
                    ctrl.$validators.pwmatch = function(modelValue) {
                        return modelValue == scope.firstPassword;
                    };

                    scope.$watch("firstPassword", function() {
                        ctrl.$validate();
                    });


                }

            };

        };
})();

(function() {
    angular
        .module('fittanyUiApp')
        .directive('passwordCheck', passwordCheck);
    //This is a password check directive to check password criteria
    //use it inside password input like this:
    //<input type="password" id="pwd" name="pwd" ng-model="user.password" password-check>
    function passwordCheck() {

        return {
            require: 'ngModel',
            link: function(scope, elem, attrs, ctrl) {
                    elem.on('keypress', function() {
                        scope.$apply(function() {
                            var lcregex = /[a-z]/; // lowercase regex
                            var upregex = /[A-Z]/; // uppercase regex
                            var numregex = /[0-9]/; // number regex
                            var specialCharRegex = /[!@#\$%\^\&*\)\(+=.,?\_\-\{}]/; // special char regex
                            scope.allValid = false;

                            ctrl.$validators.passtest = function(model, view) {
                                scope.lcValid = false; //setting each initial validation to false
                                scope.ucValid = false;
                                scope.numValid = false;
                                scope.charValid = false;
                                scope.minlengthValid = false;

                                var value = model || view || "";

                                if (lcregex.test(value)) {
                                    scope.lcValid = true;
                                }
                                if (upregex.test(value)) {
                                    scope.ucValid = true;
                                }
                                if (numregex.test(value)) {
                                    scope.numValid = true;
                                }
                                if (specialCharRegex.test(value)) {
                                    scope.charValid = true;
                                }
                                if (value.length >= 8) {
                                    scope.minlengthValid = true;
                                }
                                //setting validotor to true only if every validations pass
                                if (scope.lcValid && scope.ucValid && scope.numValid &&
                                    scope.charValid && scope.minlengthValid) {
                                    scope.allValid = true;

                                    return true;
                                }
                                return false;
                            };


                        });

                    });

            }

        };
    };
})();