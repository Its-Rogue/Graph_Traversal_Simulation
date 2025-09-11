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

public class Main extends ApplicationAdapter {
    ShapeRenderer sr;
    Stage menu;
    Graph graph;

    int node_radius = 30;
    float delta;

    Node n0,n1,n2;

    @Override
    public void create() {
        sr = new ShapeRenderer();

        graph = new Graph();
        n0 = new Node(node_radius, 0, 400,400, new ArrayList<>());
        n1 = new Node(node_radius, 1, 600,600, new ArrayList<>());
        graph.add_node(n0);
        graph.add_node(n1);
        graph.add_edge(n0, n1, 5);

        create_menu();
    }

    public void create_menu() {
        menu = new Stage();
        Table table = new Table();
        table.setFillParent(true);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton quit_button = new TextButton("Quit", skin);
        TextButton add_node_test = new TextButton("TEST_ADD_NODE_N2", skin);
        TextButton remove_node_test = new TextButton("TEST_REMOVE_NODE_N2", skin);
        quit_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
                System.exit(0);
            }
        });
        add_node_test.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(n2 == null){
                    n2 = new Node(node_radius, 2, 800, 500,  new ArrayList<>());
                    graph.add_node(n2);
                    graph.add_edge(n1, n2, 5);
                    graph.add_edge(n0, n2, 5);
                }
            }
        });
        remove_node_test.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (n2 != null) {
                    graph.remove_edge(n1, n2);
                    graph.remove_node(n2);
                    n2  = null;
                }
            }
        });
        table.add(quit_button).pad(5).row();
        table.add(add_node_test).pad(5).row();
        table.add(remove_node_test).pad(5).row();
        table.align(Align.topLeft);
        table.setPosition(25,0);
        menu.addActor(table);
    }

    @Override
    public void render() {
        Gdx.input.setInputProcessor(null);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);
        calculations();
        Inputs.all();

        sr.begin(ShapeRenderer.ShapeType.Filled);
            for(Node node: graph.get_nodes()){
                for(Edge edge: graph.get_edges(node)){
                    if(edge.getSource().getId() < edge.getTarget().getId()){
                        edge.render(sr);
                    }
                }
            }
            for(Node node: graph.get_nodes()){
                node.render(sr);
            }
        sr.end();

        draw_menu();
    }

    public void draw_menu() {
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
