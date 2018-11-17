(function() {
    function AdminService($resource) {
        console.log("initializing admin service..");
        var service = {
            login: login,
            questions: questions
        }

        return service;

        //admin login end point, change the url to real endpoint
        function login() {
            return $resource('http://localhost:5000/admin/:id', {
                id: '@id'
            }, {
                update: {
                    method: 'PUT'
                },
                query: {
                    method: 'GET',
                    isArray: true
                }

            });
        }

		//admin questionaire end point, change the url to real endpoint
        function questions(){
        	return $resource('http://localhost:5000/questions');
        }


    };

	AdminService.$inject = ['$resource'];

    angular.module('fittanyUiApp')
        .factory('AdminService', AdminService);
})();
