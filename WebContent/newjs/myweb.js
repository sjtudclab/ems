var routingDemoApp = angular.module('routingDemoApp', [ 'ui.router',
		'ui.bootstrap', 'formlogin', 'demo0', 'checkup', 'supervisor','manager','examManage'])
routingDemoApp.config(function($stateProvider, $urlRouterProvider) {

	$stateProvider.state('index', {
		url : '/index',
		views : {
			'' : {
				templateUrl : 'tpls/login.html'
			},
			'navbar@index' : {
				templateUrl : 'tpls/navbar.html'
			}
		}
	}).state('already', {
		url : '/already',
		views : {
			'' : {
				templateUrl : 'tpls/welcome.html'
			},
			'navbar@already' : {
				templateUrl : 'tpls/navbar.html'
			}
		}
	}).state('showinfo', {
		url : '/showinfo',
		views : {
			'' : {
				templateUrl : 'tpls/showinfo.html',
				controller : 'showCtrl'
			},
			'navbar@showinfo' : {
				templateUrl : 'tpls/navbar.html'
			}
		}
	}).state('supervisor', {
		url : '/supervisor',
		views : {
			'' : {
				templateUrl : 'tpls/supervisor.html'
			},
			'navbar@supervisor' : {
				templateUrl : 'tpls/navbar.html'
			},
			'forceStop@supervisor' : {
				templateUrl : 'tpls/supervi/forceStop.html'
			},
			'allowStart@supervisor' : {
				templateUrl : 'tpls/supervi/allowStart.html'
			},
			'allowStop@supervisor' : {
				templateUrl : 'tpls/supervi/allowStop.html'
			},
			'delay@supervisor' : {
				templateUrl : 'tpls/supervi/delay.html'
			},
			'deleteExam@supervisor' : {
				templateUrl : 'tpls/supervi/deleteExam.html'
			},
			'manualAssign@supervisor' : {
				templateUrl : 'tpls/supervi/manualAssign.html'
			},
			'restart@supervisor' : {
				templateUrl : 'tpls/supervi/restart.html'
			},
			'roomChange@supervisor' : {
				templateUrl : 'tpls/supervi/roomChange.html'
			},
			'seatChange@supervisor' : {
				templateUrl : 'tpls/supervi/seatChange.html'
			}
		}
	}).state('manager', {
		url : '/manager',
		views : {
			'' : {
				templateUrl : 'tpls/manager.html'
			},
			'navbar@manager' : {
				templateUrl : 'tpls/navbar.html'
			},
			'importExam@manager' : { //导入试卷
				templateUrl : 'tpls/manager/importExam.html'
			},
			'importStuArrangement@manager' : { //导入考生安排
				templateUrl : 'tpls/manager/importStuArrangement.html'
			},
			'roomArrangement@manager' : {  //考场管理
				templateUrl : 'tpls/manager/roomManager.html'
			},
			'exportExam@manager' : { //导出试卷
				templateUrl : 'tpls/manager/exportExam.html'
			},
			'systemManagement@manager' : {  //系统管理
				templateUrl : 'tpls/manager/systemManagement.html'
			}
			
		}
	}).state('examImport', {
		url : '/examImport',
		views : {
			'' : {
				templateUrl : 'tpls/examImport.html'
			},
			'navbar@examImport' : {
				templateUrl : 'tpls/navbar.html'
			},
			'subject@examImport' : {
				templateUrl : 'tpls/examManage/subjectImport.html'
			},
			'single@examImport' : {
				templateUrl : 'tpls/examManage/singleImport.html'
			},
			'multiple@examImport' : {
				templateUrl : 'tpls/examManage/multipleImport.html'
			},
			'judge@examImport' : {
				templateUrl : 'tpls/examManage/judgeImport.html'
			},
			'match@examImport' : {
				templateUrl : 'tpls/examManage/matchImport.html'
			},
			'simple@examImport' : {
				templateUrl : 'tpls/examManage/simpleImport.html'
			}
			
		}
	}).state('main', {
		url : '/main/:active/:num/:type',
		views : {
			'' : {
				templateUrl : 'tpls/main.html',
				controller : 'showMain'
			},
			'tab1@main' : {
				templateUrl : 'tpls/tabmain/tab11.html',
				controller : 'skiptb1'
			},
			'tab2@main' : {
				templateUrl : 'tpls/tabmain/tab21.html',
				controller : 'skiptb2'
			},
			'tab3@main' : {
				templateUrl : 'tpls/tabmain/tab31.html',
				controller : 'skiptb3'
			},
			'tab4@main' : {
				templateUrl : 'tpls/tabmain/tab41.html',
				controller : 'skiptb4'
			},
			'tab5@main' : {
				templateUrl : 'tpls/tabmain/tab51.html',
				controller : 'skiptb5'
			},
			'tab6@main' : {
				templateUrl : 'tpls/tabmain/fillGap.html',
				controller : 'skiptb6'
			},
			'tab7@main' : {
				templateUrl : 'tpls/tabmain/machine.html',
				controller : 'skiptb7'
			},
			'info@main' : {
				templateUrl : 'tpls/info.html',
				controller : 'showinfo'
			},
			'time@main' : {
				templateUrl : 'tpls/time.html',
				controller : 'timeinfo'
			}

		}
	}).state('checkup', {
		url : '/checkup',
		views : {
			'' : {
				templateUrl : 'tpls/checkup.html'
			},
			'Ctab1@checkup' : {
				templateUrl : 'tpls/checktab/Ctab1.html',
				controller : 'Ctab1'
			},
			'Ctab2@checkup' : {
				templateUrl : 'tpls/checktab/Ctab2.html',
				controller : 'Ctab2'
			},
			'Ctab3@checkup' : {
				templateUrl : 'tpls/checktab/Ctab3.html',
				controller : 'Ctab3'
			},
			'Ctab4@checkup' : {
				templateUrl : 'tpls/checktab/Ctab4.html',
				controller : 'Ctab4'
			},
			'Ctab5@checkup' : {
				templateUrl : 'tpls/checktab/Ctab5.html',
				controller : 'Ctab5'
			},
			'Ctab6@checkup' : {
				templateUrl : 'tpls/checktab/Ctab6.html',
				controller : 'Ctab6'
			},
			'Ctab7@checkup' : {
				templateUrl : 'tpls/checktab/Ctab7.html',
				controller : 'Ctab7'
			},
			'time@checkup' : {
				templateUrl : 'tpls/time.html',
				controller : 'timeinfo'
			},
			'info@checkup' : {
				templateUrl : 'tpls/info.html',
				controller : 'showinfo'

			}
		}
	}).state('finish', {
		url : '/finish/:score',
		views : {
			'' : {
				templateUrl : 'tpls/finish.html'
			}
		}
	}).state('examManage', {
		url : '/examManage',
		views : {
			'' : {
				templateUrl : 'tpls/examManage/examManage.html'
			},
			'navi@examManage' : {
				templateUrl : 'tpls/examManage/navbar.html'
			},
			'tab1@examManage' : {
				templateUrl : 'tpls/examManage/tab/examImport.html'
			},
			'tab2@examManage' : {
				templateUrl : 'tpls/examManage/tab/tab2.html'
			},
			'stu1@examManage' : {
				templateUrl : 'tpls/examManage/tab/stuImport.html'
			},
			'stu2@examManage' : {
				templateUrl : 'tpls/examManage/tab/stuInput.html'
			},
			'room1@examManage' : {
				templateUrl : 'tpls/examManage/tab/roomImport.html'
			},
			'room2@examManage' : {
				templateUrl : 'tpls/examManage/tab/roomInput.html'
			},
			'stuExam1@examManage' : {
				templateUrl : 'tpls/examManage/tab/stuExam.html'
			},
			'stuRoom1@examManage' : {
				templateUrl : 'tpls/examManage/tab/stuRoom.html'
			},
			'setSystem1@examManage' : {
				templateUrl : 'tpls/examManage/tab/setSystem.html'
			}
			
		}
	});

	$urlRouterProvider.otherwise('/index');
});
