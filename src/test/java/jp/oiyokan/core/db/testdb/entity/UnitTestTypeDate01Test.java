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
package jp.oiyokan.core.db.testdb.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.olingo.server.api.ODataResponse;
import org.junit.jupiter.api.Test;

import jp.oiyokan.OiyokanConstants;
import jp.oiyokan.OiyokanUnittestUtil;
import jp.oiyokan.common.OiyoInfo;
import jp.oiyokan.common.OiyoInfoUtil;
import jp.oiyokan.common.OiyoUrlUtil;
import jp.oiyokan.dto.OiyoSettingsDatabase;
import jp.oiyokan.util.OiyokanTestUtil;

/**
 * 後方空白が適切な状態での挙動確認。
 */
class UnitTestTypeDate01Test {
    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(UnitTestTypeDate01Test.class);

    @Test
    void test03() throws Exception {
        @SuppressWarnings("unused")
        final OiyoInfo oiyoInfo = OiyokanUnittestUtil.setupUnittestDatabase();

        final String dateStringInput = "2021-01-01";

        ODataResponse resp = OiyokanTestUtil.callPost("/ODataTest1", //
                "{\n" //
                        + "  \"Date1\": \"" + dateStringInput + "\"\n" //
                        + "}");
        String result = OiyokanTestUtil.stream2String(resp.getContent());
        // log.info(result);
        final String idString = OiyokanTestUtil.getValueFromResultByKey(result, "ID");
        assertEquals(201, resp.getStatusCode(), "DATE型を INSERTできることを確認.");

        resp = OiyokanTestUtil.callGet("/ODataTest1(" + idString + ")", null);
        result = OiyokanTestUtil.stream2String(resp.getContent());
        String dateString = OiyokanTestUtil.getValueFromResultByKey(result, "Date1");
        assertEquals("\"" + dateStringInput + "\"", dateString, "格納した Date型が同値で取得できること。");
        assertEquals(200, resp.getStatusCode());

        resp = OiyokanTestUtil.callGet("/ODataTest1",
                "$filter=Date1 eq " + OiyoUrlUtil.encodeUrlQuery(dateStringInput));

        OiyoSettingsDatabase database = OiyoInfoUtil.getOiyoDatabaseByEntitySetName(oiyoInfo, "ODataTest1");
        OiyokanConstants.DatabaseType databaseType = OiyokanConstants.DatabaseType.valueOf(database.getType());
        switch (databaseType) {
        case ORCL18:
            // TODO ORCL18では Date の eq がヒットしない。
            break;
        default:
            result = OiyokanTestUtil.stream2String(resp.getContent());
            // log.info(result);
            dateString = OiyokanTestUtil.getValueFromResultByKey(result, "Date1");
            assertEquals(200, resp.getStatusCode());
            break;
        }

        // DELETE
        resp = OiyokanTestUtil.callDelete("/ODataTest1(" + idString + ")");
        assertEquals(204, resp.getStatusCode(), "DELETEできることを確認.");
    }
}
