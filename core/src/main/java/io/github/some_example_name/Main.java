package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    ShapeRenderer sr;
    Stage menu;
    Graph graph;
    BitmapFont font;
    SpriteBatch batch;

    Table table;
    Skin skin;
    TextButton quit_button;
    TextButton reset_button;
    TextButton start_traversal_button;
    TextButton recreate_test_elements_button;
    TextField traversal_speed_input;
    SelectBox<String> traversal_options;
    Label fps_counter;
    Label node_counter;
    Label edge_counter;

    final int node_radius = 30;
    float traversal_speed;
    float delta;

    String selected_traversal;

    @Override
    public void create() {
        // Initialise shape renderer and graph
        sr = new ShapeRenderer();
        graph = new Graph();
        font  = new BitmapFont();
        batch = new SpriteBatch();

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
        start_traversal_button = new TextButton("Start Traversal", skin);
        recreate_test_elements_button = new TextButton("Recreate Test Elements", skin);
        traversal_speed_input = new TextField("", skin);
        traversal_speed_input.setMessageText("Enter a speed (float)");
        traversal_speed_input.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter() {});

        fps_counter = new Label("FPS: ", skin);
        node_counter = new Label("Nodes: ", skin);
        edge_counter = new Label("Edges: ", skin);

        traversal_options = new SelectBox<>(skin);
        traversal_options.setItems("Breadth-First Search", "Depth-First Search", "Dijkstra's", "A*", "Minimum Spanning Tree");

        // Create the listeners for the UI button presses
        quit_button.addListener(new ChangeListener() { // Exit the program
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Exit the GDX program
                System.exit(0); // Exit the whole Java program
            }
        });

        reset_button.addListener(new ChangeListener() { // Clear the graph
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Graph.clear();
            }
        });

        start_traversal_button.addListener(new ChangeListener() { // Start the chosen traversal algorithm
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Inputs.start_traversal(selected_traversal, traversal_speed);
            }
        });

        recreate_test_elements_button.addListener(new ChangeListener() { // Create testing grid of nodes and edges
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Testing_Functions.create(graph, node_radius);
            }
        });

        traversal_speed_input.addListener(new ChangeListener() { // Get traversal speed from text input field
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                traversal_speed = Float.parseFloat(traversal_speed_input.getText());
                System.out.println(traversal_speed);
            }
        });

        traversal_options.addListener(new ChangeListener() { // Options for graph traversal
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selected_traversal = traversal_options.getSelected();
            }
        });

        // Add the buttons to the table
        table.add(quit_button).row();

        // Add the information labels to the table
        table.add(fps_counter).pad(5).row();
        table.add(node_counter).pad(5).row();
        table.add(edge_counter).pad(5).row();
        table.add(reset_button).pad(5).row();
        table.add(traversal_speed_input).pad(5).row();
        table.add(traversal_options).pad(5).row();
        table.add(start_traversal_button).pad(5).row();
        table.add(recreate_test_elements_button).pad(5).row();

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
        calculations(); // Perform necessary but ungroupable calculations each frame, such as delta time
        Inputs.all(graph, node_radius); // Perform all keyboard and mouse input processing

        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.WHITE);
            sr.rect(250,0,10,1440); // Margin for UI
            edge_render();
            node_render(); // Render after so they are placed on top of the edge lines
        sr.end();

        batch.begin();
            render_text(); // Render text after node and edges so it is drawn on top of these elements
        batch.end();

        draw_menu(); // Draw the components of the menu
    }

    public void edge_render(){
        for(Node node: graph.get_nodes()){ // Loop over each node in the graph
            for(Edge edge: graph.get_edges(node)){ // Loop over each edge that a node has
                if(edge.getSource().getId() < edge.getTarget().getId()){ // Check if the id is less than the one of the target node
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

    public void render_text(){
        for(Node node: graph.get_nodes()){
            font.setColor(Color.RED); // Increase legibility
            font.draw(batch, Integer.toString(node.getId()),node.getPos_x() - 5, node.getPos_y() + 5); // Draw at center of node
        }
    }

    public void calculations(){
        delta = Gdx.graphics.getDeltaTime(); // Calculate the time between the last frame and the current one
    }

    @Override
    public void dispose() {
        sr.dispose(); // Dispose of the shape renderer to free up resources
        menu.dispose(); // Dispose of the menu stage to free up resources
        font.dispose(); // Dispose of the font to free up resources
        batch.dispose(); // Dispose of the batch to free up resources
    }
}