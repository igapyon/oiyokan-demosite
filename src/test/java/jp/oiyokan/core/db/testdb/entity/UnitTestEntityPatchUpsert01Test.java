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

import org.apache.olingo.server.api.ODataResponse;
import org.junit.jupiter.api.Test;

import jp.oiyokan.OiyokanUnittestUtil;
import jp.oiyokan.common.OiyoInfo;
import jp.oiyokan.common.OiyoUrlUtil;
import jp.oiyokan.util.OiyokanTestUtil;

/**
 * Entityの基本的なテスト.
 */
class UnitTestEntityPatchUpsert01Test {
    /**
     * INSERT (PATCH)
     */
    @Test
    void test01() throws Exception {
        @SuppressWarnings("unused")
        final OiyoInfo oiyoInfo = OiyokanUnittestUtil.setupUnittestDatabase();

        final int TEST_ID = OiyokanTestUtil.getNextUniqueId();

        // INSERT (PATCH)
        // 存在しないのでINSERTになるケース.
        // Decimal1,StringChar8,StringVar255
        final String key = "Decimal1=543.21,StringChar8='ZZYYXX12',StringVar255='Val" + TEST_ID + "'";
        ODataResponse resp = OiyokanTestUtil.callPatch("/ODataTest2(" + key + ")", "{\n" //
                + "  \"Name\":\"Name2\",\n" //
                + "  \"Description\":\"Description2\"\n" + "}", false, false);
        assertEquals(201, resp.getStatusCode());

        resp = OiyokanTestUtil.callGet( //
                "/ODataTest2", OiyoUrlUtil.encodeUrlQuery("$select=Decimal1,StringChar8,StringVar255"));
        @SuppressWarnings("unused")
        String result = OiyokanTestUtil.stream2String(resp.getContent());
        // System.err.println(result);

        // INSERTした後なので存在する
        resp = OiyokanTestUtil.callGet("/ODataTest2(" + key + ")", null);
        result = OiyokanTestUtil.stream2String(resp.getContent());
        // System.err.println(result);
        assertEquals(200, resp.getStatusCode());

        // DELETE
        resp = OiyokanTestUtil.callDelete("/ODataTest2(" + key + ")");
        assertEquals(204, resp.getStatusCode());

        // DELETE したあとなので存在しない.
        resp = OiyokanTestUtil.callGet("/ODataTest2(" + key + ")", null);
        assertEquals(404, resp.getStatusCode());
    }
}
