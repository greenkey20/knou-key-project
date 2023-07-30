$(function () {
    console.log("---- start ----");

    $(".checkInput").change(function () {
        if ($(".check-input").is(":checked")) {
            console.log("체크박스 체크했음");
            alert("체크박스 체크했음");
            // $(".realQuantity").val($("#plan-quantity").val());
        } else {
            console.log("체크박스 체크 해제했음");
            alert("체크박스 체크 해제했음");
            // $(".realQuantity").val(1);
        }
    });

    // $("input[name='checkInput']:checked").
});