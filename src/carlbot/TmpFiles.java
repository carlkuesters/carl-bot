package carlbot;

import java.io.File;
import java.util.UUID;

public class TmpFiles {

    private static final String TMP_DIRECTORY = "./tmp/";

    public static File create() {
        File tmpFile = new File(TMP_DIRECTORY + UUID.randomUUID().toString() + ".wav");
        new Thread(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException ex) {
            }
            tmpFile.delete();
        }).start();
        return tmpFile;
    }
}
