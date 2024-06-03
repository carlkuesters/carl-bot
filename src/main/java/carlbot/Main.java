package carlbot;

import carlbot.commands.face.FaceImageCreator;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        FaceImageCreator.loadLibraries();
        new Bot().connect();
    }
}
