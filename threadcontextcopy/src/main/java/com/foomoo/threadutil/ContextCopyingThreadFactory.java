package com.foomoo.threadutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * A {@link java.util.concurrent.ThreadFactory} that copies thread specific ({@link java.lang.ThreadLocal} data between threads. This is useful to
 * copy context across threads for use in a {@link java.util.concurrent.ThreadPoolExecutor}.
 */
public class ContextCopyingThreadFactory implements ThreadFactory {

    private final ThreadFactory threadFactory = Executors.defaultThreadFactory();

    private final List<ContextCopier> contextCopiers;

    /**
     * Construct the {@link ContextCopyingThreadFactory} with the given {@link java.util.Collection} of {@link ContextCopier}s. If the
     * {@link Collection} is ordered, the contexts will be copied in the same order when applied to a new {@link Thread}.
     *
     * @param contextCopiers The {@link ContextCopier}s to apply to new Threads.
     */
    public ContextCopyingThreadFactory(final Collection<ContextCopier> contextCopiers) {
        this.contextCopiers = new ArrayList<>(contextCopiers);
        this.contextCopiers.forEach(ContextCopier::copy);
    }

    public Thread newThread(final Runnable r) {
        return threadFactory.newThread(makeRunnableContextCopying(r));
    }

    /**
     * Takes the given {@link Runnable} and wrap it to execute the registered context-copying operations before the {@link Runnable}'s own operations.
     *
     * @param r The {@link Runnable} to wrap.
     * @return The new {@link Runnable}.
     */
    private Runnable makeRunnableContextCopying(final Runnable r) {

        return () -> {
            contextCopiers.forEach(ContextCopier::apply);
            r.run();
        };
    }

}
