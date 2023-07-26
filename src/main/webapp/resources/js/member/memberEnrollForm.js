$(function () { // 문서가 로드되면
    // 2023.7.26(수) 21h30
    // 비밀번호 유효성 검사 = 영문자, 숫자, 특수문자(!@#$%^) 모두 포함하여 총 8~15자로 입력
    $("#password1").keyup(function () {
        let $password1 = $("#password1").val();
        let regExpPw = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^])[A-Za-z\d!@#$%^]{8,15}$/;

        if (regExpPw.test($password1)) {
            $("#error_password1").text("사용 가능합니다").css("color", "green");
        } else {
            $("#error_password1").text("영문자, 숫자, 특수문자(!@#$%^) 모두 포함하여 총 8~15자로 입력하세요").css("color", "purple");
        }
    })

    // 비밀번호 확인
    $("#password2").keyup(function () {
        let $password1 = $("#password1").val();
        let $password2 = $("#password2").val();

        if ($password1 == $password2) {
            $("#error_password2").text("일치합니다").css("color", "green");
        } else {
            $("#error_password2").text("일치하지 않습니다").css("color", "purple");
        }
    })
})


// 아이디(이메일) 중복 체크
function idCheck() {
    let $memberEmail = $("input[name=email]");

    $.ajax({
        url: "idCheck.me",
        data: {checkEmail: $memberEmail.val()},
        success: function (result) {
            // result 경우의 수 = "N", "Y"
            if (result == "N") { // 중복된 아이디 = 사용 불가
                alert("이미 존재하거나 탈퇴한 회원의 이메일입니다");
                //재입력 유도
                $memberEmail.focus();
            } else { // 중복되지 않은 아이디 = 사용 가능
                // alert("사용 가능한 이메일입니다");
                $("#email-check-msg").text("사용 가능합니다").css("color", "green");
            }
        },
        error: function () {
            console.log("AJAX 통신 아이디 중복 체크 실패");
        }
    })
}

// 닉네임 중복 체크
function nicknameCheck() {
    let $memberNickname = $("input[name=nickname]");

    $.ajax({
        url: "nicknameCheck.me",
        data: {checkNickname: $memberNickname.val()},
        success: function (result) {
            if (result == "N") { // 중복되어 사용 불가한 닉네임
                alert("이미 존재하는 닉네임입니다");
                $memberNickname.focus();
            } else {
                $("#nickname-check-msg").text("사용 가능합니다").css("color", "green");
            }
        },
        error: function () {
            console.log("AJAX 통신 닉네임 중복 체크 실패")
        }
    })
}

