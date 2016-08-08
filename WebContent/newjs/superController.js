angular.module('supervisor', ['ui.bootstrap']);
angular.module('supervisor').controller('supervisorCtrl', function($scope, $http) {


    $http({
        method: 'GET',
        url: 'supervi.json'
    }).then(function success(response) {
        //功能列表
        $scope.operationMetaInfo = response.data.authorityList;
        // 考场号
        $scope.roomId = response.data.roomId;
        // 登陆用户会话token
        $scope.token = response.data.token;
        // 登陆用户id
        $scope.rId = response.data.rid;
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
    }, function error(response) {
        alert('出现错误\n' + response.status + ' ' + response.statusText);
    });

    $http({
        method: 'GET',
        url: 'examineesInfo.json'
    }).then(function success(response) {
        // 考生信息
        $scope.examineesInfo = response.data;
        $scope.examineeMetaInfo = {
            'seatNum': '座位号',
            'uname': '姓名',
            'gender': '性别',
            'cid': '证件号',
            'uid': '准考证号',
            'status': '状态'
        };
        // 状态码转化成易读string
        $scope.statusDisplay = ['未登录', '正在考试', '已交卷'];
        $scope.examineesStatus = {};
        $scope.selectionStatus = {};
        // 已选人数
        $scope.selectedNum = 0;
        // 未登录人数
        $scope.absentNum = 0;
        // 在考人数
        $scope.examingNum = 0;
        // 交卷人数
        $scope.completeNum = 0;
        for (x in $scope.examineesInfo) {
            switch ($scope.examineesInfo[x].status) {
                case 0:
                    $scope.absentNum += 1;
                    break;
                case 1:
                    $scope.examingNum += 1;
                    $scope.examineesStatus[$scope.examineesInfo[x].uid] = 'info';
                    break;
                case 2:
                    $scope.completeNum += 1;
                    $scope.examineesStatus[$scope.examineesInfo[x].uid] = 'success';
                    break;
                default:
                    alert('考生状态错误！');
            }
        }
        $scope.orderCondition = 'seatNum';
        $scope.isReverse = false;
    }, function error(response) {
        alert('出现错误\n' + response.status + ' ' + response.statusText);
    });

    // 监控index变量控制标签及标签页
    $scope.$watch('index', function() {
        if ($scope.index == undefined) {
            return;
        }
        for (i in $scope.active) {
            $scope.active[i] = "";
            $scope.display[i] = "none";
        };
        $scope.active[$scope.index] = "active";
        $scope.display[$scope.index] = "block";
    });


    $scope.confirm = function() {
        alert($scope.inputMessage);
        for (x in $scope.selectionStatus) {
            alert(x + ' ' + $scope.selectionStatus[x]);
        }
    }

    // 全选
    $scope.selectAll = function() {
        for (x in $scope.examineesInfo) {
            $scope.selectionStatus[$scope.examineesInfo[x].uid] = true;
        }
        $scope.selectedNum = $scope.examineesInfo.length;
    }

    //取消选择
    $scope.cancelAll = function() {
        for (x in $scope.examineesInfo) {
            $scope.selectionStatus[$scope.examineesInfo[x].uid] = false;
        }
        $scope.selectedNum = 0;
    }

    //单独选择
    $scope.checkSel = function(status) {
        if (status == true) {
            $scope.selectedNum += 1;
        }
        if (status == false) {
            $scope.selectedNum -= 1;
        }
    }

    // 排序变量
    $scope.thClick = function(value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

    // 刷新
    $scope.refresh = function() {
        $http({
            method: 'GET',
            url: 'examineesInfo.json'
        }).then(function success(response) {
            $scope.examineesInfo = response.data;
            $scope.absentNum = 0;
            $scope.examingNum = 0;
            $scope.completeNum = 0;
            for (x in $scope.examineesInfo) {
                switch ($scope.examineesInfo[x].status) {
                    case 0:
                        $scope.absentNum += 1;
                        break;
                    case 1:
                        $scope.examingNum += 1;
                        $scope.examineesStatus[$scope.examineesInfo[x].uid] = 'info';
                        break;
                    case 2:
                        $scope.completeNum += 1;
                        $scope.examineesStatus[$scope.examineesInfo[x].uid] = 'success';
                        break;
                    default:
                        alert('考生状态错误！');
                }
            }
            $scope.cancelAll();
        }, function error(response) {
            alert('刷新出错\n' + response.status + ' ' + response.statusText);
        });
    }
});

