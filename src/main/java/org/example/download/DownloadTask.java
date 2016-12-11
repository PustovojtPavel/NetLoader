package org.example.download;


import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.concurrent.Callable;

@Slf4j
public class DownloadTask implements Callable<Object> {

    private URI uri;

    public DownloadTask(URI uri) {
        this.uri = uri;
    }

    @Override
    public Object call() throws Exception {
        String currentThreadName =
            Thread.currentThread().getName();
        log.debug("Start Download");
        return null;
    }
}
