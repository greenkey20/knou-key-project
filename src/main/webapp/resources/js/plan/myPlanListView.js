$(function () {
    $(".detailRequest").click(function () {
        var plannerId = $(this).siblings(".plannerId").text();
        console.log(plannerId);
        location.href = 'myPlanDetail.pl?planId=' + plannerId;
    })
})