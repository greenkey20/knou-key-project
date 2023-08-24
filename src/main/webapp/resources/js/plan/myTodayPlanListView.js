// // 2023.8.24(목) 18h45
// function searchThisDayPlanList(numOfDate) {
//     console.log("searchThisDayPlanList() 함수 안에 들어왔어요~");
//
//     let $numOfYear = $("#this-year").text();
//     let $numOfMonth = $("this-month").text();
//     let $numOfDate = numOfDate;
//     console.log($numOfYear + "년 " + $numOfMonth + "월 " + $numOfDate + "일 actionDatesList를 구하고자 함");
//
//     $.ajax({
//         url: "thisDayPlanList.pl",
//         dataType: 'json',
//         data: {
//             year: $numOfYear,
//             month: $numOfMonth,
//             date: $numOfDate
//         },
//         success: function (result) {
//             console.log(result);
//
//             if (!result.length) { // 검색 결과가 없는 경우
//                 console.log("해당 날짜에 일정 없네요");
//             } else {
//                 console.log("해당 날짜에 일정 있습니다");
//             }
//         },
//         error: function () {
//             console.log("특정 날짜 일정 가져오는 AJAX 실패");
//         },
//     });
// }