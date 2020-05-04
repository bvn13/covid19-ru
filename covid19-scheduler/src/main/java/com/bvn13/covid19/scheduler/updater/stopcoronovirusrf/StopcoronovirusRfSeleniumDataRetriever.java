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
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Component
public class StopcoronovirusRfSeleniumDataRetriever implements StopcoronovirusRfDataRetriever {

    //private static final String URL = "https://стопкоронавирус.рф/";
    private static final String HOST = "xn--80aesfpebagmfblc0a.xn--p1ai";
    private static final String URL = "https://xn--80aesfpebagmfblc0a.xn--p1ai/information/";

    @Value("${app.user-agent}")
    private String userAgent;

    @Handler
    public void retrieveData(Exchange exchange) throws Exception {
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(firefoxBinary);
        options.setHeadless(true);  // <-- headless set here
        WebDriver driver = new FirefoxDriver(options);
        try {
            driver.manage().window().maximize();
            driver.get(URL);
            List<WebElement> tableData = driver.findElements(By.cssSelector(".d-map__list > table > tbody > tr"));

            if (tableData.size() <= 0) {
                throw new IllegalStateException("Data not found!");
            }

            WebElement lastRow = driver.findElement(By.cssSelector(".d-map__list > table > tbody > tr:last-child"));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,10000)");
            js.executeScript("arguments[0].scrollIntoView()",lastRow);
//            js.executeScript("$(\".d-map__list > table > tbody\").animate({ scrollTop: \"10000px\" });");

            List<RowData> rows = new ArrayList<>(tableData.size());

            for (WebElement row : tableData) {
                RowData rowData = RowData.builder()
                        .region(row.findElement(By.cssSelector("th")).getAttribute("innerText"))
                        .sick(row.findElement(By.cssSelector("td.col-sick")).getAttribute("innerText"))
                        .healed(row.findElement(By.cssSelector("td.col-healed")).getAttribute("innerText"))
                        .died(row.findElement(By.cssSelector("td.col-died")).getAttribute("innerText"))
                        .build();
                Assert.isTrue(StringUtils.isNotBlank(rowData.getRegion()), "Broken data found after " + rows.size() + " rows");
            }

            exchange.getIn().setHeader(StopcoronovirusRfUpdater.HEADER_DATE_OF_DATA, driver.findElement(By.cssSelector(".cv-section__title small")).getText());
            exchange.getIn().setBody(rows);

        } finally {
            driver.quit();
        }
    }

}
