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

package org.photonvision.vision.pipeline;

import java.util.ArrayList;
import java.util.List;
import org.photonvision.vision.barcode.Barcode;
import org.photonvision.vision.frame.Frame;
import org.photonvision.vision.frame.FrameThresholdType;
import org.photonvision.vision.pipe.CVPipe.CVPipeResult;
import org.photonvision.vision.pipe.impl.*;
import org.photonvision.vision.pipeline.result.CVPipelineResult;
import org.photonvision.vision.target.TrackedTarget;

public class BarcodePipeline extends CVPipeline<CVPipelineResult, BarcodePipelineSettings> {
    private BarcodeDetectionPipe barcodeDetectionPipe = new BarcodeDetectionPipe();
    private final CalculateFPSPipe calculateFPSPipe = new CalculateFPSPipe();

    public BarcodePipeline() {
        super(FrameThresholdType.GREYSCALE);
        settings = new BarcodePipelineSettings();
    }

    public BarcodePipeline(BarcodePipelineSettings settings) {
        super(FrameThresholdType.GREYSCALE);
        this.settings = settings;
    }

    @Override
    protected void setPipeParamsImpl() {
        var params = new BarcodeDetectionPipeParams(settings.barcodeType);
        barcodeDetectionPipe.setParams(params);
    }

    @Override
    protected CVPipelineResult process(Frame frame, BarcodePipelineSettings settings) {
        long sumPipeNanosElapsed = 0L;

        if (frame.type != FrameThresholdType.GREYSCALE) {
            // We asked for a GREYSCALE frame, but didn't get one -- best we can do is give up
            return new CVPipelineResult(frame.sequenceID, 0, 0, List.of(), frame);
        }

        CVPipeResult<List<Barcode>> tagDetectionPipeResult;
        tagDetectionPipeResult = barcodeDetectionPipe.run(frame.colorImage);
        sumPipeNanosElapsed += tagDetectionPipeResult.nanosElapsed;

        List<TrackedTarget> targetList = new ArrayList<>();
        for (Barcode detection : tagDetectionPipeResult.output) {
            TrackedTarget target = new TrackedTarget(detection);
            System.out.println("Detected barcode corners: " + detection.getCorners());
            targetList.add(target);
        }

        var fpsResult = calculateFPSPipe.run(null);
        var fps = fpsResult.output;

        return new CVPipelineResult(frame.sequenceID, sumPipeNanosElapsed, fps, targetList, frame);
    }

    @Override
    public void release() {
        barcodeDetectionPipe.release();
        barcodeDetectionPipe = null;
        super.release();
    }
}
