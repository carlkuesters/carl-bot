package carlbot.commands.face;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

public class ImageSearcher_Google implements ImageSearcher {

    @Override
    public List<String> getImageUrls(String searchTerm, int maximumCheckedImages) throws IOException {
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
}
