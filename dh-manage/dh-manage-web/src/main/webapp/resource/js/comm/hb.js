/**
 * 海博前端框架
 * 2016-8-29
 * versions 0.0.1
 */
;(function(global){
	var version = '0.0.1';


	$.fn.qser = function(){
		var obj = {};
		
		var objs = $(this).serializeArray();
		if(objs.length != 0){
			for(var i=0; i<objs.length; i++){
				obj[objs[i].name] = objs[i].value;
			}
		}
		return obj;
	};

	/** 
	 * 将data属性转为js对象
	 */
	$.fn.qdata = function(){
		var res = {};
		var data = $(this).attr('data');
		if(data){
			var options = data.split(';');
			for(var i=0; i<options.length; i++){
				if(options[i]){
					var opt = options[i].split(':');
					res[opt[0]] = opt[1];
				}
			}
		}
		return res;
	};
	
	
	var hb = {};

	/**
	 * util.js
	 * util.type
	 * util.on
	 * util.is
	 * util.ajax
	 * util.totop
	 * util.qrcode
	 * util.end
	 * util.cookie
	 * util.getSearchParam
	 */

	/**
	 * util
	 * 工具集合
	 */
	
	var util = {};

	/**
	 * util.type
	 * 判断数据的原始数据类型
	 */
	util.type = function(obj){
		var type = {}.toString.call(obj);
		return type.split(' ')[1].slice(0, -1).toLowerCase();
	}

	/**
	 * util.on
	 * 事件绑定
	 */
	util.on = function(obj, event, func){
		$(document).off(event, obj).on(event, obj, func);
	};

	/**
	 * util.is
	 * 一些常用的判断，例如数字，手机号等
	 */
	util.is = function(str, type){
		if(str && type){
			if(type == 'number') return /^\d+$/g.test(str);
			if(type == 'mobile') return /^1\d{10}$/g.test(str);
		}
	};

	/**
	 * util.ajax
	 * 对$.ajax的封装
	 */

	util.ajaxoptions = {
		url 		: '',
		data 		: {},
		type 		: 'post',
		dataType	: 'json',
		async 		: true,
		crossDomain	: false
	};
	util.ajaxopt = function(options){
		var opt = $.extend({}, util.ajaxoptions);
		if(typeof options == 'string'){
			opt.url = options;
		}else{
			$.extend(opt, options);
		}
		
		return opt;
	};
	util.ajax = function(options, success, fail){
		if(options){
			var opt = util.ajaxopt(options);
			if(typeof base != 'undefined') opt.url = base + opt.url;
			
			$.ajax(opt).done(function(obj){
				if(success) success(obj);
			}).fail(function(e){
				if(fail){
					fail(e);
				}else{
					alert('数据传输失败，请重试！');
				}
			});
		}
	};

	/**
	 * util.totop
	 * 返回顶部的方法
	 * 可以参考：plugins/_01_qtotop/qtotop.html
	 */
	util.totop = function(el){
		var $el = $(el);
		$el.hide().click(function(){
			$('body, html').animate({scrollTop : '0px'});
		});
		
		var $window = $(window);
		$window.scroll(function(){
			if ($window.scrollTop()>0){
				$el.fadeIn();
			}else{
				$el.fadeOut();
			}
		});
	};

	
	/**
	 * util.end
	 * 到达页面底部后自动加载内容
	 * end：到达底部后的回调函数
	 * $d：容器，默认是$(window)
	 * $c：内容，默认是$(document)
	 * 可以参考：plugins/_04_qend/qend.html
	 */
	util.end = function(end, $d, $c){
		if(end){
			var $d = $d || $(window);
			var $c = $c || $(document);
			
			$d.scroll(function(){if($d.scrollTop() + $d.height() >= $c.height()) end();});
		}else{
			$(window).scroll(null);
		}
	};

	/**
	 * util.cookie
	 * 对jquery.cookie.js稍作封装
	 * 注：需要引入jquery.cookie.js，<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
	 * util.cookie(key)：返回key对应的value
	 * util.cookie(key, null)： 删除key对应的cookie
	 * util.cookie(key, value)：设置key-value的cookie
	 * 可以参考：plugins/_05_qcookie/qcookie.html
	 */
	util.cookie = function(key, value){
		if(typeof value == 'undefined'){
			return $.cookie(key);
		}else if(value == null){
			$.cookie(key, value, {path:'/', expires: -1});
		}else{
			$.cookie(key, value, {path:'/', expires:1});
		}
	};

	/**
	 * util.getSearchParam
	 * 获取url后参数中的value
	 * util.getSearchParam(key)：返回参数中key对应的value
	 * 可以参考：plugins/_06_qsearch/qsearch.html
	 */
	util.getSearchParam = function(key){
		var res;
		var s = location.search;
		if(s){
			s = s.substr(1);
			if(s){
				var ss = s.split('&');
				for(var i=0; i<ss.length; i++){
					var sss = ss[i].split('=');
					if(sss && sss[0] == key) res = sss[1]; 
				}
			}
		}
		return res;
	};




	/**
	 * bootstrap 封装
	 * bs.js
	 * 1.alert
	 * 2.confirm
	 * 3.dialog
	 * 4.msg
	 * 5.tooltip
	 * 6.popover
	 * 7.tree
	 * 8.scrollspy
	 * 9.initimg
	 * 10.bsdate
	 * 11.bstro
	 */
	var bs 	= {};

	var modaloptions = {
		id		: 'bsmodal',
		url 	: '',
		fade	: 'fade',
		close	: true,
		title	: '提示',
		head	: true,
		foot	: true,
		btn		: false,
		okbtn	: '确定',
		qubtn	: '取消',
		msg		: 'msg',
		big		: false,
		show	: false,
		remote	: false,
		backdrop: 'static',
		keyboard: true,
		style	: '',
		mstyle	: '',
		callback: null
	};
	bs.modalstr = function(opt){
		var start = '<div class="modal '+opt.fade+'" id="' + opt.id + '" tabindex="-1" role="dialog" aria-labelledby="bsmodaltitle" aria-hidden="true" style="position:fixed;top:20px;'+opt.style+'">';
		if(opt.big){
			start += '<div class="modal-dialog modal-lg" style="'+opt.mstyle+'"><div class="modal-content">';
		}else{
			start += '<div class="modal-dialog" style="'+opt.mstyle+'"><div class="modal-content">';
		}
		var end = '</div></div></div>';
		
		var head = '';
		if(opt.head){
			head += '<div class="modal-header">';
			if(opt.close){
				head += '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>';
			}
			head += '<h3 class="modal-title" id="bsmodaltitle">'+opt.title+'</h3></div>';
		}
		
		var body = '<div class="modal-body"><p><h4>'+opt.msg+'</h4></p></div>';
		
		var foot = '';
		if(opt.foot){
			foot += '<div class="modal-footer"><button type="button" class="btn btn-primary bsok">'+opt.okbtn+'</button>';
			if(opt.btn){
				foot += '<button type="button" class="btn btn-default bscancel">'+opt.qubtn+'</button>';
			}
			foot += '</div>';
		}
		
		return start + head + body + foot + end;
	};
	bs.alert = function(options, func){
		// options
		var opt = $.extend({}, modaloptions);
		
		if(util.type(options) == 'object'){
			$.extend(opt, options);
		}else{
			opt.msg = options.toString();
		}
		
		// add
		$('body').append(bs.modalstr(opt));

		// init
		var $modal = $('#' + opt.id); 
		$modal.modal(opt);
		
		// bind
		util.on('button.bsok', 'click', function(){
			if(func) func();
			$modal.modal('hide');
		});
		util.on('#' + opt.id, 'hidden.bs.modal', function(){
			$modal.remove();
		});
		
		// show
		$modal.modal('show');
	};
	bs.confirm = function(options, ok, cancel){
		// options
		var opt = $.extend({}, modaloptions);

		opt.title = '确认操作';
		if(typeof options == 'string'){
			opt.msg = options;
		}else{
			$.extend(opt, options);
		}
		opt.btn = true;
		
		// append
		$('body').append(bs.modalstr(opt));
		
		// init
		var $modal = $('#' + opt.id); 
		$modal.modal(opt);
		
		// bind
		util.on('button.bsok', 'click', function(){
			if(ok) ok();
			$modal.modal('hide');
		});
		util.on('button.bscancel', 'click', function(){
			if(cancel) cancel();
			$modal.modal('hide');
		});
		util.on('#' + opt.id, 'hidden.bs.modal', function(){
			$modal.remove();
		});
		
		// show
		$modal.modal('show');
	};
	bs.dialog = function(options, func){
		// options
		var opt = $.extend({}, modaloptions, options);
		opt.big = true;
		
		// append
		$('body').append(bs.modalstr(opt));
		
		// ajax page
		util.ajax({
			url:options.url,
			dataType:'html'
		}, function(html){
			$('#' + opt.id + ' div.modal-body').empty().append(html);
			if(options.callback) options.callback();
		});
			
		// init
		var $modal = $('#' + opt.id); 
		$modal.modal(opt);
		
		// bind
		util.on('button.bsok', 'click', function(){
			var flag = true;
			if(func){
				flag = func();
			}
			
			if(flag){
				$modal.modal('hide');
			}
		});
		util.on('button.bscancel', 'click', function(){
			$modal.modal('hide');
		});
		util.on('#' + opt.id, 'hidden.bs.modal', function(){
			$modal.remove();
		});
		
		// show
		$modal.modal('show');
	};
	bs.msgoptions = {
		msg  : 'msg',
		type : 'info',
		time : 2000,
		position : 'top',
	};
	bs.msgstr = function(msg, type, position){
		return '<div class="alert alert-'+type+' alert-dismissible" role="alert" style="display:none;position:fixed;' + position + ':0;left:0;width:100%;z-index:2001;margin:0;text-align:center;" id="bsalert"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'+msg+'</div>';
	};
	bs.msg = function(options){
		var opt = $.extend({},bs.msgoptions);
		
		if(typeof options == 'string'){
			opt.msg = options;
		}else{
			$.extend(opt, options);
		}
		
		$('body').prepend(bs.msgstr(opt.msg, opt.type , opt.position));
		$('#bsalert').slideDown();
		setTimeout(function(){
			$('#bsalert').slideUp(function(){
				$('#bsalert').remove();
			});
		},opt.time);
	};
	bs.popoptions = {
		animation 	: true,
		container 	: 'body',
		content		: 'content',
		html		: true,
		placement	: 'bottom',
		title		: '',
		trigger		: 'hover'//click | hover | focus | manual.
	};


	$.fn.bstip = function(options){
		var opt = $.extend({}, bs.popoptions);
		if(typeof options == 'string'){
			opt.title = options;
		}else{
			$.extend(opt, options);
		}
		
		$(this).data(opt).tooltip();
	};
	$.fn.bspop = function(options){
		var opt = $.extend({}, bs.popoptions);
		if(typeof options == 'string'){
			opt.content = options;
		}else{
			$.extend(opt, options);
		}
		
		$(this).popover(opt);
	};
	bs.tree = {};
	bs.tree.options = {
		url 	: '',
		height 	: '600px',
		open	: true,
		edit	: false,
		checkbox: false,
		showurl	: true
	};
	$.fn.bstree = function(options){
		var opt = $.extend({}, bs.tree.options);
		if(options){
			if(typeof options == 'string'){
				opt.url = options;
			}else{
				$.extend(opt, options);
			}
		}
		
		var res = '加载失败！';
		var $this = $(this);
		util.ajax(opt.url + '/tree', function(json){
			if(json && json.object){
				var tree = json.object;
				
				var start = '<div class="panel panel-info"><div class="panel-body" ';
				if(opt.height != 'auto') 
					start += 'style="height:600px;overflow-y:auto;"';
				start += '><ul class="nav nav-list sidenav" id="treeul" data="url:' + opt.url +';">';
				var children = bs.tree.sub(tree, opt);
				var end = '</ul></div></div>';
				res = start + children + end;
			}
			
			$this.empty().append(res);
			bs.tree.init();
		});
	};
	bs.tree.sub = function(tree, opt){
		var res = '';
		if(tree){
			var res = 
				'<li>' + 
					'<a href="javascript:void(0);" data="id:' + tree.id + ';url:' + tree.url + ';">' + 
						'<span class="glyphicon glyphicon-minus"></span>';
			if(opt.checkbox){
				res += '<input type="checkbox" class="treecheckbox" ';
				if(tree.checked){
					res += 'checked';
				}
				res += '/>';
			}
				res += tree.text;
			if(opt.showurl){
				res += '(' + tree.url + ')';
			}
			if(opt.edit)
				res += 
					'&nbsp;&nbsp;<span class="label label-primary bstreeadd">添加子菜单</span>' + 
					'&nbsp;&nbsp;<span class="label label-primary bstreeedit">修改</span>' + 
					'&nbsp;&nbsp;<span class="label label-danger  bstreedel">删除</span>';
				res += '</a>';
			var children = tree.children;
			if(children && children.length > 0){
					res += '<ul style="padding-left:20px;" id="treeid_' + tree.id + '" class="nav collapse ';
				if(opt.open) 
					res += 'in';
					res += '">';
				for(var i=0; i<children.length; i++){
					res += bs.tree.sub(children[i], opt);
				}
					res += '</ul>';
			}
			res += '</li>';
		}
		
		return res;
	};
	bs.tree.init = function(){
		util.on('#treeul .glyphicon-minus', 'click', function(){
			if($(this).parent().next().length > 0){
				$('#treeid_' + $(this).parents('a').qdata().id).collapse('hide');
				$(this).removeClass('glyphicon-minus').addClass('glyphicon-plus');
			}
		});
		util.on('#treeul .glyphicon-plus', 'click', function(){
			if($(this).parent().next().length > 0){
				$('#treeid_' + $(this).parents('a').qdata().id).collapse('show');
				$(this).removeClass('glyphicon-plus').addClass('glyphicon-minus');
			}
		});
		util.on('input.treecheckbox', 'change', function(){
			// 检测子级的
			var subFlag = $(this).prop('checked');
			$(this).parent().next().find('input.treecheckbox').each(function(){
				$(this).prop('checked', subFlag);
			});
			
			// 检测父辈的
			var parentFlag = true;
			var $ul = $(this).parent().parent().parent(); 
			$ul.children().each(function(){
				var checked = $(this).children().children('input').prop('checked');
				if(!checked) parentFlag = false;
			});
			$ul.prev().children('input').prop('checked', parentFlag);
		});
		
		bs.tree.url = $('#treeul').qdata().url;
		if(bs.tree.url){
			util.on('.bstreeadd', 'click', bs.tree.addp);
			util.on('.bstreedel', 'click', bs.tree.del);
			util.on('.bstreeedit', 'click', bs.tree.editp);
		}
	};
	bs.tree.addp = function(){
		bs.dialog({
			url 	: bs.tree.url + '/add/' + $(this).parent().qdata().id,
			title 	: '添加子菜单',
			okbtn 	: '保存'
		}, bs.tree.add);
	};
	bs.tree.add = function(){
		var res;
		util.ajax({url:bs.tree.url + '/save',data:$('#bsmodal').find('form').qser(),async: false}, function(obj){res = obj;});
		
		bs.msg(res);
		if(res && res.type == 'success'){
			crud.url = bs.tree.url;
			crud.reset();
			return true;
		}else{
			return false;
		}
	};
	bs.tree.del = function(){
		util.ajax({
			url:bs.tree.url + '/del/' + $(this).parent().qdata().id,
		}, function(res){
			bs.msg(res);
			
			if(res && res.type == 'success'){
				crud.url = bs.tree.url;
				crud.reset();
			}
		});
	};
	bs.tree.editp = function(){
		bs.dialog({
			url 	: bs.tree.url + '/savep?id=' + $(this).parent().qdata().id,
			title 	: '修改菜单',
			okbtn 	: '保存'
		}, bs.tree.edit);
	};
	bs.tree.edit = function(){
		crud.url = bs.tree.url;
		return crud.save();
	};
	bs.spy = function(target,body){
		var $body = 'body';
		var $target = '.scrolldiv';
		
		if(body) $body = body;
		if(target) $target = target;
		
		$($body).scrollspy({target:$target});
	};
	bs.initimg = function(){
		$('img').each(function(){
			var clazz = $(this).attr('class');
			if(clazz){
				if(clazz.indexOf('img-responsive') == -1) $(this).addClass('img-responsive');
			}else{
				$(this).addClass('img-responsive');
			}
		});
	};
	bs.bsdateoptions = {
		autoclose: true,
		language : 'zh-CN',
		format: 'yyyy-mm-dd'
	};
	bs.bsdate = function(selector, options){
		if(selector){
			var $element = $(selector);
			if($element.length > 0){
				var opt = $.extend({}, bs.bsdateoptions, options);
				$element.each(function(){
					$(this).datepicker(opt);
				});
			}
		}
	};
	bs.bstrooptions = {
		width 	: '500px',
		html 	: 'true',
		nbtext	: '下一步',
		place 	: 'bottom',
		title 	: '网站使用引导',
		content : 'content'
	};
	bs.bstroinit = function(selector, options, step){
		if(selector){
			var $element = $(selector);
			if($element.length > 0){
				var opt = $.extend({}, bs.bstrooptions, options);
				if(typeof options == 'string'){
					opt.content = options;
				}else{
					$.extend(opt, options);
				}
				
				$element.each(function(){
					$(this).attr({
						'data-bootstro-width'			: opt.width, 
						'data-bootstro-title' 			: opt.title, 
						'data-bootstro-html'			: opt.html,
						'data-bootstro-content'			: opt.content, 
						'data-bootstro-placement'		: opt.place,
						'data-bootstro-nextButtonText'	: opt.nbtext,
						'data-bootstro-step'			: step
					}).addClass('bootstro');
				});
			}
		}
	};
	bs.bstroopts = {
		prevButtonText : '上一步',
		finishButton : '<button class="btn btn-lg btn-success bootstro-finish-btn"><i class="icon-ok"></i>完成</button>',
		stopOnBackdropClick : false,
		stopOnEsc : false
	};
	bs.bstro = function(bss, options){
		if(bss && bss.length > 0){
			for(var i=0; i<bss.length; i++){
				bs.bstroinit(bss[i][0], bss[i][1], i);
			}
			
			var opt = $.extend({}, bs.bstroopts);
			if(options){
				if(options.hasOwnProperty('pbtn')){
					opt.prevButtonText = options.pbtn;
				}
				if(options.hasOwnProperty('obtn')){
					if(options.obtn == ''){
						opt.finishButton = '';
					}else{
						opt.finishButton = '<button class="btn btn-mini btn-success bootstro-finish-btn"><i class="icon-ok"></i>'+options.obtn+'</button>';
					}
				}
				if(options.hasOwnProperty('stop')){
					opt.stopOnBackdropClick = options.stop;
					opt.stopOnEsc = options.stop;
				}
				if(options.hasOwnProperty('exit')){
					opt.onExit = options.exit;
				}
			}
			
			bootstro.start('.bootstro', opt);
		}
	};
	bs.search = function(selector, options){
		if(!selector || !options || !options.url || !options.make || !options.back) return;
		
		var $this = $(selector);
		var zIndex = options.zIndex || 900;
		var bgColor = options.bgColor || '#fff';
		
		var $table = $('<table class="table table-bordered table-hover" style="position:absolute;display:none;margin-top:10px;width:95%;z-index:' + zIndex + ';background-color:' + bgColor + ';"></table>');
		$this.after($table);
		
		var tid,xhr;
		util.on(selector, 'keyup', function(){
			if(tid) clearTimeout(tid);
			if(xhr) xhr.abort();
			tid = setTimeout(function(){
				var code = $this.val();
				if(code){
					xhr = $.ajax({
						url: base + options.url + '?code=' + code,
						type:'get',
						dataType:'json'
					}).done(function(json){
						if(json && json.type == 'success'){
							var codes = json.object;
							if(codes && codes.length > 0){
								$table.empty();
								$.each(codes, function(i, item){
									if(item) $table.append('<tr class="qsearchtr" style="cursor:pointer;" data-id="' + item.id + '"><td>' + options.make(item) + '</td></tr>');
								});
							}
						}
						
						$table.show();
					});
				}
			}, 500);
		});
		
		util.on('tr.qsearchtr', 'click', function(){
			options.back($(this).data('id'));
			
			$this.val($(this).find('td').text());
			$table.hide();
		});
	};

	/**
	 * crud
	 */
	crud = {};
	crud.url = '';
	crud.init = function(){
		// menu click
		util.on('.menus', 'click', function(){
			var url = $(this).qdata().url;
			if(url){
				crud.url = url;
				crud.reset();
			}
		});
		crud.bindcrud();
		crud.bindpage();
	};
	crud.bindcrud = function(){
		util.on('.allcheck','change', function(){$('.onecheck').prop('checked',$(this).prop('checked'));});
		util.on('.addBtn', 'click', function(){crud.savep('添加')});
		util.on('.editbtn','click', function(){crud.savep('修改',$(this).parents('tr').qdata().id)});
		util.on('.queBtn', 'click', function(){crud.search('查询')});
		util.on('.relBtn', 'click', function(){crud.reset();});
		util.on('.delBtn', 'click', function(){crud.del();});
		util.on('.delbtn', 'click', function(){crud.del($(this).parents('tr').qdata().id);});
	};


	crud.listopt = {pageNumber:1,pageSize:10};
	crud.list = function(data){
		var opt = {url : crud.url + '/index'};
		if(data) $.extend(crud.listopt, data);
		opt.data = crud.listopt;
		opt.dataType = 'html';
		
		util.ajax(opt, function(html){$('#cruddiv').empty().append(html);});
	};
	crud.reset = function(){
		crud.listopt = {pageNumber:1,pageSize:10};
		crud.list();
	};
	crud.search = function(title){
		bs.dialog({title:title,url:crud.url + '/search'}, function(){
			crud.list($('#bsmodal').find('form').qser());
			return true;
		});
	};
	crud.savep = function(title, id){
		var url = id ? (crud.url + '/savep?id=' + id) : (crud.url + '/savep');
		bs.dialog({title:title,url:url}, function(){
			return crud.save();
		});
	};
	crud.save = function(){
		var res;
		util.ajax({
			async: false,
			url:crud.url+'/save',
			data:$('#bsmodal').find('form').qser()
		}, function(json){
			res = json;
		});
		
		bs.msg(res);
		if(res && res.type == 'success'){
			crud.list();
			return true;
		}else{
			return false;
		}
	};
	crud.del = function(id){
		var ids = [];
		
		if(id){
			ids.push(id);
		}else{
			$('.onecheck:checked').each(function(){ids.push($(this).parents('tr').qdata().id);});
		}
		
		if(!ids.length){
			bs.alert('请选择要删除的记录！');
		}else{
			bs.confirm('确认要删除所选记录吗（若有子记录也会同时删除）？',function(){
				util.ajax({
					url:crud.url+'/del',
					data:{ids:ids.join(',')}
				}, function(res){
					bs.msg(res);
					crud.list();
				});
			});
		}
	};
	crud.bindpage = function(){
		util.on('.crudfirst', 'click', function(){
			if(!$(this).parent().hasClass('disabled')){
				crud.reset();
			}
		});
		util.on('.crudprev', 'click', function(){
			if(!$(this).parent().hasClass('disabled')){
				crud.list({pageNumber:crud.listopt.pageNumber - 1});
			}
		});
		util.on('.crudnext', 'click', function(){
			if(!$(this).parent().hasClass('disabled')){
				crud.list({pageNumber:crud.listopt.pageNumber + 1});
			}
		});
		util.on('.crudlast', 'click', function(){
			if(!$(this).parent().hasClass('disabled')){
				crud.list({pageNumber:$(this).qdata().page});
			}
		});
		util.on('.cruda', 'click', function(){
			if(!$(this).parent().hasClass('disabled')){
				crud.list({pageNumber:parseInt($(this).text())});
			}
		});
		util.on('.pagesize', 'change', function(){
			var value = $(this).val();
			if(value){
				crud.listopt.pageSize = value;
			}
			
			crud.list({pageSize:value});
		});
	};



	global.hbN = hb = {
		util:util,
		bs:bs,
		crud:crud
	};


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
})(typeof window !== "undefined" ? window : this)