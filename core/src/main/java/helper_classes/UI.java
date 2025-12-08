package helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import structural_classes.Edge;
import structural_classes.Graph;
import structural_classes.Node;
import fundamental_classes.Runtime_Data;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class UI {
    public static void quit_button_function(){
        Gdx.app.exit();
        System.exit(0); // Exit the whole Java program
    }

    public static void save_current_layout_button_function(Runtime_Data data){
        String documents_path =  System.getProperty("user.home") + "\\Documents\\";
        Path save_path = Path.of(documents_path + "\\GTS Saved Layouts\\");

        if (Files.notExists(save_path)) {
            new File(save_path.toString()).mkdir();
        }
    }

    public static void load_saved_layout_button_function(Runtime_Data data){

    }

    public static void reset_button_function(Runtime_Data data){
        Graph.clear(); // Clear the adj list of the graph
        data.setStart_node(0); // Reset chosen start and end node
        data.setEnd_node(0);
        data.setTraversal_canceled(false); // Reset status flags for authorising traversal start
        data.setTraversal_in_progress(false);
    }

    public static void start_traversal_button_function(Runtime_Data data){
        if (data.isTraversal_in_progress()){
            data.getError_popup().setVisible(true); // Make sure the user can only run 1 traversal at a time
            data.getError_popup_label().setText("Traversal is already running");
            return;
        }

        if (data.getStart_node() == data.getEnd_node()){ // Make sure inputted nodes are unique
            data.getError_popup_label().setText("Start node cannot be the same \nas the end node");
            data.setValid_setup(false);
            return;
        }

        if (data.isValid_setup()){
            reset_colours(data); // Ensure the nodes are all white in the graph
            data.setTraversal_canceled(false); // Reset status to make sure the traversal doesn't instantly stop
            Inputs.start_traversal(data); // Start traversal
        }
    }

    public static void reset_traversal_button_function(Runtime_Data data){
        data.setTraversal_canceled(true); // Stop the current traversal from running
        data.setTraversal_in_progress(false); // Allow a new traversal to be run
        reset_colours(data); // Reset colours of nodes in graph
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

    public static void change_edge_weight_input_function(Runtime_Data data){
        data.getError_popup().setVisible(true);
        try {
            data.setNew_edge_weight(Integer.parseInt(data.getChange_edge_weight_input().getText())); // Update the edge weight each time the user inputs a new character
            data.getError_popup_label().setText("");
            data.setValid_setup(true); // Clear errors if valid
        } catch (Exception e){
            data.getError_popup_label().setText("Invalid edge weight");
            data.getError_popup_label().setVisible(true);
            data.setValid_setup(false);
        }
    }

    public static void traversal_options_function(Runtime_Data data){
        data.setSelected_traversal(data.getTraversal_options().getSelected()); // Update selected traversal based on option from drop down menu
        if (data.getSelected_traversal().equals("Bellman-Ford")){
            data.getChange_edge_weight_input().setTextFieldFilter(((text_field, c) -> Character.isDigit(c) || Character.toString(c).equals("-")));
        } else {
            data.getChange_edge_weight_input().setTextFieldFilter((text_field, c) -> Character.isDigit(c));
        }
    }
}