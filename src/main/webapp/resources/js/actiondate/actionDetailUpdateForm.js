// 2023.8.21(월) 11h50
function getRealActionQuantity() {
    let $realStartUnit = $("input[name=realStartUnit]").val();
    let $realEndUnit = $("input[name=realEndUnit]").val();
    console.log("realStartUnit = " + $realStartUnit + ", realEndUnit = " + $realEndUnit);
    let $realActionQuantity = $realEndUnit - $realStartUnit + 1;

    if ($realStartUnit > $realEndUnit) {
        $('#valid-end-unit').text("종료 단위는 시작 단위보다 크거나 같은 수여야 합니다").css("color", "purple");
    } else {
        $('#valid-end-unit').text("");
    }

    $("#real-action-quantity-area").html($realActionQuantity);
    $("input[name=realActionQuantity]").val($realActionQuantity);
    console.log("realActionQuantity = " + $realActionQuantity);
}