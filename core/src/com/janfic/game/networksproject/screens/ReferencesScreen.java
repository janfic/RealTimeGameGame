package com.janfic.game.networksproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.janfic.game.networksproject.RealTimeGameGame;

/**
 *
 * @author Jan Fic
 */
public class ReferencesScreen extends Stage implements Screen {

    ShapeRenderer shapeRenderer;
    RealTimeGameGame game;
    
    Table mainTable;

    public ReferencesScreen(RealTimeGameGame game) {
        this.game = game;
    }
    
    
    
    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        
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
        
        mainTable.top();
        mainTable.setSize(600, 400);
        mainTable.setX(400);
        mainTable.setY(200);

        mainTable.defaults().growX().pad(10);

        Label title = new Label("References", skin);
        title.setFontScale(3);
        
        Label networksBook = new Label("\"Computer Networking: A Top Down Aproach\" ( Kurose Ross , 2017 )", skin);
        Label wiki = new Label("Is the TCP protocol good enough for real-time multiplayer games? \n\thttps://gamedev.stackexchange.com/questions/431/is-the-tcp-protocol-good-enough-for-real-time-multiplayer-games ( 2019 )", skin);
        Label video = new Label("Connected Games: Building real-time multiplayer games with Unity and Google - Unite LA\n\t     "
                + "https://youtu.be/CuQF7hXlVyk ( Unity , 2018 )", skin);
        networksBook.setWrap(true);
        wiki.setWrap(true);
        video.setWrap(true);
        
        mainTable.add(title).row();
        mainTable.add(networksBook).row();
        mainTable.add(wiki).row();
        mainTable.add(video).row();
        
        TextButton backButton = new TextButton("Back to Main Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        
        mainTable.add(backButton).row();
        
        this.addActor(mainTable);
        
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float f) {
        act();
        draw();
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
