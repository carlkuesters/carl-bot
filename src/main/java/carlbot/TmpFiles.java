package carlbot;

import java.io.File;
import java.util.UUID;

public class TmpFiles {

    private static final String TMP_DIRECTORY = "./tmp/";

    public static File create(String fileExtension) {
        File tmpFile = new File(TMP_DIRECTORY + UUID.randomUUID().toString() + "." + fileExtension);
        new Thread(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            tmpFile.delete();
        }).start();
        return tmpFile;
    }
}
