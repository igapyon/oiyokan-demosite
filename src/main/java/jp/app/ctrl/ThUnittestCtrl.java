package jp.app.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.oiyokan.OiyokanConstants;
import jp.oiyokan.demosite.OiyokanOdata4Register;

@Controller
public class ThUnittestCtrl {
    public static final String[][] ODATA_ENTRY_INFOS = new String[][] { //
            { "root", "/", "Root of OData v4 server." },
            { "version", "/Oiyokan?$format=JSON", "Show Oiyokan settings and version." },
            { "$metadata", "/$metadata", "Metadata of this OData." },
            { "ODataTest1 : $orderby", "/ODataTest1?$orderby=ID&$top=20&$count=true",
                    "Sample of sort using $orderby." },
            { "ODataTest1 : $filter",
                    "/ODataTest1?$top=2001&$filter=Description eq 'MacBook Pro (13-inch, 2020, Thunderbolt 3ポートx 4)' and ID eq 1.0&$count=true&$select=ID,Name",
                    "Sample of matching using $filter." },
            { "ODataTest1 : $search (Experimental)", "/ODataTestFulls1?$top=6&$search=macbook&$count=true&$select=ID",
                    "Full text search test (Experimental)." }, };

    @RequestMapping("/unittest.html")
    public String oiyokanUnittest(Model model) throws IOException {
        model.addAttribute("odataRootpath", OiyokanOdata4Register.ODATA_ROOTPATH);

        final List<UrlEntryBean> urlEntryList = new ArrayList<>();
        model.addAttribute("UrlEntryList", urlEntryList);

        for (String[] look : ODATA_ENTRY_INFOS) {
            if (look[0].indexOf("$search") >= 0) {
                if (!OiyokanConstants.IS_EXPERIMENTAL_SEARCH_ENABLED) {
                    // 実験的 $search について無効化されているためテスト対象から除外.
                    continue;
                }
            }

            urlEntryList.add(new UrlEntryBean(look[0], look[1], look[2]));
        }

        return "unittest";
    }
}
