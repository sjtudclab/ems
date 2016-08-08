angular.module('supervisor', ['ui.bootstrap']);
angular.module('supervisor').controller('supervisorCtrl', function($scope, $http,$window) {
	   
	    var infoStatus = JSON.parse($window.sessionStorage.infoStatus);
        //功能列表
        $scope.operationMetaInfo = infoStatus.authorityList;
        // 考场号
        $scope.roomId = infoStatus.roomId;
        // 登陆用户id
        $scope.rId = infoStatus.Rid;
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
  
        
        function success(response) {
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
        }
        function error(response) {
            alert('出现错误\n' + response.status + ' ' + response.statusText);
        }
        
    $http({
        method: 'GET',
        url: '/EMS/supervise/Refresh'
    }).then(success(response), error(response));

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


    $scope.confirm = function(url) {
        /*alert($scope.inputMessage);*/
    	
        for (x in $scope.selectionStatus) {
            alert(x + ' ' + $scope.selectionStatus[x]);
        }
        switch (url) {
        case "forceStop":  //强制终止
        	alert(url);
        	$http.get('/EMS/supervise/forceStop', {
                params: { token: $window.sessionStorage.token, uidList: password }
            }).then(success(response), error(response));

            break;
        case "allowStart": //允许开始
        	alert(url);
        	$http.get('/EMS/supervise/allowStart', {
                params: { token: $window.sessionStorage.token, uidList: password }
            }).then(success(response), error(response));
            break;
        case "allowStop":  //允许终止
        	alert(url);
        	$http.get('/EMS/supervise/allowStop', {
                params: { token: $window.sessionStorage.token, uidList: password }
            }).then(success(response), error(response));
            break;
        case "delay":   //延时操作
        	alert(url);
        	$http.get('/EMS/supervise/delay', {
                params: { token: $window.sessionStorage.token, uidList: password, delayTime:time }
            }).then(success(response), error(response));
            break;
        case "deleteExam": //撤销登录
        	alert(url);
        	$http.get('/EMS/supervise/deleteExam', {
                params: { token: $window.sessionStorage.token, uidList: password }
            }).then(success(response), error(response));
            break;
        case "manualAssign": //强行交卷
        	alert(url);
        	$http.get('/EMS/supervise/manualAssign', {
                params: { token: $window.sessionStorage.token, uidList: password }
            }).then(success(response), error(response));
            break;
        case "restart":  //撤销交卷
        	alert(url);
        	$http.get('/EMS/supervise/restart', {
                params: { token: $window.sessionStorage.token, uidList: password }
            }).then(success(response), error(response));
            break;
        case "roomChange":  //更换场次
        	alert(url);
            break;
        case "seatChange": //更换座位
        	alert(url);
        	$http.get('/EMS/supervise/seatChange', {
                params: { token: $window.sessionStorage.token, uid: passwordm ,seatNum:seatNum }
            }).then(success(response), error(response));
            break;
        default:
            alert('考生状态错误！');
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

