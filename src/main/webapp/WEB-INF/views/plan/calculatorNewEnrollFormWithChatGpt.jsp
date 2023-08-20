<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ChatGPT 계산기</title>
    <link rel="stylesheet" href="resources/css/plan/calculatorNewEnrollFormWithChatGpt.css">
</head>
<body>
<!--2023.8.2(화) 0h5 파일 생성 + 작업 시작-->

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<script src="resources/js/plan/calculatorNewEnrollFormWithChatGpt.js"></script>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>ChatGPT 계산기</h2>
    <br>

    <form id="calculator-new-enroll-form" action="newPlanByChatGptInsert.pl" method="post" modelAttribute="plan">
        <input type="hidden" name="isMeasurableNum" value="2">

        <div class="prompt">
            <h4>"[x기간] 동안 [수행 목표 대상] 계획을 세워줘!"</h4>
        </div>
        <br>

        <span class="question">수행 목표 대상은 무엇인가요?</span>
        <span class="smallerLetters">(예시: 곰인형 만들기, 풍경화 완성하기, 바디프로필 찍기 등)</span>
        <br>
        <input type="text" name="object" required>
        <br>
        <br>

        <div id="deadline-type-period">
            <div class="question">달성 목표 기간이 얼마나 되나요?</div>
            시작일로부터 <input type="number" name="deadlinePeriodNum" min="1" required>
            <select name="deadlinePeriodUnit" required>
                <option selected disabled>선택해 주세요</option>
                <option>일</option>
                <option>주</option>
                <option>개월</option>
            </select>

<%--            <input type="text" name="deadlinePeriod" placeholder="x일/주/개월" required>--%>
            <input type="hidden" name="hasDeadline" value="1">
            <input type="hidden" name="deadlineTypeNum" value="2">
            <br>
            <br>
        </div>

<%--        <button type="button" onclick="askQuestionToChatGPt();">검색</button>--%>

        <div class="question">시작일을 알고 있나요?</div>
        <div id="has-start-date">
            <input type="radio" name="hasStartDate" value="1" onclick="selectHasStartDate()"> 네, 알고 있어요
            <input type="radio" name="hasStartDate" value="0" onclick="selectHasStartDate()"> 아니오, 아직 몰라요
        </div>
        <br>
        <br>

        <div id="select-start-date" style="display: none">
            <span class="question">언제부터 시작할 예정인가요?</span>
            <input type="date" name="startDate">
            <br>
            <br>
        </div>

        <div class="question">어떤 빈도로 수행할 예정인가요?</div>
        <div id="frequency-type">
            <input type="radio" name="frequencyTypeNum" value="1" onclick="selectFrequencyType()"> 특정 요일 선택
            <input type="radio" name="frequencyTypeNum" value="2" onclick="selectFrequencyType()"> x일마다 x회
            <input type="radio" name="frequencyTypeNum" value="3" onclick="selectFrequencyType()"> 주/월 x회
        </div>

<%--        <div id="frequency-detail-question" style="display: none">--%>
<%--            <div class="question">구체적인 활동/수행 빈도를 입력해 주세요</div>--%>
<%--            <div id="frequency-detail-input-area"><input id="frequency-detail" type="text" name="frequencyDetail" required></div>--%>
<%--            <br>--%>
<%--            <br>--%>
<%--        </div>--%>

        <div id="frequency-detail-date" style="display: none">
            <div class="smallerLetters">수행 예정 요일(들)을 선택해 주세요</div>
            <input type="checkbox" name="frequencyDetailDate" value="월">월
            <input type="checkbox" name="frequencyDetailDate" value="화">화
            <input type="checkbox" name="frequencyDetailDate" value="수">수
            <input type="checkbox" name="frequencyDetailDate" value="목">목
            <input type="checkbox" name="frequencyDetailDate" value="금">금
            <input type="checkbox" name="frequencyDetailDate" value="토">토
            <input type="checkbox" name="frequencyDetailDate" value="일">일
        </div>

        <div id="frequency-detail-every" style="display: none">
            <input type="number" name="frequencyDetailEveryInterval" min="2">일마다 <input type="number" name="frequencyDetailEveryTimes" min="1">회
        </div>

        <div id="frequency-detail-times" style="display: none">
            <select name="frequencyDetailTimesInterval">
                <option selected disabled>선택해 주세요</option>
                <option>주</option>
                <option>월</option>
<%--                <option>연</option>--%>
            </select>
             <input type="number" name="frequencyDetailTimesTimes" min="1">회
        </div>
        <br>

        <%-- loginUser 속성 값 있으면 아래와 같이 작성자 정보 입력 --%>
        <c:choose>
            <c:when test="${ not empty loginUser }">
                <input type="hidden" name="memberId" value="${ loginUser.memberId }">
            </c:when>
            <c:otherwise>
                <input type="hidden" name="memberId" value="">
            </c:otherwise>
        </c:choose>
        <%-- 없으면 '저장하려면 로그인이 필요합니다' 모달 창 띄움 + 회원 가입이 필요한 경우 회원 가입 링크도 넣기(이건 계산 결과 페이지에서) --%>

        <div align="center">
            <button type="button" onclick="location.href='/key-project'">취소</button>
            <button type="button" onclick="window.location.reload()" class="grayBtn">초기화</button>
            <button type="submit" class="greenBtn">ChatGPT에게 물어보고, 계획 보기</button> <!--onclick="location.href='newPlanByChatGptInsert.pl'"-->
        </div>
    </form>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>