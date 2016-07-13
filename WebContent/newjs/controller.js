   var formlogin = angular.module('formlogin', [])


   formlogin.controller('httpCtrl', function($scope, $state, $http) {

       $scope.receive = function(name, password) {
           alert(name + password);

           $http.get('/EMS/greeting', {
               params: { name: name /*, Pass: password */ }
           }).success(function(data, status, headers, config) {
               //加载成功之后做一些事  
               alert(data.name);
               $state.go('showinfo', { name: data.name, id: data.id, cid: data.cid, subject: data.subject, time: data.time });

           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('用户不存在或密码错误');
           });
       };
   });

   formlogin.controller('showCtrl', function($scope, $state, $stateParams) {

       $scope.name = $stateParams.name;
       $scope.id = $stateParams.id;
       $scope.cid = $stateParams.cid;
       $scope.subject = $stateParams.subject;
       $scope.time = $stateParams.time;


   });


   var navbars = angular.module('navbars', [])

   navbars.controller('navbar', function($scope) {
       $scope.selectnav = {
           currentActivity: "试题标志"
       };

       $scope.activities = [
           "chos1",
           "chos2",
           "chos3",
           "chos4"
       ];


   });


   angular.module('ui.bootstrap.demo', ['ui.bootstrap'])
       .controller('TabsDemoCtrl', function($scope, $http, $window) {
           $scope.tabs = [
               { title: '多选题', content: 'tab2' },
               { title: '判断题', content: 'tab3' },
               { title: '匹配题', content: 'tab4' },

           ];
           $scope.radioreceive = function() {
               $http.get('info.json').success(function(data, status, headers, config) {
                   //加载成功之后做一些事  
                   $scope.num = data.num;
                   $scope.title = data.title;
                   $scope.option1 = data.option1;
                   $scope.option2 = data.option2;
                   $scope.option3 = data.option3;
                   $scope.option4 = data.option4;

               }).error(function(data, status, headers, config) {
                   //处理错误  
                   alert('用户不存在或密码错误！');
               });
           };
           $scope.receive = function(option) {
               alert(option);
               $http.get('info1.json').success(function(data, status, headers, config) {
                   //加载成功之后做一些事  
                   $scope.num = data.num;
                   $scope.title = data.title;
                   $scope.option1 = data.option1;
                   $scope.option2 = data.option2;
                   $scope.option3 = data.option3;
                   $scope.option4 = data.option4;
                   $scope.red = "#000000";

               }).error(function(data, status, headers, config) {
                   //处理错误  
                   alert('用户不存在或密码错误！');
               });
           };
           $scope.chgColor= function(){
              $scope.red = "#FF6347";

           }


       });
