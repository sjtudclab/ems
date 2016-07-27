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
        	   var infoStatus={};
        	   $window.sessionStorage.infoStatus=JSON.stringify(infoStatus);
        	   var infoStatus=JSON.parse($window.sessionStorage.infoStatus);
        	   infoStatus.token=data.token;
        	   infoStatus.name = data.name;
        	   infoStatus.gender = data.gender;
        	   infoStatus.id = data.id;
        	   infoStatus.cid = data.cid;
        	   infoStatus.subject = data.subject;
        	   infoStatus.time = data.time;
        	 //  $window.sessionStorage.photo=data.photo;
        	   infoStatus.photo = data.photo;
        	   infoStatus.Rid = data.Rid;
        	   $window.sessionStorage.infoStatus=JSON.stringify(infoStatus);
               $state.go('showinfo');

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
	   var problemStatus={};
	   
	      $window.sessionStorage.problemStatus=JSON.stringify(problemStatus);
	      var problemStatus=JSON.parse($window.sessionStorage.problemStatus);
	      problemStatus.single={};
	      problemStatus.multiple={};
	      problemStatus.judgment={};
	      problemStatus.match={};
	      $window.sessionStorage.problemStatus=JSON.stringify(problemStatus);

	      var infoStatus=JSON.parse($window.sessionStorage.infoStatus);
	       $scope.photo=infoStatus.photo;
		   $scope.name = infoStatus.name;
		   $scope.gender = infoStatus.gender;
		   $scope.id = infoStatus.id;
		   $scope.cid = infoStatus.cid;
		   $scope.subject = infoStatus.subject;
		   $scope.time = infoStatus.time;
       $scope.click=function(){
     	  $state.go('main',{active:'',num:'',brand:''});
       }
       


   });
   formlogin.controller('showinfo', function($scope, $state, $stateParams,$window) {

	   var infoStatus=JSON.parse($window.sessionStorage.infoStatus);
       $scope.photo=infoStatus.photo;
	   $scope.name = infoStatus.name;
	   $scope.gender = infoStatus.gender;
	   $scope.id = infoStatus.id;
	   $scope.cid = infoStatus.cid;
	   $scope.subject = infoStatus.subject;


   });
   


   var demo0 = angular.module('demo0', ['ui.bootstrap'])
   
   demo0.controller('showMain', function($scope, $state, $stateParams,$window) {

       $scope.active=$stateParams.active*1; //面板切换


   });
   
   
   demo0.controller("timeinfo", function($scope, $interval,$window,$http) {
	   $http.get('time.json', {
	        params: {token:$window.sessionStorage.token}
	    }).success(function(data, status, headers, config) {
	    	var second = data,timePromise = undefined;
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
	   
    //   $window.sessionStorage.hour = 0;
    //   $window.sessionStorage.minute = 0;
    //   $window.sessionStorage.time = 0;

       

   });
   
   demo0.controller('skiptb1', function($scope, $http, $window,  $state, $stateParams,$rootScope) {
	    //单选题
	          $scope.option = {};
	          var singleStatus=JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
	          
	    		  if ($stateParams.type == 1) {
	    		     //   alert('cp面板1'+$scope.active);
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
	    		      //  alert('cp面板2'+$scope.active);
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
	    		       // alert('cp面板3'+$scope.active);
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
	    		       // alert('cp面板4'+$scope.active);
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
	    	              
	    	                $scope.option = data.choiceIdMap;
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
	    	  	                $scope.option = data.choiceIdMap;
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
         $scope.option= data.choiceIdMap;
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
             $scope.option = data.choiceIdMap;
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
checkup.controller('btnCtrl', function($scope) {

            switch ($scope.list) {
                case 0: //检查
                    $scope.btnClass = 'button button-caution button-box button-large';
                    break;
                case 1:  //已完成
                    $scope.btnClass = 'button button-primary button-box button-large';
                    break;
                case 2:  //未完成
                    $scope.btnClass = 'button button-large button-box button-border';
                    break;
                default:
                    $scope.btnClass = 'button button-large button-box button-border';
            }

        });
//单选题
checkup.controller("Ctab1",function($scope,$http,$state,$window){
       
	        $http.get('checklist.json', {
	            params: { token:$window.sessionStorage.token,typeId:0 }
	        }).success(function(data, status, headers, config) {
	            $scope.lists = data;

	        }).error(function(data, status, headers, config) {
	            //处理错误  
	            alert('用户不存在或密码错误！');
	        });
	        
	    
       $scope.skip=function(listnum){
    	   
           //  alert(listnum);
             $state.go('main',{active:0,num:2,type:1});
 
      }


});
//多选题
checkup.controller("Ctab2",function($scope,$http,$state,$window){
    
    $http.get('checklist.json', {
        params: { token:$window.sessionStorage.token,typeId:1  }
    }).success(function(data, status, headers, config) {
        $scope.lists = data;

    }).error(function(data, status, headers, config) {
        //处理错误  
        alert('用户不存在或密码错误！');
    });
    

$scope.skip=function(listnum){
    //    alert(listnum);
    $state.go('main',{active:1,num:2,type:2});

}


});
//判断题
checkup.controller("Ctab3",function($scope,$http,$state,$window){
    
    $http.get('checklist.json', {
        params: { token:$window.sessionStorage.token,typeId:2 }
    }).success(function(data, status, headers, config) {
        $scope.lists = data;

    }).error(function(data, status, headers, config) {
        //处理错误  
        alert('用户不存在或密码错误！');
    });
    

$scope.skip=function(listnum){
   // alert(listnum);
    $state.go('main',{active:2,num:2,type:3});

}


});
//匹配题
checkup.controller("Ctab4",function($scope,$http,$state,$window){
    
    $http.get('checklist.json', {
        params: { token:$window.sessionStorage.token,typeId:3 }
    }).success(function(data, status, headers, config) {
        $scope.lists = data;

    }).error(function(data, status, headers, config) {
        //处理错误  
        alert('用户不存在或密码错误！');
    });
    

$scope.skip=function(listnum){
  // alert(listnum);
   $state.go('main',{active:3,num:2,type:4});

}


});




