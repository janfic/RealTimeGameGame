package com.janfic.game.networksproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.janfic.game.networksproject.RealTimeGameGame;
import com.janfic.game.networksproject.game.DGSMatch;
import com.janfic.game.networksproject.game.LANMatch;
import com.janfic.game.networksproject.game.P2PMatch;
import com.janfic.game.networksproject.network.Computer;
import com.janfic.game.networksproject.network.Network;
import com.janfic.game.networksproject.network.NetworkEdge;
import com.janfic.game.networksproject.network.NetworkNode;
import com.janfic.game.networksproject.network.Router;
import com.janfic.game.networksproject.network.Server;
import java.util.ArrayList;

/**
 *
 * @author Jan Fic
 */
public class HowToPlayScreen extends Stage implements Screen {

    ShapeRenderer shapeRenderer;

    Network networkExample, lanExample, p2pExample, dgsExample;
    Table infoPanel;
    Skin skin;

    RealTimeGameGame game;

    public HowToPlayScreen(RealTimeGameGame game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        ArrayList<NetworkNode> exampleNodes = new ArrayList<NetworkNode>();
        exampleNodes.add(new Router(1050, 700));
        exampleNodes.add(new Router(1050 - 100, 700));
        exampleNodes.add(new Computer(1050 + 100, 700 + 50));
        exampleNodes.add(new Computer(1050 + 100, 700 - 50));
        exampleNodes.add(new Computer(1050 - 150, 700 - 50));
        ArrayList<NetworkEdge> exampleEdges = new ArrayList<NetworkEdge>();
        exampleEdges.add(new NetworkEdge(exampleNodes.get(0), exampleNodes.get(1), 100, shapeRenderer));
        exampleEdges.add(new NetworkEdge(exampleNodes.get(2), exampleNodes.get(0), 100, shapeRenderer));
        exampleEdges.add(new NetworkEdge(exampleNodes.get(3), exampleNodes.get(0), 100, shapeRenderer));
        exampleEdges.add(new NetworkEdge(exampleNodes.get(4), exampleNodes.get(1), 100, shapeRenderer));
        exampleNodes.get(0).addConnection(exampleEdges.get(0));
        exampleNodes.get(0).addConnection(exampleEdges.get(1));
        exampleNodes.get(0).addConnection(exampleEdges.get(2));
        exampleNodes.get(1).addConnection(exampleEdges.get(0));
        exampleNodes.get(1).addConnection(exampleEdges.get(3));
        exampleNodes.get(2).addConnection(exampleEdges.get(1));
        exampleNodes.get(3).addConnection(exampleEdges.get(2));
        exampleNodes.get(4).addConnection(exampleEdges.get(3));
        networkExample = new Network(exampleNodes, exampleEdges, 200, 200, shapeRenderer);
        for (NetworkNode node : networkExample.getNodes()) {
            addActor(node);
            node.setZIndex(getActors().size - 1);
            for (NetworkEdge connection : node.getConnections()) {
                addActor(connection);
                connection.setZIndex(0);
            }
        }
        
        ArrayList<NetworkNode> lanNodes = new ArrayList<NetworkNode>();
        ArrayList<NetworkEdge> lanEdges = new ArrayList<NetworkEdge>();
        lanNodes.add(new Router(1050, 500));
        lanNodes.add(new Computer(1050, 500 + 75));
        lanNodes.add(new Computer(1050 + 75, 500 - 50));
        lanNodes.add(new Computer(1050 - 75, 500 - 50));
        lanEdges.add(new NetworkEdge(lanNodes.get(0), lanNodes.get(1),50,shapeRenderer));
        lanEdges.add(new NetworkEdge(lanNodes.get(0), lanNodes.get(2),50,shapeRenderer));
        lanEdges.add(new NetworkEdge(lanNodes.get(0), lanNodes.get(3),50,shapeRenderer));
        lanNodes.get(0).addConnection(lanEdges.get(0));
        lanNodes.get(0).addConnection(lanEdges.get(1));
        lanNodes.get(0).addConnection(lanEdges.get(2));
        lanNodes.get(1).addConnection(lanEdges.get(0));
        lanNodes.get(2).addConnection(lanEdges.get(1));
        lanNodes.get(3).addConnection(lanEdges.get(2));
        lanExample = new Network(lanNodes, lanEdges, 200, 200, shapeRenderer);
        for (NetworkNode node : lanExample.getNodes()) {
            addActor(node);
            node.setZIndex(getActors().size - 1);
            for (NetworkEdge connection : node.getConnections()) {
                addActor(connection);
                connection.setZIndex(0);
            }
        }
        LANMatch lanMatch = new LANMatch(lanExample.getComputers(), lanNodes, (Router) lanNodes.get(0), lanExample, shapeRenderer, 10000);
        lanMatch.getListeners().clear();
        addActor(lanMatch);
        
        ArrayList<NetworkNode>  p2pNodes = new ArrayList<NetworkNode>();
        ArrayList<NetworkEdge> p2pEdges = new ArrayList<NetworkEdge>();
        p2pNodes.add(new Router(1050, 300));
        p2pNodes.add(new Computer(1050, 300 + 75));
        p2pNodes.add(new Computer(1050 + 75, 300 - 50));
        p2pNodes.add(new Computer(1050 - 75, 300 - 50));
        p2pEdges.add(new NetworkEdge(p2pNodes.get(0), p2pNodes.get(1),50,shapeRenderer));
        p2pEdges.add(new NetworkEdge(p2pNodes.get(0), p2pNodes.get(2),50,shapeRenderer));
        p2pEdges.add(new NetworkEdge(p2pNodes.get(0), p2pNodes.get(3),50,shapeRenderer));
        p2pNodes.get(0).addConnection(p2pEdges.get(0));
        p2pNodes.get(0).addConnection(p2pEdges.get(1));
        p2pNodes.get(0).addConnection(p2pEdges.get(2));
        p2pNodes.get(1).addConnection(p2pEdges.get(0));
        p2pNodes.get(2).addConnection(p2pEdges.get(1));
        p2pNodes.get(3).addConnection(p2pEdges.get(2));
        p2pExample = new Network(p2pNodes, p2pEdges, 200, 200, shapeRenderer);
        for (NetworkNode node : p2pExample.getNodes()) {
            addActor(node);
            node.setZIndex(getActors().size - 1);
            for (NetworkEdge connection : node.getConnections()) {
                addActor(connection);
                connection.setZIndex(0);
            }
        }
        P2PMatch p2pMatch = new P2PMatch(p2pExample.getComputers(), p2pNodes,  p2pExample, shapeRenderer, 10000);
        p2pMatch.getListeners().clear();
        addActor(p2pMatch);
        
        ArrayList<NetworkNode>  dgsNodes = new ArrayList<NetworkNode>();
        ArrayList<NetworkEdge> dgsEdges = new ArrayList<NetworkEdge>();
        dgsNodes.add(new Router(1050, 100));
        dgsNodes.add(new Computer(1050, 100 + 75));
        dgsNodes.add(new Computer(1050 + 75, 100 - 50));
        dgsNodes.add(new Computer(1050 - 75, 100 - 50));
        dgsNodes.add(new Server(1050 + 200, 100));
        dgsEdges.add(new NetworkEdge(dgsNodes.get(0), dgsNodes.get(1),50,shapeRenderer));
        dgsEdges.add(new NetworkEdge(dgsNodes.get(0), dgsNodes.get(2),50,shapeRenderer));
        dgsEdges.add(new NetworkEdge(dgsNodes.get(0), dgsNodes.get(3),50,shapeRenderer));
        dgsEdges.add(new NetworkEdge(dgsNodes.get(0), dgsNodes.get(4),50,shapeRenderer));
        dgsNodes.get(0).addConnection(dgsEdges.get(0));
        dgsNodes.get(0).addConnection(dgsEdges.get(1));
        dgsNodes.get(0).addConnection(dgsEdges.get(2));
        dgsNodes.get(0).addConnection(dgsEdges.get(3));
        dgsNodes.get(1).addConnection(dgsEdges.get(0));
        dgsNodes.get(2).addConnection(dgsEdges.get(1));
        dgsNodes.get(3).addConnection(dgsEdges.get(2));
        dgsNodes.get(4).addConnection(dgsEdges.get(3));
        dgsExample = new Network(dgsNodes, dgsEdges, 200, 200, shapeRenderer);
        for (NetworkNode node : dgsExample.getNodes()) {
            addActor(node);
            node.setZIndex(getActors().size - 1);
            for (NetworkEdge connection : node.getConnections()) {
                addActor(connection);
                connection.setZIndex(0);
            }
        }
        DGSMatch dgsMatch = new DGSMatch(dgsExample.getComputers(), dgsNodes,  dgsExample, shapeRenderer, (Server) dgsNodes.get(4), 10000);
        dgsMatch.getListeners().clear();
        dgsMatch.setColor(Color.BLUE);
        addActor(dgsMatch);
    }

