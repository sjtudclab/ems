   var formlogin = angular.module('formlogin', [])


   formlogin.controller('httpCtrl', function($scope, $state, $http,$window) {

       $scope.receive = function(name, password) {
          // alert(name + password);

           $http.get('/EMS/greeting', {
               params: { name: name ,password: password }
           }).success(function(data, status, headers, config) {
               //加载成功之后做一些事  
             //  alert(data.name);
              // alert(data.checklist[0]);
        	   $window.sessionStorage.token=data.token;
               $state.go('showinfo', { name: data.name, id: data.id, cid: data.cid, subject: data.subject, time: data.time });

           }).error(function(data, status, headers, config) {
               //处理错误  
               alert('用户不存在或密码错误');
           });
       };
   });

   formlogin.controller('showCtrl', function($scope, $state, $stateParams,$window) {
       //对象记录状态
	  // alert(typeof($window.sessionStorage.problemStatus));
	  // if(typeof($window.sessionStorage.problemStatus)==undefined){
	   var problemStatus={single:'FFF'};
	   
	      $window.sessionStorage.problemStatus=JSON.stringify(problemStatus);
	      var problemStatus=JSON.parse($window.sessionStorage.problemStatus);
	      problemStatus.single={};
	      problemStatus.multiple={};
	      problemStatus.judgment={};
	      problemStatus.match={};
	      $window.sessionStorage.problemStatus=JSON.stringify(problemStatus);

		   $scope.name = $stateParams.name;
		   $scope.id = $stateParams.id;
		   $scope.cid = $stateParams.cid;
		   $scope.subject = $stateParams.subject;
		   $scope.time = $stateParams.time;
		   $window.sessionStorage.name = $stateParams.name;
		   $window.sessionStorage.id = $stateParams.id;
		   $window.sessionStorage.cid = $stateParams.cid;
		   $window.sessionStorage.subject = $stateParams.subject;
		   $window.sessionStorage.time = $stateParams.time;
      // alert($scope.name);
       $scope.click=function(){
     	  $state.go('main',{active:'',num:'',brand:''});
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
   
   demo0.controller('showMain', function($scope, $state, $stateParams,$window) {

       $scope.name = $window.sessionStorage.name;
       $scope.id = $window.sessionStorage.cid;
       $scope.cid = $window.sessionStorage.id;
       $scope.subject = $window.sessionStorage.subject;
       $scope.active=$stateParams.active*1; //面板切换


   });
   
   
   demo0.controller("Countdown", function($scope, $interval,$window) {
       var second = 7200,
           timePromise = undefined;
       $window.sessionStorage.hour = 0;
       $window.sessionStorage.minute = 0;
       $window.sessionStorage.time = 0;

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
               $window.sessionStorage.hour = hour;
               $window.sessionStorage.minute = minute;
               $window.sessionStorage.time = miao;
               second--;

           }
       }, 1000, 0);

   });
   
   demo0.controller('skiptb1', function($scope, $http, $window,  $state, $stateParams,$rootScope) {
	    //单选题
	          $scope.option = {};
	          var singleStatus=JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
	          
	    		  if ($stateParams.type == 1) {
	    		        alert('cp面板1'+$scope.active);
	    		        $scope.id = $stateParams.num*1; 
	    		        //检查某题
	    		        $http.get('single0.json', {
	    	                params: { typeId:0,token:$window.sessionStorage.token,id:$stateParams.num}
	    	            }).success(function(data, status, headers, config) {
	    	                // 试题
	    	                $scope.content = data.content;
	    	                $scope.lists = data.choiceList;
	    	                var length = $scope.lists.length;
		    		    	for (var i = 0; i < length; i++) {
		    		            $scope.lists[i].alp=String.fromCharCode(i+65);
		    		        }
	    	                //试题状态
	    	              
	    	                $scope.option.optionsRadios = data.choiceId;
	    	                if (data.ifCheck) {
	    	                    $scope.red = "#FF6347";
	    	                    $scope.count = true;
	    	                }else{
	    	                	 $scope.red = "#000000";
	    			             $scope.count = false;
	    	                	
	    	                }
	    	                if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }

	    	            }).error(function(data, status, headers, config) {
	    	                //处理错误  
	    	                alert('tab1出错！');
	    	            });

	    		    } else{
	    		    	 var singleStatus=JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
	    		    	 if(singleStatus.single.id){
	    		    	
	    		    	$scope.id=singleStatus.single.id;
	    		    	$scope.content=singleStatus.single.content;
	    		    	$scope.lists=singleStatus.single.choiceList;
	    		    	
	    		    	//status
	    		    	$scope.option.optionsRadios=singleStatus.single.option;
	    		    	$rootScope.counter=singleStatus.single.counter;
	    		    	if (singleStatus.single.ifCheck) {
    	                    $scope.red = "#FF6347";
    	                    $scope.count = true;
    	                }else{
   	                	 $scope.red = "#000000";
			             $scope.count = false;
	                	
	                    }
	    		    	if ( singleStatus.single.id == 1) { $scope.before = true; }else{$scope.before = false; }
	    		    	
	    
	    		    	
	    		    //	alert("保留tab0!")
	    		   	 
	    		    	 }else{
	    		    		 $window.sessionStorage.active=0;
	    		    		  //初始化试题
	    		    		  $http.get('single.json', {
	    		    			  params: { typeId:0,token:$window.sessionStorage.token}
	    			            }).success(function(data, status, headers, config) {
	    			                //试题
	    			            	$scope.id = data.id; 
	    	    	                $scope.content = data.content;
	    	    	                $scope.lists = data.choiceList;
	    	    	                var length = $scope.lists.length;
	    		    		    	for (var i = 0; i < length; i++) {
	    		    		            $scope.lists[i].alp=String.fromCharCode(i+65);
	    		    		        }
	    	    	                //试题状态
	    	    	                $scope.option.optionsRadios = data.choiceId;
	    	    	                if (data.ifCheck) {
	    	    	                    $scope.red = "#FF6347";
	    	    	                    $scope.count = true;
	    	    	                }
	    	    	                else{
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
	    		  
      
       //下一题
       $scope.receive1 = function(option) {

         //  alert(option.optionsRadios);
           var isChecked=$scope.count;
      
           singleStatus.single.id=$scope.id;
           singleStatus.single.content=$scope.content;
           singleStatus.single.choiceList=$scope.lists;
           
           singleStatus.single.option=option.optionsRadios;
           singleStatus.single.ifCheck=$scope.count;
           singleStatus.single.counter=$rootScope.counter;
           
           $window.sessionStorage.problemStatus=JSON.stringify(singleStatus);
           
          
           $http.get('single0.json', {
           params: {token:$window.sessionStorage.token,typeId:0,id: $scope.id,choiceId:option,ifCheck:isChecked}
       }).success(function(data, status, headers, config) {
    	// 试题
    	   $scope.id=data.id;
           $scope.content = data.content;
           $scope.lists = data.choiceList;
           var length = $scope.lists.length;
	       for (var i = 0; i < length; i++) {
	            $scope.lists[i].alp=String.fromCharCode(i+65);
	        }
           //试题状态
           $scope.option.optionsRadios = data.choiceId;
           if (data.ifCheck) {
               $scope.red = "#FF6347";
               $scope.count = true;
           }else{
          	 $scope.red = "#000000";
             $scope.count = false;
        	
          } 
           if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }


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
           $http.get('single.json', {
               params: { token:$window.sessionStorage.token,typeId:0,id: $scope.id,choiceId:option,ifCheck:isChecked}
           }).success(function(data, status, headers, config) {
        	// 试题
        	   $scope.id=data.id;
               $scope.content = data.content;
               $scope.lists = data.choiceList;
               var length = $scope.lists.length;
		    	for (var i = 0; i < length; i++) {
		            $scope.lists[i].alp=String.fromCharCode(i+65);
		        }
               //试题状态
               $scope.option.optionsRadios = data.choiceId;
               if (data.ifCheck) {
                   $scope.red = "#FF6347";
                   $scope.count = true;
               }else{
              	 $scope.red = "#000000";
                 $scope.count = false;
            	
              } 
               if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }


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
               $rootScope.counter= $rootScope.counter-1;
              
           } else {
               $scope.red = "#FF6347";
               $scope.count = true;
               $rootScope.counter= $rootScope.counter+1;
           
           }

       }

   });
   
   demo0.controller('skiptb2', function($scope, $http, $window,  $state, $stateParams,$rootScope) {
	    //多选题
	          $scope.option = [];
	          $scope.isSelected = function(id) {
	              return $scope.option.indexOf(id) >= 0;
	           }
	          $scope.updateSelection = function($event, id) {
	              var checkbox = $event.target;
	              var checked=checkbox.checked;
	               if (checked) {
	                 $scope.option.push(id);
	               } else {
	                 var idx = $scope.selected.indexOf(id);
	                   $scope.option.splice(idx, 1);
	               }

	          }
	          var multiStatus=JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
	          
	    		  if ($stateParams.type == 2) {
	    		        alert('cp面板2'+$scope.active);
	    		        $scope.id = $stateParams.num*1; 
	    		        //检查某题
	    		        $http.get('multiple.json', {
	    	                params: { typeId:1,token:$window.sessionStorage.token,id:$stateParams.num}
	    	            }).success(function(data, status, headers, config) {
	    	                // 试题
	    	                $scope.content = data.content;
	    	                $scope.lists = data.choiceList;
	    	                var length = $scope.lists.length;
		    		    	for (var i = 0; i < length; i++) {
		    		            $scope.lists[i].alp=String.fromCharCode(i+65);
		    		        }
	    	                //试题状态
	    	              
	    	                $scope.option = data.choiceIdList;
	    	                if (data.ifCheck) {
	    	                    $scope.red = "#FF6347";
	    	                    $scope.count = true;
	    	                }else{
	    	                	 $scope.red = "#000000";
	    			             $scope.count = false;
	    	                	
	    	                }
	    	                if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }

	    	            }).error(function(data, status, headers, config) {
	    	                //处理错误  
	    	                alert('tab1出错！');
	    	            });

	    		    } else{
	    		    	 var multiStatus=JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
	    		    	 if(multiStatus.multiple.id){
	    		    	
	    		    	$scope.id=multiStatus.multiple.id;
	    		    	$scope.content=multiStatus.multiple.content;
	    		    	$scope.lists=multiStatus.multiple.choiceList;
	    		    	
	    		    	//status
	    		    	$scope.option=multiStatus.multiple.choiceIdList;
	    		    	$rootScope.counter=multiStatus.multiple.counter;
	    		    	if (multiStatus.multiple.ifCheck) {
   	                    $scope.red = "#FF6347";
   	                    $scope.count = true;
   	                }else{
  	                	 $scope.red = "#000000";
			             $scope.count = false;
	                	
	                    }
	    		    	if ( multiStatus.multiple.id == 1) { $scope.before = true; }else{$scope.before = false; }
	    		    	
	    
	    		    	
	    		    	//alert("保留tab1!")
	    		   	 
	    		    	 }else{
	    		    		//初始化试题
	    		    		  $http.get('multiple.json', {
	    		    			  params: { typeId:1,token:$window.sessionStorage.token}
	    			            }).success(function(data, status, headers, config) {
	    			                //试题
	    			            	$scope.id = data.id; 
	    	   	                    $scope.content = data.content;
	    	   	                    $scope.lists = data.choiceList;
	    	   	                var length = $scope.lists.length;
	    		    		    	for (var i = 0; i < length; i++) {
	    		    		            $scope.lists[i].alp=String.fromCharCode(i+65);
	    		    		        }
	    	   	                //试题状态
	    	   	                $scope.option= data.choiceIdList;
	    	   	               // alert(data.choiceIdList);
	    	   	                if (data.ifCheck) {
	    	   	                    $scope.red = "#FF6347";
	    	   	                    $scope.count = true;
	    	   	                }
	    	   	                else{
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
	    
	    		        
     
      //下一题
      $scope.receive2 = function(option) {

        //  alert(option.optionsRadios);
          var isChecked=$scope.count;
     
          multiStatus.multiple.id=$scope.id;
          multiStatus.multiple.content=$scope.content;
          multiStatus.multiple.choiceList=$scope.lists;
          
          multiStatus.multiple.option=option;
          multiStatus.multiple.ifCheck=$scope.count;
          multiStatus.multiple.counter=$rootScope.counter;
          
          $window.sessionStorage.problemStatus=JSON.stringify(multiStatus);
          
         
          $http.get('multiple0.json', {
          params: {token:$window.sessionStorage.token,typeId:1,id: $scope.id,choiceId:option,ifCheck:isChecked}
      }).success(function(data, status, headers, config) {
   	// 试题
   	      $scope.id=data.id;
          $scope.content = data.content;
          $scope.lists = data.choiceList;
          var length = $scope.lists.length;
	       for (var i = 0; i < length; i++) {
	            $scope.lists[i].alp=String.fromCharCode(i+65);
	        }
          //试题状态
          $scope.option = data.choiceIdList;
          if (data.ifCheck) {
              $scope.red = "#FF6347";
              $scope.count = true;
          }else{
         	 $scope.red = "#000000";
            $scope.count = false;
       	
         } 
          if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }


          }).error(function(data, status, headers, config) {
              //处理错误  
              alert('用户不存在或密码错误！');
          });

      };
    //上一题
      $scope.sreceive2 = function(option) {
         // alert(option.optionsRadios);
          var isChecked = $scope.count;
        //  alert(isChecked);
        //  alert(ID);
          $http.get('multiple.json', {
              params: { token:$window.sessionStorage.token,typeId:1,id: $scope.id,choiceId:option,ifCheck:isChecked}
          }).success(function(data, status, headers, config) {
       	// 试题
       	   $scope.id=data.id;
              $scope.content = data.content;
              $scope.lists = data.choiceList;
              var length = $scope.lists.length;
		    	for (var i = 0; i < length; i++) {
		            $scope.lists[i].alp=String.fromCharCode(i+65);
		        }
              //试题状态
              $scope.option= data.choiceIdList;
              if (data.ifCheck) {
                  $scope.red = "#FF6347";
                  $scope.count = true;
              }else{
             	 $scope.red = "#000000";
                $scope.count = false;
           	
             } 
              if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }


          }).error(function(data, status, headers, config) {
              //处理错误  
              alert('用户不存在或密码错误！');
          });

      };
 //     $rootScope.counter = 0;
      //标志旗帜颜色
      $scope.chgColor = function(count) {


          if (count) {
              $scope.red = "#000000";
              $scope.count = false;
              $rootScope.counter= $rootScope.counter-1;
             
          } else {
              $scope.red = "#FF6347";
              $scope.count = true;
              $rootScope.counter= $rootScope.counter+1;
          
          }

      }

  });

   demo0.controller('skiptb3', function($scope, $http, $window,  $state, $stateParams,$rootScope) {
	    //判断题
	          $scope.option = {};
	          var judgStatus=JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
	          
	    
	    		  if ($stateParams.type == 3) {
	    		        alert('cp面板3'+$scope.active);
	    		        $scope.id = $stateParams.num*1; 
	    		        //检查某题
	    		        $http.get('judg.json', {
	    	                params: { typeId:2,token:$window.sessionStorage.token,id:$stateParams.num}
	    	            }).success(function(data, status, headers, config) {
	    	                // 试题
	    	                $scope.content = data.content;
	    	                $scope.lists = data.choiceList;
	    	                var length = $scope.lists.length;
		    		    	for (var i = 0; i < length; i++) {
		    		            $scope.lists[i].alp=String.fromCharCode(i+65);
		    		        }
	    	                //试题状态
	    	              
	    	                $scope.option.optionsRadios = data.choiceId;
	    	                if (data.ifCheck) {
	    	                    $scope.red = "#FF6347";
	    	                    $scope.count = true;
	    	                }else{
	    	                	 $scope.red = "#000000";
	    			             $scope.count = false;
	    	                	
	    	                }
	    	                if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }

	    	            }).error(function(data, status, headers, config) {
	    	                //处理错误  
	    	                alert('tab1出错！');
	    	            });

	    		    } else{
	    		    	var judgStatus=JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
	    		    	if(judgStatus.judgment.id){
	    		    	
	    		    	$scope.id=judgStatus.judgment.id;
	    		    	$scope.content=judgStatus.judgment.content;
	    		    	$scope.lists=judgStatus.judgment.choiceList;
	    		    	
	    		    	//status
	    		    	$scope.option.optionsRadios=judgStatus.judgment.option;
	    		    	$rootScope.counter=judgStatus.judgment.counter;
	    		    	if (judgStatus.judgment.ifCheck) {
   	                    $scope.red = "#FF6347";
   	                    $scope.count = true;
   	                }else{
  	                	 $scope.red = "#000000";
			             $scope.count = false;
	                	
	                    }
	    		    	if ( judgStatus.judgment.id == 1) { $scope.before = true; }else{$scope.before = false; }
	    		    	
	    		    	//alert("保留tab3!")
	    		    	
	    		    	}else{
	    		    		//初始化试题
	    		    		  $http.get('judg.json', {
	    		    			  params: { typeId:2,token:$window.sessionStorage.token}
	    			            }).success(function(data, status, headers, config) {
	    			                //试题
	    			            	$scope.id = data.id; 
	    	   	                    $scope.content = data.content;
	    	   	                    $scope.lists = data.choiceList;
	    	   	                     var length = $scope.lists.length;
	    		    		    	for (var i = 0; i < length; i++) {
	    		    		            $scope.lists[i].alp=String.fromCharCode(i+65);
	    		    		        }
	    	   	                //试题状态
	    	   	                $scope.option.optionsRadios = data.choiceId;
	    	   	                if (data.ifCheck) {
	    	   	                    $scope.red = "#FF6347";
	    	   	                    $scope.count = true;
	    	   	                }
	    	   	                else{
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
	    		  
	
	    		        
     
      //下一题
      $scope.receive3 = function(option) {

        //  alert(option.optionsRadios);
          var isChecked=$scope.count;
     
          judgStatus.judgment.id=$scope.id;
          judgStatus.judgment.content=$scope.content;
          judgStatus.judgment.choiceList=$scope.lists;
          
          judgStatus.judgment.option=option.optionsRadios;
          judgStatus.judgment.ifCheck=$scope.count;
          judgStatus.judgment.counter=$rootScope.counter;
          
          $window.sessionStorage.problemStatus=JSON.stringify(judgStatus);
          
         
          $http.get('judg0.json', {
          params: {token:$window.sessionStorage.token,typeId:2,id: $scope.id,choiceId:option,ifCheck:isChecked}
      }).success(function(data, status, headers, config) {
   	// 试题
   	     $scope.id=data.id;
          $scope.content = data.content;
          $scope.lists = data.choiceList;
          var length = $scope.lists.length;
	       for (var i = 0; i < length; i++) {
	            $scope.lists[i].alp=String.fromCharCode(i+65);
	        }
          //试题状态
          $scope.option.optionsRadios = data.choiceId;
          if (data.ifCheck) {
              $scope.red = "#FF6347";
              $scope.count = true;
          }else{
         	 $scope.red = "#000000";
            $scope.count = false;
       	
         } 
          if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }


          }).error(function(data, status, headers, config) {
              //处理错误  
              alert('用户不存在或密码错误！');
          });

      };
    //上一题
      $scope.sreceive3 = function(option) {
         // alert(option.optionsRadios);
          var isChecked = $scope.count;
        //  alert(isChecked);
        //  alert(ID);
          $http.get('judg.json', {
              params: { token:$window.sessionStorage.token,typeId:2,id: $scope.id,choiceId:option,ifCheck:isChecked}
          }).success(function(data, status, headers, config) {
       	// 试题
       	      $scope.id=data.id;
              $scope.content = data.content;
              $scope.lists = data.choiceList;
              var length = $scope.lists.length;
		    	for (var i = 0; i < length; i++) {
		            $scope.lists[i].alp=String.fromCharCode(i+65);
		        }
              //试题状态
              $scope.option.optionsRadios = data.choiceId;
              if (data.ifCheck) {
                  $scope.red = "#FF6347";
                  $scope.count = true;
              }else{
             	 $scope.red = "#000000";
                $scope.count = false;
           	
             } 
              if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }


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
              $rootScope.counter= $rootScope.counter-1;
             
          } else {
              $scope.red = "#FF6347";
              $scope.count = true;
              $rootScope.counter= $rootScope.counter+1;
          
          }

      }

  });
   

   demo0.controller('skiptb4', function($scope, $http, $window,  $state, $stateParams,$rootScope) {
	 //匹配题
	          $scope.option = {};
	          var matchStatus=JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
	          
	    		  if ($stateParams.type == 4) {
	    		        alert('cp面板4'+$scope.active);
	    		        $scope.id = $stateParams.num*1; 
	    		        //检查某题
	    		        $http.get('match.json', {
	    	                params: { typeId:3,token:$window.sessionStorage.token,id:$stateParams.num}
	    	            }).success(function(data, status, headers, config) {
	    	                // 试题
	    	                $scope.contentlists = data.contentList;
	    	                $scope.lists = data.choiceList;
	    	                var length = $scope.lists.length;
		    		    	for (var i = 0; i < length; i++) {
		    		            $scope.lists[i].alp=String.fromCharCode(i+65);
		    		        }
	    	                //试题状态
	    	              
	    	                $scope.option = data.choiceIdList;
	    	                if (data.ifCheck) {
	    	                    $scope.red = "#FF6347";
	    	                    $scope.count = true;
	    	                }else{
	    	                	 $scope.red = "#000000";
	    			             $scope.count = false;
	    	                	
	    	                }
	    	                if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }

	    	            }).error(function(data, status, headers, config) {
	    	                //处理错误  
	    	                alert('tab1出错！');
	    	            });

	    		    } else{
	    		    	
	    		    	var matchStatus=JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
	    		    	if(matchStatus.match.id){
	    		    	$scope.id=matchStatus.match.id;
	    		    	$scope.contentlists=matchStatus.match.contentList;
	    		    	$scope.lists=matchStatus.match.choiceList;
	    		    	
	    		    	//status
	    		    	$scope.option=matchStatus.match.option;
	    		    	$rootScope.counter=matchStatus.match.counter;
	    		    	if (matchStatus.match.ifCheck) {
  	                    $scope.red = "#FF6347";
  	                    $scope.count = true;
  	                    }else{
 	                	 $scope.red = "#000000";
			             $scope.count = false;
	                	
	                    }
	    		    	if ( matchStatus.match.id == 1) { $scope.before = true; }else{$scope.before = false; }
	    		    	
	    		    	 //alert("保留tab4!")
	    		    	}else{
	    		    		 //初始化试题
	    		    		  $http.get('match.json', {
	    		    			  params: { typeId:3,token:$window.sessionStorage.token}
	    			            }).success(function(data, status, headers, config) {
	    			                //试题
	    			            	$scope.id = data.id; 
	    	  	                    $scope.contentlists = data.contentList;
	    	  	                    $scope.lists = data.choiceList;
	    	  	                var length = $scope.lists.length;
	    		    		    	for (var i = 0; i < length; i++) {
	    		    		            $scope.lists[i].alp=String.fromCharCode(i+65);
	    		    		        }
	    	  	                //试题状态
	    	  	                $scope.option = data.choiceIdList;
	    	  	                if (data.ifCheck) {
	    	  	                    $scope.red = "#FF6347";
	    	  	                    $scope.count = true;
	    	  	                }
	    	  	                else{
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
	    		  
	    	 
	    		        
    
     //下一题
     $scope.receive4 = function(option) {

       //  alert(option.optionsRadios);
         var isChecked=$scope.count;
    
         matchStatus.match.id=$scope.id;
         alert(matchStatus.match.id);
         matchStatus.match.contentList=$scope.contentlists;
         matchStatus.match.choiceList=$scope.lists;
         
         matchStatus.match.option=option;
         matchStatus.match.ifCheck=$scope.count;
         matchStatus.match.counter=$rootScope.counter;
         
         $window.sessionStorage.problemStatus=JSON.stringify(matchStatus);
         
        
         $http.get('match0.json', {
         params: {token:$window.sessionStorage.token,typeId:3,id: $scope.id,choiceId:option,ifCheck:isChecked}
     }).success(function(data, status, headers, config) {
  	// 试题
  	     $scope.id=data.id;
         $scope.contentlists = data.contentList;
         $scope.lists = data.choiceList;
         var length = $scope.lists.length;
	       for (var i = 0; i < length; i++) {
	            $scope.lists[i].alp=String.fromCharCode(i+65);
	        }
         //试题状态
         $scope.option= data.choiceIdList;
         if (data.ifCheck) {
             $scope.red = "#FF6347";
             $scope.count = true;
         }else{
        	 $scope.red = "#000000";
           $scope.count = false;
      	
        } 
         if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }


         }).error(function(data, status, headers, config) {
             //处理错误  
             alert('用户不存在或密码错误！');
         });

     };
   //上一题
     $scope.sreceive4 = function(option) {
        // alert(option.optionsRadios);
         var isChecked = $scope.count;
       //  alert(isChecked);
       //  alert(ID);
         $http.get('match.json', {
             params: { token:$window.sessionStorage.token,typeId:3,id: $scope.id,choiceId:option,ifCheck:isChecked}
         }).success(function(data, status, headers, config) {
      	// 试题
      	   $scope.id=data.id;
             $scope.contentlists = data.contentList;
             $scope.lists = data.choiceList;
             var length = $scope.lists.length;
		    	for (var i = 0; i < length; i++) {
		            $scope.lists[i].alp=String.fromCharCode(i+65);
		        }
             //试题状态
             $scope.option = data.choiceIdList;
             if (data.ifCheck) {
                 $scope.red = "#FF6347";
                 $scope.count = true;
             }else{
            	 $scope.red = "#000000";
                 $scope.count = false;
          	
            } 
             if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }


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
             $rootScope.counter= $rootScope.counter-1;
            
         } else {
             $scope.red = "#FF6347";
             $scope.count = true;
             $rootScope.counter= $rootScope.counter+1;
         
         }

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
         //  alert(listnum);
           $state.go('main',{active:0,num:2,type:1});
 
      }
 



});




