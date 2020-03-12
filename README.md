# Spring / Keycloak Tutorials

This is a sandbox / sample project to illustrate [this blog article](https://blog.ineat-group.com/2017/12/securisez-vos-apis-spring-avec-keycloak-3-utilisation-des-connecteurs-spring-de-keycloak/), part of our [Keycloak series](https://blog.ineat-group.com/tag/keycloak-series/).

## Technologies

* Java 11
* JUnit 5
* Spring Boot 2.4.2
* Keycloak 12.0.2

## Pre-requisites

* Start your Keycloak server before running the app
>  👉 [How to install & start Keycloak](https://blog.ineat-group.com/2017/11/securisez-vos-apis-spring-avec-keycloak-1-installation-de-keycloak/)

* Create a demo realm working with this app

> * Import the provided [realm-export.json](realm-export.json)
> * 👉 Or follow [How to create a realm for this sample project](https://blog.ineat-group.com/2017/11/securisez-vos-apis-spring-avec-keycloak-2-parametrage-dun-domaine-keycloak/)

* Create 2 users
    * One `ineat-admin` / `password`  with `ADMIN` role associated
    * One `ineat-user` / `password` With `USER` role associated

## How to start

* For test :

```shell
mvn clean test
```

* To run the app :

```shell
mvn spring-boot:run
```

## How to test the Keycloak security

* Use the provided [postman_collection_v2.json](postman_collection_v2.json)
    * Generate an `access_token` thanks to the `KEYCLOAK  request TOKEN for USER` or `KEYCLOAK  request TOKEN for ADMIN`
    * Use this `access_token` as `Bearer` to call the protected `/user` or `/admin` endpoints thanks to the `Request /user path` or `Request /admin path`

