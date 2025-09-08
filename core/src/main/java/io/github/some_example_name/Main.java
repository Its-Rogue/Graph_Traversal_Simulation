package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    ShapeRenderer sr;
    Stage menu;

    List<Node> nodes;
    List<Edge> edges;

    int node_radius = 30;
    float delta;

    @Override
    public void create() {
        sr = new ShapeRenderer();
        create_menu();

        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();

        nodes.add(new Node(node_radius, 0,400,400, edges));
        nodes.add(new Node(node_radius, 1,500,200, edges));
        nodes.add(new Node(node_radius, 2,300,400, edges));
        nodes.add(new Node(node_radius, 3,600,800, edges));
        nodes.add(new Node(node_radius, 4,800,400, edges));
        edges.add(new Edge(nodes.get(0), nodes.get(1), 0));
        edges.add(new Edge(nodes.get(1), nodes.get(2), 0));
        edges.add(new Edge(nodes.get(2), nodes.get(3), 0));
        edges.add(new Edge(nodes.get(0), nodes.get(3), 0));
        edges.add(new Edge(nodes.get(3), nodes.get(4), 0));
    }

    public void create_menu() {
        menu = new Stage();
        Table table = new Table();
        table.setFillParent(true);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton quit_button = new TextButton("Quit", skin);
        quit_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
                System.exit(0);
            }
        });
        table.add(quit_button).pad(5).row();
        table.align(Align.topLeft);
        menu.addActor(table);
    }

    @Override
    public void render() {
        Gdx.input.setInputProcessor(null);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);
        calculations();
        Inputs.all();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        for(Edge edge : edges){
            edge.render(sr);
        }
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
            for(Node node : nodes){
                node.render(sr);
            }
        sr.end();



        Gdx.input.setInputProcessor(menu);
        menu.act(delta);
        menu.draw();
    }

    public void calculations(){
        delta = Gdx.graphics.getDeltaTime();
    }

    @Override
    public void dispose() {
        sr.dispose();
        menu.dispose();
    }
}
