<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>활동 계획 계산기</title>
    <link rel="stylesheet" href="resources/css/plan/calculatorNewEnrollForm.css">
</head>
<body>
<!--2023.7.23(일) 0h50 파일 생성 + 작업 시작-->

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<script src="resources/js/plan/calculatorNewEnrollForm.js"></script>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>활동 계획 계산기</h2>
    <br>

    <%--    <p>* 표시는 필수 입력 사항입니다.</p>--%>

<%--    <c:url var="new_plan_insert_url" value="newPlanInsert.pl" />--%>
    <form id="calculator-new-enroll-form" action="newPlanInsert.pl" method="post" modelAttribute="plan">

        <p class="question">무엇을 준비하고 계신가요?</p>
        <div id="is-measurable">
            <input type="radio" name="isMeasurableNum" value="1" onclick="selectIsMeasurable()"> 측정 가능한 일
            <input type="radio" name="isMeasurableNum" value="0" onclick="selectIsMeasurable()"> 측정이 어려운 일
            <input type="radio" name="isMeasurableNum" value="2" onclick="selectIsMeasurable()"> 잘 모르겠어요
        </div>
        <br>

        <div id="result-measurable"></div> <!--측정 가능 여부에 따른 결과 내용 보여주는 div 영역 끝-->

        <%-- 측정 어려운/모르는 일인 경우, '서비스 준비 중' 문구 띄우거나, ChatGpt 검색 결과로 안내 --%>
        <div id="not-measurable-result" style="display: none">
            서비스 준비 중입니다.<br>
            혹시 대신, ChatGPT 검색 결과가 궁금하신가요?<br>
            <br>
            '네, 보고 싶어요' vs '아니오, 괜찮아요' 버튼
        </div>

        <%-- 측정 가능한 일인 경우, 추가 질문들을 담은 div를 보이게 함 --%>
        <div id="measurable-questions" style="display: none">
            <span class="question">수행 목표 대상은 무엇인가요?</span>
            <span class="smallerLetters">(예시: 책/교과서/수험서/악보/원고 등 pages, 과목/단어/문제 등 개수, 자격증, 체중 등)</span>
            <br>
            <input type="text" name="object" required>
            <br>

            <br>
            <div id="search-book-on-aladin" style="font-style: italic">
                * 목표 대상이 도서인 경우, 검색해서 목표 분량을 기재할 수 있어요!
                <br>
                <input type="text" name="bookSearchKeyword" placeholder="도서명(전체 또는 일부)을 입력해 주세요">
                <div id="auto-complete"></div>
            </div>
            <br>

            <span class="question">목표 수량/분량을 기재해 주세요</span>
            <input type="number" name="totalQuantity" min="1" required>
            <br>
            <br>

            <span class="question">측정 단위를 입력해 주세요</span>
            <input type="text" name="unit" required>
            <br>
            <br>

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
            <br>
            <br>

            <div id="frequency-detail-question" style="display: none">
                <div class="question">구체적인 활동/수행 빈도를 입력해 주세요</div>
                <input id="frequency-detail" type="text" name="frequencyDetail">
                <br>
                <br>
            </div>

            <div class="question">목표 달성 (희망)기한이 정해져 있나요?</div>
            <div id="has-deadline">
                <input type="radio" name="hasDeadline" value="1" onclick="selectHasDeadline()"> 네 <span class="smallerLetters">(시험, 면접, 보고서/과제 제출 등
                날짜가 주어진 경우)</span>
                <input type="radio" name="hasDeadline" value="0" onclick="selectHasDeadline()"> 아니오, 아직 계획/생각 중이에요
            </div>
            <br>
            <br>

            <div id="yes-deadline" style="display: none">
                <div class="question">목표 기한의 종류를 알려주세요</div>
                <div id="deadline-type">
                    <input type="radio" name="deadlineTypeNum" value="1" onclick="selectDeadlineType()"> 특정 날짜
                    <input type="radio" name="deadlineTypeNum" value="2" onclick="selectDeadlineType()"> 기간 (x일/주/개월 등)
                </div>
                <br>
                <br>
            </div>

            <div id="deadline-type-date" style="display: none">
                <span class="question">달성 목표 날짜는 언제인가요?</span>
                <input type="date" name="deadlineDate">
                <br>
                <br>
            </div>

            <div id="deadline-type-period" style="display: none">
                <div class="question">달성 목표 기간이 얼마나 되나요?</div>
                시작일로부터 <input type="text" name="deadlinePeriod" placeholder="x일/주/개월">
                <br>
                <br>
            </div>

            <div id="no-deadline" style="display: none">
                <span class="question">1회당/하루에 얼마나 수행할 수 있을 것 같나요?</span>
                <input type="number" name="quantityPerDayPredicted" min="1">
                <br>
                <br>
            </div>
        </div> <!--측정 가능한 일인 경우, 추가 질문들을 담은 div 영역 끝-->

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
            <button type="button" onclick="location.href='/'">취소</button> <!--2023.7.24(월) 1810 나의 발견 = 계산 결과 화면으로부터 '새로 계산' 버튼 클릭해서 이 화면으로 넘어왔을 때, 이 버튼으로 '이전 화면'으로 가면(goBack() js 함수 호출) 이전 계산 결과 페이지로 감-->
            <%--            <button type="reset" class="grayBtn">초기화</button>--%>
            <button type="button" onclick="window.location.reload()" class="grayBtn">초기화</button>
            <button type="submit" class="greenBtn">계산하기</button> <!--onclick="location.href='newPlanInsert.pl'"-->
        </div>
    </form>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>