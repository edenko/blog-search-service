<h1 align="center">Welcome to Blog-Search-Service 👋</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-0.5.0-blue.svg?cacheSeconds=2592000" />
  <img alt="License" src="https://img.shields.io/badge/License-MIT-yellow.svg"/>
</p>

> Springboot simple "Blog Search Service" with Multi Module
> (현재 local 동작 상태)

## Requirements
* Java 11.0
* Springboot 2.5.12 with Gradle
* H2 Database with JPA


## Skills
* JUnit : 테스트 자동화
* Lombok : 코드 간략 표현
* Redis : 고성능 키-값 저장소
* Design patterns
  * 오픈 API 추가 및 교체를 위한 Adapter pattern, Template pattern
  * 전반적인 Strategy pattern


## Features
* 블로그 검색
    * 오픈 API 이용하여 키워드 검색 기능
        * 정확도순, 최신순 호출 기능
        * Pagination 형태로 제공
* 인기 검색어 상위 10위 키워드 제공


## App Structure
* Multi Module 
  * common-api : config, error, entity, repository, utils
  * external-api : 외부 api 호출 등 외부 로직 관련 모듈 (server.port=8091)
  * internal-api : 내부 로직 관련 모듈 (server.port=8092)
```bash
search/
├── common-api
│   ├── src
│   └── build.gradle
│
├── external-api
│   ├── src
│   └── build.gradle
│
├── internal-api
│   ├── src
│   └── build.gradle
│
├── jar
│   ├── external-api-0.0.1-SNAPSHOT.jar
│   └── internal-api-0.0.1-SNAPSHOT.jar
│
├── sql
│   └── ddl.sql
│
├── build.gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
├── README.md
└── .gitignore
```


## List of API Endpoints
```sh
+--------+-----------------------------------------------------+
  Method | URI                | PARAMS          | DESC
+--------+-----------------------------------------------------+
  GET    | /api/v1/list       | query, sort     | 블로그 검색
  GET    | /api/v1/top/list   |                 | 인기 검색어
+--------+-----------------------------------------------------+
```
* http://localhost:8091/api/v1/list?query=더글로리&sort=acc
  * query : 검색 키워드  ex) 더글로리
  * sort : acc(정확도순), date(최신순)


## Let's try it!
`java -jar external-api-0.0.1-SNAPSHOT.jar` or `java -jar internal-api-0.0.1-SNAPSHOT.jar`
```bash
search/
...

└── jar
    ├── external-api-0.0.1-SNAPSHOT.jar
    └── internal-api-0.0.1-SNAPSHOT.jar
```



## Author
👤 **HaYoung Ko**

* Github: [@edenko](https://github.com/edenko)
* email: goodeden3@gmail.com
