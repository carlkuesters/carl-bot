package carlbot.commands.face;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

public class ImageSearcher_Google implements ImageSearcher {

    private static final String HOST = "https://www.google.com";

    @Override
    public List<String> getImageUrls(String searchTerm, int maximumCheckedImages) throws IOException {
        LinkedList<String> imageUrls = new LinkedList<>();
        String resultsUrl = HOST + "/search?q=" + URLEncoder.encode(searchTerm, "UTF-8") + "&tbm=isch";
        Document documentResults = Jsoup.connect(resultsUrl).get();
        Elements links = documentResults.select("a");
        for (Element link : links) {
            String href = link.attr("href");
            // FIXME: Google removed the links from the instant HTML, they are loaded async now :(
            if (href.startsWith("/imgres?")) {
                String detailsUrl = HOST + href;
                Document documentDetails = Jsoup.connect(detailsUrl).get();
                Element image = documentDetails.getElementById("imi");
                if (image != null) {
                    imageUrls.add(image.attr("src"));
                }
            }
        }
        return imageUrls;
    }
}
