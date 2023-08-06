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