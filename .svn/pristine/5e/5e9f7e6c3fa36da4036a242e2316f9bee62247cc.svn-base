drop database  if  exists `niurenhui`;

create database `niurenhui` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
use `niurenhui`;

/**数据库的时间采用统一的时间戳形式**/
create table userinfo(
	id int unsigned primary key auto_increment,
	tel char(11) unique,
	password char(32),
	salt char(6),/**密码加密生成的随机值**/
	username varchar(20),
	intro varchar(200), /**个人简介**/
	register_time long not null,
	exp int default 0, /**用户的经验值，根据多个指标计算**/
	photo_path varchar(50) default '/upload/U', /**用户上传头像存储目录**/
	havePhoto bit default 0,
	device_id char(32), /**设备唯一标识号**/
	school varchar(20),
	address varchar(40),
	copy_num int default 0 /**被抱大腿次数**/
);

create table user_token(
	user_id int unsigned primary key,
	token char(32) not null,
	create_time long,
	update_time long,
	authority int /**0,未登录，只读权限，1，登录，读写权限，后面可扩展VIP等**/
);

create table score(
	remark_id int unsigned not null,/**打分人**/
	receiver_id int unsigned not null,/**被评分人**/
	score tinyint,/**打的分**/
	primary key(remark_id, receiver_id)
);

create table groupinfo(
	id int unsigned primary key auto_increment,
	creater varchar(20) not null, /**创建人**/
	name varchar(20),/**群组名字**/
	intro varchar(200),/**群介绍**/
	create_time long, /**创建时间**/
	grade int default 0, /**群等级**/
	place int, /**群排名**/
	image_path varchar(50) default '/upload/G',
	invite_code varchar(10), /**群邀请码 群组Id+3位随机数**/
	user_limit int default 10000 /**群人数限制**/
);

create table groupuser(
	group_id int unsigned not null,
	user_id int unsigned not null,
	auth tinyint default 0,/**该群的权限(0:普通成员,1:群主...)**/
	exp int,/**群经验(或群等级)**/ 
	primary key(group_id,user_id)
);

create table topic(
	id int unsigned primary key auto_increment,
	user_id int unsigned not null,
	username varchar(20) not null,/**发帖人**/
	group_id int unsigned,
	content varchar(500) not null,
	sendtime long not null,
	share_num int default 0, /**被分享的数量**/
	receiver_users varchar(20), /**邀请哪些用户回答，以,间隔 如 1，2，3**/
	image_dir varchar(50) default '/upload/T',
	share_code char(6), /**分享码**/
	is_reprint bit default 0,/**是否是转载的**/
	origin_id int unsigned,/**如果是转发，转发的那个帖子Id**/
	visible enum('ALL','GROUP','PRIVATE'),
	image_num smallint default 0,
	receiver_users varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
);

/**评论拆分**/
create table topic_reply(
	id int unsigned primary key auto_increment,
	user_id int unsigned not null,
	username varchar(20) not null,/**写回复人的姓名**/
	receiver varchar(20),/**被回复人的姓名**/
	attach_id int unsigned not null,
	reply_id int unsigned,
	content varchar(200) not null,
	sendtime long not null
);

create table news_reply(
	id int unsigned primary key auto_increment,
	user_id int unsigned not null,
	username varchar(20) not null,/**写回复人的姓名**/
	receiver varchar(20),/**被回复人的姓名**/
	attach_id varchar(100) unsigned not null,
	reply_id int unsigned,
	content varchar(200) not null,
	sendtime long not null
);

/**粉丝列表(关注列表)**/
create table fans(
	user_id int unsigned not null,
	fans_id int unsigned not null,
	primary key(user_id, fans_id)
);

/**点赞表**/
--create table agree(
--	subject_id int unsigned not null,/**点赞的某个主题Id**/
--	subject_type tinyint not null,/**主题的种类(topic:1,reply:2,news:3)**/
--	user_id int unsigned not null,
--	isSupprot bit default 0, /**赞还是踩 0表示赞，1表示踩**/ 
--	primary key(subject_id, subject_type, user_id)
--); /**点赞数量比较多，后台先拆分比较好**/

