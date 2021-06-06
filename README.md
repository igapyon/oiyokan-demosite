# Oiyokan DemoSite

This is the Oiyokan demo site, available under the Apache license, where you can experience the OData v4 Server for free.

## Provide Sakila DVD rental as OData v4 Server.

- This demo site uses Oiyokan to provide the [Sakila DVD rental](https://www.jooq.org/sakila) stored in the h2 database as an OData v4 Server.
- Metadata of OData v4 is provided at [$metadata](http://localhost:8080/odata4.svc/$metadata).
- [Source code at github](https://github.com/igapyon/oiyokan-demosite).

## Getting Started with the Oiyokan

Getting Started with the Oiyokan can be viewed at the following URL (written in Japanese).

- https://qiita.com/igapyon/items/3fbdb0f3d3520a54f2a9

## Supported OData Method

| HTTP method               | 対応する SQL | 準拠する OData specification 記述               |
| ------                    | ------            | ------                                             |
| GET                       | SELECT            | [Request](https://docs.oasis-open.org/odata/odata/v4.01/odata-v4.01-part1-protocol.html#sec_RequestingData) ([Individual](https://docs.oasis-open.org/odata/odata/v4.01/odata-v4.01-part1-protocol.html#sec_RequestingIndividualEntities), [Query](https://docs.oasis-open.org/odata/odata/v4.01/odata-v4.01-part1-protocol.html#sec_SystemQueryOptionselect)) |
| POST                      | INSERT            | [Create](https://docs.oasis-open.org/odata/odata/v4.01/odata-v4.01-part1-protocol.html#sec_CreateanEntity) |
| PATCH                     | UPSERT            | [Update](https://docs.oasis-open.org/odata/odata/v4.01/odata-v4.01-part1-protocol.html#sec_UpdateanEntity) |
| PATCH (If-Match="*")      | UPDATE            | [Update with header If-Match](https://docs.oasis-open.org/odata/odata/v4.01/odata-v4.01-part1-protocol.html#sec_HeaderIfMatch) |
| PATCH (If-None-Match="*") | INSERT            | [Update with header If-None-Match](https://docs.oasis-open.org/odata/odata/v4.01/odata-v4.01-part1-protocol.html#sec_HeaderIfNoneMatch) |
| DELETE                    | DELETE            | [Delete](https://docs.oasis-open.org/odata/odata/v4.01/odata-v4.01-part1-protocol.html#sec_DeleteanEntity) |

## Supported OData system query options

- $select
- $count
- $filter
- $orderby
- $top
- $skip
- Note: In Oiyokan 1.x, $search and $expand are not supported.

## Oiyokan in Maven repository

- [Maven Repository - Oiyokan](https://mvnrepository.com/artifact/jp.igapyon.oiyokan)

## Oiyokan 関連リポジトリ 

- [Oiyokan Library - github](https://github.com/igapyon/oiyokan)
- [Oiyokan Initializr - github](https://github.com/igapyon/oiyokan-initializr)
- [Oiyokan Demosite - github](https://github.com/igapyon/oiyokan-demosite)
- [Oiyokan Demosite-Test - github](https://github.com/igapyon/oiyokan-demosite-test)

# Try the Oiyokan OData v4 sample server

## Local

You can run OData v4 DemoSite server at your computer.

Check out source code repository and you can run it as Spring Boot Web Server.

```sh
mvn package spring-boot:run
```

## Heroku

You can find the running Oiyokan DemoSite server at Heroku.

- https://oiyokan.herokuapp.com/
