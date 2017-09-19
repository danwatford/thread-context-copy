package com.foomoo.threadutils.example;

import org.apache.logging.log4j.ThreadContext;

/**
 * ThreadLocal storage of the X-Request-ID value.
 * <p>
 * The X-Request-ID is normally unique to any particular request serviced by a server application. In many servers it is likely that a single thread
 * is responsible for servicing the request, therefore it is appropriate to store information scoped to the request in ThreadLocal storage.
 * <p>
 * X-Request-ID values are useful for tracing processing in response to a particular request through a system, therefore it will be stored in the
 * Log4J ThreadContext (similar to a ThreadLocal variable) so it can be included in all logging.
 */
public class XRequestIdStorage {

    public static final String KEY = "requestId";

    public static void setRequestId(final String requestId) {
        ThreadContext.put(KEY, requestId);
    }

    public static String getRequestId() {
        return ThreadContext.get(KEY);
    }
}
