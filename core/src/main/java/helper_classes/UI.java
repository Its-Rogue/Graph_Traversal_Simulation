package helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import structural_classes.Edge;
import structural_classes.Node;
import essential_classes.Runtime_Data;

public class UI {
    public static void quit_button_function() {
        Gdx.app.exit();
        System.exit(0); // Exit the whole Java program
    }

    public static void save_current_layout_button_function(Runtime_Data data) {
        Save_Handling.save_current_layout(data); // Save the layout of nodes and edges in the current graph
    }

    public static void load_saved_layout_button_function(Runtime_Data data) {
        Save_Handling.load_saved_layout(data); // Load a previously saved layout, opening a file browser window to select the file
        clear_user_nodes(data);
    }

    public static void reset_button_function(Runtime_Data data) {
        data.getGraph().clear(); // Clear the adj list of the graph
        data.setStart_node(0); // Reset chosen start and end node
        data.setEnd_node(0);
        data.setTraversal_canceled(false); // Reset status flags for authorising traversal start
        data.setTraversal_in_progress(false);
        Inputs.clear_selected_node(); // Clear to keep consistent functionality
        clear_user_nodes(data);
    }

    public static void start_traversal_button_function(Runtime_Data data) {
        if (data.isTraversal_in_progress()) {
            data.getError_popup().setVisible(true); // Make sure the user can only run 1 traversal at a time
            data.getError_popup_label().setText("Traversal is already running");
            return;
        }

        if (data.getStart_node() == data.getEnd_node()) { // Make sure inputted nodes are unique
            data.getError_popup_label().setText("Start node cannot be the same \nas the end node");
            data.setValid_setup(false);
            return;
        }

        if (data.isValid_setup()) {
            reset_colours(data); // Ensure the nodes are all white in the graph
            data.setTraversal_canceled(false); // Reset status to make sure the traversal doesn't instantly stop
            Inputs.start_traversal(data); // Start traversal
        }
    }

    public static void step_traversal_button_function(Runtime_Data data) {
        if (!data.isTraversal_in_progress()) { // Check to see if any traversal is currently running to be stepped
            data.getError_popup_label().setText("No traversal to step");
            data.getError_popup().setVisible(true);
            return;
        }

        if (data.isStep_button_pressed()) { // Only allow 1 step per stepping frame
            data.getError_popup().setVisible(true);
            data.getError_popup_label().setText("Already stepping traversal");
            return;
        }

        data.setStep_button_pressed(true);
    }