    @Override
    public void show() {
        skin = RealTimeGameGame.Assets.SKIN;

        
        infoPanel = new Table(skin) {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
                shapeRenderer.end();
                batch.begin();
                super.draw(batch, parentAlpha);
            }
        };

        infoPanel.setWidth(700);
        infoPanel.setHeight(800);
        infoPanel.setPosition(0, 0);
        infoPanel.top();
        infoPanel.pad(20);
        infoPanel.defaults().growX().center().top().space(15);

        Label title = new Label("How To Play", skin);
        title.setFontScale(2);
        title.setAlignment(Align.center);
        
        Label subTitle = new Label("(scroll for more)", skin);
        subTitle.setAlignment(Align.center);

        Label theNetworkLabel = new Label("Simulated Network Basics", skin);
        Label theNetworkInfo = new Label("Real-Time Game Game's purpose is to let you learn about how real-time gaming works in terms of networking. "
                + "This simulator, however, does not encompass all the inticate details of real-time gaming and developing a game that can support it. "
                + "The main focus here is the networking structures that real-time games use today, and which ones are most effective in a real-time game. "
                + "To do this, we simulate a basic network with a graph. A graph in this context is a collection of nodes and connections between them, called edges. "
                + "Each node represents a router, computer, or server. "
                + "The edges represent the trasmition medium between the nodes. ie. the coaxial cable, the fiber or electormagnetic spectrum. "
                + "For the purposes of this simulation it doesn't particularly matter which medium, as long as it can simulate a connection. "
                + "An example of this network can be viewed to the right. ->\n\nDo note that this network is slowed down for readability. "
                + "The packets between will be moving much faster in real life. "
                + "A second in this simulation is a millisecondd in the real world. It is also important to not that you can assume all these messqages are being sent over UDP. "
                + "TCP is only used for data that must arrive, such as in-game text chat, or a score. "
                + "Remember a developer isn't restricted to use one protocol for everything, they have options(as long as they are ablle to succesfully implement it).", skin);
        theNetworkLabel.setFontScale(2);
        theNetworkInfo.setWrap(true);

