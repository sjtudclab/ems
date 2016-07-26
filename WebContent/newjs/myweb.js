var routingDemoApp=angular.module('routingDemoApp', ['ui.router','ui.bootstrap','formlogin','navbars','demo0','checkup'])
routingDemoApp.config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/index');
    $stateProvider
        .state('index', {
            url: '/index',
            views: {
                '': {
                    templateUrl: 'tpls/welcome.html'
                },
                'navbar@index': {
                    templateUrl: 'tpls/navbar.html'
                }
            }
        })
        .state('already', {
            url: '/already',
            views: {
                '': {
                    templateUrl: 'tpls/login.html'
                },
                'navbar@already': {
                    templateUrl: 'tpls/navbar.html'
                }
            }
        })
        .state('showinfo', {
            url: '/showinfo/:name/:id/:cid/:subject/:time',
            views: {
                '': {
                    templateUrl: 'tpls/showinfo.html',
                     controller: 'showCtrl'
                },
               'navbar@showinfo': {
                   templateUrl: 'tpls/navbar.html'
                }
            }
        })
        .state('main', {
            url: '/main/:active/:num/:type',
            views: {
                '': {
                    templateUrl: 'tpls/main.html',
                    controller: 'showMain'
                },
               'tab1@main': {
                   templateUrl: 'tpls/tabmain/tab11.html',
                   controller: 'skiptb1'
                },
               'tab2@main': {
                   templateUrl: 'tpls/tabmain/tab21.html',
                   controller: 'skiptb2'
                },
               'tab3@main': {
                   templateUrl: 'tpls/tabmain/tab31.html',
                   controller: 'skiptb3'
                },
               'tab4@main': {
                   templateUrl: 'tpls/tabmain/tab41.html',
                   controller: 'skiptb4'
                },
                'info@main': {
                    templateUrl: 'tpls/info.html'
                 },
                 'time@main': {
                     templateUrl: 'tpls/time.html'
                  }
              
            }
        })
        .state('checkup', {
            url: '/checkup',
            views: {
                '': {
                    templateUrl: 'tpls/checkup.html'
                },
               'Ctab1@checkup': {
                   templateUrl: 'tpls/checktab/Ctab1.html'
                },
               'Ctab2@checkup': {
                   templateUrl: 'tpls/checktab/Ctab2.html'
                },
               'Ctab3@checkup': {
                   templateUrl: 'tpls/checktab/Ctab3.html'
                },
               'Ctab4@checkup': {
                   templateUrl: 'tpls/checktab/Ctab4.html'
                },
                'time@checkup': {
                    templateUrl: 'tpls/time.html'
                 }
            }
        })
});
