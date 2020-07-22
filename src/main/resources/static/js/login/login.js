function login() {
    var username = $("#username").val();
    var password = $("#password").val();
    var param = {"username":username,"password":password};
    //var vcode = $("#vcode").val();
    var token = "";
    $.ajax({
        type:"POST",
        url:"/projectone/login/loginIn",
        data:param,
        dataType:"json",
        async:false,
        success:function (data) {
            token = data.data;
        },error:function () {
            console.log("系统异常")
        }
    })
}