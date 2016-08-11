var examManage = angular.module('examManage', ['ui.bootstrap']);
examManage.controller('examManagerCtrl', function ($scope, $http, $window) {
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
            // // 初始化显示标签0
            // $scope.index = 0;
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

examManage.controller('singleCtrl', function ($scope, $http, $window) {
    $scope.itemMessage = ['', '', '', ''];
    $scope.rightAnswer = [];

    $scope.save = function () {
     
        var choice = {};
        for (x in $scope.itemMessage) {
            choice[x * 1 + 1] = $scope.itemMessage[x];
        }
        // console.log(choice);
        // console.log($scope.rightAnswer);
        $http({
            method: 'GET',
            url: '/EMS/admin/addTopic',
            params: {
                token: $window.sessionStorage.stoken,
                typeId: 0,
                content: $scope.content,
                choice: choice,
                List: $scope.rightAnswer

            }
        }).then(function success(response) {
            alert("保存成功！");
        })

    }
    $scope.delete = function () {
        $scope.itemMessage = ['', '', '', ''];
        $scope.rightAnswer = [];
        $scope.content = [];

    }


});
examManage.controller('judgeCtrl', function ($scope, $http, $window) {
    $scope.itemMessage = ['', ''];
    $scope.rightAnswer = [];

    $scope.save = function () {
      
        var choice = {};
        for (x in $scope.itemMessage) {
            choice[x * 1 + 1] = $scope.itemMessage[x];
        }
        $http({
            method: 'GET',
            url: '/EMS/admin/addTopic',
            params: {
                token: $window.sessionStorage.stoken,
                typeId: 2,
                content: $scope.content,
                choice: choice,
                List: $scope.rightAnswer

            }
        }).then(function success(response) {
            alert("保存成功！");
        })

    }
    $scope.delete = function () {
        $scope.itemMessage = ['', ''];
        $scope.rightAnswer = [];
        $scope.content = [];

    }


});
examManage.controller('multipleCtrl', function ($scope, $http, $window) {
    $scope.right = [];
    $scope.itemMessage = ['', '', '', ''];
    var List = [];
    $scope.save = function () {
      
        var choice = {};
        for (x in $scope.itemMessage) {
            choice[x] = $scope.itemMessage[x];
            if ($scope.right[x]) {
                List.push(x);
            }

        }
     
        $http({
            method: 'GET',
            url: '/EMS/admin/addTopic',
            params: {
                token: $window.sessionStorage.stoken,
                typeId: 1,
                content: $scope.content,
                choice: choice,
                List: List

            }
        }).then(function success(response) {
            alert("保存成功！");
        })

    }
    $scope.delete = function () {
        $scope.itemMessage = ['', '', '', ''];
        $scope.right = [];
        $scope.content = [];

    }


});

examManage.controller('mpCtrl', function ($scope, $http, $window) {
    $scope.itemMessage = ['', ''];
    $scope.answerMessage = ['', ''];
    var choice={};
    $scope.save = function () {
        for (x in $scope.itemMessage) {
            choice[x] = $scope.answerMessage[x];
        }

        $http({
            method: 'GET',
            url: '/EMS/admin/addTopic',
            params: {
                token: $window.sessionStorage.stoken,
                typeId: 3,
                content: $scope.content,
                item: $scope.itemMessage,
                List: $scope.answerMessage,
                choice:choice

            }
        }).then(function success(response) {
            alert("保存成功！");
        })

    }
    $scope.delete = function () {
        $scope.itemMessage = ['', ''];
        $scope.answerMessage = ['', ''];
        $scope.content = [];

    }
});

examManage.controller('simpleCtrl', function ($scope, $http, $window) {

    $scope.save = function () {
        $http({
            method: 'GET',
            url: '/EMS/admin/addTopic',
            params: {
                token: $window.sessionStorage.stoken,
                typeId: 4,
                content: $scope.content

            }
        }).then(function success(response) {
            alert("保存成功！");
        })

    }
    $scope.delete = function () {
        $scope.content = [];

    }
});

examManage.controller('subjectCtrl', function ($scope, $http, $window) {
 
    $scope.save = function () {
       
        var choice = {};
        if ($scope.singleCheckbox) {
            choice[0] = $scope.singleScore;
        }
        if ($scope.multipleCheckbox) {
            choice[1] = $scope.multiScore + ',' + $scope.multifulScore;
        }
        if ($scope.judgeCheckbox) {
            choice[2] = $scope.judgeScore;
        }
        if ($scope.matchCheckbox) {
            choice[3] = $scope.matchScore;
        }
        if ($scope.simpleCheckbox) {
            choice[4] = $scope.simpleScore;
        }
    
        $http({
            method: 'GET',
            url: '/EMS/admin/addSubject',
            params: {
                token: $window.sessionStorage.stoken,
                name: $scope.name,
                duration: $scope.duration,
                earliestSubmit: $scope.earliestSubmit,
                latestLogin: $scope.latestLogin,
                map: choice

            }
        }).then(function success(response) {
            alert("保存成功！");
        })

    }
    $scope.delete = function () {
        $scope.name = [];
        $scope.duration = [];
        $scope.earliestSubmit = [];
        $scope.latestLogin = [];
        $scope.singleCheckbox = false;
        $scope.singleScore = [];
        $scope.multipleCheckbox = false;
        $scope.multifulScore = [];
        $scope.multiScore = [];
        $scope.judgeCheckbox = false;
        $scope.judgeScore = [];
        $scope.matchCheckbox = false;
        $scope.matchScore = [];
        $scope.simpleCheckbox = false;
        $scope.simpleScore = [];

    }
});