package com.janfic.game.networksproject.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.janfic.game.networksproject.RealTimeGameGame;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1400;
        config.height = 800;
        config.foregroundFPS = 60;
        config.resizable = false;
        config.title = "Real-Time Game Game";
        if (System.getProperty("os.name").contains("mac")) {
            config.addIcon("icon-mac.png", Files.FileType.Internal);
        } else {
            config.addIcon("icon.png", Files.FileType.Internal);
        }
        new LwjglApplication(new RealTimeGameGame(), config);
    }
}
