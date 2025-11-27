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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import structural_classes.Node;
import structural_classes.Edge;
import helper_classes.UI;
import helper_classes.Inputs;

public class Main extends ApplicationAdapter {
    Runtime_Data data; // Class that holds variables / structures that need to be accessed throughout the entire program

    ShapeRenderer sr; // Shapes and text
    BitmapFont font;
    SpriteBatch batch;
    GlyphLayout layout;

    Stage GUI; // Scene2D elements
    Table table;

    TextButton quit_button;
    TextButton reset_button;
    TextButton reset_traversal_button;
    TextButton start_traversal_button;
    TextButton recreate_test_elements_button;

    Label fps_counter;
    Label node_counter;
    Label edge_counter;

    float frame_delta;

    @Override
    public void create() {
        // Initialise elements
        data =  new Runtime_Data();
        sr = new ShapeRenderer();
        font  = new BitmapFont();
        batch = new SpriteBatch();
        layout = new GlyphLayout();

        create_GUI();
    }

    public void create_GUI() {
        GUI = new Stage();
        table = new Table();

        table.setFillParent(true);
        data.getError_popup().setFillParent(true);
        data.getError_popup().setVisible(false);
        data.getChange_edge_weight_popup().setVisible(false);

        // Create all the different elements for the UI

        quit_button = new TextButton("Quit", data.getSkin());
        reset_button = new TextButton("Reset Graph", data.getSkin());
        start_traversal_button = new TextButton("Start Traversal", data.getSkin());
        reset_traversal_button = new TextButton("Reset Traversal", data.getSkin());
        recreate_test_elements_button = new TextButton("Recreate Test Elements", data.getSkin());

        data.getTraversal_speed_slider().setValue(data.getTraversal_speed());
        data.getStart_node_input().setMessageText("Enter the start node");
        data.getEnd_node_input().setMessageText("Enter the end node");
        data.getChange_edge_weight_input().setMessageText("Input new weight");
        data.getStart_node_input().setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter() {});
        data.getEnd_node_input().setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter() {});
        data.getChange_edge_weight_input().setTextFieldFilter((text_field, c) -> Character.isDigit(c));

        fps_counter = new Label("FPS: ", data.getSkin());
        node_counter = new Label("Nodes: ", data.getSkin());
        edge_counter = new Label("Edges: ", data.getSkin());
        data.getError_popup_label().setColor(1,0,0,1); // Set colour to red

        data.getTraversal_options().setItems("Breadth-First Search", "Depth-First Search", "Bidirectional Search", "Dijkstra's", "A*", "Bellman-Ford");

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
                UI.reset_button_function(data);
            }
        });

        // Button to start the chosen traversal algorithm
        start_traversal_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.start_traversal_button_function(data);
            }
        });

        // Reset the colours of the previous traversal
        reset_traversal_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.reset_traversal_button_function(data);
            }
        });

        // Create testing grid of nodes and edges to test rendering
        recreate_test_elements_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.recreate_test_elements_button_function(data);
            }
        });

        // Get traversal speed from text input field
        data.getTraversal_speed_slider().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.traversal_speed_input_function(data);
            }
        });

        // Text field input for the user's desired start node
        data.getStart_node_input().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.start_node_input_function(data);
            }
        });

        // Text field input for the user's desired end node
        data.getEnd_node_input().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.end_node_input_function(data);
            }
        });

        // Text field input for changing the weight of the user's chosen edge
        data.getChange_edge_weight_input().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.change_edge_weight_input_function(data);
            }
        });

        // Drop down GUI options for graph traversal algorithm
        data.getTraversal_options().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UI.traversal_options_function(data);
            }
        });

        // Add the various actors to the tables
        table.add(quit_button).row();
        table.add(fps_counter).pad(5).row();
        table.add(node_counter).pad(5).row();
        table.add(edge_counter).pad(5).row();
        table.add(reset_button).pad(5).row();
        table.add(data.getTraversal_speed_slider()).pad(5).row();
        table.add(data.getTraversal_speed_label()).pad(5).row();
        table.add(data.getTraversal_options()).pad(5).row();
        table.add(data.getStart_node_input()).pad(5).row();
        table.add(data.getEnd_node_input()).pad(5).row();
        table.add(start_traversal_button).pad(5).row();
        table.add(reset_traversal_button).pad(5).row();

        // Testing elements
        table.add(recreate_test_elements_button).pad(5).row();

        // Error message label
        data.getError_popup().add(data.getError_popup_label());

        // Change edge weight label
        data.getChange_edge_weight_popup().add(data.getChange_edge_weight_input()).row();
        data.getChange_edge_weight_popup().add(data.getChange_edge_weight_label()).pad(2).row();

        // Align the UI to the top left and offset it so it does not render off the screen bounds
        table.align(Align.topLeft);
        data.getError_popup().align(Align.topLeft);
        table.setPosition(15,0);
        data.getError_popup().setPosition(15,-600);

        // Add the table, and subsequent buttons, to the GUI stage
        GUI.addActor(table);
        GUI.addActor(data.getError_popup());
        GUI.addActor(data.getChange_edge_weight_popup());
    }

    // Render loop
    @Override
    public void render() {
        Gdx.input.setInputProcessor(null); // Change the input processor back to the main simulation, and not a Scene2D element
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f); // Set the background colour to #000000 (Black)
        calculations(); // Perform necessary but ungroupable calculations each frame, such as delta time
        Inputs.all(data); // Perform all keyboard and mouse input processing

        sr.begin(ShapeRenderer.ShapeType.Filled);
            edge_render();
            node_render(); // Render the nodes after the edges so that they are drawn on top of the edge lines
            colour_key_render();
            shape_render(); // Render simple shapes provided by LibGDX
        sr.end();

        batch.begin();
            render_text(); // Render text after node and edges so it is drawn on top of these elements
        batch.end();

        draw_GUI(); // Draw the components of the GUI
    }

    // Shape render loop
    public void shape_render(){
        sr.setColor(Color.WHITE);
        sr.rect(250,0,10,1440); // Margin for UI

        if (data.getChange_edge_weight_popup().isVisible()){ // Add a rectangle behind the popup to increase the legibility of the text hint
            sr.setColor(Color.RED); // Border shape
            sr.rect(data.getChange_edge_weight_popup().getX() - 127,data.getChange_edge_weight_popup().getY() - 24,252,52);
            sr.setColor(Color.BLACK); // Background shape
            sr.rect(data.getChange_edge_weight_popup().getX() - 126 ,data.getChange_edge_weight_popup().getY() - 23,250,50);
        }
    }

    // Edge render loop
    public void edge_render(){
        for (Node node: data.getGraph().get_nodes()){ // Loop over each node in the graph
            for (Edge edge: data.getGraph().get_edges(node)){ // Loop over each edge that a node has
                if (edge.getSource().getId() < edge.getTarget().getId()){ // Check if the id is less than the one of the target node
                    edge.render(sr); // Draw the edge one time, rather than for both directions of the bidirectional edge
                }
            }
        }
    }

    // Node render loop
    public void node_render(){
        for (Node node: data.getGraph().get_nodes()){ // Loop over each node in the graph
            node.render(sr); // Draw each node after all its edges have been drawn
        }
    }

    public void colour_key_render(){
        sr.rect(10,25, 25, 25, Color.PURPLE, Color.PURPLE, Color.PURPLE, Color.PURPLE);  // Visited node / edge
        sr.rect(10,60, 25, 25, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW);  // Discovered node / edge
        sr.rect(10,95, 25, 25, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);          // Explored node / edge
        sr.rect(10,130, 25, 25, Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE); // Current node / edge
        sr.rect(10,165, 25, 25, Color.RED, Color.RED, Color.RED, Color.RED);             // End node / edge
        sr.rect(10,200, 25, 25, Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);     // Start node / edge
        sr.rect(10,228, 70, 2, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);      // Header underlining
    }

    // GUI render
    public void draw_GUI() {
        fps_counter.setText("FPS: " + Gdx.graphics.getFramesPerSecond()); // FPS counter
        node_counter.setText("Nodes: " + data.getGraph().get_nodes().size()); // Number of nodes in scene
        edge_counter.setText("Edges: " + data.getGraph().get_total_edges()); // Number of edges in scene
        Gdx.input.setInputProcessor(GUI); // Set the input processor to the GUI, to check for mouse clicks on the buttons
        GUI.act(frame_delta); // Pass delta through to ensure all the actors animate in a synchronised matter
        GUI.draw(); // Actually draw the GUI
    }

    // Edge weight & colour key render loop
    public void render_text(){
        // Colour key code
        font.setColor(Color.WHITE);
        font.draw(batch, "Colour Key", 10, 244);            // Header
        font.draw(batch, "Start node", 40, 219);            // Green
        font.draw(batch, "End node", 40, 184);              // Red
        font.draw(batch, "Visited node", 40, 149);          // Orange
        font.draw(batch, "Current node", 40, 114);          // Cyan
        font.draw(batch, "Discovered node", 40, 79);        // Yellow
        font.draw(batch, "Fully explored node", 40, 44);    // Purple

        // Edge weight code
        for (Node node: data.getGraph().get_nodes()){
            if (node.getColour() == Color.PURPLE || node.getColour() == Color.RED){
                font.setColor(Color.WHITE); // Increase legibility on darker colours
            } else {
                font.setColor(Color.BLACK);
            }

            String text = Integer.toString(node.getId()); // Get node ID and cast to a string
            layout.setText(font, text);
            font.draw(batch, text, node.getPosition().getX() - layout.width / 2, node.getPosition().getY() + layout.height / 2);

            // Draw edge weights centered on edges, offset for legibility
            if (!(data.getSelected_traversal().equals("Breadth-First Search") || data.getSelected_traversal().equals("Depth-First Search") ||
                data.getSelected_traversal().equals("Bidirectional Search"))){
                for (Edge edge: data.getGraph().get_edges(node)){
                    if (edge.getDirection().equals("reverse")){
                        continue;
                    }
                    font.setColor(Color.WHITE);
                    float midpoint_x = (edge.getSource().getPosition().getX() + edge.getTarget().getPosition().getX()) / 2f;
                    float midpoint_y = (edge.getSource().getPosition().getY() + edge.getTarget().getPosition().getY()) / 2f;
                    float[] offsets = calculate_offsets(edge);
                    font.draw(batch, Integer.toString(edge.getWeight()), (midpoint_x - layout.width / 2) + offsets[0], (midpoint_y + layout.height / 2) +  offsets[1]);
                }
            }
        }
    }

    public float[] calculate_offsets(Edge edge){
        float offset_x = 0,  offset_y = 0;

        if (edge.getSource().getPosition().getX() == edge.getTarget().getPosition().getX()){
            offset_x = -15;
            return new float[]{offset_x, offset_y};
        }

        if (edge.getSource().getPosition().getY() == edge.getTarget().getPosition().getY()){
            offset_y = 10;
            return new float[]{offset_x, offset_y};
        }

        if ((edge.getSource().getPosition().getY() < edge.getTarget().getPosition().getY())){
            if (edge.getSource().getPosition().getX() < edge.getTarget().getPosition().getX()){
                offset_x = -15;
            } else {
                offset_x = 15;
            }

        } else {
            if (edge.getSource().getPosition().getX() < edge.getTarget().getPosition().getX()){
                offset_x = 15;
            } else {
                offset_x = -15;
            }

        }
        offset_y = 10;
        return new float[]{offset_x, offset_y}; // For each instance, return an array with the 2 offsets, rather than having 2 identical functions for x / y
    }

    // Other uncategorisable calculations that need to happen every frame
    public void calculations(){
        frame_delta = Gdx.graphics.getDeltaTime(); // Calculate the time between the last frame and the current one
    }

    // Dispose of unneeded elements
    @Override
    public void dispose() {
        sr.dispose(); // Dispose of the shape renderer to free up resources
        GUI.dispose(); // Dispose of the GUI stage to free up resources
        font.dispose(); // Dispose of the font to free up resources
        batch.dispose(); // Dispose of the batch to free up resources
    }
}