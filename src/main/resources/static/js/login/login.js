var base_url = '/projectone';
$("#form-btn").click(function(){
    var username = $("#username").val();
    var password = $("#password").val();
    var data = {
        username : username,
        password : password
    }
    $.ajax({
        type:"post",
        dataType: 'json',
        url: base_url + "/login/loginIn",
        data: data,
        headers:{
          'Content-Type':'application/x-www-form-urlencoded'
        },
        success:function(res){
            if(res.code != 200){
                console.log(res.msg);
            }else{
                location.href = "loginWelcome";
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            if(XMLHttpRequest.readyState == 0) {
                //here request not initialization, do nothing
            } else if(XMLHttpRequest.readyState == 4 && XMLHttpRequest.status == 0){
                console.log("服务器忙，请重试!");
            } else {
                console.log("系统异常，请联系系统管理员!");
            }
        }
    })

})