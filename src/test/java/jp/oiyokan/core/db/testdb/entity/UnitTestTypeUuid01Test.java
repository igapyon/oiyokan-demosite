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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.olingo.server.api.ODataResponse;
import org.junit.jupiter.api.Test;

import jp.oiyokan.OiyokanUnittestUtil;
import jp.oiyokan.common.OiyoInfo;
import jp.oiyokan.util.OiyokanTestUtil;

/**
 * UUID 型に着眼したテスト.
 */
class UnitTestTypeUuid01Test {
    @Test
    void test01() throws Exception {
        @SuppressWarnings("unused")
        final OiyoInfo oiyoInfo = OiyokanUnittestUtil.setupUnittestDatabase();

        final int TEST_ID = OiyokanTestUtil.getNextUniqueId();

        ODataResponse resp = OiyokanTestUtil.callPost("/ODataTest7", "{\n" //
                + "  \"ID\":" + TEST_ID + "\n" //
                + "}");
        String result = OiyokanTestUtil.stream2String(resp.getContent());
        assertTrue(
                result.startsWith("{\"@odata.context\":\"$metadata#ODataTest7\",\"ID\":" + TEST_ID
                        + ",\"Name\":\"UUID UnitTest\",\"Description\":\"UUID UnitTest table.\",\"Uuid1\":"), //
                "INSERTできることを確認.");
        assertEquals(201, resp.getStatusCode());

        resp = OiyokanTestUtil.callGet("/ODataTest7(" + TEST_ID + ")", null);
        result = OiyokanTestUtil.stream2String(resp.getContent());
        // System.err.println(result);
        assertEquals(200, resp.getStatusCode(), "INSERTしたレコードが格納されていることを確認.");

        /// 通常のfilter
        resp = OiyokanTestUtil.callGet("/ODataTest7", "$filter=ID eq " + TEST_ID);
        result = OiyokanTestUtil.stream2String(resp.getContent());
        // System.err.println("TRACE: " + result);
        assertTrue(
                result.startsWith("{\"@odata.context\":\"$metadata#ODataTest7\",\"value\":[{\"ID\":" + TEST_ID
                        + ",\"Name\":\"UUID UnitTest\",\"Description\":\"UUID UnitTest table.\",\"Uuid1\":"), //
                "通常のFILTER検索ができることを確認.");
        assertEquals(200, resp.getStatusCode());
        @SuppressWarnings("unused")
        final String uuid1String = OiyokanTestUtil.getValueFromResultByKey(result, "Uuid1");
        // System.err.println(uuid1String);

        // DELETE
        resp = OiyokanTestUtil.callDelete("/ODataTest7(" + TEST_ID + ")");
        assertEquals(204, resp.getStatusCode(), "DELETEできることを確認.");

        resp = OiyokanTestUtil.callGet("/ODataTest7(" + TEST_ID + ")", null);
        assertEquals(404, resp.getStatusCode(), "DELETEされたことを確認.");
    }
}
