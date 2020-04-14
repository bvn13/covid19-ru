/*
Copyright [2020] [bvn13]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.bvn13.covid19.scheduler.updater.stopcoronovirusrf;

import com.bvn13.covid19.scheduler.updater.stopcoronovirusrf.model.RowData;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StopcoronovirusRfDataRetriever {

    //private static final String URL = "https://стопкоронавирус.рф/";
    private static final String HOST = "xn--80aesfpebagmfblc0a.xn--p1ai";
    private static final String URL = "https://xn--80aesfpebagmfblc0a.xn--p1ai/";

    @Value("${app.user-agent}")
    private String userAgent;

    @Handler
    public void retrieveData(Exchange exchange) throws Exception {
        Document doc = Jsoup.connect(URL)
                .userAgent(userAgent)
                .timeout(30*1000)
                //.referrer("http://google.com")
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//                .header("Accept-Encoding", "gzip, deflate, br")
//                .header("Accept-Language", "ru-RU,ru;q=0.5")
//                .header("Cache-Control", "no-cache")
//                .header("Connection", "keep-alive")
//                .header("Pragma", "no-cache")
//                .header("Host", HOST)
                .get();

        Elements tableData = doc.select("div.d-map__list > table > tbody > tr");

        List<RowData> rows = new ArrayList<>(tableData.size());

        for (Element row : tableData) {
            rows.add(RowData.builder()
                    .region(row.selectFirst("th").text())
                    .sick(row.selectFirst("td > span.d-map__indicator_sick").parent().ownText())
                    .healed(row.selectFirst("td > span.d-map__indicator_healed").parent().ownText())
                    .died(row.selectFirst("td > span.d-map__indicator_die").parent().ownText())
                    .build());
        }

        exchange.getIn().setHeader(StopcoronovirusRfUpdater.HEADER_DATE_OF_DATA, doc.selectFirst(".d-map__title span").ownText());
        exchange.getIn().setBody(rows);

    }

}
