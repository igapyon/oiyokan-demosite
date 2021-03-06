# Release 1.19 (2021-06-03)

## EN

- Follows the main library upgrade.

## JA

- 本体ライブラリバージョンアップに追従。

# Release 1.18 (2021-05-26)

## JA

- 本体ライブラリバージョンアップに追従。
- Oiyokan本体ライブラリのインタフェース変更を追いかけ。

# Release 1.17 (2021-05-25)

## JA

- 本体ライブラリバージョンアップに追従。
- Oiyokanの説明になっていたのを、Oiyokan の DemoSite であるとの説明に更新。

# Internal (2021-05-13)

## JA

- いままで Demosite には デモサイトと UnitTest目的と2目的を満たしていたが、混乱するのでこれを分離。
- これからは Demosiateはデモサイトとしての用途に限定する。

# Release 1.14 (2021-05-10)

## EN

- Fixed search under Entity without Key (table without Primary Key).

## JA

- Key の存在しないEntity (Primary Key のないテーブル) でも検索が正しく動作するよう変更。

# Release 1.13 (2021-05-09)

## EN

- Create a new `oiyokan-initializr` project that allows you to edit the Oiyokan configuration file on the web screen.
- Add `java.io.Serializable` to DTO of setting information

## JA

- Oiyokan 設定ファイルを Web 画面で編集できる `oiyokan-initializr` プロジェクトを新規作成
- 設定情報の DTO に `java.io.Serializable` を付与

# Release 1.12 (2021-05-06)

v1.x Testing.

## EN

- Stabilization work of software v1.x.
- Fixed a bug that the DB cursor was not closed in some case.
- Created a new project oiyokan-initializr for Getting Started.
- Deployed the oiyokan library to Maven Repository.
- Changed mvn to work with en.
- Changed Javadoc to be generated with en.
- Changed to output cache control meta to http response.

## JA

- v1.x ソフトウェア安定化作業
- DBカーソルのクローズ漏れバグがあったのを訂正
- Getting Started 用のプロジェクト oiyokan-initializr を新規作成
- Maven Repository にデプロイ
- mvn を en で動作するように変更
- Javadocを en で生成するように変更
- キャッシュコントロール記述をレスポンスに出力するよう変更

# Release 1.11 (2021-05-02)

v1.x Feature freeze.

## EN

- Changed Oiyokan Library to be independent of Spring Boot.
- Added DB connection password encryption function.
- Changed to set ID in Entity search.
- Check / change group id to prepare Maven Repository registration.
- Standardizing the processing related to time and re-tested.
- Implemented CLOB, BLOB judgment processing and implement behavior switching.
- Checked SQL Server BIT type support.
- Confirmed that the request body is not output to the log by log.trace.
- Confirmed that no garbage records remain in the unit test.
- Conducted source code coverage test.
- Bug fixes.

## JA

- Oiyokan Library が Spring Boot に依存しないよう変更。
- DB接続パスワードの暗号化機能を追加。
- Entityの検索で IDを設定する。
- Maven Repository 登録を意識して group id を確認・変更。
- 時間に関する処理を共通化したうえでテストを実施。
- CLOB、BLOB判定処理および指定に伴う挙動変更を実装。
- SQL Server の BIT 型の対応確認。
- log.traceにて リクエストボディがログに出力されないよう確認。
- 単体テストでゴミレコードが残らないよう確認。
- ソースコードカバレッジテストの実施。
- その他バグ修正

# Release 1.10 (2021-04-30)

## EN

- Previously, 204 and 304 were returned when PATCH was executed, but this has been changed to return as 200 or 201 with body data.
- Changed the log format during Unit test to a shorter one.
- An error occurs when a value other than * (ie: ETag) is specified for If-Match and If-None-Match.

## JA

- PATCH実行時に、いままでは 204 や 304 を返却していたが、これをデータ付きの 200 や 201 で戻すように変更。
- Unit test 時の ログ format を短いものに変更。
- If-Match, If-None-Match に * 以外(ETag) が指定された場合にエラーにする。

# Release 1.9 (2021-04-29)

## EN

- Improved typing for null values of SQL input parameter.
- Make the name of EntitySet and EntityType of unit test data the same.

## JA

- SQL入力 parameter の NULL値の場合の型決定を改善
- 単体テストデータの EntitySet と EntityType の名称を同じにする

# Release 1.8 (2021-04-28)

## EN

