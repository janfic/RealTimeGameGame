package com.janfic.game.networksproject.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.janfic.game.networksproject.network.Computer;
import com.janfic.game.networksproject.network.Network;
import java.util.ArrayList;

/**
 *
 * @author Jan Fic
 */
public class Game extends Actor {

    private final String name;
    private final String genre;
    private final String style;

    private int maxPlayers;

    private float money,
            income,
            costs,
            price,
            demand;

    private float averagePing,
            matchWaitTime,
            matchLength,
            playTime;

    private boolean[] supportedTech;

    private Network network;
    private MatchMaker matchMaker;

    public Game(String name, String genre, String style, int maxPlayers, float matchLength, float price, boolean[] supportedTech, Network network, MatchMaker matchMaker) {
        this.name = name;
        this.genre = genre;
        this.style = style;
        this.maxPlayers = maxPlayers;
        this.matchLength = matchLength;
        this.supportedTech = supportedTech;
        this.price = price;
        this.network = network;
        this.matchMaker = matchMaker;
        this.demand = 0.5f;
        this.money = 2000;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public String getStyle() {
        return style;
    }

    public float getDemand() {
        ArrayList<Computer> comps = network.getComputers();
        demand = 0;
        for (Computer computer : network.getComputers()) {
            demand += computer.getDemand();
        }
        demand = demand / comps.size();
        if(network.getNumberOfNodes() < 5) demand = 1;
        return demand;
    }

    public void setSupportedTech(int i, boolean b) {
        this.supportedTech[i] = b;
    }
    
    public boolean[] getSupportedTech() {
        return supportedTech;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public float getMatchLength() {
        return matchLength;
    }

    public float getMoney() {
        return money;
    }

    public void addMoney(float change) {
        money += change;
    }
    
}
