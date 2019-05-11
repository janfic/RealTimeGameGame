package com.janfic.game.networksproject.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.janfic.game.networksproject.network.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Jan Fic
 */
public class MatchMaker {

    private final LinkedList<GameMatch> matches;

    private final Network network;
    private final ShapeRenderer shapeRenderer;

    public MatchMaker(Network network, ShapeRenderer sr) {
        matches = new LinkedList<GameMatch>();
        this.network = network;
        this.shapeRenderer = sr;
    }

    public LANMatch makeLAN(int maxPlayers, float matchLength, float timeDelay) {
        ArrayList<Computer> availableComputers = removeServers(getAvailableComputers(timeDelay));

        ArrayList<Computer> match;

        //new Matches
        for (Computer computer : availableComputers) {
            match = new ArrayList<Computer>();
            match.add(computer);
            Router router = computer.getRouter();
            for (NetworkNode connectedNode : router.getConnectedNodes()) {
                if (connectedNode == computer || connectedNode instanceof Server || connectedNode instanceof Router || !availableComputers.contains((Computer)connectedNode)) {
                    continue;
                }
                match.add((Computer) connectedNode);
            }
            if (match.size() > 1) {
                ArrayList<NetworkNode> local = new ArrayList<NetworkNode>(network.getLocalNetwork(match));
                LANMatch game = new LANMatch(match, local, router, network, shapeRenderer, matchLength);
                game.setColor(Color.GREEN);
                matches.add(game);
                return game;
            }
        }

        return null;
    }

    public P2PMatch makeP2P(int maxPlayers, float matchLength,  float timeDelay) {
        ArrayList<Computer> availableComputers = removeServers(getAvailableComputers(timeDelay));

        ArrayList<Computer> match = new ArrayList<Computer>();

        for (int i = 0; i < maxPlayers && i < availableComputers.size(); i++) {
            match.add(availableComputers.get(i));
        }

        if (match.size() > 2) {
            ArrayList<NetworkNode> local = new ArrayList<NetworkNode>(network.getLocalNetwork(match));
            P2PMatch game = new P2PMatch(match, local, network, shapeRenderer, matchLength);
            game.setColor(Color.YELLOW);
            matches.add(game);
            return game;
        }
        return null;
    }

    public DGSMatch makeDGS(int maxPlayers, float matchLength,  float timeDelay) {
        ArrayList<Computer> availableComputers = removeServers(getAvailableComputers(timeDelay));
        ArrayList<Server> availableServers = network.getServers();

        ArrayList<Computer> match = new ArrayList<Computer>();

        for (int i = 0; i < maxPlayers && i < availableComputers.size(); i++) {
            match.add(availableComputers.get(i));
        }

        if (match.size() > 3 && availableServers.size() > 0) {
            ArrayList<NetworkNode> temp = new ArrayList<NetworkNode>(match);
            int s = (int) (Math.random() * availableServers.size());
            temp.add(availableServers.get(s));
            ArrayList<NetworkNode> local = new ArrayList<NetworkNode>(network.getLocalNetwork(temp));
            DGSMatch game = new DGSMatch(match, local, network, shapeRenderer, availableServers.get(s), matchLength);
            game.setColor(Color.BLUE);
            matches.add(game);
            return game;
        }

        return null;
    }

    public ArrayList<Computer> removeServers(ArrayList<Computer> list) {
        ArrayList<Computer> ret = new ArrayList<Computer>();
        for (Computer computer : list) {
            if (computer instanceof Server) {
                continue;
            }
            ret.add(computer);
        }
        return ret;
    }

    public ArrayList<Computer> getAvailableComputers(float timeDelay) {
        ArrayList<Computer> availableComputers = new ArrayList<Computer>();
        ArrayList<NetworkNode> nodes = network.getNodes();

        for (NetworkNode node : nodes) {
            if (node instanceof Computer) {
                boolean available = true;
                for (GameMatch match : matches) {
                    if (match.isInMatch((Computer) node) || ((Computer)node).getWaitTime() > timeDelay) {
                        available = false;
                        break;
                    }
                }
                if (available) {
                    availableComputers.add((Computer) node);
                }
            }
        }

        return availableComputers;
    }

    public LinkedList<GameMatch> getMatches() {
        return matches;
    }
    
    public void update(float delta) {
        for (int i = matches.size() - 1; i >= 0; i--) {
            if(matches.get(i).isOver()) {
                matches.get(i).endMatch();
                matches.get(i).remove();
                matches.remove(i);
            }
        }
    }
}
