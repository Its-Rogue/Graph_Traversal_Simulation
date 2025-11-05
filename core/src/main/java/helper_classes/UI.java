package helper_classes;

import com.badlogic.gdx.graphics.Color;
import fundamental_classes.*;

public class UI {
    public static void quit_button_function(){
        System.exit(0); // Exit the whole Java program
    }

    public static void reset_button_function(Runtime_Data data){
        Graph.clear(); // Clear the adj list of the graph
        data.setStart_node(0); // Reset chosen start and end node
        data.setEnd_node(0);
        data.setTraversal_canceled(false);
        data.setTraversal_in_progress(false);
    }

    public static void start_traversal_button_function(Runtime_Data data){
        if (data.isTraversal_in_progress()){
            data.getError_popup().setVisible(true);
            data.getError_popup_label().setText("Traversal is already running");
            return;
        }

        if (data.getStart_node() == data.getEnd_node()){ // Make sure inputted nodes are unique
            data.getError_popup_label().setText("Start node cannot be the same \nas the end node");
            data.setValid_setup(false);
            return;
        }

        if (data.isValid_setup()){
            reset_colours(data);
            data.setTraversal_canceled(false);
            Inputs.start_traversal(data); // Start traversal
        }
    }

    public static void reset_traversal_button_function(Runtime_Data data){
        data.setTraversal_in_progress(false);
        data.setTraversal_canceled(true);
        reset_colours(data);
    }

    public static void reset_colours(Runtime_Data data){
        for (Node node: data.getGraph().get_nodes()){
            if (node.getColour() != Color.WHITE){
                node.setColour(Color.WHITE); // Reset all nodes' colour if they aren't white
            }
            for (Edge edge: data.getGraph().get_edges(node)){
                if (edge.getColour() != Color.WHITE){
                    edge.setColour(Color.WHITE); // Reset all edges' colour if they aren't white
                }
            }
        }
    }

    public static void recreate_test_elements_button_function(Runtime_Data data){
        Testing_Functions.create(data); // Create testing node grid
    }

    public static void traversal_speed_input_function(Runtime_Data data){
        data.setTraversal_speed(data.getTraversal_speed_slider().getValue()); // Get divider value from slider
        data.getTraversal_speed_label().setText(String.format("Traversal speed: %.1f", data.getTraversal_speed())); // Edit label below slider
    }

    public static void start_node_input_function(Runtime_Data data){
        data.getError_popup().setVisible(true); // Ensure error label displays regardless of status
        try {
            int user_start_node = Integer.parseInt(data.getStart_node_input().getText()); // Ensure only integer values can be inputted

            if (user_start_node == data.getEnd_node()) {
                data.getError_popup_label().setText("Start node cannot be the same \nas the end node"); // Make sure inputted nodes are unique
                data.setValid_setup(false);
                return;
            }
            if (data.getGraph().get_node_id(user_start_node) == null) {
                data.getError_popup_label().setText("Desired start node does not exist"); // Make sure node exists
                data.setValid_setup(false);
                return;
            }

            data.setStart_node(user_start_node);
            data.getError_popup_label().setText(""); // Clear errors if valid
            data.setValid_setup(true);
        } catch (NumberFormatException e) {
            data.getError_popup_label().setText("Invalid start node input:\ninput must be a integer"); // Catch invalid type error - prevent crashing
            data.setValid_setup(false);
        }
    }

    public static void end_node_input_function(Runtime_Data data){
        data.getError_popup().setVisible(true);
        try {
            int user_end_node = Integer.parseInt(data.getEnd_node_input().getText()); // Ensure only integer values can be inputted

            if (user_end_node == data.getStart_node()) {
                data.getError_popup_label().setText("End node cannot be the same \nas the start node"); // Make sure inputted nodes are unique
                data.setValid_setup(false);
                return;
            }
            if (data.getGraph().get_node_id(user_end_node) == null) {
                data.getError_popup_label().setText("Desired end node does not exist"); // Make sure node exists
                data.setValid_setup(false);
                return;
            }

            data.setEnd_node(user_end_node);
            data.getError_popup_label().setText(""); // Clear errors if valid
            data.setValid_setup(true);
        } catch (NumberFormatException e) {
            data.getError_popup_label().setText("Invalid end node input:\ninput must be a integer"); // Catch invalid type error - prevent crashing
            data.setValid_setup(false);
        }
    }

    public static void traversal_options_function(Runtime_Data data){
        data.setSelected_traversal(data.getTraversal_options().getSelected()); // Update selected traversal based on option from drop down menu
    }
}