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

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.photonvision.vision.barcode.Barcode;
import org.photonvision.vision.opencv.CVMat;
import org.photonvision.vision.opencv.Releasable;
import org.photonvision.vision.pipe.CVPipe;

import boofcv.factory.fiducial.ConfigQrCode;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.abst.fiducial.QrCodePreciseDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.struct.image.GrayU8;

public class BarcodeDetectionPipe
        extends CVPipe<CVMat, List<Barcode>, BarcodeDetectionPipeParams>
        implements Releasable {
    private QrCodePreciseDetector<GrayU8> detector;

    public BarcodeDetectionPipe() {
        super();
        detector = FactoryFiducial.qrcode(ConfigQrCode.fast(), GrayU8.class);
    }

    @Override
    protected List<Barcode> process(CVMat in) {
        if (in.getMat().empty()) {
            System.out.println("Input Mat is empty.");
            return List.of();
        }

        List<Barcode> barcodes = new ArrayList<>();
        try {
            // Convert Mat to GrayU8 directly
            Mat mat = in.getMat();
            if (mat.empty()) {
                System.out.println("Input Mat is empty.");
                return barcodes;
            }

            GrayU8 gray = new GrayU8(mat.width(), mat.height());
            byte[] data = new byte[mat.width() * mat.height()];
            mat.get(0, 0, data);
            gray.data = data;

            // Use BoofCV to detect barcodes
            detector.process(gray);

            for (QrCode code : detector.getDetections()) {
                List<Point> barcodeCorners = new ArrayList<>();
                for (int i = 0; i < code.bounds.size(); i++) {
                    barcodeCorners.add(new Point(code.bounds.get(i).x, code.bounds.get(i).y));
                }
                barcodes.add(new Barcode(code.message, Barcode.Type.QR_CODE, barcodeCorners));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return barcodes;
    }

    @Override
    public void setParams(BarcodeDetectionPipeParams newParams) {
        if (this.params == null || !this.params.equals(newParams)) {
            // Set parameters if needed
        }

        super.setParams(newParams);
    }

    @Override
    public void release() {
        detector = null;
    }
}
