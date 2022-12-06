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

package org.opennms.velocloud.client.api.model;

import java.math.BigDecimal;

import com.google.common.base.MoreObjects;

public class Score {
    private final BigDecimal audio;
    private final BigDecimal video;
    private final BigDecimal transactional;

    private Score(BigDecimal audio, BigDecimal video, BigDecimal transactional) {
        this.audio = audio;
        this.video = video;
        this.transactional = transactional;
    }

    public static class Builder {
        BigDecimal audio = new BigDecimal(0);
        BigDecimal video = new BigDecimal(0);
        BigDecimal transactional = new BigDecimal(0);

        public Score.Builder withAudio(BigDecimal audio) {
            this.audio = audio;
            return this;
        }

        public Score.Builder withVideo(BigDecimal video) {
            this.video = video;
            return this;
        }

        public Score.Builder withTransactional(BigDecimal transactional) {
            this.transactional = transactional;
            return this;
        }

        public Score build() {
            return new Score(audio, video, transactional);
        }
    }

    public BigDecimal getAudio() {
        return audio;
    }

    public BigDecimal getVideo() {
        return video;
    }

    public BigDecimal getTransactional() {
        return transactional;
    }

    public static Score.Builder builder() {
        return new Score.Builder();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("audio", audio)
                .add("video", video)
                .add("transactional", transactional)
                .toString();
    }
}
