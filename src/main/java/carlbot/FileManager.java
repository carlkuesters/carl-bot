package carlbot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class FileManager {

    public static String getFileContent(String filePath) {
        String text = "";
        String[] lines = getFileLines(filePath);
        for (int i = 0; i < lines.length; i++) {
            if (i != 0) {
                text += "\n";
            }
            text += lines[i];
        }
        return text;
    }

    public static String[] getFileLines(String filePath) {
        LinkedList<String> linesList = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                linesList.add(line);
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String[] lines = new String[linesList.size()];
        linesList.toArray(lines);
        return lines;
    }
}
