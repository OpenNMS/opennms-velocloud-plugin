/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.velocloud.client.cache;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class for caching results of a particular function with particular parameters for specified time, or retrieve those
 * if nor existing or expired and to (re)cache
 * @param <A> (A)API Class to call its method
 * @param <P> type of (P)parameter for API method: A.method(P param)
 * @param <C> Typ of the (C)cacheable result of API call
 */
public class Cache<A, P, C, E extends Exception> {
    private static class Element<C> {
        final long timestamp;
        final C result;
        final Exception exception;
        public Element(long timestamp, C result, Exception exception) {
            this.timestamp = timestamp;
            this.result = result;
            this.exception = exception;
        }
    }

    // time to cache in milliseconds
    private long maxTimeInMillsToCache;

    private final HashMap<Object, Element<C>> cacheMap = new HashMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final ApiCall<A, P, C, E> apiCall;

    public Cache(ApiCall<A, P, C, E> apiCall, int maxTimeInSecondsToCache) {
        Objects.requireNonNull(apiCall);
        this.apiCall = apiCall;
        this.maxTimeInMillsToCache = maxTimeInSecondsToCache * 1000L;
    }

    /**
     * Checks if there is a non expired call result in cache. If so, the cached result is returned.
     * Otherwise, the call is performed, its result is cached and returned
     *
     * @param api object to perform a call on it
     * @param parameter parameter for the call
     * @param key the key that identifies cacheable call
     * @return cached result if there is a non expired one or call result otherwise
     * @throws E rethrows the exception if there was an exception while performing call or if there was a non expired
     *         API-Call, and there was also an exception thrown
     */
    public C doCall(A api, P parameter, Object key) throws E {
        Objects.requireNonNull(api);
        Objects.requireNonNull(parameter);
        Objects.requireNonNull(key);

        final long time = System.currentTimeMillis();
        final long oldestOk = time - maxTimeInMillsToCache;
        lock.readLock().lock();
        boolean writeLocked = false;
        try {
            final Element<C> element = cacheMap.get(key);
            if (element != null) {
                if (element.exception != null) {
                    if (element.exception instanceof RuntimeException) {
                        throw (RuntimeException) element.exception;
                    }
                    throw (E) element.exception;
                }
                if (element.timestamp > oldestOk) {
                    return element.result;
                }
            }
            lock.readLock().unlock();
            lock.writeLock().lock();
            writeLocked = true;
            C result = null;
            Exception e = null;
            try {
                result = apiCall.doCall(api, parameter);
            } catch (Exception ex) {
                e = ex;
            }
            cacheMap.put(key, new Element<>(time, result, e));
            if (e != null) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw (E) e;
            }
            return result;
        } finally {
            if (writeLocked) {
                lock.writeLock().unlock();
            } else {
                lock.readLock().unlock();
            }
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            cacheMap.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setTime(int maxTimeInSecondsToCache) {
        this.maxTimeInMillsToCache = maxTimeInSecondsToCache * 1000L;
        removeExpired();
    }

    public void removeExpired() {
        lock.writeLock().lock();
        final long oldestOk = System.currentTimeMillis() - maxTimeInMillsToCache;
        try {
            cacheMap.entrySet().removeIf(entry -> entry.getValue().timestamp < oldestOk);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
