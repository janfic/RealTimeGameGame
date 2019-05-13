package com.janfic.game.networksproject.network.generators;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.janfic.game.networksproject.network.Network;
import com.janfic.game.networksproject.network.NetworkNode;
import java.util.Random;

/**
 *
 * @author Jan Fic
 */
public abstract class NetworkGenerator {

    Random random;

    ShapeRenderer sr;
    
    int gridSize;
    int gridWidth;
    int gridHeight;

    public NetworkGenerator(int gridSize, int gridWidth, int gridHeight, ShapeRenderer sr) {
        this.gridSize = gridSize;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        this.sr = sr;
        
        random = new Random();
    }

    public abstract Network create(int width, int height);
    public abstract Network create(int width, int height, int overideRadius);
    public abstract NetworkNode addNode(Network network);
    public abstract NetworkNode addNode(Network network, NetworkNode node);
    public abstract NetworkNode addNode(Network network, float x, float y);
    public abstract NetworkNode addNode(Network network, Class<? extends NetworkNode> type, int x, int y);
    public abstract NetworkNode addNode(Network network, Class<? extends NetworkNode> type);
}
