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
    }

    public static void reset_button_function(Runtime_Data data) {
        data.getGraph().clear(); // Clear the adj list of the graph
        data.setStart_node(0); // Reset chosen start and end node
        data.setEnd_node(0);
        data.setTraversal_canceled(false); // Reset status flags for authorising traversal start
        data.setTraversal_in_progress(false);
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
        if (data.isStep_button_pressed()) {
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
        reset_colours(data); // Reset colours of nodes in graph
    }

    public static void reset_colours(Runtime_Data data) {
        for (Node node: data.getGraph().get_nodes()) {
            if (node.getColour() != Color.WHITE) {
                node.setColour(Color.WHITE); // Reset all nodes' colour if they aren't white (default colour)
            }
            for (Edge edge: data.getGraph().get_edges(node)) {
                if (edge.getColour() != Color.WHITE) {
                    edge.setColour(Color.WHITE); // Reset all edges' colour if they aren't white (default colour)
                }
            }
        }
    }

    public static void generate_grid_button_function(Runtime_Data data) {
        Generate_Graphs.generate_grid(data); // Generate a predetermined 10 * 10 grid where the 2-4 nodes adjacent are neighbours
    }

    public static void generate_random_graph_button_function(Runtime_Data data) {
        Generate_Graphs.generate_random_graph(data); // Generate a random graph based on set regions and generate sensible edges between them
    }

    public static void traversal_speed_input_function(Runtime_Data data) {
        data.setTraversal_speed(data.getTraversal_speed_slider().getValue()); // Get divider value from slider
        data.getTraversal_speed_label().setText(String.format("Traversal speed: %.1f", data.getTraversal_speed())); // Edit label below slider
    }

    public static void start_node_input_function(Runtime_Data data) {
        data.getError_popup().setVisible(true); // Ensure error label displays regardless of status
        try {
            int user_start_node_id = Integer.parseInt(data.getStart_node_input().getText()); // Ensure only integer values can be inputted

            if (user_start_node_id == data.getEnd_node()) {
                data.getError_popup_label().setText("Start node cannot be the same \nas the end node"); // Make sure inputted nodes are unique
                data.setValid_setup(false);
                return;
            }
            if (data.getGraph().get_node_from_id(user_start_node_id) == null) {
                data.getError_popup_label().setText("Desired start node does not exist"); // Make sure node exists
                data.setValid_setup(false);
                return;
            }

            data.setStart_node(user_start_node_id);
            data.getError_popup_label().setText(""); // Clear errors if valid
            data.setValid_setup(true);
        } catch (NumberFormatException e) {
            data.getError_popup_label().setText("Invalid start node input:\ninput must be a integer"); // Catch invalid type error - prevent crashing
            data.setValid_setup(false);
        }
    }

    public static void end_node_input_function(Runtime_Data data) {
        data.getError_popup().setVisible(true);
        try {
            int user_end_node_id = Integer.parseInt(data.getEnd_node_input().getText()); // Ensure only integer values can be inputted

            if (user_end_node_id == data.getStart_node()) {
                data.getError_popup_label().setText("End node cannot be the same \nas the start node"); // Make sure inputted nodes are unique
                data.setValid_setup(false);
                return;
            }
            if (data.getGraph().get_node_from_id(user_end_node_id) == null) {
                data.getError_popup_label().setText("Desired end node does not exist"); // Make sure node exists
                data.setValid_setup(false);
                return;
            }

            data.setEnd_node(user_end_node_id);
            data.getError_popup_label().setText(""); // Clear errors if valid
            data.setValid_setup(true);
        } catch (NumberFormatException e) {
            data.getError_popup_label().setText("Invalid end node input:\ninput must be a integer"); // Catch invalid type error - prevent crashing
            data.setValid_setup(false);
        }
    }

    public static void change_edge_weight_input_function(Runtime_Data data) {
        data.getError_popup().setVisible(true);
        try {
            data.setNew_edge_weight(Integer.parseInt(data.getChange_edge_weight_input().getText())); // Update the edge weight each time the user inputs a new character
            data.getError_popup_label().setText("");
            data.setValid_setup(true); // Clear errors if valid
        } catch (Exception e) {
            data.getError_popup_label().setText("Invalid edge weight");
            data.setValid_setup(false);
        }
    }

    public static void traversal_options_function(Runtime_Data data) {
        data.setSelected_traversal(data.getTraversal_options().getSelected()); // Update selected traversal based on option from drop down menu
        if (data.getSelected_traversal().equals("Bellman-Ford")) {
            data.getChange_edge_weight_input().setTextFieldFilter(((text_field, c) -> Character.isDigit(c) || Character.toString(c).equals("-")));
        } else {
            data.getChange_edge_weight_input().setTextFieldFilter((text_field, c) -> Character.isDigit(c));
        }
    }

    public static void traversal_progress_options_function(Runtime_Data data) {
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
            case "No delay":
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
}