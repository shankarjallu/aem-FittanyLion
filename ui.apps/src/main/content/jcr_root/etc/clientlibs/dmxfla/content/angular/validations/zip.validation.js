(function() {

//this is a real time zip validation directive which checks for the PA zip
// to use this directive:
// eg: <input type="text/tel/number" zip-check>
    var zipCheck = function(ZipService,$q,$timeout) {
        return {
            require: 'ngModel',
            link: function(scope,elem,attrs,ctrl){
                ctrl.$asyncValidators.pazip = function(value){
                    var deferred = $q.defer();
                    console.log("zip value: " + value);
                        var checkZip = ZipService.get({zip:value});
                        checkZip.$promise.then(function(data){
                            if(data.CountiesMessage.payload.counties.stateAbbreviation === "PA"){
                                console.log("PA zip: ");
                                deferred.resolve();
                            }else {
                                console.log("not a PA zip");
                                 deferred.reject();
                            }
                        },function(err){
                            console.log("not a PA zip");
                             deferred.reject();
                        });
                    return deferred.promise;
                }; 
            }
        };
    };

    zipCheck.$inject = ['ZipService', '$q', '$timeout'];

    angular
        .module('fittanyUiApp')
        .directive('zipCheck', zipCheck);
}());
