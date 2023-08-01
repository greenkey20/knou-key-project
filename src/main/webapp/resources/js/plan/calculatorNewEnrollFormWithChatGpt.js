$(function () { // 문서가 로드되면

})

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
        $("#frequency-detail-date").show();
        $("#frequency-detail-every").hide();
        $("#frequency-detail-times").hide();
    } else if ($valueFrequencyType == 2) {
        $("#frequency-detail-date").hide();
        $("#frequency-detail-every").show();
        $("#frequency-detail-times").hide();
    } else {
        $("#frequency-detail-date").hide();
        $("#frequency-detail-every").hide();
        $("#frequency-detail-times").show();
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