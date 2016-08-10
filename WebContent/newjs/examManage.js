var examManage = angular.module('examManage', ['ui.bootstrap']);
examManage.controller('examManagerCtrl', function ($scope, $http) {
    $scope.isCollapsed = false;


    $http({
        method: 'GET',
        url: 'problem.json',



    })
        .then(
        function success(response) {
            // 功能列表
            $scope.operationMetaInfo = response.data.authorityList;

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
        })

    // $scope.operationMetaInfo = ['单选题', '多选题', '判断题', '匹配题', '简答题'];
    // // 控制标签显示
    // $scope.active = [];
    // // 控制标签页显示
    // $scope.display = [];
    // for (i in $scope.operationMetaInfo) {
    //     $scope.active[i] = "";
    //     $scope.display[i] = "none";
    // }
    // // 初始化显示标签0
    // $scope.index = 0;

    $scope.importIcon = 'glyphicon glyphicon-menu-right';
    $scope.displayImport = 'none';


    // 监控index变量控制标签及标签页
    $scope.$watch('index', function () {
        if ($scope.index == undefined) {
            return;
        }
        for (i in $scope.active) {
            $scope.active[i] = "";
            $scope.display[i] = "none";
        }
        $scope.active[$scope.index] = "active";
        $scope.display[$scope.index] = "block";
    });

    $scope.toggleImport = function () {
        if ($scope.displayImport == 'none') {
            $scope.displayImport = 'block';
            $scope.importIcon = 'glyphicon glyphicon-menu-down';
        } else {
            $scope.displayImport = 'none';
            $scope.importIcon = 'glyphicon glyphicon-menu-right';
        }
    }


});

examManage.controller('singleCtrl', function ($scope, $http) {
    $scope.lists = ['A', 'B', 'C', 'D'];
    $scope.options = ['A', 'B', 'C', 'D'];
    $scope.op = [];

    $scope.save = function () {
        alert($scope.content + $scope.op);

    }


});
examManage.controller('judgeCtrl', function ($scope, $http) {
    $scope.lists = ['A', 'B'];
    $scope.options = ['A', 'B'];
    $scope.op = [];

    $scope.save = function () {
        alert($scope.content + $scope.op);

    }


});
examManage.controller('multipleCtrl', function ($scope, $http) {
    $scope.options = ['A', 'B', 'C', 'D'];
    $scope.op = [];
    $scope.rightOption = [];


    $scope.save = function () {
        alert($scope.content + $scope.op + $scope.rightOption);

    }


});

examManage.controller('mpCtrl', function ($scope) {
    $scope.itemMessage = [''];
    $scope.answerMessage = [''];
    $scope.showMessage = function () {
        console.log('stem: ' + $scope.stem);
        for (x in $scope.itemMessage) {
            console.log($scope.itemMessage[x] + ' ' + $scope.answerMessage[x]);
        }
    }
});