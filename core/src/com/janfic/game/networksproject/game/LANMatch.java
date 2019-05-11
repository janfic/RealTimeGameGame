package com.janfic.game.networksproject.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.janfic.game.networksproject.network.Computer;
import com.janfic.game.networksproject.network.Network;
import com.janfic.game.networksproject.network.NetworkNode;
import com.janfic.game.networksproject.network.Packet;
import com.janfic.game.networksproject.network.Router;
import com.janfic.game.networksproject.screens.PlayScreen;
import java.util.ArrayList;

/**
 *
 * @author Jan Fic
 */
public class LANMatch extends GameMatch {

    private Computer host;
    private Router router;

    private float toHostTime, fromHostTime;

    public LANMatch(ArrayList<Computer> computers, ArrayList<NetworkNode> localNetwork, Router router, Network network, ShapeRenderer sr, float matchLength) {
        super(computers, localNetwork, network, sr, matchLength);
        this.host = computers.get(0);
        this.host.setColor(Color.GREEN);
        this.router = router;
        this.toHostTime = 0;
        this.fromHostTime = 0;
        router.setColor(Color.GREEN);
        this.setColor(Color.GREEN);

        this.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                ((PlayScreen) LANMatch.this.getStage()).getPanel().getInfoLabel().setText(
                        "This is a real-time game match using LAN.\n"
                        + "Computers: " + LANMatch.this.computers.size() + "\n"
                        + "Total Nodes in Local Network: " + LANMatch.this.localNetwork.size());
            }
        });
    }

    @Override
    public void endMatch() {
        super.endMatch(); //To change body of generated methods, choose Tools | Templates.
        router.setColor(Color.WHITE);
    }

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);
        toHostTime += deltaTime;
        fromHostTime += deltaTime;
        if (toHostTime >= 0.5f) {
            for (Computer computer : computers) {
                computer.pingAll(network);
                if (computer != host) {
                    Packet toHost = new Packet(computer.getShortestPaths().get(host));
                    toHost.setColor(Color.FOREST);
                    computer.sendPacket(toHost);

                }
            }
            toHostTime = 0;
        }
        if (fromHostTime >= 1) {
            for (Computer computer : computers) {
                computer.pingAll(network);
                if (computer != host) {
                    Packet fromHost = new Packet(host.getShortestPaths().get(computer));
                    fromHost.setColor(Color.GREEN);
                    host.sendPacket(fromHost);
                }
            }
            fromHostTime = 0;
        }

    }

    @Override
    public void draw(Batch batch, float f) {
        super.draw(batch, f);
        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(getColor());
        for (Computer computer : computers) {
            if (computer == host) {
                continue;
            }
            sr.rectLine(host.getX(), host.getY(), computer.getX(), computer.getY(), focused / 3 + 1);
        }
        sr.end();
        batch.begin();
    }

}
