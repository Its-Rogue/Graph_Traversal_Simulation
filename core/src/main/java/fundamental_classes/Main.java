package fundamental_classes;

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
import helper_classes.*;

public class Main extends ApplicationAdapter {
    ShapeRenderer sr;
    Stage menu;
    public Graph graph;
    BitmapFont font;
    SpriteBatch batch;
    GlyphLayout layout;

    Table table;
    public Table popup;
    Skin skin;
    TextButton quit_button;
    TextButton reset_button;
    TextButton reset_traversal_button;
    TextButton start_traversal_button;
    TextButton recreate_test_elements_button;
    public TextField start_node_input;
    public TextField end_node_input;
    public Slider traversal_speed_slider;
    public SelectBox<String> traversal_options;
    Label fps_counter;
    Label node_counter;
    Label edge_counter;
    public Label popup_label;
    public Label traversal_speed_label;

    public final int node_radius = 30;
    public int start_node;
    public int end_node;
    public float traversal_speed = 1;
    float delta;

    public boolean valid_setup = false;
    public String selected_traversal = "Breadth-First Search";

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
        traversal_speed_slider = new Slider(1,20,1,false, skin);
        start_node_input.setMessageText("Enter the start node");
        end_node_input.setMessageText("Enter the end node");
        start_node_input.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter() {});
        end_node_input.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter() {});

        fps_counter = new Label("FPS: ", skin);
        node_counter = new Label("Nodes: ", skin);
        edge_counter = new Label("Edges: ", skin);
        popup_label = new Label("", skin);
        popup_label.setColor(1,0,0,1);
        traversal_speed_label = new Label("Traversal Speed: " + (int) traversal_speed_slider.getValue(), skin);

        traversal_options = new SelectBox<>(skin);
        traversal_options.setItems("Breadth-First Search", "Depth-First Search", "Dijkstra's", "A*", "Minimum Spanning Tree");

        // Create the listeners for the UI button presses

        // Button to exit the program
        quit_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.quit_button_function();
            }
        });

        // Button to clear the graph
        reset_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.reset_button_function(Main.this);
            }
        });

        // Button to start the chosen traversal algorithm
        start_traversal_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.start_traversal_button_function(Main.this);
            }
        });

        // Reset the colours of the previous traversal
        reset_traversal_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.reset_traversal_button_function(Main.this);
            }
        });

        // Create testing grid of nodes and edges to test rendering
        recreate_test_elements_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.recreate_test_elements_button_function(Main.this);
            }
        });

        // Get traversal speed from text input field
        traversal_speed_slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.traversal_speed_input_function(Main.this);
            }
        });

        // Text field input for the user's desired start node
        start_node_input.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.start_node_input_function(Main.this);
            }
        });

        // Text field input for the user's desired end node
        end_node_input.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.end_node_input_function(Main.this);
            }
        });

        // Options for graph traversal
        traversal_options.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.traversal_options_function(Main.this);
            }
        });

        // Add the various actors to the tables
        table.add(quit_button).row();
        table.add(fps_counter).pad(5).row();
        table.add(node_counter).pad(5).row();
        table.add(edge_counter).pad(5).row();
        table.add(reset_button).pad(5).row();
        table.add(traversal_speed_slider).pad(5).row();
        table.add(traversal_speed_label).pad(5).row();
        table.add(traversal_options).pad(5).row();
        table.add(start_node_input).pad(5).row();
        table.add(end_node_input).pad(5).row();
        table.add(start_traversal_button).pad(5).row();
        table.add(reset_traversal_button).pad(5).row();

        // Testing elements
        table.add(recreate_test_elements_button).pad(5).row();

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
            font.draw(batch, text, node.getPosition().x - layout.width / 2, node.getPosition().y + layout.height / 2);

            // Draw edge weights centered on edges
            for(Edge edge: graph.get_edges(node)){
                font.setColor(Color.WHITE);
                float midpoint_x = (edge.getSource().getPosition().x + edge.getTarget().getPosition().x) / 2f;
                float midpoint_y = (edge.getSource().getPosition().y + edge.getTarget().getPosition().y) / 2f;
                font.draw(batch, Integer.toString(edge.getWeight()), midpoint_x - layout.width / 2, midpoint_y + layout.height / 2);
            }
        }
    }

    // Other uncategorisable calculations that need to happen every frame
    public void calculations(){
        delta = Gdx.graphics.getDeltaTime(); // Calculate the time between the last frame and the current one
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