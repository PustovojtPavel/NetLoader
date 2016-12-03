package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.download.DownloadTask;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

@Slf4j
public class Application {

    public static void main(String[] args) {

        ApplicationSettings settings = getApplicationSettings(args);
        if (null == settings) {
            return;
        }

        ConcurrentLinkedQueue<DownloadTask> taskQueue = null;

        try {
            taskQueue = createTaskQueueFrom(
                getLinkListFrom(settings.getFileName()));
        } catch (NoSuchFileException e) {
            log.error("File {} not found", settings.getFileName());
        }
        catch (IOException e) {
            log.error("Error read file {}", settings.getFileName());
        }
    }

    private static ConcurrentLinkedQueue<DownloadTask> createTaskQueueFrom(List<String> linkList) {
        ConcurrentLinkedQueue<DownloadTask> tasks =
            new ConcurrentLinkedQueue<>();
        linkList.forEach((k)->{
            DownloadTask task  = new DownloadTask();
            task.setLink(k);
            tasks.add(task);
        });
        return tasks;
    }

    private static List<String> getLinkListFrom(String fileName) throws IOException {
        List<String> linkList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(linkList::add);
        }
        return linkList;
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
