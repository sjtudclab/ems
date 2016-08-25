

var formlogin = angular.module('formlogin', [])


formlogin.controller('httpCtrl', function ($rootScope, $scope, $state, $http, $window) {



    $scope.receive = function (name, password) {
        // alert(name + password);

        $http.get('/EMS/greeting', {
            params: { name: name, password: password }
        }).success(function (data, status, headers, config) {
            //加载成功之后做一些事  
            //  alert(data.name);
            // alert(data.checklist[0]);

            // alert(data.Rid);
            // $state.go('examImport');
            //   $state.go('examManage');
            switch (data.Rid) {
                case 0: //考生登录
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
                    infoStatus.photo = data.photo;
                    infoStatus.Rid = data.Rid;
                    $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                    $state.go('showinfo');
                    break;
                case 1:  //监考员登录
                    $window.sessionStorage.token = data.token;
                    var infoStatus = {};
                    $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                    var infoStatus = JSON.parse($window.sessionStorage.infoStatus);
                    //功能列表
                    infoStatus.authorityList = data.authorityList;
                    // 考场号
                    infoStatus.roomId = data.roomId;
                    // 登陆用户id
                    infoStatus.Rid = data.Rid;
                    $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                    $state.go('supervisor');
                    break;
                case 2:  //管理员登录
                    $window.sessionStorage.stoken = data.token;
                    var adminStatus = {};
                    $window.sessionStorage.adminStatus = JSON.stringify(adminStatus);
                    var adminStatus = JSON.parse($window.sessionStorage.adminStatus);
                    //功能列表
                    adminStatus.authorityList = data.authorityList;
                    // 登陆用户id
                    adminStatus.Rid = data.Rid;
                    $window.sessionStorage.adminStatus = JSON.stringify(adminStatus);
                    $state.go('manager');
                    break;
                default:
                    alert(data.detail);
            }

            /*  if (data.Rid == 0) {
                 
              } else {
                 
              }*/


        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
    };
});

formlogin.controller('showCtrl', function ($scope, $state, $stateParams, $window) {
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
    problemStatus.fillgap = {};
    problemStatus.machine = {};
    $window.sessionStorage.problemStatus = JSON.stringify(problemStatus);

    var infoStatus = JSON.parse($window.sessionStorage.infoStatus);
    $scope.photo = infoStatus.photo;
    $scope.name = infoStatus.name;
    $scope.gender = infoStatus.gender;
    $scope.id = infoStatus.id;
    $scope.cid = infoStatus.cid;
    $scope.subject = infoStatus.subject;
    $scope.time = infoStatus.time;
    $scope.click = function () {
        $state.go('main', { active: '', num: '', type: '' });
    }



});
formlogin.controller('showinfo', function ($scope, $state, $stateParams, $window) {

    var infoStatus = JSON.parse($window.sessionStorage.infoStatus);
    $scope.photo = infoStatus.photo;
    $scope.name = infoStatus.name;
    $scope.gender = infoStatus.gender;
    $scope.id = infoStatus.id;
    $scope.cid = infoStatus.cid;
    $scope.subject = infoStatus.subject;


});



var demo0 = angular.module('demo0', ['ui.bootstrap'])

demo0.controller('finish', function ($scope, $state, $stateParams, $window) {
    if ($stateParams.score) {
        $scope.score = "考试成绩为：" + $stateParams.score;
    }

});


demo0.controller('TabsDemoCtrl', function ($scope, $rootScope) {
    $scope.problemMetaInfo = ['单选题', '多选题', '判断题', '匹配题', '简答题', '填空题', '上机题'];
    $scope.active = [];
    $scope.display = [];
    $scope.color = [];
    $rootScope.index = 0;
    for (x in $scope.problemMetaInfo) {
        $scope.active[x] = "";
        $scope.display[x] = 'none';
        $scope.color[x] = "white";
    };
    $scope.$watch('index', function (newValue, oldValue) {

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
            case 5:
                $scope.color[$rootScope.index] = "rgba(242,222,222,.8)";
                break;
            case 6:
                $scope.color[$rootScope.index] = "rgba(102,186,183,.8)";
                break;
            default:
                alert('error');
            /*   case 0:
        $scope.color[$scope.index] = "#d9edf7";
        break;
    case 1:
        $scope.color[$scope.index] = "#A8D8B9";//
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

    $scope.sel = function (index) {
        $rootScope.index = index;
    };

});


demo0.controller('showMain', function ($scope, $state, $stateParams, $window, $http, $rootScope, $uibModal) {

    function refer() {
        var allStatus = JSON.parse($window.sessionStorage.problemStatus);

        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 0, id: allStatus.single.nid, requestId: 0, choiceId: allStatus.single.option, ifCheck: allStatus.single.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("单选题succeed");
            if (data.flag == false) {
                //  alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }
        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 1, id: allStatus.multiple.nid, requestId: 0, choiceIdList: allStatus.multiple.choiceIdList, ifCheck: allStatus.multiple.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("多选题succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }


        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 2, id: allStatus.judgment.nid, requestId: 0, choiceId: allStatus.judgment.option, ifCheck: allStatus.judgment.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("判断题succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 3, id: allStatus.match.nid, requestId: 0, choiceIdMap: allStatus.match.choiceIdMap, ifCheck: allStatus.match.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("匹配提succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }


        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 4, id: allStatus.simple.nid, requestId: 0, answer: allStatus.simple.answer, ifCheck: allStatus.simple.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("简答题succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }
        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 5, id: allStatus.fillgap.nid, requestId: 0, answerList: allStatus.fillgap.option, ifCheck: allStatus.fillgap.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("填空题succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 6, id: allStatus.machine.nid, requestId: 0, ifCheck: allStatus.machine.ifCheck, fileName: allStatus.machine.fileName }
        }).success(function (data, status, headers, config) {
            // alert("上机题succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });


    }

    $scope.submit = function () {
        refer();
    }

    $scope.submit_confirm = function () {

        refer();
        var modalParam = {
            backdrop: 'static',
            size: 'sm'
        };
        var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
        var headerBottom = '</h3></div>';
        var footer =
            '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button><button class="btn btn-warning" type="button" ng-click="$parent.confirm=false;$close()">取消</button></div>';
        modalParam.template = headerTop + '1' + headerBottom + '<div class="modal-body"><p style="font-size:150%">确认交卷？</p></div>' + footer;
        $uibModal.open(modalParam).result.then(function () {
            if ($scope.confirm) {
                modalParam.template = headerTop + '2' + headerBottom + '<div class="modal-body"><p style="font-size:150%">确认交卷？</p></div>' + footer;
                $uibModal.open(modalParam).result.then(function () {
                    if ($scope.confirm) {
                        modalParam.template = headerTop + '3' + headerBottom + '<div class="modal-body"><p style="font-size:150%">确认交卷？</p></div>' + footer;
                        $uibModal.open(modalParam).result.then(function () {
                            if ($scope.confirm) {
                                $http.get('/EMS/exam/handExam', {
                                    params: { token: $window.sessionStorage.token }
                                }).success(function (data, status, headers, config) {
                                    /*alert("简答题succeed");*/

                                    if (data.flag == false) {
                                        alert(data.detail);
                                        if(data.detail=="还没到交卷时间"){}else{
                                         $state.go("finish", { score: data });
                                        }
                                      
                                    } else {

                                        $state.go("finish", { score: data });

                                    }

                                }).error(function (data, status, headers, config) {
                                    //处理错误  
                                    alert('服务器拒绝访问');
                                });

                                /* alert('您已提交');*/
                            }
                        });
                    }
                });
            }
        });
    }


});



