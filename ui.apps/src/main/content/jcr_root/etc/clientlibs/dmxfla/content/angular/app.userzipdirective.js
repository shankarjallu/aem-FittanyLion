
(function() {

    //this is a real time zip validation directive which checks for the PA zip
    // to use this directive:
    // eg: <input type="text/tel/number" zip-check>
    var zipCheck = function(ZipDataService, $q) {
        return {
            require: 'ngModel',
            link: function(scope, elem, attrs, ctrl) {
                ctrl.$asyncValidators.pazip = function(value) {
                    var deferred = $q.defer();
                    console.log("zip value: " + value);
                    var ziplist = ZipDataService.getZipList();
                    var paZip = false;
                    for(var i = 0; i< ziplist.length; i++){
                            if(ziplist[i].Zip === value){
                                console.log("PA zip: ");
                                paZip = true;
                                break;
                            }
                    }

                    if(paZip){
                        deferred.resolve();
                    }else {
                        deferred.reject();
                    }
                    
                    return deferred.promise;
                };
            }
        };
    };

    angular
        .module('fittanyUiApp')
        .directive('zipCheck', zipCheck);
}());