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
import com.janfic.game.networksproject.screens.PlayScreen;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Jan Fic
 */
public class P2PMatch extends GameMatch {

    float time;

    public P2PMatch(ArrayList<Computer> computers, ArrayList<NetworkNode> localNetwork, Network network, ShapeRenderer sr, float matchLength) {
        super(computers, localNetwork, network, sr, matchLength);
        this.setColor(Color.YELLOW);
        for (Computer computer : computers) {
            computer.setColor(Color.YELLOW);
        }
        time = 0;
        
        this.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                ((PlayScreen) P2PMatch.this.getStage()).getPanel().getInfoLabel().setText(
                        "This is a real-time game match using P2P.\n"
                        + "Computers: " + P2PMatch.this.computers.size() + "\n"
                        + "Total Nodes in Local Network: " + P2PMatch.this.localNetwork.size());
            }
        });
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color); //To change body of generated methods, choose Tools | Templates.
        for (Computer computer : computers) {
            computer.setColor(color);
        }
    }
    
   

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);
        time += deltaTime;
        if (time >= 1) {
            for (Computer computer : computers) {
                computer.pingAll(network);
                for (Computer c : computers) {
                    if (c == computer) {
                        continue;
                    }
                    LinkedList<NetworkNode> path = computer.getShortestPaths().get(c);
                    Packet packet = new Packet(path);
                    packet.setColor(this.getColor());
                    computer.sendPacket(packet);
                }
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
        for (int i = 0; i < computers.size(); i++) {
            for (int j = i; j < computers.size(); j++) {
                Computer a = computers.get(i);
                Computer b = computers.get(j);
                sr.rectLine(a.getX(), a.getY(), b.getX(), b.getY(), focused / 3 + 1);
            }
        }
        sr.end();

        batch.begin();
    }

}
