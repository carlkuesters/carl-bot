package carlbot.commands.face;

import java.util.List;

public interface ImageSearcher {

    List<String> getImageUrls(String searchTerm, int maximumCheckedImages) throws Exception;
}
