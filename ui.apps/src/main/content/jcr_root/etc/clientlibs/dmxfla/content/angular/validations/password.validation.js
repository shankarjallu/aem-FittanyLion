(function() {
    angular
        .module('fittanyUiApp')
        .directive('passwordCheck', passwordCheck);
    //This is a password validation directive
    //use it inside confirmation password input like this:
    //<input type="password" id="pwd" name="pwd" >
    // <input type="password" name="confirmpwd" password-check="pwd">
    function passwordCheck() {

        return {
            require: 'ngModel',
            link: function(scope, elem, attrs, ctrl) {
                if (attrs.passwordCheck != "") {
                    var firstPassword = '#' + attrs.passwordCheck;
                    elem.append(firstPassword).on('keypress', function() {
                        scope.$apply(function() {
                            ctrl.$validators.pwmatch = function(model, view) {
                                var firstpass = $(firstPassword).val();
                                var value = model || view || "";
                                if(value == firstpass){
                                  scope.passChangeValid = true;  
                                }else {
                                    scope.passChangeValid = false;
                                }
                                return (value === firstpass);
                            };
                        });
                    });
                } else {
                    var secondPassword = '#cpassword';
                    scope.passChangeValid = true; // setting initial empty confirmation password to true
                    scope.checkPassword = function(){
                        var $ctrl = ctrl;
                        var firstPass = $('#pwd').val();
                        var confirmPass = $(secondPassword).val();
                        if(confirmPass != firstPass){
                            $ctrl.$$parentForm.$invalid = true;
                            $ctrl.$$parentForm.$valid = false;
                            scope.passChangeValid = false;
                        }
                    }
                    elem.on('keypress', function() {
                        scope.$apply(function() {
                            var lcregex = /[a-z]/; // lowercase regex
                            var upregex = /[A-Z]/; // uppercase regex
                            var numregex = /[0-9]/; // number regex
                            var specialCharRegex = /[!@#\$%\^\&*\)\(+=.,?\_\-\{}]/; // special char regex


                            console.log(ctrl.$validators);

                            ctrl.$validators.passtest = function(model, view) {
                                scope.lcValid = false; //setting each initial validation to false
                                scope.ucValid = false;
                                scope.numValid = false;
                                scope.charValid  = false;
                                scope.minlengthValid = false;
                                scope.allValid = false;
                                var value = model || view || "";

                                if(lcregex.test(value)){
                                    scope.lcValid  = true;
                                }
                                if(upregex.test(value)){
                                    scope.ucValid  = true;
                                }
                                if(numregex.test(value)){
                                    scope.numValid  = true;
                                }
                                if(specialCharRegex.test(value)){
                                    scope.charValid  = true;
                                }
                                if(value.length >= 8){
                                    scope.minlengthValid = true;
                                }
                                //setting validotor to true only if every validations pass
                                if(scope.lcValid && scope.ucValid && scope.numValid 
                                    && scope.charValid && scope.minlengthValid){  
                                    scope.allValid = true;
                                    var confirmPass = $(secondPassword).val();
                                        if(confirmPass != value){
                                            scope.passChangeValid = false; 
                                            //return false;
                                        }else {
                                            scope.passChangeValid = true;

                                        }
                                                                     
                                    return true;
                                }
                                return false;
                            };
                            
                        });

                    });

                }

            }

        };
    };
})();