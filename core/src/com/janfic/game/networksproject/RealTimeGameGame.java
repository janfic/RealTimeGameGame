package com.janfic.game.networksproject;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.janfic.game.networksproject.screens.MainMenuScreen;
import com.janfic.game.networksproject.screens.PlayScreen;

public class RealTimeGameGame extends Game {

    ShapeRenderer shapeRenderer;

    MainMenuScreen mainMenuScreen;

    @Override
    public void create() {
        mainMenuScreen = new MainMenuScreen(this);
        
        this.setScreen(mainMenuScreen);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.6f, 0.6f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose() {
    }

    public static class Assets {

        public final static Texture ROUTER = new Texture("router.png");
        public final static Texture COMPUTER = new Texture("computer.png");
        public final static Texture SERVER = new Texture("server.png");
        public final static Texture PACKET = new Texture("packet.png");
        public final static Skin SKIN = new Skin(Gdx.files.internal("skin/skin.json"));
    }
}