    public static void reset_traversal_button_function(Runtime_Data data) {
        data.setTraversal_canceled(true); // Stop the current traversal from running
        data.setTraversal_in_progress(false); // Allow a new traversal to be run
        data.getError_popup_label().setText("");
        data.getError_popup().setVisible(false);
        clear_user_nodes(data);

        try {
            Thread.sleep(100);
            reset_colours(data); // Reset colours of nodes in graph
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reset_colours(Runtime_Data data) {
        for (Node node: data.getGraph().get_nodes()) {
            node.setColour(Color.WHITE); // Reset all nodes and edges to be white
            for (Edge edge: data.getGraph().get_edges(node)) {
                edge.setColour(Color.WHITE);
            }
        }
    }

    public static void generate_grid_button_function(Runtime_Data data) {
        Generate_Graphs.generate_grid(data); // Generate a predetermined 10 * 10 grid where the 2-4 nodes adjacent are neighbours
        clear_user_nodes(data);
    }

    public static void generate_random_graph_button_function(Runtime_Data data) {
        Generate_Graphs.generate_random_graph(data); // Generate a random graph based on set regions and generate sensible edges between them
        clear_user_nodes(data);
    }

    public static void traversal_speed_input_function(Runtime_Data data) {
        data.setTraversal_speed(data.getTraversal_speed_slider().getValue()); // Get divider value from slider
        data.getTraversal_speed_label().setText(String.format("Traversal Speed: %.1f", data.getTraversal_speed())); // Edit label below slider
    }

    public static void start_node_input_function(Runtime_Data data) {
        data.getError_popup().setVisible(true); // Ensure error label displays regardless of status
        String input = data.getStart_node_input().getText();
        int user_start_node_id;

        try {
            user_start_node_id = Integer.parseInt(input); // Ensure only integer values can be inputted
        } catch (NumberFormatException e) {
            Node n = data.getGraph().get_node_from_label(input);
            if (n == null) {
                data.getError_popup_label().setText("No node found with ID or label:\n'" + input + "'"); // Catch invalid type error - prevent crashing
                data.setValid_setup(false);
                data.getCurrent_start_node_label().setText("Start node: INVALID");
                return;
            }
            user_start_node_id = n.getId();
        }

        if (user_start_node_id == data.getEnd_node()) {
            data.getError_popup_label().setText("Start node cannot be the same \nas the end node"); // Make sure inputted nodes are unique
            data.setValid_setup(false);
            data.getCurrent_start_node_label().setText("Start node: INVALID");
            return;
        }

        if (data.getGraph().get_node_from_id(user_start_node_id) == null) {
            data.getError_popup_label().setText("Desired start node does not exist"); // Make sure node exists
            data.setValid_setup(false);
            data.getCurrent_start_node_label().setText("Start node: INVALID");
            return;
        }

        if (user_start_node_id > 99 || user_start_node_id < 0) {
            data.getError_popup_label().setText("Desired start node outside valid range"); // Make sure start node in valid range
            data.setValid_setup(false);
            data.getCurrent_start_node_label().setText("Start node: INVALID");
            return;
        }

        data.setStart_node(user_start_node_id);
        data.getError_popup_label().setText(""); // Clear errors if valid
        data.setValid_setup(true);
        data.getCurrent_start_node_label().setText("Start node: " + user_start_node_id);
    }

    public static void end_node_input_function(Runtime_Data data) {
        data.getError_popup().setVisible(true);
        String input = data.getEnd_node_input().getText();
        int user_end_node_id;

        try {
            user_end_node_id = Integer.parseInt(input); // Ensure only integer values can be inputted
        } catch (NumberFormatException e) {
            Node n = data.getGraph().get_node_from_label(input);
            if (n == null) {
                data.getError_popup_label().setText("No node found with ID or label:\n'" + input + "'"); // Catch invalid type error - prevent crashing
                data.setValid_setup(false);
                data.getCurrent_end_node_label().setText("End node: INVALID");
                return;
            }
            user_end_node_id = n.getId();
        }

        if (user_end_node_id == data.getStart_node()) {
            data.getError_popup_label().setText("End node cannot be the same \nas the start node"); // Make sure inputted nodes are unique
            data.setValid_setup(false);
            data.getCurrent_end_node_label().setText("End node: INVALID");
            return;
        }

        if (data.getGraph().get_node_from_id(user_end_node_id) == null) {
            data.getError_popup_label().setText("Desired end node does not exist"); // Make sure node exists
            data.setValid_setup(false);
            data.getCurrent_end_node_label().setText("End node: INVALID");
            return;
        }

        if (user_end_node_id > 99 || user_end_node_id < 0) {
            data.getError_popup_label().setText("Desired end node outside valid range"); // Make sure end node id is in the valid range
            data.setValid_setup(false);
            data.getCurrent_end_node_label().setText("End node: INVALID");
            return;
        }

        data.setEnd_node(user_end_node_id);
        data.getError_popup_label().setText(""); // Clear errors if valid
        data.setValid_setup(true);
        data.getCurrent_end_node_label().setText("End node: " + user_end_node_id);
    }

    public static void change_edge_weight_input_function(Runtime_Data data) {
        data.getError_popup().setVisible(true);
        try {
            String new_weight = data.getChange_edge_weight_input().getText();

            if (new_weight.length() >= 4) {
                data.getError_popup_label().setText("New weight too high");
                data.setValid_setup(false);
                return;
            }

            data.setNew_edge_weight(Integer.parseInt(data.getChange_edge_weight_input().getText())); // Update the edge weight each time the user inputs a new character
            data.getError_popup_label().setText("");
            data.setValid_setup(true); // Clear errors if valid
        } catch (NumberFormatException e) {
            data.getError_popup_label().setText("Invalid edge weight");
            data.setValid_setup(false);
        }
    }

    public static void change_node_label_input_function(Runtime_Data data) {
        data.getError_popup().setVisible(true);
        try {
            String new_label = data.getChange_node_label_input().getText();

            if (new_label.length() >= 6) {
                data.getError_popup_label().setText("Node label too long");
                data.setValid_setup(false);
                return;
            }

            if (data.getGraph().label_already_exists(new_label, data.getNode_to_edit())) {
                data.getError_popup_label().setText("Node label already in use");
                data.setValid_setup(false);
                return;
            }

            data.setNew_node_label(new_label);
            data.getError_popup_label().setText("");
            data.setValid_setup(true);
        } catch (Exception e) {
            data.getError_popup_label().setText("Invalid node label");
            data.setValid_setup(false);
        }
    }

    public static void traversal_options_function(Runtime_Data data) {
        data.setSelected_traversal(data.getTraversal_options().getSelected()); // Update selected traversal based on option from drop down menu
        if (data.getSelected_traversal().equals("Bellman-Ford")) {
            data.getTraversal_progress_options().setVisible(false);
            data.getChange_edge_weight_input().setTextFieldFilter(((text_field, c) -> Character.isDigit(c) || Character.toString(c).equals("-")));
        } else {
            check_edge_weights(data); // Make sure all the weights are >= 0 for all traversals that aren't Bellman-Ford
            if (!data.getTraversal_progress_options().isVisible()) {
                data.getTraversal_progress_options().setVisible(true);
            }
            data.getChange_edge_weight_input().setTextFieldFilter((text_field, c) -> Character.isDigit(c));
        }
    }

    public static void traversal_progress_options_function(Runtime_Data data) {
        if (data.isTraversal_in_progress()) { // Do not allow the traversal option to be changed while a traversal is in progress
            data.getTraversal_progress_options().setSelected(data.getSelected_traversal_progress());
            data.getError_popup_label().setText("Cannot change progress option\nwhile a traversal is running");
            data.getError_popup().setVisible(true);
        }

        data.setSelected_traversal_progress(data.getTraversal_progress_options().getSelected()); // Update selected traversal progress based on option from drop down menu

        switch (data.getSelected_traversal_progress()) { // Switch case the different options and update booleans in data accordingly
            case "Automatic":
                data.setShould_sleep(true);
                data.setShould_step(false);
                data.getStep_traversal_button().setVisible(false);
                data.getStep_traversal_label().setVisible(false);
                break;
            case "Stepped":
                data.setShould_step(true);
                data.setShould_sleep(false);
                data.getStep_traversal_button().setVisible(true);
                data.getStep_traversal_label().setVisible(true);
                break;
            case "No Delay":
                data.setShould_step(false);
                data.setShould_sleep(false);
                data.getStep_traversal_button().setVisible(false);
                data.getStep_traversal_label().setVisible(false);
                break;
        }

        if (data.getStep_traversal_button().isVisible()) {
            data.getError_popup().setY(-660);
        } else {
            data.getError_popup().setY(-600);
        }
    }

    private static void clear_user_nodes(Runtime_Data data) {
        // Reset user's start nodes and end nodes
        data.setStart_node(1000);
        data.getStart_node_input().setText("");
        data.getCurrent_start_node_label().setText("Start Node: ");

        data.setEnd_node(1001);
        data.getEnd_node_input().setText("");
        data.getCurrent_end_node_label().setText("End Node: ");
    }

    private static void check_edge_weights(Runtime_Data data) {
        for (Node n: data.getGraph().get_nodes()) {
            for (Edge e: data.getGraph().get_edges(n)) {
                if (e.getWeight() < 0) {
                    e.setWeight(Math.abs(e.getWeight())); // Get the absolute value (positive) of the edge weight if it is negative
                }
            }
        }
    }
}