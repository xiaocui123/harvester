/**
 * 前台常量变量
 */
 /* 系统配置 */
 /** SessionKey: 用户uuid */
var SESN_USR_UID = "sesnUsrId";
/** session用户 */
var SESSION_USER = "SYSTEM_USER";
/** session角色模块 */
var SESSION_MENU = "AuthFunc";
/** SessionKey: 用户帐号 */
var SESN_USR_ACOUNT = "sesnUsrACOUNT";
/** 系统业务权限 */
var SESSION_SYSTEM_AUTH = "sysAuth";
/** 系统来源 */
var SESSION_SYSTEM_NAME = "sysName";
/** 系统别称 */
var SYSTEM_NAME = "ffap";

 /* 码值配置 */
var  DICT_1001 = 1001;
	
var  DICT_1002 = 1002;

var  DICT_1003 = 1003;

var  DICT_1004 = 1004;

var  DICT_1005 = 1005;

var  DICT_1006 = 1006;

var  DICT_1007 = 1007;

var  DICT_1008 = 1008;

var  DICT_1009 = 1009;
//1报案 2举报 3咨询 4投诉 5申诉
var   DICT_1009_1 = 1;
var   DICT_1009_2 = 2;
var   DICT_1009_3 = 3;
var   DICT_1009_4 = 4;
var   DICT_1009_5 = 5;

var  DICT_1010 = 1010;

var  DICT_1011 = 1011;

var  DICT_1012 = 1012;

var  DICT_1013 = 1013;

var  DICT_1014 = 1014;

var  DICT_1015 = 1015;

var  DICT_1016 = 1016;

var  DICT_1017 = 1017;

var  DICT_1018 = 1018;

var  DICT_1019 = 1019;

var  DICT_1020 = 1020;

var  DICT_1021 = 1021;
// 接警后处理审核流程
var  DICT_1021_1 = 1;

var  DICT_1022 = 1022;

var  DICT_1023 = 1023;
// 审核状态“提交审核”
var  DICT_1023_1 = 1;
// 审核状态“审核中”
var  DICT_1023_2 = 2;
// 审核状态“审核通过”
var  DICT_1023_3 = 3;
// 审核状态“审核不通过”
var  DICT_1023_4 = 4;

var  DICT_1024 = 1024;

var  DICT_1025 = 1025;
//1025转发处理状态0未转发，1已转发，2已审核，3已反馈，4处理结果已确认 
var  DICT_1025_0 = 0;
var  DICT_1025_1 = 1;
var  DICT_1025_2 = 2;
var  DICT_1025_3 = 3;
var  DICT_1025_4 = 4;

var  DICT_1026 = 1026;
// 转发类型“网警”
var  DICT_1026_1 = 1;
// 转发类型“银行”
var  DICT_1026_2 = 2;
// 转发类型“通信公司”
var  DICT_1026_3 = 3;
// 转发类型“技侦”
var  DICT_1026_4 = 4;
// 转发类型“情报”
var  DICT_1026_5 = 5;

var  DICT_1027 = 1027;

var  DICT_1028 = 1028;
// 转发状态“未转发”
var  DICT_1028_0 = 0;
// 转发状态“已转发”
var  DICT_1028_1 = 1;
// 转发状态“处理结果已确认”
var  DICT_1028_2 = 2;

var  DICT_1029 = 1029;

var  DICT_1030 = 1030;

var  DICT_1031 = 1031;

var  DICT_1031_1 = 1;

var  DICT_1032 = 1032;

var  DICT_1033 = 1033;

var  DICT_1034 = 1034;

var  DICT_1035 = 1035;

var  DICT_1036 = 1036;

var  DICT_1037 = 1037;

var  DICT_1038 = 1038;

var  DICT_1039 = 1039;
//转发部门缩写（2位）包括WJ（网警）、JZ（技侦）、YH（银行）、TX（通信公司） QB(情报)
var  TRANSMIT_TYPE_WJ = "WJ";
var  TRANSMIT_TYPE_JZ = "JZ";
var  TRANSMIT_TYPE_YH = "YH";
var  TRANSMIT_TYPE_TX = "TX";
var  TRANSMIT_TYPE_QB = "QB";
//定义sequence名字
var  TRANSMIT_SEQUENCE_WJ = "sq_transmit_wj";
var  TRANSMIT_SEQUENCE_JZ = "sq_transmit_jz";
var  TRANSMIT_SEQUENCE_YH = "sq_transmit_yh";
var  TRANSMIT_SEQUENCE_TX = "sq_transmit_tx";
var  TRANSMIT_SEQUENCE_QB = "sq_transmit_qb";
//转发案件是否来自系统
//3.	是否为系统内（1位）包括A（接警系统的案件）、B（不是接警系统的案件）
var  TRANSMIT_IS_SYSTEM = "A";
var  TRANSMIT_NOT_SYSTEM = "B";
//移动、联通、电信
var  TX_YD = "yd";
var  TX_LT = "lt";
var  TX_DX = "dx";


/* 业务配置 */
//超级管理员
var  ROLE_1=1;
//接警员
var  ROLE_2=2;
//审核员
var  ROLE_3=3;
 
var WARNING_YPFX='ypfx';
var WARNING_ZZYD='zzyd';
var WARNING_DJQR='djqr';