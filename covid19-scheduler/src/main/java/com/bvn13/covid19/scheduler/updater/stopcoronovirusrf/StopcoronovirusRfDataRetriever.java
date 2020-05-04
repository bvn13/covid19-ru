package com.bvn13.covid19.scheduler.updater.stopcoronovirusrf;

import org.apache.camel.Exchange;

public interface StopcoronovirusRfDataRetriever {
    void retrieveData(Exchange exchange) throws Exception;
}