demo0.controller("timeinfo", function ($scope, $interval, $window, $http, $state, $rootScope) {

    function refer() {
        var allStatus = JSON.parse($window.sessionStorage.problemStatus);

        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 0, id: allStatus.single.nid, requestId: 0, choiceId: allStatus.single.option, ifCheck: allStatus.single.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("单选题succeed");
            if (data.flag == false) {
                //  alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }
        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 1, id: allStatus.multiple.nid, requestId: 0, choiceIdList: allStatus.multiple.choiceIdList, ifCheck: allStatus.multiple.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("多选题succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }


        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 2, id: allStatus.judgment.nid, requestId: 0, choiceId: allStatus.judgment.option, ifCheck: allStatus.judgment.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("判断题succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 3, id: allStatus.match.nid, requestId: 0, choiceIdMap: allStatus.match.choiceIdMap, ifCheck: allStatus.match.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("匹配提succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }


        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 4, id: allStatus.simple.nid, requestId: 0, answer: allStatus.simple.answer, ifCheck: allStatus.simple.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("简答题succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }
        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 5, id: allStatus.fillgap.nid, requestId: 0, answerList: allStatus.fillgap.option, ifCheck: allStatus.fillgap.ifCheck }
        }).success(function (data, status, headers, config) {
            // alert("填空题succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
        $http.get('/EMS/exam/getTopic', {
            params: { token: $window.sessionStorage.token, typeId: 6, id: allStatus.machine.nid, requestId: 0, ifCheck: allStatus.machine.ifCheck, fileName: allStatus.machine.fileName }
        }).success(function (data, status, headers, config) {
            // alert("上机题succeed");
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    // alert(data);
                    $state.go("finish", { score: data });
                });
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });


    }

    var second,
        timePromise = undefined;
    $http.get('/EMS/exam/getTime', {
        params: { token: $window.sessionStorage.token }
    }).success(function (data, status, headers, config) {
        if (data.flag == false) {
            // alert(data.detail);
            $http.get('/EMS/exam/handExam', {
                params: { token: $window.sessionStorage.token }
            }).success(function (data, status, headers, config) {
                $state.go("finish", { score: data });
            });
        } else {
            second = data;
            timeRequest();
        }
    });

    var timingPromise = undefined;
    timingPromse = $interval(function () {
        $http.get('/EMS/exam/getTime', {
            params: { token: $window.sessionStorage.token }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                // refer();
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
                second = data;
                // timeRequest();
            }
        });

    }, 30000);



    function timeRequest() {
        // $http.get('/EMS/exam/getTime', {
        //     params: { token: $window.sessionStorage.token }
        // }).success(function (data, status, headers, config) {
        //     if (data.flag == false) {
        //         alert(data.detail);
        //     }
        //     second = data;
        timePromise = $interval(function () {
            if (second < 0) {
                $interval.cancel(timePromise);
                timePromise = undefined;
                refer();

                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {

                    if (data.flag == false) {
                        // alert(data.detail);
                        $http.get('/EMS/exam/handExam', {
                            params: { token: $window.sessionStorage.token }
                        }).success(function (data, status, headers, config) {
                            $state.go("finish", { score: data });
                        });
                    }
                    alert("考试时间结束！");

                    $state.go("finish", { score: data });

                }).error(function (data, status, headers, config) {
                    //处理错误  
                    alert('服务器拒绝访问');
                });
                // $state.go('finish');
                /*alert("重新");*/
            } else {
                if (second == 0) {
                    $http.get('/EMS/exam/getTime', {
                        params: { token: $window.sessionStorage.token }
                    }).success(function (data, status, headers, config) {

                        if (data.flag == false) {
                            // alert(data.detail);
                            $http.get('/EMS/exam/handExam', {
                                params: { token: $window.sessionStorage.token }
                            }).success(function (data, status, headers, config) {
                                $state.go("finish", { score: data });
                            });
                        } else {
                            second = data;
                        }
                    })
                }
                hour = Math.floor(second / 3600);
                minute = Math.floor((second % 3600) / 60);
                miao = second % 3600 % 60;
                $scope.hour = hour;
                $scope.minute = minute;
                $scope.time = miao;
                second--;

            }
        }, 1000);

        // }).error(function (data, status, headers, config) {
        //     //处理错误  
        //     alert('服务器拒绝访问');
        // });

    }

});

