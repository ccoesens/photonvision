package org.photonvision.vision.barcode;

import java.util.List;
import org.opencv.core.Point;

public class Barcode {
    private final String data;
    private final String type;
    private final List<Point> corners;

    public Barcode(String data, String type, List<Point> corners) {
        this.data = data;
        this.type = type;
        this.corners = corners;
    }

    public String getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public List<Point> getCorners() {
        return corners;
    }
}
