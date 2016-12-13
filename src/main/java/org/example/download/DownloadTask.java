package org.example.download;

import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.UUID;
import java.util.concurrent.Callable;

@Slf4j
public class DownloadTask implements Callable<Object> {

    private URI uri;
    private String downloadDirectoryName;

    public DownloadTask(URI uri, String downloadDirectoryName) {
        this.uri = uri;
        this.downloadDirectoryName = downloadDirectoryName;
    }

    @Override
    public Object call() throws Exception {
        log.info("Start download uri={} ", uri);
        try (ReadableByteChannel rbc = Channels.newChannel(uri.toURL().openStream());
                FileOutputStream fos = new FileOutputStream(generateFileName())) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Throwable t) {
            log.info("Error occur", t);
        }
        log.info("Finish download");
        return null;
    }

    private String generateFileName() {
        if (downloadDirectoryName.endsWith("/")) {
            return downloadDirectoryName + UUID.randomUUID().toString();
        } else {
            return downloadDirectoryName + "/" + UUID.randomUUID().toString();
        }
    }
}
