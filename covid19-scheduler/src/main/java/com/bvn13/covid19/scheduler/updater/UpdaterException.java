package com.bvn13.covid19.scheduler.updater;

import lombok.Getter;

public class UpdaterException extends RuntimeException {

    @Getter
    private String body;

    public UpdaterException(String message, String body) {
        super(message);
        this.body = body;
    }

}
