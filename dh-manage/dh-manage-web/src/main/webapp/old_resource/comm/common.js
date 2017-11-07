/**
 * 验证开始结束时间
 * 
 * @param {}
 *            dateStart
 * @param {}
 *            dateEnd
 * @return boolean
 * @author xuhj
 */
function dateValidation(dateStart, dateEnd, name) {
	if (isBlank(dateStart) && isBlank(dateEnd)) {
		return true;
	}
	if (isBlank(dateStart) && !isBlank(dateEnd)) {
		$.messager.alert("信息", "请输入" + name + "开始时间。", "info");
		return false;
	}
	if (!isBlank(dateStart) && isBlank(dateEnd)) {
		$.messager.alert("信息", "请输入" + name + "结束时间。", "info");
		return false;
	}
	var arr1 = dateStart.split("-"), arr2 = dateEnd.split("-")
	var dtStart = new Date(arr1[0], arr1[1], arr1[2])
	var dtEnd = new Date(arr2[0], arr2[1], arr2[2])
	var tmpDt = dtEnd - dtStart; // 相差毫秒数
	if (tmpDt < 0) {
		$.messager.alert("信息", name + "结束时间不能小于" + name + "开始时间。", "info");
		return false;
	}
	if (tmpDt > 1000 * 60 * 60 * 24 * 365) {
		$.messager.alert("信息", name + "结束时间不能大于" + name + "开始时间一年。", "info");
		return false;
	}
	return true;
}

// 判空
function isBlank(string) {
	string=$.trim(string);
	return typeof string == "undefined" || string == null || string == "" || string == "null"
}

/**
 * Date Formatter
 * 
 * @param {}
 *            fmt
 * @return {}
 */
