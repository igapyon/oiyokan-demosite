/*
 * Copyright 2021 Toshiki Iga
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.oiyokan.demosite.oiyogen;

import java.io.File;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jp.oiyokan.OiyokanConstants;
import jp.oiyokan.OiyokanUnittestUtil;
import jp.oiyokan.common.OiyoCommonJdbcUtil;
import jp.oiyokan.common.OiyoInfo;
import jp.oiyokan.common.OiyoInfoUtil;
import jp.oiyokan.dto.OiyoSettings;
import jp.oiyokan.dto.OiyoSettingsDatabase;
import jp.oiyokan.dto.OiyoSettingsEntitySet;
import jp.oiyokan.oiyogen.OiyokanSettingsGenUtil;
import jp.oiyokan.util.OiyoEncryptUtil;

/**
 * Generate oiyokan-unittest-settings.json
 */
class Gen11OiyokanSettingsJsonTest {
    private static final String TARGET_UNITTEST_DATABASE = "oiyoUnitTestDb";
    // private static final String TARGET_UNITTEST_DATABASE = "postgres1";
    // private static final String TARGET_UNITTEST_DATABASE = "mysql2";

    @Test
    void test01() throws Exception {
        final OiyoInfo oiyoInfo = OiyokanUnittestUtil.setupUnittestDatabase();

        OiyoSettingsDatabase settingsDatabase = OiyoInfoUtil.getOiyoDatabaseByName(oiyoInfo, TARGET_UNITTEST_DATABASE);
        System.err.println("確認対象データベース: " + settingsDatabase.getName());

        try (Connection connTargetDb = OiyoCommonJdbcUtil.getConnection(settingsDatabase)) {
            final List<String> tableNameList = new ArrayList<>();

            ResultSet rset = connTargetDb.getMetaData().getTables(null, "%", "%", new String[] { "TABLE", "VIEW" });
            for (; rset.next();) {
                final String tableName = rset.getString("TABLE_NAME");
                tableNameList.add(tableName);
            }

            Collections.sort(tableNameList);

            final OiyoSettings oiyoSettings = new OiyoSettings();
            oiyoSettings.setEntitySet(new ArrayList<>());

            // データベース設定も生成.
            oiyoSettings.setNamespace("Oiyokan");
            oiyoSettings.setContainerName("Container");
            oiyoSettings.setDatabase(new ArrayList<>());

            final String[][] DATABASE_SETTINGS = new String[][] { //
                    { "oiyoUnitTestDb", "h2", "Oiyokan internal Target Test DB. Used for build unit test.", //
                            "org.h2.Driver", //
                            "jdbc:h2:file:./src/main/resources/db/oiyokan-internal;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=FALSE;MODE=MSSQLServer", //
                            "sa", "" }, //
                    { "postgres1", "PostgreSQL",
                            "Sample postgres settings. Change the settings to suit your environment.", //
                            "org.postgresql.Driver", //
                            "jdbc:postgresql://localhost:5432/dvdrental", //
                            "", "" }, //
                    { "mysql1", "MySQL", "Sample MySQL settings. Change the settings to suit your environment.", //
                            "com.mysql.jdbc.Driver", //
                            "jdbc:mysql://localhost/mysql?useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8&useCursorFetch=true&defaultFetchSize=128&useServerPrepStmts=true&emulateUnsupportedPstmts=false", //
                            "root", "passwd123" }, //
                    { "mysql2", "MySQL",
                            "Sample MySQL settings for Sakila. Change the settings to suit your environment.", //
                            "com.mysql.jdbc.Driver", //
                            "jdbc:mysql://localhost/sakila?useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8&useCursorFetch=true&defaultFetchSize=128&useServerPrepStmts=true&emulateUnsupportedPstmts=false", //
                            "root", "passwd123" }, //
                    { "mssql1", "SQLSV2008",
                            "Sample MS SQL Server 2008 settings. Change the settings to suit your environment.", //
                            "com.microsoft.sqlserver.jdbc.SQLServerDriver", //
                            "jdbc:sqlserver://localhost\\SQLExpress", //
                            "sa", "passwd123" }, //
                    { "oracle1", "ORCL18",
                            "Sample Oracle XE (18c) settings. Change the settings to suit your environment.", //
                            "oracle.jdbc.driver.OracleDriver", //
                            "jdbc:oracle:thin:@10.0.2.15:1521/xepdb1", //
                            "orauser", "passwd123" }, //
            };

            for (String[] databaseSetting : DATABASE_SETTINGS) {
                OiyoSettingsDatabase database = new OiyoSettingsDatabase();
                oiyoSettings.getDatabase().add(database);
                database.setName(databaseSetting[0]);
                database.setType(databaseSetting[1]);
                database.setDescription(databaseSetting[2]);
                database.setJdbcDriver(databaseSetting[3]);
                database.setJdbcUrl(databaseSetting[4]);
                database.setJdbcUser(databaseSetting[5]);
                database.setJdbcPassEnc(OiyoEncryptUtil.encrypt(databaseSetting[6], oiyoInfo.getPassphrase()));
            }

            for (String tableName : tableNameList) {
                try {
                    final OiyoSettingsEntitySet entitySet = OiyokanSettingsGenUtil.generateSettingsEntitySet(
                            connTargetDb, tableName, OiyokanConstants.DatabaseType.valueOf(settingsDatabase.getType()));
                    oiyoSettings.getEntitySet().add(entitySet);
                    entitySet.setDbSettingName(TARGET_UNITTEST_DATABASE);
                } catch (Exception ex) {
                    System.err.println("Fail to read table: " + tableName);
                }
            }

            // Sakila Sample db の名称調整。
            for (OiyoSettingsEntitySet entitySet : oiyoSettings.getEntitySet()) {
                if (entitySet.getName().equals("actor")) {
                    entitySet.setName("SklActors");
                    entitySet.getEntityType().setName("SklActor");
                } else if (entitySet.getName().equals("actor_info")) {
                    entitySet.setName("SklActorInfos");
                    entitySet.getEntityType().setName("SklActorInfo");
                    entitySet.getEntityType().getKeyName().add("actor_id");
                } else if (entitySet.getName().equals("address")) {
                    entitySet.setName("SklAddresses");
                    entitySet.getEntityType().setName("SklAddress");
                } else if (entitySet.getName().equals("category")) {
                    entitySet.setName("SklCategories");
                    entitySet.getEntityType().setName("SklCategory");
                } else if (entitySet.getName().equals("city")) {
                    entitySet.setName("SklCities");
                    entitySet.getEntityType().setName("SklCity");
                } else if (entitySet.getName().equals("country")) {
                    entitySet.setName("SklCountries");
                    entitySet.getEntityType().setName("SklCountry");
                } else if (entitySet.getName().equals("customer")) {
                    entitySet.setName("SklCustomers");
                    entitySet.getEntityType().setName("SklCustomer");
                } else if (entitySet.getName().equals("customer_list")) {
                    entitySet.setName("SklCustomerLists");
                    entitySet.getEntityType().setName("SklCustomerList");
                    entitySet.getEntityType().getKeyName().add("id");
                } else if (entitySet.getName().equals("film")) {
                    entitySet.setName("SklFilms");
                    entitySet.getEntityType().setName("SklFilm");
                } else if (entitySet.getName().equals("film_actor")) {
                    entitySet.setName("SklFilmActors");
                    entitySet.getEntityType().setName("SklFilmActor");
                } else if (entitySet.getName().equals("film_category")) {
                    entitySet.setName("SklFilmCategories");
                    entitySet.getEntityType().setName("SklFilmCategory");
                } else if (entitySet.getName().equals("film_list")) {
                    entitySet.setName("SklFilmLists");
                    entitySet.getEntityType().setName("SklFilmList");
                    entitySet.getEntityType().getKeyName().add("fid");
                } else if (entitySet.getName().equals("inventory")) {
                    entitySet.setName("SklInventories");
                    entitySet.getEntityType().setName("SklInventory");
                } else if (entitySet.getName().equals("language")) {
                    entitySet.setName("SklLanguages");
                    entitySet.getEntityType().setName("SklLanguage");
                } else if (entitySet.getName().equals("nicer_but_slower_film_list")) {
                    entitySet.setName("SklNicerButSlowerFilmLists");
                    entitySet.getEntityType().setName("SklNicerButSlowerFilmList");
                    entitySet.getEntityType().getKeyName().add("fid");
                } else if (entitySet.getName().equals("payment")) {
                    entitySet.setName("SklPayments");
                    entitySet.getEntityType().setName("SklPayment");
                } else if (entitySet.getName().equals("rental")) {
                    entitySet.setName("SklRentals");
                    entitySet.getEntityType().setName("SklRental");
                } else if (entitySet.getName().equals("sales_by_film_category")) {
                    entitySet.setName("SklSalesByFilmCategories");
                    entitySet.getEntityType().setName("SklSalesByFilmCategory");
                    entitySet.getEntityType().getKeyName().add("category");
                } else if (entitySet.getName().equals("sales_by_store")) {
                    entitySet.setName("SklSalesByStores");
                    entitySet.getEntityType().setName("SklSalesByStore");
                    entitySet.getEntityType().getKeyName().add("store");
                } else if (entitySet.getName().equals("staff")) {
                    entitySet.setName("SklStaffs");
                    entitySet.getEntityType().setName("SklStaff");
                } else if (entitySet.getName().equals("staff_list")) {
                    entitySet.setName("SklStaffLists");
                    entitySet.getEntityType().setName("SklStaffList");
                    entitySet.getEntityType().getKeyName().add("id");
                } else if (entitySet.getName().equals("store")) {
                    entitySet.setName("SklStores");
                    entitySet.getEntityType().setName("SklStore");
                }
            }

            Gen01OiyokanUnittestSettingsJsonTest.adjustUnitTableName(oiyoSettings);
            Collections.sort(oiyoSettings.getEntitySet(), new Comparator<OiyoSettingsEntitySet>() {
                @Override
                public int compare(OiyoSettingsEntitySet o1, OiyoSettingsEntitySet o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(writer, oiyoSettings);
            writer.flush();

            new File("./target/generated-oiyokan").mkdirs();
            final File generateFile = new File("./target/generated-oiyokan/auto-generated-oiyokan-settings.json");
            FileUtils.writeStringToFile(generateFile, writer.toString(), "UTF-8");
            System.err.println("oiyokan unittest setting file auto-generated: " + generateFile.getCanonicalPath());
        }
    }
}
