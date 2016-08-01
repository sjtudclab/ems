/*routingDemoApp.filter("trustUrl", ['$sce', function ($sce) {
        return function (recordingUrl) {
            return $sce.trustAsResourceUrl(recordingUrl);
        };
    }]);   */

var formlogin = angular.module('formlogin', [])


   formlogin.controller('httpCtrl', function($scope, $state, $http, $window) {

       $scope.receive = function(name, password) {
           // alert(name + password);

           $http.get('/EMS/greeting', {
               params: { name: name, password: password }
           }).success(function(data, status, headers, config) {
               //加载成功之后做一些事  
               //  alert(data.name);
               // alert(data.checklist[0]);
               $window.sessionStorage.token = data.token;
               var infoStatus = {};
               $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
               var infoStatus = JSON.parse($window.sessionStorage.infoStatus);


               infoStatus.name = data.name;
               infoStatus.gender = data.gender;
               infoStatus.id = data.id;
               infoStatus.cid = data.cid;
               infoStatus.subject = data.subject;
               infoStatus.time = data.time;
               //  $window.sessionStorage.photo=data.photo;
               infoStatus.photo = data.photo;
               infoStatus.Rid = data.Rid;
               $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
               if(data.Rid==0){
            	   $state.go('showinfo');
               }else{
            	   $state.go('supervisor');
               }
             

           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('用户不存在或密码错误');
           });
       };
   });

   formlogin.controller('showCtrl', function($scope, $state, $stateParams, $window) {
       //对象记录状态
       // alert(typeof($window.sessionStorage.problemStatus));
       // if(typeof($window.sessionStorage.problemStatus)==undefined){
       var problemStatus = {};

       $window.sessionStorage.problemStatus = JSON.stringify(problemStatus);
       var problemStatus = JSON.parse($window.sessionStorage.problemStatus);
       problemStatus.single = {};
       problemStatus.multiple = {};
       problemStatus.judgment = {};
       problemStatus.match = {};
       problemStatus.simple = {};
       $window.sessionStorage.problemStatus = JSON.stringify(problemStatus);

       var infoStatus = JSON.parse($window.sessionStorage.infoStatus);
       $scope.photo = infoStatus.photo;
       $scope.name = infoStatus.name;
       $scope.gender = infoStatus.gender;
       $scope.id = infoStatus.id;
       $scope.cid = infoStatus.cid;
       $scope.subject = infoStatus.subject;
       $scope.time = infoStatus.time;
       $scope.click = function() {
           $state.go('main', { active: '', num: '', brand: '' });
       }



   });
   formlogin.controller('showinfo', function($scope, $state, $stateParams, $window) {

       var infoStatus = JSON.parse($window.sessionStorage.infoStatus);
       $scope.photo = infoStatus.photo;
       $scope.name = infoStatus.name;
       $scope.gender = infoStatus.gender;
       $scope.id = infoStatus.id;
       $scope.cid = infoStatus.cid;
       $scope.subject = infoStatus.subject;


   });



   var demo0 = angular.module('demo0', ['ui.bootstrap'])
   
   demo0.controller('TabsDemoCtrl', function($scope) {
    $scope.problemMetaInfo = ['单选题', '多选题', '判断题', '匹配题','简答题'];
    $scope.active = [];
    $scope.display=[];
    $scope.color = [];
    $scope.index = 0;
    for(x in $scope.problemMetaInfo){
	  $scope.active[x]="";
	  $scope.display[x]='none';
	  $scope.color[x] = "white";
    };
    $scope.$watch('index', function(newValue, oldValue) {
    	
        for (i in $scope.active) {
            $scope.active[i] = "";
            $scope.color[i] = "white";
            $scope.display[i]="none";
            
        };
        


        $scope.active[$scope.index] = "active";
        $scope.display[$scope.index] = "block";

        switch ($scope.index) {
    
            case 0:
                $scope.color[$scope.index] = "rgba(88,178,220,.5)";
                break;
            case 1:
                $scope.color[$scope.index] = "rgba(204,204,255,.6)";
                break;
            case 2:
                $scope.color[$scope.index] = "rgba(168,216,185,.6)";
                break;
            case 3:
                $scope.color[$scope.index] = "rgba(255,204,204,.6)";
                break;
            case 4:
                $scope.color[$scope.index] = "rgba(165,222,228,.8)";
                break;
            default:
                alert('error');
            /*   case 0:
            $scope.color[$scope.index] = "#d9edf7";
            break;
        case 1:
            $scope.color[$scope.index] = "#A8D8B9";
            break;
        case 2:
            $scope.color[$scope.index] = "#dff0d8";
            break;
        case 3:
            $scope.color[$scope.index] = "#f2dede";
            break;
        case 4:
            $scope.color[$scope.index] = "#66BAB7";
            break;*/

        };
    });

    $scope.sel = function(index) {
        $scope.index = index;
    };

});
   
   

   demo0.controller('showMain', function($scope, $state, $stateParams, $window) {

       $scope.active = $stateParams.active * 1; //面板切换


   });


   demo0.controller("timeinfo", function($scope, $interval, $window, $http) {
       $http.get('/EMS/exam/getTime', {
           params: { token: $window.sessionStorage.token }
       }).success(function(data, status, headers, config) {
           var second = data,
               timePromise = undefined;
           timePromise = $interval(function() {
               if (second <= 0) {
                   $interval.cancel(timePromise);
                   timePromise = undefined;
                   alert("重新");
               } else {
                   hour = Math.floor(second / 3600);
                   minute = Math.floor((second % 3600) / 60);
                   miao = second % 3600 % 60;
                   $scope.hour = hour;
                   $scope.minute = minute;
                   $scope.time = miao;
                   second--;
               }
           }, 1000, 0);

       }).error(function(data, status, headers, config) {
           //处理错误  
           alert('用户不存在或密码错误！');
       });

   });

   demo0.controller('skiptb1', function($scope, $http, $window, $state, $stateParams, $rootScope) {
       //单选题
       function shuffle(list) {
           for (var i = 0; i < list.length; i++) {
               var swapIndex = Math.floor(Math.random() * list.length);
               var temp = list[i];
               list[i] = list[swapIndex];
               list[swapIndex] = temp;
           }
           return list;
       }
       $scope.option = {};
       var singleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态

       if ($stateParams.type == 1) {
           //   alert('cp面板1'+$scope.active);
           $scope.nid = $stateParams.num * 1;
           //检查某题
           $http.get('/EMS/exam/toTopic', {
               /* $http.get('single.json', {*/
               params: { typeId: 0, token: $window.sessionStorage.token, id: $stateParams.num }
           }).success(function(data, status, headers, config) {
        	   
               // 试题
        	   $scope.totalItems = data.singleNum;
               $scope.content = data.content;
               $scope.lists = data.choiceList;
               $scope.lists = shuffle(data.choiceList);
               var length = $scope.lists.length;
               for (var i = 0; i < length; i++) {
                   $scope.lists[i].alp = String.fromCharCode(i + 65);
               }
               if(data.audio){  
        		   $scope.audiohide="block";
        		   $scope.audio=data.audio;
        	   }else{
        		   $scope.audiohide="none";	 
        	   }
        	   if(data.img){
        		   $scope.imghide="block";
        		   $scope.img=data.img;
        		  
        	   }else{
        		   $scope.imghide="none"; 
        	   }
               //试题状态

               $scope.option.optionsRadios = data.choiceId;
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               if ($scope.nid == 1) { $scope.before = true; } else { $scope.before = false; }

           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('tab1出错！');
           });

       } else {
           var singleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
           if (singleStatus.single.nid) {

               $scope.nid = singleStatus.single.nid;
               $scope.content = singleStatus.single.content;
               $scope.lists = singleStatus.single.choiceList;
               $scope.totalItems=singleStatus.single.totalItems;
               if(singleStatus.single.audiohide=="block"){  
        		   $scope.audiohide="block";
        		   $scope.audio=singleStatus.single.audio;
        	   }else{
        		   $scope.audiohide="none";
        		  // alert("隐藏");
        		 
        	   }
        	   if(singleStatus.single.imghide=="block"){
        		   $scope.imghide="block";
        		   $scope.img=singleStatus.single.img;
        		  
        	   }else{
        		   $scope.imghide="none";
        		   //alert("隐藏");
        		  
        	   }

               //status
               $scope.option.optionsRadios = singleStatus.single.option;
               $rootScope.counter = singleStatus.single.counter;
               if (singleStatus.single.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               if (singleStatus.single.nid == 1) { $scope.before = true; } else { $scope.before = false; }


           } else {
               $window.sessionStorage.active = 0;
               //初始化试题
               $http.get('/EMS/exam/start', {
                   /*  $http.get('single.json', {*/
                   params: { typeId: 0, token: $window.sessionStorage.token }
               }).success(function(data, status, headers, config) {
                   //试题
            	 //  alert("dfjs"+data.img);
            	   alert("dfjs"+data.video);
            	   if(data.audio){  
            		   $scope.audiohide="block";
            		   $scope.audio=data.audio;
            	   }else{
            		   $scope.audiohide="none";	 
            	   }
            	   if(data.img){
            		   $scope.imghide="block";
            		   $scope.img=data.img;
            		  
            	   }else{
            		   $scope.imghide="none"; 
            	   }
            	   if(data.video){  
            		   /*alert(data.video);*/
            		   $scope.videohide="block";
            		   $scope.video=data.video;
            	   }else{
            		   $scope.videohide="none";	 
            	   }
            	 
            	   $scope.totalItems = data.singleNum;
                   $scope.nid = 1;
                   $scope.content = data.content;
                   $scope.lists = data.choiceList;
                   $scope.lists = shuffle(data.choiceList);
                   var length = $scope.lists.length;
                   for (var i = 0; i < length; i++) {
                       $scope.lists[i].alp = String.fromCharCode(i + 65);
                   }
                   //试题状态
                   $scope.option.optionsRadios = data.choiceId;
                   if (data.ifCheck) {
                       $scope.red = "#FF6347";
                       $scope.count = true;
                   } else {
                       $scope.red = "#000000";
                       $scope.count = false;

                   }

                   $scope.before = true;



               }).error(function(data, status, headers, config) {
                   //处理错误  
                   alert('用户不存在或密码错误！');
               });
           }
       }

       $scope.previousText = "上一题";
       $scope.nextText = "下一题";
      /* $scope.totalItems = 20;*/
       $scope.itemsPerPage = 1;
       $scope.currentPage = 1;
       $scope.maxSize = 2;

       $scope.pageChanged = function(option) {
          // alert($scope.currentPage);
         //  alert($scope.id);
           var isChecked = $scope.count;

           singleStatus.single.nid = $scope.nid;
           singleStatus.single.content = $scope.content;
           singleStatus.single.choiceList = $scope.lists;
           if( $scope.audiohide=="block"){
        	   singleStatus.single.audio=$scope.sudio;
        	   singleStatus.single.audiohide=$scope.sudiohide;
           }else{
        	   singleStatus.single.audiohide=$scope.sudiohide;
           }
           if( $scope.imghide=="block"){
        	   singleStatus.single.img=$scope.img;
        	   singleStatus.single.imghide=$scope.imghide;
           }else{
        	   singleStatus.single.imghide=$scope.imghide;
           }
          

           singleStatus.single.option = option.optionsRadios;
           singleStatus.single.ifCheck = $scope.count;
           singleStatus.single.counter = $rootScope.counter;
           singleStatus.single.totalItems = $scope.totalItems;

           $window.sessionStorage.problemStatus = JSON.stringify(singleStatus);

           $http.get('/EMS/exam/getTopic', {
               /*   $http.get('single0.json', {*/
               params: { token: $window.sessionStorage.token, typeId: 0, id: $scope.nid, requestId: $scope.currentPage, choiceId: option.optionsRadios, ifCheck: isChecked }
           }).success(function(data, status, headers, config) {
               // 试题
        	   $scope.totalItems = data.singleNum;
               $scope.nid = $scope.currentPage;
               $scope.content = data.content;
               $scope.lists = data.choiceList;
               $scope.lists = shuffle(data.choiceList);
               var length = $scope.lists.length;
               for (var i = 0; i < length; i++) {
                   $scope.lists[i].alp = String.fromCharCode(i + 65);
               }
               if(data.audio){  
        		   $scope.audiohide="block";
        		   $scope.audio=data.audio;
        	   }else{
        		   $scope.audiohide="none";	 
        	   }
        	   if(data.img){
        		   $scope.imghide="block";
        		   $scope.img=data.img;
        		  
        	   }else{
        		   $scope.imghide="none"; 
        	   }
        	   if(data.video){  
        		   /*alert(data.video);*/
        		   $scope.videohide="block";
        		   $scope.video=data.video;
        	   }else{
        		   $scope.videohide="none";	 
        	   }
               //试题状态
               alert( data.choiceId);
               $scope.option.optionsRadios = data.choiceId;
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               if ($scope.id == 1) { $scope.before = true; } else { $scope.before = false; }


           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('用户不存在或密码错误！');
           });
       };


   
       $rootScope.counter = 0;
       //标志旗帜颜色
       $scope.chgColor = function(count) {


           if (count) {
               $scope.red = "#000000";
               $scope.count = false;
               $rootScope.counter = $rootScope.counter - 1;

           } else {
               $scope.red = "#FF6347";
               $scope.count = true;
               $rootScope.counter = $rootScope.counter + 1;

           }

       }
   });

   demo0.controller('skiptb2', function($scope, $http, $window, $state, $stateParams, $rootScope) {
       //多选题
       function shuffle(list) {
           for (var i = 0; i < list.length; i++) {
               var swapIndex = Math.floor(Math.random() * list.length);
               var temp = list[i];
               list[i] = list[swapIndex];
               list[swapIndex] = temp;
           }
           return list;
       }
       $scope.option =[];
       $scope.isSelected = function(id) {
           return $scope.option.indexOf(id) >= 0;
       }
       $scope.updateSelection = function($event, id) {
           var checkbox = $event.target;
           var checked = checkbox.checked;
           if (checked) {
               $scope.option.push(id);
           } else {
        	   //alert()
               var idx = $scope.option.indexOf(id);
               $scope.option.splice(idx, 1);
           }

       }
      /* $scope.updateSelection = function($event, id){
           var checkbox = $event.target;
          var action = (checkbox.checked?'add':'remove');
           updateSelected(action,id);
       }
       var updateSelected = function(action,id){
           if(action == 'add' && $scope.selected.indexOf(id) == -1){
               $scope.selected.push(id);
           }
           if(action == 'remove' && $scope.selected.indexOf(id)!=-1){
               var idx = $scope.selected.indexOf(id);
               $scope.selected.splice(idx,1);
           }
       }*/
   
      
       var multiStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态

       if ($stateParams.type == 2) {
           //  alert('cp面板2'+$scope.active);
           $scope.nid = $stateParams.num * 1;
           //检查某题
           $http.get('/EMS/exam/toTopic', {
               /* $http.get('multiple.json', {*/
               params: { typeId: 1, token: $window.sessionStorage.token, id: $stateParams.num }
           }).success(function(data, status, headers, config) {
               // 试题
        	   $scope.totalItems = data.multiNum;
               $scope.content = data.content;
               $scope.lists = data.choiceList;
               $scope.lists = shuffle(data.choiceList);
               var length = $scope.lists.length;
               for (var i = 0; i < length; i++) {
                   $scope.lists[i].alp = String.fromCharCode(i + 65);
               }
               if(data.audio){  
        		   $scope.audiohide="block";
        		   $scope.audio=data.audio;
        	   }else{
        		   $scope.audiohide="none";	 
        	   }
        	   if(data.img){
        		   $scope.imghide="block";
        		   $scope.img=data.img;
        		  
        	   }else{
        		   $scope.imghide="none"; 
        	   }
               //试题状态
         
               $scope.option = data.choiceIdList;
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               /*                if ( $scope.nid == 1) { $scope.before = true; }else{$scope.before = false; }*/

           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('tab1出错！');
           });

       } else {
           var multiStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
           if (multiStatus.multiple.nid) {

               $scope.nid = multiStatus.multiple.nid;
               $scope.content = multiStatus.multiple.content;
               $scope.lists = multiStatus.multiple.choiceList;
               $scope.totalItems=multiStatus.multiple.totalItems;
               if(multiStatus.multiple.audiohide=="block"){  
        		   $scope.audiohide="block";
        		   $scope.audio=multiStatus.multiple.audio;
        	   }else{
        		   $scope.audiohide="none";
        		  // alert("隐藏");
        		 
        	   }
        	   if(multiStatus.multiple.imghide=="block"){
        		   $scope.imghide="block";
        		   $scope.img=multiStatus.multiple.img;
        		  
        	   }else{
        		   $scope.imghide="none";
        		   //alert("隐藏");
        		  
        	   }

               //status
               $scope.option = multiStatus.multiple.choiceIdList;
               $rootScope.counter = multiStatus.multiple.counter;
               if (multiStatus.multiple.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               /*if ( multiStatus.multiple.id == 1) { $scope.before = true; }else{$scope.before = false; }*/



               //alert("保留tab1!")

           } else {
               //初始化试题
               $http.get('/EMS/exam/start', {
                   /*  $http.get('multiple.json', {*/
                   params: { typeId: 1, token: $window.sessionStorage.token }
               }).success(function(data, status, headers, config) {
                   //试题
            	   $scope.totalItems = data.multiNum;
                   $scope.nid = 1;
                   $scope.content = data.content;
                   $scope.lists = data.choiceList;
                   $scope.lists = shuffle(data.choiceList);
                   var length = $scope.lists.length;
                   for (var i = 0; i < length; i++) {
                       $scope.lists[i].alp = String.fromCharCode(i + 65);
                   }
                   if(data.audio){  
            		   $scope.audiohide="block";
            		   $scope.audio=data.audio;
            	   }else{
            		   $scope.audiohide="none";	 
            	   }
            	   if(data.img){
            		   $scope.imghide="block";
            		   $scope.img=data.img;
            		  
            	   }else{
            		   $scope.imghide="none"; 
            	   }
                   //试题状态
                   //alert($scope.lists[0].choiceId);
/*                   data.choiceIdList[0]=$scope.lists[0].choiceId;
                   data.choiceIdList[1]=$scope.lists[1].choiceId;*/
                
                   $scope.option = data.choiceIdList;
                   // alert(data.choiceIdList);
                   if (data.ifCheck) {
                       $scope.red = "#FF6347";
                       $scope.count = true;
                   } else {
                       $scope.red = "#000000";
                       $scope.count = false;

                   }

                   /*   $scope.before = true;*/



               }).error(function(data, status, headers, config) {
                   //处理错误  
                   alert('用户不存在或密码错误！');
               });

           }
       }

       $scope.previousText = "上一题";
       $scope.nextText = "下一题";
      /* $scope.totalItems = 20;*/
       $scope.itemsPerPage = 1;
       $scope.currentPage = 1;
       $scope.maxSize = 5;

       $scope.pageChanged = function(option) {
           //alert($scope.currentPage);
           alert("next"+option);
           var isChecked = $scope.count;

           multiStatus.multiple.nid = $scope.nid;
           multiStatus.multiple.content = $scope.content;
           multiStatus.multiple.choiceList = $scope.lists;
           if( $scope.audiohide=="block"){
        	   multiStatus.multiple.audio=$scope.sudio;
        	   multiStatus.multiple.audiohide=$scope.sudiohide;
           }else{
        	   multiStatus.multiple.audiohide=$scope.sudiohide;
           }
           if( $scope.imghide=="block"){
        	   multiStatus.multiple.img=$scope.img;
        	   multiStatus.multiple.imghide=$scope.imghide;
           }else{
        	   multiStatus.multiple.imghide=$scope.imghide;
           }
          

           multiStatus.multiple.choiceIdList = option;
           multiStatus.multiple.ifCheck = $scope.count;
           multiStatus.multiple.counter = $rootScope.counter;
           multiStatus.multiple.totalItems = $scope.totalItems;

           $window.sessionStorage.problemStatus = JSON.stringify(multiStatus);

           $http.get('/EMS/exam/getTopic', {
               /*   $http.get('multiple.json', {*/
               params: { token: $window.sessionStorage.token, typeId: 1, id: $scope.nid, requestId: $scope.currentPage, choiceIdList: option, ifCheck: isChecked }
           }).success(function(data, status, headers, config) {
               // 试题
        	   $scope.totalItems = data.multiNum;
               $scope.nid = $scope.currentPage;
               $scope.content = data.content;
               $scope.lists = data.choiceList;
               $scope.lists = shuffle(data.choiceList);
               var length = $scope.lists.length;
               for (var i = 0; i < length; i++) {
                   $scope.lists[i].alp = String.fromCharCode(i + 65);
               }
               if(data.audio){  
        		   $scope.audiohide="block";
        		   $scope.audio=data.audio;
        	   }else{
        		   $scope.audiohide="none";	 
        	   }
        	   if(data.img){
        		   $scope.imghide="block";
        		   $scope.img=data.img;
        		  
        	   }else{
        		   $scope.imghide="none"; 
        	   }
               //试题状态
            //   alert( data.choiceIdList);
               alert("aa"+data.choiceIdList);
               $scope.option = data.choiceIdList;
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               /*   if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }*/

           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('用户不存在或密码错误！');
           });

       };


       //标志旗帜颜色
       $scope.chgColor = function(count) {


           if (count) {
               $scope.red = "#000000";
               $scope.count = false;
               $rootScope.counter = $rootScope.counter - 1;

           } else {
               $scope.red = "#FF6347";
               $scope.count = true;
               $rootScope.counter = $rootScope.counter + 1;

           }

       }

   });

   demo0.controller('skiptb3', function($scope, $http, $window, $state, $stateParams, $rootScope) {
       //判断题
       function shuffle(list) {
           for (var i = 0; i < list.length; i++) {
               var swapIndex = Math.floor(Math.random() * list.length);
               var temp = list[i];
               list[i] = list[swapIndex];
               list[swapIndex] = temp;
           }
           return list;
       }
       $scope.option = {};
       var judgStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态


       if ($stateParams.type == 3) {
           // alert('cp面板3'+$scope.active);
           $scope.nid = $stateParams.num * 1;
           //检查某题
           $http.get('/EMS/exam/toTopic', {
               /* $http.get('judg.json', {*/
               params: { typeId: 2, token: $window.sessionStorage.token, id: $stateParams.num }
           }).success(function(data, status, headers, config) {
               // 试题
        	   $scope.totalItems = data.judgeNum;
               $scope.content = data.content;
               $scope.lists = data.choiceList;
               $scope.lists = shuffle(data.choiceList);
               var length = $scope.lists.length;
               for (var i = 0; i < length; i++) {
                   $scope.lists[i].alp = String.fromCharCode(i + 65);
               }
               if(data.audio){  
        		   $scope.audiohide="block";
        		   $scope.audio=data.audio;
        	   }else{
        		   $scope.audiohide="none";	 
        	   }
        	   if(data.img){
        		   $scope.imghide="block";
        		   $scope.img=data.img;
        		  
        	   }else{
        		   $scope.imghide="none"; 
        	   }
               //试题状态

               $scope.option.optionsRadios = data.choiceId;
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               /* if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }*/

           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('tab1出错！');
           });

       } else {
           var judgStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
           if (judgStatus.judgment.id) {

               $scope.nid = judgStatus.judgment.nid;
               $scope.content = judgStatus.judgment.content;
               $scope.lists = judgStatus.judgment.choiceList;
               $rootScope.totalItems=judgStatus.judgment.totalItems;
               if(judgStatus.judgment.audiohide=="block"){  
        		   $scope.audiohide="block";
        		   $scope.audio=judgStatus.judgment.audio;
        	   }else{
        		   $scope.audiohide="none";
        		  // alert("隐藏");
        		 
        	   }
        	   if(judgStatus.judgment.imghide=="block"){
        		   $scope.imghide="block";
        		   $scope.img=judgStatus.judgment.img;
        		  
        	   }else{
        		   $scope.imghide="none";
        		   //alert("隐藏");
        		  
        	   }

               //status
               $scope.option.optionsRadios = judgStatus.judgment.option;
               $scope.counter = judgStatus.judgment.counter;
               if (judgStatus.judgment.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               /*if ( judgStatus.judgment.id == 1) { $scope.before = true; }else{$scope.before = false; }*/

               //alert("保留tab3!")

           } else {
               //初始化试题
               $http.get('/EMS/exam/start', {
                   /* $http.get('judg.json', {*/
                   params: { typeId: 2, token: $window.sessionStorage.token }
               }).success(function(data, status, headers, config) {
                   //试题
            	   $scope.totalItems = data.judgeNum;
                   $scope.nid = 1;
                   $scope.content = data.content;
                   $scope.lists = data.choiceList;
                   $scope.lists = shuffle(data.choiceList);
                   var length = $scope.lists.length;
                   for (var i = 0; i < length; i++) {
                       $scope.lists[i].alp = String.fromCharCode(i + 65);
                   }
                   if(data.audio){  
            		   $scope.audiohide="block";
            		   $scope.audio=data.audio;
            	   }else{
            		   $scope.audiohide="none";	 
            	   }
            	   if(data.img){
            		   $scope.imghide="block";
            		   $scope.img=data.img;
            		  
            	   }else{
            		   $scope.imghide="none"; 
            	   }
                   //试题状态
                   $scope.option.optionsRadios = data.choiceId;
                   if (data.ifCheck) {
                       $scope.red = "#FF6347";
                       $scope.count = true;
                   } else {
                       $scope.red = "#000000";
                       $scope.count = false;

                   }

                   /*  $scope.before = true;*/



               }).error(function(data, status, headers, config) {
                   //处理错误  
                   alert('用户不存在或密码错误！');
               });


           }
       }

       $scope.previousText = "上一题";
       $scope.nextText = "下一题";
   /*    $scope.totalItems = 20;*/
       $scope.itemsPerPage = 1;
       $scope.currentPage = 1;
       $scope.maxSize = 5;

       $scope.pageChanged = function(option) {
           alert($scope.currentPage);
           //alert($scope.id);
           var isChecked = $scope.count;

           judgStatus.judgment.nid = $scope.nid;
           judgStatus.judgment.content = $scope.content;
           judgStatus.judgment.choiceList = $scope.lists;
           if( $scope.audiohide=="block"){
        	   judgStatus.judgment.audio=$scope.sudio;
        	   judgStatus.judgment.audiohide=$scope.sudiohide;
           }else{
        	   judgStatus.judgment.audiohide=$scope.sudiohide;
           }
           if( $scope.imghide=="block"){
        	   judgStatus.judgment.img=$scope.img;
        	   judgStatus.judgment.imghide=$scope.imghide;
           }else{
        	   judgStatus.judgment.imghide=$scope.imghide;
           }

           judgStatus.judgment.option = option.optionsRadios;
           judgStatus.judgment.ifCheck = $scope.count;
           judgStatus.judgment.counter = $rootScope.counter;
           judgStatus.judgment.totalItems = $scope.totalItems;
          

           $window.sessionStorage.problemStatus = JSON.stringify(judgStatus);


           $http.get('/EMS/exam/getTopic', {
               /*$http.get('judg0.json', {*/
               params: { token: $window.sessionStorage.token, typeId: 2, id: $scope.nid, requestId: $scope.currentPage, choiceId: option.optionsRadios, ifCheck: isChecked }
           }).success(function(data, status, headers, config) {
               // 试题
        	   $scope.totalItems = data.judgeNum;
               $scope.nid = $scope.currentPage;
               $scope.content = data.content;
               $scope.lists = data.choiceList;
               $scope.lists = shuffle(data.choiceList);
               var length = $scope.lists.length;
               for (var i = 0; i < length; i++) {
                   $scope.lists[i].alp = String.fromCharCode(i + 65);
               }
               if(data.audio){  
        		   $scope.audiohide="block";
        		   $scope.audio=data.audio;
        	   }else{
        		   $scope.audiohide="none";	 
        	   }
        	   if(data.img){
        		   $scope.imghide="block";
        		   $scope.img=data.img;
        		  
        	   }else{
        		   $scope.imghide="none"; 
        	   }
               //试题状态
               $scope.option.optionsRadios = data.choiceId;
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               /* if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }*/


           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('用户不存在或密码错误！');
           });

       };

       //  $rootScope.counter = 0;
       //标志旗帜颜色
       $scope.chgColor = function(count) {


           if (count) {
               $scope.red = "#000000";
               $scope.count = false;
               $rootScope.counter = $rootScope.counter - 1;

           } else {
               $scope.red = "#FF6347";
               $scope.count = true;
               $rootScope.counter = $rootScope.counter + 1;

           }

       }

   });


   demo0.controller('skiptb4', function($scope, $http, $window, $state, $stateParams, $rootScope) {
       //匹配题
       function shuffle(list) {
           for (var i = 0; i < list.length; i++) {
               var swapIndex = Math.floor(Math.random() * list.length);
               var temp = list[i];
               list[i] = list[swapIndex];
               list[swapIndex] = temp;
           }
           return list;
       }
       $scope.option = {};
       var matchStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态

       if ($stateParams.type == 4) {
           // alert('cp面板4'+$scope.active);
           $scope.nid = $stateParams.num * 1;
           //检查某题
           $http.get('/EMS/exam/toTopic', {
               /*  $http.get('match.json', {*/
               params: { typeId: 3, token: $window.sessionStorage.token, id: $stateParams.num }
           }).success(function(data, status, headers, config) {
               // 试题
        	   $scope.totalItems = data.matchNum;
               $scope.contentlists = data.contentList;
               $scope.lists = data.choiceList;
               $scope.lists = shuffle(data.choiceList);
               var length = $scope.lists.length;
               for (var i = 0; i < length; i++) {
                   $scope.lists[i].alp = String.fromCharCode(i + 65);
               }
               if(data.audio){  
        		   $scope.audiohide="block";
        		   $scope.audio=data.audio;
        	   }else{
        		   $scope.audiohide="none";	 
        	   }
        	   if(data.img){
        		   $scope.imghide="block";
        		   $scope.img=data.img;
        		  
        	   }else{
        		   $scope.imghide="none"; 
        	   }
               //试题状态

               $scope.option = data.choiceIdMap;
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               /* if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }*/

           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('tab1出错！');
           });

       } else {

           var matchStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
           if (matchStatus.match.nid) {
               $scope.nid = matchStatus.match.nid;
               $scope.contentlists = matchStatus.match.contentList;
               $scope.lists = matchStatus.match.choiceList;
               $scope.totalItems = matchStatus.match.totalItems;
               if(matchStatus.match.audiohide=="block"){  
        		   $scope.audiohide="block";
        		   $scope.audio=matchStatus.match.audio;
        	   }else{
        		   $scope.audiohide="none";
        		  // alert("隐藏");
        		 
        	   }
        	   if(matchStatus.match.imghide=="block"){
        		   $scope.imghide="block";
        		   $scope.img=matchStatus.match.img;
        		  
        	   }else{
        		   $scope.imghide="none";
        		   //alert("隐藏");
        		  
        	   }

               //status
               $scope.option = matchStatus.match.choiceIdMap;
               $rootScope.counter = matchStatus.match.counter;
               if (matchStatus.match.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               /*	if ( matchStatus.match.id == 1) { $scope.before = true; }else{$scope.before = false; }*/

               //alert("保留tab4!")
           } else {
               //初始化试题
                $http.get('/EMS/exam/start', {
               /*$http.get('match.json', {*/
                   params: { typeId: 3, token: $window.sessionStorage.token }
               }).success(function(data, status, headers, config) {
                   //试题
            	  /* alert("match"+data.matchNum);*/
            	   $scope.totalItems = data.matchNum;
                   $scope.nid = 1;
                   $scope.contentlists = data.contentList;
                   //    alert(data.choiceList);
                   $scope.lists = data.choiceList;
                   $scope.lists = shuffle(data.choiceList);
                   var length = $scope.lists.length;
                   for (var i = 0; i < length; i++) {
                       $scope.lists[i].alp = String.fromCharCode(i + 65);
                   }
                   if(data.audio){  
            		   $scope.audiohide="block";
            		   $scope.audio=data.audio;
            	   }else{
            		   $scope.audiohide="none";	 
            	   }
            	   if(data.img){
            		   $scope.imghide="block";
            		   $scope.img=data.img;
            		  
            	   }else{
            		   $scope.imghide="none"; 
            	   }
                   //试题状态
               //    alert("match"+data.choiceIdMap);
                   $scope.option = data.choiceIdMap;
                   if (data.ifCheck) {
                       $scope.red = "#FF6347";
                       $scope.count = true;
                   } else {
                       $scope.red = "#000000";
                       $scope.count = false;

                   }

                   /*  $scope.before = true;*/



               }).error(function(data, status, headers, config) {
                   //处理错误  
                   alert('用户不存在或密码错误！');
               });
           }


       }

       $scope.previousText = "上一题";
       $scope.nextText = "下一题";
      /* $scope.totalItems = 20;*/
       $scope.itemsPerPage = 1;
       $scope.currentPage = 1;
       $scope.maxSize = 5;
       $scope.pageChanged = function(option) {
         //  alert($scope.currentPage);
           alert($scope.option);
           var isChecked = $scope.count;

           matchStatus.match.nid = $scope.nid;
           matchStatus.match.contentList = $scope.contentlists;
           matchStatus.match.choiceList = $scope.lists;
           if( $scope.audiohide=="block"){
        	   matchStatus.match.audio=$scope.sudio;
        	   matchStatus.match.audiohide=$scope.sudiohide;
           }else{
        	   matchStatus.match.audiohide=$scope.sudiohide;
           }
           if( $scope.imghide=="block"){
        	   matchStatus.match.img=$scope.img;
        	   matchStatus.match.imghide=$scope.imghide;
           }else{
        	   matchStatus.match.imghide=$scope.imghide;
           }

           

           matchStatus.match.choiceIdMap = option;
           matchStatus.match.ifCheck = $scope.count;
           matchStatus.match.counter = $rootScope.counter;
           matchStatus.match.totalItems = $scope.totalItems;

           $window.sessionStorage.problemStatus = JSON.stringify(matchStatus);


           $http.get('/EMS/exam/getTopic', {
                /* $http.get('match0.json', {*/
               params: { token: $window.sessionStorage.token, typeId: 3, id: $scope.nid, requestId: $scope.currentPage, choiceIdMap: option, ifCheck: isChecked }
           }).success(function(data, status, headers, config) {
               // 试题
        	   $scope.totalItems = data.matchNum;
               $scope.nid = $scope.currentPage;
               $scope.contentlists = data.contentList;
               $scope.lists = data.choiceList;
               $scope.lists = shuffle(data.choiceList);
               var length = $scope.lists.length;
               for (var i = 0; i < length; i++) {
                   $scope.lists[i].alp = String.fromCharCode(i + 65);
               }
               if(data.audio){  
        		   $scope.audiohide="block";
        		   $scope.audio=data.audio;
        	   }else{
        		   $scope.audiohide="none";	 
        	   }
        	   if(data.img){
        		   $scope.imghide="block";
        		   $scope.img=data.img;
        		  
        	   }else{
        		   $scope.imghide="none"; 
        	   }
               //试题状态
               $scope.option = data.choiceIdMap;
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               if ($scope.id == 1) { $scope.before = true; } else { $scope.before = false; }


           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('用户不存在或密码错误！');
           });

       };


       //  $rootScope.counter = 0;
       //标志旗帜颜色
       $scope.chgColor = function(count) {


           if (count) {
               $scope.red = "#000000";
               $scope.count = false;
               $rootScope.counter = $rootScope.counter - 1;

           } else {
               $scope.red = "#FF6347";
               $scope.count = true;
               $rootScope.counter = $rootScope.counter + 1;

           }

       }

   });

   demo0.controller('skiptb5', function($scope, $http, $window, $state, $stateParams, $rootScope) {
       //简答题
      
       /*$scope.option = "撰写答案";*/
       var simpleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态

       if ($stateParams.type == 5) {
           // alert('cp面板4'+$scope.active);
           $scope.nid = $stateParams.num * 1;
           //检查某题
         /*  $http.get('/EMS/exam/toTopic', {*/
                 $http.get('match.json', {
               params: { typeId: 4, token: $window.sessionStorage.token, id: $stateParams.num }
           }).success(function(data, status, headers, config) {
               // 试题
               $scope.content = data.content;
             
               //试题状态

               $scope.answer = data.answer;
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               /* if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }*/

           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('tab1出错！');
           });

       } else {

           var simpleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
           if (simpleStatus.simple.nid) {
               $scope.nid = simpleStatus.simple.nid;
               $scope.content = simpleStatus.simple.content;
               $scope.totalItems = simpleStatus.simple.totalItems;
          
               //status
               $scope.answer = simpleStatus.simple.answer;
               $rootScope.counter = simpleStatus.simple.counter;
               if (simpleStatus.simple.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
               /*	if ( matchStatus.match.id == 1) { $scope.before = true; }else{$scope.before = false; }*/

               //alert("保留tab4!")
           } else {
               //初始化试题
                $http.get('/EMS/exam/start', {
              /* $http.get('judg.json', {*/
                   params: { typeId: 4, token: $window.sessionStorage.token }
               }).success(function(data, status, headers, config) {
                   //试题
            	   $scope.totalItems = 2/*data.shortNum*/;
                   $scope.nid = 1;
                   $scope.content = data.content;
                   //    alert(data.choiceList);
                
                   //试题状态
                   $scope.answer = data.answer;
                   if (data.ifCheck) {
                       $scope.red = "#FF6347";
                       $scope.count = true;
                   } else {
                       $scope.red = "#000000";
                       $scope.count = false;

                   }

             
               }).error(function(data, status, headers, config) {
                   //处理错误  
                   alert('用户不存在或密码错误！');
               });
           }


       }

       $scope.previousText = "上一题";
       $scope.nextText = "下一题";
       $scope.totalItems = 20;
       $scope.itemsPerPage = 1;
       $scope.currentPage = 1;
       
       
       
       $scope.maxSize = 5;
       
       $scope.pageChanged = function(answer) {
           var isChecked = $scope.count;
           alert(answer);
           simpleStatus.simple.nid = $scope.nid;
           simpleStatus.simple.content = $scope.content;

           simpleStatus.simple.answer = answer;
           simpleStatus.simple.ifCheck = $scope.count;
           simpleStatus.simple.counter = $rootScope.counter;
           simpleStatus.simple.totalItems = $scope.totalItems;
           
           $window.sessionStorage.problemStatus = JSON.stringify(simpleStatus);


           $http.get('/EMS/exam/getTopic', {
              /*   $http.get('judg0.json', {*/
               params: { token: $window.sessionStorage.token, typeId: 4, id: $scope.nid, requestId: $scope.currentPage, answer: answer, ifCheck: isChecked }
           }).success(function(data, status, headers, config) {
               // 试题
        	   $scope.totalItems = data.shortNum;
               $scope.nid = $scope.currentPage;
               $scope.content = data.content;
               $scope.answer ="";
            
               //试题状态
               $scope.answer = data.answer;
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               } else {
                   $scope.red = "#000000";
                   $scope.count = false;

               }
   
           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('用户不存在或密码错误！');
           });

       };

       //标志旗帜颜色
       $scope.chgColor = function(count) {


           if (count) {
               $scope.red = "#000000";
               $scope.count = false;
               $rootScope.counter = $rootScope.counter - 1;

           } else {
               $scope.red = "#FF6347";
               $scope.count = true;
               $rootScope.counter = $rootScope.counter + 1;

           }

       }

   });
   

   var checkup = angular.module('checkup', [])
   checkup.controller('btnCtrl', function($scope) {

       switch ($scope.list.status) {
           case 0: //检查
               $scope.btnClass = 'button button-caution button-box button-large';
               break;
           case 1: //已完成
               $scope.btnClass = 'button button-primary button-box button-large';
               break;
           case 2: //未完成
               $scope.btnClass = 'button button-large button-box button-border';
               break;
           default:
               $scope.btnClass = 'button button-large button-box button-border';
       }

   });
   //单选题
   checkup.controller("Ctab1", function($scope, $http, $state, $window,$rootScope) {

       $http.get('/EMS/exam/check', {
           params: { token: $window.sessionStorage.token, typeId: 0 }
       }).success(function(data, status, headers, config) {
    	  // alert(data.checkList[0].status);
           $scope.lists = data.checkList;
           $rootScope.topicNum=data.topicNum;
           $rootScope.finishNum=data.finishNum;
           $rootScope.otherNum=data.topicNum-data.finishNum;                                                                                      
          
          /* $rootScope.topicNum=3;
           $rootScope.finishNum=2;
           $rootScope.otherNum=1;*/
           
       }).error(function(data, status, headers, config) {
           //处理错误  
           alert('用户不存在或密码错误！');
       });


       $scope.skip = function(listnum) {

           //  alert(listnum);
           $state.go('main', { active: 0, num: listnum, type: 1 });

       }


   });
   //多选题
   checkup.controller("Ctab2", function($scope, $http, $state, $window) {

       $http.get('/EMS/exam/check', {
           params: { token: $window.sessionStorage.token, typeId: 1 }
       }).success(function(data, status, headers, config) {
    	   $scope.lists = data.checkList;
         /*  $rootScope.topicNum=data.topicNum;
           $rootScope.finishNum=data.finishNum;
           $rootScope.otherNum=data.topicNum-data.finishNum;*/

       }).error(function(data, status, headers, config) {
           //处理错误  
           alert('用户不存在或密码错误！');
       });


       $scope.skip = function(listnum) {
           //    alert(listnum);
           $state.go('main', { active: 1, num: listnum, type: 2 });

       }


   });
   //判断题
   checkup.controller("Ctab3", function($scope, $http, $state, $window) {

       $http.get('/EMS/exam/check', {
           params: { token: $window.sessionStorage.token, typeId: 2 }
       }).success(function(data, status, headers, config) {
    	   $scope.lists = data.checkList;
          /* $rootScope.topicNum=data.topicNum;
           $rootScope.finishNum=data.finishNum;
           $rootScope.otherNum=data.topicNum-data.finishNum;*/

       }).error(function(data, status, headers, config) {
           //处理错误  
           alert('用户不存在或密码错误！');
       });


       $scope.skip = function(listnum) {
           // alert(listnum);
           $state.go('main', { active: 2, num: listnum, type: 3 });

       }


   });
   //匹配题
   checkup.controller("Ctab4", function($scope, $http, $state, $window) {

       $http.get('/EMS/exam/check', {
           params: { token: $window.sessionStorage.token, typeId: 3 }
       }).success(function(data, status, headers, config) {
    	   $scope.lists = data.checkList;
          /* $rootScope.topicNum=data.topicNum;
           $rootScope.finishNum=data.finishNum;
           $rootScope.otherNum=data.topicNum-data.finishNum;*/

       }).error(function(data, status, headers, config) {
           //处理错误  
           alert('用户不存在或密码错误！');
       });


       $scope.skip = function(listnum) {
           // alert(listnum);
           $state.go('main', { active: 3, num: listnum, type: 4 });

       }


   });
   //简答题
   checkup.controller("Ctab5", function($scope, $http, $state, $window) {

       $http.get('/EMS/exam/check', {
           params: { token: $window.sessionStorage.token, typeId: 4 }
       }).success(function(data, status, headers, config) {
    	   $scope.lists = data.checkList;
          /* $rootScope.topicNum=data.topicNum;
           $rootScope.finishNum=data.finishNum;
           $rootScope.otherNum=data.topicNum-data.finishNum;*/

       }).error(function(data, status, headers, config) {
           //处理错误  
           alert('用户不存在或密码错误！');
       });


       $scope.skip = function(listnum) {
           // alert(listnum);
           $state.go('main', { active: 4, num: listnum, type: 5 });

       }


   });
