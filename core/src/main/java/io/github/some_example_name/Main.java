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

public class Main extends ApplicationAdapter {
    ShapeRenderer sr;
    Stage menu;
    Graph graph;

    Table table;
    Skin skin;
    TextButton quit_button;
    TextButton add_node_test;
    TextButton remove_node_test;

    final int node_radius = 30;
    int mouse_x_pos;
    int mouse_y_pos;
    float delta;

    @Override
    public void create() {
        // Initialise shape renderer and graph
        sr = new ShapeRenderer();
        graph = new Graph();

        testing_functions.create(graph, node_radius); // Call out to create testing elements

        create_menu();
    }

    public void create_menu() {
        menu = new Stage();
        table = new Table();
        table.setFillParent(true);

        // Create all the different elements for the UI
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        quit_button = new TextButton("Quit", skin);

        // TEST BUTTONS
        add_node_test = new TextButton("TEST_ADD_NODE_N2", skin);
        remove_node_test = new TextButton("TEST_REMOVE_NODE_N2", skin);

        // Create the listeners for the UI button presses
        quit_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Exit the GDX program
                System.exit(0); // Exit the whole Java program
            }
        });

        // TEST BUTTONS
        add_node_test.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                testing_functions.create_button(graph, node_radius);
            }
        });
        remove_node_test.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                testing_functions.remove_button(graph);
            }
        });

        // Add the buttons to the table
        table.add(quit_button).pad(5).row();
        table.add(add_node_test).pad(5).row();
        table.add(remove_node_test).pad(5).row();

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
            for(Node node: graph.get_nodes()){ // Loop over each node in the graph
                for(Edge edge: graph.get_edges(node)){ // Loop over each edge that a node has
                    if(edge.getSource().getId() < edge.getTarget().getId()){ // Check if the id is less than the one in the target node
                        edge.render(sr); // Draw the edge
                    }
                }
                node.render(sr); // Draw each node after all its edges have been drawn
            }
        sr.end();
        draw_menu(); // Draw the components of the menu
    }

    public void draw_menu() {
        Gdx.input.setInputProcessor(menu); // Set the input processor to the menu, to check for mouse clicks on the buttons
        menu.act(delta); // Pass delta through to ensure all the actors animate in a synchronised matter
        menu.draw(); // Actually draw the menu
    }

    public void calculations(){
        delta = Gdx.graphics.getDeltaTime(); // Calculate the time between the last frame and the current one
        mouse_x_pos = Gdx.input.getX(); // Get the mouses x coordinate on the screen
        mouse_y_pos = Gdx.input.getY(); // Get the mouses y coordinate on the screen
    }

    @Override
    public void dispose() {
        sr.dispose(); // Dispose of the shape renderer to free up resources
        menu.dispose(); // Dispose of the menu stage to free up resources
    }
}
