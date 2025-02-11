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

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.objdetect.QRCodeDetector;
import org.photonvision.vision.barcode.Barcode;
import org.photonvision.vision.opencv.CVMat;
import org.photonvision.vision.opencv.Releasable;
import org.photonvision.vision.pipe.CVPipe;

public class BarcodeDetectionPipe extends CVPipe<CVMat, List<Barcode>, BarcodeDetectionPipeParams>
        implements Releasable {

    private QRCodeDetector m_detector = new QRCodeDetector();

    public BarcodeDetectionPipe() {
        super();
    }

    @Override
    protected List<Barcode> process(CVMat in) {
        if (in.getMat().empty()) {
            return List.of();
        }
        if (m_detector == null) {
            throw new RuntimeException("Barcode detector was released!");
        }
        List<String> data = new ArrayList<String>();
        // List<String> type = new ArrayList<String>();
        Mat corners = new Mat();
        m_detector.detectAndDecodeMulti(in.getMat(), data, corners);

        List<Barcode> barcodes = new ArrayList<>();
        List<Point> barcodeCorners = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < 4; j++) {
                barcodeCorners.add(new Point(corners.get(i, j)[0] , corners.get(i, j)[1]));
            }
            barcodes.add(new Barcode(data.get(i), "QR_CODE", barcodeCorners));
        }

        corners.release();

        return barcodes;
    }

    @Override
    public void setParams(BarcodeDetectionPipeParams newParams) {
        if (this.params == null || !this.params.equals(newParams)) {
        }

        super.setParams(newParams);
    }

    @Override
    public void release() {
        m_detector = null;
    }
}
