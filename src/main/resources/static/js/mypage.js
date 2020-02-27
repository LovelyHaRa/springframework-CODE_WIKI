$(function () {
    $.validator.addMethod("regex", function(value, element, regexp) {
        var re = new RegExp(regexp);

        return this.optional(element) || re.test(value);
    },  "Please check your input.");

    $(".form-mypage").validate({
        rules: {
            password: {
                required: true
            },
            newPassword: {
                required: true,
                minlength: 8,
                maxlength: 20,
                regex: "(?=.*[0-9])(?=.*[a-zA-z])(?=.*\\W)(?=\\S+$).{8,20}"
            },
            confirmPassword: {
                required: true,
                equalTo: "#newPassword"
            },
            name: {
                required: true,
                minlength: 2
            }
        },
        messages: {
            password: {
                required: "필수 입력 사항입니다."
            },
            newPassword: {
                required: "필수 입력 사항입니다.",
                minlength: "최소 {0}자 이상이어야 합니다.",
                maxlength: "{0}자를 초과할 수 없습니다.",
                regex: "영문 대/소문자, 특수문자가 각각 1개이상 포함되어야합니다."
            },
            confirmPassword: {
                required: "필수 입력 사항입니다.",
                equalTo: "비밀번호가 일치하지 않습니다."
            },
            name: {
                required: "필수 입력 사항입니다.",
                minlength: "최소 {0}자 이상이어야 합니다."
            }
        }
    });

    $('.btn-leave').click(function (e) {
        e.preventDefault();
    });
});