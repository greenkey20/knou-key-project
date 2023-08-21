// 2023.8.1(화) 0H30
function enterKey() {
    if (window.event.keyCode == 13) {
        $("#form").submit();
    }
}

// 2023.8.1(화) 0h40
function searchBookTitle(currentPage) {
    let $keyword = $("#book-search-input").val();
    // $("#books-table").show();
    console.log("keyword 변수의 값 = " + $keyword);

    $.ajax({
        url: "bookTitleSearch.pl",
        dataType: 'json',
        data: {
            bookSearchKeyword: $keyword,
            cpage: currentPage
        },
        success: function (resultMap) {
            console.log(resultMap);

            let $bookInfoDtos = resultMap.bookInfoDtos;
            console.log($bookInfoDtos)

            let $pageInfo = resultMap.pageInfo;
            console.log($pageInfo);

            // 2023.7.31(월) 22h5 도전!
            let tbody = "";

            if (!$bookInfoDtos.length) { // 검색 결과가 없는 경우
                tbody = "<tr><td colspan='2'>검색 결과가 없습니다</td></tr>";
            } else {
                $.each($bookInfoDtos, function (index, item) {
                    console.log("책 목록 표 만들 때 index = " + index)

                    tbody += "<tr>"
                        + "<td rowspan='3'><img src='" + item.cover + "'></td>"
                        + "<td class='question'>"
                        + "<input type='radio' id='selectBook' name='selectBook' value='" + item.title + "'> " + item.title
                        + "<span class='smallerLetters'> | </span>"
                        + "<span class='smallerLetters'>" + item.numOfPages + "</span>"
                        + "<span class='smallerLetters'>페이지 (ISBN: </span>"
                        + "<span class='smallerLetters'>" + item.isbn13 + "</span>"
                        + "<span class='smallerLetters'>)</span>"
                        + "</td>"
                        + "</tr>";
                    tbody += "<tr>"
                        + "<td>" + item.author + "</td>"
                        + "</tr>";
                    tbody += "<tr>"
                        + "<td>" + item.publisher + ", " + item.pubDate + "<span class='smallerLetters bookSrc'><a href='" + item.link + "' target='_blank'> → 자세히 보기</a></span></td>"
                        + "</tr>";
                });
            }

            $("#book-list").html(tbody);

            // 페이징 바
            let pagingBar = "<ul class='pagination justify-content-center'>";

            if ($pageInfo.currentPage == 1) {
                pagingBar += "<li class='page-item disabled'><a class='page-link' href='#'>이전 페이지</a></li>";
            } else {
                pagingBar += "<li class='page-item'><a class='page-link' href='javascript:void(0)' onclick='searchBookTitle(" + (currentPage - 1) + ");'>이전 페이지</a></li>";
            }

            // console.log("왜 '다음 페이지' 버튼 안 생기지? bookInfoDtos 길이 = " + $bookInfoDtos.length + ", boardLimit = " + $pageInfo.boardLimit);
            if ($bookInfoDtos.length === $pageInfo.boardLimit) { // '현재 조회된 결과 갯수 == boardLimit'인 경우에도 마지막 페이지일 수는 있긴 함
                // console.log("if문 안에는 들어오나?"); // 2023.8.20(일) 7h 현재 들어옴
                pagingBar += "<li class='page-item'><a class='page-link' href='javascript:void(0);' onclick='searchBookTitle(" + (currentPage + 1) + ");'>다음 페이지</a></li></ul>";
            } else { // 현재 조회된 결과 갯수가 boardLimit보다 작다면, 이건 해당 키워드 검색 결과의 마지막 페이지임
                // console.log("또는 else문 안에는 들어오나?")
                pagingBar += "<li class='page-item disabled'><a class='page-link' href='#'>다음 페이지️</a></li></ul>";
            }
            // console.log("'다음 페이지' 버튼 만드는 코드를 그냥 넘어가나?")
            // 2023.8.20(일) 7h20 현재 '다음 페이지' 버튼 누르면 JSON 결과 데이터만 출력되는데.. 뭔가 순환/재귀 호출인 것 같은데.. 어떻게 이 success 처리 내용이 '다음 페이지'에서도 적용되게 할 수 있을까?

            /*
            for (let i = $pageInfo.startPage; i <= $pageInfo.endPage; i++) {
                if (i != $pageInfo.currentPage) {
                    pagingBar += "<li class='page-item'><a class='page-link' href='bookTitleSearch.pl?cpage='" + i + "'>" + i + "</a></li>";
                } else {
                    pagingBar += "<li class='page-item disabled'><a class='page-link' href='#'>" + i + "</a></li>";
                }
            }

            if ($pageInfo.currentPage == $pageInfo.maxPage) {
                pagingBar += "<li class='page-item disabled'><a class='page-link' href='#'>➡️</a></li>"
                    + "<li class='page-item disabled'><a class='page-link' href='#'>끝</a></li>";
            } else {
                pagingBar += "<li class='page-item'><a class='page-link' href='bookTitleSearch.pl?cpage='" + ($pageInfo.currentPage + 1) + "'>➡️️</a></li>"
                    + "<li class='page-item'><a class='page-link' href='bookTitleSearch.pl?cpage='" + $pageInfo.maxPage + "'>마지막</a></li>";
            }
             */

            $("#search-book-paging").html(pagingBar);


            let srcDivBody = "도서 DB 제공 : 알라딘 인터넷서점(www.aladin.co.kr)";
            $("#book-info-src-area").html(srcDivBody);

            let btnDivBody = "<button type='button' id='select-book-btn' onclick='confirmBook();'>선택</button>";
            $("#select-book-btn-area").html(btnDivBody);
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
    // let $numOfPages = $("#num-of-pages-digits").text();
    let $numOfPages = $("input[name='selectBook']:checked").siblings().eq(1).text();
    let $isbn13 = $("input[name='selectBook']:checked").siblings().eq(3).text();
    console.log("title = " + $title + ", numOfPages = " + $numOfPages + ", isbn13 = " + $isbn13);

    $("#object-div").text($title);
    $("#num-of-page-span").text($numOfPages);

    $("input[name=object]").val($title);
    $("input[name=totalQuantity]").val($numOfPages);
    $("input[name=isbn13]").val($isbn13);

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