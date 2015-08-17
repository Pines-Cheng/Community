/**
 *  Module
 *angular.module可在全局位置创建、注册、获取Angular模块。
 *所有模块（angular核心或第三方）都必须使用这个机制注册才能在应用中生效。
 * Description
 */
var manage = angular.module('manage', ['ngRoute', 'ngAnimate', 'ui.router', 'manageController', 'manageService']);

manage.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/contentManage');

    //topicManage/show 显示帖子  topicTable.html
    //topicManage/comment 显示详情及评论 topicDetail.html
    $stateProvider.state('topic', {
            url: '/topic/:operate?pageNumber',
            views: {
                'content': {
                    //要使用[param,function(){}]的形式，必须使用templateProvider
                    templateUrl: function($stateParams) {
                        console.dirxml($stateParams);
                        return '../manage/topic/' + $stateParams.operate + '.html';
                    }
                }
            }

        })
        .state('contentManage', {
            url: '/contentManage',
            views: {
                'content': {
                    templateUrl: '../manage/contentManage.html'
                }
            }
        }).state('logManage', {
            url: '/logManage',
            views: {
                'content': {
                    templateUrl: '../manage/logManage.html'
                }
            }
        }).state('androidExceptionManage', {
            url: '/androidExceptionManage',
            views: {
                'content': {
                    templateUrl: '../manage/androidExceptionManage.html'
                }
            }
        });


}]);
