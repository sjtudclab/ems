   var formlogin = angular.module('formlogin', [])


   formlogin.controller('httpCtrl', function($scope, $state, $http) {

       $scope.receive = function(name, password) {
          // alert(name + password);

           $http.get('/EMS/greeting', {
               params: { name: name /*, Pass: password */ }
           }).success(function(data, status, headers, config) {
               //加载成功之后做一些事  
             //  alert(data.name);
              // alert(data.checklist[0]);
               $state.go('showinfo', { name: data.name, id: data.id, cid: data.cid, subject: data.subject, time: data.time });

           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('用户不存在或密码错误');
           });
       };
   });

   formlogin.controller('showCtrl', function($scope, $state, $stateParams,$rootScope) {
      
	   $rootScope.name = $stateParams.name;
	   $rootScope.id = $stateParams.id;
	   $rootScope.cid = $stateParams.cid;
	   $rootScope.subject = $stateParams.subject;
	   $rootScope.time = $stateParams.time;
      // alert($scope.name);
       $scope.click=function(){
     	  $state.go('main');
       }
       


   });
   
   formlogin.controller('MainCtrl', function($scope, $state) {
          

         
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
   navbars.controller("flag",function($scope){
	   $scope.$on('transfer.type', function(event, data) {  
		   if(data){$scope.checkednum=checkednum+1}else{$scope.checkednum=checkednum-1;}
	     }); 
   });
   


   var demo0 = angular.module('demo0', ['ui.bootstrap'])
   
   demo0.controller('showMain', function($scope, $state, $rootScope,$stateParams) {

       $scope.name = $rootScope.name;
       $scope.id = $rootScope.cid;
       $scope.cid = $rootScope.id;
       $scope.subject = $rootScope.subject;
       $scope.active=$stateParams.active*1; //面板切换


   });
   
   
   demo0.controller("Countdown", function($scope, $interval,$rootScope) {
       var second = 7200,
           timePromise = undefined;
       $rootScope.hour = 0;
       $rootScope.minute = 0;
       $rootScope.time = 0;

       timePromise = $interval(function() {
           if (second <= 0) {
               $interval.cancel(timePromise);
               timePromise = undefined;
               alert("重新");
               second = 60;
           } else {
               hour = Math.floor(second / 3600);
               minute = Math.floor((second % 3600) / 60);
               miao = second % 3600 % 60;
               $rootScope.hour = hour;
               $rootScope.minute = minute;
               $rootScope.time = miao;
               second--;

           }
       }, 1000, 0);

   });
   
   demo0.controller('skiptb1', function($scope, $http, $window, $rootScope, $state, $stateParams) {
	    //单选题
	    if (($stateParams.active+1) == 1) {
	        alert('cp面板1'+$scope.active);
	        $rootScope.num = $stateParams.num*1; 
	        //检查某题
	        $http.get('new2.json', {
                params: { typeId:0,token:$rootScope.token,id:$stateParams.num}
            }).success(function(data, status, headers, config) {
                // 试题
                $rootScope.Id1 = data.choiceIdA;
                $rootScope.Id2 = data.choiceIdB;
                $rootScope.Id3 = data.choiceIdC;
                $rootScope.Id4 = data.choiceIdD;
                $rootScope.title = data.content;
                $rootScope.option1 = data.choiceA;
                $rootScope.option2 = data.choiceB;
                $rootScope.option3 = data.choiceC;
                $rootScope.option4 = data.choiceD;
                //试题状态
                $scope.option.optionsRadios = data.choiceIdA;
                if (data.ifCheck) {
                    $scope.red = "#FF6347";
                    $scope.count = true;
                }
                if ($rootScope.num == 1) { $scope.before = true; }

            }).error(function(data, status, headers, config) {
                //处理错误  
                alert('tab1出错！');
            });

	    } else {
	    	  if($stateParams.active == 2||$stateParams.active == 3||$stateParams.active == 4){}else{
	    		  alert("hh-1");
	    		  $rootScope.active=0;
	    		  //初始化试题
	    		  $http.get('new.json', {
	    			  params: { typeId:0,token:$rootScope.token}
		            }).success(function(data, status, headers, config) {
		                //试题
		                $rootScope.Id1 = data.choiceIdA;
		                $rootScope.Id2 = data.choiceIdB;
		                $rootScope.Id3 = data.choiceIdC;
		                $rootScope.Id4 = data.choiceIdD;
		                $rootScope.num = 1;
		                $rootScope.title = data.content;
		                $rootScope.option1 = data.choiceA;
		                $rootScope.option2 = data.choiceB;
		                $rootScope.option3 = data.choiceC;
		                $rootScope.option4 = data.choiceD;
		                //试题状态
		                $scope.before = true;
		                $scope.red = "#000000";
		                $scope.count = false;
		                

		            }).error(function(data, status, headers, config) {
		                //处理错误  
		                alert('用户不存在或密码错误！');
		            });
	    		  
	    	  }
	    		     
	    }
   
       $scope.option = {};
       //下一题
       $scope.receive = function(option) {
    	  // var sec=$rootScope.hour*3600+$rootScope.minute*60+$rootScope.time;
         //  alert(sec);
           alert(option.optionsRadios);
           var isChecked=$scope.count;
           alert(isChecked);
 
           $http.get('new1.json', {
           params: {token:$rootScope.token,typeId:0,id:$rootScope.num,choiceId:option,ifCheck:isChecked}
       }).success(function(data, status, headers, config) {
               //试题
               $rootScope.Id1=data.choiceAId;
               $rootScope.Id2=data.choiceBId;
               $rootScope.Id3=data.choiceCId;
               $rootScope.Id4=data.choiceDId;
               $rootScope.num = $rootScope.num+1;
               $rootScope.title = data.content;
               $rootScope.option1 = data.choiceA;
               $rootScope.option2 = data.choiceB;
               $rootScope.option3 = data.choiceC;
               $rootScope.option4 = data.choiceD;
               //试题状态
               $scope.red = "#000000";
               $scope.count = false;
               $scope.option = {};
               $scope.before= false;


           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('用户不存在或密码错误！');
           });

       };
     //上一题
       $scope.sreceive = function(option) {
          // alert(option.optionsRadios);
           var isChecked = $scope.count;
         //  alert(isChecked);
         //  alert(ID);
           $http.get('new2.json', {
               params: { token:$rootScope.token,typeId:0,id:$rootScope.num,choiceId:option,ifCheck:isChecked}
           }).success(function(data, status, headers, config) {
               //试题
               $rootScope.Id1 = data.choiceIdA;
               $rootScope.Id2 = data.choiceIdB;
               $rootScope.Id3 = data.choiceIdC;
               $rootScope.Id4 = data.choiceIdD;
               $rootScope.num = $rootScope.num - 1;
               $scope.title = data.content;
               $scope.option1 = data.choiceA;
               $scope.option2 = data.choiceB;
               $scope.option3 = data.choiceC;
               $scope.option4 = data.choiceD;
               //试题状态
               if (data.count) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               }

               $scope.option.optionsRadios = data.choiceIdA;
               if ($rootScope.num == 1) { $scope.before = true; }


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

           alert($scope.count);


       }

   });

   demo0.controller('checkboxCtrl', function($scope, $http, $window,$rootScope) {
       //多选题
       $scope.checkboxreceive = function() {
           $http.get('info1.json').success(function(data, status, headers, config) {
               //加载成功之后做一些事  
              
               $scope.title = data.title;
               $scope.option1 = data.option1;
               $scope.option2 = data.option2;
               $scope.option3 = data.option3;
               $scope.option4 = data.option4;
               $scope.num = 1;
               $scope.before= true;
               $scope.count = false;

           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('未读到文件！');
           });
       };


       $scope.phone = {};
       $scope.receive2 = function(event) {
    	   var sec=$rootScope.hour*3600+$rootScope.minute*60+$rootScope.time;
           alert(sec);
           alert(event.tab1);
           alert(event.tab2);
           alert(event.tab3);
           alert(event.tab4);
           $http.get('info.json').success(function(data, status, headers, config) {
               //加载成功之后做一些事  
               $scope.title = data.title;
               $scope.option1 = data.option1;
               $scope.option2 = data.option2;
               $scope.option3 = data.option3;
               $scope.option4 = data.option4;
               $scope.num = $scope.num+1;
               alert($scope.num);
               $scope.red = "#000000";
               $scope.count = false;
               $scope.phone = {};
               $scope.before= false;



           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('未读到文件！');
           });
       };
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

           alert($scope.count);


       };


   });

   //判断题
  demo0.controller('skiptb3', function($scope, $http, $window,$rootScope,$stateParams) {
	  if (($stateParams.active+1) == 3) {
	        alert('cp面板3'+$scope.active3);
	        $rootScope.num3 = $stateParams.num; 
	        //检查某题
	        $http.get('info1.json', {
              params: { typeId:2,token:$rootScope.token3,id:$stateParams.num}
          }).success(function(data, status, headers, config) {
              // $rootScope.ID = data.id;
    
          	  $rootScope.title3 = data.title;
              $scope.before= true;
              $scope.count = false;
              $scope.option.optionsRadios = true;
              if (data.count) {
                  $scope.red = "#FF6347";
                  $scope.count = true;
              }
              if ($rootScope.num == 1) { $scope.before = true; }

          }).error(function(data, status, headers, config) {
              //处理错误  
              alert('tab3出错！');
          });

	    } else {
	    	  if($stateParams.active == 1||$stateParams.active == 2||$stateParams.active == 4){}else{
	    		  alert("ss-1");
	    		  $scope.active=0;
	    		  //初始化试题
	    		  $http.get('info.json', {
		                params: { typeId:2,token:$rootScope.token3 }
		            }).success(function(data, status, headers, config) {
		                //加载成功之后做一些事 
		                // $rootScope.ID = data.id;
		            	$rootScope.num3 = 1;
		            	$rootScope.title3 = data.title;
		                $scope.before= true;
		                $scope.count = false;

		            }).error(function(data, status, headers, config) {
		                //处理错误  
		                alert('用户不存在或密码错误！');
		            });
	    		  
	    	  }
	    		     

	    }
       $scope.op1 = {};
       $scope.receive3 = function(option) {
           alert(option.options);
           var sec=$rootScope.hour*3600+$rootScope.minute*60+$rootScope.time;
           var isChecked = $scope.count;
        //   alert(sec);
           //下一题
           $http.get('info1.json', {
               params: { token:$rootScope.token,typeId:0,id:$rootScope.num,choiceId:option,ifCheck:isChecked}
           }).success(function(data, status, headers, config) {
        	   $rootScope.title3 = data.title;
           
        	   $rootScope.num3 = $rootScope.num3+1;
               $scope.red = "#000000";
               $scope.count = false;
               $scope.op1 = {};
               $scope.before= false;


           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('未读到文件');
           });

       };
     //上一题
       $scope.sreceive3 = function(option) {
           var isChecked = $scope.count;
         //  alert(isChecked);
         //  alert(ID);
           $http.get('info.json', {
               params: { token:$rootScope.token,typeId:0,id:$rootScope.num,choiceId:option,ifCheck:isChecked}
           }).success(function(data, status, headers, config) {
               //加载成功之后做一些事  
        	   $rootScope.title3 = data.title;
               
        	   $rootScope.num3 = $rootScope.num3-1;
              
               if (data.count) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               }

               $scope.op1.options= "true";
               if ($rootScope.num3 == 1) { $scope.before = true; }


           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('用户不存在或密码错误！');
           });

       };
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

           alert($scope.count);


       };



   });

