package com.janfic.game.networksproject.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.janfic.game.networksproject.RealTimeGameGame;
import com.janfic.game.networksproject.game.Game;
import com.janfic.game.networksproject.game.MatchMaker;
import com.janfic.game.networksproject.network.Network;
import java.text.DecimalFormat;

/**
 *
 * @author Jan Fic
 */
public class SidePanel extends Table {

    private final ShapeRenderer shapeRenderer;

    private final Network network;
    private final MatchMaker matchMaker;
    private Game game;
    private DecimalFormat format;

    private final Label nodesLabel, edgesLabel, packetsLabel, playersLabel, activePlayers, averageDemand, moneyLabel;

    private Table infoTable;
    private Label infoLabel, infoTitle;

    private int nodeCount, connectionCount;

    private Graph currentGraph, demandGraph, moneyGraph, packetGraph;
    float moneyTimer, demandTimer;
    private TextButton demandButton, moneyButton, packetButton;

    public SidePanel(int x, int y, Network network, MatchMaker matchMaker, ShapeRenderer shapeRenderer, Game game) {
        super(RealTimeGameGame.Assets.SKIN);
        setX(x);
        setY(y);
        setWidth(600);
        setHeight(800);
        this.network = network;
        this.matchMaker = matchMaker;
        this.nodeCount = network.getNumberOfNodes();
        this.connectionCount = network.getNumberOfEdges();
        this.shapeRenderer = shapeRenderer;
        this.game = game;
        this.format = new DecimalFormat("00.0");
        demandGraph = new Graph(550, 200, shapeRenderer);
        moneyGraph = new Graph(550, 200, shapeRenderer);
        packetGraph = new Graph(550, 200, shapeRenderer);
        currentGraph = moneyGraph;
        demandButton = new TextButton("Show Demand Graph", getSkin());
        moneyButton = new TextButton("Show Money Graph", getSkin());
        packetButton = new TextButton("Show Packet Graph", getSkin());

        moneyTimer = 0;
        demandTimer = 0;

        defaults().pad(5).center().growX();

        Label networkTitle = new Label("The Network", getSkin());
        networkTitle.setFontScale(2f);
        add(networkTitle);

        Label gameTitle = new Label(game.getName(), getSkin());
        gameTitle.setFontScale(2f);
        add(gameTitle).row();

        nodesLabel = new Label("NODES: " + network.getNumberOfNodes(), getSkin());
        add(nodesLabel);

        playersLabel = new Label("Players: " + game.getDemand(), getSkin());

        add(playersLabel).row();

        edgesLabel = new Label("EDGES: " + network.getNumberOfEdges(), getSkin());

        add(edgesLabel);

        activePlayers = new Label("Active Players: " + (network.getComputers().size() - matchMaker.getAvailableComputers(300).size()), getSkin());

        add(activePlayers).row();

        packetsLabel = new Label("Packets: " + network.getPackets(), getSkin());

        add(packetsLabel);

        averageDemand = new Label("Average Demand: " + network.getDemand(), getSkin());

        add(averageDemand).row();

        Table store = new Table();
        store.defaults().space(5);
        Label storeLabel = new Label("Business", getSkin());
        moneyLabel = new Label("$" + game.getMoney(), getSkin());
        moneyLabel.setFontScale(2f);
        moneyLabel.setAlignment(Align.right);
        storeLabel.setFontScale(2f);
        final TextButton lanButton = new TextButton("Develop LAN\n$500", getSkin());
        final TextButton p2pButton = new TextButton("Develop P2P\n$1000", getSkin());
        final TextButton dgsButton = new TextButton("Develop DGS\n$1500\n$500/server", getSkin());
        store.add(storeLabel).growX().colspan(2);
        store.add(moneyLabel).growX().row();
        store.add(lanButton).grow();
        store.add(p2pButton).grow();
        store.add(dgsButton).grow().row();

        add(store).colspan(2).row();

        infoTable = new Table(getSkin());
        infoTitle = new Label("Information:", getSkin());
        infoTitle.setFontScale(2);
        infoLabel = new Label("Hover over something to show more information", getSkin());
        infoLabel.setWrap(true);
        infoLabel.setAlignment(Align.topLeft);

        infoTable.add(infoTitle).growX().pad(5).row();
        infoTable.add(infoLabel).top().grow().pad(5);
        add(infoTable).colspan(2).height(280).row();

        final Table graphTable = new Table(getSkin());
        graphTable.defaults().space(5).grow().top().center();

        graphTable.add(currentGraph).colspan(3).row();

        graphTable.add(demandButton, moneyButton, packetButton);

        demandButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                graphTable.clear();
                graphTable.add(demandGraph).colspan(3).row();
                graphTable.add(demandButton, moneyButton, packetButton);
            }
        });
        moneyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                graphTable.clear();
                graphTable.add(moneyGraph).colspan(3).row();
                graphTable.add(demandButton, moneyButton, packetButton);
            }
        });
        packetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                graphTable.clear();
                graphTable.add(packetGraph).colspan(3).row();
                graphTable.add(demandButton, moneyButton, packetButton);
            }
        });

        add(graphTable).colspan(2);

        lanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (SidePanel.this.game.getMoney() >= 500 && !lanButton.isDisabled()) {
                    SidePanel.this.game.setSupportedTech(0, true);
                    SidePanel.this.game.addMoney(-500);
                    lanButton.setDisabled(true);
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                infoLabel.setText(
                        "The LAN real-time game network is the most basic structure a real-time game can implement. "
                        + "This structure allows player only on the same local network to connect and play. "
                        + "One player acts as the \"Host\" while others are considered a \"client\". Though this is a "
                        + "cheap model and one relatively easy to implement into a game, it does present a few probelems."
                        + " Everything in this structure depends on the host computer, meaning the host is determining what "
                        + "goes on in-game. This allows the host to have an advantage as well as more strain on their computer.\n\n"
                        + "This basic structure will allow players to play local games. Earning you profit and increasing the d"
                        + "emand of the game");
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                infoLabel.setText("Hover over something to show more information");
            }

        });
        p2pButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (SidePanel.this.game.getMoney() >= 1000 && !p2pButton.isDisabled()) {
                    SidePanel.this.game.setSupportedTech(1, true);
                    SidePanel.this.game.addMoney(-1000);
                    p2pButton.setDisabled(true);
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                infoLabel.setText("The P2P, Peer-to-Peer, structure of a real-time game can connect many people to each"
                        + " other to play a game. Each player sends data to every other player for interpretation. In a perfect system"
                        + ", ie when this in implemented well, each computer will interpret all other data the same, even if it has come"
                        + " in at different times. This takes a lot of capital to make sure this works properly. In addition to make sure that "
                        + "this structure is secure and cheat free is also difficult without communicating more between ach computer, ie. more "
                        + "work for each computer."
                        + "\n\n Peer-to-Peer makes sure everyone is in a game, increasing your demand and profits, if you can afford the implementation cost.");
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                infoLabel.setText("Hover over something to show more information");
            }

        });
        dgsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (SidePanel.this.game.getMoney() >= 1500 && !dgsButton.isDisabled()) {
                    SidePanel.this.game.setSupportedTech(2, true);
                    SidePanel.this.game.addMoney(-1500);
                    dgsButton.setDisabled(true);
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                infoLabel.setText("A Dedicated Game Server, DGS, real-time network structure is the most commonly used structure in real-time games today. "
                        + "This is because it is the most reliable and effective way of connecting players. In this structure players connect to a server, "
                        + "the DGS, and speak to that and only the server, not to other players. This makes the server in control of the game, not a player. "
                        + "This is helpful for anti-cheat as well as security issues that players may have, giving out their IP address to other players. "
                        + "It is important to note that the server is expensive compared to the other structures. However, the implementation of interpreting "
                        + "data from players is simular, but less than P2P.\n\n"
                        + "This structure has reduced network weight than P2P because the players don't need to send many copies of the same data to different "
                        + "players.\n\nAfter this has been bought PRESS S to add another DGS to the network for $500");
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                infoLabel.setText("Hover over something to show more information");
            }
        });

        left();
        top();
    }

    public Label getInfoLabel() {
        return infoLabel;
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
        nodesLabel.setText("NODES: " + network.getNumberOfNodes());
        edgesLabel.setText("EDGES: " + network.getNumberOfEdges());
        playersLabel.setText("Players: " + network.getComputers().size());
        activePlayers.setText("Active Players: " + (network.getComputers().size() - matchMaker.getAvailableComputers(31).size()));
        moneyLabel.setText("$" + format.format(game.getMoney()));
        packetsLabel.setText("Packets: " + network.getPackets());
        averageDemand.setText("Average Demand: " + format.format(game.getDemand() * 100) + "%");
        demandTimer += delta;
        if(demandTimer >= 0.05f) {
            demandGraph.next(game.getDemand());
            demandTimer = 0;
        }
        
        moneyTimer += delta;
        if (moneyTimer >= 0.25f) {
            moneyGraph.next(game.getMoney() / 2000f);
            moneyTimer = 0;
        }
        packetGraph.next(network.getPackets() / 1500f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(getX() - 1, getY() - 1, getWidth() + 2, getHeight() + 2);
        shapeRenderer.setColor(getColor());
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
        batch.begin();
        super.draw(batch, parentAlpha);

    }

}
