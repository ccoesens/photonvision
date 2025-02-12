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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.photonvision.vision.barcode.Barcode;
import org.photonvision.vision.opencv.CVMat;
import org.photonvision.vision.opencv.Releasable;
import org.photonvision.vision.pipe.CVPipe;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

public class BarcodeDetectionPipe
        extends CVPipe<CVMat, List<Barcode>, BarcodeDetectionPipeParams>
        implements Releasable {

    private MultiFormatReader m_reader = new MultiFormatReader();

    public BarcodeDetectionPipe() {
        super();
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, List.of(BarcodeFormat.DATA_MATRIX, BarcodeFormat.QR_CODE, BarcodeFormat.AZTEC));
        m_reader.setHints(hints);
    }

    @Override
    protected List<Barcode> process(CVMat in) {
        if (in.getMat().empty()) {
            System.out.println("Input Mat is empty.");
            return List.of();
        }

        List<Barcode> barcodes = new ArrayList<>();
        try {
            // Convert Mat to BufferedImage
            Mat mat = in.getMat();
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".png", mat, matOfByte);
            byte[] byteArray = matOfByte.toArray();
            BufferedImage image = null;
            try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArray)) {
                image = ImageIO.read(bais);
            }
            if (image == null) {
                System.out.println("BufferedImage is null.");
                return barcodes;
            }

            // Use ZXing to detect barcodes
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            Result result = m_reader.decodeWithState(bitmap);

            // Create Barcode object
            List<Point> barcodeCorners = new ArrayList<>();
            for (ResultPoint p : result.getResultPoints()) {
                barcodeCorners.add(new Point(p.getX(), p.getY()));
            }
            barcodes.add(new Barcode(result.getText(), Barcode.typeFromString(result.getBarcodeFormat().toString()), barcodeCorners));
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
        m_reader = null;
    }
}
