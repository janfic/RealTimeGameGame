package com.janfic.game.networksproject.network;

import com.badlogic.gdx.graphics.Texture;
import com.janfic.game.networksproject.RealTimeGameGame;

/**
 *
 * @author Jan Fic
 */
public class Server extends Computer {
    
    public Server(float x, float y) {
        super(x, y, RealTimeGameGame.Assets.SERVER);
    }

    @Override
    public int getMaxEdges() {
        return 5;
    }

  
}
