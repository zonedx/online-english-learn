function login() {
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/login/api",
        type: "post",
        data: {
            username: $("#username").val(),
            email: $("#username").val(),
            password: $("#password").val(),
        },
        success: function (data) {
            if (data.code == 200) {
                $.cookie("token", data.data.token);
                $.cookie("username", data.data.username);
                window.location.href = "http://localhost:8080/onlineEnglishLearning/index";
                //$(window).attr('location', "index.html?token='data.data.token'");
            }
            else if (data.code == 400) {
                swal("登陆失败","该帐户未被激活","error");
            } else if (data.code == 445) {
                swal("登陆失败","用户名密码错误，请重新登陆","error");
            }
        }
    });
}

function loginCheck() {
    var username = $.cookie("username");
    if (username != null) {
        alert(username)
        $("#loginusername").hide()
        alert(username + 1)
    }
}

function tanslate() {
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/findTranslation/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            english: $("#english").html()
        },
        success: function (data) {
            if (data.code == 200) {
                $("#translate").text(data.data).css("fonts-size", "25px");
            } else {
                alert("该单词错误");
            }
        },
        error: function () {
            alert("请登陆后查看")
        }
    });
}

function findTranslate() {
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/findTranslation/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            english: $("#inputenglish").val()
        },
        success: function (data) {
            if (data.code == 200) {
                $("#english").text($("#inputenglish").val());
                $("#translate").text(data.data);
                $("#photo").attr("src", null);
            }
            if (data.code == 400) {
                swal("查询失败",data.messeage,"error");
            }
        },
        error: function (data) {
            swal("请登陆后查看");
        }
    });
}

function searchBook() {
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    var name = $("#search").val();
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/findBooksByCondition/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            name: name,
            condition: '4'
        },
        success: function () {
            window.location.href = "http://localhost:8080/onlineEnglishLearning/findBooksByCondition/api?name=" + name + "&condition=4";
        }
    });
}

function addToCart(obj) {
    var id = $(obj).attr("name");
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/addToCart/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            bookId: id,
            amount: $('#amount').val()
        },
        success: function (data) {
            if (data.code == 200) {
                swal("加入购物车成功");
            }
            if (data.code == 400) {
                swal("加入失败，购物车中已存在此商品");
            }
        }
    });
}

function removeCart(obj) {
    var id = $(obj).attr("name");
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    if (confirm("确认移除购物车？")) {
        $.ajax({
            url: "http://localhost:8080/onlineEnglishLearning/removeCart/api",
            type: "post",
            headers: {'token': $.cookie('token')},
            data: {
                id: id,
            },
            success: function (data) {
                if (data.code == 200) {
                    swal("移除购物车成功");
                    setTimeout(function () {
                        window.location.reload();
                    },2000);
                }
            }
        });
    }
}

function deleteOrder(obj) {
    var id = $(obj).attr("name");
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    if (confirm("确认删除订单？")) {
        $.ajax({
            url: "http://localhost:8080/onlineEnglishLearning/deleteOrder/api",
            type: "post",
            headers: {'token': $.cookie('token')},
            data: {
                id: id,
            },
            success: function (data) {
                if (data.code == 200) {
                    swal("删除订单成功");
                    setInterval(function () {
                        window.location.reload();
                    },2000);
                }
            }
        });
    }
}

function show(obj) {
    var id = $(obj).attr("name");
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/removeCart/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            id: id,
        },
        success: function (data) {
            if (data.code == 200) {
                swal("移除购物车成功");
                window.location.reload();
            }
            if (data.code == 400) {
                swal("移除购物车失败");
            }
        }
    });
    // var id=$(obj).attr("name");
    // alert('id is '+id);
}

function getScore() {
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/getScore/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        success: function (data) {
            if (data.code == 200) {
                alert(data.data.score);
            }
            alert("获取积分失败");
        }
    });
}

function findCorrectWord(obj) {
    var str = $(obj).attr("name");
    //设置ajax请求完成后运行的函数,
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                setTimeout(function () {
                    win.location.href = xhr.getResponseHeader("CONTENTPATH");
                },2000);//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/findCorrectWord/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            sentenceId: str
        },
        success: function (data) {
            if (data.code == 200) {
                swal("答案是："+ data.data);
            }
        },
        error: function () {
            swal("","请登陆后查看","warning")
        }
    })
}

function checkWord(obj) {
    var id = $(obj).attr("name");
    var str = $(obj).attr("id");

    //if(event.keyCode==13){//event.keyCode==9  Tab不起作用
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/checkFillWord/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            fillword: $("#" + str).val(),
            sentenceId: id,
        },
        success: function (data) {
            if (data.code == 200) {
                swal("回答正确，并增加用户积分");
                $("#" + str).css("color", "green");
            } else if (data.code == 201) {
                $("#" + str).css("color", "green");
                swal("回答正确，但由于已看过答案，故不增加用户积分");
            }
            else if (data.code == 400) {
                $("#" + str).css("color", "red")
            }
        },
        error: function () {
            swal("","请登陆后答题","warning")
        }
    })

}

