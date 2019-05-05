package carlbot.commands.face;

import org.opencv.core.Rect;

import java.awt.image.BufferedImage;

class FaceImageResult {

    FaceImageResult(BufferedImage image, Rect[] faceDetections) {
        this.image = image;
        this.faceDetections = faceDetections;
    }
    private BufferedImage image;
    private Rect[] faceDetections;

    BufferedImage getImage() {
        return image;
    }

    Rect[] getFaceDetections() {
        return faceDetections;
    }
}
