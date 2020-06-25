package carlbot.commands.face;

import org.opencv.core.*;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class FaceImageCreator {

    FaceImageCreator(int maximumCheckedImages, boolean randomOrder, int maxWidth, String imagesDirectory) {
        this.maximumCheckedImages = maximumCheckedImages;
        this.randomOrder = randomOrder;
        this.maxWidth = maxWidth;
        this.imagesDirectory = imagesDirectory;
        imageSearcher = new ImageSearcher_Flickr();
        faceDetector = new CascadeClassifier();
        faceDetector.load("./data/haarcascades/haarcascade_frontalface_default.xml");
    }
    private int maximumCheckedImages;
    private boolean randomOrder;
    private int maxWidth;
    private String imagesDirectory;
    private ImageSearcher imageSearcher;
    private CascadeClassifier faceDetector;

    public static void loadNativeLibraries() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    BufferedImage createImage(String searchTerm, int minimumFaces, String carlName) throws Exception {
        System.out.println("Creating image for: " + searchTerm);
        List<String> imageUrls = imageSearcher.getImageUrls(searchTerm, maximumCheckedImages);
        if (randomOrder) {
            Collections.shuffle(imageUrls);
        }
        FaceImageResult result = findResult(imageUrls, minimumFaces);
        if (result != null) {
            return modifyImage(result, carlName);
        }
        return null;
    }

    BufferedImage createImage(URL url, String carlName) throws IOException {
        System.out.println("Creating image for: " + url);
        BufferedImage image = ImageIO.read(url);
        FaceImageResult result = calculateResult(image);
        return modifyImage(result, carlName);
    }

    private FaceImageResult findResult(List<String> imageUrls, int minimumFaces) {
        while (imageUrls.size() > 0) {
            try {
                String fullImageUrl = imageUrls.remove(0);
                BufferedImage image = ImageIO.read(new URL(fullImageUrl));
                FaceImageResult result = calculateResult(image);
                if (result.getFaceDetections().length >= minimumFaces) {
                    return result;
                }
            } catch (Exception ex) {
                System.out.println("Error occured while loading image: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return null;
    }

    private FaceImageResult calculateResult(BufferedImage image) {
        Mat imageMat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        imageMat.put(0, 0, data);
        MatOfRect faceDetectionsMat = new MatOfRect();
        faceDetector.detectMultiScale(imageMat, faceDetectionsMat);
        return new FaceImageResult(image, faceDetectionsMat.toArray());
    }

    private BufferedImage modifyImage(FaceImageResult result, String carlName) throws IOException {
        BufferedImage image = result.getImage();
        for (Rect rect : result.getFaceDetections()) {
            BufferedImage carlFace = ImageIO.read(getCarlFile(carlName));
            Image scaledCarlFace = carlFace.getScaledInstance(rect.width, rect.height, Image.SCALE_SMOOTH);
            image.getGraphics().drawImage(scaledCarlFace, rect.x, rect.y, null);
        }
        if (image.getWidth() > maxWidth) {
            int height = (int) ((((float) image.getHeight()) / image.getWidth()) * maxWidth);
            image = scaleImage(image, maxWidth, height);
        }
        return image;
    }

    File getCarlFile(String carlName) {
        File[] carlFiles = new File(imagesDirectory + "carl/").listFiles();
        if ("random".equals(carlName)) {
            return carlFiles[(int) (Math.random() * carlFiles.length)];
        }
        for (File carlFile : carlFiles) {
            if (carlFile.getName().equals(carlName + ".png")) {
                return carlFile;
            }
        }
        return null;
    }

    private static BufferedImage scaleImage(BufferedImage bufferedImage, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        float scaleX = (((float) width) / bufferedImage.getWidth());
        float scaleY = (((float) height) / bufferedImage.getHeight());
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.drawImage(bufferedImage, scaleTransform, null);
        graphics2D.dispose();
        return scaledImage;
    }
}