function selectwords() {
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/findAllWords/api",
        type: "post",
        header: {
            'Content-Type': 'application/json',
            'token': currentToken
        }
    });
}

function forgotTag(td) {
    var english = $(td).attr("name");
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/forgotTag/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            english: english
        },
        success: function (data) {
            if (data.code == 200) {
                swal("标记成功，增加用户积分");
            } else {
                swal("标记失败");
            }
        },
        error: function () {
            swal("请登陆后操作");
        }
    });
}

function getGrade() {
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/getGrade/api",
        headers: {'token': $.cookie('token')},
        data: $('#form').serialize(),
        success: function (data) {
            swal("答题得分:" + data.data + "分");
        },
        error: function () {
            swal("请登陆后答题");
        }
    })
}

function calGameScore() {
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/calScore/api",
        type: "post",
        success: function (data) {
            swal("成绩为:" + data.data + "分");
        },
        error: function () {
            swal("请游戏后获得积分");
        }
    })
}

function confirmOrder(obj) {
    var id = $(obj).attr("name");
    var token = $.cookie('token');
    var firstImage = $('#firstImage')[0].src;
    var name = $('#bookName').text();
    var price = $('#bookPrice').text();
    var amount = $('#amount').val();
    window.location.href = "http://localhost:8080/onlineEnglishLearning/confirmOrder/api?token="
        + token + "&id=" + id + "&picturePlace=" + firstImage + "&name=" + name + "&price=" + price + "&amount=" + amount;
}

function confirmOrder2(obj) {
    var id = $(obj).attr("name");
    var token = $.cookie('token');
    var firstImage = $('#firstImage' + id)[0].src;
    var name = $('#bookName' + id).text();
    var price = $('#price' + id).text();
    var amount = $('#shang' + id).val();
    window.location.href = "http://localhost:8080/onlineEnglishLearning/confirmOrder/api?token="
        + token + "&id=" + id + "&picturePlace=" + firstImage + "&name=" + name + "&price=" + price + "&amount=" + amount;
}

function purchaseBook() {
    var address = $('input[name="address"]:checked').val();
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/purchaseBooks/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            addressId: address,
            bookId: $('#bookId').text(),
            amount: $('#amount').text()
        },
        success: function (data) {
            if (data.code == 200) {
                swal("购买成功","购买成功","success");
                window.location.href="/onlineEnglishLearning/findAllOrders/api?token="+$.cookie('token');
            }
        },
        error: function (data) {
            swal("购买失败","积分不足|库存不足（请检查）","error");

        }
    });
}

function ass() {
    alert("941025835@qq.com".indexOf("@qq"))
}

function jia(obj) {
    var id = $(obj).attr("name");
    var amount = $('#shang' + id).val();
    amount = parseInt(amount) + 1;
    var price = $('#price' + id).text();
    var price1 = amount * price;
    $('#' + id).text(price1);
    $('#shang' + id).val(amount);
}

function jian(obj) {
    var id = $(obj).attr("name");
    var amount = $('#shang' + id).val();
    amount = parseInt(amount) - 1;
    var price = $('#price' + id).text();
    if (amount < 1) {
        $('#' + id).text(0);
        $('#shang' + id).val(0);
    } else {
        var price1 = amount * price;
        $('#' + id).text(price1);
        $('#shang' + id).val(amount);
    }
}

function updateLogo() {
    var formData = new FormData();
    formData.append("logo", $("#logo")[0].files[0]);
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/updateUserLogo/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: formData,
        contentType: false,
        processData: false,
        success: function (data) {
            if (data.code == 200) {
                swal("修改头像成功")
            }
        },
        error: function (data) {
            swal("请选择图片")
        }
    });
}

function addNewAddress() {
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/addNewAddress/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            recipient: $("#recipient").val(),
            recipientPhone: $("#recipientPhone").val(),
            address: $("#address").val(),
        },
        success: function (data) {
            if (data.code == 200) {
                swal("成功","新增地址成功","success")
            }
            if (data.code == 400) {
                swal("失败","新增地址失败,收货信息（收货人、收货电话、收货地址）不能为空","error")
            }
        },
        error: function (data) {
            swal("未知错误")
        }
    });
}

function updateUserInfo() {
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/updateUserInfo/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            username: $("#username").val(),
            introduce: $("#introduce").val()
        },
        success: function (data) {
            if (data.code == 200) {
                swal("保存成功",data.messeage,"success");
            }
            if (data.code == 400) {
                swal("保存失败",data.messeage,"error");
            }
        },
        error: function (data) {
            swal("未知错误")
        }
    });
}

