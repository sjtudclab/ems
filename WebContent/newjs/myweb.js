var routingDemoApp=angular.module('routingDemoApp', ['ui.router','ui.bootstrap','formlogin','navbars','ui.bootstrap.demo'])
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
            url: '/main',
            views: {
                '': {
                    templateUrl: 'tpls/main.html'
                },
               'tab1@main': {
                   templateUrl: 'tpls/tabmain/tab1.html'
                },
               'tab2@main': {
                   templateUrl: 'tpls/tabmain/tab2.html'
                },
               'tab3@main': {
                   templateUrl: 'tpls/tabmain/tab3.html'
                },
               'tab4@main': {
                   templateUrl: 'tpls/tabmain/tab4.html'
                }
            }
        })
});
