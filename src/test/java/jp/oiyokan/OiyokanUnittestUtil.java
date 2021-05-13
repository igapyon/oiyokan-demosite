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
package jp.oiyokan;

import org.apache.olingo.server.api.ODataApplicationException;

import jp.oiyokan.common.OiyoInfo;

public class OiyokanUnittestUtil {
    public static OiyoInfo setupUnittestDatabase() throws ODataApplicationException {
        final OiyoInfo oiyoInfo = OiyokanEdmProvider.getOiyoInfoInstance();
        // 取得した OiyoInfo をそのまま返却.
        return oiyoInfo;
    }
}
