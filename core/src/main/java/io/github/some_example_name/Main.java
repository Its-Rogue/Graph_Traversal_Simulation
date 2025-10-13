package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    GlyphLayout layout;

    Table table;
    Table popup;
    Skin skin;
    TextButton quit_button;
    TextButton reset_button;
    TextButton reset_traversal_button;
    TextButton start_traversal_button;
    TextButton recreate_test_elements_button;
    TextField traversal_speed_input;
    TextField start_node_input;
    TextField end_node_input;
    SelectBox<String> traversal_options;
    Label fps_counter;
    Label node_counter;
    Label edge_counter;
    Label popup_label;
    Label test_popup_label;

    final int node_radius = 30;
    int start_node;
    int end_node;
    float traversal_speed;
    float delta;

    boolean valid_setup = false;
    String selected_traversal = "Breadth-First Search";

    @Override
    public void create() {
        // Initialise elements
        sr = new ShapeRenderer();
        graph = new Graph();
        font  = new BitmapFont();
        batch = new SpriteBatch();
        layout = new GlyphLayout();

        create_menu();
    }

    public void create_menu() {
        menu = new Stage();
        table = new Table();
        popup = new Table();

        table.setFillParent(true);
        popup.setFillParent(true);
        popup.setVisible(false);

        // Create all the different elements for the UI
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        quit_button = new TextButton("Quit", skin);
        reset_button = new TextButton("Reset Graph", skin);
        start_traversal_button = new TextButton("Start Traversal", skin);
        reset_traversal_button = new TextButton("Reset Traversal", skin);
        recreate_test_elements_button = new TextButton("Recreate Test Elements", skin);

        start_node_input = new TextField("", skin);
        end_node_input = new TextField("", skin);
        traversal_speed_input = new TextField("", skin);
        start_node_input.setMessageText("Enter the start node");
        end_node_input.setMessageText("Enter the end node");
        traversal_speed_input.setMessageText("Enter a speed (float)");
        start_node_input.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter() {});
        end_node_input.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter() {});
        traversal_speed_input.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter() {});

        fps_counter = new Label("FPS: ", skin);
        node_counter = new Label("Nodes: ", skin);
        edge_counter = new Label("Edges: ", skin);
        popup_label = new Label("", skin);
        popup_label.setColor(1,0,0,1);
        test_popup_label = new Label("", skin);

        traversal_options = new SelectBox<>(skin);
        traversal_options.setItems("Breadth-First Search", "Depth-First Search", "Dijkstra's", "A*", "Minimum Spanning Tree");

        // Create the listeners for the UI button presses

        // Button to exit the program
        quit_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Exit the GDX program
                System.exit(0); // Exit the whole Java program
            }
        });

        // Button to clear the graph
        reset_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Graph.clear();
                start_node = 0;
                end_node = 0;
            }
        });

        // Button to start the chosen traversal algorithm
        start_traversal_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(start_node == end_node){
                    popup_label.setText("Start node cannot be the same \nas the end node");
                    valid_setup = false;
                    return;
                }
                if(valid_setup){
                    Inputs.start_traversal(graph, selected_traversal, traversal_speed, start_node, end_node);
                }
            }
        });

        reset_traversal_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (Node node: graph.get_nodes()){
                    if (node.getColor() != Color.WHITE){
                        node.setColor(Color.WHITE);
                    }
                }
            }
        });

        // Create testing grid of nodes and edges to test rendering
        recreate_test_elements_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Testing_Functions.create(graph, node_radius);
            }
        });

        // Get traversal speed from text input field
        traversal_speed_input.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int user_traversal_speed;
                try{
                    user_traversal_speed = (int) Float.parseFloat(traversal_speed_input.getText());
                } catch (Exception e){
                    return;
                }
                if(user_traversal_speed < 0){
                    popup_label.setText("Traversal Speed must be greater or \nequal to zero");
                    valid_setup = false;
                    return;
                } else if (user_traversal_speed > 1000){
                    popup_label.setText("Traversal Speed must be less or \nequal to 1000");
                    valid_setup = false;
                    return;
                }
                popup_label.setText(""); // Clear errors if valid
                valid_setup = true;
                traversal_speed = user_traversal_speed;
            }
        });

        // Text field input for the user's desired start node
        start_node_input.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                popup.setVisible(true);
                try {
                    int user_start_node = Integer.parseInt(start_node_input.getText());

                    if (user_start_node == end_node) {
                        popup_label.setText("Start node cannot be the same \nas the end node");
                        valid_setup = false;
                        return;
                    }
                    if (graph.get_node_id(user_start_node) == null) {
                        popup_label.setText("Desired start node does not exist");
                        valid_setup = false;
                        return;
                    }

                    start_node = user_start_node;
                    popup_label.setText(""); // Clear errors if valid
                    valid_setup = true;
                    test_popup_label.setText("Start node: " + start_node + " End node: " + end_node);
                } catch (NumberFormatException e) {
                    popup_label.setText("Invalid start node input:\ninput must be a integer");
                    valid_setup = false;
                }
            }
        });

        // Text field input for the user's desired end node
        end_node_input.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                popup.setVisible(true);
                try {
                    int user_end_node = Integer.parseInt(end_node_input.getText());

                    if (user_end_node == start_node) {
                        popup_label.setText("End node cannot be the same \nas the start node");
                        valid_setup = false;
                        return;
                    }
                    if (graph.get_node_id(user_end_node) == null) {
                        popup_label.setText("Desired end node does not exist");
                        valid_setup = false;
                        return;
                    }

                    end_node = user_end_node;
                    popup_label.setText(""); // Clear errors if valid
                    valid_setup = true;
                    test_popup_label.setText("Start node: " + start_node + " End node: " + end_node);
                } catch (NumberFormatException e) {
                    popup_label.setText("Invalid end node input:\ninput must be a integer");
                    valid_setup = false;
                }
            }
        });

        traversal_options.addListener(new ChangeListener() { // Options for graph traversal
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selected_traversal = traversal_options.getSelected();
            }
        });

        // Add the various actors to the tables
        table.add(quit_button).row();
        table.add(fps_counter).pad(5).row();
        table.add(node_counter).pad(5).row();
        table.add(edge_counter).pad(5).row();
        table.add(reset_button).pad(5).row();
        table.add(traversal_speed_input).pad(5).row();
        table.add(traversal_options).pad(5).row();
        table.add(start_node_input).pad(5).row();
        table.add(end_node_input).pad(5).row();
        table.add(start_traversal_button).pad(5).row();
        table.add(reset_traversal_button).pad(5).row();

        // Testing elements
        table.add(recreate_test_elements_button).pad(5).row();
        table.add(test_popup_label).pad(5).row();

        popup.add(popup_label);

        // Align the UI to the top left and offset it so it does not render off the screen bounds
        table.align(Align.topLeft);
        popup.align(Align.topLeft);
        table.setPosition(15,0);
        popup.setPosition(15,-600);

        // Add the table, and subsequent buttons, to the menu stage
        menu.addActor(table);
        menu.addActor(popup);
    }

    // Render loop
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

    // Edge render loop
    public void edge_render(){
        for(Node node: graph.get_nodes()){ // Loop over each node in the graph
            for(Edge edge: graph.get_edges(node)){ // Loop over each edge that a node has
                if(edge.getSource().getId() < edge.getTarget().getId()){ // Check if the id is less than the one of the target node
                    edge.render(sr); // Draw the edge
                }
            }
        }
    }

    // Node render loop
    public void node_render(){
        for(Node node: graph.get_nodes()){ // Loop over each node in the graph
            node.render(sr); // Draw each node after all its edges have been drawn
        }
    }

    // Menu render
    public void draw_menu() {
        fps_counter.setText("FPS: " + Gdx.graphics.getFramesPerSecond()); // FPS counter
        node_counter.setText("Nodes: " + graph.get_nodes().size()); // Number of nodes in scene
        edge_counter.setText("Edges: " + graph.get_total_edges()); // Number of edges in scene
        Gdx.input.setInputProcessor(menu); // Set the input processor to the menu, to check for mouse clicks on the buttons
        menu.act(delta); // Pass delta through to ensure all the actors animate in a synchronised matter
        menu.draw(); // Actually draw the menu
    }

    // Edge weight render loop
    public void render_text(){
        for(Node node: graph.get_nodes()){
            if(node.getColor() == Color.PURPLE){
                font.setColor(Color.WHITE);
            } else {
                font.setColor(Color.BLACK);
            }
            String text = Integer.toString(node.getId());
            layout.setText(font, text);
            font.draw(batch, text, node.getPos_x() - layout.width / 2, node.getPos_y() + layout.height / 2);

            // Draw edge weights centered on edges
            for(Edge edge: graph.get_edges(node)){
                font.setColor(Color.WHITE);
                float midpoint_x = (edge.getSource().getPos_x() + edge.getTarget().getPos_x()) / 2f;
                float midpoint_y = (edge.getSource().getPos_y() + edge.getTarget().getPos_y()) / 2f;
                font.draw(batch, Integer.toString(edge.getWeight()), midpoint_x - layout.width / 2, midpoint_y + layout.height / 2);
            }
        }
    }

    // Other uncategorisable calculations that need to happen every frame
    public void calculations(){
        delta = Gdx.graphics.getDeltaTime(); // Calculate the time between the last frame and the current one
        /*System.out.println(); // Check the neighbours of every node each frame, TESTING ELEMENT TODO: REMOVE WHEN NECESSARY
        for (Node n: graph.get_nodes()) {
            if(n != null){
                System.out.print(n.getId() + ": ");
                for(Node n2: n.getNeighbours()){
                    System.out.print(n2.getId() + " ");
                } System.out.println();
            }
        }*/
    }

    // Dispose of unneeded elements
    @Override
    public void dispose() {
        sr.dispose(); // Dispose of the shape renderer to free up resources
        menu.dispose(); // Dispose of the menu stage to free up resources
        font.dispose(); // Dispose of the font to free up resources
        batch.dispose(); // Dispose of the batch to free up resources
    }
}