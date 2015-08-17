var manageService = angular.module('manageService', []);

manageService.factory('androidExceptionService', ['$http', 'myAlertService', function($http, myAlertService) {

    //不返回的内容依赖注入后也无法获取
    var getFileInfoListURL = "/Community/manage/exception/show";
    var deleteFileURL = "/Community/manage/exception/delete";


    function getFileInfoList($scope) {

        $http.get(getFileInfoListURL).success(function(data, status) {
            myAlertService.successDataInterceptor(data); //显示错误、跳转等
            if (data.stat === 'OK' && data.fileInfoList.length === 0) {
                myAlertService.showAlert("提示：服务器暂时无异常！", "secondary", "false", 1000);
            }
            if (data.stat === 'OK' && data.fileInfoList.length !== 0) {
                $scope.contents = data.fileInfoList;
            }
        }).error(function(data, status) {
            myAlertService.showAlert("警告：获取服务器数据失败！", "secondary", "false", 1000);
        });
    }

    function deleteFile(fileName, $scope) {
        $http.post(deleteFileURL, $.param({
            "fileName": fileName
        }), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            }
        }).success(function(data, status) {
            myAlertService.successDataInterceptor(data);
            if (data.stat === 'OK') {
                $scope.contents = data.fileInfoList;
            }
            // myAlertService.showAlert("警告：获取服务器数据失败！", "secondary", "false", 1000);

        }).error(function(data, status) {
            myAlertService.showAlert("警告：删除文件错误！", "secondary", "false", 1000);
        });
    }


    //函数需要返回之后才可以调用
    return {
        //返回的函数
        getFileInfoList: getFileInfoList,
        //使用post需要注意
        deleteFile: deleteFile
    };

}]);


//如果没有依赖，不可写作''!!!
manageService.factory('myAlertService', ['$timeout', function($timeout) {

    var loginURL = "/Community/manage/login.html";
    var manageURL = "/Community/manage/manage.html";

    //创建一个Array对象,
    var alerts = [];

    //添加alert推送的信息
    function addAlert(content, type, closeable) {
        alerts.push({
            "content": content,
            "type": type,
            "closeable": closeable
        });
    }

    //删除第index个
    function removeAlert(index) {
        alerts.splice(index, 1);
    }

    function popAlert() {
        alerts.pop();
    }

    function showAlert(content, type, closeable, time) {

        addAlert(content, type, closeable);
        $timeout(function(argument) {
            popAlert();
        }, time);
    }

    function successDataInterceptor(data) {
        if (data.stat === "INVALID_TOKEN") {
            window.location.href = loginURL;
            return;
        }
        if (data.stat === "LOGIN_SUCCESS") {
            window.location.href = manageURL;
            return;
        }

        if (data.stat !== "OK") {
            showAlert("警告：" + data.errText, "secondary", "false", 1000);
        }

    }

    /**
     * ajax请求后的错误信息处理
     * @param  {[type]} data [description]
     * @return {[type]}      [description]
     */
    function errorDataInterceptor(data) {
        myAlertService.showAlert("警告：删除文件错误！", "secondary", "false", 1000);

    }

    return {
        //添加一个警告
        addAlert: addAlert,
        //删除一个指定的警告     
        removeAlert: removeAlert,
        showAlert: showAlert,
        getAlerts: function() {
            return alerts;
        },
        //显示返回的错误、处理跳转等。
        successDataInterceptor: successDataInterceptor,
        errorDataInterceptor: errorDataInterceptor

    };
}]);

manageService.factory('loginService', ['$rootScope', '$http', 'myAlertService', function($rootscope, $http, myAlertService) {

    var loginURL = "/Community/manage/user/login";

    function login(param) {

        $http.post(loginURL, $.param(param), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            }
        }).success(function(data, status) {
            myAlertService.successDataInterceptor(data);
            // myAlertService.showAlert("警告：获取服务器数据失败！", "secondary", "false", 1000);
        }).error(function(data, status) {
            myAlertService.errorDataInterceptor(data);
        });
    }

    return {
        login: login
    };
}]);


