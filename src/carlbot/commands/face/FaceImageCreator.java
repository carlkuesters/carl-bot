package carlbot.commands.face;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedList;

public class FaceImageCreator {

    FaceImageCreator(int maximumCheckedImages, boolean randomOrder, int maxWidth, String imagesDirectory) {
        this.maximumCheckedImages = maximumCheckedImages;
        this.randomOrder = randomOrder;
        this.maxWidth = maxWidth;
        this.imagesDirectory = imagesDirectory;
        faceDetector = new CascadeClassifier();
        faceDetector.load("./data/haarcascades/haarcascade_frontalface_default.xml");
    }
    private int maximumCheckedImages;
    private boolean randomOrder;
    private int maxWidth;
    private String imagesDirectory;
    private CascadeClassifier faceDetector;

    public static void loadNativeLibraries() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    BufferedImage createImage(String searchTerm, int minimumFaces, String carlName) throws IOException {
        System.out.println("Creating image for: " + searchTerm);
        LinkedList<String> imageUrls = getImageUrls(searchTerm);
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

    private LinkedList<String> getImageUrls(String searchTerm) throws IOException {
        String googleImagesUrl = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&tbm=isch";
        String urlDelimiterBefore = "\"ou\":\"";
        String urlDelimiterAfter = "\",\"";
        Document document = Jsoup.connect(googleImagesUrl).get();
        String html = document.outerHtml();

        LinkedList<String> imageUrls = new LinkedList<>();
        int htmlSearchOffset = 0;
        while (true) {
            try {
                int fullImageUrlStart = html.indexOf(urlDelimiterBefore, htmlSearchOffset);
                if (fullImageUrlStart == -1) {
                    break;
                }
                fullImageUrlStart += urlDelimiterBefore.length();
                int fullImageUrlEnd = html.indexOf(urlDelimiterAfter, fullImageUrlStart);
                if (fullImageUrlEnd == -1) {
                    break;
                }
                htmlSearchOffset = fullImageUrlEnd;
                String encodedFullImageUrl = html.substring(fullImageUrlStart, fullImageUrlEnd);
                Gson gson = new Gson();
                String fullImageUrl = gson.fromJson("\"" + encodedFullImageUrl + "\"", String.class);
                fullImageUrl = fullImageUrl.replace("\\u003d", "=");
                imageUrls.add(fullImageUrl);
                if (imageUrls.size() >= maximumCheckedImages) {
                    break;
                }
            } catch (Exception ex) {
                System.out.println("Error occured while parsing image url: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return imageUrls;
    }

    private FaceImageResult findResult(LinkedList<String> imageUrls, int minimumFaces) {
        while (imageUrls.size() > 0) {
            try {
                String fullImageUrl = imageUrls.removeFirst();
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
