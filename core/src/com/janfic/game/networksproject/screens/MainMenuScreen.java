package com.janfic.game.networksproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.janfic.game.networksproject.RealTimeGameGame;
import com.janfic.game.networksproject.network.Computer;
import com.janfic.game.networksproject.network.Network;
import com.janfic.game.networksproject.network.NetworkEdge;
import com.janfic.game.networksproject.network.NetworkNode;
import com.janfic.game.networksproject.network.Packet;
import com.janfic.game.networksproject.network.generators.CenteredNetworkGenerator;
import com.janfic.game.networksproject.network.generators.NetworkGenerator;

/**
 *
 * @author Jan Fic
 */
public class MainMenuScreen extends Stage implements Screen {

    RealTimeGameGame game;

    ShapeRenderer shapeRenderer;

    Table mainTable;
    Label title, subTitle, authorTitle;
    Image leftPlay, rightPlay, leftHow, rightHow, leftCredits, rightCredits;
    Network displayNetwork;
    NetworkGenerator generator;

    TextButton howToPlayButton, playButton, creditsButton, exitButton;

    public MainMenuScreen(RealTimeGameGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        shapeRenderer = new ShapeRenderer();

        generator = new CenteredNetworkGenerator(70, 500, shapeRenderer);
        displayNetwork = generator.create(1400, 800, 100);

        for (NetworkNode node : displayNetwork.getNodes()) {
            addActor(node);
            node.setZIndex(getActors().size - 1);
            for (NetworkEdge connection : node.getConnections()) {
                addActor(connection);
                connection.setZIndex(0);
            }
        }

        Skin skin = RealTimeGameGame.Assets.SKIN;
        mainTable = new Table(skin) {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.end();
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(0.90f, 1f, 1f, 0.95f);
                shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
                shapeRenderer.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
                shapeRenderer.end();
                batch.begin();
                super.draw(batch, parentAlpha);
            }
        };

        mainTable.defaults().pad(2);

        title = new Label("Real Time Game Tycoon", skin);
        title.setFontScale(3);

        mainTable.add(title).colspan(3).pad(10).row();

        subTitle = new Label("A  Computer Networks Project", skin);
        subTitle.setFontScale(2);

        mainTable.add(subTitle).colspan(3).pad(5).row();

        authorTitle = new Label("By Jan Fic", skin);
        authorTitle.setFontScale(2f);

        mainTable.add(authorTitle).colspan(3).pad(5).row();

        mainTable.add().colspan(3).pad(25).row();

        leftPlay = new Image(RealTimeGameGame.Assets.COMPUTER);
        rightPlay = new Image(RealTimeGameGame.Assets.COMPUTER);
        leftHow = new Image(RealTimeGameGame.Assets.ROUTER);
        rightHow = new Image(RealTimeGameGame.Assets.ROUTER);
        leftCredits = new Image(RealTimeGameGame.Assets.SERVER);
        rightCredits = new Image(RealTimeGameGame.Assets.SERVER);

        howToPlayButton = new TextButton("How To Play", skin);
        playButton = new TextButton("Play", skin);
        creditsButton = new TextButton("Credits", skin);
        exitButton = new TextButton("Exit", skin);

        playButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button); //To change body of generated methods, choose Tools | Templates.
                game.setScreen(new PlayScreen(game));
            }

        });

        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button); //To change body of generated methods, choose Tools | Templates.
                game.setScreen(new HowToPlayScreen(game));
            }
        });
        
        creditsButton.addListener(new ClickListener(){
         @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ReferencesScreen(game));
            }
        });
        
        exitButton.addListener(new ClickListener(){
         @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        howToPlayButton.getLabel().setFontScale(2);
        playButton.getLabel().setFontScale(2);
        creditsButton.getLabel().setFontScale(2);
        exitButton.getLabel().setFontScale(2);

        mainTable.add(leftHow).right();
        mainTable.add(howToPlayButton).width(250);
        mainTable.add(rightHow).left().row();
        
        mainTable.add(leftPlay).right();
        mainTable.add(playButton).width(250);
        mainTable.add(rightPlay).left().row();


        mainTable.add(leftCredits).right();
        mainTable.add(creditsButton).width(250);
        mainTable.add(rightCredits).left().row();

        mainTable.add();
        mainTable.add(exitButton).width(250).row();

        mainTable.top();
        mainTable.setSize(600, 400);
        mainTable.setX(400);
        mainTable.setY(200);

        addActor(mainTable);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float f) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        act();
        draw();
        int i = (int) (Math.random() * displayNetwork.getComputers().size());
        int j = (int) (Math.random() * displayNetwork.getComputers().size());
        if (i != j && Math.random() < 0.25f) {
            Computer a = displayNetwork.getComputers().get(i);
            Computer b = displayNetwork.getComputers().get(j);
            a.pingAll(displayNetwork);

            Packet p = new Packet(a.getShortestPaths().get(b));
            a.sendPacket(p);
        }
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

}
