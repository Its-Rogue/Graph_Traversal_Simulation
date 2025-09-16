package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    ShapeRenderer sr;
    Stage menu;
    Graph graph;

    Table table;
    Skin skin;
    TextButton quit_button;
    TextButton reset_button;
    Label fps_counter;
    Label node_counter;
    Label edge_counter;

    final int node_radius = 30;
    float delta;

    @Override
    public void create() {
        // Initialise shape renderer and graph
        sr = new ShapeRenderer();
        graph = new Graph();

        Testing_Functions.create(graph, node_radius); // Call out to testing function to create test nodes and edge

        create_menu();
    }

    public void create_menu() {
        menu = new Stage();
        table = new Table();
        table.setFillParent(true);

        // Create all the different elements for the UI
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        quit_button = new TextButton("Quit", skin);
        reset_button = new TextButton("Reset Graph", skin);
        fps_counter = new Label("FPS: ", skin);
        node_counter = new Label("Nodes: ", skin);
        edge_counter = new Label("Edges: ", skin);

        // Create the listeners for the UI button presses
        quit_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Exit the GDX program
                System.exit(0); // Exit the whole Java program
            }
        });

        reset_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                graph.clear();
            }
        });

        // Add the buttons to the table
        table.add(quit_button).row();

        // Add the information labels to the table
        table.add(fps_counter).pad(5).row();
        table.add(node_counter).pad(5).row();
        table.add(edge_counter).pad(5).row();
        table.add(reset_button).pad(10).row();

        // Align the UI to the top left and offset it so it does not render off the screen bounds
        table.align(Align.topLeft);
        table.setPosition(25,0);

        // Add the table, and subsequent buttons, to the menu stage
        menu.addActor(table);
    }

    @Override
    public void render() {
        Gdx.input.setInputProcessor(null); // Change the input processor back to the main simulation
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f); // Set the background colour to #000000
        calculations(); // Perform necessary but ungroupable calculations each frame, such as delta and mouse position
        Inputs.all(graph, node_radius); // Perform all keyboard input processing

        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.WHITE);
            sr.rect(250,0,10,1440); // Margin for UI
            edge_render();
            node_render(); // Render after so they are placed on top of the edge lines
        sr.end();

        draw_menu(); // Draw the components of the menu
    }

    public void edge_render(){
        for(Node node: graph.get_nodes()){ // Loop over each node in the graph
            for(Edge edge: graph.get_edges(node)){ // Loop over each edge that a node has
                if(edge.getSource().getId() < edge.getTarget().getId()){ // Check if the id is less than the one in the target node
                    edge.render(sr); // Draw the edge
                }
            }
        }
    }

    public void node_render(){
        for(Node node: graph.get_nodes()){ // Loop over each node in the graph
            node.render(sr); // Draw each node after all its edges have been drawn
        }
    }

    public void draw_menu() {
        fps_counter.setText("FPS: " + Gdx.graphics.getFramesPerSecond()); // FPS counter
        node_counter.setText("Nodes: " + graph.get_nodes().size()); // Number of nodes in scene
        edge_counter.setText("Edges: " + graph.get_total_edges()); // Number of edges in scene
        Gdx.input.setInputProcessor(menu); // Set the input processor to the menu, to check for mouse clicks on the buttons
        menu.act(delta); // Pass delta through to ensure all the actors animate in a synchronised matter
        menu.draw(); // Actually draw the menu
    }

    public void calculations(){
        delta = Gdx.graphics.getDeltaTime(); // Calculate the time between the last frame and the current one
    }

    @Override
    public void dispose() {
        sr.dispose(); // Dispose of the shape renderer to free up resources
        menu.dispose(); // Dispose of the menu stage to free up resources
    }
}