function cz() {
    var p = $('input[name="price"]:checked').val();
    if (p == 0) {
        p = $('#other').val();
        if (p == "") {
            swal('请输入正确金额');
        }
    }
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        async: false,
        url: "http://localhost:8080/onlineEnglishLearning/chongzhi/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            qrcodeUrl: p
        },
        success: function (data) {
            $('#erweima').attr('width', "340px");
            $('#erweima').attr('height', "340px");
            $('#erweima').attr('src', "http://localhost:8080/onlineEnglishLearning/chongzhi/api?qrcodeUrl=" + p + "&token=" + $.cookie('token'));
            $.ajax(
                {
                    async: false,
                    url: "http://localhost:8080/onlineEnglishLearning/chongzhiConfirm/api",
                    type: "post",
                    headers: {'token': $.cookie('token')},
                    data: {
                        points: p
                    }
                }
            );
        }
    });
}

function usernameConfirm() {
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/usernameConfirm/api",
        type: "post",
        data: {
            username:$("#username").val()
        },
        success: function (data) {
            if(data.code==200){
                $("#usernameConfirm").text(data.messeage);
                $("#usernameConfirm").css("color","green");
            }else if(data.code==400){
                $("#usernameConfirm").text(data.messeage);
                $("#usernameConfirm").css("color","red");
            }
        }
    });
}
function emailConfirm() {
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/emailConfirm/api",
        type: "post",
        data: {
            email:$("#email").val()
        },
        success: function (data) {
            if(data.code==200){
                $("#emailConfirm").text(data.messeage);
                $("#emailConfirm").css("color","green");
            }else if(data.code==400){
                $("#emailConfirm").text(data.messeage);
                $("#emailConfirm").css("color","red");
            }
        }
    });
}
function phoneConfirm() {
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/phoneConfirm/api",
        type: "post",
        data: {
            phone:$("#phone").val()
        },
        success: function (data) {
            if(data.code==200){
                $("#phoneConfirm").text(data.messeage);
                $("#phoneConfirm").css("color","green");
            }else if(data.code==400){
                $("#phoneConfirm").text(data.messeage);
                $("#phoneConfirm").css("color","red");
            }
        }
    });
}
function register() {
    var email=$("#email").val();
    if($("#password").val()==$("#passwordVerify").val()){
        $.ajax({
            url: "http://localhost:8080/onlineEnglishLearning/register/api",
            type: "post",
            data: {
                username:$("#username").val(),
                password:$("#password").val(),
                email:email,
                phone:$("#phone").val()
            },
            success: function (data) {
                if(data.code==200){
                    $("#emailConfirm").text("点击前往");
                    $("#emailConfirm").css("color","blue");
                    if(email.indexOf("@qq")!=-1){
                        swal(data.messeage);
                        $("#emailConfirm").attr("href","https://mail.qq.com/cgi-bin/loginpage");
                    }else {
                        $("#emailConfirm").attr("href","https://mail.163.com/");
                    }
                }else if(data.code==400){
                    swal(data.messeage);
                }
            }
        });
    }else {
        swal("两次输入密码不一致");
    }

}
function resetPassword() {
    if($("#newPassword").val()!=$("#confirmPassword").val()){
        swal("保存失败","两次密码输入不一致","error")
    }else {
        $.ajaxSetup({
            complete: function (xhr, status) {
                if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                    var win = window;
                    while (win != win.top) {
                        win = win.top;
                    }
                    win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
                }
            }
        });
        $.ajax({
            url: "http://localhost:8080/onlineEnglishLearning/resetPassword/api",
            type: "post",
            headers: {'token': $.cookie('token')},
            data: {
                token:$.cookie('token'),
                oldPswd:$("#oldPassword").val(),
                newPswd:$("#newPassword").val()
            },
            success: function (data) {
                if(data.code==200){
                    swal("保存成功",data.messeage,"success");
                    window.location.reload()
                }else {
                    swal("保存失败",data.messeage,"error")
                }
            }
        });
    }

}
function exitLogin() {
    $.ajaxSetup({
        complete: function (xhr, status) {
            if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
                var win = window;
                while (win != win.top) {
                    win = win.top;
                }
                win.location.href = xhr.getResponseHeader("CONTENTPATH");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            }
        }
    });
    $.ajax({
        url: "http://localhost:8080/onlineEnglishLearning/exitLogin/api",
        type: "post",
        headers: {'token': $.cookie('token')},
        data: {
            token:$.cookie('token')
        },
        success: function (data) {
            swal("退出登陆成功");
            setInterval(function () {
                window.location.href = "http://localhost:8080/onlineEnglishLearning/index";
            },1000);

        }
    });
}