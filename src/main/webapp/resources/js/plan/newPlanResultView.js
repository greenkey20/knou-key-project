// 2023.7.24(월) 14h55
function checkLogin() {
    var loginUser = '${ loginUser }';

    if (loginUser == "") {
        alert('로그인 후 이용하실 수 있습니다');
        location.href = 'loginPage.me';
        return false;
    }

    return true;
}