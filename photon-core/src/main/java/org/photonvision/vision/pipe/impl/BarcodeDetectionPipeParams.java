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

import org.photonvision.vision.barcode.Barcode;
public class BarcodeDetectionPipeParams {
    public final Barcode.Type barcodeType;

    public BarcodeDetectionPipeParams(
        Barcode.Type barcodeType) {
        this.barcodeType = barcodeType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((barcodeType == null) ? 0 : barcodeType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BarcodeDetectionPipeParams other = (BarcodeDetectionPipeParams) obj;
        if (barcodeType != other.barcodeType) return false;
        return true;
    }
}
