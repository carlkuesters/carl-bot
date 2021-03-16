package carlbot.commands.face;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class ImageSearcher_Bing implements ImageSearcher {

    private static final String HOST = "https://www.bing.com";

    @Override
    public List<String> getImageUrls(String searchTerm, int maximumCheckedImages) throws Exception {
        LinkedList<String> imageUrls = new LinkedList<>();
        String resultsUrl = HOST + "/images/search?q=" + URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
        String responseHTML = getResponse(resultsUrl);
        Document documentResults = Jsoup.parse(responseHTML);
        Elements links = documentResults.select("a");
        for (Element link : links) {
            String m = link.attr("m");
            if (m.length() > 0) {
                JSONObject jsonObject = new JSONObject(m);
                String imageUrl = jsonObject.getString("murl");
                imageUrls.add(imageUrl);
            }
        }
        return imageUrls;
    }

    private String getResponse(String url) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(url));
        request.setHeader("cookie", "MMCA=ID=309D46A3161B463581D183EC96FB7F6E; _IDET=MIExp=0&VSNoti2=20210316; MUID=215F70546E276FCD26407EBB6F4C6E68; MUIDB=215F70546E276FCD26407EBB6F4C6E68; SRCHD=AF=NOFORM; SRCHUID=V=2&GUID=A14651452AEB4308A8B0D75D3B88147A&dmnchg=1; BFBUSR=BAWAS=1&BAWFS=1; OID=AhBNlO9owyFNq_ma1d2r9NTMgo0gX_7Vm2FMRpsBrDNLFApr4EPY9KCFQKlY7USyYLPV3_XOaEcH8Qhl6HvmYyqk8rZ6eme5HWM1_UxQQFIYaxazeC-1TeShAe-h4SEmLNI; OIDI=ghBzGWPpKqOyDMgIHXFtw8dnPqDHk7eo4hhFWsg8JEASp6PMRzBbjM4wc3dvxUt9qxRkbZXTzNxGewfXJ3SZGKKPVjkhWvWg8_ASYxgSpuJSXyCRzbA1UYrdpC9I7lkdblLMgtwOuLK6ZkFgBiD5v8O6zNOVLVjIgsImlW3ide6ebZw54SBUWmKHNwRN039WblkkUYXuYPNClJE8WljbNFPNIQPlkT815l3T-agChr-V7eMgkhk7-CDUvPFX7FRDxoB7RW2_rMnah93t6cwtGmtruW8atQAUtAduCJUwifR5XTHYT1aWcxdRYE6LyRDyfUkdeAz2HJ-bCUitBXGqPXV2TP_5CFGpITkLPiXmoYLao4YrEmaQPeRSwnF98G34bijGb28hMLb8hAJR1zg-Bi0-vAnsfb3bTjiVuef1B5PFMlx00TRGG0MdeofrUjoo__RsesGIhrPbOQ7BZFNw1wyP-2WJuZn0CYMTyVKqPEyFG9oIKnlpXFfKlqHtGbWtc4Fj493xTsmr2np2YmXydGefKvb4Wq_P-F36BZnQFm_Qeswic5BtRKlgjUWdeQiFJ3B4iaaLv0AVIRlAurpY9gorKh518h4GhD58BQ4MUpnj6AKA2H4FW9C5OYKtuYbxAi1c43x6c1GpsJfdZTRN_fQSxCNMfO3oYUhvmzQDlb00TMjvu3Xcry6TzudzE36JqM0UI1d5_rx21-bu7cz_mb27uMe7HS2x-kEBTg16JEUCuqdaCMeya7Pl184NUmK_1ax-1B8OHj0KdFqiOc3DrkVkWuxxRRiW6AFJTqt0l5W6lQtdoG1wPF42nXJBWyXkxPAP9c-e7PW6SuYocXgUotNvQ1I4Ra0tUlv_H00N1nQEObAXIpz9gqRz9ENRuwcJbp_H0316x7j17DxpeA3o-X4cnflOMV0xMe2-Yd3T5ex_OP0_bMul3XOcS5s9_3V6hMUnSaoHuyUwQ8RcsQAPfaJMfwTMcp1784sxkYx9z-dei12WQIfmfx3MKcwHEIHxRqBfN9GWqFpRR0lkDFYlarp8OOPXMGYopPMVvwy-6ScMUoFtUhC1G7FceBcoVzHgf81oNpEtPxLnOB9J4V73vZrgg5BE1RE8HxzJHxfy0ge-bkR8UFiVCZsdi49Et1wGNcHmIxzzsvrTouK8pyVXXXJSJ5xkJTF1wUP14WmVqGzhVICLHcg5Y06o1pavnovisksp7REyOtaknGB-oD6lD9iMKBIhA_iyR0SNa7GVbIVIXgUmCR7VT17591vBpIxgb63r6oWe2reCF1eQm92qpcyAG4bbqE8HYLPaP21Vn-D2nBSEZVq5SulLu9A3vfQBa_M_lDho7ZLee78l7GJ9IuxzjoFOtNGzAmslPMpsmQz_BoV-MvRILoP3ordoJObRwcFfl8JHMM3fCo1w68ATuSsQ0S_9UTKcdlBhtmSN9LFeDAbjYZYtS1dfzXlfxHOSuERo6AthN_npNHIIVDCoXzWwAxF5O57F_Q8BAcovWdQUnLOkdaVYh690hFBSGdz6i45M-AGkQmbcyX8W0fQXjvGPxaD1RbBl3ZKQbhkyOcX6xo-YmnTvaP7bYILuKaBJ5Jb53BfhCfZBwYYdWxLLah4RQgdFeEb6KKqNldQJ61e-RKiPUzrieF16-qceS6Z6oVc7MAZDuB9PgasA8xyfP0zBSkE_0WkfqHC7an018WoSa53jHV3Mn1HLeH3i7Wm7P8Rjy64K_D38TbdVIp-dfsKS0AD3p3Iv8ND1od8C1GRGt5YZe5CqgxWr7H4lG-LiTmy3n5Q29gckt5onKs2YBfTmlHNNnN4yK5PXgvGMT17ljOhOwRWXrlzBL_xVBaeh0W4V63O7kwtBpzAVk564HwmWlh6JNpBYoa_RX0AzYO9BSH1ZKMA8GoKCWsAsVO-4OAmAui8BV-JmO6nrIN0LXcPgFM72Ifm--mCG4x420TzRdAa3sxnQ-JLqP6Ds3uRTMMVe2dMayWFdaHzjeLmH84BuBPgLRbaEgdb4U3aCs-4a08MnJ04k3Mjnz8Vb6UDAJOxk-xoSD92MODqYwTfvv07Y; OIDR=ghCY1td9zE51KStGH7oau8WLi6DhKy-nMIub6pTfxdNUqflec8lYxxzx41BeY9LmAuYPJYXCBDkTLRGEnsjyaZPq_xoWbptPI5RVfeFMp0M3j3AnjYUvfPnKFxstCiId6efWR4FTxuK4EV0H8otIbhShMwfo1fVEBiSJ27mZPj8DUZ-x0f8oZ9ho6rA3Cy7RI_aanVAwJaoF2W4El_NP_wLhMIV0SbHGNyTcFEefhxjuy1AWOVUZeO5RPLWMQr0VIOvgzWzbZGmgB6P9JUPkC_g2cH3wd1bo50-dMVjzTLVU9o4sO9qDht9gnKX4XPFyA5ffJ3oCRFhlfLCIOdzXVqEYEw-EddtjNvGST08gmxs2JGOHT3q2aoIVHp6sCp5KoDrt2r9b6EpKd1-H8MpSfFU_93ivA3aUbTSCLbIqQInoIzN_n5rwEXTb3yZuct_nE4-2lMIUs5B5-On2FuSJoF8_GfXgS6J13xnAJK-JraEpaRUeeuzTLW4zpRytNigbPlWA0FGaNRKbtFF1lBDQZiKfBKzJI83yjWSh7KVdMcTAgnAwelPEcYET7KS2dHrU0oKi4cJItS_yGDIYs7RCNlno8E55cNCEwHyUp7AzU4BmZOoiKyLqvW48gfPbsefmjLwmCSOWF_nB8cqyNiazEkrMcmTDQKFWSphjUvNVuLrFB5Bjx8zOifgezo7m1VJx_xAYAJcuzJ7R7R4e4whQSem_BwGPzNkEEtR2YHCqcIVoTsDKYlEpGwNciQtISjEblzotaVQcnhRfbsoZBNh1HQQZjD77CXwBIg0FAtSOJz1y9fCV0p7VdBFYNB0cpAuCSjmt3TjKzecdvR3jzPlKC6EZlcqWleHUzx6kmXjlSiNY1JZbrtBha2vYRcFULyf7HmBC108DDBlpFSJqu2B6wvB8l2gA2mubo0TqDh8W8UHEUIYv0At6Crfu2u92NqAOGUC2r15kl0JjTM0AbYXhKIhmOyYztLQNrN42lgv60iUPRABIJpFlvsA4rYtDf3Rrv1v6TTjrSHwomNgsTi9zHF0dpreqnCGIHPW57x_RROBGZ3mumjoMWSjU0V7AHjaq98ePfE6CJZuk7Ebsiccm2LgZBbUgtDFWz0uj1toCCuDR844Kq4uPh7cRAjsu8TCt86RMh4OzuRqo15Hlz60UGGHQtt7dXBSVVxo7PavYNA9OLptb5iivaO0s66iwgCbT3VtcxEPz6m_ZjBRbXWCGCiDcIyez-RCWbZOnUUB7LxZk3dHVtBNajfbaV6uexeL_KW9_JtOyBGByzq-hIh383PAeehbdLpoyY-5-7DKB19wTV_-a3mnb7Q4y5ZHpVZBL4E_dLlbS-fYrfZkpk8NjKp2PIaK8OLL_mYyH4F2CQEbfKA; ipv6=hit=1615896034514&t=4; imgv=flts=20210316; SRCHUSR=DOB=20210316&T=1615892390000&TPC=1615892439000; BCP=AD=1&AL=1&SM=1; _EDGE_S=SID=19AB0B9ABDE767BF02C80466BC84669E&mkt=de-de&ui=de-de; _HPVN=CS=eyJQbiI6eyJDbiI6MSwiU3QiOjAsIlFzIjowLCJQcm9kIjoiUCJ9LCJTYyI6eyJDbiI6MSwiU3QiOjAsIlFzIjowLCJQcm9kIjoiSCJ9LCJReiI6eyJDbiI6MSwiU3QiOjAsIlFzIjowLCJQcm9kIjoiVCJ9LCJBcCI6dHJ1ZSwiTXV0ZSI6dHJ1ZSwiTGFkIjoiMjAyMS0wMy0xNlQwMDowMDowMFoiLCJJb3RkIjowLCJEZnQiOm51bGwsIk12cyI6MCwiRmx0IjowLCJJbXAiOjR9; _RwBf=mtu=0&g=0&cid=&o=2&p=&c=&t=0&s=0001-01-01T00:00:00.0000000+00:00&ts=2021-03-16T11:45:09.4410755+00:00&e=; _SS=SID=19AB0B9ABDE767BF02C80466BC84669E&R=70&RB=0&GB=0&RG=200&RP=65; SRCHHPGUSR=SRCHLANGV2=de&CW=1177&CH=952&DPR=1&UTC=60&DM=0&HV=1615895152&WTS=63751489190&BRW=HTP&BRH=M&ADLT=OFF");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
