(function ($) {
    var printAreaCount = 0;
    $.fn.printArea = function () {
        var ele = $(this);
        var idPrefix = "printArea_";
        removePrintArea(idPrefix + printAreaCount);
        printAreaCount++;
        var iframeId = idPrefix + printAreaCount;
        var iframeStyle = 'position:absolute;width:0px;height:0px;left:-500px;top:-500px;';
        iframe = document.createElement('IFRAME');
        $(iframe).attr({
            style: iframeStyle,
            id: iframeId
        });
        document.body.appendChild(iframe);
        var doc = iframe.contentWindow.document;
        $(document).find("link").filter(function () {
            return $(this).attr("rel").toLowerCase() == "stylesheet";
        }).each(
                function () {
                    doc.write('<link type="text/css" rel="stylesheet" href="'
                            + $(this).attr("href") + '" >');
                });
        doc.write('<div class="' + $(ele).attr("class") + '">' + $(ele).html()
                + '</div>');
                
        doc.close();
        var frameWindow = iframe.contentWindow;
//        if(isIE){
//        	var WebBrowser = '<OBJECT classid=CLSID:8856F961_340A_11D0_A96B_00C04FD705A2 height=0 id=WebBrowser width=0></OBJECT>';
//        	frameWindow.document.body.insertAdjacentHTML('beforeEnd', WebBrowser);//在body标签内加入html（WebBrowser activeX控件）
////        	frameWindow.document.all.WebBrowser.ExecWB(7,1);
//        	WebBrowser.ExecWB(7, 1);//打印预览
//        }else{
        	frameWindow.close();
       		frameWindow.focus();
        	frameWindow.print();
//        }
        
    }
    var removePrintArea = function (id) {
        $("iframe#" + id).remove();
    };
})(jQuery);

//
//$.fn.printview = function () {
//    	if(isIE){//是IE
//		if(!isActiveXObject){//没有设置
//			alert("请调整IE的安全级别。\n\n具体操作：\n\n"+"工具 → Internet选项 → 安全 → 自定义级别 → 对没有标记为安全的ActiveX进行初始化和脚本运行 → 启用");
//		}else{
//			var WebBrowser = '<OBJECT ID="WebBrowser1" WIDTH=0 HEIGHT=0 CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>';
//			document.body.insertAdjacentHTML('beforeEnd', WebBrowser);//在body标签内加入html（WebBrowser activeX控件）
//			WebBrowser1.ExecWB(7, 1);//打印预览
//			WebBrowser1.outerHTML = "";//从代码中清除插入的html代码
//		}
//	}else{//是谷歌
////			this.printArea();
//	}