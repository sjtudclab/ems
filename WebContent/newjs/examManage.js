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

examManage.controller('examManageCtrl', function ($scope, $http, $window) {


    $scope.operationMetaInfo = ['试卷管理', '考生管理', '考场管理', '考生试卷安排', '考生考场安排', '系统管理'];
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
        switch ($scope.index) {
            case 0:
                $scope.problemMetaInfo = ['试卷导入', '试卷录入'];
                $scope.tab = "tab";
                break;
            case 1:
                $scope.problemMetaInfo = ['考生导入', '考生录入'];
                $scope.tab = "stu";
                break;
            case 2:
                $scope.problemMetaInfo = ['考场导入', '考场录入'];
                $scope.tab = "room";
                break;
            case 3:
                $scope.problemMetaInfo = ['考生试卷安排'];
                $scope.tab = "stuExam";
                break;
            case 4:
                $scope.problemMetaInfo = ['考生考场安排'];
                $scope.tab = "stuRoom";
                break;
            case 5:
                $scope.problemMetaInfo = ['系统管理'];
                $scope.tab = "setSystem";
                break;
        }

    });
});
examManage.controller('TabsDCtrl', function ($scope, $rootScope) {

    $scope.active = [];
    $scope.display = [];
    $scope.color = [];
    $scope.index = 0;
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

        $scope.active[$scope.index] = "active";
        $scope.display[$scope.index] = "block";

        switch ($scope.index) {

            case 0:
                $scope.color[$scope.index] = "rgba(88,178,220,.5)";
                break;
            case 1:
                $scope.color[$scope.index] = "rgba(204,204,255,.6)";
                break;
            default:
                alert('error');

        };
    });

    $scope.sel = function (index) {
        $scope.index = index;
    };

});