- Adjusted the behavior when the primary key is autoGenKey. Relatedly, autoGenKey mapped nullable as Edm.
- Start project which test demosite from client (https://github.com/igapyon/oiyokan-demosite-test)
- Improved to log ODataLibraryException into the server log.
- Added a unit test table called ODataTests8.
- Added NULL test case of CHAR search.
- If nullable is not specified, treat as nullable.
- Bug fix.

## JA

- primary key が autoGenKey の際の挙動を調整。関連して autoGenKey は Edm として null許容とする。
- クライアントからの demosite テストプロジェクトを開始 (https://github.com/igapyon/oiyokan-demosite-test)
- ODataLibraryException がサーバログに記録されるよう改善
- ODataTests8 という単体テスト用テーブルを追加
- CHAR検索のNULLテストケースの追加 
- nullable指定なしは、null許容とする。
- Bug fix

# Release 1.7 (2021-04-25)

## EN

- In Entity PATCH, suppress the behavior of INSERT when Key automatic numbering is involved.
- When a member is used in the EQ of $ filter, the value is returned even if $ select is not specified.
- Heroku support by temporary support.
- Improving unit testing.

## JA

- Entity PATCH にて、Key自動採番を伴う場合は INSERT の挙動を抑止するようにする
- メンバーが $filter の EQ で利用された場合に、$select 指定がなくとも値返却する
- 分割されたプロジェクトでも Heroku にデプロイできるよう仮対応
- ユニットテストを改善

# Release 1.6 (2021-04-21)

## EN

- Splitted the repository into two repositories, a library and an executable web.
    - https://github.com/igapyon/oiyokan
    - https://github.com/igapyon/oiyokan-demosite
- Improved SQL Server efficiency.
- Changed to use information from Property instead of object type when reading Query execution result.
- Added the function to supplement the shortage of CHAR.
- Reduced internal ResultSetMetaData calls as much as possible.
- DB timeout time can be specified in JSON.
- Refactoring.

## JA

- リポジトリを ライブラリと実行可能なWebの2リポジトリに分割
    - https://github.com/igapyon/oiyokan
    - https://github.com/igapyon/oiyokan-demosite
- SQL Server効率アップ
- Query 実行結果の読み込み時に、オブジェクト型ではなくPropertyからの情報を利用するように変更
- CHAR の長さ不足を補完する機能を追加
- 内部的な ResultSetMetaData 呼び出しを極力減らす
- DBタイムアウト時間をJSONで指定可能にする
- リファクタリング

# Release 1.5 (2021-04-18)

## EN

- Spring Boot log settings. Output spring.log to runtime current by default
- Massive refactoring
- Removal of unnecessary classes
- Rebuild message system
- Created JDBC / Edm common functions

## JA

- Spring Boot のログ設定。デフォルトで実行時カレントに spring.log を出力
- 大規模なリファクタリング
- 不要クラスの除去
- メッセージ体系を再構築
- JDBC/Edmの共通関数の作成

# Release 1.4 (2021-04-17)

## EN

- Checked the contents of the Oiyokan setting file `oiyokan-settings.json`, and updated it to a format that is conscious of Edm standard.
- Added the feature to control authz CRUD authority for each EntitySet.
- Added the feature to avoid record count by unconditional SELECT COUNT (*).
- Rough test for supported RDBMSs.

## JA

- Oiyokan 動作設定ファイル `oiyokan-settings.json` の内容を見直し、Edm を意識した形式に更新。
- EntitySet ごとの CRUD 権限を制御する機能の追加。
- 条件なし SELECT COUNT(*) による全件カウントを EntitySet ごとに抑止する機能を追加。
- 対応 RDBMS に対する疎通テストの実施。

# Release 1.3 (2021-04-15)

## EN

- Changed the behavior of PATCH to be correct
- Removed PUT method support
- Corresponding methods are now GET, POST, PATCH, DELETE.
- Support for ROWID of getGeneratedKeys of ORACLE

## JA

- PATCH の挙動を正しくなるよう変更
- PUT メソッド対応を除去
- 対応メソッドは、GET, POST, PATCH, DELETE になった。
- ORACLE の getGeneratedKeys の ROWID 対応

# Release 1.2 (2021-04-11)

## EN

- Added Entity access function.
- Add the message ID for TIMEOUT.
- Tested support for column names containing spaces.
- In this release, ORACLE support is treated as a BETA version.

## JA

- Entity アクセス機能を追加.
- TIMEOUTについてメッセージIDを採番.
- 空白を含む項目名への対応をテスト.
- 今回のリリースでは ORACLE サポートは BETA 版扱い.

# Release 1.1 (2021-04-06)

- Added MySQL v8, SQL Server 2008, Oracle XE 18c to supported rdbms.
- Some bugs have been fixed.

## Supported target RDBMS

- PostgreSQL (13)
- MySQL (8)
- SQL Server (2008)
- Oracle XE (18c)

## Supported OData system query options

- $select
- $count
- $filter
- $orderby
- $top
- $skip

# Release 1.0 (2021-03-28)

- First stable release (v1.0.20210328)

## Supported target RDBMS

- PostgreSQL (13)

# Release 0.9 (2021-03-19)

- First beta release

## OData v4 server のサンプル(simple-odata4) を祖先

oiyokan プロジェクトは、OData v4 server のシンプルなサンプル(https://github.com/igapyon/simple-odata4) を祖先に作成されたものです。
