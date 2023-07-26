$(function () { // 문서가 로드되면
    $(".detailRequest").click(function () {
        let $planId = $(this).siblings(".planId").value();
        console.log($planId); // todo
        location.href = 'myPlanDetail.pl?planId=' + $planId;
    })
})