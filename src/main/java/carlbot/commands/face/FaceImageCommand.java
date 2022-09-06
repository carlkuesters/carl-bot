package carlbot.commands.face;

import carlbot.Bot;
import carlbot.Command;
import carlbot.TmpFiles;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FaceImageCommand extends Command<MessageReceivedEvent> {

    public FaceImageCommand(Bot bot) {
        super(bot);
    }
    private static final String commandPrefix = "!carl ";
    private static final String imagesDirectory = "./data/images/";
    private static final int maximumMinimumFaces = 20;
    private FaceImageCreator faceImageCreator = new FaceImageCreator(50, true, 800, imagesDirectory);

    private String[] commandParts;
    private int parameterParseIndex;
    private URL imageUrl;
    private String carlName;
    private int minimumFaces;
    private String searchTerm;

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        return content.startsWith(commandPrefix);
    }

    @Override
    public void parse(MessageReceivedEvent event, String content) {
        commandParts = content.substring(commandPrefix.length()).split(" ");
        parameterParseIndex = 0;
        parseCarlName();
        parseImageUrl();
        if (imageUrl == null) {
            parseMinimumFaces(event);
            parseSearchTerm();
        }
    }

    private void parseCarlName() {
        carlName = "carlW";
        if (parameterParseIndex < commandParts.length) {
            String currentCommandPart = commandParts[parameterParseIndex];
            File carlFile = faceImageCreator.getCarlFile(currentCommandPart);
            if (carlFile != null) {
                carlName = currentCommandPart;
                parameterParseIndex++;
            }
        }
    }

    private void parseImageUrl() {
        try {
            imageUrl = new URL(commandParts[parameterParseIndex]);
            parameterParseIndex++;
        } catch (MalformedURLException ex) {
            imageUrl = null;
        }
    }

    private void parseMinimumFaces(MessageReceivedEvent event) {
        minimumFaces = 1;
        if (parameterParseIndex < commandParts.length) {
            try {
                minimumFaces = Integer.parseInt(commandParts[parameterParseIndex]);
                if (minimumFaces > maximumMinimumFaces) {
                    minimumFaces = maximumMinimumFaces;
                    event.getChannel().sendMessage("Ich werd nur nach mindestens " + maximumMinimumFaces + " Gesichtern suchen...").queue();
                }
                parameterParseIndex++;
            } catch (NumberFormatException ex) {
            }
        }
    }

    private void parseSearchTerm() {
        searchTerm = "";
        for (int i = parameterParseIndex; i < commandParts.length; i++) {
            if (i != parameterParseIndex) {
                searchTerm += " ";
            }
            searchTerm += commandParts[i];
        }
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        boolean wasImageSent = false;
        try {
            BufferedImage image = createImage();
            if (image != null) {
                sendImage(event, image);
                wasImageSent = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!wasImageSent) {
            event.getChannel().sendMessage("Konnte nix finden...").queue();
        }
    }

    private BufferedImage createImage() throws Exception {
        if (imageUrl != null) {
            return faceImageCreator.createImage(imageUrl, carlName);
        } else {
            return faceImageCreator.createImage(searchTerm, minimumFaces, carlName);
        }
    }

    private void sendImage(MessageReceivedEvent event, BufferedImage image) throws IOException {
        // TODO: Send embedded image instead of saving+deleting file
        File tmpFile = TmpFiles.create("png");
        ImageIO.write(image, "png", tmpFile);
        FileUpload fileUpload = FileUpload.fromData(tmpFile, "carl.png");
        event.getChannel().sendFiles(fileUpload).queue();
    }
}
