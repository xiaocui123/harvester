  //自定义验证方法标示符
  var oflag;
  
 	/**
     *@ easyui validate验证功能扩展 
     *@ haohs
   	 **/
$.extend($.fn.validatebox.defaults.rules, {
	 /**
     *@ 数据有效性（唯一）格式校验
     *@ haohs
     *@ value:url参数值， param:url
     */
      remote:{
    	validator:function(value,param){
    		var url=param[0];//请求url
    		var msg = param[2];//提示信息
    		var paramname=param[1];//参数名
    		var data = {};//
    		data[paramname]=value;
    		var flag;
    		$.ajax({
                    type: 'POST', 
                    url: url,
                    async:false,
                    data:data,
                    success: function(data) {
                        if(data==null || ""==data) {
                            flag = true;
                        }else{
                            flag =  false;
                        }
                    }
             });
    		return flag;
    	},
    	message:"{2}！"
    },
     /**
     *@ 身份证格式校验
     *@ haohs
     *@ value 验证文本
     */
    idnumber:{
    	validator:function(value,param){
    		reg=/^[1-9][0-9]{14}$|^[1-9]{1}[0-9]{16}[0-9xX]{1}$/;
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'身份证号码格式错误！'
    },
     /**
     *@ 整数格式校验
     *@ haohs
     *@ value 验证文本，param 最大/最小值
     */
    int:{
    	validator:function(value,param){
    		var min = param[0];
    		var max = param[1];
    		var reg = reg=/^(-?[1-9]\d*)|0$/;
    		if(reg.test(value)){
    			if(("undefined"==min || null==min || ""==min)&&( "undefined"==max || null==max || ""==max) ){//无参数
    				return true;
    			}else if(("undefined"!=min || null!=min || ""!=min)&&( "undefined"!=max || null!=max || ""!=max)){//正常两位参数
    				if(min<=value && max>=value){
	    				return true;
	    			}
    			}else{ //参数错误
    				return false;
    			}
    		}
    		return false;
    	},
    	message:'请输入{0}到{1}的整数！'
    },
    /**
     *@ 浮点数格式校验
     *@ haohs
     *@ value 验证文本，param 小数位数
     */
    float:{
    	validator:function(value,param){
    		var fnum = param[0];//小数位数
    		var reg = new RegExp("^-?(\\d+(\\.\\d{1,"+fnum+"})?)$");
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'输入内容最多包含{0}位小数！'
    },
    /**
     *@ 电话号码格式校验
     *@ haohs
     *@ value 验证文本
     */
    phone:{
    	validator:function(value,param){
    		var reg= /^(\d{3,4}\-)?\d{5,9}(\-\d{3,5})?$/;
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'电话号码格式错误！'
    },
    /**
     *@ 手机号码格式校验
     *@ haohs
     *@ value 验证文本
     */
    mobile:{
    	validator:function(value,param){
    		var reg= /^1[3,5,8]{1}\d{9}$/;
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'手机号码格式错误！'
    },
     /**
     *@ 联系方式格式校验
     *@ haohs
     *@ value 验证文本
     */
    mobilephone:{
    	validator:function(value,param){
    		var reg= /^((1[3|5|8]{1}\d{9})|((\d{3,4}\-)?\d{5,9}(\-\d{3,5})?))$/;
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'联系方式号码格式错误！'
    },
     /**
     *@ 多个联系方式格式校验
     *@ haohs
     *@ value 验证文本
     */
    mobilephones:{
    	validator:function(value,param){
    		var reg= /^((1[3|5|8]{1}\d{9})|((\d{3,4}\-)?\d{5,9}(\-\d{3,5})?))((\,((1[3|5|8]{1}\d{9})|((\d{3,4}\-)?\d{5,9}(\-\d{3,5})?)))+)?$/;
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'联系方式号码格式错误，多个联系方式以“,”分隔！'
    },
    /**
     *@ 日期格式校验
     *@ haohs
     *@ param 日期格式符
     */
    date:{
    	validator:function(value,param){
    		var reg=new RegExp("^([1-2]\\d{3})\\"+param[0]+"(0?[1-9]|10|11|12)\\"+param[0]+"((0[1-9]{1})|([1-2]{1}[0-9]{1})|30|31)$");
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'请输入正确的日期格式！如:[ 2000{0}01{0}01 ]'
    },
    /**
     *@ 时间格式校验
     *@ haohs
     *@ value 验证文本
     */
    time:{
    	validator:function(value,param){
    		var reg=new RegExp("^((0?[1-9]{1})|(1[0-9]{1})|20|21|22|23)\\:(0?[1-9]{1}|([1-5]{1}[0-9]{1}))\\:(0?[1-9]{1}|([1-5]{1}[0-9]{1}))$");
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'请输入正确的时间格式！如:[ 12:01:01 ]'
    },
    /**
     *@ 日期时间格式校验
     *@ haohs
     *@ param 日期格式符
     */
    datetime:{
    	validator:function(value,param){
    		var reg=new RegExp("^([1-2]\\d{3})\\"+param[0]+"(0?[1-9]|10|11|12)\\"+param[0]+"((0[1-9]{1})|([1-2]{1}[0-9]{1})|30|31)\\s((0?[1-9]{1})|(1[0-9]{1})|20|21|22|23)\\:(0?[1-9]{1}|([1-5]{1}[0-9]{1}))\\:(0?[1-9]{1}|([1-5]{1}[0-9]{1}))$");
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'请输入正确的日期时间格式！如:[ 2000{0}01{0}01 12:01:01 ] '
    },
    /**
     *@ 邮编格式校验
     *@ haohs
     *@ value 验证文本
     */
    postcode:{
    	validator:function(value,param){
    		var reg = /^[1-9]\d{5}(?!\d)$/;
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'邮编格式错误！'
    },
    /**
     *@ 两值比较校验
     *@ haohs
     *@ value验证文本，param 目标对象id
     */
    equals:{
    	validator:function(value,param){
    		return $("#"+param).val()==value;
    	},
    	message:'两次输入不一致！'
    },
    /**
     *@ 限制输入禁止项字符校验
     *@ haohs
     *@ value 验证文本，param 禁用字符串
     */
    limitchar:{
    	validator:function(value,param){
    		var parr= "";
    		for(var i=0;i<param[0].length;i++){
   				parr+="\\"+param[0].substr(i,1);
    		}
    		var reg = new RegExp("["+parr+"]",'im');
   			if(reg.test(value.replace("\\","\0"))){
    			return false;
    		}
    		return true;
    	},
    	message:'请勿输入禁用字符！如 {0}'
    },
    /**
     *@ 英文或汉字格式校验
     *@ haohs
     *@ value 验证文本
     */
    enchina:{
    	validator:function(value,param){
    		var reg= /^([\u4E00-\uFA29]*[a-z]*[A-Z]*)+$/;
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'只允许输入英文或汉字！'
    },
    /**
     *@ 英文格式校验
     *@ haohs
     *@ value 验证文本
     */
    en:{
    	validator:function(value,param){
    		var reg= /^([a-z]*[A-Z]*)+$/;
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'只允许输入英文！'
    },
    /**
     *@ 汉字格式校验
     *@ haohs
     *@ value 验证文本
     */
    china:{
    	validator:function(value,param){
    		var reg= /^([\u4E00-\uFA29]*)+$/;
    		if(!reg.test(value)){
    			return false;
    		}
    		return true;
    	},
    	message:'只允许输入汉字！'
    },
    /**
     *@ 自定义验证方法校验
     *@ haohs
     *@ value 验证文本，param: 1 method 方法名,2 message 错误提示信息
     */
    others:{
    	validator:function(value,param){
    		var func=new Function(param[0]+"();");
    		if(typeof func === 'function' && typeof func!='undefined'){
    			try{
    				func();
    				return oflag;
    			}catch(e){
    				$.fn.validatebox.defaults.rules.others.message = '自定义验证方法不存在！';
    				return oflag;
    			}
    		}
    	},
    	message:'{1}'
    },
    /**
     *@ 去空格
     *@ haohs
     *@ value 操作文本 param：type 去空格类型，id 目标对象id
     */
    replace:{
    	validator:function(value,param){
    		var type = param[0];
    		var id = param[1];
    		$("#"+id).val(Trim($("#"+id).val(),type));
    		return 1;
    	}
    }
});