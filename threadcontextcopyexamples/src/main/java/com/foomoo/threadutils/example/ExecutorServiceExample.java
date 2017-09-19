package com.foomoo.threadutils.example;

import com.foomoo.threadutil.ContextCopier;
import com.foomoo.threadutil.ContextCopyingThreadFactory;
import com.google.common.collect.ImmutableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ExecutorServiceExample {

    private static final String KEY = "requestId";

    /**
     * Example use case of the {@link ContextCopyingThreadFactory} where the RequestId for a server processed request is stored in thread-specific
     * storage and copied to other threads.
     * <p>
     * In this example the original request id is set and then the executor is created, making use of the {@link ContextCopyingThreadFactory} itself
     * configured with an instance of {@link XRequestIdContextCopier}. The {@link Callable}s submitted to the executor then log output to demonstrate
     * the RequestId values held in their thread specific storage.
     *
     * @throws InterruptedException Not expected to be thrown.
     */
    public static void main(final String args[]) throws InterruptedException {

        setRequestId("000");

        final Logger logger = LogManager.getLogger();
        logger.info("Example start");

        final ContextCopyingThreadFactory threadFactory = new ContextCopyingThreadFactory(ImmutableList.of(new XRequestIdContextCopier()));
        final ExecutorService executorService = Executors.newFixedThreadPool(5, threadFactory);

        IntStream.rangeClosed(1, 20)
                 .forEach(taskId -> executorService.submit(getRunnable(taskId, logger)));

        logger.info("All tasks submitted");

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }

    private static Callable<Void> getRunnable(final int taskId, final Logger logger) {
        return () -> {
            final String padding = String.join("", Collections.nCopies(taskId, "  "));
            final String message = String.format("%s%02d", padding, taskId);
            logger.info(message);
            Thread.sleep(200);
            logger.info(message);
            return null;
        };
    }

    static void setRequestId(final String requestId) {
        ThreadContext.put(KEY, requestId);
    }

    static String getRequestId() {
        return ThreadContext.get(KEY);
    }

    /**
     * {@link com.foomoo.threadutil.ContextCopier} for interacting with the X Request Id thread-specific storage in order to copy X Request Id values to
     * new threads. Applies a suffix to the requestId for each thread created.
     */
    private static class XRequestIdContextCopier implements ContextCopier {

        private String requestId;
        private AtomicInteger applyCount = new AtomicInteger();

        @Override
        public void copy() {
            requestId = getRequestId();
        }

        @Override
        public void apply() {
            setRequestId(String.format("%s-%02d", requestId, applyCount.getAndIncrement()));
        }
    }
}
