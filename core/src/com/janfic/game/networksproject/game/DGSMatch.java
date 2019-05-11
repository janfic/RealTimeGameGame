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
import com.janfic.game.networksproject.network.Server;
import com.janfic.game.networksproject.screens.PlayScreen;
import java.util.ArrayList;

/**
 *
 * @author Jan Fic
 */
public class DGSMatch extends GameMatch {

    Color darker;
    Server server;
    float time;

    public DGSMatch(ArrayList<Computer> computers, ArrayList<NetworkNode> localNetwork, Network network, ShapeRenderer sr, Server server, float matchLength) {
        super(computers, localNetwork, network, sr, matchLength);
        this.server = server;
        time = 0;
        this.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                ((PlayScreen) DGSMatch.this.getStage()).getPanel().getInfoLabel().setText(
                        "This is a real-time game match using DGS.\n"
                        + "Computers: " + DGSMatch.this.computers.size() + "\n"
                        + "Total Nodes in Local Network: " + DGSMatch.this.localNetwork.size());
            }
        });
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color); //To change body of generated methods, choose Tools | Templates.
        darker = color.cpy().mul(0.5f, 0.5f, 0.5f, 1);
        for (Computer computer : computers) {
            computer.setColor(color);
        }
//        server.setColor(color);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
        if (time >= 1) {
            server.pingAll(network);
            for (Computer computer : computers) {
                computer.pingAll(network);
                Packet toServer = new Packet(computer.getShortestPaths().get(server));
                toServer.setColor(darker);
                computer.sendPacket(toServer);
                Packet fromServer = new Packet(server.getShortestPaths().get(computer));
                fromServer.setColor(getColor());
                server.sendPacket(fromServer);
            }
            time = 0;
        }

    }

    @Override
    public void draw(Batch batch, float f) {
        super.draw(batch, f);
        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(getColor());
        for (Computer computer : computers) {

            sr.rectLine(server.getX(), server.getY(), computer.getX(), computer.getY(), focused / 3 + 1);
        }
        sr.end();
        batch.begin();
    }

}
