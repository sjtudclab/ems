var supervisor = angular.module('supervisor', [])

supervisor.controller('TabsCtrl', function($scope) {
    $scope.operationMetaInfo = ['强制终止', '允许开始', '允许终止', '更换座位', '删除试卷', '延时操作', '更换场次',
        '回到试卷', '交卷操作'
    ];
    $scope.active = [];
    $scope.display = [];
    $scope.index = 0;
    for (i in $scope.operationMetaInfo) {
        $scope.active[i] = "";
        $scope.display[i] = "none";
    }
    $scope.$watch('index', function() {
        for (i in $scope.active) {
            $scope.active[i] = "";
            $scope.display[i] = "none";
        };
        $scope.active[$scope.index] = "active";
        $scope.display[$scope.index] = "block";
    });

    $scope.sel = function(index) {
        $scope.index = index;
    };

    $scope.studentMetaInfo = ['ID', '姓名', '考号', '状态'];
    $scope.studentsInfo = [{
        id: 1001,
        name: 'a',
        code: '001',
        status: 0
    }, {
        id: 1002,
        name: 'b',
        code: '002',
        status: 1
    }, {
        id: 1003,
        name: 'c',
        code: '003',
        status: 1
    }, {
        id: 1004,
        name: 'd',
        code: '004',
        status: 0
    }, {
        id: 1005,
        name: 'e',
        code: '005',
        status: 1
    }, {
        id: 1006,
        name: 'f',
        code: '006',
        status: 1
    }, {
        id: 1007,
        name: 'g',
        code: '007',
        status: 0
    }, {
        id: 1005,
        name: 'e',
        code: '005',
        status: 1
    }, {
        id: 1006,
        name: 'f',
        code: '006',
        status: 1
    }, {
        id: 1007,
        name: 'g',
        code: '007',
        status: 0
    }, {
        id: 1005,
        name: 'e',
        code: '005',
        status: 1
    }, {
        id: 1006,
        name: 'f',
        code: '006',
        status: 1
    }, {
        id: 1007,
        name: 'g',
        code: '007',
        status: 0
    }, {
        id: 1005,
        name: 'e',
        code: '005',
        status: 1
    }, {
        id: 1006,
        name: 'f',
        code: '006',
        status: 1
    }, {
        id: 1007,
        name: 'g',
        code: '007',
        status: 0
    }];

    $scope.inputMessage = 'elvin';
    $scope.studentsStatus = {};
    $scope.selectionStatus = {};
    $scope.selectedNum = 0;

    for (x in $scope.studentsInfo) {
        if ($scope.studentsInfo[x].status == 1) {
            $scope.studentsStatus[$scope.studentsInfo[x].id] = 'success';
        }
    }

    $scope.confirm = function() {
        alert($scope.inputMessage);
        for (x in $scope.selectionStatus) {
            alert(x + ' ' + $scope.selectionStatus[x]);
        }
    }

    $scope.selectAll = function() {
        for (x in $scope.studentsInfo) {
            $scope.selectionStatus[$scope.studentsInfo[x].id] = true;
        }
        $scope.selectedNum = $scope.studentsInfo.length;
    }
    $scope.cancelAll = function() {
        for (x in $scope.studentsInfo) {
            $scope.selectionStatus[$scope.studentsInfo[x].id] = false;
        }
        $scope.selectedNum = 0;
    }
    $scope.checkSel = function(status) {
        if (status == true) {
            $scope.selectedNum += 1;
        }
        if (status == false) {
            $scope.selectedNum -= 1;
        }
    }
});