manageService.factory('topicService', ['$http', 'myAlertService', function($http, myAlertService) {

    var getTopicPaginateURL = "/Community/manage/topic/getPaginate"; //获取topic的分页
    var deleteTopicByTopicIdURL = "/Community/manage/topic/deleteTopicByTopicId"; //通过topicid删除topic
    var deleteTopicsURL = "/Community/manage/topic/deleteTopics"; //通过topicId删除多个topic
    var getTopicByIdURL = "/Community/manage/topic/get"; //通过topicId获取topic详细信息

    var selectTopic = {
        topicId: 20,
        userId: 26
    };



    //定义一个帖子的类。其实，也可以定义一个单例的对象
    function Paginate(pageNumber, pageSize) {
        this.pageNumber = pageNumber; //当前页
        this.pageSize = pageSize; //每页的大小
        this.topicSum = {}; //帖子的总数
        this.pageCount = {}; //总页数
    }

    Paginate.prototype = {
        constructor: Paginate,
        //可以获取到外部的参数
        nextPage: function() {
            if (this.pageNumber < this.pageCount) {
                this.pageNumber++;
            }
        },
        lastPage: function() {
            if (this.pageNumber > 0) {
                this.pageNumber--;
            }
        },
        setTopicSum: function(topicSum) {
            this.topicSum = topicSum;
        },
        setPageCount: function(pageCount) {
            this.pageCount = pageCount;
        }
    };

    //默认
    var paginate = new Paginate(0, 10);

    /**
     * 刷新数据
     */
    function refresh($scope) {
        $http.post(getTopicPaginateURL, $.param(paginate), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            }
        }).success(function(data, status) {
            myAlertService.successDataInterceptor(data);
            if (data.stat === 'OK' && data.topics.length === 0) {
                myAlertService.showAlert("提示：服务器暂时无数据！", "secondary", "false", 1000);
            }
            if (data.stat === 'OK' && data.topics.length !== 0) {
                $scope.topics = data.topics;
                paginate.setTopicSum(data.topicSum);
                paginate.setPageCount(data.pageCount);
            }
        }).error(function(data, status) {
            myAlertService.showAlert("警告：获取topic失败！", "secondary", "false", 1000);
        });
    }

    /**
     * 删除一个topic
     */
    function deleteCheckedTopic(topicId) {
        $http.post(deleteTopicByTopicIdURL, $.param({
            "topicId": topicId
        }), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            }
        }).success(function(data, status) {
            myAlertService.successDataInterceptor(data);
            if (data.stat === 'OK') {}
            // myAlertService.showAlert("警告：获取服务器数据失败！", "secondary", "false", 1000);

        }).error(function(data, status) {
            myAlertService.showAlert("警告：删除topic失败！", "secondary", "false", 1000);
        });
    }

    function deleteCheckedTopics(topicIdList) {
        $http.post(deleteTopicsURL, $.param({
            "topicIdList": topicIdList
        }), {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            }
        }).success(function(data, status) {
            myAlertService.successDataInterceptor(data);
            if (data.stat === 'OK') {}
            // myAlertService.showAlert("警告：获取服务器数据失败！", "secondary", "false", 1000);

        }).error(function(data, status) {
            myAlertService.showAlert("警告：删除topic失败！", "secondary", "false", 1000);
        });
    }

    function getTopicById($scope) {
        $http.get(getTopicByIdURL, {
            params: {
                'topicId': selectTopic.topicId,
                'userId': selectTopic.userId
            }
        }).success(function(data, status) {
            myAlertService.successDataInterceptor(data);
            if (data.stat === 'OK' && data.topic !== null) {
                myAlertService.showAlert("提示：服务器暂时无数据！", "secondary", "false", 1000);
            }
            if (data.stat === 'OK' && data.topic !== null) {
                $scope.topic = data.topic;
                $scope.replys = data.replys;
            }
        }).error(function(data, status) {
            myAlertService.showAlert("警告：获取topic失败！", "secondary", "false", 1000);
        });

    }

    return {
        //变量
        selectTopic: selectTopic,
        paginate: paginate,
        //函数
        refresh: refresh,
        deleteCheckedTopic: deleteCheckedTopic,
        deleteCheckedTopics: deleteCheckedTopics,
        getTopicById: getTopicById
    };
}]);

manageService.factory('replyService', ['$http', 'myAlertService', function($http, myAlertService) {

    var deleteReplyURL = "/Community/manage/reply/delete"; //通过topicId获取topic详细信息

    function deleteReply (replyId) {
        $http.get(deleteReplyURL, {
            params: {
                'replyId': replyId,
            }
        }).success(function(data, status) {
            myAlertService.successDataInterceptor(data);
            if (data.stat === 'OK' ) {
            }
        }).error(function(data, status) {
            myAlertService.showAlert("警告：删除评论失败！", "secondary", "false", 1000);
        });
    }

    return {
        deleteReply: deleteReply

    };
}]);
