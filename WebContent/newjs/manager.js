angular.module('manager', ['ui.bootstrap']);

angular
    .module('manager')
    .controller(
				'managerCtrl',
				function ($rootScope, $scope, $http, $window, $state) {

        var adminStatus = JSON.parse($window.sessionStorage.adminStatus);
        // 功能列表
        $scope.operationMetaInfo = adminStatus.authorityList;
        // 登陆用户id
        $scope.Rid = response.data.Rid;
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

        // $http({
        //     method: 'GET',
        //     url: 'manger.json',
        //     params: {
        //         token: $window.sessionStorage.token
        //     }
        // })
        //     .then(
        //     function success(response) {
        //         // 功能列表
        //         $scope.operationMetaInfo = response.data.authorityList;
        //         // 登陆用户id
        //         $scope.rId = response.data.Rid;
        //         // 控制标签显示
        //         $scope.active = [];
        //         // 控制标签页显示
        //         $scope.display = [];
        //         for (i in $scope.operationMetaInfo) {
        //             $scope.active[i] = "";
        //             $scope.display[i] = "none";
        //         }
        //         // 初始化显示标签0
        //         $scope.index = 0;
        //     })
 // $http({
        //     method: 'GET',
        //     url: 'roominfo.json',
        //     params: {
        //         token: $window.sessionStorage.token
        //     }
        // })

        $http({
            method: 'GET',
            url: '/EMS/admin/Refresh',
            params: {
                token: $window.sessionStorage.token
            }
        })
            .then(
            function success(response) {
                // 考场信息
                $scope.roomsInfo = response.data;
                $scope.roomMetaInfo = {
                    'roomId': '考场号',
                    'name': '考场位置',
                    'supervisor': '监考老师',
                    'size': '考场人数',
                    'status': '状态'
                };
                // 状态码转化成易读string
                $scope.statusDisplay = ['未登录', '已登录'];
                $scope.roomsStatus = {};
                $scope.selectionStatus = {};
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
                $scope.orderCondition = 'roomId';
                $scope.isReverse = false;
            },
            function error(response) {
                alert('出现错误\n' + response.status + ' '
                    + response.statusText);
            });
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
        });
        //试题导入
        $scope.superRequest = function (url) {
            /* console.log($scope); */
            /* $parent.index=$index; */
            switch (url) {
                case "examImport": //试题导入
                    $state.go('examImport');
                    break;
                case "roomManager": // 考场管理
                    $scope.selectionStatus = {};
                    break;

            }

        };

        $scope.confirm = function (url) {
            /* alert($scope.inputMessage); */
            /* console.log($scope); */
            var uidList = [];
            for (x in $scope.selectionStatus) {
                // alert(x + ' ' + $scope.selectionStatus[x]);
                if ($scope.selectionStatus[x]) {
                    uidList.push(x);
                }
            }
            // alert(uidList);
            $http.get('/EMS/admin/roomConfirm', {
            // $http.get('info.json', {
                params: {
                    token: $window.sessionStorage.token,
                    roomId: uidList
                }
            }).then(function successCallback(response) {
                var infoStatus = {};
                $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
                var infoStatus = JSON.parse($window.sessionStorage.infoStatus);
                //功能列表
                infoStatus.authorityList = response.data.authorityList;
                // 考场号
                infoStatus.roomId = uidList;
                $window.sessionStorage.infoStatus = JSON.stringify(infoStatus);
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
        function refresh() {
            /* alert(refresh); */
            $http({
                method: 'GET',
                url: '/EMS/admin/Refresh',
                params: {
                    token: $window.sessionStorage.token
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
                    token: $window.sessionStorage.token
                }
            }).then(function successCallback(response) {
                console.log('success');
                alert("试题装载成功");
                $scope.loadStyle = 'cursor:default';
                $scope.loadButtonDisabled = false;
            })
        }

				});
