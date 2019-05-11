package com.janfic.game.networksproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.janfic.game.networksproject.game.DGSMatch;
import com.janfic.game.networksproject.game.Game;
import com.janfic.game.networksproject.game.LANMatch;
import com.janfic.game.networksproject.game.MatchMaker;
import com.janfic.game.networksproject.game.P2PMatch;
import com.janfic.game.networksproject.network.Computer;
import com.janfic.game.networksproject.network.Network;
import com.janfic.game.networksproject.network.NetworkEdge;
import com.janfic.game.networksproject.network.NetworkNode;
import com.janfic.game.networksproject.network.Router;
import com.janfic.game.networksproject.network.Server;
import com.janfic.game.networksproject.network.generators.CenteredNetworkGenerator;
import com.janfic.game.networksproject.network.generators.NetworkGenerator;

/**
 *
 * @author Jan Fic
 */
public class PlayScreen extends Stage implements Screen {

    private NetworkGenerator generator;
    private Network network;
    private MatchMaker matchMaker;
    private Game game;

    private SidePanel panel;

    private ShapeRenderer shapeRenderer;
    //private SpriteBatch spriteBatch;

    private Viewport viewport;
    private OrthographicCamera camera;

    @Override
    public void show() {

        shapeRenderer = new ShapeRenderer();
        //spriteBatch = new SpriteBatch();

        generator = new CenteredNetworkGenerator(60, 5, shapeRenderer);
        network = generator.create(800, 800);
        matchMaker = new MatchMaker(network, shapeRenderer);
        game = new Game("YOUR GAME", "Sandbox", "Pixel", 5, 30f, 10, new boolean[]{false, false, false}, network, matchMaker);

        panel = new SidePanel(800, 0, network, matchMaker, shapeRenderer, game);

        camera = new OrthographicCamera(800, 800);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        viewport = new ExtendViewport(800, 800, camera);

        for (NetworkNode node : network.getNodes()) {
            addActor(node);
            node.setZIndex(getActors().size - 1);
            for (NetworkEdge connection : node.getConnections()) {
                addActor(connection);
                connection.setZIndex(0);
            }
        }

        Gdx.input.setInputProcessor(this);

        panel.setColor(new Color(0.95f, 0.95f, .96f, 1));

        addActor(panel);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);

        if (Math.random() < game.getDemand() / 2) {
            NetworkNode node = generator.addNode(network, Math.random() < game.getDemand() ? Computer.class : Router.class);
            if (node != null) {
                if (node instanceof Computer && !(node instanceof Server)) {
                    game.addMoney(15);
                }
                addActor(node);
                node.setZIndex(getActors().size - 1);
                for (NetworkEdge connection : node.getConnections()) {
                    addActor(connection);
                    connection.setZIndex(0);
                }
            }
        }
        for (int i = network.getNumberOfNodes() - 1; i >= 0; i--) {
            NetworkNode node = network.getNode(i);
            if (node instanceof Router && node.getConnectedNodes().size() <= 1) {
                if (((Router) node).getTime() >= 30) {
                    network.removeNode(node);
                }
            }
        }

        if (Math.random() < game.getDemand() && game.getSupportedTech()[2]) {
            DGSMatch dgs = matchMaker.makeDGS(game.getMaxPlayers(), game.getMatchLength(), 10);
            if (dgs != null) {
                this.addActor(dgs);
                game.addMoney(dgs.getNumberOfPlayers() * 0.5f);
                dgs.setZIndex(getActors().size - 1);
            }
        }
        if (Math.random() < game.getDemand() && game.getSupportedTech()[0]) {
            LANMatch lan = matchMaker.makeLAN(game.getMaxPlayers(), game.getMatchLength(), 5);
            if (lan != null) {
                this.addActor(lan);
                game.addMoney(lan.getNumberOfPlayers() * 0.5f);
                lan.setZIndex(getActors().size - 1);
            }
        }
        if (Math.random() < game.getDemand() && game.getSupportedTech()[1]) {
            P2PMatch p2p = matchMaker.makeP2P(game.getMaxPlayers(), game.getMatchLength(), 0);
            if (p2p != null) {
                this.addActor(p2p);
                game.addMoney(p2p.getNumberOfPlayers() * 0.5f);
                p2p.setZIndex(getActors().size - 1);
            }
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.S) && game.getSupportedTech()[2] && game.getMoney() >= 500) {
            NetworkNode server = generator.addNode(network, Server.class, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            if (server != null) {
                addActor(server);
                game.addMoney(-500);
                server.setZIndex(getActors().size - 1);
                for (NetworkEdge connection : server.getConnections()) {
                    addActor(connection);
                    connection.setZIndex(0);
                }
            }
        }

        act(delta);
        draw();
        matchMaker.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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

    public SidePanel getPanel() {
        return panel;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        //spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
