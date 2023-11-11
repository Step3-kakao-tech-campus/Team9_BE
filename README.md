# 부산대 9조 링크나무🌲

[//]: # (로고)
링크 통합 관리 서비스 **링크나무** 입니다.

## 목차 
> - [👋 프로젝트 소개](#프로젝트-소개)
> - [📖 기획 의도](#기획-의도)
> - [👩‍👩‍👧‍👧 팀원 소개](#팀원-소개)
> - [📋 배포 링크](#배포-링크)
> - [💻 아키텍쳐](#아키텍쳐)
> - [📜 ERD](#erd)
> - [✏️ 주요 기능](#주요-기능)
> - [📄 API 모아보기](#api-모아보기)
> - [📁 파일 구조](#파일-구조)
> - [🚩 시작 가이드](#시작-가이드)
> - [🖥️ 기술 스택](#기술-스택)
> - [🖥 코딩 컨벤션](#코딩-컨벤션)
> - [🔗 관련 링크](#관련-링크)
> - [🔒 라이선스](#라이선스)


## 프로젝트 소개

|내용|설명|
|-----------|----|
|프로젝트 이름 |링크나무| 
|한줄 소개 |다양한 곳에 분산되어 있는 링크를 통합해 관리 및 공유해주는 사이트|
|개발 기간 |2023.09 - 2023.11|


## 기획 의도

링크나무 프로젝트는 **분산된 링크를 한눈에 볼 수 있도록 통합**하고, 다양한 소스에서 링크를 효율적으로 관리하여 사용자들에게 직관적이고 편리한 링크 관리 경험을 제공하는 것을 목표로 하고 있습니다.

평소에 감명 깊었던 유튜브 영상이나 기술 블로그 같은 북마크를 저장하는 것은 많은 사람들이 공감할 수 있는 경험이죠.
그러나 이렇게 저장해놔도 **어디에 저장해놨는지 기억하지 못하거나, 저장한 사실을 잊고 다시 검색**하는 일은 흔한 문제입니다.
이렇게 하면 저장해 놓은 링크를 효과적으로 관리하는 데 어려움이 생깁니다.

이러한 문제에 대응하기 위해 링크나무는 사용자들이 간편하게 링크를 저장하고 찾을 수 있는 다양한 기능을 제공합니다. 
**그룹화, 태깅, 공유 기능을 통해 링크를 카테고리화**하고 필요에 따라 쉽게 찾을 수 있도록 도와줍니다. 

**설문조사 결과에 따르면** 대다수의 인터넷 사용자들이 노션, 구글 독스, 카카오톡, 이메일 등을 활용하여 링크를 저장한다는 사실을 확인했습니다.
따라서, 저희는 **노션, 구글 독스, 카카오톡 등 다양한 소스에서 링크를 가져와 한 곳에서 통합적으로 관리**할 수 있는 기능을 제공합니다.
이러한 다양한 소스에서의 링크를 링크나무를 통해 통합 관리함으로써 사용자들은 불필요한 중복 작업을 피하고, 한눈에 모든 링크를 확인할 수 있는 편의성을 경험할 수 있을 것입니다. 


## 팀원 소개

|조장|타임 키퍼 |기획 리더| 리액셔너|
|:-----:|:------:|:--------:|:------:|
| [김태호](https://github.com/Train0303) | [손재성](https://github.com/JS970)|[조영진](https://github.com/jhy0285)|[김이경](https://github.com/ikyeong) |
| <img src="https://github.com/Train0303.png" width="100"> | <img src="https://github.com/JS970.png" width="100"> | <img src="https://github.com/jhy0285.png" width="100"> | <img src="https://github.com/ikyeong.png" width="100"> |

## 배포 링크
-  https://k3f12e8e83a24a.user-app.krampoline.com

## 아키텍쳐
<img src="https://github.com/Step3-kakao-tech-campus/Team9_BE/assets/89145559/355174f3-dd9e-4317-b932-27d0cda2bf0a" width="90%"/>

## ERD 
![image](https://github.com/Step3-kakao-tech-campus/Team9_BE/assets/89145559/72351fa5-3b74-4991-bba7-01b96392809b)


## 주요 기능
|     기능     | 화면  |             설명              |
|:----------:|:--------------:|:-------------:|
| 카카오톡 링크 추출 |    <img src="https://github.com/ikyeong/Team9_BE/assets/103908923/0a10ad03-e34e-4c85-b360-29027ff4f84a" width="70%" >| 카카오톡 메세지에 포함된 링크를 추출해 저장한다. |
|   노션 연동    |  <img src="https://github.com/ikyeong/Team9_BE/assets/103908923/3dd3d89a-2c89-458c-bcd3-6038f973678d" width="70%" >|  원하는 노션 페이지에 저장된 링크를 저장한다.  |
|  구글 문서 연동  |  <img src="https://github.com/ikyeong/Team9_BE/assets/103908923/a24a17d4-7a0f-412e-ba3b-f6325829d95f" width="70%" >  |     구글문서에 저장된 링크를 저장한다.     |
|   공유 링크 생성   |  <img src="https://github.com/ikyeong/Team9_BE/assets/103908923/23ff717e-b5dc-41ec-b4ad-1b84db57324f" width="80%" > | 워크스페이스/카테고리를 다른 유저와 공유하는 링크를 생성한다. |
|공유 링크로 조회|<img src="https://github.com/ikyeong/Team9_BE/assets/103908923/fbde6ef6-d178-4b45-8fce-ded2a434ff91" width="70%" >|공유 링크를 통해 다른 유저의 워크스페이스/카테고리를 조회한다.|
|공유 링크로 저장|<img src="https://github.com/ikyeong/Team9_BE/assets/103908923/792f0431-b729-40a8-9a9d-e562e69868c6" width="70%" >|공유 링크를 통해 다른 유저의 워크스페이스/카테고리를 저장한다. |
|북마크 검색| <img src="https://github.com/ikyeong/Team9_BE/assets/103908923/1683d704-3b56-4913-a898-e36fc03de50b" width="70%" > | 검색창을 통해 원하는 키워드의 북마크를 검색한다.|


## API 모아보기 

https://www.notion.so/taeho1234/API-eec9b74e1dff454d9a51789550cb1b85?pvs=4


## 파일 구조

https://www.notion.so/taeho1234/API-eec9b74e1dff454d9a51789550cb1b85?pvs=4


## 시작 가이드 

https://www.notion.so/taeho1234/API-eec9b74e1dff454d9a51789550cb1b85?pvs=4


## 기술 스택 

https://www.notion.so/taeho1234/API-eec9b74e1dff454d9a51789550cb1b85?pvs=4


## 코딩 컨벤션 
[캠퍼스 핵데이 Java 코딩 컨벤션](https://github.com/naver/hackday-conventions-java)

editorconfig 설정
```
# top-most EditorConfig file
root = true

[*]
charset = utf-8
end_of_line = lf
insert_final_newline = true

[*.bat]
end_of_line = crlf

[*.adoc]
indent_style = space
indent_size = 4
tab_width = 4
trim_trailing_whitespace = true
```

Intellij 포멧터 설정
```
<code_scheme name="Naver-coding-convention-v1.2">
  <option name="CLASS_COUNT_TO_USE_IMPORT_ON_DEMAND" value="99" />
  <option name="NAMES_COUNT_TO_USE_IMPORT_ON_DEMAND" value="1" />
  <option name="IMPORT_LAYOUT_TABLE">
    <value>
      <emptyLine />
      <package name="" withSubpackages="true" static="true" />
      <emptyLine />
      <package name="java" withSubpackages="true" static="false" />
      <emptyLine />
      <package name="javax" withSubpackages="true" static="false" />
      <emptyLine />
      <package name="org" withSubpackages="true" static="false" />
      <emptyLine />
      <package name="net" withSubpackages="true" static="false" />
      <emptyLine />
      <package name="com" withSubpackages="true" static="false" />
      <emptyLine />
      <package name="" withSubpackages="true" static="false" />
      <emptyLine />
      <package name="com.nhncorp" withSubpackages="true" static="false" />
      <emptyLine />
      <package name="com.navercorp" withSubpackages="true" static="false" />
      <emptyLine />
      <package name="com.naver" withSubpackages="true" static="false" />
      <emptyLine />
    </value>
  </option>
  <option name="RIGHT_MARGIN" value="120" />
  <option name="ENABLE_JAVADOC_FORMATTING" value="false" />
  <option name="JD_KEEP_EMPTY_LINES" value="false" />
  <option name="FORMATTER_TAGS_ENABLED" value="true" />
  <XML>
    <option name="XML_LEGACY_SETTINGS_IMPORTED" value="true" />
  </XML>
  <codeStyleSettings language="JAVA">
    <option name="LINE_COMMENT_AT_FIRST_COLUMN" value="false" />
    <option name="LINE_COMMENT_ADD_SPACE" value="true" />
    <option name="KEEP_FIRST_COLUMN_COMMENT" value="false" />
    <option name="KEEP_CONTROL_STATEMENT_IN_ONE_LINE" value="false" />
    <option name="KEEP_BLANK_LINES_IN_DECLARATIONS" value="1" />
    <option name="KEEP_BLANK_LINES_IN_CODE" value="1" />
    <option name="KEEP_BLANK_LINES_BEFORE_RBRACE" value="1" />
    <option name="ALIGN_MULTILINE_PARAMETERS" value="false" />
    <option name="SPACE_AFTER_TYPE_CAST" value="false" />
    <option name="SPACE_BEFORE_ARRAY_INITIALIZER_LBRACE" value="true" />
    <option name="CALL_PARAMETERS_WRAP" value="1" />
    <option name="METHOD_PARAMETERS_WRAP" value="1" />
    <option name="EXTENDS_LIST_WRAP" value="1" />
    <option name="METHOD_CALL_CHAIN_WRAP" value="5" />
    <option name="THROWS_LIST_WRAP" value="5" />
    <option name="EXTENDS_KEYWORD_WRAP" value="1" />
    <option name="BINARY_OPERATION_WRAP" value="1" />
    <option name="BINARY_OPERATION_SIGN_ON_NEXT_LINE" value="true" />
    <option name="TERNARY_OPERATION_WRAP" value="1" />
    <option name="ARRAY_INITIALIZER_WRAP" value="1" />
    <indentOptions>
      <option name="CONTINUATION_INDENT_SIZE" value="4" />
      <option name="USE_TAB_CHARACTER" value="true" />
    </indentOptions>
  </codeStyleSettings>
</code_scheme>
```
## 관련 링크
- [링크나무 팀 노션](https://www.notion.so/taeho1234/9-c9956e710af447b4b0aa1197fc4cf76f)
- [링크나무 API 명세서](https://www.notion.so/taeho1234/API-eec9b74e1dff454d9a51789550cb1b85)
- [링크나무 ERD](https://www.erdcloud.com/d/abkTHAyST7msXzBh7)
- [링크나무 와이어프레임](https://www.figma.com/file/RD1mjdlArlVzteT8L8g6hs/%EC%99%80%EC%9D%B4%EC%96%B4-%ED%94%84%EB%A0%88%EC%9E%84-%ED%85%8C%EC%8A%A4%ED%8A%B8?type=design&node-id=114-299&mode=design&t=xwNpPiwxeWToXGlN-0)
- [링크나무 기획안](https://www.notion.so/taeho1234/5edcf26e1f844c4c88fc1b471abbebb5)
- [링크나무 기획안](https://www.notion.so/taeho1234/5edcf26e1f844c4c88fc1b471abbebb5)
- [링크나무 테스트 결과보고서](https://www.notion.so/a41e2905a8644c249f95d58c9344e933)
- [링크나무 테스트 시나리오 명세서](https://www.notion.so/04a226ff46b6469186e05694ed916726)
---

## 라이선스 

The MIT License (MIT)

Copyright (c) <year> <copyright holders>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.














<details>
    <summary><h2>카카오 테크 캠퍼스 3단계 진행 보드</h2></summary>
    <br>

## 배포와 관련하여

```

최종 배포는 크램폴린으로 배포해야 합니다.

하지만 배포 환경의 불편함이 있는 경우를 고려하여 

임의의 배포를 위해 타 배포 환경을 자유롭게 이용해도 됩니다. (단, 금액적인 지원은 어렵습니다.)

아래는 추가적인 설정을 통해 (체험판, 혹은 프리 티어 등)무료로 클라우드 배포가 가능한 서비스입니다.

ex ) AWS(아마존), GCP(구글), Azure(마이크로소프트), Cloudtype 

```
## Notice

```
필요 산출물들은 수료 기준에 영향을 주는 것은 아니지만, 
주차 별 산출물을 기반으로 평가가 이루어 집니다.

주차 별 평가 점수는 추 후 최종 평가에 최종 합산 점수로 포함됩니다.
```

![레포지토리 운영-001 (1)](https://github.com/Step3-kakao-tech-campus/practice/assets/138656575/acb0dccd-0441-4200-999a-981865535d5f)
![image](https://github.com/Step3-kakao-tech-campus/practice/assets/138656575/b42cbc06-c5e7-4806-8477-63dfa8e807a0)

[git flowchart_FE.pdf](https://github.com/Step3-kakao-tech-campus/practice/files/12521045/git.flowchart_FE.pdf)


<br>

## 필요 산출물
<details>
<summary>Step3. Week-1</summary>
<div>

✅**1주차**

```
    - 5 Whys
    - 마켓 리서치
    - 페르소나 & 저니맵
    - 와이어 프레임
    - 칸반보드
```

</div>
</details>

---

<details>
<summary>Step3. Week-2</summary>
<div>

✅**2주차**

```
    - ERD 설계서
    
    - API 명세서
```

</div>
</details>

---

<details>
<summary>Step3. Week-3</summary>
<div>

✅**3주차**

```
    - 최종 기획안
```

</div>
</details>

---

<details>
<summary>Step3. Week-4</summary>
<div>

✅**4주차**

```
    - 4주차 github
    
    - 4주차 노션
```

</div>
</details>

---
<details>
<summary>Step3. Week-5</summary>
<div>

✅**5주차**

```
    - 5주차 github
    
    - 5주차 노션
```

</div>
</details>

---

<details>
<summary>Step3. Week-6</summary>
<div>

✅**6주차**

```
    - 6주차 github
    
    - 중간발표자료
    
    - 피어리뷰시트
```

</div>
</details>

---

<details>
<summary>Step3. Week-7</summary>
<div>

✅**7주차**

```
    - 7주차 github
    
    - 7주차 노션
```

</div>
</details>

---

<details>
<summary>Step3. Week-8</summary>
<div>

✅**8주차**

```
    - 중간고사
    
```

</div>
</details>

---

<details>
<summary>Step3. Week-9</summary>
<div>

✅**9주차**

```
    - 9주차 github
    
    - 9주차 노션
```

</div>
</details>

---

<details>
<summary>Step3. Week-10</summary>
<div>

✅**10주차**

```
    - 10주차 github
    
    - 테스트 시나리오 명세서
    
    - 테스트 결과 보고서
```

</div>
</details>

---

<details>
<summary>Step3. Week-11</summary>
<div>

✅**11주차**

```
    - 최종 기획안
    
    - 배포 인스턴스 링크
```

</div>
</details>

---

## **과제 상세 : 수강생들이 과제를 진행할 때, 유념해야할 것**

```
1. README.md 파일은 동료 개발자에게 프로젝트에 쉽게 랜딩하도록 돕는 중요한 소통 수단입니다.
해당 프로젝트에 대해 아무런 지식이 없는 동료들에게 설명하는 것처럼 쉽고, 간결하게 작성해주세요.

2. 좋은 개발자는 디자이너, 기획자, 마케터 등 여러 포지션에 있는 분들과 소통을 잘합니다.
UI 컴포넌트의 명칭과 이를 구현하는 능력은 필수적인 커뮤니케이션 스킬이자 필요사항이니 어떤 상황에서 해당 컴포넌트를 사용하면 좋을지 고민하며 코드를 작성해보세요.

```

<br>

## **코드리뷰 관련: review branch로 PR시, 아래 내용을 포함하여 코멘트 남겨주세요.**

**1. PR 제목과 내용을 아래와 같이 작성 해주세요.**

> PR 제목 : 부산대_0조_아이템명_0주차
>

<br>

</details>
