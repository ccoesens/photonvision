package org.photonvision.vision.barcode;

import java.util.List;
import org.opencv.core.Point;

public class Barcode {
    private final String data;
    private final Type type;
    private final List<Point> corners;

    public Barcode(String data, Type type, List<Point> corners) {
        this.data = data;
        this.type = type;
        this.corners = corners;
    }

    public String getData() {
        return data;
    }

    public Type getType() {
        return type;
    }

    public List<Point> getCorners() {
        return corners;
    }

    public enum Type {
        EAN_13,
        EAN_8,
        UPC_A,
        UPC_E,
        CODE_128,
        CODE_39,
        CODE_93,
        CODABAR,
        ITF,
        QR_CODE,
        DATA_MATRIX,
        PDF_417,
        AZTEC,
        OTHER,
        ALL
    }
    
    public static Type typeFromString(String type) {
        try {
            return Type.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Type.OTHER;
        }
    }
}
