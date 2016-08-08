﻿/*routingDemoApp.filter("trustUrl", ['$sce', function ($sce) {
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
            alert(data.Rid);
            
            switch (data.Rid) {
            case 0: //考生登录
            	 var infoStatus = {};
                 $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                 var infoStatus = JSON.parse($window.sessionStorage.infoStatus);
                 infoStatus.name = data.name;
                 infoStatus.gender = data.gender;
                 infoStatus.id = data.id;
                 infoStatus.cid = data.cid;
                 infoStatus.subject = data.subject;
                 infoStatus.time = data.time;
                 infoStatus.photo = data.photo;
                 infoStatus.Rid = data.Rid;
                 $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                break;
            case 1:  //监考员登录
            	var infoStatus = {};
                $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                var infoStatus = JSON.parse($window.sessionStorage.infoStatus);
            	//功能列表
                infoStatus.operationMetaInfo = data.authorityList;
                // 考场号
                infoStatus.roomId = data.roomId;
                // 登陆用户id
                infoStatus.Rid = data.Rid;
                $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                break;
            case 2:  //管理员登录
            	var infoStatus = {};
                $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                var infoStatus = JSON.parse($window.sessionStorage.infoStatus);
            	//功能列表
                infoStatus.operationMetaInfo = data.authorityList;
                // 考场号
                infoStatus.roomId = data.roomId;
                // 登陆用户id
                infoStatus.Rid = data.Rid;
                $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                break;
            default:
                alert('考生状态错误！');
        }
           
            if (data.Rid == 0) {
                $state.go('showinfo');
            } else {
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
        $state.go('main', { active: '', num: '', type: '' });
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

demo0.controller('TabsDemoCtrl', function($scope, $rootScope) {
    $scope.problemMetaInfo = ['单选题', '多选题', '判断题', '匹配题', '简答题'];
    $scope.active = [];
    $scope.display = [];
    $scope.color = [];
    $rootScope.index = 0;
    for (x in $scope.problemMetaInfo) {
        $scope.active[x] = "";
        $scope.display[x] = 'none';
        $scope.color[x] = "white";
    };
    $scope.$watch('index', function(newValue, oldValue) {

        for (i in $scope.active) {
            $scope.active[i] = "";
            $scope.color[i] = "white";
            $scope.display[i] = "none";

        };



        $scope.active[$rootScope.index] = "active";
        $scope.display[$rootScope.index] = "block";

        switch ($rootScope.index) {

            case 0:
                $scope.color[$rootScope.index] = "rgba(88,178,220,.5)";
                break;
            case 1:
                $scope.color[$rootScope.index] = "rgba(204,204,255,.6)";
                break;
            case 2:
                $scope.color[$rootScope.index] = "rgba(168,216,185,.6)";
                break;
            case 3:
                $scope.color[$rootScope.index] = "rgba(255,204,204,.6)";
                break;
            case 4:
                $scope.color[$rootScope.index] = "rgba(165,222,228,.8)";
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
        $rootScope.index = index;
    };

});



demo0.controller('showMain', function($scope, $state, $stateParams, $window, $http, $rootScope,$uibModal) {


    $scope.submit = function() {
        var allStatus = JSON.parse($window.sessionStorage.problemStatus);

        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 0, id: allStatus.single.nid, requestId: 0, choiceId: allStatus.single.option, ifCheck: allStatus.single.ifCheck }
        }).success(function(data, status, headers, config) {
            alert("单选题succeed");


        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 1, id: allStatus.multiple.nid, requestId: 0, choiceIdList: allStatus.multiple.option, ifCheck: allStatus.multiple.ifCheck }
        }).success(function(data, status, headers, config) {
            alert("多选题succeed");


        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 2, id: allStatus.judgment.nid, requestId: 0, choiceId: allStatus.judgment.option, ifCheck: allStatus.judgment.ifCheck }
        }).success(function(data, status, headers, config) {
            alert("判断题succeed");


        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 3, id: allStatus.match.nid, requestId: 0, choiceIdMap: allStatus.match.choiceIdMap, ifCheck: allStatus.match.ifCheck }
        }).success(function(data, status, headers, config) {
            alert("匹配提succeed");


        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 4, id: allStatus.simple.nid, requestId: 0, choiceId: allStatus.simple.answer, ifCheck: allStatus.simple.ifCheck }
        }).success(function(data, status, headers, config) {
            alert("简答题succeed");


        });
      }
    
    $scope.submit_confirm = function() {
    	var allStatus = JSON.parse($window.sessionStorage.problemStatus);

        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 0, id: allStatus.single.nid, requestId: 0, choiceId: allStatus.single.option, ifCheck: allStatus.single.ifCheck }
        }).success(function(data, status, headers, config) {
            /*alert("单选题succeed");*/


        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 1, id: allStatus.multiple.nid, requestId: 0, choiceIdList: allStatus.multiple.option, ifCheck: allStatus.multiple.ifCheck }
        }).success(function(data, status, headers, config) {
            /*alert("多选题succeed");*/


        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 2, id: allStatus.judgment.nid, requestId: 0, choiceId: allStatus.judgment.option, ifCheck: allStatus.judgment.ifCheck }
        }).success(function(data, status, headers, config) {
            /*alert("判断题succeed");*/


        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 3, id: allStatus.match.nid, requestId: 0, choiceIdMap: allStatus.match.choiceIdMap, ifCheck: allStatus.match.ifCheck }
        }).success(function(data, status, headers, config) {
            /*alert("匹配提succeed");*/


        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 4, id: allStatus.simple.nid, requestId: 0, choiceId: allStatus.simple.answer, ifCheck: allStatus.simple.ifCheck }
        }).success(function(data, status, headers, config) {
            /*alert("简答题succeed");*/


        });
    	
    	
        var modalParam = {
            backdrop: 'static',
            size: 'sm'
        };
        var header = '<div class="modal-header"><h3 class="modal-title">提醒</h3></div>';
        var footer =
            '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button><button class="btn btn-warning" type="button" ng-click="$parent.confirm=false;$close()">取消</button></div>';
        modalParam.template = header + '<div class="modal-body"><p style="font-size:150%">要提交么？</p></div>' + footer;
        $uibModal.open(modalParam).result.then(function() {
            if ($scope.confirm) {
                modalParam.template = header + '<div class="modal-body"><p style="font-size:150%">真的要提交么？</p></div>' + footer;
                $uibModal.open(modalParam).result.then(function() {
                    if ($scope.confirm) {
                        modalParam.template = header + '<div class="modal-body"><p style="font-size:150%">真的真的要提交么？</p></div>' + footer;
                        $uibModal.open(modalParam).result.then(function() {
                            if ($scope.confirm) {
                            	$state.go("finish");
                               /* alert('您已提交');*/
                            }
                        });
                    }
                });
            }
        });
    }


});