demo0.controller('skiptb1', function ($scope, $http, $window, $state, $stateParams, $rootScope) {
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
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
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
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
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
            }).success(function (data, status, headers, config) {
                //试题
                if (data.flag == false) {
                    // alert(data.detail);
                    $http.get('/EMS/exam/handExam', {
                        params: { token: $window.sessionStorage.token }
                    }).success(function (data, status, headers, config) {
                        $state.go("finish", { score: data });
                    });
                } else {
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
                }

            }).error(function (data, status, headers, config) {
                //处理错误  
                alert('服务器拒绝访问');
            });
        }
    }

    $scope.opChanged = function (option) {
        var singleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        singleStatus.single.option = option.optionsRadios;
        $window.sessionStorage.problemStatus = JSON.stringify(singleStatus);
    }


    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    /* $scope.totalItems = 20;*/
    $scope.itemsPerPage = 1;

    $scope.maxSize = 2;

    $scope.pageChanged = function (option) {

        var isChecked = $scope.count;
        $http.get('/EMS/exam/getTopic', {
            /*   $http.get('single0.json', {*/
            params: { token: $window.sessionStorage.token, typeId: 0, id: $scope.nid, requestId: $scope.currentPage, choiceId: option.optionsRadios, ifCheck: isChecked }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
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

            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });
    };


    //标志旗帜颜色
    $scope.chgColor = function (count) {

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

demo0.controller('skiptb2', function ($scope, $http, $window, $state, $stateParams, $rootScope) {
    //多选题

    $scope.option = [];
    $scope.isSelected = function (id) {
        return $scope.option.indexOf(id) >= 0;
    }
    $scope.updateSelection = function ($event, id) {
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
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
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
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
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
            }).success(function (data, status, headers, config) {
                if (data.flag == false) {
                    // alert(data.detail);
                    $http.get('/EMS/exam/handExam', {
                        params: { token: $window.sessionStorage.token }
                    }).success(function (data, status, headers, config) {
                        $state.go("finish", { score: data });
                    });
                } else {
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
                }

            }).error(function (data, status, headers, config) {
                //处理错误  
                alert('服务器拒绝访问');
            });

        }
    }

    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    $scope.itemsPerPage = 1;
    $scope.maxSize = 5;

    $scope.pageChanged = function (option) {
        //alert($scope.currentPage);
        var isChecked = $scope.count;

        $http.get('/EMS/exam/getTopic', {
            /*   $http.get('multiple.json', {*/
            params: { token: $window.sessionStorage.token, typeId: 1, id: $scope.nid, requestId: $scope.currentPage, choiceIdList: option, ifCheck: isChecked }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
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
            }
        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });

    };


    //标志旗帜颜色
    $scope.chgColor = function (count) {


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

demo0.controller('skiptb3', function ($scope, $http, $window, $state, $stateParams, $rootScope) {
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
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
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
            }
        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });

    } else {
        var judgStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        if (judgStatus.judgment.nid) {
            $scope.totalItems = judgStatus.judgment.totalItems;
            $scope.currentPage = judgStatus.judgment.nid;

            $scope.nid = judgStatus.judgment.nid;
            $scope.content = judgStatus.judgment.content;
            $scope.lists = judgStatus.judgment.choiceList;
            // $rootScope.totalItems = judgStatus.judgment.totalItems;
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
            }).success(function (data, status, headers, config) {
                if (data.flag == false) {
                    // alert(data.detail);
                    $http.get('/EMS/exam/handExam', {
                        params: { token: $window.sessionStorage.token }
                    }).success(function (data, status, headers, config) {
                        $state.go("finish", { score: data });
                    });
                } else {
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
                }

            }).error(function (data, status, headers, config) {
                //处理错误  
                alert('服务器拒绝访问');
            });


        }
    }

    $scope.opChanged = function (option) {
        var judgStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        judgStatus.judgment.option = option.optionsRadios;
        $window.sessionStorage.problemStatus = JSON.stringify(judgStatus);
    }



    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    $scope.itemsPerPage = 1;
    $scope.maxSize = 5;

    $scope.pageChanged = function (option) {
        /*  alert($scope.currentPage);*/

        var isChecked = $scope.count;
        $http.get('/EMS/exam/getTopic', {
            /*$http.get('judg0.json', {*/
            params: { token: $window.sessionStorage.token, typeId: 2, id: $scope.nid, requestId: $scope.currentPage, choiceId: option.optionsRadios, ifCheck: isChecked }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
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
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });

    };

    //  $rootScope.counter = 0;
    //标志旗帜颜色
    $scope.chgColor = function (count) {


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


demo0.controller('skiptb4', function ($scope, $http, $window, $state, $stateParams, $rootScope) {
    //匹配题
    /*$scope.option = [];*/


    if ($stateParams.type == 4) {
        // alert('cp面板4'+$scope.active);
        $scope.nid = $stateParams.num * 1;
        $rootScope.index = $stateParams.active * 1; //面板切换
        //检查某题
        $http.get('/EMS/exam/toTopic', {
            /*  $http.get('match.json', {*/
            params: { typeId: 3, token: $window.sessionStorage.token, id: $stateParams.num }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
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
            }
        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });

    } else {

        var matchStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近匹配题以及状态
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
            }).success(function (data, status, headers, config) {
                if (data.flag == false) {
                    // alert(data.detail);
                    $http.get('/EMS/exam/handExam', {
                        params: { token: $window.sessionStorage.token }
                    }).success(function (data, status, headers, config) {
                        $state.go("finish", { score: data });
                    });
                } else {
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
                }

            }).error(function (data, status, headers, config) {
                //处理错误  
                alert('服务器拒绝访问');
            });
        }


    }

    $scope.opChanged = function (option) {
        var matchStatus = JSON.parse($window.sessionStorage.problemStatus);
        matchStatus.match.choiceIdMap = option;
        $window.sessionStorage.problemStatus = JSON.stringify(matchStatus);
    }


    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    /* $scope.totalItems = 20;*/
    $scope.itemsPerPage = 1;

    $scope.maxSize = 5;
    $scope.pageChanged = function (option) {
        //  alert($scope.currentPage);
        /*  alert(option);*/
        var isChecked = $scope.count;




        $http.get('/EMS/exam/getTopic', {
            /* $http.get('match0.json', {*/
            params: { token: $window.sessionStorage.token, typeId: 3, id: $scope.nid, requestId: $scope.currentPage, choiceIdMap: option, ifCheck: isChecked }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
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
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });

    };


    //  $rootScope.counter = 0;
    //标志旗帜颜色
    $scope.chgColor = function (count) {

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

demo0.controller('skiptb5', function ($scope, $http, $window, $state, $stateParams, $rootScope) {
    //简答题

    /*$scope.option = "撰写答案";*/
    $scope.askPdf=function(){
         window.open("/EMS/exam/getPdf?token=" + $window.sessionStorage.token + "&id=" + $scope.currentPage+"&typeId=4");
    }


    if ($stateParams.type == 5) {
        // alert('cp面板4'+$scope.active);
        $scope.nid = $stateParams.num * 1;
        $rootScope.index = $stateParams.active * 1; //面板切换
        //检查某题
        $http.get('/EMS/exam/toTopic', {
            /* $http.get('match.json', {*/
            params: { typeId: 4, token: $window.sessionStorage.token, id: $stateParams.num }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
                $scope.totalItems = data.shortNum;
                $scope.currentPage = $scope.nid;
                $scope.showPdf = data.showPdf;
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
                simpleStatus.simple.showPdf = $scope.showPdf;
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
            }
        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });

    } else {

        var simpleStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        if (simpleStatus.simple.nid) {
            $scope.totalItems = simpleStatus.simple.totalItems;
            $scope.currentPage = simpleStatus.simple.nid;

            $scope.nid = simpleStatus.simple.nid;
            $scope.content = simpleStatus.simple.content;
            $scope.showPdf=simpleStatus.simple.showPdf;
           
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
            }).success(function (data, status, headers, config) {
                if (data.flag == false) {
                    // alert(data.detail);
                    $http.get('/EMS/exam/handExam', {
                        params: { token: $window.sessionStorage.token }
                    }).success(function (data, status, headers, config) {
                        $state.go("finish", { score: data });
                    });
                } else {
                    $scope.totalItems = data.shortNum;
                    $scope.currentPage = 1;
                    //是否有PDF
                    // $scope.showPdf = true;
                    $scope.showPdf = data.showPdf;
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
                    simpleStatus.simple.showPdf = $scope.showPdf;
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
                }

            }).error(function (data, status, headers, config) {
                //处理错误  
                alert('服务器拒绝访问');
            });
        }


    }
    $scope.$watch("answer", function () {
        var simpleStatus = JSON.parse($window.sessionStorage.problemStatus);
        simpleStatus.simple.answer = $scope.answer;
        $window.sessionStorage.problemStatus = JSON.stringify(simpleStatus);

    });
    // $scope.opChanged = function (answer) {
    //     var simpleStatus = JSON.parse($window.sessionStorage.problemStatus);
    //     simpleStatus.simple.answer = answer;
    //     $window.sessionStorage.problemStatus = JSON.stringify(simpleStatus);
    // }

    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    $scope.itemsPerPage = 1;
    $scope.maxSize = 5;

    $scope.pageChanged = function (answer) {
        var isChecked = $scope.count;

        $http.get('/EMS/exam/getTopic', {
            /*   $http.get('judg0.json', {*/
            params: { token: $window.sessionStorage.token, typeId: 4, id: $scope.nid, requestId: $scope.currentPage, answer: answer, ifCheck: isChecked }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
                // 试题
                $scope.totalItems = data.shortNum;

                $scope.nid = $scope.currentPage;
                $scope.content = data.content;

                $scope.showPdf = data.showPdf;
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
                simpleStatus.simple.showPdf = $scope.showPdf;
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
            }
        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });

    };

    //标志旗帜颜色
    $scope.chgColor = function (count) {


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
demo0.controller('skiptb6', function ($scope, $http, $window, $state, $stateParams, $rootScope) {
    //填空题

    // $scope.showPdf = true;
    $scope.askPdf=function(){
         window.open("/EMS/exam/getPdf?token=" + $window.sessionStorage.token + "&id=" + $scope.currentPage+"&typeId=5");
    }

    if ($stateParams.type == 6) {
        $scope.nid = $stateParams.num * 1;
        $rootScope.index = $stateParams.active * 1; //面板切换
        //检查某题
        $http.get('/EMS/exam/toTopic', {
            // $http.get('fillgap.json', {
            params: { typeId: 5, token: $window.sessionStorage.token, id: $stateParams.num }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
                // 试题
                $scope.totalItems = data.gapNum;
                $scope.currentPage = $scope.nid;

                $scope.content = data.content;
                $scope.fillNum = data.fillNum;
                $scope.fillLists = Array(data.fillNum + 1).join('a').split('');

                $scope.showPdf = data.showPdf;

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

                $scope.option = data.answerList;
                if (data.ifCheck) {
                    $scope.red = "#FF6347";
                    $scope.count = true;
                } else {
                    $scope.red = "#000000";
                    $scope.count = false;

                }

                var gapStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近判断题以及状态
                gapStatus.fillgap.nid = $scope.nid;
                gapStatus.fillgap.content = $scope.content;
                gapStatus.fillgap.fillLists = $scope.fillLists;
                gapStatus.fillgap.showPdf = $scope.showPdf;
                if ($scope.audiohide == "block") {
                    gapStatus.fillgap.audio = $scope.sudio;
                    gapStatus.fillgap.audiohide = $scope.sudiohide;
                } else {
                    gapStatus.fillgap.audiohide = $scope.sudiohide;
                }
                if ($scope.imghide == "block") {
                    gapStatus.fillgap.img = $scope.img;
                    gapStatus.fillgap.imghide = $scope.imghide;
                } else {
                    gapStatus.fillgap.imghide = $scope.imghide;
                }
                if ($scope.videohide == "block") {
                    gapStatus.fillgap.video = $scope.video;
                    gapStatus.fillgap.videohide = $scope.videohide;
                } else {
                    gapStatus.fillgap.videohide = $scope.videohide;
                }

                gapStatus.fillgap.option = $scope.option;
                gapStatus.fillgap.ifCheck = $scope.count;


                $window.sessionStorage.problemStatus = JSON.stringify(gapStatus);
            }
        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });

    } else {
        var gapStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        if (gapStatus.fillgap.nid) {
            $scope.totalItems = gapStatus.fillgap.gapNum;
            $scope.currentPage = gapStatus.fillgap.nid;

            $scope.nid = gapStatus.fillgap.nid;
            $scope.content = gapStatus.fillgap.content;
            $scope.fillLists = gapStatus.fillgap.fillLists;
            $scope.showPdf=gapStatus.fillgap.showPdf;
            
            // $rootScope.totalItems = gapStatus.fillgap.totalItems;
            if (gapStatus.fillgap.audiohide == "block") {
                $scope.audiohide = "block";
                $scope.audio = gapStatus.fillgap.audio;
            } else {
                $scope.audiohide = "none";
                // alert("隐藏");

            }
            if (gapStatus.fillgap.imghide == "block") {
                $scope.imghide = "block";
                $scope.img = gapStatus.fillgap.img;

            } else {
                $scope.imghide = "none";
                //alert("隐藏");

            }
            if (gapStatus.fillgap.videohide == "block") {
                $scope.videohide = "block";
                $scope.video = gapStatus.fillgap.video;

            } else {
                $scope.videohide = "none";
                //alert("隐藏");

            }

            //status
            $scope.option = gapStatus.fillgap.option;
            if (gapStatus.fillgap.ifCheck) {
                $scope.red = "#FF6347";
                $scope.count = true;
            } else {
                $scope.red = "#000000";
                $scope.count = false;

            }


        } else {
            //初始化试题
            $http.get('/EMS/exam/start', {
                // $http.get('fillgap.json', {
                params: { typeId: 5, token: $window.sessionStorage.token }
            }).success(function (data, status, headers, config) {
                if (data.flag == false) {
                    // alert(data.detail);
                    $http.get('/EMS/exam/handExam', {
                        params: { token: $window.sessionStorage.token }
                    }).success(function (data, status, headers, config) {
                        $state.go("finish", { score: data });
                    });
                } else {
                    //试题

                    $scope.totalItems = data.gapNum;
                    $scope.currentPage = 1;

                    $scope.nid = 1;
                    $scope.content = data.content;
                    $scope.fillNum = data.fillNum;
                    $scope.fillLists = Array(data.fillNum + 1).join('a').split('');

                     $scope.showPdf=data.showPdf;

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
                    $scope.option = [];
                    // $scope.option = data.answerList;
                    if (data.ifCheck) {
                        $scope.red = "#FF6347";
                        $scope.count = true;
                    } else {
                        $scope.red = "#000000";
                        $scope.count = false;

                    }

                    /*  $scope.before = true;*/
                    var gapStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近判断题以及状态
                    gapStatus.fillgap.nid = $scope.nid;
                    gapStatus.fillgap.content = $scope.content;
                    gapStatus.fillgap.fillLists = $scope.fillLists;
                    gapStatus.fillgap.showPdf = $scope.showPdf;
                    if ($scope.audiohide == "block") {
                        gapStatus.fillgap.audio = $scope.sudio;
                        gapStatus.fillgap.audiohide = $scope.sudiohide;
                    } else {
                        gapStatus.fillgap.audiohide = $scope.sudiohide;
                    }
                    if ($scope.imghide == "block") {
                        gapStatus.fillgap.img = $scope.img;
                        gapStatus.fillgap.imghide = $scope.imghide;
                    } else {
                        gapStatus.fillgap.imghide = $scope.imghide;
                    }
                    if ($scope.videohide == "block") {
                        gapStatus.fillgap.video = $scope.video;
                        gapStatus.fillgap.videohide = $scope.videohide;
                    } else {
                        gapStatus.fillgap.videohide = $scope.videohide;
                    }

                    gapStatus.fillgap.option = $scope.option;
                    gapStatus.fillgap.ifCheck = $scope.count;
                    gapStatus.fillgap.gapNum = $scope.totalItems;

                    $window.sessionStorage.problemStatus = JSON.stringify(gapStatus);
                }

            }).error(function (data, status, headers, config) {
                //处理错误  
                alert('服务器拒绝访问');
            });


        }
    }

    // $scope.opChanged = function (option,index) {
    //     var gapStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
    //     gapStatus.fillgap.option[index] = option;
    //     $window.sessionStorage.problemStatus = JSON.stringify(gapStatus);
    // }
    $scope.$watch("option", function () {
        // alert("ff");
        var gapStatus = JSON.parse($window.sessionStorage.problemStatus);
        gapStatus.fillgap.option = $scope.option;
        $window.sessionStorage.problemStatus = JSON.stringify(gapStatus);

    }, true);

    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    $scope.itemsPerPage = 1;
    $scope.maxSize = 5;

    $scope.pageChanged = function (option) {
        /*  alert($scope.currentPage);*/
        // alert(option);

        var isChecked = $scope.count;
        $http.get('/EMS/exam/getTopic', {
            // $http.get('fillgap0.json', {
            params: { token: $window.sessionStorage.token, typeId: 5, id: $scope.nid, requestId: $scope.currentPage, answerList: option, ifCheck: isChecked }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
                // 试题
                $scope.totalItems = data.gapNum;

                $scope.nid = $scope.currentPage;
                $scope.content = data.content;
                $scope.fillNum = data.fillNum;
                $scope.fillLists = Array(data.fillNum + 1).join('a').split('');

                $scope.showPdf=data.showPdf;

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
                $scope.option = data.answerList;
                if (data.ifCheck) {
                    $scope.red = "#FF6347";
                    $scope.count = true;
                } else {
                    $scope.red = "#000000";
                    $scope.count = false;

                }
                /* if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }*/
                var gapStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近判断题以及状态
                gapStatus.fillgap.nid = $scope.nid;
                gapStatus.fillgap.content = $scope.content;
                gapStatus.fillgap.fillLists = $scope.fillLists;
                gapStatus.fillgap.showPdf = $scope.showPdf;
                if ($scope.audiohide == "block") {
                    gapStatus.fillgap.audio = $scope.sudio;
                    gapStatus.fillgap.audiohide = $scope.sudiohide;
                } else {
                    gapStatus.fillgap.audiohide = $scope.sudiohide;
                }
                if ($scope.imghide == "block") {
                    gapStatus.fillgap.img = $scope.img;
                    gapStatus.fillgap.imghide = $scope.imghide;
                } else {
                    gapStatus.fillgap.imghide = $scope.imghide;
                }
                if ($scope.videohide == "block") {
                    gapStatus.fillgap.video = $scope.video;
                    gapStatus.fillgap.videohide = $scope.videohide;
                } else {
                    gapStatus.fillgap.videohide = $scope.videohide;
                }

                gapStatus.fillgap.option = $scope.option;
                gapStatus.fillgap.ifCheck = $scope.count;
                $window.sessionStorage.problemStatus = JSON.stringify(gapStatus);
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });

    };

    //  $rootScope.counter = 0;
    //标志旗帜颜色
    $scope.chgColor = function (count) {


        if (count) {
            $scope.red = "#000000";
            $scope.count = false;
            $rootScope.counter = $rootScope.counter - 1;

            var gapStatus = JSON.parse($window.sessionStorage.problemStatus);
            gapStatus.fillgap.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(gapStatus);

        } else {
            $scope.red = "#FF6347";
            $scope.count = true;
            $rootScope.counter = $rootScope.counter + 1;

            var gapStatus = JSON.parse($window.sessionStorage.problemStatus);
            gapStatus.fillgap.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(gapStatus);

        }

    }

});
demo0.directive('customOnChange', function () {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var onChangeFunc = scope.$eval(attrs.customOnChange);
            element.bind('change', onChangeFunc);
        }
    };
});
demo0.controller('skiptb7', function ($scope, $http, $window, $state, $stateParams, $rootScope) {
    // /$scope.showPdf = true;
    $scope.askPdf=function(){
         window.open("/EMS/exam/getPdf?token=" + $window.sessionStorage.token + "&id=" + $scope.currentPage+"&typeId=6");
    }
    //上机题
    $scope.progressPer = 0;

    $scope.selectFile = function () {
        $scope.$apply(function () {
            $scope.selectedFile = event.target.files[0];
            $scope.fileName = $scope.selectedFile.name;
        })
    }
    $scope.upload = function () {
        $scope.ngshow = true;
        var formData = new FormData();
        formData.append("file", $scope.selectedFile);
        formData.append("token", $window.sessionStorage.token);
        formData.append("id", $scope.nid);

        if ($scope.selectedFile == undefined) {
            return;
        }
        $scope.progressPer = 0;
        $http({
            method: 'POST',
            url: '/EMS/exam/machineForm',
            data: formData,
            headers: {
                'Content-Type': undefined,
            },
            uploadEventHandlers: {
                progress: function (e) {
                    $scope.progressPer = e.loaded / e.total * 100;
                    $scope.progressInfo = '上传中';
                }
            }
        }).then(function success(response) {
            $scope.progressInfo = response.data.info;
        }, function error(response) {
            alert('出现错误\n' + response.status + ' ' + response.statusText);
        });
    }



    if ($stateParams.type == 7) {
        $scope.nid = $stateParams.num * 1;
        $rootScope.index = $stateParams.active * 1; //面板切换
        $scope.ngshow = false;
        //检查某题
        $http.get('/EMS/exam/toTopic', {
            // $http.get('machine.json', {
            params: { typeId: 6, token: $window.sessionStorage.token, id: $stateParams.num }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
                // 试题
                $scope.totalItems = data.machineNum;
                $scope.currentPage = $scope.nid;

                $scope.content = data.content;
                $scope.showPdf=data.showPdf;

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
                $scope.fileName = data.fileName;

                if (data.ifCheck) {
                    $scope.red = "#FF6347";
                    $scope.count = true;
                } else {
                    $scope.red = "#000000";
                    $scope.count = false;

                }

                var machineStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近判断题以及状态
                machineStatus.machine.nid = $scope.nid;
                machineStatus.machine.content = $scope.content;
                machineStatus.machine.showPdf = $scope.showPdf;
                if ($scope.audiohide == "block") {
                    machineStatus.machine.audio = $scope.sudio;
                    machineStatus.machine.audiohide = $scope.sudiohide;
                } else {
                    machineStatus.machine.audiohide = $scope.sudiohide;
                }
                if ($scope.imghide == "block") {
                    machineStatus.machine.img = $scope.img;
                    machineStatus.machine.imghide = $scope.imghide;
                } else {
                    machineStatus.machine.imghide = $scope.imghide;
                }
                if ($scope.videohide == "block") {
                    machineStatus.machine.video = $scope.video;
                    machineStatus.machine.videohide = $scope.videohide;
                } else {
                    machineStatus.machine.videohide = $scope.videohide;
                }
                machineStatus.machine.fileName = $scope.fileName;
                machineStatus.machine.ifCheck = $scope.count;


                $window.sessionStorage.problemStatus = JSON.stringify(machineStatus);
            }
        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });

    } else {
        $scope.ngshow = false;
        var machineStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
        if (machineStatus.machine.nid) {
            $scope.totalItems = machineStatus.machine.machineNum;
            $scope.currentPage = machineStatus.machine.nid;

            $scope.nid = machineStatus.machine.nid;
            $scope.content = machineStatus.machine.content;
            $scope.fileName = machineStatus.machine.fileName;
            $scope.showPdf=machineStatus.machine.showPdf;

            // $rootScope.totalItems = gapStatus.fillgap.totalItems;
            if (machineStatus.machine.audiohide == "block") {
                $scope.audiohide = "block";
                $scope.audio = machineStatus.machine.audio;
            } else {
                $scope.audiohide = "none";
                // alert("隐藏");

            }
            if (machineStatus.machine.imghide == "block") {
                $scope.imghide = "block";
                $scope.img = machineStatus.machine.img;

            } else {
                $scope.imghide = "none";
                //alert("隐藏");

            }
            if (machineStatus.machine.videohide == "block") {
                $scope.videohide = "block";
                $scope.video = machineStatus.machine.video;

            } else {
                $scope.videohide = "none";
                //alert("隐藏");

            }

            //status

            if (machineStatus.machine.ifCheck) {
                $scope.red = "#FF6347";
                $scope.count = true;
            } else {
                $scope.red = "#000000";
                $scope.count = false;

            }


        } else {
            //初始化试题
            $http.get('/EMS/exam/start', {
                // $http.get('machine.json', {
                params: { typeId: 6, token: $window.sessionStorage.token }
            }).success(function (data, status, headers, config) {
                if (data.flag == false) {
                    // alert(data.detail);
                    $http.get('/EMS/exam/handExam', {
                        params: { token: $window.sessionStorage.token }
                    }).success(function (data, status, headers, config) {
                        $state.go("finish", { score: data });
                    });
                } else {
                    //试题
                    $scope.totalItems = data.machineNum;
                    $scope.currentPage = 1;

                    $scope.nid = 1;
                    $scope.content = data.content;
                    $scope.showPdf=data.showPdf;

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
                    $scope.fileName = data.fileName;
                    if (data.ifCheck) {
                        $scope.red = "#FF6347";
                        $scope.count = true;
                    } else {
                        $scope.red = "#000000";
                        $scope.count = false;

                    }

                    /*  $scope.before = true;*/
                    var machineStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近判断题以及状态
                    machineStatus.machine.nid = $scope.nid;
                    machineStatus.machine.content = $scope.content;
                     machineStatus.machine.showPdf=$scope.showPdf;

                    if ($scope.audiohide == "block") {
                        machineStatus.machine.audio = $scope.sudio;
                        machineStatus.machine.audiohide = $scope.sudiohide;
                    } else {
                        machineStatus.machine.audiohide = $scope.sudiohide;
                    }
                    if ($scope.imghide == "block") {
                        machineStatus.machine.img = $scope.img;
                        machineStatus.machine.imghide = $scope.imghide;
                    } else {
                        machineStatus.machine.imghide = $scope.imghide;
                    }
                    if ($scope.videohide == "block") {
                        machineStatus.machine.video = $scope.video;
                        machineStatus.machine.videohide = $scope.videohide;
                    } else {
                        machineStatus.machine.videohide = $scope.videohide;
                    }
                    machineStatus.machine.fileName = $scope.fileName;
                    machineStatus.machine.ifCheck = $scope.count;
                    machineStatus.machine.machineNum = $scope.totalItems;

                    $window.sessionStorage.problemStatus = JSON.stringify(machineStatus);
                }

            }).error(function (data, status, headers, config) {
                //处理错误  
                alert('服务器拒绝访问');
            });


        }
    }

    // $scope.opChanged = function (option) {
    //     var gapStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近单选题以及状态
    //     gapStatus.fillgap.option = option.optionsRadios;
    //     $window.sessionStorage.problemStatus = JSON.stringify(gapStatus);
    // }
    $scope.$watch("fileName", function () {
        var machineStatus = JSON.parse($window.sessionStorage.problemStatus);
        machineStatus.machine.fileName = $scope.fileName;
        $window.sessionStorage.problemStatus = JSON.stringify(machineStatus);
        // alert($scope.option);

    });


    $scope.previousText = "上一题";
    $scope.nextText = "下一题";
    $scope.itemsPerPage = 1;
    $scope.maxSize = 5;

    $scope.pageChanged = function (option) {
        /*  alert($scope.currentPage);*/
        alert(option);

        var isChecked = $scope.count;
        $http.get('/EMS/exam/getTopic', {
            // $http.get('machine.json', {
            params: { token: $window.sessionStorage.token, typeId: 6, id: $scope.nid, requestId: $scope.currentPage, ifCheck: isChecked, fileName: $scope.fileName }
        }).success(function (data, status, headers, config) {
            if (data.flag == false) {
                // alert(data.detail);
                $http.get('/EMS/exam/handExam', {
                    params: { token: $window.sessionStorage.token }
                }).success(function (data, status, headers, config) {
                    $state.go("finish", { score: data });
                });
            } else {
                $scope.ngshow = false;
                // 试题
                $scope.totalItems = data.machineNum;

                $scope.nid = $scope.currentPage;
                $scope.content = data.content;
                $scope.showPdf=data.showPdf;

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
                $scope.fileName = data.fileName;
                if (data.ifCheck) {
                    $scope.red = "#FF6347";
                    $scope.count = true;
                } else {
                    $scope.red = "#000000";
                    $scope.count = false;

                }
                /* if ( $scope.id == 1) { $scope.before = true; }else{$scope.before = false; }*/
                var machineStatus = JSON.parse($window.sessionStorage.problemStatus); //解析存储最近判断题以及状态
                machineStatus.machine.nid = $scope.nid;
                machineStatus.machine.content = $scope.content;
                machineStatus.machine.showPdf = $scope.showPdf;
                if ($scope.audiohide == "block") {
                    machineStatus.machine.audio = $scope.sudio;
                    machineStatus.machine.audiohide = $scope.sudiohide;
                } else {
                    machineStatus.machine.audiohide = $scope.sudiohide;
                }
                if ($scope.imghide == "block") {
                    machineStatus.machine.img = $scope.img;
                    machineStatus.machine.imghide = $scope.imghide;
                } else {
                    machineStatus.machine.imghide = $scope.imghide;
                }
                if ($scope.videohide == "block") {
                    machineStatus.machine.video = $scope.video;
                    machineStatus.machine.videohide = $scope.videohide;
                } else {
                    machineStatus.machine.videohide = $scope.videohide;
                }
                machineStatus.machine.fileName = $scope.fileName;
                machineStatus.machine.ifCheck = $scope.count;
                $window.sessionStorage.problemStatus = JSON.stringify(machineStatus);
            }

        }).error(function (data, status, headers, config) {
            //处理错误  
            alert('服务器拒绝访问');
        });

    };

    //  $rootScope.counter = 0;
    //标志旗帜颜色
    $scope.chgColor = function (count) {


        if (count) {
            $scope.red = "#000000";
            $scope.count = false;
            $rootScope.counter = $rootScope.counter - 1;

            var machineStatus = JSON.parse($window.sessionStorage.problemStatus);
            machineStatus.machine.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(machineStatus);

        } else {
            $scope.red = "#FF6347";
            $scope.count = true;
            $rootScope.counter = $rootScope.counter + 1;

            var machineStatus = JSON.parse($window.sessionStorage.problemStatus);
            machineStatus.machine.ifCheck = $scope.count;
            $window.sessionStorage.counter = $rootScope.counter;
            $window.sessionStorage.problemStatus = JSON.stringify(machineStatus);

        }

    }

});
var checkup = angular.module('checkup', []);
checkup.controller('btnCtrl', function ($scope) {

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
checkup.controller("Ctab1", function ($scope, $http, $state, $window, $rootScope) {

    $http.get('/EMS/exam/check', {
        params: { token: $window.sessionStorage.token, typeId: 0 }
    }).success(function (data, status, headers, config) {
        if (data.flag == false) {
            // alert(data.detail);
            $http.get('/EMS/exam/handExam', {
                params: { token: $window.sessionStorage.token }
            }).success(function (data, status, headers, config) {
                $state.go("finish", { score: data });
            });
        } else {
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
        }
    }).error(function (data, status, headers, config) {
        //处理错误  
        alert('服务器拒绝访问');
    });


    $scope.skip = function (listnum) {

        //  alert(listnum);
        $state.go('main', { active: 0, num: listnum, type: 1 });

    }


});
//多选题
checkup.controller("Ctab2", function ($scope, $http, $state, $window) {

    $http.get('/EMS/exam/check', {
        params: { token: $window.sessionStorage.token, typeId: 1 }
    }).success(function (data, status, headers, config) {
        if (data.flag == false) {
            // alert(data.detail);
            $http.get('/EMS/exam/handExam', {
                params: { token: $window.sessionStorage.token }
            }).success(function (data, status, headers, config) {
                $state.go("finish", { score: data });
            });
        } else {
            $scope.lists = data.checkList;
            /*  $rootScope.topicNum=data.topicNum;
              $rootScope.finishNum=data.finishNum;
              $rootScope.otherNum=data.topicNum-data.finishNum;*/
        }
    }).error(function (data, status, headers, config) {
        //处理错误  
        alert('服务器拒绝访问');
    });


    $scope.skip = function (listnum) {
        //    alert(listnum);
        $state.go('main', { active: 1, num: listnum, type: 2 });

    }


});
//判断题
checkup.controller("Ctab3", function ($scope, $http, $state, $window) {

    $http.get('/EMS/exam/check', {
        params: { token: $window.sessionStorage.token, typeId: 2 }
    }).success(function (data, status, headers, config) {
        if (data.flag == false) {
            // alert(data.detail);
            $http.get('/EMS/exam/handExam', {
                params: { token: $window.sessionStorage.token }
            }).success(function (data, status, headers, config) {
                $state.go("finish", { score: data });
            });
        } else {
            $scope.lists = data.checkList;
            /* $rootScope.topicNum=data.topicNum;
             $rootScope.finishNum=data.finishNum;
             $rootScope.otherNum=data.topicNum-data.finishNum;*/
        }
    }).error(function (data, status, headers, config) {
        //处理错误  
        alert('服务器拒绝访问');
    });


    $scope.skip = function (listnum) {
        // alert(listnum);
        $state.go('main', { active: 2, num: listnum, type: 3 });

    }


});
//匹配题
checkup.controller("Ctab4", function ($scope, $http, $state, $window) {

    $http.get('/EMS/exam/check', {
        params: { token: $window.sessionStorage.token, typeId: 3 }
    }).success(function (data, status, headers, config) {
        if (data.flag == false) {
            // alert(data.detail);
            $http.get('/EMS/exam/handExam', {
                params: { token: $window.sessionStorage.token }
            }).success(function (data, status, headers, config) {
                $state.go("finish", { score: data });
            });
        } else {
            $scope.lists = data.checkList;
            /* $rootScope.topicNum=data.topicNum;
             $rootScope.finishNum=data.finishNum;
             $rootScope.otherNum=data.topicNum-data.finishNum;*/
        }
    }).error(function (data, status, headers, config) {
        //处理错误  
        alert('服务器拒绝访问');
    });


    $scope.skip = function (listnum) {
        // alert(listnum);
        $state.go('main', { active: 3, num: listnum, type: 4 });

    }


});
//简答题
checkup.controller("Ctab5", function ($scope, $http, $state, $window) {

    $http.get('/EMS/exam/check', {
        params: { token: $window.sessionStorage.token, typeId: 4 }
    }).success(function (data, status, headers, config) {
        if (data.flag == false) {
            // alert(data.detail);
            $http.get('/EMS/exam/handExam', {
                params: { token: $window.sessionStorage.token }
            }).success(function (data, status, headers, config) {
                $state.go("finish", { score: data });
            });
        } else {
            $scope.lists = data.checkList;
            /* $rootScope.topicNum=data.topicNum;
             $rootScope.finishNum=data.finishNum;
             $rootScope.otherNum=data.topicNum-data.finishNum;*/
        }
    }).error(function (data, status, headers, config) {
        //处理错误  
        alert('服务器拒绝访问');
    });


    $scope.skip = function (listnum) {
        /*    alert(listnum);*/
        $state.go('main', { active: 4, num: listnum, type: 5 });

    }


});
//填空题
checkup.controller("Ctab6", function ($scope, $http, $state, $window) {

    $http.get('/EMS/exam/check', {
        params: { token: $window.sessionStorage.token, typeId: 5 }
    }).success(function (data, status, headers, config) {
        if (data.flag == false) {
            // alert(data.detail);
            $http.get('/EMS/exam/handExam', {
                params: { token: $window.sessionStorage.token }
            }).success(function (data, status, headers, config) {
                $state.go("finish", { score: data });
            });
        } else {
            $scope.lists = data.checkList;
            /* $rootScope.topicNum=data.topicNum;
             $rootScope.finishNum=data.finishNum;
             $rootScope.otherNum=data.topicNum-data.finishNum;*/
        }
    }).error(function (data, status, headers, config) {
        //处理错误  
        alert('服务器拒绝访问');
    });


    $scope.skip = function (listnum) {
        /*    alert(listnum);*/
        $state.go('main', { active: 5, num: listnum, type: 6 });

    }


});

//上机题
checkup.controller("Ctab7", function ($scope, $http, $state, $window) {

    $http.get('/EMS/exam/check', {
        params: { token: $window.sessionStorage.token, typeId: 6 }
    }).success(function (data, status, headers, config) {
        if (data.flag == false) {
            // alert(data.detail);
            $http.get('/EMS/exam/handExam', {
                params: { token: $window.sessionStorage.token }
            }).success(function (data, status, headers, config) {
                $state.go("finish", { score: data });
            });
        } else {
            $scope.lists = data.checkList;
            /* $rootScope.topicNum=data.topicNum;
             $rootScope.finishNum=data.finishNum;
             $rootScope.otherNum=data.topicNum-data.finishNum;*/
        }
    }).error(function (data, status, headers, config) {
        //处理错误  
        alert('服务器拒绝访问');
    });


    $scope.skip = function (listnum) {
        /*    alert(listnum);*/
        $state.go('main', { active: 6, num: listnum, type: 7 });

    }


});