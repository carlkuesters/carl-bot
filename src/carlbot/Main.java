package carlbot;

import carlbot.commands.face.FaceImageCreator;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) throws LoginException {
        FaceImageCreator.loadNativeLibraries();
        new Bot().connect();
    }
}
