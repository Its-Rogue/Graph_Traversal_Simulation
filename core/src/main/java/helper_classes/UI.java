package helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import fundamental_classes.*;

public class UI {
    public static void quit_button_function(){
        Gdx.app.exit(); // Exit the GDX program
        System.exit(0); // Exit the whole Java program
    }

    public static void reset_button_function(Main main){
        Graph.clear(); // Clear the adj list of the graph
        main.start_node = 0; // Reset chosen start and end node
        main.end_node = 0;
    }

    public static void start_traversal_button_function(Main main){
        if (main.traversal_in_progress){
            main.error_popup_label.setText("Traversal is already running");
            return;
        }
        if(main.start_node == main.end_node){ // Make sure inputted nodes are unique
            main.error_popup_label.setText("Start node cannot be the same \nas the end node");
            main.valid_setup = false;
            return;
        }
        if(main.valid_setup){
            Inputs.start_traversal(main.graph, main.selected_traversal, main.traversal_speed, main.start_node, main.end_node, main); // Start traversal
        }
    }

    public static void reset_traversal_button_function(Main main){
        for (Node node: main.graph.get_nodes()){
            if (node.getColor() != Color.WHITE){
                node.setColor(Color.WHITE); // Reset all nodes' colour if they aren't white
            }
        }
    }

    public static void recreate_test_elements_button_function(Main main){
        Testing_Functions.create(main.graph, main.node_radius, main); // Create testing node grid
    }

    public static void traversal_speed_input_function(Main main){
        main.traversal_speed = main.traversal_speed_slider.getValue(); // Get divider value from slider
        main.traversal_speed_label.setText("Traversal Speed: " + main.traversal_speed); // Edit label below slider
    }

    public static void start_node_input_function(Main main){
        main.error_popup.setVisible(true); // Ensure error label displays regardless of status
        try {
            int user_start_node = Integer.parseInt(main.start_node_input.getText()); // Ensure only integer values can be inputted

            if (user_start_node == main.end_node) {
                main.error_popup_label.setText("Start node cannot be the same \nas the end node"); // Make sure inputted nodes are unique
                main.valid_setup = false;
                return;
            }
            if (main.graph.get_node_id(user_start_node) == null) {
                main.error_popup_label.setText("Desired start node does not exist"); // Make sure node exists
                main.valid_setup = false;
                return;
            }

            main.start_node = user_start_node;
            main.error_popup_label.setText(""); // Clear errors if valid
            main.valid_setup = true;
        } catch (NumberFormatException e) {
            main.error_popup_label.setText("Invalid start node input:\ninput must be a integer"); // Catch invalid type error - prevent crashing
            main.valid_setup = false;
        }
    }

    public static void end_node_input_function(Main main){
        main.error_popup.setVisible(true);
        try {
            int user_end_node = Integer.parseInt(main.end_node_input.getText()); // Ensure only integer values can be inputted

            if (user_end_node == main.start_node) {
                main.error_popup_label.setText("End node cannot be the same \nas the start node"); // Make sure inputted nodes are unique
                main.valid_setup = false;
                return;
            }
            if (main.graph.get_node_id(user_end_node) == null) {
                main.error_popup_label.setText("Desired end node does not exist"); // Make sure node exists
                main.valid_setup = false;
                return;
            }

            main.end_node = user_end_node;
            main.error_popup_label.setText(""); // Clear errors if valid
            main.valid_setup = true;
        } catch (NumberFormatException e) {
            main.error_popup_label.setText("Invalid end node input:\ninput must be a integer"); // Catch invalid type error - prevent crashing
            main.valid_setup = false;
        }
    }

    public static void traversal_options_function(Main main){
        main.selected_traversal = main.traversal_options.getSelected(); // Update selected traversal based on option from drop down menu
    }
}