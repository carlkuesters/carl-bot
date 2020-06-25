package carlbot;

import carlbot.commands.face.FaceImageCreator;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, LoginException {
        FaceImageCreator.loadLibraries();
        new Bot().connect();
    }
}
