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

import com.bvn13.covid19.model.entities.CovidStat;
import com.bvn13.covid19.model.entities.Region;
import com.bvn13.covid19.model.entities.CovidUpdateInfo;
import com.bvn13.covid19.scheduler.CovidStatsRepository;
import com.bvn13.covid19.scheduler.RegionsRepository;
import com.bvn13.covid19.scheduler.CovidUpdateInfosRepository;
import com.bvn13.covid19.scheduler.updater.stopcoronovirusrf.model.RowData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class StopcoronovirusRfService {

    private static final Pattern PATTERN_DATE = Pattern.compile("[а-яА-Я\\s]*(\\d{1,2})\\s([а-яА-Я]+)\\s(\\d{1,2})\\:(\\d{1,2})", Pattern.CASE_INSENSITIVE);

    private final CovidStatsRepository repository;
    private final RegionsRepository regionsRepository;
    private final CovidUpdateInfosRepository updatesRepository;

    @Value("${app.zone-id}")
    private String zoneIdStr;
    private ZoneId zoneId;

    @PostConstruct
    public void init() {
        zoneId = ZoneId.of(zoneIdStr);
    }

    @Transactional
    public void saveRawData(String date, Collection<RowData> rawData) {

        ZonedDateTime zdt = parseZonedDateTime(date);

        CovidUpdateInfo updateInfo = new CovidUpdateInfo();
        updateInfo.setCreatedOn(LocalDateTime.now());
        updateInfo.setDatetime(zdt);
        updateInfo = updatesRepository.save(updateInfo);

        Iterator<RowData> iter = rawData.iterator();
        while (iter.hasNext())  {
            RowData row = iter.next();

            Region region = regionsRepository.findByName(row.getRegion()).orElseGet(() -> {
                Region r = new Region();
                r.setName(row.getRegion());
                return regionsRepository.save(r);
            });

            CovidStat covidStat = new CovidStat();
            covidStat.setUpdateInfo(updateInfo);
            covidStat.setRegion(region);
            covidStat.setSick(Long.parseLong(row.getSick()));
            covidStat.setHealed(Long.parseLong(row.getHealed()));
            covidStat.setDied(Long.parseLong(row.getDied()));

            repository.save(covidStat);
        }

    }

    private ZonedDateTime parseZonedDateTime(String date) {
        Matcher matcher = PATTERN_DATE.matcher(date);
        if (matcher.find()) {
            return ZonedDateTime.of(
                    2020,
                    detectMonth(matcher.group(2)),
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4)),
                    0, 0, zoneId
            );
        } else {
            throw new IllegalArgumentException("Could not parse date: "+date);
        }
    }

    private int detectMonth(String m) {
        switch (m.trim().toLowerCase()) {
            case "январь":
            case "января":
                return 1;
            case "февраль":
            case "февраля":
                return 2;
            case "март":
            case "марта":
                return 3;
            case "апрель":
            case "апреля":
                return 4;
            case "май":
            case "мая":
                return 5;
            case "июнь":
            case "июня":
                return 6;
            case "июль":
            case "июля":
                return 7;
            case "август":
            case "августа":
                return 8;
            case "сентябрь":
            case "сентября":
                return 9;
            case "октябрь":
            case "октября":
                return 10;
            case "ноябрь":
            case "ноября":
                return 11;
            case "декабрь":
            case "декабря":
                return 12;
            default: throw new IllegalArgumentException("Could not detect month: "+m);
        }
    }

}
