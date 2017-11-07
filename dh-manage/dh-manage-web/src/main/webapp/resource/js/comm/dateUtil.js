// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
// 例子： 
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

//字符转转化为日期
function StringToDate(DateStr){
	return  new Date(Date.parse(DateStr.replace(/-/g,   "/"))); //转换成Data
}

//增加月  
function AddMonths(date, value) {
    date.setMonth(date.getMonth() + value);
    return date;
}

//增加天  
function AddDays(date, value) {
    date.setDate(date.getDate() + value);
    return date;
}

//增加时
function AddHours(date, value) {
    date.setHours(date.getHours() + value);
    return date;
}

//返回月份(两位数)  
function GetFullMonth(date) {
	var v = date.getMonth() + 1;
	if (v > 9) return v.toString();
	return "0" + v;
 }
 
//返回日(两位数)  
function GetFullDate(date) {
    var v = date.getDate();
    if (v > 9) return v.toString();
    return "0" + v;
}

//返回时(两位数)
function GetFullHour(date) {
    var v = date.getHours();
    if (v > 9) return v.toString();
    return "0" + v;
}

//当前时间(字符串格式）
function CurentTime(fmt){ 
    var now = new Date();
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分
    var ss = now.getSeconds();
     var clock = year + "-";
	    if(month < 10){
	        clock += "0";
	    }
	    clock += month + "-";
	    if(day < 10){
	        clock += "0";
	    }
	    clock += day ;
    if(fmt=="yyyy-MM-dd HH:mm:ss"){
	   clock += " ";
	    if(hh < 10){
	        clock += "0";
	    }
	    clock += hh + ":";
	    if (mm < 10){ clock += '0'}; 
	    clock += mm+':'+ss; 
    }else if(fmt=="yyy-MM-dd"){
    	//如果是该种格式就不用在处理了，默认格式为此格式
    }
    return clock; 
};

function formatDatebox(value) {
    if (value == null || value == '') {
        return '';
    }
    var dt;
    if (value instanceof Date) {
        dt = value;
    } else {
        dt = new Date(value);
    }
    return dt.Format("yyyy-MM-dd hh:mm:ss"); //扩展的Date的format方法(上述插件实现)
}

/**
 * 格式化日期--不带时分秒
 * @param value
 * @returns
 */
function formatDatebox1(value) {
    if (value == null || value == '') {
        return '';
    }
    var dt;
    if (value instanceof Date) {
        dt = value;
    } else {
        dt = new Date(value);
    }
    return dt.Format("yyyy-MM-dd"); //扩展的Date的format方法(上述插件实现)
}

/**
 * 格式化日期 
 * @param value
 * @returns
 */
function formatDateByFmt(value,fmt) {
    if (value == null || value == '') {
        return '';
    }
    var dt;
    if (value instanceof Date) {
        dt = value;
    } else {
        dt = new Date(value);
    }
    return dt.Format(fmt); //扩展的Date的format方法(上述插件实现)
}

//获取当前月份第一天
function getFirstDate(date){
	var firstDate = date==null?new Date():date;
	firstDate.setDate(1); //第一天
	return firstDate;
}

//获取当前月份第一天
function getFirstYearDate(date){
	var firstDate = date==null?new Date():date;
	firstDate.setMonth(0);//第一个月
	firstDate.setDate(1); //第一天
	return firstDate;
}

