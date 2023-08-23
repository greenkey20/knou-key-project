function openCloseToc() {
    if ($("#toc-content").css('display') == 'block') {
        $("#toc-content").css('display', 'none');
        $("#toc-toggle").html('보기');
    } else {
        $("#toc-content").css('display', 'block');
        $("#toc-toggle").html('숨기기');
    }
}

function openCloseChatGptLines() {
    if ($("#chatgpt-content").css('display') == 'block') {
        $("#chatgpt-content").css('display', 'none');
        $("#chatgpt-toggle").html('보기');
    } else {
        $("#chatgpt-content").css('display', 'block');
        $("#chatgpt-toggle").html('숨기기');
    }
}

// $(function () {
//     console.log("---- start ----");
//
//     $(".checkInput").change(function () {
//         if ($(".check-input").is(":checked")) {
//             console.log("체크박스 체크했음");
//             alert("체크박스 체크했음");
//             // $(".realQuantity").val($("#plan-quantity").val());
//         } else {
//             console.log("체크박스 체크 해제했음");
//             alert("체크박스 체크 해제했음");
//             // $(".realQuantity").val(1);
//         }
//     });
//
//     // $("input[name='checkInput']:checked").
// });