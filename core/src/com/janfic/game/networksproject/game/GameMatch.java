package com.janfic.game.networksproject.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.janfic.game.networksproject.network.Computer;
import com.janfic.game.networksproject.network.Network;
import com.janfic.game.networksproject.network.NetworkEdge;
import com.janfic.game.networksproject.network.NetworkNode;
import com.janfic.game.networksproject.screens.PlayScreen;
import java.util.ArrayList;

/**
 *
 * @author Jan Fic
 */
public abstract class GameMatch extends Actor {

    float timeLeft;

    protected ArrayList<Computer> computers;
    protected ArrayList<NetworkNode> localNetwork;

    Network network;

    ShapeRenderer sr;

    float[] verticies;
    Rectangle bounds;

    float matchTime = 60;
    int focused;

    public GameMatch(ArrayList<Computer> computers, ArrayList<NetworkNode> lN, Network network, ShapeRenderer sr, float matchLength) {
        this.computers = computers;
        this.localNetwork = lN;
        this.sr = sr;

        this.network = network;

        verticies = new float[lN.size() * 2];
        for (int i = 0; i < lN.size(); i++) {
            verticies[2 * i] = lN.get(i).getX();
            verticies[2 * i + 1] = lN.get(i).getY();
        }

        Polygon points = new Polygon(verticies);
        bounds = points.getBoundingRectangle();
        this.setBounds(bounds.getX() - 32, bounds.getY() - 32, bounds.getWidth() + 64, bounds.getHeight() + 64);
        focused = 1;

        this.matchTime = matchLength;
        timeLeft = matchLength;

        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                GameMatch.this.setZIndex(GameMatch.this.getStage().getActors().size - 1);
                focused = 4;
                for (NetworkNode node : GameMatch.this.localNetwork) {
                    node.setZIndex(GameMatch.this.getZIndex() + 1);
                    for (NetworkEdge connection : node.getConnections()) {
                        for (NetworkNode n : GameMatch.this.localNetwork) {
                            if (n == node) {
                                continue;
                            }
                            if (connection.isEdgeOf(n) && connection.getNumberOfPackets() > 0) {
                                connection.setZIndex(GameMatch.this.getZIndex() + 1);
                                break;
                            }
                        }

                    }
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                ((PlayScreen) GameMatch.this.getStage()).getPanel().getInfoLabel().setText("Hover over something to show more information");
                GameMatch.this.setZIndex(0);
                focused = 1;
                for (NetworkNode node : GameMatch.this.localNetwork) {
                    for (NetworkEdge connection : node.getConnections()) {
                        for (NetworkNode n : GameMatch.this.localNetwork) {
                            if (n == node) {
                                continue;
                            }
                            if (connection.isEdgeOf(n)) {
                                connection.setZIndex(0);
                                break;
                            }
                        }

                    }
                }
            }

        });
    }

    public boolean isInMatch(Computer computer) {
        return computers.contains(computer);
    }

    public void addComputerToMatch(Computer computer) {
        computers.add(computer);
    }

    public boolean isFocused() {
        return focused == 4;
    }

    public boolean contains(int x, int y) {
        return bounds.contains(x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timeLeft -= delta;
    }

    public boolean isOver() {
        return timeLeft <= 0;
    }

    public int getNumberOfPlayers() {
        return computers.size();
    }

    public void endMatch() {
        for (Computer computer : computers) {
            computer.setWaitTime(10f);
            computer.setColor(Color.WHITE);
            if (computer.getDemand() < 0.8f || Math.random() < 0.25f) {
                network.removeNode(computer);
            }
        }
    }

    @Override
    public void draw(Batch batch, float f) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(new Color(0.7f, 0.7f, 0.7f, (focused - 1) / 4f));
        sr.rect(getX(), getY(), getWidth(), getHeight());
        sr.setColor(this.getColor());
        sr.rectLine(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight(), focused);
        sr.rectLine(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight(), focused);
        sr.rectLine(getX(), getY(), getX(), getY() + getHeight(), focused);
        sr.rectLine(getX(), getY(), getX() + getWidth(), getY(), focused);
        sr.rectLine(getX(), getY() + getHeight() - focused / 2, getX() + getWidth() * (matchTime - timeLeft) / matchTime, getY() + getHeight() - focused / 2, focused + 1);
        sr.end();
        batch.begin();
    }
}
