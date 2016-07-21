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
       //对象记录状态
	  // alert(typeof($rootScope.problemStatus));
	  // if(typeof($rootScope.problemStatus)==undefined){
		   $rootScope.problemStatus={"single":{},"multiple":{},"judgment":{},"match":{}};
		  // alert("haha");
	 //  }
	   
	   $rootScope.name = $stateParams.name;
	   $rootScope.id = $stateParams.id;
	   $rootScope.cid = $stateParams.cid;
	   $rootScope.subject = $stateParams.subject;
	   $rootScope.time = $stateParams.time;
      // alert($scope.name);
       $scope.click=function(){
     	  $state.go('main',{active:"",num:'',brand:''});
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
	          $scope.option = {};
	    	  if($stateParams.brand==1){
	    		  if ($stateParams.active == 0) {
	    		        alert('cp面板1'+$scope.active);
	    		        $scope.num1 = $stateParams.num*1; 
	    		        //检查某题
	    		        $http.get('new2.json', {
	    	                params: { typeId:0,token:$rootScope.token,id:$stateParams.num}
	    	            }).success(function(data, status, headers, config) {
	    	                // 试题
	    	                $scope.Id1 = data.choiceIdA;
	    	                $scope.Id2 = data.choiceIdB;
	    	                $scope.Id3 = data.choiceIdC;
	    	                $scope.Id4 = data.choiceIdD;
	    	                $scope.title1 = data.content;
	    	                $scope.option1 = data.choiceA;
	    	                $scope.option2 = data.choiceB;
	    	                $scope.option3 = data.choiceC;
	    	                $scope.option4 = data.choiceD;
	    	                //试题状态
	    	                $scope.option.optionsRadios = data.choiceIdA;
	    	                if (data.ifCheck) {
	    	                    $scope.red = "#FF6347";
	    	                    $scope.count = true;
	    	                }
	    	                if ( $scope.num1 == 1) { $scope.before = true; }

	    	            }).error(function(data, status, headers, config) {
	    	                //处理错误  
	    	                alert('tab1出错！');
	    	            });

	    		    } else{
	    		    	
	    		    	$scope.Id1=$rootScope.problemStatus.single.choiceAId;
	    		    	$scope.Id2=$rootScope.problemStatus.single.choiceBId;
	    		    	$scope.Id3=$rootScope.problemStatus.single.choiceCId;
	    		    	$scope.Id4=$rootScope.problemStatus.single.choiceDId;
	    		    	$scope.option1=$rootScope.problemStatus.single.choiceA;
	    		    	$scope.option2=$rootScope.problemStatus.single.choiceB;
	    		    	$scope.option3=$rootScope.problemStatus.single.choiceC;
	    		    	$scope.option4=$rootScope.problemStatus.single.choiceD;
	    		    	$scope.title1=$rootScope.problemStatus.single.title1;
	    		    	$scope.num1=$rootScope.problemStatus.single.num1;
	    		    	$scope.count=$rootScope.problemStatus.single.count;
	    		    	$scope.red=$rootScope.problemStatus.single.red;
	    		    	$scope.before=$rootScope.problemStatus.single.before;
	    		    	$scope.option.optionsRadios=$rootScope.problemStatus.single.option.optionsRadios;
	    		    	
	    		    	alert("保留tab0!")
	    		    	}
	    		  
	    	  }else{
	    		  alert("hh-1");
	    		  $rootScope.active=0;
	    		  //初始化试题
	    		  $http.get('new.json', {
	    			  params: { typeId:0,token:$rootScope.token}
		            }).success(function(data, status, headers, config) {
		                //试题
		            	 $scope.Id1 = data.choiceIdA;
		            	 $scope.Id2 = data.choiceIdB;
		            	 $scope.Id3 = data.choiceIdC;
		            	 $scope.Id4 = data.choiceIdD;
		            	 $scope.num1 = 1;
		            	 $scope.title1 = data.content;
		            	 $scope.option1 = data.choiceA;
		            	 $scope.option2 = data.choiceB;
		            	 $scope.option3 = data.choiceC;
		            	 $scope.option4 = data.choiceD;
		                //试题状态
		                $scope.before = true;
		                $scope.red = "#000000";
		                $scope.count = false;
		                

		            }).error(function(data, status, headers, config) {
		                //处理错误  
		                alert('用户不存在或密码错误！');
		            });
	    		  
	    	  }
	    		     

   
      
       //下一题
       $scope.receive1 = function(option) {
    	  // var sec=$rootScope.hour*3600+$rootScope.minute*60+$rootScope.time;
         //  alert(sec);
           alert(option.optionsRadios);
           var isChecked=$scope.count;
           alert(isChecked);
           
           alert($rootScope.problemStatus);
           
           $rootScope.problemStatus.single.choiceAId=$scope.Id1;
           $rootScope.problemStatus.single.choiceBId=$scope.Id2;
           $rootScope.problemStatus.single.choiceCId=$scope.Id3;
           $rootScope.problemStatus.single.choiceDId=$scope.Id4;
           $rootScope.problemStatus.single.choiceA=$scope.option1;
           $rootScope.problemStatus.single.choiceB=$scope.option2;
           $rootScope.problemStatus.single.choiceC=$scope.option3;
           $rootScope.problemStatus.single.choiceD=$scope.option4;
           $rootScope.problemStatus.single.title1=$scope.title1;
           $rootScope.problemStatus.single.num1=$scope.num1;
           $rootScope.problemStatus.single.count=$scope.count;
           $rootScope.problemStatus.single.red=$scope.red;
           $rootScope.problemStatus.single.option=$scope.option;
           $rootScope.problemStatus.single.before=$scope.before;
           
           
 
           $http.get('new1.json', {
           params: {token:$rootScope.token,typeId:0,id: $scope.num1,choiceId:option,ifCheck:isChecked}
       }).success(function(data, status, headers, config) {
               //试题
    	   $scope.Id1=data.choiceAId;
    	   $scope.Id2=data.choiceBId;
    	   $scope.Id3=data.choiceCId;
    	   $scope.Id4=data.choiceDId;
    	   $scope.num1 =  $scope.num1+1;
    	   $scope.title1 = data.content;
    	   $scope.option1 = data.choiceA;
    	   $scope.option2 = data.choiceB;
    	   $scope.option3 = data.choiceC;
    	   $scope.option4 = data.choiceD;
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
       $scope.sreceive1 = function(option) {
          // alert(option.optionsRadios);
           var isChecked = $scope.count;
         //  alert(isChecked);
         //  alert(ID);
           $http.get('new2.json', {
               params: { token:$rootScope.token,typeId:0,id: $scope.num1,choiceId:option,ifCheck:isChecked}
           }).success(function(data, status, headers, config) {
               //试题
        	   $scope.Id1 = data.choiceIdA;
        	   $scope.Id2 = data.choiceIdB;
        	   $scope.Id3 = data.choiceIdC;
        	   $scope.Id4 = data.choiceIdD;
        	   $scope.num1 =  $scope.num1 - 1;
               $scope.title1 = data.content;
               $scope.option1 = data.choiceA;
               $scope.option2 = data.choiceB;
               $scope.option3 = data.choiceC;
               $scope.option4 = data.choiceD;
               //试题状态
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               }

               $scope.option.optionsRadios = data.choiceIdA;
               if ( $scope.num1 == 1) { $scope.before = true; }


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
	         
	          $scope.op3 = {};
	    	  if($stateParams.brand == 1){
	    		  if ($stateParams.active == 2) {
	    		        alert('cp面板3'+$scope.active3);
	    		        $scope.num3 = $stateParams.num; 
	    		        //检查某题
	    		        $http.get('info1.json', {
	    	              params: { typeId:2,token:$rootScope.token,id:$stateParams.num}
	    	          }).success(function(data, status, headers, config) {
	    	              // $rootScope.ID = data.id;
	    	    
	    	          	  $scope.title3 = data.title;
	    	              $scope.before= true;
	    	              $scope.count = false;
	    	              $scope.op3.options = true;
	    	              if (data.count) {
	    	                  $scope.red = "#FF6347";
	    	                  $scope.count = true;
	    	              }
	    	              if ($stateParams.num == 1) { $scope.before = true; }

	    	          }).error(function(data, status, headers, config) {
	    	              //处理错误  
	    	              alert('tab3出错！');
	    	          });

	    		    }else{
	    		    	
	    		    	alert($rootScope.problemStatus.judgment.op3.options);
	    		    	alert("hahah");
	    		    	//$scope.Id1=$rootScope.problemStatus.judgment.choiceAId;
	    		    	//$scope.Id2=$rootScope.problemStatus.judgment.choiceBId;
	    		    //	$scope.option1=$rootScope.problemStatus.judgment.choiceA;
	    		    //	$scope.option2=$rootScope.problemStatus.judgment.choiceB;
	    		    	$scope.title3=$rootScope.problemStatus.judgment.title3;
	    		    	$scope.num3=$rootScope.problemStatus.judgment.num3;
	    		    	$scope.count=$rootScope.problemStatus.judgment.count;
	    		    	$scope.red=$rootScope.problemStatus.judgment.red;
	    		    	$scope.before=$rootScope.problemStatus.judgment.before;
	    		    	$scope.op3.options=$rootScope.problemStatus.judgment.op3.options;
	    		    	
	    		    	alert("保留tab3!")
	    		    	} 
	    	  }else{
	    		  alert("ss-1");
	    		
	    		  //初始化试题
	    		  $http.get('info.json', {
		                params: { typeId:2,token:$rootScope.token }
		            }).success(function(data, status, headers, config) {
		                //加载成功之后做一些事 
		                // $rootScope.ID = data.id;
		            	$scope.num3 = 1;
		            	$scope.title3 = data.title;
		                $scope.before= true;
		                $scope.count = false;

		            }).error(function(data, status, headers, config) {
		                //处理错误  
		                alert('用户不存在或密码错误！');
		            });
	    		  
	    	  }
	    		     
       
       $scope.receive3 = function(option) {
           alert(option.options);
          // var sec=$rootScope.hour*3600+$rootScope.minute*60+$rootScope.time;
           var isChecked = $scope.count;
        //   alert(sec);
           //下一题
           $rootScope.problemStatus.judgment.choiceAId=$scope.Id1;
           $rootScope.problemStatus.judgment.choiceBId=$scope.Id2;
           $rootScope.problemStatus.judgment.choiceA=$scope.option1;
           $rootScope.problemStatus.judgment.choiceB=$scope.option2;
           $rootScope.problemStatus.judgment.title3=$scope.title3;
           $rootScope.problemStatus.judgment.num3=$scope.num3;
           $rootScope.problemStatus.judgment.count=$scope.count;
           $rootScope.problemStatus.judgment.red=$scope.red;
           $rootScope.problemStatus.judgment.before=$scope.before;
           $rootScope.problemStatus.judgment.op3=option;
          
           
           
           $http.get('info1.json', {
               params: { token:$rootScope.token,typeId:0,id:$scope.num3,choiceId:option,ifCheck:isChecked}
           }).success(function(data, status, headers, config) {
        	   $scope.title3 = data.title;
           
        	   $scope.num3 = $scope.num3+1;
               $scope.red = "#000000";
               $scope.count = false;
               $scope.op3 = {};
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
               params: { token:$rootScope.token,typeId:2,id:$scope.num3,choiceId:option,ifCheck:isChecked}
           }).success(function(data, status, headers, config) {
               //加载成功之后做一些事  
        	   $scope.title3 = data.title;
               
        	   $scope.num3 = $scope.num3-1;
              
               if (data.count) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               }

               $scope.op3.options= "true";
               if ($scope.num3 == 1) { $scope.before = true; }


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
           $state.go('main',{active:0,num:2,brand:1});
 
      }
 



});