//匹配题

demo0.controller('ppCtrl', function($scope, $http, $window,$rootScope) {
     //单选题
     $scope.ppreceive = function() {
         $http.get('pd.json', {
         params: { typeId:"3"}
     }).success(function(data, status, headers, config) {
             //加载成功之后做一些事 
      
             $scope.Id1=data.choiseIdA;
             $scope.Id2=data.choiseIdB;
             $scope.Id3=data.choiseIdC;
             $scope.Id4=data.choiseIdD;
             $scope.num = 1;
             $scope.content1 = data.content1;
             $scope.content2 = data.content2;
             $scope.content3 = data.content3;
             $scope.option1 = data.choiceA;
             $scope.option2 = data.choiceB;
             $scope.option3 = data.choiceC;
             $scope.option4 = data.choiceD;
             $scope.count = false;
             $scope.before= true;

         }).error(function(data, status, headers, config) {
             //处理错误  
             alert('用户不存在或密码错误！');
         });
     };
     $scope.opt = {};
     $scope.receive4 = function(option) {
    	 var sec=$rootScope.hour*3600+$rootScope.minute*60+$rootScope.time;
         alert(sec);
         alert(option.selected1);
         alert(option.selected2);
         alert(option.selected3);
         var isChecked=$scope.count;
         alert(isChecked);
         $http.get('pd.json', {
         params: {choiceId:option,ifCheck:isChecked}
     }).success(function(data, status, headers, config) {
             //加载成功之后做一些事  
             $scope.Id1=data.choiseIdA;
             $scope.Id2=data.choiseIdB;
             $scope.Id3=data.choiseIdC;
             $scope.Id4=data.choiseIdD;
             $scope.num = $scope.num+1;
             $scope.content1 = data.content2;
             $scope.content2 = data.content1;
             $scope.content3 = data.content3;
             $scope.option1 = data.choiceA;
             $scope.option2 = data.choiceB;
             $scope.option3 = data.choiceC;
             $scope.option4 = data.choiceD;
             $scope.red = "#000000";
             $scope.count = false;
             $scope.opt = {};
             $scope.before=false;
             

         }).error(function(data, status, headers, config) {
             //处理错误  
             alert('用户不存在或密码错误！');
         });

     };
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

         alert($scope.count);


     }

 });

var checkup = angular.module('checkup', [])
checkup.controller("Ctab1",function($scope,$http,$state){
        
      //   $scope.lists = {'list0':[1,2,3,4,5,6,7,8,9,10,11,12,23]};

      //   $scope.lists1={'list1':[13,14,15,20,21,22]};
      //   $scope.lists2={'list2':[16,17,18,19]};
         $scope.listrecev=function(){
             $http.get('list.json', {
              params: { typeId: "1" }
          }).success(function(data, status, headers, config) {
              //加载成功之后做一些事  
             
              $scope.lists=data.list0;
              $scope.lists1=data.list1;
              $scope.lists2=data.list2;


          }).error(function(data, status, headers, config) {
              //处理错误  
              alert('用户不存在或密码错误！');
          });


       };
       $scope.skip=function(listnum){
           alert(listnum);
           $state.go('main',{active:1,num:2});
 
      }
 



});




