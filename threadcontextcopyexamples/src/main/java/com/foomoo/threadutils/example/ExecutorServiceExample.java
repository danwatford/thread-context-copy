package com.foomoo.threadutils.example;

import com.foomoo.threadutil.ContextCopyingThreadFactory;
import com.google.common.collect.ImmutableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceExample {

    /**
     * Example use case of the {@link ContextCopyingThreadFactory} where the RequestId for a server processed request is stored in thread-specific
     * storage and copied to other threads.
     * <p>
     * In this example the original request id is set, the executor created and then the request id changed. The {@link Runnable} submitted to the
     * executor then log output to demonstrate the RequestId values held in their thread specific storage.
     *
     * @throws InterruptedException Not expected to be thrown.
     */
    public static void main(final String args[]) throws InterruptedException {

        XRequestIdStorage.setRequestId("OriginalRequestId");

        final Logger logger = LogManager.getLogger();
        logger.info("Example start");

        final ContextCopyingThreadFactory threadFactory = new ContextCopyingThreadFactory(ImmutableList.of(new XRequestIdContextCopier()));
        final ExecutorService executorService = Executors.newFixedThreadPool(5, threadFactory);

        XRequestIdStorage.setRequestId("NewRequestId");

        executorService.submit(getRunnable(1, logger));
        executorService.submit(getRunnable(2, logger));
        executorService.submit(getRunnable(3, logger));

        logger.info("All tasks submitted");

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }

    private static Runnable getRunnable(final int taskId, final Logger logger) {
        return () -> {
            logger.info("Task {} before sleep", taskId);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("Task {} after sleep", taskId);
        };
    }
}
