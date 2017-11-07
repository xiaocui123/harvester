
$(function() {
	$('#loading').fadeOut('500');
	
	App.init();
	
    $(document).mouseup(function(e) {
        // 设置目标区域
        var _con = $('#nav-list, #nav-list-btn');
        if (!_con.is(e.target) && _con.has(e.target).length === 0) {
            $('#nav-list').hide();
        }
    });

    /*$('#proscenium').tinyscrollbar();*/

//
//    // 切换左侧导航
//    $('#left-bar .menu-btn-toggle').on('click', function() {
//        var className = 'unfold';
//        if ($('#left-bar').hasClass(className)) {
//            $('#left-bar [data-toggle="tooltip"]').tooltip();
//            $(this).find('.glyphicon')
//                .addClass('glyphicon-forward')
//                .removeClass('glyphicon-backward');
//            $('#left-bar').removeClass(className);
//            $('#proscenium').removeClass(className);
//        } else {
//            $('#left-bar [data-toggle="tooltip"]').tooltip('destroy');
//            $(this).find('.glyphicon')
//                .addClass('glyphicon-backward')
//                .removeClass('glyphicon-forward');
//            $('#left-bar').addClass(className);
//            $('#proscenium').addClass(className);
//        }
//    });

    // 切换显示导航列表
    $('#nav-list-btn').on('click', function() {
        var $navList = $("#nav-list");
        if ($navList.is(":hidden")) {
            $("#nav-list").show();
//            setTimeout(function() {
//                $('#nav-list').tinyscrollbar();
//            }, 0)
        } else {
            $("#nav-list").hide();
        }
    });

//    $('#add-message').on('click', function() {
//        var $html,
//            html = ('<div class="messages-list" data-top="0">' +
//                '<span class="glyphicon glyphicon-remove"></span>' +
//                '<span>' + Math.floor(Math.random() * 1111111111111) + '</span>' +
//                '</div>');
//        $html = $(html);
//
//        var $messagesList = $('.messages-list');
//        $('.messages-list').each(function(index, elem) {
//            var $this = $(this);
//            var offsetTop = $this.data('top') - 31;
//            $this.data('top', offsetTop).animate({
//                'top': offsetTop
//            })
//        });
//
//        $('#hb-messages-box').append($html);
//
//
//        if ($messagesList.length >= 3) {
//            $messagesList.eq(0).remove();
//        }
//
//
//        var dataOffsetTop = -$html[0].clientWidth;
//        $html.animate({
//            'left': dataOffsetTop + 'px'
//        });
//
//        setTimeout(function() {
//            $html.remove();
//        }, 5000);
//
//    });
    $('#hb-messages-box').on('click', '.glyphicon-remove', function(){
    	$(this).parent().remove();
    })

    
    $('#left-bar .menu-list').on('click', '.menu-out-box', function(){
    	var url = $(this).data('url');
    	var html = '';
    	if(url == 'usr/init' || url == 'deptment/init' || url == 'role/init'){
    		html = '<iframe style=" width: 100%; height: 100%; margin: 0; padding: 0; border: 0; " src="' + ctxPath + '/' + url + '"></iframe>'
    		$('#proscenium-box').css('height','100%').html(html);
    	}else{
    		$('#proscenium-box').load(ctxPath + '/' + url);
    	}
    })
    
    //关闭 关于弹窗
    $("#hzp161011_about").find(".hzp161010_about_close").click(function(){
    	$(this).parent().parent().fadeOut();
    });
    
    //登陆后判断角色，跳转页面
    judgeRole();
    
    
//    $.post(ctxPath + '/menu/getMenus', {'type':'left', 'pid':1001}, function(data){
//    	var menuHtml = '',
//    		el = {};
//    	for(var i=0;i<data.length;i++){
//    		el = data[i];
//    		menuHtml += (            
//    		'<div class="menu-btn">'+
//    			'<div class="menu-out-box" data-url="' + el.url + '">'+
//    				'<div class="menu-btn-box" data-toggle="tooltip" data-placement="right" title="'+ el.name + '">'+
//    					'<span class="glyphicon glyphicon-th"></span>'+
//    				'</div>'+
//    				'<span class="btn-title">' + el.name + '</span>'+
//    			'</div>'+
//    		'</div>');
//    	}
//
//    	
//    	
////    	临时用 用户管理
//		menuHtml += (            
//	    		'<div class="menu-btn">'+
//	    			'<div class="menu-out-box" data-url="usr/init">'+
//	    				'<div class="menu-btn-box" data-toggle="tooltip" data-placement="right" title="用户管理">'+
//	    					'<span class="glyphicon glyphicon-th"></span>'+
//	    				'</div>'+
//	    				'<span class="btn-title">用户管理</span>'+
//	    			'</div>'+
//	    		'</div>');
//		menuHtml += (            
//	    		'<div class="menu-btn">'+
//	    			'<div class="menu-out-box" data-url="deptment/init">'+
//	    				'<div class="menu-btn-box" data-toggle="tooltip" data-placement="right" title="部门管理">'+
//	    					'<span class="glyphicon glyphicon-th"></span>'+
//	    				'</div>'+
//	    				'<span class="btn-title">部门管理</span>'+
//	    			'</div>'+
//	    		'</div>');
//		menuHtml += (            
//	    		'<div class="menu-btn">'+
//	    			'<div class="menu-out-box" data-url="role/init">'+
//	    				'<div class="menu-btn-box" data-toggle="tooltip" data-placement="right" title="角色管理 ">'+
//	    					'<span class="glyphicon glyphicon-th"></span>'+
//	    				'</div>'+
//	    				'<span class="btn-title">角色管理</span>'+
//	    			'</div>'+
//	    		'</div>');
//    	
//    	$('#left-bar .menu-list').html(menuHtml);
//        $('#left-bar [data-toggle="tooltip"]').tooltip();
//    });

//    $.post(ctxPath + '/menu/getMenus', {'type':'left','pid':1001}, function(data){
////    	console.log(data);
//    });
});
/*弹出 关于窗口
 * Author：hzp
 * Date：2016/10/10
 */
function showAbout(){
	$("#hzp161011_about").fadeIn();
}
/*登陆后判断角色，跳转页面
 * Author：hzp
 * Date：2016/10/10
 */
function judgeRole(){

	var html,url;
	switch(dqcookie){
		case 'alarm':
		url = "alarm/inputInit";
		break;
		
		case 'other':
		url = "menu/init";
		break;
		
		default:
		url = "menu/init";
		break;
	}
	$('#proscenium-box').load(ctxPath + '/' + url);
}
