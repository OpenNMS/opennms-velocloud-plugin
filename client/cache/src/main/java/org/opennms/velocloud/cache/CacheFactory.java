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
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

public class CacheFactory {

    private static int defaultTimeInSeconds = 5;

    public static int getDefaultTimeInSeconds() {
        return defaultTimeInSeconds;
    }

    public static void setDefaultTimeInSeconds(final int defaultTimeInSeconds) {
        CacheFactory.defaultTimeInSeconds = defaultTimeInSeconds;
    }

    private static ReentrantLock lock = new ReentrantLock();

    private static final Map<CacheableFunction<?, ?>,  Cache<?, ?>> caches = new HashMap<>();

    @Nonnull
    public static <API, T, C, E extends Exception> Cache<T, C> getCache(final CacheableFunction<T, C> function) {
        requireNonNull(function);
        lock.lock();
        try {
            Cache<?, ?> cache = caches.get(function);
            if (cache != null) {
                return (Cache<T, C>) cache;
            }
            Cache<T, C> newCache = new Cache<>(function, defaultTimeInSeconds);
            caches.put(function, newCache);
            return newCache;
        } finally {
            lock.unlock();
        }
    }

    public static void forEachCache(final Consumer<Cache<?, ?>> action) {
        lock.lock();
        try {
            caches.forEach((key, value) -> action.accept(value));
        } finally {
            lock.unlock();
        }
    }

    public static void setTimeForAllCaches(final int seconds) {
        forEachCache(cache -> cache.setTime(seconds));
    }

    public static void clear() {
        lock.lock();
        try {
            final Iterator<Map.Entry<CacheableFunction<?, ?>, Cache<?, ?>>> iterator = caches.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<?, Cache<?, ?>> entry = iterator.next();
                entry.getValue().clear();
                iterator.remove();
            }
        } finally {
            lock.unlock();
        }
    }
}
