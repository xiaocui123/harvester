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
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
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
	return len > length
}
function lengthCheckForCH(val, length) {
	var v = $.trim(val);
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

function sing_msg(msg) {
	$.messager.alert("信息", msg, "info");
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
 * Edit 2016/11/11
 */
function contrastTime(e){
	if(!e)
		return false;
	var tMark = e;//.find("input");
	if(tMark.attr("id") == 'startTime')
		$('#endTime').datetimepicker('setStartDate',tMark.val());
	else if(tMark.attr("id") == 'endTime')
		$('#startTime').datetimepicker('setEndDate',tMark.val());
	
	if(tMark.attr("id") == 'combineStartDt')
		$('#combineEndDt').datetimepicker('setStartDate',tMark.val());
	else if(tMark.attr("id") == 'combineEndDt')
		$('#combineStartDt').datetimepicker('setEndDate',tMark.val());
	
	if(tMark.attr("id") == 'creatStarTime')
		$('#creatEndTime').datetimepicker('setStartDate',tMark.val());
	else if(tMark.attr("id") == 'creatEndTime')
		$('#creatStarTime').datetimepicker('setEndDate',tMark.val());
};

function CurentTime(){
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
    clock += day + " ";
    if(hh < 10){
        clock += "0";
    }
    clock += hh + ":";
    if (mm < 10){ clock += '0'}; 
    clock += mm+':'+ss; 
    return(clock); 
};


$(function(){
	'use strict';
	if(window.formValidtor === undefined){
		window.formValidtor = new init();
	}
	
	/**
	 * 自定义细粒化验证规则
	 */
	var validators = {
		//规则名称
		idCard:{
			//消息提示
			message:'身份证验证错误',
	        validate: function(validator, $field, options) {
	        	//验证处理 返回布尔值
	        	if($field.val().length == 0){return true;}
	        	var reg = /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
	    		return reg.test($field.val());
	        }  
	    },
	    bankCard:{
			message:'银行账号验证错误',
	        validate: function(validator, $field, options) {
	        	if($field.val().length == 0){return true;}
	        	var reg = /^(\d{16}|\d{19})$/;
	    		return reg.test($field.val());
	        }  
	    },
	    touchType:{
	    	message:'联系方式错误',
	    	validate:function(validator, $field, options){
	    		if($field.val().length == 0){return true;}
	    		var reg = /(^1[3,4,5,7,8]{1}[0-9]{1}[0-9]{8}$)|(^0[0-9]{2,3}-[0-9]{7,8}$)|(^[0-9]{8}$)/;
	    		return reg.test($field.val());
	    	}
	    },
	    lenChinese1000:{
	    	message:'字符长度不能超过1000个汉字',
	    	validate:function(validator, $field, options){
	    		var val = $field.val();
	    		return val.length<=1000;
	    	}
	    },
	    lenChinese400:{
	    	message:'字符长度不能超过400个汉字',
	    	validate:function(validator, $field, options){
	    		var val = $field.val();
	    		return val.length<=400;
	    	}
	    },
	    lenChinese200:{
	    	message:'字符长度不能超过200个汉字',
	    	validate:function(validator, $field, options){
	    		var val = $field.val();
	    		return val.length<=200;
	    	}
	    },
	    lenChinese100:{
	    	message:'字符长度不能超过100个汉字',
	    	validate:function(validator, $field, options){
	    		var val = $field.val();
	    		return val.length<=100;
	    	}
	    },
	    lenChinese20:{
	    	message:'字符长度不能超过20个汉字',
	    	validate:function(validator, $field, options){
	    		var val = $field.val();
	    		return val.length<=20;
	    	}
	    },
	    lenChinese12:{
	    	message:'字符长度不能超过12个汉字',
	    	validate:function(validator, $field, options){
	    		var val = $field.val();
	    		return val.length<=12;
	    	}
	    },
	    lenChinese30:{
	    	message:'字符长度不能超过30个汉字',
	    	validate:function(validator, $field, options){
	    		var val = $field.val();
	    		return val.length<=30;
	    	}
	    },
	    selectNotEmpty:{
	    	message:'下拉列表不能为空',
	    	validate:function(validator, $field, options){
	    		var val = $field.val();
	    		return val != '-1';
	    	}
	    }
	};
	$.extend($.fn.bootstrapValidator.validators, validators);
	function init (){
		this.validAllQuery = validAllQuery;
		this.validQuery = validQuery;
	}	
	/**
	 * 验证入口
	 * query 需要验证参数
	 * reles 验证规则
	 * return 返回一个布尔值，如果有验证不通过将返回一个false
	 */

	function validAllQuery(query, rules){
		var state = true;
		for(var key in query){
			if(!validQuery(query[key], rules)){
				state = false;
				break;
			}
		}
		return state;
	}

	/**
	 * 单个对象验证
	 * queryObject 单个对象
	 * reles 验证规则
	 * return 返回一个布尔值，如果有验证不通过将返回一个false
	 */
	function validQuery(queryObject, rules){
		var state = true;
		for(var key in queryObject){
			if(!!rules[key]){
				var bol = valid(key, queryObject[key], rules[key] );
				if(!bol){
					state = bol;
					break;
				}
			}
		}
		return state;
	}
	
	/**
	 * 验证单个输入框中的值对应的每个验证规则的多个小规则
	 * name 字段名
	 * value 被验证的数据
	 * rulesObj 一个大的验证（由多个小的规则组合而成）
	 * return 返回布尔值
	 */
	function valid(name, value, rulesObj){
		// 获取当前所有最细粒的验证规则，方便使用
		var validtors = $.fn.bootstrapValidator.validators;
		var tempValidtors={};
		var state = true;
		if(rulesObj){
			for(var key in rulesObj.validators){
				tempValidtors = rulesObj.validators;
//				if(!validtors[key].message)
//					continue;
				var a = validtors[key].validate('', _getVal(value) )
				if(!a){
					var $name = $('[name='+ name +']');
					// 关闭提示时获得焦点
					hbN.bs.alert({msg:rulesObj.message}, function(){
						if($name.length > 0){
							setTimeout(function(){
								$name.focus();
							},10);
						}
					});
					state = false;
					break;
				}
			}
		}
		return state;
	}
	/**
	 * 为了兼容验证规则中的一个条件添加获取方式
	 */
	function _getVal(value){
		var _val = value;
		return {
			val:function(){
				return _val;
			},
			attr:function(a){
				
			}
		}
	}

	
});

/*
 * 提交按钮锁止，防止重复提交
 * Author hzp
 * Data   2016/11/17
 */
var limitButn = 0;
function limitButton(){
//	$("button").attr("disabled",true);
//	setTimeout(function(){$("button").removeAttr("disabled");},2000);
	var judge = limitButn;
	limitButn = 1; 
	setTimeout(function(){limitButn = 0;},3000);
	return judge;
}


var App = function(){
	var tooltip = function(msg) {
		var $html,
	        html = ('<div class="messages-list" data-top="0">'
	        		+ '<span class="glyphicon glyphicon-remove"></span>'
	        		+ '<span>' + msg + '</span>'
	            	+ '</div>');
		
	    $html = $(html);
	
	    var $messagesList = $('.messages-list');
	    $('.messages-list').each(function(index, elem) {
	        var $this = $(this);
	        var offsetTop = $this.data('top') - 31;
	        $this.data('top', offsetTop).animate({
	            'top': offsetTop
	        })
	    });

	    $('#hb-messages-box').append($html);
	    if ($messagesList.length >= 3) {
	        $messagesList.eq(0).remove();
	    }
	
	    var dataOffsetTop = -$html[0].clientWidth;
	    $html.animate({
	        'left': dataOffsetTop + 'px'
	    });

	    setTimeout(function() {
	        $html.remove();
	    }, 5000);
	
	}
	var tool = {};
	
	
	/**
	 * 左侧导航
	 */
	function sideBar(){
	    $.post(ctxPath + '/menu/getMenuMenu', {}, function(data){
	    	if(data == null) return
	    	var ele = {};
	    	var str = '';
	    	for(var i=0,len=data.length; i<len; i++){
	    		ele = data[i];
	    		str += (
	    			'<li class="nav-item' + ((i===0)?' open':'') + '">' + 
	    				'<a href="' + (ele.url || 'javascript:;') + '">' +
		    				'<i class="glyphicon ' + ele.iconCls + '"></i>' +
		    				'<span class="title">' + ele.name + '</span>' +
		    				'<span class="arrow glyphicon glyphicon-chevron-left"></span>' +
		    				'<span class="arrow glyphicon glyphicon-chevron-down"></span>' +
		    			'</a>');
	    		
	    		if(!!data[i].menus && ele.menus.length > 0){
	    			str += createSubMenu(ele.menus);
	    		}
	    		
	    		str += ('</li>')
	    	}
	    	
	    	$('.app-side-bar .sidebar-wrapper').html(str);

	    	/**
	    	 * 创建二级菜单
	    	 */
	    	function createSubMenu(arr){
	    		var str = '<ul class="sub-menu">';
	    		var ele = {};
	    		for(var i=0, len=arr.length; i<len; i++){
	    			ele = arr[i];
	    			str += (
	    				'<li>' + 
		    				'<a href="' + (ele.url || 'javascript:;') + '">' +
			    				'<i class="glyphicon ' + ele.iconCls + '"></i>'+
			    				'<span class="title">' + ele.name + '</span>'+
			    				'<span class="selected"></span>'+
		    				'</a>'+
	    				'</li>');
	    		}
	    		str += ('</ul>')
	    		return str;
	    	}
	    	
	    	

			/**	
			 * 切换展示列表
			 */
			$('.sidebar-wrapper .nav-item > a').on('click', function(e){
				e.preventDefault();

				var $this = $(this);
				var $thisNext  = $(this).next();
				if($('.app-sidebar-wrapper').hasClass('app-sidebar-close')) return false;
				
				$('.sidebar-wrapper .nav-item .sub-menu').slideUp('fast');
				$('.sidebar-wrapper .nav-item > a').removeClass('open');
				var url = $this.attr('href');
				if($this.attr('href') != 'javascript:;'){
					$('.app-sidebar-wrapper .sidebar-wrapper .nav-item').removeClass('open')
					$this.parent().addClass('open');
					
					loading({'show':true, msg:'正在加载页面..',elemName:'#proscenium'})
					$('#proscenium-box').load(ctxPath + '/' + url, function(){
						loading({'show':false})
					});
					return
				}
				
				if($thisNext.is(':hidden')){
					$this.addClass('open');
					$thisNext.slideDown('fast');
				}else{
					$this.removeClass('open');
					$thisNext.slideUp('fast');
				}
			})
			
			/**
			 * 绑定加载事件
			 */
			$('.app-sidebar-wrapper .sub-menu a').on('click', function(e){
				e.preventDefault();
				var $this = $(this);
				$('.app-sidebar-wrapper .sidebar-wrapper .nav-item').removeClass('open');
				$this.parent().parent().parent().addClass('open');
		    	var url = $this.attr('href');
		    	var html = '';
		    	if(url == 'usr/init' || url == 'deptment/init' || url == 'role/init'){
		    		html = '<iframe id="sysmngIfream"style="position:absolute; width: 100%; height: 100%; margin: 0; padding: 0; border: 0; " frameborder="0" src="' + ctxPath + '/' + url + '"></iframe>'
		    		$('#proscenium-box').css('height','100%').html(html);
		    	}else{
					loading({'show':true, msg:'正在加载页面..',elemName:'#proscenium'})
					$('#proscenium-box').load(ctxPath + '/' + url, function(){
						loading({'show':false})
					});
		    	}
			})
	    	
	    });

	    
	    /**
	     * 切换sidebar方法
	     * 通过增加和删除app-sidebar-close使用样式控制具体内容展示
	     */
		$('.app-sidebar-toggle').on('click', function(){
			var className = 'app-sidebar-close';
			var $appSidebarWraper = $('.app-sidebar-wrapper');
			if($appSidebarWraper.hasClass(className)){
				$(this).find('span').html('&lt;&lt;');
				$('.app-sidebar-wrapper').removeClass(className);
	            $('#proscenium').addClass('unfold');
			}else{
				$(this).find('span').html('&gt;&gt;');
				$('#proscenium').removeClass('unfold');
				$('.app-sidebar-wrapper').addClass(className)
			}
			//重新计算数据列表区域宽度，避免显示不全，消除滑块
			var nWidth = $(window).width()-$(".app-sidebar-wrapper").width();
			$("#_grid,#vditDiv").datagrid({
                width: nWidth-30
            });
			
//			$(".panel.datagrid").width(nWidth-25);
		})
	}
	
	function editPassword(){
//		$('#passOld').on('input', function(){
//			var thisVal = $(this).val();
//			console.log(thisVal)
//		})
//		
//		
		
		$('#passNew').on('input', function(){
			
			var passOld = $.trim($('#passOld').val());
			var passNew = $.trim($('#passNew').val());
			if(passNew != ''){
				$('#passNew').parent().removeClass('has-error');
			}else{
				$('#passNew').parent().addClass('has-error');
			}
//			if(passOld == passNew){
//				$('#passNewTwo').parent().removeClass('has-error');
//			}else{
//				$('#passNewTwo').parent().addClass('has-error');
//			}
		})
		
		$('#passNewTwo').on('input', function(){
			var thisVal = $(this).val();
			var passNew = $('#passNew').val();
			if(thisVal == passNew){
				$('#passNewTwo').parent().removeClass('has-error');
			}else{
				$('#passNewTwo').parent().addClass('has-error');
			}
		})
			
		$('#myModal').on('hidden.bs.modal', function (e) {
			$('#passOld').val('');
			$('#passNew').val('');
			$('#passNewTwo').val('');
			
			$('#passOld').parent().removeClass('has-error');
			$('#passNew').parent().removeClass('has-error');
			$('#passNewTwo').parent().removeClass('has-error');
		})
		
		
		$('#editPass-save').on('click', function(){
			var passOld = $.trim($('#passOld').val());
			var passNew = $.trim($('#passNew').val());
			var passNewTwo = $.trim($('#passNewTwo').val());
			
			if(passNew == ''){
				$('#passNew').parent().addClass('has-error');
				return false;
			}
			if(passNew != passNewTwo){
				$('#passNewTwo').parent().addClass('has-error');
				return false;
			}
			
			$.post(ctxPath + '/login/editPass', {'passOld':passOld,'passNew':passNew},function(data){
				if(data == 'pass_pwd_err'){
					$('#passOld').parent().addClass('has-error');
				}else if(data == 'pass_true'){
					hbN.bs.alert({'msg':'密码修改成功'});
					$('#myModal').modal('toggle');
				}else if(data == 'pass_true'){
					hbN.bs.alert({'msg':'更新失败'});
				}
			})
		})
	}
	
	
	
	function init(){
		sideBar();
		editPassword()
		
	}
	
	function loading(opt){
		var $appLoading = ($('#app-loading').length > 0) ? $('#app-loading') : $('<div id="app-loading"></div>');
		var option = $.extend({}, opt);
		if(!!opt.show){
			var $msg = $('<div class="app-loading-msg"><span class="text-box">' + (!!option.msg ? option.msg : '') + '</span></div>');
			$appLoading.html($msg);
			if(option.elemName){
				$appLoading.appendTo(option.elemName);
			}else{
				$appLoading.appendTo('body');
			}
			$appLoading.fadeIn('fast');
		}else{
			$appLoading.fadeOut('fast');
		}
	}
	
	return {
		tooltip:tooltip,
		tool:tool,
		init:init,
		loading:loading
	}
}()

if(!window.console){
	window.console = {
		log:function(){},
		info:function(){},
		error:function(){}
	};
}

//读取cookie值
function getCookie(name){  
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));  
    if(arr != null){  
     return unescape(arr[2]);   
    }else{  
     return null;  
    }
}

