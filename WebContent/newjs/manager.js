angular.module('manager', ['ui.bootstrap']);

angular.module('manager').controller('managerCtrl', function ($rootScope, $scope, $http, $window, $state, $interval) {

    var adminStatus = JSON.parse($window.sessionStorage.adminStatus);
    // 功能列表
    $scope.operationMetaInfo = adminStatus.authorityList;

    //侧边栏左
    $scope.operationMetaInfo = [
        { "name": "试卷管理", "url": "importExam" },
        { "name": "考生管理", "url": "importStuArrangement" },
        { "name": "考场管理", "url": "roomArrangement" },
        { "name": "成绩管理", "url": "exportExam" },
        { "name": "系统管理", "url": "systemManagement" }
    ];
    // 登陆用户id
    $scope.Rid = adminStatus.Rid;
    // 控制标签显示
    $scope.active = [];
    // 控制标签页显示
    $scope.display = [];
    for (i in $scope.operationMetaInfo) {
        $scope.active[i] = "";
        $scope.display[i] = "none";
    }
    // 初始化显示标签0
    $scope.index = 0;

    $rootScope.role = "管理员";
    $rootScope.logout = function () {
        $state.go("index");
    }

    // 监控index变量控制标签及标签页
    $scope.$watch('index', function () {
        if ($scope.index == undefined) {
            return;
        }
        for (i in $scope.active) {
            $scope.active[i] = "";
            $scope.display[i] = "none";
        };
        $scope.active[$scope.index] = "active";
        $scope.display[$scope.index] = "block";
        $scope.ngshow = false;
    });
    //试题导入
    $scope.superRequest = function (url) {
        /* console.log($scope); */
        /* $parent.index=$index; */

        switch (url) {
            case "examImport": //试题导入
                $state.go('examImport');
                break;

            case "importExam": // 导入试卷
                // $scope.showRoom = "none";
                $rootScope.refresh0();

                // $rootScope.selectedFile =undefined;   
                break;
            case "importStuArrangement": // 导入考生安排
                // $scope.showRoom = "none";
                $rootScope.refresh1();
                break;
            case "roomArrangement": // 考场管理
                // $scope.showRoom = "block";
                $rootScope.refresh2();
                $scope.selectionStatus = {};
                break;
            case "exportExam": // 成绩导出
                // $scope.showRoom = "none";
                $scope.exportByRoom = [{
                    id: 1
                }, { id: 31 }, { id: 11 }, { id: 14 }]
                $scope.orderCondition = 'id';
                $scope.isReverse = false;
                $rootScope.refresh3();
                //请求场次列表/sumList在父域中赋值
                // $http.get('/EMS/admin/roomLists', {
                //     // $http.get('info.json', {
                //     params: {
                //         token: $window.sessionStorage.stoken
                //     }
                // }).then(function successCallback(response) {
                //     $scope.exportByRoom = response.data;
                //     $scope.orderCondition = 'id';
                //     $scope.isReverse = false;
                // }, function errorCallback(response) { });
                break;
            case "systemManagement": // 系统管理
                $scope.showRoom = "none";
                break;
            default:
                $scope.showRoom = "none";

        }

    };

});
angular.module('manager').controller('roomCtrl', function ($rootScope, $scope, $http, $window, $state, $uibModal, $interval) {

    //侧边栏左
    $scope.operationMetaInfo = [
        { "name": "导入试卷", "url": "importExam" },
        { "name": "导入考生安排", "url": "importStuArrangement" },
        { "name": "考场管理", "url": "roomArrangement" },
        { "name": "成绩导出", "url": "exportExam" },
        { "name": "系统管理", "url": "systemManagement" }
    ];

    //初始化表格
    $scope.roomMetaInfo = {
        // 'id': '场次',
        'roomName': '考场名',
        'startTime': '开考时间',
        'supervisorId':'监考员账号',
        'size': '考场人数',
        'status': '登录状态',
        'status1': '试卷发放状态',
        'status2': '开考状态状态',
    };
    // 状态码转化成易读string
    $scope.statusDisplay = ['未登录', '已登录'];
    $scope.statusDisplay1 = ['未分发', '已分发'];
    $scope.statusDisplay2 = ['未开考', '已开考','已结束'];
    $scope.roomsStatus = {};
    $scope.selectionStatus = {};

    $scope.confirm = function (url) {

        var uidList = [];
        for (x in $scope.selectionStatus) {

            if ($scope.selectionStatus[x]) {
                uidList.push(x);
            }
        }
        if (uidList.length == 1) {
            $http.get('/EMS/admin/roomConfirm', {
                // $http.get('info.json', {
                params: {
                    token: $window.sessionStorage.stoken,
                    roomId: uidList
                }
            }).then(function successCallback(response) {
                var infoStatus = {};
                $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                var infoStatus = JSON.parse($window.sessionStorage.infoStatus);
                //功能列表
                infoStatus.authorityList = response.data.authorityList;
                //角色
                infoStatus.Rid = $scope.Rid;
                // 考场号
                infoStatus.roomId = uidList;
                $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                $window.sessionStorage.token = response.data.token;
                $window.open('#/supervisor');
                // var url = 'supervisor';
                // var urlHref = $state.href(url);
                // window.open(mv.urlHref);
            }, function errorCallback(response) { });

        } else {
            var modalParam = {
                backdrop: 'static',
                size: 'sm'
            };
            var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
            var headerBottom = '</h3></div>';
            var footer =
                '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
            modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">请只选择一个考场！</p></div>' + footer;
            $uibModal.open(modalParam).result.then(function () {
                if ($scope.confirm) {

                }
            });

        }



    }

    // 全选
    $scope.selectAll = function () {
        for (x in $scope.roomsInfo) {
            $scope.selectionStatus[$scope.roomsInfo[x].id] = true;
        }
        $scope.selectedNum = $scope.roomsInfo.length;
    }

    // 取消选择
    $scope.cancelAll = function () {
        for (x in $scope.roomsInfo) {
            $scope.selectionStatus[$scope.roomsInfo[x].id] = false;
        }
        $scope.selectedNum = 0;
    }

    // 单独选择
    $scope.checkSel = function (status, roomId) {
      //  $scope.cancelAll();
        // alert(status)
        // $scope.selectionStatus[roomId] = true;
        if (status == true) {
            $scope.selectionStatus[roomId] = true;
        } else {
            $scope.selectionStatus[roomId] = false;
        }
    }

    // 排序变量
    $scope.thClick = function (value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

    // 刷新
    refresh();
    $scope.refresh = function () {
        refresh();
    };

    $rootScope.refresh2 = function () {
        refresh();
    };
    //每间隔10s自动刷新
    //var timingPromise = undefined;
    //  timingPromise = $interval(function () { refresh() }, 30000);

    function refresh() {

        $http({
            method: 'GET',
            url: '/EMS/admin/Refresh',
            params: {
                token: $window.sessionStorage.stoken
            }
        })
            .then(
            function success(response) {
                $scope.roomsInfo = response.data;
                $scope.absentNum = 0;
                $scope.examingNum = 0;
                for (x in $scope.roomsInfo) {
                    switch ($scope.roomsInfo[x].status) {
                        case 0:
                            $scope.absentNum += 1;
                            $scope.roomsStatus[$scope.roomsInfo[x].roomId] = '';
                            break;
                        case 1:
                            $scope.examingNum += 1;
                            $scope.roomsStatus[$scope.roomsInfo[x].roomId] = 'info';
                            break;
                        default:
                            alert('考生状态错误！');
                    }
                }
                $scope.cancelAll();
                $scope.orderCondition = 'roomName';
                $scope.isReverse = false;
            },
            function error() { });
        // function error(response) {
        //     alert('刷新出错\n' + response.status
        //         + ' ' + response.statusText);
        // });
    }
    //试题装载
    $scope.loadExam = function () {

        $scope.loadStyle = 'cursor:wait';
        $scope.loadButtonDisabled = true;
        $http.get('/EMS/admin/load', {
            // $http.get('info.json', {
            params: {
                token: $window.sessionStorage.stoken
            }
        }).then(function successCallback(response) {
            // console.log('success');
            // alert("试题装载成功");
            if (response.data.flag) {
                var modalParam = {
                    backdrop: 'static',
                    size: 'sm'
                };
                var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
                var headerBottom = '</h3></div>';
                var footer =
                    '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
                modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">分发试卷成功！</p></div>' + footer;
                $uibModal.open(modalParam).result.then(function () {
                    if ($scope.confirm) {

                    }
                });
                $scope.loadStyle = 'cursor:default';
                $scope.loadButtonDisabled = false;

                refresh();

            } else {
                $scope.loadStyle = 'cursor:default';
                $scope.loadButtonDisabled = false;
                var modalParam = {
                    backdrop: 'static',
                    size: 'sm'
                };
                var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
                var headerBottom = '</h3></div>';
                var footer =
                    '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
                modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">' + response.data.detail + '</p></div>' + footer;
                $uibModal.open(modalParam).result.then(function () {
                    if ($scope.confirm) {

                    }
                });
            }

        })
    }
    //开考
    $scope.loadStart = function () {
        var uidList = [];
        for (x in $scope.selectionStatus) {

            if ($scope.selectionStatus[x]) {
                uidList.push(x);
            }
        }
        $scope.loadStyles = 'cursor:wait';
        $scope.loadButtonDisableds = true;
        $http.get('/EMS/admin/start', {
            params: {
                token: $window.sessionStorage.stoken,
                list:uidList
            }
        }).then(function successCallback(response) {
            if (response.data.flag) {
                var modalParam = {
                    backdrop: 'static',
                    size: 'sm'
                };
                var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
                var headerBottom = '</h3></div>';
                var footer =
                    '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
                modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">开考成功！</p></div>' + footer;
                $uibModal.open(modalParam).result.then(function () {
                    if ($scope.confirm) {
                        refresh();
                    }
                });
                $scope.loadStyles = 'cursor:default';
                $scope.loadButtonDisableds = false;
            } else {
                var modalParam = {
                    backdrop: 'static',
                    size: 'sm'
                };
                var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
                var headerBottom = '</h3></div>';
                var footer =
                    '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
                modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">' + response.data.detail + '</p></div>' + footer;
                $uibModal.open(modalParam).result.then(function () {
                    if ($scope.confirm) {

                    }
                });
                $scope.loadStyles = 'cursor:default';
                $scope.loadButtonDisableds = false;
            }



        })
    }


});
angular.module('manager').controller('ImportFile', function ($rootScope, $scope, $http, $window, $timeout, $state, $interval, $uibModal) {
    //初始化表格
    $scope.examMetaInfo = {
        'Mid': '专业名称',
        'Mnum': '专业编号',
        'Sid': '科目名称',
        'Snum': '科目编号',
        'Jid': '试卷号',
        'sum': '试卷总分',
        'a': '各题型题数与总分',
        //  'dura': '考试时长',
    };

    $scope.selectionStatus = {};

    // 全选
    // $scope.selectAll = function () {
    //     for (x in $scope.Examinfo) {
    //         $scope.selectionStatus[$scope.Examinfo[x].id] = true;
    //     }
    // }

    // // 取消选择
    // $scope.cancelAll = function () {
    //     for (x in $scope.Examinfo) {
    //         $scope.selectionStatus[$scope.Examinfo[x].id] = false;
    //     }
    // }

    // // 单独选择
    // $scope.checkSel = function (status, roomId) {
    //     $scope.cancelAll();
    //     $scope.selectionStatus[roomId] = true;

    // }

    // 排序变量
    $scope.thClick = function (value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

    // 表格刷新
    refresh();
    $scope.refresh = function () {
        refresh();
    };
    $rootScope.refresh0 = function () {
        refresh();
    };

    function refresh() {
        $http.get('/EMS/admin/paperInfo', {
            // $http.get('info.json', {
            params: {
                token: $window.sessionStorage.stoken
            }
        }).then(function successCallback(response) {
            $scope.Examinfo = response.data;
            $scope.orderCondition = 'Mid';
            $scope.isReverse = false;
        }, function errorCallback(response) { })

    }



    //正在检测不显示
    $scope.checkshow = false;

    $scope.clearPer = 0;
    $scope.clearshow = false;
    $scope.progressPer = 0;
    $scope.ngshow = false;
    $scope.selectFile = function () {
        $scope.$apply(function () {
            $scope.selectedFile = event.target.files[0];
            $scope.fileName = $scope.selectedFile.name;
        })
    }
    //上传文件
    $scope.upload = function () {
        if ($scope.selectedFile) {

            $scope.ngshow = true;

            var formData = new FormData();
            formData.append("file", $scope.selectedFile);
            if ($scope.selectedFile == undefined) {
                return;
            }
            $scope.progressPer = 0;
            $http({
                method: 'POST',
                url: '/EMS/admin/examForm',
                data: formData,
                headers: {
                    'Content-Type': undefined,
                },
                uploadEventHandlers: {
                    progress: function (e) {
                        $scope.progressPer = Math.round((e.loaded / e.total * 100) * 100) / 100;
                        // $scope.progressInfo = '上传中';
                        if ($scope.progressPer == 100) {
                            $scope.checkshow = true;
                            $scope.ngshow = false;
                        }
                    }
                }
            }).then(function success(response) {
                //$scope.ngshow = false;
                $scope.checkshow = false;

                var modalParam = {
                    backdrop: 'static',
                    size: 'sm'
                };
                var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
                var headerBottom = '</h3></div>';
                var footer =
                    '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
                modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">' + response.data.info + '</p></div>' + footer;
                $uibModal.open(modalParam).result.then(function () {
                    if ($scope.confirm) {
                        $scope.checkshow = false;
                        refresh();
                        // $scope.fileName=undefined;                        
                    }
                });
            }, function error(response) {
                alert('出现错误\n' + response.status + ' ' + response.statusText);
            });
        } else {
            // alert("请选择文件");
            var modalParam = {
                backdrop: 'static',
                size: 'sm'
            };
            var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
            var headerBottom = '</h3></div>';
            var footer =
                '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
            modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">请选择文件！</p></div>' + footer;
            $uibModal.open(modalParam).result.then(function () {
                if ($scope.confirm) {

                }
            });
        }
    }

    // $scope.upload = function () {
    //     if ($scope.selectedFile) {

    //         $scope.ngshow = true;
    //         var formData = new FormData();
    //         formData.append("file", $scope.selectedFile);
    //         if ($scope.selectedFile == undefined) {
    //             return;
    //         }
    //         $scope.progressPer = 0;
    //         $http({
    //             method: 'POST',
    //             url: '/EMS/admin/examForm',
    //             data: formData,
    //             headers: {
    //                 'Content-Type': undefined,
    //             },
    //             uploadEventHandlers: {
    //                 progress: function (e) {
    //                     $scope.progressPer = Math.round((e.loaded / e.total * 100) * 100) / 100;
    //                     // $scope.progressInfo = '上传中';
    //                 }
    //             }
    //         }).then(function success(response) {
    //             $scope.ngshow = false;
    //             // $scope.progressInfo = response.data.info;
    //             var modalParam = {
    //                 backdrop: 'static',
    //                 size: 'sm'
    //             };
    //             var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
    //             var headerBottom = '</h3></div>';
    //             var footer =
    //                 '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
    //             modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">' + response.data.info + '</p></div>' + footer;
    //             $uibModal.open(modalParam).result.then(function () {
    //                 if ($scope.confirm) {
    //                     if (response.data.info == "上传成功") {
    //                         $scope.checkshow = true;
    //                         // $scope.selectedFile.name=null;
    //                         $http({
    //                             method: 'GET',
    //                             url: '/EMS/admin/uploadCheck'
    //                         }).then(function successCallback(response) {
    //                             $scope.checkshow = false;

    //                             var modalParam = {
    //                                 backdrop: 'static',
    //                                 size: 'sm'
    //                             };
    //                             var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
    //                             var headerBottom = '</h3></div>';
    //                             var footer =
    //                                 '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
    //                             modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">' + response.data.info + '</p></div>' + footer;
    //                             $uibModal.open(modalParam).result.then(function () {
    //                                 if ($scope.confirm) {
    //                                     $scope.checkshow = false;
    //                                 }
    //                             });


    //                         }, function errorCallback(response) {
    //                             // called asynchronously if an error occurs
    //                             // or server returns response with an error status.
    //                         });

    //                     }


    //                 }
    //             });
    //         }, function error(response) {
    //             alert('出现错误\n' + response.status + ' ' + response.statusText);
    //         });
    //     } else {
    //         // alert("请选择文件");
    //         var modalParam = {
    //             backdrop: 'static',
    //             size: 'sm'
    //         };
    //         var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
    //         var headerBottom = '</h3></div>';
    //         var footer =
    //             '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
    //         modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">请选择文件！</p></div>' + footer;
    //         $uibModal.open(modalParam).result.then(function () {
    //             if ($scope.confirm) {

    //             }
    //         });
    //     }
    // }

    $scope.clear = function () {
        $scope.clearPer = 0;
        $scope.clearshow = true;

        // updateProgressBar(20);

        // $timeout(function () {
        //     updateProgressBar(100);
        // }, 4000);


        // function updateProgressBar(value) {
        //     $scope.clearPer = value;

        //     // if (value == 100) $scope.active = false;
        // }
        // eventHandlers: {
        //         progress: function(e) {
        //             // alert("haah");
        //             // console.log(e);
        //             $scope.clearPer = e.total / e.loaded * 100;
        //         }
        //     }

        $http.get('/EMS/admin/examClear', {
            params: {
                token: $window.sessionStorage.stoken
            }

        }).then(function successCallback(response) {
            refresh();
            $scope.clearshow = false;
            var modalParam = {
                backdrop: 'static',
                size: 'sm'
            };
            var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
            var headerBottom = '</h3></div>';
            var footer =
                '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
            modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">' + response.data.info + '</p></div>' + footer;
            $uibModal.open(modalParam).result.then(function () {
                if ($scope.confirm) {

                }
            });
            // alert(response.data.info);
        }, function errorCallback(response) { });
    }

});
angular.module('manager').controller('ImportStuFile', function ($rootScope, $scope, $http, $window, $state, $interval, $uibModal) {
    //初始化表格
    $scope.stuMetaInfo = {
        'stuR': '考场名',
        'stuT': '开考时间',
        'stuZ': '准考证号',
        'stuS': '座位号',
        'stuN': '姓名',
        'stuL': '登录状态',
        'stuI': 'IP地址',
        'stuG': '性别',
        'stuid': '证件号',
        'stuP': '照片',
        'Mnum': '专业名称',
        'Sid': '科目名称',
        'num': '试卷号',
    };

    $scope.stuStatus = ["未登录", "正在考试", "已交卷"];
    // 排序变量
    $scope.thClick = function (value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

    // 刷新
    refresh();
    $scope.refresh = function () {
        refresh();
    };
    $rootScope.refresh1 = function () {
        refresh();
    };
    //每间隔5s自动刷新
    //var timingPromise = undefined;
    // timingPromise = $interval(function () { refresh() }, 5000);

    function refresh() {
        $http.get('/EMS/admin/stuInfo', {
            // $http.get('info.json', {
            params: {
                token: $window.sessionStorage.stoken
            }
        }).then(function successCallback(response) {
            $scope.stuinfo = response.data;
            $scope.orderCondition = 'stuR';
            $scope.isReverse = false;
        }, function errorCallback(response) { })

    }


    //正在检测不显示
    $scope.checkshow = false;


    $scope.progressPer = 0;
    $scope.ngshow = false;
    $scope.clearPer = 0;
    $scope.clearshow = false;

    $scope.selectFile = function () {

        $scope.$apply(function () {
            $scope.selectedFile = event.target.files[0];
        })
    }

    $scope.upload = function () {
        if ($scope.selectedFile) {
            $scope.ngshow = true;
            var formData = new FormData();
            formData.append("file", $scope.selectedFile);
            formData.append("token", $window.sessionStorage.stoken);
            if ($scope.selectedFile == undefined) {
                return;
            }
            $scope.progressPer = 0;
            $http({
                method: 'POST',
                url: '/EMS/admin/stuForm',
                data: formData,
                headers: {
                    'Content-Type': undefined,
                },
                uploadEventHandlers: {
                    progress: function (e) {
                        $scope.progressPer = Math.round((e.loaded / e.total * 100) * 100) / 100;
                        // $scope.progressInfo = '上传中';
                        if ($scope.progressPer == 100) {
                            $scope.checkshow = true;
                            $scope.ngshow = false;
                        }
                    }
                }
            }).then(function success(response) {

                $scope.checkshow = false;

                var modalParam = {
                    backdrop: 'static',
                    size: 'sm'
                };
                var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
                var headerBottom = '</h3></div>';
                var footer =
                    '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
                modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">' + response.data.info + '</p></div>' + footer;
                $uibModal.open(modalParam).result.then(function () {
                    if ($scope.confirm) {
                        $scope.checkshow = false;
                        refresh();
                    }
                });
            }, function error(response) {
                alert('出现错误\n' + response.status + ' ' + response.statusText);
            });
        } else {
            // alert("请选择文件");
            var modalParam = {
                backdrop: 'static',
                size: 'sm'
            };
            var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
            var headerBottom = '</h3></div>';
            var footer =
                '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
            modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">请选择文件！</p></div>' + footer;
            $uibModal.open(modalParam).result.then(function () {
                if ($scope.confirm) {

                }
            });
        }
    }


    // $scope.upload = function () {
    //     if ($scope.selectedFile) {
    //         $scope.ngshow = true;
    //         var formData = new FormData();
    //         formData.append("file", $scope.selectedFile);
    //         formData.append("token", $window.sessionStorage.stoken);
    //         if ($scope.selectedFile == undefined) {
    //             return;
    //         }
    //         $scope.progressPer = 0;
    //         $http({
    //             method: 'POST',
    //             url: '/EMS/admin/stuForm',
    //             data: formData,
    //             headers: {
    //                 'Content-Type': undefined,
    //             },
    //             uploadEventHandlers: {
    //                 progress: function (e) {
    //                     $scope.progressPer = Math.round((e.loaded / e.total * 100) * 100) / 100;
    //                     // $scope.progressInfo = '上传中';
    //                     if($scope.progressPer==100){
    //                         $scope.checkshow = true;
    //                     }
    //                 }
    //             }
    //         }).then(function success(response) {
    //             $scope.ngshow = false;
    //             // $scope.progressInfo = response.data.info;
    //             var modalParam = {
    //                 backdrop: 'static',
    //                 size: 'sm'
    //             };
    //             var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
    //             var headerBottom = '</h3></div>';
    //             var footer =
    //                 '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
    //             modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">' + response.data.info + '</p></div>' + footer;
    //             $uibModal.open(modalParam).result.then(function () {
    //                 if ($scope.confirm) {
    //                     if (response.data.info == "上传成功") {
    //                         $scope.checkshow = true;
    //                         // $scope.selectedFile.name=null;
    //                         $http({
    //                             method: 'GET',
    //                             url: '/EMS/admin/uploadCheck'
    //                         }).then(function successCallback(response) {
    //                             $scope.checkshow = false;

    //                             var modalParam = {
    //                                 backdrop: 'static',
    //                                 size: 'sm'
    //                             };
    //                             var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
    //                             var headerBottom = '</h3></div>';
    //                             var footer =
    //                                 '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
    //                             modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">' + response.data.info + '</p></div>' + footer;
    //                             $uibModal.open(modalParam).result.then(function () {
    //                                 if ($scope.confirm) {
    //                                     $scope.checkshow = false;
    //                                 }
    //                             });


    //                         }, function errorCallback(response) {
    //                             // called asynchronously if an error occurs
    //                             // or server returns response with an error status.
    //                         });

    //                     }
    //                 }
    //             });
    //         }, function error(response) {
    //             alert('出现错误\n' + response.status + ' ' + response.statusText);
    //         });
    //     } else {
    //         // alert("请选择文件");
    //         var modalParam = {
    //             backdrop: 'static',
    //             size: 'sm'
    //         };
    //         var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
    //         var headerBottom = '</h3></div>';
    //         var footer =
    //             '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
    //         modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">请选择文件！</p></div>' + footer;
    //         $uibModal.open(modalParam).result.then(function () {
    //             if ($scope.confirm) {

    //             }
    //         });
    //     }
    // }
    $scope.clear = function () {
        $scope.clearPer = 0;
        $scope.clearshow = true;

        $http.get('/EMS/admin/stuClear', {
            params: {
                token: $window.sessionStorage.stoken
            },
            eventHandlers: {
                progress: function (e) {
                    // alert("haah");
                    $scope.clearPer = e.loaded / e.total * 100;
                }
            }
        }).then(function successCallback(response) {
            refresh();
            $scope.clearshow = false;
            var modalParam = {
                backdrop: 'static',
                size: 'sm'
            };
            var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
            var headerBottom = '</h3></div>';
            var footer =
                '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
            modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">' + response.data.info + '</p></div>' + footer;
            $uibModal.open(modalParam).result.then(function () {
                if ($scope.confirm) {

                }
            });
            // alert(response.data.info);
        }, function errorCallback(response) { });
    }

});
angular.module('manager').controller('exportFile', function ($uibModal, $rootScope, $scope, $http, $window, $state, $interval) {
    //初始化表格
    $scope.roomMetaInfo = {
        // 'id': '场次',
        'roomName': '考场名',
        'time': '开始时间',
        'status': '考场状态',
        'sumNum': '应考人数',
        'log': '已登录人数',
        'unlog': '未登录人数'
    };

    $scope.selectionStatus = {};
    $scope.roomStatus = ['未分发', '已分发', '未开考', '已开考','已结束'];
    // 全选
    $scope.selectAll = function () {
        for (x in $scope.exportByRoom) {
            $scope.selectionStatus[$scope.exportByRoom[x].id] = true;
        }
    }

    // 取消选择
    $scope.cancelAll = function () {
        for (x in $scope.exportByRoom) {
            $scope.selectionStatus[$scope.exportByRoom[x].id] = false;
        }
    }

    // 单独选择
    $scope.checkSel = function (status, roomId) {

        // alert(status);
        if (status == true) {
            $scope.selectionStatus[roomId] = true;
        } else {
            $scope.selectionStatus[roomId] = false;
        }


    }

    // 排序变量
    $scope.thClick = function (value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

    // 刷新
    refresh();
    $scope.refresh = function () {
        refresh();
    };
    $rootScope.refresh3 = function () {
        refresh();
    };
    //每间隔30s自动刷新
    // var timingPromise = undefined;
    // timingPromise = $interval(function () { refresh() }, 10000);

    function refresh() {
        $http.get('/EMS/admin/roomLists', {
            // $http.get('info.json', {
            params: {
                token: $window.sessionStorage.stoken
            }
        }).then(function successCallback(response) {
            $scope.exportByRoom = response.data;
            $scope.orderCondition = 'roomName';
            $scope.isReverse = false;
        }, function errorCallback(response) { })

    }
    $scope.sum = {};
    $scope.sumE = function () {
        // 友好提示信息，url未定
        // alert("");
        if ($scope.sum.type) {
            if ($scope.sum.type == "sumExport") {
                // $http.get('/EMS/admin/load', {
                //     params: {
                //         token: $window.sessionStorage.stoken
                //     }
                // }).then(function successCallback(response) {
                //     var modalParam = {
                //         backdrop: 'static',
                //         size: 'sm'
                //     };
                //     var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
                //     var headerBottom = '</h3></div>';
                //     var footer =
                //         '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
                //     modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">'+response.info+'</p></div>' + footer;
                //     $uibModal.open(modalParam).result.then(function () {
                //         if ($scope.confirm) {

                //         }
                //     });
                // });

                $scope.sumExport();
                refresh();
            } else {

                // $http.get('/EMS/admin/load', {
                //     params: {
                //         token: $window.sessionStorage.stoken
                //     }
                // }).then(function successCallback(response) {

                //     var modalParam = {
                //         backdrop: 'static',
                //         size: 'sm'
                //     };
                //     var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
                //     var headerBottom = '</h3></div>';
                //     var footer =
                //         '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
                //     modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">'+response.info+'</p></div>' + footer;
                //     $uibModal.open(modalParam).result.then(function () {
                //         if ($scope.confirm) {

                //         }
                //     });
                // });
                $scope.stuExport();
                refresh();
            }
        } else {
            var modalParam = {
                backdrop: 'static',
                size: 'sm'
            };
            var headerTop = '<div class="modal-header"><h3 class="modal-title">提醒'
            var headerBottom = '</h3></div>';
            var footer =
                '<div class="modal-footer"><button class="btn btn-primary" type="button" ng-click="$parent.confirm=true;$close()">确认</button></div>';
            modalParam.template = headerTop + headerBottom + '<div class="modal-body"><p style="font-size:150%">请选择成绩汇总/考生答卷</p></div>' + footer;
            $uibModal.open(modalParam).result.then(function () {
                if ($scope.confirm) {

                }
            });
        }
    }

    //导出信息
    $scope.sumExport = function () {
        var uidList = [];
        for (x in $scope.selectionStatus) {

            if ($scope.selectionStatus[x]) {
                uidList.push(x);
            }
        }
        window.open("/EMS/admin/sumDownload?token=" + $window.sessionStorage.stoken + "&id=" + uidList);
    };

    $scope.stuExport = function () {
        var uidList = [];
        for (x in $scope.selectionStatus) {

            if ($scope.selectionStatus[x]) {
                uidList.push(x);
            }
        }
        window.open("/EMS/admin/stuDownload?token=" + $window.sessionStorage.stoken + "&id=" + uidList);
    }
    //考生答卷清空
    $scope.stuClear = function () {
        var uidList = [];
        for (x in $scope.selectionStatus) {

            if ($scope.selectionStatus[x]) {
                uidList.push(x);
            }
        }
        $http.get('/EMS/admin/stuDownloadClear', {
            params: {
                token: $window.sessionStorage.stoken
            }
        }).then(function successCallback(response) {
            // alert("清空成功！");
            alert(response.data.info);
        }, function errorCallback(response) { });
    }



});
