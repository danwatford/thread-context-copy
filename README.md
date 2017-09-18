[![Build Status](https://travis-ci.org/danwatford/thread-context-copy.svg?branch=master)](https://travis-ci.org/danwatford/thread-context-copy)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Thread Context Copy

A method of copying context to threads created by a ThreadFactory for use by an ExecutorService.

Sometimes it is necessary to copy context in the form of ThreadLocal storage across the threads used by an ExecutorService.
An example could be when managing cookies to be attached to multiple concurrent calls to a webservice.

At the point where the concurrent tasks should be executed, construct an ExecutorService which uses the
ContextCopyingThreadFactory. This ThreadFactory is itself constructed with a Collection of ContextCopier implementations.
The ContextCopiers are responsible for copying or capturing the contextual information on the initial thread
and applying the contextual information onto the new threads created by the ThreadFactory.



