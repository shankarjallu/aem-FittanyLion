(function() {
    angular.module('fittanyUiApp')
        .factory('User', ['$resource', function($resource) {
            return $resource(
                'http://localhost:5000/users/:id',
                {id: '@id'},
                {
                    'update' : {
                        method: 'PUT'   
                    }    
                }    
            )
        }]); 

})();
