//manageController依赖于指令模块
var manageController = angular.module('manageController', ['manageDirective', 'manageService']);

/**
 * manage页面的父控制器，在这儿配置一些全局都需要使用的东西
 * 
 */
manageController.controller('manageController', function($scope) {

    //将菜单控制定义为对象，以实现双向同步
    $scope.pathTips = {
        firstTips: "首页",
        secondTips: ""
    };



});


/**
 * 左侧菜单控制器
 * @param  {[type]} $scope) {               $scope.setFirstTips [description]
 * @return {[type]}         [description]
 */
manageController.controller('leftMenuController', ['$scope', function($scope) {
    $scope.setFirstTips = function setFirstTips(event) {
        $scope.pathTips.firstTips = $(event.target).text(); //转换成jQuery对象！！
    };

    $scope.setSecondTips = function setSecondTips(event) {

        $scope.pathTips.firstTips = $(event.target).parent().parent().prev("a").text();
        $scope.pathTips.secondTips = $(event.target).text(); //转换成jQuery对象！！
    };
}]);

//控制安卓异常反馈的表格显示与操作
//这是一个指令的学习例子，感觉这样使用不太对，后期再调整。
manageController.controller('androidExceptionController', ['$scope', 'androidExceptionService', 'myAlertService', function($scope, androidExceptionService, myAlertService) {

    //为指令注入配置
    $scope.configs = {
        "tableTitleConfig": [{
            "width": "5%",
            "title": "数量"
        }, {
            "width": "10%",
            "title": "异常"
        }, {
            "width": "10%",
            "title": "时间"
        }, {
            "width": "10%",
            "title": "操作"
        }],
        "operateShow": {
            "edit": true,
            "copy": false,
            "delete": true
        }
    };

    $scope.table = {
        totalOperate: false
    };

    $scope.contents = [];

    //通过androidExceptionService获取列表     注意：传入$scope用于更新contents
    $scope.refresh = function() {
        androidExceptionService.getFileInfoList($scope);
    };

    /**
     * 删除一列服务端文件  
     * //获取超链接作为参数：
        //1. 通过event获取jquery对象，然后按照jQuery方法获取。
        //2. 通过数据绑定直接传递进来。  第二种好
        // console.log(fileName);
     */
    $scope.delete = function(fileName) {
        androidExceptionService.deleteFile(fileName, $scope);
    };



    /**
     * 删除所有选中的文件(做成指令？？)
     * @return {[type]} [description]
     */
    $scope.deleteChecked = function() {
        var filesName = new Array();
        var checkedInputs = $("#exceptionTable").children("tbody").find("input:checked");

        //使用jQuery的遍历函数
        checkedInputs.each(function() {
            var fileName = $(this).attr("fileName");
            if (fileName !== undefined && fileName !== null) {
                filesName.push(fileName);
            }
        });

        $scope.delete(filesName);
    };

    //每次默认刷新，显示服务器上的异常文件
    $scope.refresh();

}]);

//这个控制器绑定alert服务的数据，alert服务的数据可以在其他控制器中修改
manageController.controller('androidExceptionAlertController', ['$scope', 'myAlertService', function($scope, myAlertService) {
    //绑定服务数据
    $scope.alerts = myAlertService.getAlerts();

}]);


manageController.controller('loginController', ['$scope', 'myAlertService', 'loginService', function($scope, myAlertService, loginService) {

    //绑定服务数据
    $scope.alerts = myAlertService.getAlerts();

    $scope.login = function(param) {

        loginService.login(param);
    };

}]);

manageController.controller('topicTableController', ['$scope', '$stateParams', 'myAlertService', 'topicService', function($scope, $stateParams, myAlertService, topicService) {

    $scope.paginate = topicService.paginate;
    //包含的模板
    $scope.templates = {
        topicDetail: '../manage/topic/topicDetail.html'
    };


    //点击时设置选中的topicId userId
    $scope.setSelectTopic = function(topicId, userId) {
        topicService.selectTopic.topicId = topicId;
        topicService.selectTopic.userId = userId;
    };

    //绑定函数
    $scope.refresh = function() {
        topicService.refresh($scope);
    };

    $scope.deleteCheckedTopic = function(topicId) {
        topicService.deleteCheckedTopic(topicId, $scope);
        $scope.refresh();
    };

    $scope.deleteCheckedTopics = function() {
        var topicIdList = [];
        var checkedInputs = $("#topicTable").children("tbody").find("input:checked");

        //使用jQuery的遍历函数
        checkedInputs.each(function() {
            var topicId = $(this).attr("topicId");
            if (topicId !== undefined && topicId !== null) {
                topicIdList.push(topicId);
            }
        });

        topicService.deleteCheckedTopics(topicIdList);
        $scope.refresh();
    };

    //自动执行
    $scope.refresh();

}]);

manageController.controller('topicDetailController', ['$scope', 'myAlertService', 'topicService','replyService', function($scope, myAlertService, topicService,replyService) {



    //获取topic的详细信息
    $scope.getTopicById = function() {
        topicService.getTopicById($scope);
    };

    //通过replyId删除回复
    $scope.deleteReply = function(replyId) {
        replyService.deleteReply(replyId);
        topicService.getTopicById($scope);
    };

    $scope.getTopicById();

}]);
