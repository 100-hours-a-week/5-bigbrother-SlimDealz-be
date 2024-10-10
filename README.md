# 카카오 부트 캠프 오프라인 2개월 과정

## SlimDealz

### 다이어트 식품 최저가 비교 서비스

### 프로젝트 기간: 2024.08.08 ~ 2024.10.11 (2개월간 진행)

### 기술 스택

- **FE**: React, Vie js, Babel js, Typescript
- **BE**: Spring Boot, JPA, Query DSL
- **DB**: AWS RDS MySQL
- **SCRAPPING&CROLLING:** Python, BeuatifulSoup4, Selenium ****
- 배포: Docker, AWS

### 팀 소개

- **팀명**: 빅브라더
- **팀장**: kevin.lee(이강윤)
- **팀원**: lily.jo(조의연), rhea.chu(추연수), derek.yoo(유문호), danny.cho(조용원)

<table>
<tbody>
<tr>
<td align="center"><a href="https://github.com/lky3004me"><br /> <img width="211" alt="스크린샷 2024-10-10 오전 12 57 49" src="https://github.com/user-attachments/assets/1c36d0a1-53f9-4bca-9594-769b214f6ed5">

 <sub><b>BE 팀장: kevin.lee(이강윤)</b></sub></a><br /></td>
<td align="center"><a href="https://github.com/lucy726j"><br /><img width="208" alt="스크린샷 2024-10-10 오전 12 58 01" src="https://github.com/user-attachments/assets/b0dea7b0-3de1-46a6-b207-35b12617c522">

  <sub><b>FE 팀원: lily.jo(조의연)</b></sub></a><br /></td>
</tr>
<tr>
<td align="center"><a href="https://github.com/bysoyeon"><br /> <img width="208" alt="스크린샷 2024-10-10 오전 12 58 07" src="https://github.com/user-attachments/assets/1d738aef-4efe-4f31-9648-cf32f1c5101d">

 <sub><b>DevOps 팀원: derek.yoo(유문호)</b></sub></a><br /></td>
<td align="center"><a href="https://github.com/lky3004me"><br /> <img width="210" alt="스크린샷 2024-10-10 오전 12 58 37" src="https://github.com/user-attachments/assets/12beee15-56d8-48b4-a717-a0bab6028e9e">

 <sub><b>BE 팀원: danny.cho(조용원)</b></sub></a><br /></td>
<td align="center"><a href="https://github.com/grulla79"><br /> <img width="206" alt="스크린샷 2024-10-10 오전 12 58 28" src="https://github.com/user-attachments/assets/58a823d2-06dc-492f-a1a2-79baf5ac2497">

 <sub><b>BE 팀원: rhea.chu(추연수)</b></sub></a><br /></td>
</tr>
</tbody>
</table>

### 역할 분담

- **kevin.lee(이강윤)**: 팀장, BE, Python 크롤링(다나와 상품 정보 조회), DB
- **Lily.jo(조의연)**: FE, CSS
- **Rhea.chu(추연수)**: BE, 검색기능
- **Derek.yoo(유문호)**: DEVOPS
- **Danny.cho(조용원)**: FE, BE, Kakao Oauth2

## 프로젝트 설명

### 프로젝트 개요

닭가슴살, 프로틴, 샐러드 등 다이어트 관련 식품의 최저가를 손쉽게 찾고, 저장할 수 있는 최저가 비교 플랫폼 입니다.

### 프로젝트 필요성

1. **누가 사용하면 좋을까?**
    - **헬스인**: 운동과 다이어트에 집중하는 사람들.
    - **건강을 중시하는 사람들**: 건강식품에 관심이 많고 최저가를 찾고자 하는 사람들.
    - **노년층**: 건강 관리를 위해 건강식품을 자주 구매하는 연령층.
2. **기존 서비스 분석**
    - 대형 온라인 마켓에서도 닭가슴살 가격 비교가 어렵고 일관되지 않음.
    - 가격 변동이 자주 일어나며 이를 자동으로 추적하기 어려움.
    - 카테고리별로 분류된 닭가슴살 제품의 최저가를 한눈에 확인하기 어려움.
    - 닭가슴살에 특화된 플랫폼이 드문 상황이며, 추후 다른 건강식품으로 확장 계획.
3. **서비스 목표**
    - **쉽고 빠른 닭가슴살 최저가 확인**: 한눈에 다양한 닭가슴살 제품의 최저가를 확인할 수 있는 플랫폼 제공.
    - **리뷰 및 추천 시스템**: 닭가슴살을 직접 구매한 사람들의 리뷰와 추천 시스템을 제공하고, 확장 시 건강식품 전반에 대한 리뷰 시스템 추가.
4. **서비스 기능**
    - **검색 기능**: 사용자가 원하는 제품의 최저가를 실시간으로 검색 및 비교 가능.
    - **인기 많은 제품/ 북마크 맞춤 추천 기능**: 향후 확장 시 사용자의 건강 목표나 취향에 따라 맞춤형 건강식품 추천.
    - **리뷰 및 평점 시스템**: 사용자가 구매한 닭가슴살에 대한 리뷰를 남기고 평가할 수 있는 기능, 추후 건강식품 카테고리 확장.
5. **서비스 시스템 아키텍쳐**<br/>

6. **기대 효과**
    - 사용자들이 닭가슴살을 최저가로 구매하여 비용을 절감할 수 있음.
    - 닭가슴살에 대한 투명한 리뷰와 정보를 통해 신뢰성 높은 제품을 구매 가능.
    - 향후 건강 목표(체중 감량, 근육 증진 등)에 맞는 맞춤형 건강식품 추천을 통해 만족도 향상.
7. **향후 계획**
- **제품 데이터 확장**: 다양한 온라인 쇼핑몰의 닭가슴살 데이터를 지속적으로 업데이트하며, 이후 다른 건강식품(프로틴, 샐러드 등)으로 확장.
- **가격 알림 기능 추가**: 사용자가 설정한 특정 닭가슴살 제품이나 건강식품의 가격 변동 시 알림을 받아볼 수 있는 기능을 추가하여 최적의 구매 타이밍을 제공.
- **검색 및 필터 기능 보완**: 더 세분화된 검색과 필터링 기능을 추가해 사용자가 원하는 제품을 쉽게 찾을 수 있도록 개선.

### 발표 자료 링크

[발표 자료 링크](https://www.canva.com/design/DAGNDa1-m9U/M8CdlGe54FRHH5cu7OJjLQ/view?utm_content=DAGNDa1-m9U&utm_campaign=designshare&utm_medium=link&utm_source=editor)