examManage.controller('examImportCtrl', function ($scope, $http, $window) {
    //编辑
    $scope.edit = function () {
        $scope.sel(1);

    }

    //控制导入文件
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
            url: 'form',
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
    //控制表格内容
    // $scope.examineesInfo = response.data;
    $scope.examineesInfo = [{
        'examNum': 'a1',
        'number': '1',
        'content': 'sdhkjsdhf',
        'answer': 'hahah',
        'point': '6',
        'filePic': 'file.img',
        'fileVe': 'ss.video',
        'fileRR': 'ss.audio'

    }, {
            'examNum': 'a1',
            'number': '1',
            'content': 'sdhkjsdhf',
            'answer': 'hahah',
            'point': '6',
            'filePic': 'file.img',
            'fileVe': 'ss.video',
            'fileRR': 'ss.audio'

        }]

    $scope.examineeMetaInfo = {
        'examNum': '试卷号',
        'number': '序号',
        'content': '题干',
        'answer': '参考答案',
        'point': '分值',
        'filePic': '图片文件',
        'fileVe': '视频文件',
        'fileRR': '音频文件'

    };


    $scope.orderCondition = 'seatNum';
    $scope.isReverse = false;

    // 排序变量
    $scope.thClick = function (value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

});
examManage.controller('stuImportCtrl', function ($scope, $http,$rootScope, $window) {
    //编辑
    $scope.edit = function (name,gender,id,subject,subjectNum) {
        $scope.sel(1);
        $rootScope.stuName=name ;
        $rootScope.stuGender=gender ;
        $rootScope.stuId=id;
        $rootScope.stuSubject=subject;
        $rootScope.stuSubNum=subjectNum ;

    }

    //控制导入文件
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
            url: 'form',
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
    //控制表格内容
    // $scope.examineesInfo = response.data;
    $scope.examineesInfo = [{
        'name': '小宋',
        'gender': '男',
        'id': '87798',
        'picture': '1.jpg',
        'subject': '政治',
        'subjectNum': 'de'

    }, {
            'name': '小李',
            'gender': '男',
            'id': '5687687',
            'picture': '2.jpg',
            'subject': '地理',
            'subjectNum': 'e3'

        }]

    $scope.examineeMetaInfo = {
        'name': '姓名',
        'gender': '性别',
        'id': '证件号',
        'picture': '照片',
        'subject': '科目',
        'subjectNum': '科目编号'

    }


    $scope.orderCondition = 'name';
    $scope.isReverse = false;

    // 排序变量
    $scope.thClick = function (value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

});
examManage.controller('stuInputCtrl', function ($scope, $http, $rootScope, $window, roomManage) {

    // $scope.$watch("roomManage.ipAdd", function (newVal, oldVal) {
    //     if (newVal !== oldVal) {
    //  alert("ahha"+roomManage.roomName);

    //     $scope.roomName = roomManage.roomName;
    //     $scope.seatName = roomManage.seatName;
    //     $scope.ipAdd = roomManage.ipAdd;
    // }

    // }, true);
    $scope.save = function () {
        alert($scope.roomName + $scope.seatName);
        $rootScope.stuName = '';
        $rootScope.stuGender = '';
        $rootScope.stuId = '';
        $rootScope.stuSubject = '';
        $rootScope.stuSubNum = '';



    }


});
examManage.factory('roomManage', function () {
    return {
        roomName: '',
        seatName: '',
        ipAdd: ''

    };
})
examManage.controller('roomImportCtrl', function ($scope, $http,$rootScope, $window, roomManage) {
    //编辑
    $scope.edit = function (roomNum, seatNum, ip) {
        // roomManage.roomName = roomNum;
        // roomManage.seatName = seatNum;
        // roomManage.ipAdd = ip;
        $scope.sel(1);

        $rootScope.roomName = roomNum;
        $rootScope.seatName = seatNum;
        $rootScope.ipAdd = ip;
        // alert(roomManage.seatName);

    }

    //控制导入文件
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
            url: 'form',
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
    //控制表格内容
    // $scope.examineesInfo = response.data;
    $scope.examineesInfo = [{
        'roomNum': '东中院3-213',
        'seatNum': '7',
        'ip': '192.168.3.234'

    }, {
            'roomNum': '东中院1-311',
            'seatNum': '5',
            'ip': '192.168.0.343'

        }]

    $scope.examineeMetaInfo = {
        'roomNum': '考场名',
        'seatNum': '座位号',
        'ip': 'IP'

    }


    $scope.orderCondition = 'roomNum';
    $scope.isReverse = false;

    // 排序变量
    $scope.thClick = function (value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

});
examManage.controller('roomIputCtrl', function ($scope, $http, $rootScope, $window, roomManage) {

    // $scope.$watch("roomManage.ipAdd", function (newVal, oldVal) {
    //     if (newVal !== oldVal) {
    //  alert("ahha"+roomManage.roomName);

    //     $scope.roomName = roomManage.roomName;
    //     $scope.seatName = roomManage.seatName;
    //     $scope.ipAdd = roomManage.ipAdd;
    // }

    // }, true);
    $scope.save = function () {
        alert($scope.roomName + $scope.seatName);
        $rootScope.roomName = '';
        $rootScope.seatName = '';
        $rootScope.ipAdd = '';



    }


});

examManage.controller('stuExamCtrl', function ($scope, $http, $window) {

    //控制表格内容
    // $scope.examineesInfo = response.data;


    $scope.examineeMetaInfo = {
        'name': '姓名',
        'id': '证件号',
        'subject': '科目名称'

    }


    $scope.orderCondition = 'name';
    $scope.isReverse = false;

    // 排序变量
    $scope.thClick = function (value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

    //选择相应科目
    $scope.sumSub = [{
        "subName": "政治",
        "subNum": ["a1", "a2"]
    }, {
            "subName": "英语",
            "subNum": ["b1", "b2"]
        }, {
            "subName": "数学",
            "subNum": ["c1", "c2"]
        }];
    $scope.selectSubject = function (selectedSub) {

        switch (selectedSub.subName) {
            case '政治':
                $scope.subNumlists = selectedSub.subNum;
                $scope.examineesInfo = [{
                    'name': '李煜',
                    'id': '678689',
                    'subject': '政治'

                }, {
                        'name': '李静',
                        'id': '678689',
                        'subject': '政治'

                    }]
                break;
            case '英语':
                $scope.subNumlists = selectedSub.subNum;
                $scope.examineesInfo = [{
                    'name': '李煜',
                    'id': '678689',
                    'subject': '英语'

                }, {
                        'name': '李静',
                        'id': '678689',
                        'subject': '英语'

                    }]
                break;
            case '数学':
                $scope.subNumlists = selectedSub.subNum;
                break;

        }
    }


});
examManage.controller('stuRoomCtrl', function ($scope, $http, $window) {

    //控制表格内容
    $scope.selectionStatus = [];
    // 全选
    $scope.selectAll = function () {
        for (x in $scope.examineesInfo) {
            $scope.selectionStatus[$scope.examineesInfo[x].id] = true;
        }
    }

    // 取消选择
    $scope.cancelAll = function () {
        for (x in $scope.examineesInfo) {
            $scope.selectionStatus[$scope.examineesInfo[x].id] = false;
        }
    }

    // $scope.examineesInfo = response.data;

    $scope.examineesInfo = [{
        'name': '李煜',
        'id': '678689',
        'subject': '政治'

    }, {
            'name': '李静',
            'id': '6789',
            'subject': '政治'

        }, {
            'name': '于一',
            'id': '678349',
            'subject': '政治'

        }]
    $scope.examineeMetaInfo = {
        'name': '姓名',
        'id': '证件号',
        'subject': '科目名称'

    }


    $scope.orderCondition = 'name';
    $scope.isReverse = false;

    // 排序变量
    $scope.thClick = function (value) {
        $scope.orderCondition = value;
        $scope.isReverse = !$scope.isReverse;
    }

    //选择相应科目
    $scope.sumSub = [{
        "subName": "东中院1-101",
        "subNum": ["a1", "a2"]
    }, {
            "subName": "东中院1-103",
            "subNum": ["b1", "b2"]
        }, {
            "subName": "东中院1-104",
            "subNum": ["c1", "c2"]
        }];
    $scope.selectSubject = function (selectedSub) {
        var uidList = [];
        for (x in $scope.selectionStatus) {

            if ($scope.selectionStatus[x]) {
                uidList.push(x);
            }
        }
        alert(selectedSub.subName);
        alert(uidList);

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
    var choice = {};
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
                choice: choice

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