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
package jp.oiyokan.demosite;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.oiyokan.OiyokanOdata4RegisterImpl;

/**
 * Oiyokan (OData v4 server) を Spring Boot の Servlet として登録.
 * 
 * 特定のパス '/odata4.svc/' に対するリクエストを OData 処理に連携.
 */
@RestController
public class OiyokanOdata4Register {
    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(OiyokanOdata4Register.class);

    public static final String ODATA_ROOTPATH = "/odata4.svc";

    /**
     * Oiyokan (OData v4 server) を Spring Boot の Servlet として登録.
     * 
     * @param req  HTTPリクエスト.
     * @param resp HTTPレスポンス.
     * @throws ServletException サーブレット例外.
     */
    @RequestMapping(ODATA_ROOTPATH + "/*")
    public void serv(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {
        // ライブラリ側でキャッシュOFFを実施する。

        // Process Oiyokan.
        OiyokanOdata4RegisterImpl.serv(req, resp, ODATA_ROOTPATH);
    }
}