create table topic_agree(
	subject_id int unsigned not null,/**点赞的某个主题Id**/
	user_id int unsigned not null,
	isSupport bit default 0, /**赞还是踩 0表示赞，1表示踩**/ 
	primary key(subject_id, subject_type, user_id)
); 

create table reply_agree(
	subject_id int unsigned not null,/**点赞的某个主题Id**/
	user_id int unsigned not null,
	isSupport bit default 0, /**赞还是踩 0表示赞，1表示踩**/ 
	primary key(subject_id, subject_type, user_id)
); 

create table news_agree(
	subject_id varchar(100) not null,/**点赞的某个主题Id**/
	user_id int unsigned not null,
	isSupport bit default 0, /**赞还是踩 0表示赞，1表示踩**/ 
	primary key(subject_id, subject_type, user_id)
);

create table message(
	id int unsigned primary key auto_increment,
	send_id int unsigned not null, /**消息发送者**/
	username varchar(20) not null,
	msg_type int not null, /**消息类型 1:申请入群消息,2:有人回复了帖子的消息,3被邀请回复的消息,4:有人点赞的消息(赞帖子),5:(赞回复),**/
	attach_id int unsigned not null, /**消息相关联的id, 可以是帖子的Id，也可以是群组Id**/
	title varchar(30) not null,
	content varchar(500) not null,
	create_time long not null
);

create table user_message(
	message_id int unsigned,
	receiver_id int unsigned not null, /**消息接收者**/
	status tinyint default 0, /**0:未发送,1:已发送**/
	primary key(message_id, receiver_id)
);

create table suggest(
	id int unsigned primary key auto_increment,
	user_id int unsigned not null,
	content varchar(200) not null,
	contact varchar(40) not null,
	create_time long not null
);

/**新闻相关**/
create table trade_with_news(
	user_id int unsigned not null, /**用户Id**/
	type_pair varchar(10) not null, /**货币对 如(EUR/USD(欧元兑美元))**/
	news_ids varchar(100) not null, /**该决策对应的消息Id列表，以,间隔，如:1,2,3**/
	isBuy bit not null /**买还是卖**/
);

create table user_atten_tags(
	user_id int unsigned not null primary key, /**用户Id**/
	tags varchar(100) not null, /**用户标签 以,间隔 如 美元,欧元,日元**/
	tags_code varchar(30) /**用户关注标签代号 以,间隔 如EUR/USD,XAU/USD**/
);

/**用户交易数据报告，每隔一段时间刷新**/
create table trade_report(
	user_id int unsigned not null,
	profit float, /**盈利**/
	win_rate float, /**胜率**/
	max_lost float, /**最大亏损**/
	trans_num int, /**出手次数(交易次数)**/
	avg_secords int /**平均持仓时间**/
);

create table copyuser( /**抱大腿记录表**/
	user_id int unsigned not null, /****/
	copy_id int unsigned not null,
	copy_time long not null,
	status int default 0
);

create table combination(
	id int unsigned primary key auto_increment,
	user_id int unsigned not null,
	name varchar(30) not null,
	description varchar(30),
	analysis_types int not null,
	currency_types varchar(500) not null,
	crash_remaning int not null,
	create_time long not null,
	modify_time long not null,
	lever smallint not null,
	score int not null default 0
);

--use niurenhui;
--insert into user_atten_tags (user_id, tags, tags_code) values (3, '美元,欧元', 'EUR/USD,XAU/USD');

--create table news(
--	id int primary key auto_increment,
--	ntype varchar(20) not null, /**消息类型**/
--	title varchar(100) not null, /**消息标题**/
--	content varchar(1000), /**新闻内容**/
--	about_tags varchar(100), /**该新闻相关的标签**/
--	detail_url varchar(50),
--	importance int default 0,  /**重要度**/
--	attachs varchar(100)  /**附加的属性 用json存储**/
--);
