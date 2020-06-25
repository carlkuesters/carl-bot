package carlbot.commands.face;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageSearcher_Flickr implements ImageSearcher {

    ImageSearcher_Flickr() {
        flickr = new Flickr("YOUR-KEY", "YOUR-SECRET-KEY", new REST());
    }
    private Flickr flickr;

    @Override
    public List<String> getImageUrls(String searchTerm, int maximumCheckedImages) throws FlickrException {
        PhotosInterface photos = flickr.getPhotosInterface();
        SearchParameters params = new SearchParameters();
        params.setMedia("photos"); // One of "photos", "videos" or "all"
        params.setExtras(Stream.of("media").collect(Collectors.toSet()));
        params.setText(searchTerm);
        params.setSort(SearchParameters.RELEVANCE);
        PhotoList<Photo> results = photos.search(params, maximumCheckedImages, 0);
        return results.stream()
                .map(Photo::getMediumUrl)
                .collect(Collectors.toList());
    }
}
