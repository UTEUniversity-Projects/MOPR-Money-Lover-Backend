package com.mobile.api.listener;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

@Component
public class LogFileCleanupListener implements ApplicationListener<ApplicationStartingEvent> {
    private static final Logger LOGGER = Logger.getLogger(LogFileCleanupListener.class.getName());
    private static final String LOG_DIR = "./logs";
    private static final String LOG_EXTENSION = ".log";
    private static final int MAX_LOG_FILES = 10;

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        File logDir = new File(LOG_DIR);

        // Ensure log directory exists
        if (!logDir.exists() || !logDir.isDirectory()) {
            return;
        }

        // Get all log files
        File[] logFiles = logDir.listFiles((dir, name) -> name.endsWith(LOG_EXTENSION));

        if (logFiles == null || logFiles.length <= MAX_LOG_FILES) {
            return; // No need to delete logs
        }

        // Sort log files by last modified time (oldest first)
        Arrays.sort(logFiles, Comparator.comparingLong(File::lastModified));

        // Delete the oldest files, keeping only the latest 10
        for (int i = 0; i < logFiles.length - MAX_LOG_FILES; i++) {
            if (logFiles[i].delete()) {
                LOGGER.info("Deleted old log file: " + logFiles[i].getName());
            } else {
                LOGGER.warning("Failed to delete log file: " + logFiles[i].getName());
            }
        }
    }
}
