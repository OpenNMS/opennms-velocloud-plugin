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

package org.opennms.velocloud.cache;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;

import org.opennms.velocloud.client.api.VelocloudApiClientCredentials;
import org.opennms.velocloud.client.api.VelocloudApiException;

/**
 * Class to retrieve results of a function and cache it for defined time
 * @param <T> the type of the input to the function that should be cached
 * @param <C> the type of the result of the function that should be cached
 */
public class Cache<T, C> {
    private static class Element<R> {
        long timestamp;
        Optional<R> result;
        public Element(long timestamp, Optional<R> result) {
            this.timestamp = timestamp;
            this.result = result;
        }
    }

    private static class Key<T> {
        VelocloudApiClientCredentials credentials;
        T parameter;
        public Key(VelocloudApiClientCredentials credentials, T parameter) {
            this.credentials = requireNonNull(credentials);
            this.parameter = requireNonNull(parameter);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key<?> key = (Key<?>) o;
            return credentials.equals(key.credentials) && parameter.equals(key.parameter);
        }

        @Override
        public int hashCode() {
            return Objects.hash(credentials, parameter);
        }
    }

    // time to cache in milliseconds
    private long maxTimeInMillsToCache;

    private HashMap<Key<T>, Element<C>> cacheMap = new HashMap<>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private final CacheableFunction<T, C> function;

    public Cache(CacheableFunction<T, C> function, int maxTimeInSecondsToCache) {
        Objects.requireNonNull(function);
        this.function = function;
        this.maxTimeInMillsToCache = maxTimeInSecondsToCache * 1000;
    }

    public C doCall(VelocloudApiClientCredentials credentials, T parameter) throws VelocloudApiException {
        Objects.requireNonNull(credentials);
        Objects.requireNonNull(parameter);
        final long time = System.currentTimeMillis();
        final long oldestOk = time - maxTimeInMillsToCache;
        lock.readLock().lock();
        boolean writeLocked = false;
        try {
            final Element<C> element = cacheMap.get(parameter);
            if (element != null) {
                if (element.timestamp > oldestOk) {
                    return element.result.orElse(null);
                }
            }
            lock.readLock().unlock();
            lock.writeLock().lock();
            writeLocked = true;
            C result = function.apply(credentials, parameter);
            cacheMap.put(new Key<>(credentials, parameter), new Element<>(time, Optional.ofNullable(result)));
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
        this.maxTimeInMillsToCache = maxTimeInSecondsToCache * 1000;
        removeExpired();
    }

    public void removeExpired() {
        lock.writeLock().lock();
        final long oldestOk = System.currentTimeMillis() - maxTimeInMillsToCache;
        try {
            final Iterator<Map.Entry<Key<T>, Element<C>>> iterator = cacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<Key<T>, Element<C>> next = iterator.next();
                if (next.getValue().timestamp < oldestOk) {
                    iterator.remove();
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
