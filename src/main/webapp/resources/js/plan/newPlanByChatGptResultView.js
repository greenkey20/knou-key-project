// 2023.7.24(월) 14h55 -> 2023.7.27(목) 오후 이 메서드가 잘 실행 안 되어서, PlanController에서 처리

// 2023.7.25(화) 12h50
$(function () { // 문서가 로드되면
    function checkLogin() {
        // let $loginUser = '${ loginUser }';
        let $memberId = $("#hidden-member-id").val();

        if ($memberId == null) {
            console.log("로그인 후 이용하실 수 있습니다")
            alert('로그인 후 이용하실 수 있습니다');
            location.href = 'loginPage.me';
            return false;
        }

        return true;
    }
    // getCalendar();
});