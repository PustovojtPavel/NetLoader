package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.download.DownloadTask;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

@Slf4j
public class Application {

    public static void main(String[] args) {

        ApplicationSettings settings = getApplicationSettings(args);
        if (null == settings) {
            return;
        }

        List<URI> downloadList = null;
        try {
            downloadList = createURIListFrom(settings.getFileName());
        } catch (NoSuchFileException e) {
            log.info("File {} not found", settings.getFileName());
        } catch (IOException e) {
            log.info("Error read file {}", settings.getFileName());
        }

        if (null == downloadList) {
            return;
        }

        downloadAllAsync(downloadList,
            getDownloadDirNameOrDefaultIfNull(
                settings.getDownloadDir()),
            settings.getThreadCount());
    }

    private static void downloadAllAsync(List<URI> uriList,
                                         String downloadPath,
                                         Integer threadCount) {

        List<Callable<Object>> tasks = new ArrayList<>(uriList.size());
        try (Stream<URI> stream = uriList.stream()) {
            stream.forEach((k)->{
                tasks.add(new DownloadTask(k));
            });
        }

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        List<Future<Object>> invokeAll = null;
        try {
            invokeAll = pool.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    private static String getDownloadDirNameOrDefaultIfNull(String downloadDir) {
        if (null != downloadDir)
            return downloadDir;
        return getTempDirectory();
    }

    private static String getTempDirectory() {
        return "c:/tmp";
    }

    private static List<URI> createURIListFrom(String fileName) throws IOException {
        List<URI> taskList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach((k) -> {
                taskList.add(URI.create(k));
            });
        }
        return taskList;
    }

    private static ApplicationSettings getApplicationSettings(String[] args) {

        ApplicationSettings settings = new ApplicationSettings();
        CmdLineParser parser = new CmdLineParser(settings);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return null;
        }
        return settings;
    }
}
