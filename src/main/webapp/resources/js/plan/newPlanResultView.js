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

// 2023.7.25(화) 12h50
$(function () { // 문서가 로드되면
    // getCalendar();
});

function getCalendar() {
    let $now = new Date(); // 현재 날짜 및 시간
    console.log("현재 = " + $now); // todo 현재 = Tue Jul 25 2023 18:33:09 GMT+0900 (Korean Standard Time)

    $.ajax({
        url: "calendar.pl", // ?year=" + $now.getFullYear() + "&month=" + $now.getMonth(),
        // type: "get",
        data: {
            year: $now.getFullYear(),
            month: $now.getMonth() + 1
        },
        // dataType: "JSON",
        success: function (result) {
            console.log("ajax result = " + result); // todo

            let dateDataList = result.dateDataList;
            console.log("dateDataList = " + dateDataList); // todo

            let todayInfo = result.todayInfo;
            console.log("todayInfo = " + todayInfo); // todo

            let tbody = "";
            let i = 0;

            $.each(dateDataList, function (index, individualDay) {
                if (individualDay.day % 7 == 0) {
                    tbody += "<tr>"
                        + "<td class='day holiday' align='left'>" + individualDay.date + "</td>";
                } else if (individualDay.day % 7 == 6) {
                    tbody += "</tr>" + "<td class='day' align='left'>" + individualDay.date + "</td>";
                } else {
                    tbody += "<td class='day' align='left'>" + individualDay.date + "</td>";
                }
            })

            $(".calendarBody tbody").append(tbody);
        },
        error: function () {
            console.log("getCalendar AJAX 실패");
        }
    })
}