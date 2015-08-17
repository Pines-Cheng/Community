//获取异常左右的异常文件的信息
// 127.0.0.1:8080/exception/show
// 127.0.0.1:8080/Community/manage/exception/show
// 


//
POST http://127.0.0.1:8080/Community/topic/getList
Content-type: application/x-www-form-urlencoded
POST_BODY:
token=e59813dc3982a653183993810588ef9f

POST http://127.0.0.1:8080/Community/topic/get
Content-type: application/x-www-form-urlencoded
POST_BODY:
token=e59813dc3982a653183993810588ef9f&userId=16&id=12


//通过userId和id获取topic的详细信息
POST http://127.0.0.1:8080/Community/manage/topic/get
Content-type: application/x-www-form-urlencoded
POST_BODY:
userId=16&id=11

//通过分页获取帖子的基本信息
POST http://127.0.0.1:8080/Community/manage/topic/getPaginate
Content-type: application/x-www-form-urlencoded
POST_BODY:
pageNumber=0&pageSize=5

