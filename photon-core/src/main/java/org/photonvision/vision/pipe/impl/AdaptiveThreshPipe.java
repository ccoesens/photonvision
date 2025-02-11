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
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.photonvision.vision.pipe.MutatingPipe;

/** Represents a pipeline that blurs the image. */
public class AdaptiveThreshPipe extends MutatingPipe<Mat, AdaptiveThreshPipe.AdaptiveThreshParams> {
    /**
     * Processes this pipe.
     *
     * @param in Input for pipe processing.
     * @return The processed frame.
     */
    @Override
    protected Void process(Mat in) {
        Imgproc.adaptiveThreshold(
                in,
                in,
                255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY,
                params.m_blockSize,
                params.m_C);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.morphologyEx(in, in, Imgproc.MORPH_CLOSE, kernel);
        kernel.release();
        return null;
    }

    public static class AdaptiveThreshParams {
        // Default BlurImagePrams with zero blur.
        public static AdaptiveThreshParams DEFAULT = new AdaptiveThreshParams(11, 2);

        // Member to store the blur size.
        private final int m_blockSize;
        private final int m_C;

        /**
         * Constructs a new BlurImageParams.
         *
         * @param blurSize The blur size.
         */
        public AdaptiveThreshParams(int blockSize, int C) {
            m_C = C;
            m_blockSize = blockSize;
        }

        public int getLow() {
            return m_blockSize;
        }

        public int getHigh() {
            return m_C;
        }
    }
}