demo0.controller("timeinfo", function($scope, $interval, $window, $http, $state) {
    $http.get('/EMS/exam/getTime', {
        params: { token: $window.sessionStorage.token }
    }).success(function(data, status, headers, config) {
        var second = data,
            timePromise = undefined;
        timePromise = $interval(function() {
            if (second <0) {
                $interval.cancel(timePromise);
                timePromise = undefined;

                var allStatus = JSON.parse($window.sessionStorage.problemStatus);

                $http.get('/EMS/exam/getTopic', {
                    params: { token: $window.sessionStorage.token, typeId: 0, id: allStatus.single.nid, requestId: 0, choiceId: allStatus.single.option, ifCheck: allStatus.single.ifCheck }
                }).success(function(data, status, headers, config) {
                    alert("单选题succeed");


                });
                $http.get('/EMS/exam/getTopic', {
                    params: { token: $window.sessionStorage.token, typeId: 1, id: allStatus.multiple.nid, requestId: 0, choiceIdList: allStatus.multiple.option, ifCheck: allStatus.multiple.ifCheck }
                }).success(function(data, status, headers, config) {
                    alert("多选题succeed");


                });
                $http.get('/EMS/exam/getTopic', {
                    params: { token: $window.sessionStorage.token, typeId: 2, id: allStatus.judgment.nid, requestId: 0, choiceId: allStatus.judgment.option, ifCheck: allStatus.judgment.ifCheck }
                }).success(function(data, status, headers, config) {
                    alert("判断题succeed");


                });
                $http.get('/EMS/exam/getTopic', {
                    params: { token: $window.sessionStorage.token, typeId: 3, id: allStatus.match.nid, requestId: 0, choiceIdMap: allStatus.match.choiceIdMap, ifCheck: allStatus.match.ifCheck }
                }).success(function(data, status, headers, config) {
                    alert("匹配提succeed");


                });
                $http.get('/EMS/exam/getTopic', {
                    params: { token: $window.sessionStorage.token, typeId: 4, id: allStatus.simple.nid, requestId: 0, choiceId: allStatus.simple.answer, ifCheck: allStatus.simple.ifCheck }
                }).success(function(data, status, headers, config) {
                    alert("简答题succeed");


                });
                $state.go('finish');
                /*alert("重新");*/
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
    /*   function shuffle(list) {
           for (var i = 0; i < list.length; i++) {
               var swapIndex = Math.floor(Math.random() * list.length);
               var temp = list[i];
               list[i] = list[swapIndex];
               list[swapIndex] = temp;
           }
           return list;
       }*/

    $scope.option = {};


    if ($stateParams.type == 1) {
        //   alert('cp面板1'+$scope.active);
        $scope.nid = $stateParams.num * 1;
        $rootScope.index = $stateParams.active * 1; //面板切换
        $rootScope.counter = $window.sessionStorage.counter * 1;
        //检查某题
        $http.get('/EMS/exam/toTopic', {
            /* $http.get('single.json', {*/
            params: { typeId: 0, token: $window.sessionStorage.token, id: $stateParams.num }
        }).success(function(data, status, headers, config) {

            var singleStatus = JSON.parse($window.sessionStorage.problemStatus);

            $scope.totalItems = data.singleNum;
            $scope.currentPage = $scope.nid;

            // 试题
            $scope.content = data.content;
            $scope.lists = data.choiceList;
            $scope.lists = data.choiceList;
            var length = $scope.lists.length;
            for (var i = 0; i < length; i++) {
                $scope.lists[i].alp = String.fromCharCode(i + 65);
            }
            if (data.audio) {
                $scope.audiohide = "block";
                $scope.audio = data.audio;
            } else {
                $scope.audiohide = "none";
            }
            if (data.img) {
                $scope.imghide = "block";
                $scope.img = data.img;

            } else {
                $scope.imghide = "none";
            }
            if (data.video) {
                /*alert(data.video);*/
                $scope.videohide = "block";
                $scope.video = data.video;
            } else {
                $scope.videohide = "none";
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

            singleStatus.single.nid = $scope.nid;
            singleStatus.single.content = $scope.content;
            singleStatus.single.choiceList = $scope.lists;
            singleStatus.single.totalItems = $scope.totalItems;
            if ($scope.audiohide == "block") {
                singleStatus.single.audio = $scope.sudio;
                singleStatus.single.audiohide = $scope.sudiohide;
            } else {
                singleStatus.single.audiohide = $scope.sudiohide;
            }
            if ($scope.imghide == "block") {
                singleStatus.single.img = $scope.img;
                singleStatus.single.imghide = $scope.imghide;
            } else {
                singleStatus.single.imghide = $scope.imghide;
            }
            if ($scope.videohide == "block") {
                singleStatus.single.video = $scope.video;
                singleStatus.single.videohide = $scope.videohide;
            } else {
                singleStatus.single.videohide = $scope.videohide;
            }
            //status
            singleStatus.single.option = $scope.option.optionsRadios;
            singleStatus.single.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;


            $window.sessionStorage.problemStatus = JSON.stringify(singleStatus);


        }).error(function(data, status, headers, config) {
            //处理错误  
            alert('tab1出错！');
        });

    } else {
        var singleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态

        /**/
        if (singleStatus.single.nid) {

            $rootScope.counter = $window.sessionStorage.counter * 1;
            /*alert("single"+$rootScope.counter);*/
            $scope.nid = singleStatus.single.nid;
            $scope.content = singleStatus.single.content;
            $scope.lists = singleStatus.single.choiceList;

            $scope.totalItems = singleStatus.single.totalItems;
            $scope.currentPage = singleStatus.single.nid;
            /*  $rootScope.counter = $window.sessionStorage.counter;*/
            if (singleStatus.single.audiohide == "block") {
                $scope.audiohide = "block";
                $scope.audio = singleStatus.single.audio;
            } else {
                $scope.audiohide = "none";
                // alert("音频");

            }
            if (singleStatus.single.imghide == "block") {
                $scope.imghide = "block";
                $scope.img = singleStatus.single.img;

            } else {
                $scope.imghide = "none";
                //alert("图片");

            }
            if (singleStatus.single.videohide == "block") {
                $scope.videohide = "block";
                $scope.video = singleStatus.single.video;

            } else {
                $scope.videohide = "none";
                //alert("视频");

            }

            //status
            $scope.option.optionsRadios = singleStatus.single.option;

            if (singleStatus.single.ifCheck) {
                $scope.red = "#FF6347";
                $scope.count = true;
            } else {
                $scope.red = "#000000";
                $scope.count = false;

            }


        } else {
            //初始化试题
            $http.get('/EMS/exam/start', {
                /*  $http.get('single.json', {*/
                params: { typeId: 0, token: $window.sessionStorage.token }
            }).success(function(data, status, headers, config) {
                //试题

                if (data.audio) {
                    $scope.audiohide = "block";
                    $scope.audio = data.audio;
                } else {
                    $scope.audiohide = "none";
                }
                if (data.img) {
                    $scope.imghide = "block";
                    $scope.img = data.img;

                } else {
                    $scope.imghide = "none";
                }
                if (data.video) {
                    /*alert(data.video);*/
                    $scope.videohide = "block";
                    $scope.video = data.video;
                } else {
                    $scope.videohide = "none";
                }

                $scope.totalItems = data.singleNum;
                $scope.currentPage = 1;
                $rootScope.counter = 0;

                $scope.nid = 1;
                $scope.content = data.content;
                $scope.lists = data.choiceList;
                $scope.lists = data.choiceList;
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
                $window.sessionStorage.counter = $rootScope.counter;
                var singleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态

                singleStatus.single.nid = $scope.nid;
                singleStatus.single.content = $scope.content;
                singleStatus.single.choiceList = $scope.lists;
                singleStatus.single.totalItems = $scope.totalItems;
                if ($scope.audiohide == "block") {
                    singleStatus.single.audio = $scope.sudio;
                    singleStatus.single.audiohide = $scope.sudiohide;
                } else {
                    singleStatus.single.audiohide = $scope.sudiohide;
                }
                if ($scope.imghide == "block") {
                    singleStatus.single.img = $scope.img;
                    singleStatus.single.imghide = $scope.imghide;
                } else {
                    singleStatus.single.imghide = $scope.imghide;
                }
                if ($scope.videohide == "block") {
                    singleStatus.single.video = $scope.video;
                    singleStatus.single.videohide = $scope.videohide;
                } else {
                    singleStatus.single.videohide = $scope.videohide;
                }
                //status
                singleStatus.single.option = $scope.option.optionsRadios;
                singleStatus.single.ifCheck = $scope.count;
                $window.sessionStorage.counter = $rootScope.counter;
                $window.sessionStorage.problemStatus = JSON.stringify(singleStatus);


            }).error(function(data, status, headers, config) {
                //处理错误  
                alert('用户不存在或密码错误！');
            });
        }
    }

    $scope.opChanged = function(option) {
        var singleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        singleStatus.single.option = option.optionsRadios;
        $window.sessionStorage.problemStatus = JSON.stringify(singleStatus);
    }


    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    /* $scope.totalItems = 20;*/
    $scope.itemsPerPage = 1;

    $scope.maxSize = 2;

    $scope.pageChanged = function(option) {
        // alert($scope.currentPage);
        //  alert($scope.id);
        var isChecked = $scope.count;
        $http.get('/EMS/exam/getTopic', {
            /*   $http.get('single0.json', {*/
            params: { token: $window.sessionStorage.token, typeId: 0, id: $scope.nid, requestId: $scope.currentPage, choiceId: option.optionsRadios, ifCheck: isChecked }
        }).success(function(data, status, headers, config) {
            // 试题
            $scope.totalItems = data.singleNum;
            $scope.nid = $scope.currentPage;
            $scope.content = data.content;
            $scope.lists = data.choiceList;
            $scope.lists = data.choiceList;
            var length = $scope.lists.length;
            for (var i = 0; i < length; i++) {
                $scope.lists[i].alp = String.fromCharCode(i + 65);
            }
            if (data.audio) {
                $scope.audiohide = "block";
                $scope.audio = data.audio;
            } else {
                $scope.audiohide = "none";
            }
            if (data.img) {
                $scope.imghide = "block";
                $scope.img = data.img;

            } else {
                $scope.imghide = "none";
            }
            if (data.video) {
                /*alert(data.video);*/
                $scope.videohide = "block";
                $scope.video = data.video;
            } else {
                $scope.videohide = "none";
            }
            //试题状态
          /*  alert(data.choiceId);*/
            $scope.option.optionsRadios = data.choiceId;
            if (data.ifCheck) {
                $scope.red = "#FF6347";
                $scope.count = true;
            } else {
                $scope.red = "#000000";
                $scope.count = false;

            }


            var singleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
            singleStatus.single.nid = $scope.nid;
            singleStatus.single.content = $scope.content;
            singleStatus.single.choiceList = $scope.lists;
            singleStatus.single.totalItems = $scope.totalItems;
            if ($scope.audiohide == "block") {
                singleStatus.single.audio = $scope.sudio;
                singleStatus.single.audiohide = $scope.sudiohide;
            } else {
                singleStatus.single.audiohide = $scope.sudiohide;
            }
            if ($scope.imghide == "block") {
                singleStatus.single.img = $scope.img;
                singleStatus.single.imghide = $scope.imghide;
            } else {
                singleStatus.single.imghide = $scope.imghide;
            }
            if ($scope.videohide == "block") {
                singleStatus.single.video = $scope.video;
                singleStatus.single.videohide = $scope.videohide;
            } else {
                singleStatus.single.videohide = $scope.videohide;
            }
            //status
            singleStatus.single.option = $scope.option.optionsRadios;
            singleStatus.single.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(singleStatus);



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
            var singleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
            singleStatus.single.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(singleStatus);

        } else {
            $scope.red = "#FF6347";
            $scope.count = true;
            $rootScope.counter = $rootScope.counter + 1;
            var singleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
            singleStatus.single.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(singleStatus);

        }

    }
});

demo0.controller('skiptb2', function($scope, $http, $window, $state, $stateParams, $rootScope) {
    //多选题

    $scope.option = [];
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
        var multiStatus = JSON.parse($window.sessionStorage.problemStatus);
        multiStatus.multiple.choiceIdList = $scope.option;
        $window.sessionStorage.problemStatus = JSON.stringify(multiStatus);

    }



    if ($stateParams.type == 2) {
        //  alert('cp面板2'+$scope.active);
        $scope.nid = $stateParams.num * 1;
        $rootScope.index = $stateParams.active * 1;
        /*    $rootScope.counter =  $window.sessionStorage.counter;*/
        /* alert("mul"+$rootScope.counter);*/
        //检查某题
        $http.get('/EMS/exam/toTopic', {
            /* $http.get('multiple.json', {*/
            params: { typeId: 1, token: $window.sessionStorage.token, id: $stateParams.num }
        }).success(function(data, status, headers, config) {

            var multiStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近多选题以及状态
            $scope.totalItems = data.multiNum;
            $scope.currentPage = $scope.nid;

            // 试题
            $scope.content = data.content;
            $scope.lists = data.choiceList;
            var length = $scope.lists.length;
            for (var i = 0; i < length; i++) {
                $scope.lists[i].alp = String.fromCharCode(i + 65);
            }
            if (data.audio) {
                $scope.audiohide = "block";
                $scope.audio = data.audio;
            } else {
                $scope.audiohide = "none";
            }
            if (data.img) {
                $scope.imghide = "block";
                $scope.img = data.img;

            } else {
                $scope.imghide = "none";
            }
            if (data.video) {
                $scope.videohide = "block";
                $scope.video = data.video;

            } else {
                $scope.videohide = "none";
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


            multiStatus.multiple.nid = $scope.nid;
            multiStatus.multiple.content = $scope.content;
            multiStatus.multiple.choiceList = $scope.lists;
            if ($scope.audiohide == "block") {
                multiStatus.multiple.audio = $scope.sudio;
                multiStatus.multiple.audiohide = $scope.sudiohide;
            } else {
                multiStatus.multiple.audiohide = $scope.sudiohide;
            }
            if ($scope.imghide == "block") {
                multiStatus.multiple.img = $scope.img;
                multiStatus.multiple.imghide = $scope.imghide;
            } else {
                multiStatus.multiple.imghide = $scope.imghide;
            }
            if ($scope.videohide == "block") {
                multiStatus.multiple.video = $scope.video;
                multiStatus.multiple.videohide = $scope.videohide;
            } else {
                multiStatus.multiple.videohide = $scope.videohide;
            }
            //status
            multiStatus.multiple.choiceIdList = $scope.option;
            multiStatus.multiple.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;

            $window.sessionStorage.problemStatus = JSON.stringify(multiStatus);


        }).error(function(data, status, headers, config) {
            //处理错误  
            alert('tab1出错！');
        });

    } else {
        var multiStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        if (multiStatus.multiple.nid) {
            /* $rootScope.counter =  $window.sessionStorage.counter;*/
            $scope.totalItems = multiStatus.multiple.totalItems;
            $scope.currentPage = multiStatus.multiple.nid;

            $scope.nid = multiStatus.multiple.nid;
            $scope.content = multiStatus.multiple.content;
            $scope.lists = multiStatus.multiple.choiceList;

            if (multiStatus.multiple.audiohide == "block") {
                $scope.audiohide = "block";
                $scope.audio = multiStatus.multiple.audio;
            } else {
                $scope.audiohide = "none";
                // alert("隐藏");

            }
            if (multiStatus.multiple.imghide == "block") {
                $scope.imghide = "block";
                $scope.img = multiStatus.multiple.img;

            } else {
                $scope.imghide = "none";
                //alert("隐藏");

            }
            if (multiStatus.multiple.videohide == "block") {
                $scope.videohide = "block";
                $scope.video = multiStatus.multiple.video;

            } else {
                $scope.videohide = "none";
                //alert("隐藏");

            }

            //status
            $scope.option = multiStatus.multiple.choiceIdList;
            /* $rootScope.counter = $window.sessionStorage.counter;*/
            if (multiStatus.multiple.ifCheck) {
                $scope.red = "#FF6347";
                $scope.count = true;
            } else {
                $scope.red = "#000000";
                $scope.count = false;

            }


        } else {
            //初始化试题
            $http.get('/EMS/exam/start', {
                /*  $http.get('multiple.json', {*/
                params: { typeId: 1, token: $window.sessionStorage.token }
            }).success(function(data, status, headers, config) {
                //试题
                $scope.totalItems = data.multiNum;
                $scope.currentPage = 1;

                $scope.nid = 1;
                $scope.content = data.content;
                $scope.lists = data.choiceList;
                var length = $scope.lists.length;
                for (var i = 0; i < length; i++) {
                    $scope.lists[i].alp = String.fromCharCode(i + 65);
                }
                if (data.audio) {
                    $scope.audiohide = "block";
                    $scope.audio = data.audio;
                } else {
                    $scope.audiohide = "none";
                }
                if (data.img) {
                    $scope.imghide = "block";
                    $scope.img = data.img;

                } else {
                    $scope.imghide = "none";
                }
                if (data.video) {
                    $scope.videohide = "block";
                    $scope.video = data.video;

                } else {
                    $scope.videohide = "none";
                }
                //试题状态

                $scope.option = data.choiceIdList;
                // alert(data.choiceIdList);
                if (data.ifCheck) {
                    $scope.red = "#FF6347";
                    $scope.count = true;
                } else {
                    $scope.red = "#000000";
                    $scope.count = false;

                }

                var multiStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近多选题以及状态
                multiStatus.multiple.nid = $scope.nid;
                multiStatus.multiple.content = $scope.content;
                multiStatus.multiple.choiceList = $scope.lists;
                if ($scope.audiohide == "block") {
                    multiStatus.multiple.audio = $scope.sudio;
                    multiStatus.multiple.audiohide = $scope.sudiohide;
                } else {
                    multiStatus.multiple.audiohide = $scope.sudiohide;
                }
                if ($scope.imghide == "block") {
                    multiStatus.multiple.img = $scope.img;
                    multiStatus.multiple.imghide = $scope.imghide;
                } else {
                    multiStatus.multiple.imghide = $scope.imghide;
                }
                if ($scope.videohide == "block") {
                    multiStatus.multiple.video = $scope.video;
                    multiStatus.multiple.videohide = $scope.videohide;
                } else {
                    multiStatus.multiple.videohide = $scope.videohide;
                }
                multiStatus.multiple.totalItems = $scope.totalItems;

                multiStatus.multiple.choiceIdList = $scope.option;
                multiStatus.multiple.ifCheck = $scope.count;
                $window.sessionStorage.counter = $rootScope.counter;

                $window.sessionStorage.problemStatus = JSON.stringify(multiStatus);


            }).error(function(data, status, headers, config) {
                //处理错误  
                alert('用户不存在或密码错误！');
            });

        }
    }

    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    $scope.itemsPerPage = 1;
    $scope.maxSize = 5;

    $scope.pageChanged = function(option) {
        //alert($scope.currentPage);
        var isChecked = $scope.count;

        $http.get('/EMS/exam/getTopic', {
            /*   $http.get('multiple.json', {*/
            params: { token: $window.sessionStorage.token, typeId: 1, id: $scope.nid, requestId: $scope.currentPage, choiceIdList: option, ifCheck: isChecked }
        }).success(function(data, status, headers, config) {
            // 试题
            $scope.totalItems = data.multiNum;
            $scope.nid = $scope.currentPage;
            $scope.content = data.content;
            $scope.lists = data.choiceList;
            var length = $scope.lists.length;
            for (var i = 0; i < length; i++) {
                $scope.lists[i].alp = String.fromCharCode(i + 65);
            }
            if (data.audio) {
                $scope.audiohide = "block";
                $scope.audio = data.audio;
            } else {
                $scope.audiohide = "none";
            }
            if (data.img) {
                $scope.imghide = "block";
                $scope.img = data.img;

            } else {
                $scope.imghide = "none";
            }
            //试题状态
            /*   alert("aa"+data.choiceIdList);*/
            /* $scope.option = data.choiceIdList;*/
            if (data.choiceIdList) { $scope.option = data.choiceIdList; } else { $scope.option = []; };

            if (data.ifCheck) {
                $scope.red = "#FF6347";
                $scope.count = true;
            } else {
                $scope.red = "#000000";
                $scope.count = false;

            }

            var multiStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近多选题以及状态
            multiStatus.multiple.nid = $scope.nid;
            multiStatus.multiple.content = $scope.content;
            multiStatus.multiple.choiceList = $scope.lists;
            if ($scope.audiohide == "block") {
                multiStatus.multiple.audio = $scope.sudio;
                multiStatus.multiple.audiohide = $scope.sudiohide;
            } else {
                multiStatus.multiple.audiohide = $scope.sudiohide;
            }
            if ($scope.imghide == "block") {
                multiStatus.multiple.img = $scope.img;
                multiStatus.multiple.imghide = $scope.imghide;
            } else {
                multiStatus.multiple.imghide = $scope.imghide;
            }
            if ($scope.videohide == "block") {
                multiStatus.multiple.video = $scope.video;
                multiStatus.multiple.videohide = $scope.videohide;
            } else {
                multiStatus.multiple.videohide = $scope.videohide;
            }

            //status
            multiStatus.multiple.choiceIdList = $scope.option;
            multiStatus.multiple.ifCheck = $scope.count;
         /*   alert($scope.count);*/
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(multiStatus);

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
            var multiStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
            multiStatus.multiple.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(multiStatus);

        } else {
            $scope.red = "#FF6347";
            $scope.count = true;
            $rootScope.counter = $rootScope.counter + 1;
            var multiStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
            multiStatus.multiple.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(multiStatus);

        }

    }

});

demo0.controller('skiptb3', function($scope, $http, $window, $state, $stateParams, $rootScope) {
    //判断题

    $scope.option = {};



    if ($stateParams.type == 3) {
        // alert('cp面板3'+$scope.active);
        $scope.nid = $stateParams.num * 1;
        $rootScope.index = $stateParams.active * 1; //面板切换
        //检查某题
        $http.get('/EMS/exam/toTopic', {
            /* $http.get('judg.json', {*/
            params: { typeId: 2, token: $window.sessionStorage.token, id: $stateParams.num }
        }).success(function(data, status, headers, config) {
            // 试题
            $scope.totalItems = data.judgeNum;
            $scope.currentPage = $scope.nid;

            $scope.content = data.content;
            $scope.lists = data.choiceList;
            var length = $scope.lists.length;
            for (var i = 0; i < length; i++) {
                $scope.lists[i].alp = String.fromCharCode(i + 65);
            }
            if (data.audio) {
                $scope.audiohide = "block";
                $scope.audio = data.audio;
            } else {
                $scope.audiohide = "none";
            }
            if (data.img) {
                $scope.imghide = "block";
                $scope.img = data.img;

            } else {
                $scope.imghide = "none";
            }
            if (data.video) {
                $scope.videohide = "block";
                $scope.video = data.video;

            } else {
                $scope.videohide = "none";
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

            var judgStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近判断题以及状态
            judgStatus.judgment.nid = $scope.nid;
            judgStatus.judgment.content = $scope.content;
            judgStatus.judgment.choiceList = $scope.lists;
            if ($scope.audiohide == "block") {
                judgStatus.judgment.audio = $scope.sudio;
                judgStatus.judgment.audiohide = $scope.sudiohide;
            } else {
                judgStatus.judgment.audiohide = $scope.sudiohide;
            }
            if ($scope.imghide == "block") {
                judgStatus.judgment.img = $scope.img;
                judgStatus.judgment.imghide = $scope.imghide;
            } else {
                judgStatus.judgment.imghide = $scope.imghide;
            }
            if ($scope.videohide == "block") {
                judgStatus.judgment.video = $scope.video;
                judgStatus.judgment.videohide = $scope.videohide;
            } else {
                judgStatus.judgment.videohide = $scope.videohide;
            }

            judgStatus.judgment.option = $scope.option.optionsRadios;
            judgStatus.judgment.ifCheck = $scope.count;


            $window.sessionStorage.problemStatus = JSON.stringify(judgStatus);

        }).error(function(data, status, headers, config) {
            //处理错误  
            alert('tab1出错！');
        });

    } else {
        var judgStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        if (judgStatus.judgment.id) {
            $scope.totalItems = judgStatus.judgment.judgeNum;
            $scope.currentPage = judgStatus.judgment.nid;

            $scope.nid = judgStatus.judgment.nid;
            $scope.content = judgStatus.judgment.content;
            $scope.lists = judgStatus.judgment.choiceList;
            $rootScope.totalItems = judgStatus.judgment.totalItems;
            if (judgStatus.judgment.audiohide == "block") {
                $scope.audiohide = "block";
                $scope.audio = judgStatus.judgment.audio;
            } else {
                $scope.audiohide = "none";
                // alert("隐藏");

            }
            if (judgStatus.judgment.imghide == "block") {
                $scope.imghide = "block";
                $scope.img = judgStatus.judgment.img;

            } else {
                $scope.imghide = "none";
                //alert("隐藏");

            }
            if (judgStatus.judgment.videohide == "block") {
                $scope.videohide = "block";
                $scope.video = judgStatus.judgment.video;

            } else {
                $scope.videohide = "none";
                //alert("隐藏");

            }

            //status
            $scope.option.optionsRadios = judgStatus.judgment.option;
            if (judgStatus.judgment.ifCheck) {
                $scope.red = "#FF6347";
                $scope.count = true;
            } else {
                $scope.red = "#000000";
                $scope.count = false;

            }


        } else {
            //初始化试题
            $http.get('/EMS/exam/start', {
                /* $http.get('judg.json', {*/
                params: { typeId: 2, token: $window.sessionStorage.token }
            }).success(function(data, status, headers, config) {
                //试题
                $scope.totalItems = data.judgeNum;
                $scope.currentPage = 1;

                $scope.nid = 1;
                $scope.content = data.content;
                $scope.lists = data.choiceList;
                var length = $scope.lists.length;
                for (var i = 0; i < length; i++) {
                    $scope.lists[i].alp = String.fromCharCode(i + 65);
                }
                if (data.audio) {
                    $scope.audiohide = "block";
                    $scope.audio = data.audio;
                } else {
                    $scope.audiohide = "none";
                }
                if (data.img) {
                    $scope.imghide = "block";
                    $scope.img = data.img;

                } else {
                    $scope.imghide = "none";
                }
                if (data.video) {
                    $scope.videohide = "block";
                    $scope.video = data.video;

                } else {
                    $scope.videohide = "none";
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
                var judgStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近判断题以及状态
                judgStatus.judgment.nid = $scope.nid;
                judgStatus.judgment.content = $scope.content;
                judgStatus.judgment.choiceList = $scope.lists;
                if ($scope.audiohide == "block") {
                    judgStatus.judgment.audio = $scope.sudio;
                    judgStatus.judgment.audiohide = $scope.sudiohide;
                } else {
                    judgStatus.judgment.audiohide = $scope.sudiohide;
                }
                if ($scope.imghide == "block") {
                    judgStatus.judgment.img = $scope.img;
                    judgStatus.judgment.imghide = $scope.imghide;
                } else {
                    judgStatus.judgment.imghide = $scope.imghide;
                }
                if ($scope.videohide == "block") {
                    judgStatus.judgment.video = $scope.video;
                    judgStatus.judgment.videohide = $scope.videohide;
                } else {
                    judgStatus.judgment.videohide = $scope.videohide;
                }

                judgStatus.judgment.option = $scope.option.optionsRadios;
                judgStatus.judgment.ifCheck = $scope.count;
                judgStatus.judgment.totalItems = $scope.totalItems;

                $window.sessionStorage.problemStatus = JSON.stringify(judgStatus);


            }).error(function(data, status, headers, config) {
                //处理错误  
                alert('用户不存在或密码错误！');
            });


        }
    }

    $scope.opChanged = function(option) {
        var judgStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        judgStatus.judgment.option = option.optionsRadios;
        $window.sessionStorage.problemStatus = JSON.stringify(judgStatus);
    }



    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    $scope.itemsPerPage = 1;
    $scope.maxSize = 5;

    $scope.pageChanged = function(option) {
        /*  alert($scope.currentPage);*/

        var isChecked = $scope.count;
        $http.get('/EMS/exam/getTopic', {
            /*$http.get('judg0.json', {*/
            params: { token: $window.sessionStorage.token, typeId: 2, id: $scope.nid, requestId: $scope.currentPage, choiceId: option.optionsRadios, ifCheck: isChecked }
        }).success(function(data, status, headers, config) {
            // 试题
            $scope.totalItems = data.judgeNum;

            $scope.nid = $scope.currentPage;
            $scope.content = data.content;
            $scope.lists = data.choiceList;
            var length = $scope.lists.length;
            for (var i = 0; i < length; i++) {
                $scope.lists[i].alp = String.fromCharCode(i + 65);
            }
            if (data.audio) {
                $scope.audiohide = "block";
                $scope.audio = data.audio;
            } else {
                $scope.audiohide = "none";
            }
            if (data.img) {
                $scope.imghide = "block";
                $scope.img = data.img;

            } else {
                $scope.imghide = "none";
            }
            if (data.video) {
                $scope.videohide = "block";
                $scope.video = data.video;

            } else {
                $scope.videohide = "none";
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
            var judgStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近判断题以及状态
            judgStatus.judgment.nid = $scope.nid;
            judgStatus.judgment.content = $scope.content;
            judgStatus.judgment.choiceList = $scope.lists;
            if ($scope.audiohide == "block") {
                judgStatus.judgment.audio = $scope.sudio;
                judgStatus.judgment.audiohide = $scope.sudiohide;
            } else {
                judgStatus.judgment.audiohide = $scope.sudiohide;
            }
            if ($scope.imghide == "block") {
                judgStatus.judgment.img = $scope.img;
                judgStatus.judgment.imghide = $scope.imghide;
            } else {
                judgStatus.judgment.imghide = $scope.imghide;
            }
            if ($scope.videohide == "block") {
                judgStatus.judgment.video = $scope.video;
                judgStatus.judgment.videohide = $scope.videohide;
            } else {
                judgStatus.judgment.videohide = $scope.videohide;
            }

            judgStatus.judgment.option = $scope.option.optionsRadios;
            judgStatus.judgment.ifCheck = $scope.count;
            $window.sessionStorage.problemStatus = JSON.stringify(judgStatus);


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

            var judgStatus = JSON.parse($window.sessionStorage.problemStatus);
            judgStatus.judgment.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(judgStatus);

        } else {
            $scope.red = "#FF6347";
            $scope.count = true;
            $rootScope.counter = $rootScope.counter + 1;

            var judgStatus = JSON.parse($window.sessionStorage.problemStatus);
            judgStatus.judgment.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(judgStatus);

        }

    }

});


demo0.controller('skiptb4', function($scope, $http, $window, $state, $stateParams, $rootScope) {
    //匹配题
    $scope.option = {};


    if ($stateParams.type == 4) {
        // alert('cp面板4'+$scope.active);
        $scope.nid = $stateParams.num * 1;
        $rootScope.index = $stateParams.active * 1; //面板切换
        //检查某题
        $http.get('/EMS/exam/toTopic', {
            /*  $http.get('match.json', {*/
            params: { typeId: 3, token: $window.sessionStorage.token, id: $stateParams.num }
        }).success(function(data, status, headers, config) {
            // 试题
            $scope.totalItems = data.matchNum;
            $scope.currentPage = $scope.nid;

            $scope.contentlists = data.contentList;
            $scope.lists = data.choiceList;
            var length = $scope.lists.length;
            for (var i = 0; i < length; i++) {
                $scope.lists[i].alp = String.fromCharCode(i + 65);
            }
            if (data.audio) {
                $scope.audiohide = "block";
                $scope.audio = data.audio;
            } else {
                $scope.audiohide = "none";
            }
            if (data.img) {
                $scope.imghide = "block";
                $scope.img = data.img;

            } else {
                $scope.imghide = "none";
            }
            if (data.video) {
                $scope.videohide = "block";
                $scope.video = data.video;

            } else {
                $scope.videohide = "none";
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
            var matchStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近匹配题以及状态
            matchStatus.match.nid = $scope.nid;
            matchStatus.match.contentList = $scope.contentlists;
            matchStatus.match.choiceList = $scope.lists;
            if ($scope.audiohide == "block") {
                matchStatus.match.audio = $scope.sudio;
                matchStatus.match.audiohide = $scope.sudiohide;
            } else {
                matchStatus.match.audiohide = $scope.sudiohide;
            }
            if ($scope.imghide == "block") {
                matchStatus.match.img = $scope.img;
                matchStatus.match.imghide = $scope.imghide;
            } else {
                matchStatus.match.imghide = $scope.imghide;
            }
            if ($scope.videohide == "block") {
                matchStatus.match.video = $scope.video;
                matchStatus.match.videohide = $scope.videohide;
            } else {
                matchStatus.match.videohide = $scope.videohide;
            }


            matchStatus.match.choiceIdMap = $scope.option;
            matchStatus.match.ifCheck = $scope.count;
            matchStatus.match.totalItems = $scope.totalItems;

            $window.sessionStorage.problemStatus = JSON.stringify(matchStatus);

        }).error(function(data, status, headers, config) {
            //处理错误  
            alert('tab1出错！');
        });

    } else {

        var matchStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        if (matchStatus.match.nid) {
            $scope.totalItems = matchStatus.match.totalItems;
            $scope.currentPage = matchStatus.match.nid;

            $scope.nid = matchStatus.match.nid;
            $scope.contentlists = matchStatus.match.contentList;
            $scope.lists = matchStatus.match.choiceList;

            if (matchStatus.match.audiohide == "block") {
                $scope.audiohide = "block";
                $scope.audio = matchStatus.match.audio;
            } else {
                $scope.audiohide = "none";

            }
            if (matchStatus.match.imghide == "block") {
                $scope.imghide = "block";
                $scope.img = matchStatus.match.img;

            } else {
                $scope.imghide = "none";

            }
            if (matchStatus.match.videohide == "block") {
                $scope.videohide = "block";
                $scope.video = matchStatus.match.video;

            } else {
                $scope.videohide = "none";

            }

            //status
            $scope.option = matchStatus.match.choiceIdMap;
            if (matchStatus.match.ifCheck) {
                $scope.red = "#FF6347";
                $scope.count = true;
            } else {
                $scope.red = "#000000";
                $scope.count = false;

            }
            /*  if ( matchStatus.match.id == 1) { $scope.before = true; }else{$scope.before = false; }*/

            //alert("保留tab4!")
        } else {
            //初始化试题
            $http.get('/EMS/exam/start', {
                /*$http.get('match.json', {*/
                params: { typeId: 3, token: $window.sessionStorage.token }
            }).success(function(data, status, headers, config) {
                //试题
                $scope.totalItems = data.matchNum;
                $scope.currentPage = 1;

                $scope.nid = 1;
                $scope.contentlists = data.contentList;
                //    alert(data.choiceList);
                $scope.lists = data.choiceList;
                var length = $scope.lists.length;
                for (var i = 0; i < length; i++) {
                    $scope.lists[i].alp = String.fromCharCode(i + 65);
                }
                if (data.audio) {
                    $scope.audiohide = "block";
                    $scope.audio = data.audio;
                } else {
                    $scope.audiohide = "none";
                }
                if (data.img) {
                    $scope.imghide = "block";
                    $scope.img = data.img;

                } else {
                    $scope.imghide = "none";
                }
                if (data.video) {
                    $scope.videohide = "block";
                    $scope.video = data.video;

                } else {
                    $scope.videohide = "none";
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

                /*  $scope.before = true;*/
                var matchStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近匹配题以及状态
                matchStatus.match.nid = $scope.nid;
                matchStatus.match.contentList = $scope.contentlists;
                matchStatus.match.choiceList = $scope.lists;
                if ($scope.audiohide == "block") {
                    matchStatus.match.audio = $scope.sudio;
                    matchStatus.match.audiohide = $scope.sudiohide;
                } else {
                    matchStatus.match.audiohide = $scope.sudiohide;
                }
                if ($scope.imghide == "block") {
                    matchStatus.match.img = $scope.img;
                    matchStatus.match.imghide = $scope.imghide;
                } else {
                    matchStatus.match.imghide = $scope.imghide;
                }
                if ($scope.videohide == "block") {
                    matchStatus.match.video = $scope.video;
                    matchStatus.match.videohide = $scope.videohide;
                } else {
                    matchStatus.match.videohide = $scope.videohide;
                }


                matchStatus.match.choiceIdMap = $scope.option;
                matchStatus.match.ifCheck = $scope.count;
                matchStatus.match.totalItems = $scope.totalItems;

                $window.sessionStorage.problemStatus = JSON.stringify(matchStatus);


            }).error(function(data, status, headers, config) {
                //处理错误  
                alert('用户不存在或密码错误！');
            });
        }


    }

    $scope.opChanged = function(option) {
        var matchStatus = JSON.parse($window.sessionStorage.problemStatus);
        matchStatus.match.choiceIdMap = option;
        $window.sessionStorage.problemStatus = JSON.stringify(matchStatus);
    }


    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    /* $scope.totalItems = 20;*/
    $scope.itemsPerPage = 1;

    $scope.maxSize = 5;
    $scope.pageChanged = function(option) {
        //  alert($scope.currentPage);
       /* alert($scope.option);*/
        var isChecked = $scope.count;




        $http.get('/EMS/exam/getTopic', {
            /* $http.get('match0.json', {*/
            params: { token: $window.sessionStorage.token, typeId: 3, id: $scope.nid, requestId: $scope.currentPage, choiceIdMap: option, ifCheck: isChecked }
        }).success(function(data, status, headers, config) {
            // 试题
            $scope.totalItems = data.matchNum;
            $scope.nid = $scope.currentPage;
            $scope.contentlists = data.contentList;
            $scope.lists = data.choiceList;
            var length = $scope.lists.length;
            for (var i = 0; i < length; i++) {
                $scope.lists[i].alp = String.fromCharCode(i + 65);
            }
            if (data.audio) {
                $scope.audiohide = "block";
                $scope.audio = data.audio;
            } else {
                $scope.audiohide = "none";
            }
            if (data.img) {
                $scope.imghide = "block";
                $scope.img = data.img;

            } else {
                $scope.imghide = "none";
            }
            if (data.video) {
                $scope.videohide = "block";
                $scope.video = data.video;

            } else {
                $scope.videohide = "none";
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
            /* if ($scope.id == 1) { $scope.before = true; } else { $scope.before = false; }*/
            var matchStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近匹配题以及状态
            matchStatus.match.nid = $scope.nid;
            matchStatus.match.contentList = $scope.contentlists;
            matchStatus.match.choiceList = $scope.lists;
            if ($scope.audiohide == "block") {
                matchStatus.match.audio = $scope.sudio;
                matchStatus.match.audiohide = $scope.sudiohide;
            } else {
                matchStatus.match.audiohide = $scope.sudiohide;
            }
            if ($scope.imghide == "block") {
                matchStatus.match.img = $scope.img;
                matchStatus.match.imghide = $scope.imghide;
            } else {
                matchStatus.match.imghide = $scope.imghide;
            }
            if ($scope.videohide == "block") {
                matchStatus.match.video = $scope.video;
                matchStatus.match.videohide = $scope.videohide;
            } else {
                matchStatus.match.videohide = $scope.videohide;
            }


            matchStatus.match.choiceIdMap = $scope.option;
            matchStatus.match.ifCheck = $scope.count;
            matchStatus.match.totalItems = $scope.totalItems;

            $window.sessionStorage.problemStatus = JSON.stringify(matchStatus);


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

            var matchStatus = JSON.parse($window.sessionStorage.problemStatus);
            matchStatus.match.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(matchStatus);

        } else {
            $scope.red = "#FF6347";
            $scope.count = true;
            $rootScope.counter = $rootScope.counter + 1;

            var matchStatus = JSON.parse($window.sessionStorage.problemStatus);
            matchStatus.match.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(matchStatus);

        }

    }

});

demo0.controller('skiptb5', function($scope, $http, $window, $state, $stateParams, $rootScope) {
    //简答题

    /*$scope.option = "撰写答案";*/


    if ($stateParams.type == 5) {
        // alert('cp面板4'+$scope.active);
        $scope.nid = $stateParams.num * 1;
        $rootScope.index = $stateParams.active * 1; //面板切换
        //检查某题
        $http.get('/EMS/exam/toTopic', {
            /* $http.get('match.json', {*/
            params: { typeId: 4, token: $window.sessionStorage.token, id: $stateParams.num }
        }).success(function(data, status, headers, config) {

            $scope.totalItems = data.shortNum;
            $scope.currentPage = $scope.nid;
            // 试题
            $scope.content = data.content;
            if (data.audio) {
                $scope.audiohide = "block";
                $scope.audio = data.audio;
            } else {
                $scope.audiohide = "none";
            }
            if (data.img) {
                $scope.imghide = "block";
                $scope.img = data.img;

            } else {
                $scope.imghide = "none";
            }
            if (data.video) {
                $scope.videohide = "block";
                $scope.video = data.video;

            } else {
                $scope.videohide = "none";
            }

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
            var simpleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近简答题以及状态
            simpleStatus.simple.nid = $scope.nid;
            simpleStatus.simple.content = $scope.content;
            if ($scope.audiohide == "block") {
                simpleStatus.simple.audio = $scope.sudio;
                simpleStatus.simple.audiohide = $scope.sudiohide;
            } else {
                simpleStatus.simple.audiohide = $scope.sudiohide;
            }
            if ($scope.imghide == "block") {
                simpleStatus.simple.img = $scope.img;
                simpleStatus.simple.imghide = $scope.imghide;
            } else {
                simpleStatus.simple.imghide = $scope.imghide;
            }
            if ($scope.videohide == "block") {
                simpleStatus.simple.video = $scope.video;
                simpleStatus.simple.videohide = $scope.videohide;
            } else {
                simpleStatus.simple.videohide = $scope.videohide;
            }

            simpleStatus.simple.answer = $scope.option;
            simpleStatus.simple.ifCheck = $scope.count;
            simpleStatus.simple.totalItems = $scope.totalItems;

            $window.sessionStorage.problemStatus = JSON.stringify(simpleStatus);

        }).error(function(data, status, headers, config) {
            //处理错误  
            alert('tab1出错！');
        });

    } else {

        var simpleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        if (simpleStatus.simple.nid) {
            $scope.totalItems = simpleStatus.simple.totalItems;
            $scope.currentPage = simpleStatus.simple.nid;

            $scope.nid = simpleStatus.simple.nid;
            $scope.content = simpleStatus.simple.content;
            if (simpleStatus.simple.audiohide == "block") {
                $scope.audiohide = "block";
                $scope.audio = simpleStatus.simple.audio;
            } else {
                $scope.audiohide = "none";

            }
            if (simpleStatus.simple.imghide == "block") {
                $scope.imghide = "block";
                $scope.img = simpleStatus.simple.img;

            } else {
                $scope.imghide = "none";

            }
            if (simpleStatus.simple.videohide == "block") {
                $scope.videohide = "block";
                $scope.video = simpleStatus.simple.video;

            } else {
                $scope.videohide = "none";

            }


            //status
            $scope.answer = simpleStatus.simple.answer;
            /*$rootScope.counter = simpleStatus.simple.counter;*/
            if (simpleStatus.simple.ifCheck) {
                $scope.red = "#FF6347";
                $scope.count = true;
            } else {
                $scope.red = "#000000";
                $scope.count = false;

            }
            /*  if ( matchStatus.match.id == 1) { $scope.before = true; }else{$scope.before = false; }*/

            //alert("保留tab4!")
        } else {
            //初始化试题
            $http.get('/EMS/exam/start', {
                /* $http.get('judg.json', {*/
                params: { typeId: 4, token: $window.sessionStorage.token }
            }).success(function(data, status, headers, config) {
                $scope.totalItems = data.shortNum;
                $scope.currentPage = 1;
                //试题
                $scope.nid = 1;
                $scope.content = data.content;
                if (data.audio) {
                    $scope.audiohide = "block";
                    $scope.audio = data.audio;
                } else {
                    $scope.audiohide = "none";
                }
                if (data.img) {
                    $scope.imghide = "block";
                    $scope.img = data.img;

                } else {
                    $scope.imghide = "none";
                }
                if (data.video) {
                    $scope.videohide = "block";
                    $scope.video = data.video;

                } else {
                    $scope.videohide = "none";
                }
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

                var simpleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近简答题以及状态
                simpleStatus.simple.nid = $scope.nid;
                simpleStatus.simple.content = $scope.content;
                if ($scope.audiohide == "block") {
                    simpleStatus.simple.audio = $scope.sudio;
                    simpleStatus.simple.audiohide = $scope.sudiohide;
                } else {
                    simpleStatus.simple.audiohide = $scope.sudiohide;
                }
                if ($scope.imghide == "block") {
                    simpleStatus.simple.img = $scope.img;
                    simpleStatus.simple.imghide = $scope.imghide;
                } else {
                    simpleStatus.simple.imghide = $scope.imghide;
                }
                if ($scope.videohide == "block") {
                    simpleStatus.simple.video = $scope.video;
                    simpleStatus.simple.videohide = $scope.videohide;
                } else {
                    simpleStatus.simple.videohide = $scope.videohide;
                }

                simpleStatus.simple.answer = $scope.answer;
                simpleStatus.simple.ifCheck = $scope.count;
                simpleStatus.simple.totalItems = $scope.totalItems;

                $window.sessionStorage.problemStatus = JSON.stringify(simpleStatus);


            }).error(function(data, status, headers, config) {
                //处理错误  
                alert('用户不存在或密码错误！');
            });
        }


    }

    $scope.opChanged = function(answer) {
        var simpleStatus = JSON.parse($window.sessionStorage.problemStatus);
        simpleStatus.simple.answer = answer;
        $window.sessionStorage.problemStatus = JSON.stringify(simpleStatus);
    }

    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    $scope.itemsPerPage = 1;
    $scope.maxSize = 5;

    $scope.pageChanged = function(answer) {
        var isChecked = $scope.count;

        $http.get('/EMS/exam/getTopic', {
            /*   $http.get('judg0.json', {*/
            params: { token: $window.sessionStorage.token, typeId: 4, id: $scope.nid, requestId: $scope.currentPage, answer: answer, ifCheck: isChecked }
        }).success(function(data, status, headers, config) {
            // 试题
            $scope.totalItems = data.shortNum;

            $scope.nid = $scope.currentPage;
            $scope.content = data.content;
            if (data.audio) {
                $scope.audiohide = "block";
                $scope.audio = data.audio;
            } else {
                $scope.audiohide = "none";
            }
            if (data.img) {
                $scope.imghide = "block";
                $scope.img = data.img;

            } else {
                $scope.imghide = "none";
            }
            if (data.video) {
                $scope.videohide = "block";
                $scope.video = data.video;

            } else {
                $scope.videohide = "none";
            }


            //试题状态
            $scope.answer = data.answer;
            if (data.ifCheck) {
                $scope.red = "#FF6347";
                $scope.count = true;
            } else {
                $scope.red = "#000000";
                $scope.count = false;

            }

            var simpleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近简答题以及状态
            simpleStatus.simple.nid = $scope.nid;
            simpleStatus.simple.content = $scope.content;
            if ($scope.audiohide == "block") {
                simpleStatus.simple.audio = $scope.sudio;
                simpleStatus.simple.audiohide = $scope.sudiohide;
            } else {
                simpleStatus.simple.audiohide = $scope.sudiohide;
            }
            if ($scope.imghide == "block") {
                simpleStatus.simple.img = $scope.img;
                simpleStatus.simple.imghide = $scope.imghide;
            } else {
                simpleStatus.simple.imghide = $scope.imghide;
            }
            if ($scope.videohide == "block") {
                simpleStatus.simple.video = $scope.video;
                simpleStatus.simple.videohide = $scope.videohide;
            } else {
                simpleStatus.simple.videohide = $scope.videohide;
            }

            simpleStatus.simple.answer = $scope.answer;
            simpleStatus.simple.ifCheck = $scope.count;
            simpleStatus.simple.totalItems = $scope.totalItems;

            $window.sessionStorage.problemStatus = JSON.stringify(simpleStatus);

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

            var simpleStatus = JSON.parse($window.sessionStorage.problemStatus);
            simpleStatus.simple.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(simpleStatus);

        } else {
            $scope.red = "#FF6347";
            $scope.count = true;
            $rootScope.counter = $rootScope.counter + 1;

            var simpleStatus = JSON.parse($window.sessionStorage.problemStatus);
            simpleStatus.simple.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(simpleStatus);

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
checkup.controller("Ctab1", function($scope, $http, $state, $window, $rootScope) {

    $http.get('/EMS/exam/check', {
        params: { token: $window.sessionStorage.token, typeId: 0 }
    }).success(function(data, status, headers, config) {
        // alert(data.checkList[0].status);
    	/*var lists=[];
    	for (var i = 0; i < 100; i++) {
    	    lists[i]=0;
    	}
    	$scope.lists=lists;*/
        $scope.lists = data.checkList;
        $rootScope.topicNum = data.topicNum;
        $rootScope.finishNum = data.finishNum;
        $rootScope.otherNum = data.topicNum - data.finishNum;

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
        /*    alert(listnum);*/
        $state.go('main', { active: 4, num: listnum, type: 5 });

    }


});
