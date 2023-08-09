<img src="https://github.com/greenkey20/knou-key-project/assets/87472526/5d81a34d-8c07-40af-b531-45e607a08873">

# Introduction

**Alloc(알록)** 은 활동 계획 계산기입니다.
특정 과목/자격증을 위한 공부, 독서, 면접 대비 예상 질문 등 측정 가능한(페이지 수, 개수 등) 대상의 활동 뿐만 아니라, 측정이 비교적 어려운 활동도, 목표 달성을 위해서는 얼만큼의 기간동안 매 회 얼만큼의 분량을 수행해야 하는지 계획하는 데 가이드라인을 제공합니다.

회원 가입을 하시면 계산 결과를 '나의 일정'으로 저장하여 수행 내역을 트래킹하고 통계 내역을 조회할 수 있습니다.
또한 나의 활동 내역 및 성공적인 수행 내역에 대해 게시글로 경험 이야기를 나눠주실 수 있습니다.

- 웹사이트: [Alloc](http://ec2-13-209-255-107.ap-northeast-2.compute.amazonaws.com:8080/key-project/)
- 작업 일지: [Notion](https://greenkey20.notion.site/knou-SW-_-d45c4af16b474e7bab7b867205b4436e)

<img width="788" alt="Screenshot 2023-08-07 at 15 25 52" src="https://github.com/greenkey20/knou-key-project/assets/87472526/660ab171-6792-49ec-a9ed-1692e13c0b5f">


# Technologies

- 소프트웨어 종류: 웹 애플리케이션 프로그램
- 동작 플랫폼: 웹 브라우저
- 개발 환경
  - macOS 13.4.1
  - IntelliJ IDEA 2022.2 (Ultimate Edition)
  - Apache Tomcat 10.1.11
  - ubuntu-jammy-22.04 (AWS EC2)
- 개발 기간: 2023.6.28(수) ~ 2023.8.6(일)
- 개발 인원: 1명(백엔드, 프론트엔드, 기획, 인프라)
- 기술 스택
  - 백엔드
    - java-17-openjdk-17.0.7
    - H2 Database
    - Spring Boot 3.1.1, Spring Data JPA 3.1.1, Gradle
  - 프론트엔드
    - HTML/CSS, JavaScript, jQuery 3.4.1
    - JSP, JSTL
  - 기타 도구: GitHub(버전 관리), Notion(작업 일지)


# Main Features

**1. 측정 가능한 일 계획 계산기**

<img src="https://github.com/greenkey20/knou-key-project/assets/87472526/a19cb64e-fb3d-4e59-8e48-df8a2a65c7ba">
<img src="https://github.com/greenkey20/knou-key-project/assets/87472526/37d50155-99f1-45db-8001-9909cad1e6af">


**2. 독서 계획 계산기**

<img src="https://github.com/greenkey20/knou-key-project/assets/87472526/bf0c28ca-75ca-4059-880c-c95ff9e8a6af">
<img src="https://github.com/greenkey20/knou-key-project/assets/87472526/5e67b49f-019e-421a-9b9d-ba0c36015ebd">


**3. 측정이 어려운 일 계획 계산기**

<img src="https://github.com/greenkey20/knou-key-project/assets/87472526/52a6dff2-6fcc-4675-a6f3-4f7809a766c2">
<img src="https://github.com/greenkey20/knou-key-project/assets/87472526/0a9b4a6b-8dd4-48e0-a68e-6f81de70d404">


# Example Of Use

> YYYY-MM-DD부터 YYYY-MM-DD까지의 기간 중 매일/x요일마다 __페이지의 책을 읽어 xx 책을 완독할 거에요
> 
> _페이지의 책을 __일만에 읽기 위해 월~금요일마다 __페이지씩 책을 읽어요
> 
> YYYY-MM-DD xx 시험을 대비하여 오늘부터 매일 수험서 __페이지를 공부해요! 시험 전 __일은 복습 기간으로 남겨두었어요
> 
> YYYY-MM-DD 중간고사를  대비하여 오늘부터 월~금요일마다 _._챕터씩 공부해요! 시험 전 _일은 복습 기간으로 남겨두었어요
> 
> YYYY-MM-DD 기말고사를 대비하여 오늘부터 매일 _._과목씩 공부해요!
> 
> YYYY-MM-DD 출석수업 과제물 제출을 위해 이번 주말부터 매 토/일요일마다 _%씩 진행해요
> 
> YYYY-MM-DD xx 회사 면접을 대비하여 내일부터 매일 _개의 면접 대비 질문에 대해 준비해요
> 
> YYYY-MM-DD xx 시험을 위해 3일마다 오답노트 __개 문제를 복습해요
> 
> YYYY-MM-DD 까지 _페이지의 원고 작성을 위해 매일 _페이지씩 집필해요. 마지막 _일은 최종 검토 기간으로 남겨두었어요
> 
> _개 원고 검토를 위해 매일 _개의 원고를 YYYY-MM-DD 까지 __일 동안 검토해요
> 
> _개 문제 풀이를 위해 이틀에 한 번 _개의 문제를 YYYY-MM-DD 까지 __일 동안 풀어요
> 
> _개 단어 암기를 위해 매일 _개의 단어를 YYYY-MM-DD 까지 __일 동안 암기해요
> 
> knou 기말시험을 대비하여 오늘부터 _과목의 _챕터를 매 월요일마다 공부해요 등


# Project Structure

## File
<img width="350" src="https://github.com/greenkey20/knou-key-project/assets/87472526/1e8785fb-2cd8-4e20-bce9-99eebbe99edc">
<br>
<img width="400" src="https://github.com/greenkey20/knou-key-project/assets/87472526/b2114473-3e7d-47c4-8fdc-3a714d80bff2">


## Database

<img src="https://github.com/greenkey20/knou-key-project/assets/87472526/d8c0e8dc-1d08-465e-a015-969bed5b6f2b">


## Use Case Diagram

<img src="https://github.com/greenkey20/knou-key-project/assets/87472526/39cdc1c2-d855-43fa-b0d3-e74ae477b251">


# Release History

- 2023.8.7(월) : 1.0.0 1차 배포
