package org.example.download;

import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

        URL url = uri.toURL();
        Instant start = Instant.now();

        log.info("[{}] Start download", url);

        try (ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                FileOutputStream fos = new FileOutputStream(generateFileName())) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Throwable t) {
            log.info("Error occur", t);
        }

        log.info("[{}] Finish download, elapsed time: {}", url,
            localTimeToString(
                getElapsedLocalTime(start, Instant.now())));
        return null;
    }

    private String localTimeToString(LocalTime time) {
        DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_TIME;
        return time.format(df);
    }

    private LocalTime getElapsedLocalTime(Instant start, Instant end) {
        return LocalTime.ofNanoOfDay(
            Duration.between(start, end).getNano());
    }

    private String generateFileName() {
        if (downloadDirectoryName.endsWith("/")) {
            return downloadDirectoryName + UUID.randomUUID().toString();
        } else {
            return downloadDirectoryName + "/" + UUID.randomUUID().toString();
        }
    }
}
