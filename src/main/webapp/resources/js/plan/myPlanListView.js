$(function () { // 문서가 로드되면
    $(".detailRequest").click(function () {
        var plannerId = $(this).siblings(".plannerId").text();
        console.log(plannerId); // todo
        location.href = 'myPlanDetail.pl?planId=' + plannerId;
    })
})