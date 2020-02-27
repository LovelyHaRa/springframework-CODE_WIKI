$(function () {
    const inputEmail = $("#email");
    const ckeckboxRememberEmail = $("#rememberEmail");
    inputEmail.val(Cookies.get('key'));
        if(inputEmail.val() !== ""){
            ckeckboxRememberEmail.attr("checked", true);
    }

    ckeckboxRememberEmail.change(function(){
        if(ckeckboxRememberEmail.is(":checked")){
            Cookies.set('key', inputEmail.val(), { expires: 30 });
        }else{
            Cookies.remove('key');
        }
    });

    inputEmail.keyup(function(){
        if(ckeckboxRememberEmail.is(":checked")){
            Cookies.set('key', $("#email").val(), { expires: 30 });
        }
    });
})