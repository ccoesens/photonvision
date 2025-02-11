/*
 * Copyright (C) Photon Vision.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.photonvision.vision.pipe.impl;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.photonvision.vision.pipe.MutatingPipe;

/** Represents a pipeline that blurs the image. */
public class CannyPipe extends MutatingPipe<Mat, CannyPipe.CannyParams> {
    /**
     * Processes this pipe.
     *
     * @param in Input for pipe processing.
     * @return The processed frame.
     */
    @Override
    protected Void process(Mat in) {
        Imgproc.Canny(in, in, params.m_low, params.m_high);
        return null;
    }

    public static class CannyParams {
        // Default BlurImagePrams with zero blur.
        public static CannyParams DEFAULT = new CannyParams(50, 150);

        // Member to store the blur size.
        private final int m_low;
        private final int m_high;

        /**
         * Constructs a new BlurImageParams.
         *
         * @param blurSize The blur size.
         */
        public CannyParams(int low, int high) {
            m_high = high;
            m_low = low;
        }

        public int getLow() {
            return m_low;
        }

        public int getHigh() {
            return m_high;
        }
    }
}
