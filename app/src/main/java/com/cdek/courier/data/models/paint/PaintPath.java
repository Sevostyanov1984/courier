package com.cdek.courier.data.models.paint;

import android.graphics.Path;

public class PaintPath {
    public int color;
    public boolean emboss;
    public int strokeWidth;
    public Path path;

    public PaintPath(int color, boolean emboss, int strokeWidth, Path path) {
        this.color = color;
        this.emboss = emboss;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}
