// 2023.8.1(화) 0H30
function enterKey() {
    if (window.event.keyCode == 13) {
        $("#form").submit();
    }
}

// 2023.8.1(화) 0h40
function searchBookTitle() {
    let $keyword = $("#book-search-input").val();
    // $("#books-table").show();
    console.log("keyword 변수의 값 = " + $keyword);

    $.ajax({
        url: "bookTitleSearch.pl",
        dataType: 'json',
        data: {
            bookSearchKeyword: $keyword
        },
        success: function (result) {
            console.log(result);

            // 2023.7.31(월) 22h5 도전!
            let tbody = "";

            if (!result.length) { // 검색 결과가 없는 경우
                tbody = "<tr><td colspan='2'>검색 결과가 없습니다</td></tr>";
            } else {
                $.each(result, function (index, item) {
                    console.log("책 목록 표 만들 때 index = " + index)

                    tbody += "<tr>"
                        + "<td rowspan='3'><img src='" + item.cover + "'></td>"
                        + "<td><span id='book-title' class='question'>" + item.title + " </span><span class='smallerLetters'>(ISBN " + item.isbn13 + ")</span></td>"
                        + "</tr>";
                    tbody += "<tr>"
                        + "<td>" + item.author + "</td>"
                        + "</tr>";
                    tbody += "<tr>"
                        + "<td>" + item.publisher + ", " + item.pubDate + "</td>"
                        + "</tr>";
                    tbody += "<tr id='num-of-pages-row'>"
                        + "<td><div align='center'><input type='radio' id='selectBook' name='selectBook' value='hello" + index + "'></div></td>"
                        + "<td><span id='num-of-pages-digits'>" + item.numOfPages + "</span><span>페이지</span></td>"
                        + "</tr>";
                });
            }

            $("#book-list").html(tbody);

            let divbody = "<button type='button' id='select-book-btn' onclick='confirmBook();'>선택</button>";
            $("#select-book-btn-area").html(divbody);
        },
        error: function () {
            console.log("키워드로 책 검색 AJAX 실패");
        },
    });
}

// $(function () {
//     $("#select-book").change(function () {
//         console.log("책 선택 radio 버튼 이벤트 발생해요~")
//         let $title = $(this).siblings("#book-title").text();
//         let $numOfPages = $(this).siblings("#num-of-pages-digits").text();
//         console.log("title = " + $title + ", numOfPages = " + $numOfPages)
//
//         $("#object-div").text($title);
//         $("#num-of-page-span").text($numOfPages);
//
//         $("input[name=object]").val($title);
//         $("input[name=totalQuantity]").val($numOfPages);
//
//         $("#books-table").hide();
//     });
// });

function confirmBook() {
    console.log("confirmBook() 함수 호출되었어요")
    let $title = $("input[name='selectBook']:checked").val(); // .parent().children().eq(0).text()
    let $numOfPages = $("#num-of-pages-digits").text();
    console.log()
    console.log("title = " + $title + ", numOfPages = " + $numOfPages)

    $("#object-div").text($title);
    $("#num-of-page-span").text($numOfPages);

    $("input[name=object]").val($title);
    $("input[name=totalQuantity]").val($numOfPages);

    // $("#books-table").hide();
}


// <script>
// $(function() {
// 	$("input[type=radio]").click(function() {
// 		console.log("라디오 버튼이 클릭됨");

$(function () { // 문서가 로드되면

})

// 2023.7.23(일) 10h20
function selectIsMeasurable() {
    let $valueIsMeasurableNum = $("#is-measurable input[type=radio]:checked").val();

    if ($valueIsMeasurableNum == 1) {
        // 측정 가능한 일 관련 질문들을 보여주는 div hidden -> 보이게
        $("#not-measurable-result").hide();
        $("#measurable-questions").show();
        // $("#measurable-questions").css("display", "block");
    } else if ($valueIsMeasurableNum == 0 || $valueIsMeasurableNum == 2) {
        $("#measurable-questions").hide();
        // let text = "서비스 준비 중입니다";
        // $("#result-measurable").html(text);
        $("#not-measurable-result").show();
    }
}

// 2023.7.23(일) 15h55
function selectHasStartDate() {
    let $valueHasStartDate = $("#has-start-date input[type=radio]:checked").val();

    if ($valueHasStartDate == 1) {
        $("#select-start-date").show();
    } else {
        $("#select-start-date").hide();
    }
}

// 2023.7.23(일) 16h5
function selectFrequencyType() {
    $("#frequency-detail-question").show();

    let $valueFrequencyType = $("#frequency-type input[type=radio]:checked").val();

    if ($valueFrequencyType == 1) {
        $("#frequency-detail").attr("placeholder", "예시) 월화수목금토일, 월수금, 월화수목금 등");
    } else if ($valueFrequencyType == 2) {
        $("#frequency-detail").attr("placeholder", "예시) 2일마다 1회, 5일마다 2회 등");
    } else {
        $("#frequency-detail").attr("placeholder", "예시) 주 2회, 월 10회 등");
    }
}

// 2023.7.23(일) 16h20
function selectHasDeadline() {
    let $valueHasDeadline = $("#has-deadline input[type=radio]:checked").val();

    if ($valueHasDeadline == 1) {
        $("#no-deadline").hide();
        $("#yes-deadline").show();
    } else {
        $("#yes-deadline").hide();
        $("#deadline-type-date").hide();
        $("#deadline-type-period").hide();
        $("#no-deadline").show();

    }
}

// 2023.7.23(일) 16h25
function selectDeadlineType() {
    let $valueDeadlineTypeNum = $("#deadline-type input[type=radio]:checked").val();
    if ($valueDeadlineTypeNum == 1) {
        $("#deadline-type-period").hide();
        $("#deadline-type-date").show();
    } else {
        $("#deadline-type-date").hide();
        $("#deadline-type-period").show();
    }
}

// 2023.7.23(일) 16h40
function goBack() {
    history.back();
    // history.go(-1);
}

// </script>