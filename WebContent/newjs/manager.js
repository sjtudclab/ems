angular.module('manager', ['ui.bootstrap']);

angular.module('manager').controller('managerCtrl', function ($rootScope, $scope, $http, $window, $state, $interval) {

    var adminStatus = JSON.parse($window.sessionStorage.adminStatus);
    // 功能列表
    $scope.operationMetaInfo = adminStatus.authorityList;

    //侧边栏左
    $scope.operationMetaInfo = [
        { "name": "导入试卷", "url": "importExam" },
        { "name": "导入考生安排", "url": "importStuArrangement" },
        { "name": "考场管理", "url": "roomArrangement" },
        { "name": "成绩导出", "url": "exportExam" },
        { "name": "系统管理", "url": "systemManagement" }];
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
   

    // 监控index变量控制标签及标签页
    $scope.$watch('index', function () {
        if ($scope.index == undefined) {
            return;
        }
        for (i in $scope.active) {
            $scope.active[i] = "";
            $scope.display[i] = "none";
        }
        ;
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
                break;
            case "importStuArrangement": // 导入考生安排
                // $scope.showRoom = "none";
                break;
            case "roomArrangement": // 考场管理
                // $scope.showRoom = "block";
                $scope.selectionStatus = {};
                break;
            case "exportExam": // 成绩导出
                // $scope.showRoom = "none";
                $scope.exportByRoom = [{
                    id: 1
                }, { id: 31 }, { id: 11 }, { id: 14 }]

                //请求场次列表/sumList在父域中赋值
                $http.get('/EMS/admin/roomLists', {
                    // $http.get('info.json', {
                    params: {
                        token: $window.sessionStorage.stoken
                    }
                }).then(function successCallback(response) {
                    $scope.exportByRoom = response.data;
                    $scope.orderCondition = 'id';
			        $scope.isReverse = false;
                }, function errorCallback(response) {
                });
                break;
            case "systemManagement": // 系统管理
                $scope.showRoom = "none";
                break;
            default:
                $scope.showRoom = "none";

        }

    };

});
angular.module('manager').controller('roomCtrl', function ($rootScope, $scope, $http, $window, $state, $interval) {

    //侧边栏左
    $scope.operationMetaInfo = [
        { "name": "导入试卷", "url": "importExam" },
        { "name": "导入考生安排", "url": "importStuArrangement" },
        { "name": "考场管理", "url": "roomArrangement" },
        { "name": "成绩导出", "url": "exportExam" },
        { "name": "系统管理", "url": "systemManagement" }];

    //初始化表格
    $scope.roomMetaInfo = {
        'id': '场次',
        'roomName': '考场名',
        'startTime': '开考时间',
        'size': '考场人数',
        'status': '状态'
    };
    // 状态码转化成易读string
    $scope.statusDisplay = ['未登录', '已登录'];
    $scope.roomsStatus = {};
    $scope.selectionStatus = {};

    $scope.confirm = function (url) {

        var uidList = [];
        for (x in $scope.selectionStatus) {

            if ($scope.selectionStatus[x]) {
                uidList.push(x);
            }
        }

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
        }, function errorCallback(response) {
        });


    }

    // 全选
    $scope.selectAll = function () {
        for (x in $scope.roomsInfo) {
            $scope.selectionStatus[$scope.roomsInfo[x].roomId] = true;
        }
        $scope.selectedNum = $scope.roomsInfo.length;
    }

    // 取消选择
    $scope.cancelAll = function () {
        for (x in $scope.roomsInfo) {
            $scope.selectionStatus[$scope.roomsInfo[x].roomId] = false;
        }
        $scope.selectedNum = 0;
    }

    // 单独选择
    $scope.checkSel = function (status, roomId) {
        $scope.cancelAll();
        $scope.selectionStatus[roomId] = true;
        if (status == true) {
            $scope.selectedNum += 1;
        }
        if (status == false) {
            $scope.selectedNum -= 1;
        }
    }

    // 排序变量
    $scope.thClick = function (value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

    // 刷新
    $scope.refresh = function () {
        refresh();
    };
    //每间隔30s自动刷新
    var timingPromise = undefined;
    timingPromise = $interval(function () { refresh() }, 30000);

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
                $scope.orderCondition = 'id';
			    $scope.isReverse = false;
            },
            function error(response) {
                alert('刷新出错\n' + response.status
                    + ' ' + response.statusText);
            });
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
            alert("试题装载成功");
            $scope.loadStyle = 'cursor:default';
            $scope.loadButtonDisabled = false;
            $http({
                method: 'GET',
                url: '/EMS/admin/Refresh',
                params: {
                    token: $window.sessionStorage.stoken
                }
            })
                .then(
                function success(response) {
                    // 考场信息
                    $scope.roomsInfo = response.data;
                    // 已选人数
                    $scope.selectedNum = 0;
                    // 未登录人数
                    $scope.absentNum = 0;
                    // 已登录人数
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
                    $scope.orderCondition = 'id';
                    $scope.isReverse = false;
                },
                function error(response) {
                    alert('出现错误\n' + response.status + ' '
                        + response.statusText);
                });

        })
    }

});
angular.module('manager').controller('ImportFile', function ($rootScope, $scope, $http, $window, $state, $interval) {

    $scope.progressPer = 0;
    $scope.ngshow = false;
    $scope.selectFile = function () {
        $scope.$apply(function () {
            $scope.selectedFile = event.target.files[0];
        })
    }

    $scope.upload = function () {
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


});
angular.module('manager').controller('ImportStuFile', function ($rootScope, $scope, $http, $window, $state, $interval) {

    $scope.progressPer = 0;
    $scope.ngshow = false;
    $scope.selectFile = function () {

        $scope.$apply(function () {
            $scope.selectedFile = event.target.files[0];
        })
    }

    $scope.upload = function () {
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


});
angular.module('manager').controller('exportFile', function ($rootScope, $scope, $http, $window, $state, $interval) {
    //初始化表格
    $scope.roomMetaInfo = {
        'id': '场次'
    };
    // 状态码转化成易读string
    // $scope.statusDisplay = ['未登录', '已登录'];
    // $scope.roomsStatus = {};
    $scope.selectionStatus = {};
    // $scope.exportByRoom=[{
    //     id:1
    // },{id:3},{id:11},{id:14}]

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
        $scope.cancelAll();
        $scope.selectionStatus[roomId] = true;
        if (status == true) {
            $scope.selectedNum += 1;
        }
        if (status == false) {
            $scope.selectedNum -= 1;
        }
    }

    // 排序变量
    $scope.thClick = function (value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

    // 刷新
    $scope.refresh = function () {
        // refresh();
    };
    //每间隔30s自动刷新
    var timingPromise = undefined;
    timingPromise = $interval(function () { refresh() }, 30000);

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
                $scope.exportByRoom = response.data;
                $scope.absentNum = 0;
                $scope.examingNum = 0;
                for (x in $scope.exportByRoom) {
                    switch ($scope.exportByRoom[x].status) {
                        case 0:
                            $scope.absentNum += 1;
                            $scope.roomsStatus[$scope.exportByRoom[x].roomId] = '';
                            break;
                        case 1:
                            $scope.examingNum += 1;
                            $scope.roomsStatus[$scope.exportByRoom[x].roomId] = 'info';
                            break;
                        default:
                            alert('考生状态错误！');
                    }
                }
                $scope.cancelAll();
                $scope.orderCondition = 'id';
			    $scope.isReverse = false;
            },
            function error(response) {
                alert('刷新出错\n' + response.status
                    + ' ' + response.statusText);
            });
    }


    $scope.sumExport = function () {
        var uidList = [];
        for (x in $scope.selectionStatus) {

            if ($scope.selectionStatus[x]) {
                uidList.push(x);
            }
        }
        alert(uidList);
        window.open("/EMS/admin/sumDownload?token=" + $window.sessionStorage.stoken);
    };
    $scope.stuExport = function () {
        var uidList = [];
        for (x in $scope.selectionStatus) {

            if ($scope.selectionStatus[x]) {
                uidList.push(x);
            }
        }
        window.open("/EMS/admin/stuDownload?token=" + $window.sessionStorage.stoken);
    }



});