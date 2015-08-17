var manageDirective = angular.module('manageDirective', []);

/**
 * 表单的指令，并没有什么复用价值，仅供学习参考
 */
manageDirective.directive('myTable', function() {
    // Runs during compile
    return {
        // name: '',
        // priority: 1,
        // terminal: true,
        // scope: {}, // {} = isolate, true = child, false/undefined = no change
        // controller: function($scope, $element, $attrs, $transclude) {},
        // require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
        restrict: 'AE', // E = Element, A = Attribute, C = Class, M = Comment
        // template: '',
        templateUrl: '../manage/tpls/table.html',
        replace: false, //将自定义标签完全移除
        // transclude: true,
        // compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),
        link: function($scope, iElm, iAttrs, controller) {

        }
    };


});

/**
 * 警告框的指令
 */
manageDirective.directive("myAlert", function() {
    return {
        // name: '',
        // priority: 1,
        // terminal: true,
        // scope: {}, // {} = isolate, true = child, false/undefined = no change
        // controller: function($scope, $element, $attrs, $transclude) {},
        // require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
        restrict: 'E', // E = Element, A = Attribute, C = Class, M = Comment
        // template: '',
        templateUrl: '../manage/tpls/alert.html',
        // replace: true,
        // transclude: true,
        // compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),
        // 可以在指令中设置自动隐藏
        link: function($scope, iElm, iAttrs, controller) {
            // console.dir(iElm);
            // console.dir(iAttrs);
            // $(iElm).slideDown('slow');
        }
    };
});



/**
 * 分页的指令，这个写得不错
 *
 *注意： 即使没有依赖，['topicService',function (topicService)里面也不可为空
 */
manageDirective.directive("myPaginate", function() {
    return {
        // name: '',
        // priority: 1,
        // terminal: true,
        // scope: true, // {} = isolate,隔离作用域 true = child, false/undefined = no change
        // controller: function($scope, $element, $attrs, $transclude) {},
        // require: 'ngModel', // Array = multiple requires, ? = optional, ^ = check parent elements
        restrict: 'AE', // E = Element, A = Attribute, C = Class, M = Comment
        // template: '',
        templateUrl: '../manage/tpls/paginate.html',
        // replace: true,
        transclude: true,
        // compile: function(tElement, tAttrs, function transclude(function(scope, cloneLinkingFn){ return function linking(scope, elm, attrs){}})),
        link: function($scope, iElm, iAttrs, controller) {
            // console.dirxml($scope.paginate);
            // console.dirxml(iElm);
            // console.dirxml(iAttrs);
            // console.dirxml(controller);

            //封装成JQuery元素,选择所有的<li>元素
            var $total = $(iElm).find('li');
            //获取前一页、后一页、及数字页的按钮
            var $lastPage = $($total[0]);
            var $nextPage = $($total[6]);
            var $pageNumbers = $total.filter(".pageNumbers");
            // console.dirxml($pageNumbers);

            //上一页点击事件
            $lastPage.on('click', function() {
                if ($scope.paginate.pageNumber > 0) {
                    $scope.paginate.pageNumber--;
                    $scope.refresh(); //刷新页面
                } else {
                    $scope.paginate.pageNumber = 0;

                }
            });

            //下一页点击事件
            $nextPage.on('click', function() {
                if ($scope.paginate.pageNumber < $scope.paginate.pageCount - 1) {
                    $scope.paginate.pageNumber++;
                    $scope.refresh(); //刷新页面
                } else {
                    $scope.paginate.pageNumber = $scope.paginate.pageCount - 1;

                }
            });

            //为页面按钮绑定点击跳转事件
            $.each($pageNumbers, function(index, val) {
                $(val).on('click', function() {
                    $scope.paginate.pageNumber = $(val).children().text() - 1;
                    $scope.refresh();
                });
            });

            /**
             * 页面跳转的功能
             */
            $scope.pageJump = function(pageJumpNum) {
                if ($.isNumeric && pageJumpNum > 0 && pageJumpNum <= $scope.paginate.pageCount) {
                    $scope.paginate.pageNumber = pageJumpNum - 1;
                    $scope.refresh();
                }
                //执行完后清空数据
                $scope.pageJumpNum = "";
            };

            //监控paginate对象,如果有变化，则更新分页的显示
            $scope.$watch('paginate', function() {

                //判断下一页是否有效
                if ($scope.paginate.pageNumber >= $scope.paginate.pageCount - 1) {
                    $nextPage.addClass('am-disabled');

                } else {
                    $nextPage.removeClass('am-disabled');
                }

                //判断上一页是否有效
                if ($scope.paginate.pageNumber <= 0) {
                    $lastPage.addClass('am-disabled');
                } else {
                    $lastPage.removeClass('am-disabled');
                }

                //控制1-5页的显示与高亮
                $.each($pageNumbers, function(index, val) {

                    var firstPageNum = $scope.paginate.pageNumber - $scope.paginate.pageNumber % 5;
                    $(val).children().text(firstPageNum + index + 1);

                    //当前页面的切换
                    if ($(val).text() == $scope.paginate.pageNumber + 1) {
                        $(val).addClass('am-active');
                    } else {
                        $(val).removeClass('am-active');
                    }

                    //页面按钮显示与否
                    if ($(val).children().text() > $scope.paginate.pageCount) {
                        $(val).css('display', 'none');
                    } else {
                        $(val).css('display', 'inline-block');
                    }

                });
            }, true); //监控的为对象（引用），需要将第三个参数设为true


        }
    };
});

/**
 * fancybox封装成了一个指令.
 * 使用$complie编译通过ajax获取的页面，然后在使用fancybox手动打开
 */
manageDirective.directive('myFancybox', function($compile, $http) {
    return {
        restrict: 'AE',

        controller: function($scope) {

            $scope.openFancybox = function(url) {

                // console.log(url);

                $http.get(url).then(function(response) {
                    if (response.status == 200) {

                        var template = angular.element(response.data);
                        var compiledTemplate = $compile(template);
                        compiledTemplate($scope);

                        $.fancybox.open({
                            maxWidth: 800,
                            maxHeight: 600,
                            fitToView: false,
                            width: '70%',
                            height: '70%',
                            autoSize: false,
                            closeClick: false,
                            openEffect: 'none',
                            closeEffect: 'none',
                            
                            afterClose:function  () {
                                $scope.refresh();
                            },

                            content: template,
                            type: 'html'
                        });
                    }
                });
            };
        }
    };
});
