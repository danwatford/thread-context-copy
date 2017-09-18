package com.foomoo.threadutil;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for {@link ContextCopyingThreadFactory}.
 */
public class ContextCopyingThreadFactoryTest {

    /**
     * Ensures context is applied to a new thread compared to the current test thread.
     */
    @Test
    public void contextAppliedOnDifferentThread() throws InterruptedException {

        final ThreadIdCapturingContextCopier contextCopier = new ThreadIdCapturingContextCopier();
        final ContextCopyingThreadFactory threadFactory = new ContextCopyingThreadFactory(ImmutableList.of(contextCopier));

        final Thread newThread = threadFactory.newThread(() -> {
        });

        newThread.start();

        newThread.join(1000);
        assertFalse(newThread.isAlive());

        assertNotEquals(contextCopier.copiedThreadId, contextCopier.appliedThreadId);
    }

    /**
     * Test implementation of {@link ContextCopier} to capture thread ids used in calls to copy() and apply().
     */
    private static class ThreadIdCapturingContextCopier implements ContextCopier {

        public volatile long copiedThreadId;
        public volatile long appliedThreadId;

        @Override
        public void copy() {
            copiedThreadId = Thread.currentThread()
                                   .getId();
            appliedThreadId = Thread.currentThread()
                                    .getId();
        }

        @Override
        public void apply() {
            appliedThreadId = Thread.currentThread()
                                    .getId();
        }
    }

}