var Login = function () {

    var handleLogin = function () {
        $('.login-form').validate({
            errorElement: 'span', // default input error message container
            errorClass: 'help-block', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            rules: {
                staff_code: {
                    required: true
                },
                staff_pwd: {
                    required: true
                }
            },

            messages: {
                staff_code: {
                    required: "账号不能为空."
                },
                staff_pwd: {
                    required: "密码不能为空."
                }
            },

            invalidHandler: function (event, validator) { // display error
                // alert on form
                // submit
                $('.alert-danger', $('.login-form')).show();
            },

            highlight: function (element) { // hightlight error inputs
                $(element).closest('.form-group').addClass('has-error'); // set
                // error
                // class
                // to
                // the
                // control
                // group
            },

            success: function (label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },

            errorPlacement: function (error, element) {
                error.insertAfter(element.closest('.input-icon'));
            },


            submitHandler: function (form) {
                form.submit();
            }

        });

        $('.login-form input').keypress(function (e) {
            if (e.which == 13) {
                if ($('.login-form').validate().form()) {
                    login();
                }
                return false;
            }
        });
    }

    var handleForgetPassword = function () {
        $('.forget-form').validate({
            errorElement: 'span', // default input error message container
            errorClass: 'help-block', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "",
            rules: {
                email: {
                    required: true,
                    email: true
                }
            },

            messages: {
                email: {
                    required: "请输入Email地址",
                    email: "请输入正确的email地址"
                }
            },

            invalidHandler: function (event, validator) { // display error
                // alert on form
                // submit

            },

            highlight: function (element) { // hightlight error inputs
                $(element).closest('.form-group').addClass('has-error'); // set
                // error
                // class
                // to
                // the
                // control
                // group
            },

            success: function (label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },

            errorPlacement: function (error, element) {
                error.insertAfter(element.closest('.input-icon'));
            },

            submitHandler: function (form) {
                form.submit();
            }
        });

        $('.forget-form input').keypress(function (e) {
            if (e.which == 13) {
                if ($('.forget-form').validate().form()) {
                    $('.forget-form').submit();
                }
                return false;
            }
        });

        jQuery('#forget-password').click(function () {
            jQuery('.login-form').hide();
            jQuery('.forget-form').show();
        });

        jQuery('#back-btn').click(function () {
            jQuery('.login-form').show();
            jQuery('.forget-form').hide();
        });

    }

    var handleRegister = function () {

        function format(state) {
            if (!state.id)
                return state.text; // optgroup
            return "<img class='flag' src='../../assets/global/img/flags/"
                + state.id.toLowerCase() + ".png'/>&nbsp;&nbsp;"
                + state.text;
        }


        $('.update-form').validate({
            errorElement: 'span', // default input error message container
            errorClass: 'help-block', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "",
            rules: {
                register_password: {
                    required: true,
                    minlength: 6
                },
                rpassword: {
                    required: true,
                    equalTo: "#register_password",
                    minlength: 6
                }
            },

            messages: { // custom messages for radio buttons and checkboxes
                register_password: {
                    required: "请输入密码",
                    minlength: "密码不能小于6个字符"
                },
                rpassword: {
                    required: "请输入确认密码",
                    minlength: "确认密码不能小于6个字符",
                    equalTo: "两次输入密码不一致不一致"
                }
            },

            invalidHandler: function (event, validator) { // display error
                // alert on form
                // submit

            },

            highlight: function (element) { // hightlight error inputs
                $(element).closest('.form-group').addClass('has-error'); // set
                // error
                // class
                // to
                // the
                // control
                // group
            },

            success: function (label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },

            errorPlacement: function (error, element) {
                if (element.attr("name") == "tnc") { // insert checkbox
                    // errors after the
                    // container
                    error.insertAfter($('#register_tnc_error'));
                } else if (element.closest('.input-icon').size() === 1) {
                    error.insertAfter(element.closest('.input-icon'));
                } else {
                    error.insertAfter(element);
                }
            },

            submitHandler: function (form) {
                //document.getElementById("staff_pwd_reg").value=hex_md5(document.getElementById("register_password").value);

                form.submit();
            }
        });
        $('.register-form input').keypress(function (e) {
            if (e.which == 13) {
                if ($('.register-form').validate().form()) {
                    $('.register-form').submit();
                }
                return false;
            }
        });

        jQuery('#register-btn').click(function () {
            jQuery('.login-form').hide();
            jQuery('.register-form').show();
        });

        jQuery('#register-back-btn-sm').click(function () {
            jQuery('.login-form').show();
            jQuery('.register-form').hide();
        });
        jQuery('#register-back-btn').click(function () {
            jQuery('.login-form').show();
            jQuery('.register-form').hide();
        });
    }

    return {
        // main function to initiate the module
        init: function () {

            handleLogin();
            handleForgetPassword();
            handleRegister();
        }

    };

}();