Date.prototype.Format = function(fmt) { // author: csdn meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
		// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4
						- RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

// 特殊字符
function sqlCheck(sql) {
	var patrn = /[`~!@#$%^&*_+<>?"{},.\/;'[\]]/im;
	return patrn.test(sql)
}

// 字符串长度
function lengthCheck(val, length) {
	var len = 0;
	var v = $.trim(val);
	for (i = 0; i < v.length; i++) {
		if (v.charCodeAt(i) > 256) {
			len += 3;
		} else {
			len++;
		}
	}
	// alert(len+"_"+length)
	return len > length
}
function lengthCheckForCH(val, length) {
	var v = $.trim(val);
	// alert(len+"_"+length)
	return v.length > length
}

//加载等待提示框
function onloading(){
	$("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");   
	$("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候 . . .").appendTo("body").css({display:"block",'font-size':"14px",left:($(window).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
}
//移除等待提示框  
function removeload(){  
    $(".datagrid-mask").remove();  
    $(".datagrid-mask-msg").remove();  
} 

// 设置用户权限:按钮 & 链接
function setFuncAuth(sysName, authStr, permissinId, obj, type){
	if(authStr.indexOf(permissinId+'=true')==-1){ // 无权限
		if(type==0){//不操作
			
		} else if(type==1){ // 总览页面 超链接移除href
			$("a[name='"+obj+"']").removeAttr("href").removeAttr("onclick").css("color","#999");
		} else if(type==2){ // 详情页面 超链接
			$("a[name='"+obj+"']").css("display","none").unbind("click").removeAttr("onclick");
		} else if(type==3){ // 输入框
			$("#"+obj).attr('disabled', 'disabled');
		} else if(type==4){ // 时间选择框
			$("#"+obj).datetimebox({disabled:true});
		} else if(type==5){ // 下拉框
			$("#"+obj).combobox({disabled:true});
		}else if(type==6){//按钮
			$("#"+obj).css("display","none").unbind("click").removeAttr("onclick");
		}
		return false;
	}
	return true;
}
(function(){
    var hb = {};
    /** 日志输出类 */
    hb.LogUtil = {
        log:function(msg,param){ // 输出日志信息 log
        	if(window.console){
        		console.log(msg, typeof param == 'undefined' ? '':param);
        	}
        },
        warn:function(msg,param){ // 输出日志信息 warn
        	if(window.console){
        		console.warn(msg, typeof param == 'undefined' ? '':param);
        	}
        },
        error:function(msg,param){ // 输出日志信息 error
        	if(window.console){
        		console.error(msg, typeof param == 'undefined' ? '':param);
        	}
        },
        info:function(msg,param){ // 输出日志信息 info
        	if(window.console){
        		console.info(msg, typeof param =='undefined' ? '':param);
        	}
        }
    };
    hb.StrUtil = {
        trim:function(str){ // trim字符串前后空格
           return str.replace(/(^\s*)|(\s*$)/g, ''); 
        },
        lTrim :function(str){ // trim字符串左边空格
           return str.replace(/(^\s*)/g, ''); 
        },
        rTrim :function(str){ // trim字符串右边空格
           return str.replace(/(\s*$)/g, ''); 
        },
        isNull:function(str){ // 判断字符串是否为null
            if(typeof str == 'undefined' || str == null || str=='' || str=='null'){
                return true;
            }else{
                return false;
            }
        },
        undefined2Empty:function(str){//处理字符串，是undifined返回null
        	if(typeof str == 'undefined'){
                return "";
            }else{
                return str;
            }
        },
        isBlank:function(str){ // 判断字符串是否为空
            if(typeof str == 'undefined' || str == null || hb.StrUtil.trim(str) == ''){
                return true;
            }else{
                return false;
            }
        },
        isNumber: function(str) {  // 判断字符串是否全为数据
            if(typeof str == 'undefined' || str == null || hb.StrUtil.trim(str) == ''){
                return false;
            }
            if (!(/^\d*$/g).test(str)) {
                return false;
            }
            return true;
        }
    };

	hb.Obejct = {
     	parseJSON:function(obj){
     		if (typeof obj == "string") {
     			if(obj=="" || null==obj){//数据为空字符串返回false
     				return false;
     			}
				obj = $.parseJSON(obj);
				if(obj.msgCode == 'SYS_101'){//解析为无权 返回false
					alert('会话超时，请重新登录！');
					getTopWinow().location.href=ctxPath+"/login/goOut";
				}else if(obj.msgCode == 'SYS_102'){
					alert('系统错误，请联系管理员！');
					return false;
				}else{//数据为其他字符串 返回obj
					return obj;
				}
				return false;
			}else{//数据为json 返回true
				if(obj.msgCode == 'SYS_101'){//解析为无权 返回false
					alert('会话超时，请重新登录！');
					getTopWinow().location.href=ctxPath+"/login/goOut";
					return false;
				}else if(obj.msgCode == 'SYS_102'){
					alert('系统错误，请联系管理员！');
					return false;
				}else{
					return obj;
				}
				return true;
			}
     	}
     	
     };
	/**
		浏览器信息 
	*/
	hb.Browser={
		agent:navigator.userAgent.toLowerCase(),
		_ie:null,
		/**
		 * 获取浏览器信息
		 * @memberOf {TypeName} 
		 * @return {TypeName} 
		 */
		getBrowserInfo:function(){
			var regStr_ie = /msie [\d.]+;/gi ;
			var regStr_ff = /firefox\/[\d.]+/gi;
			var regStr_chrome = /chrome\/[\d.]+/gi ;
			var regStr_saf = /safari\/[\d.]+/gi ;
			this._ie=false;
			//IE
			if(this.agent.indexOf("msie") > 0){
				this._ie=true;
				return this.agent.match(regStr_ie) ;
			}
			//firefox
			if(this.agent.indexOf("firefox") > 0){
				return this.agent.match(regStr_ff) ;
			}
			//Chrome
			if(this.agent.indexOf("chrome") > 0){
				return this.agent.match(regStr_chrome) ;
			}
			//Safari
			if(this.agent.indexOf("safari") > 0 && this.agent.indexOf("chrome") < 0){
				return this.agent.match(regStr_saf) ;
			}
		},
		/**
		 * 获取浏览器版本
		 * @memberOf {TypeName} 
		 * @return {TypeName} 
		 */
		getVersion:function(){
			return (this.getBrowserInfo()+"").replace(/[^0-9.]/ig,"");
		},
		/**
		 * 是否是IE
		 * @memberOf {TypeName} 
		 * @return {TypeName} 
		 */
		isIE:function(){
			if (this._ie==null || this._ie==undefined){
				this.getBrowserInfo();
			}
			return this._ie;
		}
	};
	/**
	 * 隐藏Form提交
	 * @param {Object} url
	 * @param {Object} method
	 * @param {Object} params
	 */
	hb.Form={
		submit:function(url,method,params){
			var form=$("<form>");//定义一个form表单
				form.attr("style","display:none");
				form.attr("target","");
				form.attr("method",method);
				form.attr("action",url);
			for(var key in params){
				var input1=$("<input>");
						input1.attr("type","hidden");
						input1.attr("name",key);
						input1.attr("value",params[key]);
						form.append(input1);
  			}
			$("body").append(form);//将表单放置在web中
			form.submit();//表单提交
		}
	};
    window.hb = hb;
})(window);

/**获取浏览器最上层对象*/
function getTopWinow(){  
    var p = window;  
    while(p != p.parent){  
        p = p.parent;  
    }  
    return p;  
}  
/** 替换全部字符 */
String.prototype.replaceAll = function(s1,s2){   
	return this.replace(new RegExp(s1,"gm"),s2);
}
String.prototype.trim = function(){
	return hb.StrUtil.trim(this);
}
//ajax 请求公共处理类
$.ajaxSetup({ 
	error: function (XMLHttpRequest, textStatus, errorThrown){
		if(XMLHttpRequest.status==403){
			alert('您没有权限访问此资源或进行此操作');
			return false;
		}
	},  
    complete:function(XMLHttpRequest,textStatus){
    	var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus"); //通过XMLHttpRequest取得响应头,sessionstatus -- response.setHeader
    	var headerresult=XMLHttpRequest.getResponseHeader("headerresult"); //通过XMLHttpRequest获取响应头信息,headerresult -- response.setHeader
   		var resulttext = XMLHttpRequest.responseText;//取得相应信息text -- response.getWriter();
        if(sessionstatus=="timeout"){   
           hb.Obejct.parseJSON(headerresult);//通过公共方法处理 返回信息字符串
   	    }else{
   	    	//解决easyui datagrid 序号过大显示不全问题
   	    	try{
   	    		//返回的数据集字符串
   	    		var textJson = eval('('+resulttext+')');
	   	    	if(textJson.total && textJson.rows && (textJson.total+'').length>3){
	   	    		//rownumber列加宽
	   	    		$('.datagrid-header-rownumber,.datagrid-cell-rownumber').width(8*(textJson.total+'').length);
	   	    		//datagrid resize
	   	    		$('.panel .datagrid-view2:visible').next().datagrid('resize');
	   	    	}
	   	    	//设置页数输入框宽度
   	    		if((parseInt(textJson.total/20)+'').length>3){
   	    			$(".pagination-num").width(10*(parseInt(textJson.total/20)+'').length)
   	    		}
   	    	}catch(e){}
   	    }   
    }   
});
// toolbar 初始化 （由公共框架改为和车驾管同步，xuhj）
//(function($) {
//  // options是js文件中设置的属性
//  $.fn.toolBar = function(options) {
//  	var cnt = this;
//  	// 控件的默认属性
//	var defaults={
//  		url:'../login/getToolBar',
//  		page:300001,
//  		nowrap:false
//  	};
// 
//  	// 页面中设置的属性
//  	$.each($(this)[0].attributes,function(idx,item){
//  		if(item.name=='page'){
//  			defaults['page']=item.value;
//  		}else if(item.name=='url'){
//  			defaults['url']=item.value;
//  		}else if(item.name=='nowrap'){
//  			defaults['nowrap']=item.value;
//  		}
//  	});
//
//	var opts = $.extend(defaults, options);
//	if(hb.StrUtil.isBlank(opts.url)){
//		hb.LogUtil.error('取得toolBar的相对URL路径不能为空！');
//	}else if(!hb.StrUtil.isNumber(opts.page+'')){
//		hb.LogUtil.error('此页面的UUID不能为空！');
//	}
//	
//	// 如果要求不换行，则添加不换行的CSS
//	if(opts.nowrap){
//		cnt.css({'white-space':'nowrap','overflow': 'hidden'});
//	}
//	
//	$.post(opts.url,[{name:'page',value:opts.page}],
//	  function(data){
//	  	var str="";
//	    $.each(data,function(idx, item){
//	    	if(idx==0){
//	    		str = '<a id="toolbar'+ item.uuid+'-first" class="easyui-linkbutton"  href="javascript:;" ';
//	    	}else if(idx==data.length-1){
//	    		str = '<a id="toolbar'+ item.uuid+'-last" class="easyui-linkbutton"  href="javascript:;" ';
//	    	}else{
//	    		str = '<a id="toolbar'+ item.uuid+'-middle" class="easyui-linkbutton"  href="javascript:;" ';
//	    	}
//
//           if(!hb.StrUtil.isBlank(item.openMethod)){//判断页面打开方式
//           		if(item.openMethod==2){
//           			str+= 'onclick="'+item.jsMethod+'()"';
//           		}else if(item.openMethod==1){
//           			 str+= 'onclick="parent.addTabPage(\''+item.uuid+'\',\''+item.name+'\',\''+item.iconCls+'\',\'\',\'\',\''+item.url+'\')"';//通过新标签页
//           		}
//           }
//            if(!hb.StrUtil.isBlank(item.iconCls)){//判断图标是否为空
//            	str+=" iconCls='"+item.iconCls+"'";
//            }
//            if(!hb.StrUtil.isBlank(item.title)){//判断title是否为空
//            	str+=" title='"+item.title+"'";
//            }
//            str+=' >'+item.name+'</a>';
//	    	$(cnt).append(str);
//	    });
//	    
//	  	//页面部分从新渲染easyui 样式
//   		$.parser.parse(cnt);
//  	    
//		var osWidth = window.document.body.offsetWidth;
//		var lastOs = $('#'+cnt[0].id+' a').last().offset();
//		var lastWdth = $('#'+cnt[0].id+' a').last().width();
//		if(cnt.width() - lastOs.left - lastWdth < 17 || cnt.width() > osWidth){
//			cnt.css({'margin-left':'16px', 'margin-right':'16px'});
//		  	var ofset = cnt.first().offset();
//			cnt.parent().prepend('<a class="toolbar_arrow" style="z-index:99999;left:'+(ofset.left-17)+'px;top:'+(ofset.top+1)+'px;" href="javaScript:void(0)" onclick="toolBarMove(1,\''+cnt[0].id+'\')"><span class="toolbar_left"></span></a>');
//			cnt.parent().append('<a class="toolbar_arrow" style="z-index:99999;right:0px;top:'+(ofset.top+1)+'px;" href="javaScript:void(0)" onclick="toolBarMove(2,\''+cnt[0].id+'\')"><span class="toolbar_right"></span></a>');	  		  	
//		}
//	  }
//	);
//  };     
//})(jQuery);

var bars=new Array;
/** 移动toolBar中的标签位置 */
function toolBarMove(direct, tlbar){
	// 向左移
	if(direct == 1){
		if($('#'+tlbar+' a').first().attr("id").split("-")[1]=='last'){//判断是否为最后一个
		}else{
			bars.push($('#'+tlbar+' a').first());//将移除的对象放入数组
			$('#'+tlbar+' a').first().remove();
			//$('#'+tlbar+' a').first().insertAfter($('#'+tlbar+' a').last());		
		}
	// 向右移
	}else{
		if($('#'+tlbar+' a').first().attr("id").split("-")[1]=='first'){//判断是否为第一个
		}else{
			bars[bars.length-1].insertBefore($('#'+tlbar+' a').first());
			bars.splice(bars.length-1,1);////将恢复的按钮对象重数组中移除
			//arr.splice(idx,num,element)  idx开始删除下标，num删除个数，element插入元素
			//$('#'+tlbar+' a').last().insertBefore($('#'+tlbar+' a').first());
		}
	}
}
//验证输入查询条件中是否有非法字符
function checkSql(sql){  
	var patrn=/[`~!@#$%^&*_+<>?"{},.\/;'[\]]/im;
    if(patrn.test(sql)){  
        return true;     
    }     
    return false;  
}
//自定义遮罩
function opencover(){
	$("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(document.body).height()}).appendTo("body");
}
//关闭遮罩
function closecover(){
	$(".datagrid-mask").remove();
}
//自定义dilog监听 打开关闭遮罩层
function dlglistener(obj){
	 $("#"+obj).dialog({
           onClose:function(){
        	   closecover();
           },
           onOpen:function(){
        	   opencover();
           }
      });
}
//去空格方法（type :be , all）
function Trim(str,type){ 
	var result=str; 
	if(type.toLowerCase()=="be"){//去首尾空格
		result = str.replace(/(^\s+)|(\s+$)/g,""); 
	}else if(type.toLowerCase()=="all") {//去所有空格
		result = result.replace(/\s/g,""); 
	}
	return result; 
}

/**
 * str 为0 null "" undefined 都会改为""
 * @param str
 * @returns
 */
function isNull(str){
	return (!!str) ? str : '';
}

/*判断页面是否在iframe标签里面
 * Author hzp
 * Date 2016/10/12
 */
function inIfream(){
	if (self != top) {  
	    return true;//在ifream标签，返回ture
	}
	return false;//不在ifream标签，返回false
}

/*获取指定时间的时间戳
 * Author hzp
 * Date 2016/10/13
 */
function getTimeStamp(t){
	var stringTime = t;//"2016-10-13 10:21:13";
	var timestamps = Date.parse(new Date(stringTime));
	timestamps = timestamps / 1000;
	return timestamps;
};

/*对比前后时间是否合理，开始时间应该小于结束时间	
 * Author hzp
 * Date 2016/10/14
 */
function contrastTime(e){
	var startTime1 = getTimeStamp($("#startTime").val());
	var endTime1 = getTimeStamp($("#endTime").val());
	if(startTime1 > endTime1){
		hbN.bs.alert({msg:"开始时间不能大于结束时间！"});
		e.find("input").val("");
	}
};



