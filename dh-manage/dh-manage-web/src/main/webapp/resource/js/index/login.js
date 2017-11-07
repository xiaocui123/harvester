$(function () {
    // init background slide images
    Metronic.init(); // init metronic core components
    Layout.init(); // init current layout
    Login.init();
    Demo.init();
    //RevosliderInit.initRevoSlider();
    $.backstretch(
        [
            "js/scripts/onepage2/img/1.jpg",
            "js/scripts/onepage2/img/2.jpg",
            "js/scripts/onepage2/img/3.jpg",
            "js/scripts/onepage2/img/4.jpg"],
        {
            fade: 1000,
            duration: 4000
        });
});

function login() {
    if ($('.login-form').validate().form()) {
        var staff_code = document.getElementById("staff_code").value;
        var staff_pwd = hex_md5(document.getElementById("staff_pwd").value);//md5加密
        console.log(staff_pwd);
        $.ajax({
            url: path + '/sys/login',
            type: "post",
            async: false,
            data: {
                userAccount: staff_code,
                userPsw: staff_pwd
            },
            success: function (data) {
                if (!data.success) {
                    bootbox.alert(data.message)
                } else {
                    window.location.href = path + "/sys/gotoSys";
                }
            },
            error: function (e) {
                bootbox.alert("登录系统信息错误!");
                return false;
            }
        });

    }
}
