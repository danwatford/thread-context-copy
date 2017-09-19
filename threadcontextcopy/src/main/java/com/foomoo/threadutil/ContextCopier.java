package com.foomoo.threadutil;

/**
 * A {@link ContextCopier} is responsible for capturing the context from one thread, storing it and applying it to another thread.
 * <p>
 * Although termed a 'copier', implementations are free to either copy or reference data across threads as appropriate to the nature of the
 * information being handled.
 * <p>
 * {@link ContextCopier}'s are intended to have the copy() method called once for a 'parent' thread, and the apply() method called multiple times
 * for all threads that context should be copied to.
 */
public interface ContextCopier {

    /**
     * Captures context from the current thread.
     */
    void copy();

    /**
     * Applies the captured context to the current thread.
     */
    void apply();
}
