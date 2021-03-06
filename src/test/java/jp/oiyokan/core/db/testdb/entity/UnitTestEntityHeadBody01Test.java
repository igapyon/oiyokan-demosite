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

import jp.oiyokan.OiyokanUnittestUtil;
import jp.oiyokan.common.OiyoInfo;
import jp.oiyokan.util.OiyokanTestUtil;

/**
 * EntityのHEAD, BODYのテスト.
 */
class UnitTestEntityHeadBody01Test {
    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(UnitTestEntityHeadBody01Test.class);

    /**
     * CREATE + DELETE
     */
    @Test
    void test01() throws Exception {
        @SuppressWarnings("unused")
        final OiyoInfo oiyoInfo = OiyokanUnittestUtil.setupUnittestDatabase();

        ODataResponse resp = OiyokanTestUtil.callPost("/ODataTest8", "{\n" //
                + "  \"MainKey\":\"KEY001\"\n" //
                + "}");
        // TODO 戻りに KEY指定がない..
        String result = OiyokanTestUtil.stream2String(resp.getContent());
        assertEquals(
                "{\"@odata.context\":\"$metadata#ODataTest8\",\"MainKey\":\"KEY001\",\"Description\":\"Main table\"}",
                result);
        // log.debug("TRACE: " + result);
        assertEquals(201, resp.getStatusCode(), "");

        resp = OiyokanTestUtil.callGet("/ODataTest8('KEY001')", null);
        result = OiyokanTestUtil.stream2String(resp.getContent());
        assertEquals(
                "{\"@odata.context\":\"$metadata#ODataTest8\",\"MainKey\":\"KEY001\",\"Description\":\"Main table\"}",
                result);
        // log.debug("TRACE: " + result);
        assertEquals(200, resp.getStatusCode());

        /// 通常のfilter
        resp = OiyokanTestUtil.callGet("/ODataTest8", "$filter=MainKey eq 'KEY001'");
        result = OiyokanTestUtil.stream2String(resp.getContent());
        assertEquals(
                "{\"@odata.context\":\"$metadata#ODataTest8\",\"value\":[{\"MainKey\":\"KEY001\",\"Description\":\"Main table\"}]}",
                result);
        // log.debug("TRACE: " + result);
        assertEquals(200, resp.getStatusCode());

        ////////
        // SUB

        resp = OiyokanTestUtil.callPost("/ODataTest8Sub", "{\n" //
                + "  \"MainKey\":\"KEY001\"\n" //
                + "  , \"SubKey\":\"SUB001\"\n" //
                + "}");
        result = OiyokanTestUtil.stream2String(resp.getContent());
        assertEquals(
                "{\"@odata.context\":\"$metadata#ODataTest8Sub\",\"MainKey\":\"KEY001\",\"SubKey\":\"SUB001\",\"Description\":\"Sub table\"}",
                result);
        // log.debug("TRACE: " + result);
        assertEquals(201, resp.getStatusCode(), "");

        ////////
        // SUBSUB

        resp = OiyokanTestUtil.callPost("/ODataTest8SubSub", "{\n" //
                + "  \"MainKey\":\"KEY001\"\n" //
                + "  , \"SubKey\":\"SUB001\"\n" //
                + "  , \"SubSubKey\":\"SUBSUB001\"\n" //
                + "}");
        result = OiyokanTestUtil.stream2String(resp.getContent());
        assertEquals(
                "{\"@odata.context\":\"$metadata#ODataTest8SubSub\",\"MainKey\":\"KEY001\",\"SubKey\":\"SUB001\",\"SubSubKey\":\"SUBSUB001\",\"Description\":\"Sub sub table\"}",
                result);
        // log.debug("TRACE: " + result);
        assertEquals(201, resp.getStatusCode(), "");

        ///////////////////
        // DELETE

        // DELETE
        resp = OiyokanTestUtil.callDelete("/ODataTest8SubSub(MainKey='KEY001',SubKey='SUB001',SubSubKey='SUBSUB001')");
        assertEquals(204, resp.getStatusCode());

        // DELETE
        resp = OiyokanTestUtil.callDelete("/ODataTest8Sub(MainKey='KEY001',SubKey='SUB001')");
        assertEquals(204, resp.getStatusCode());

        // DELETE
        resp = OiyokanTestUtil.callDelete("/ODataTest8('KEY001')");
        assertEquals(204, resp.getStatusCode());

        // NOT FOUND after DELETED
        resp = OiyokanTestUtil.callGet("/ODataTest8('KEY001')", null);
        assertEquals(404, resp.getStatusCode());
    }
}