        Label companyLabel = new Label("The Tycoon", skin);
        Label companyInfo = new Label("In this simulation you will be simulating a game in an economy. "
                + "This is done by simulating money, demand, and of course the customers. "
                + "When a player buys your game they will be added to the network. "
                + "You will also gain $15 profit. "
                + "Whenever a game match is made you make 50 cents for each player in that match. "
                + "Though this is not truly how profit from games work, it is the only reasonable simulatable ways to get profit for our purposes.", skin);
        companyLabel.setFontScale(2);
        companyInfo.setWrap(true);

        Label matchesLabel = new Label("Game Matches and Technology", skin);
        Label matchesInfo = new Label("To get people to join matches you must develope the technology, ie the network technology, the real-time game will use to support the matches. "
                + "There are 3 main types. Local Area Network, Peer to Peer, and Dedicated Game Server. "
                + "You can read more about these in th simulation.", skin);
        matchesLabel.setFontScale(2);
        matchesInfo.setWrap(true);

        Label demandLabel = new Label("Players and Demand", skin);
        Label demandInfo = new Label("To get people to buy your game and join the network, you must keep up the demand for the game. "
                + "To do this you must make sure that your current customers are happy. "
                + "Each plyer is happiest when they are in a match with low ping, the amount of network time between them and the game. "
                + "ie, no lag or connection issues. Players are happy as long as this ping is below 50ms ( 50 real seconds ). "
                + "If a player is not in a match their happiness will also go down. \n\n All these aspects in the game can be viewed in a graph at the bottom right of the screen", skin);
        demandLabel.setFontScale(2);
        demandInfo.setWrap(true);

        TextButton backButton = new TextButton("Back to Main Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        //--

        infoPanel.add(title).row();
        infoPanel.add(subTitle).row();

        infoPanel.row().height(30);
        infoPanel.add(theNetworkLabel).row();
        infoPanel.add(theNetworkInfo).row();
        infoPanel.add(companyLabel).row();
        infoPanel.add(companyInfo).row();
        infoPanel.add(matchesLabel).row();
        infoPanel.add(matchesInfo).row();
        infoPanel.add(demandLabel).row();
        infoPanel.add(demandInfo).row();
        infoPanel.add(backButton).width(200).row();

        ScrollPane scrollPane = new ScrollPane(infoPanel);
        scrollPane.setSize(700, 800);
        addActor(scrollPane);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float f) {
        act(f);
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
