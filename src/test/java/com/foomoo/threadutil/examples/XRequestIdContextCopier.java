package com.foomoo.threadutil.examples;

import com.foomoo.threadutil.ContextCopier;

/**
 * {@link com.foomoo.threadutil.ContextCopier} for interacting with the X Request Id thread-specific storage in order to copy X Request Id values to
 * new threads.
 */
public class XRequestIdContextCopier implements ContextCopier {

    private String requestId;

    @Override
    public void copy() {
        requestId = XRequestIdStorage.getRequestId();
    }

    @Override
    public void apply() {
        XRequestIdStorage.setRequestId(requestId);
    }
}
