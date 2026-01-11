package helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import structural_classes.Edge;
import structural_classes.Node;
import essential_classes.Runtime_Data;
import essential_classes.Traversals;

public class Inputs {
    private static Node first_node_selected = null;
    private final static int menu_padding = 250; // Padding to ensure the nodes are not hidden by the menu UI

    // Simple method for all variants of input
    public static void all(Runtime_Data data) {
        menu();
        mouse_or_keyboard_input(data);
    }

    // Code specifically related to menu hotkeys
    private static void menu() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            System.exit(0); // Exit the java program
        }
    }

    // Code for the detection of mouse / keyboard inputs
    private static void mouse_or_keyboard_input(Runtime_Data data) {
        int mouse_x = Gdx.input.getX();
        int mouse_y = Gdx.graphics.getHeight() - Gdx.input.getY(); // GetY() is based on the top left corner, thus needs offsetting

        left_click(mouse_x, mouse_y, data); // Left click detection and code
        right_click(mouse_x, mouse_y, data); // Right click detection and code for edge creation
        middle_click(mouse_x, mouse_y, data); // Middle click / Backspace (for when the user does not have MMB, such as on a laptop touchpad) detection and code for node and edge deletion
        enter_key_input(data); // Check each frame to see if the enter key has been pressed while the change edge weight popup is visible
        mouse_position_check(data, mouse_x, mouse_y); // Check each frame to see if the mouse is in the bottom left corner of the screen, where the colour key is
    }

    // Code for adding nodes upon left-clicking
    private static void left_click(int mouse_x, int mouse_y, Runtime_Data data) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            // Ensure the pointer is within the screen bounds
            if (mouse_x >= Gdx.graphics.getWidth() - data.getNode_radius()) {
                mouse_x = Gdx.graphics.getWidth() - data.getNode_radius();
            } else if (mouse_x <= data.getNode_radius() + menu_padding) {
                if (mouse_x <= menu_padding) {
                    return; // If clicked within the menu area, do not create a node
                }
                mouse_x = data.getNode_radius() + menu_padding + 10;
            }
            if (mouse_y >= Gdx.graphics.getHeight() - data.getNode_radius()) {
                mouse_y = Gdx.graphics.getHeight() - data.getNode_radius(); // Place node fully on screen if clicked in a position where it would be obscured
            } else if (mouse_y <= data.getNode_radius()) {
                mouse_y = data.getNode_radius(); // Place node fully on screen if clicked in a position where it would be obscured
            }

            for (Node node: data.getGraph().get_nodes()) {
                if (distance_check(node.getPosition().getX(), node.getPosition().getY(), mouse_x, mouse_y, data) < 4 * data.getNode_radius()) {
                    return; // Do not allow the new node to be placed too close to an already existing node
                }
            }

            if (data.getChange_edge_weight_popup().isVisible()) {
                if (distance_check((int) data.getChange_edge_weight_popup().getX(), (int) data.getChange_edge_weight_popup().getY(), mouse_x, mouse_y, data) < 2 * data.getChange_edge_weight_label().getWidth()) {
                    return; // Do not allow the new node to be placed if it is too close to the change edge weight popup, allowing the user to select the text field without accidentally editing the graph
                }
            }

            data.getGraph().add_node(data.getNode_radius(), mouse_x, mouse_y, data); // Add a new node at the coordinates clicked at if in a valid location
        }
    }

    // Code for selecting nodes / edges to create an edge / edit an edge's weight when right-clicking
    private static void right_click(int mouse_x, int mouse_y, Runtime_Data data) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            for (Node node: data.getGraph().get_nodes()) { // Loop over each node
                if (distance_check(node.getPosition().getX(), node.getPosition().getY(), mouse_x, mouse_y, data) <= data.getNode_radius()) {
                    if (first_node_selected == null) { // Checks if a node has already been selected
                        first_node_selected = node; // Assigns the clicked node to the first one selected
                        node.setColour(Color.GREEN);
                    } else if (first_node_selected != node) {
                        data.getGraph().add_edge(first_node_selected.getId(), node.getId(), 1, data); // Creates the edge between the nodes
                        first_node_selected.setColour(Color.WHITE);
                        first_node_selected = null; // Clears the first node to be used again
                    }
                    return; // Return so the loop is not run all the way through if the desired node is already found
                }
            }

            if (first_node_selected != null) {
                first_node_selected.setColour(Color.WHITE);
                first_node_selected = null; // Set to null if not right-clicked on a node
            }
        }
    }

    // Code for deleting a node / edge when middle-clicking
    private static void middle_click(int mouse_x, int mouse_y, Runtime_Data data) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE) || Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && data.getChange_edge_weight_popup().isVisible()) {
                return; // Prevent accidental deletion of nodes while typing in new edge weight
            }
            for (Node node: data.getGraph().get_nodes()) { // Loop over each node
                if (distance_check(node.getPosition().getX(), node.getPosition().getY(), mouse_x, mouse_y, data) <= data.getNode_radius()) {
                    if (first_node_selected != null) { // Checks to see if a node has already been selected
                        if (first_node_selected != node) { // Checks to see if the node selected again is different
                            data.getGraph().remove_edge(first_node_selected.getId(), node.getId(), data); // Remove edge if nodes are different
                        }
                        first_node_selected.setColour(Color.WHITE); // Reset first node selected
                        if (first_node_selected == node) {
                            data.getGraph().remove_node(node.getId(), data);
                        }
                    } else {
                        data.getGraph().remove_node(node.getId(), data);
                    }
                    first_node_selected = null;
                    return;
                }
            }
            if (first_node_selected != null) { // Reset selected node if node selected then user clicks again not on a different node
                first_node_selected.setColour(Color.WHITE);
                first_node_selected = null;
            }
        }
    }

    // Code for updating the edge's weight after it has been inputted
    private static void enter_key_input(Runtime_Data data) {
        if (!data.getChange_edge_weight_popup().isVisible()) {
            return; // Do not run if edge weight is not being changed
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            data.getEdge_to_edit().setWeight(data.getNew_edge_weight()); // Set forward edge to have the new weight
            for (Node node: data.getEdge_to_edit().getSource().getNeighbours()) {
                if (node == data.getEdge_to_edit().getTarget()) {
                    for (Edge edge: data.getGraph().get_edges(node)) {
                        if (edge.getTarget().equals(data.getEdge_to_edit().getSource())) {
                            edge.setWeight(data.getNew_edge_weight()); // Find the reverse edge and set it to have the new weight as well
                        }
                    }
                }
            }
            data.getChange_edge_weight_popup().setVisible(false);
            data.getChange_edge_weight_input().setText(""); // Reset inputted text and hide the input field / popup
        }
    }

    private static void mouse_position_check(Runtime_Data data, int mouse_x, int mouse_y){
        if (mouse_x > 250 || mouse_y > 260){
            data.getColour_hint_popup().setVisible(false);
            return;
        }

        if (!data.getColour_hint_popup().isVisible()){
            data.getColour_hint_popup().setVisible(true);
        }

        if (mouse_y < 50) {
            data.getColour_hint_popup().setPosition(367,86);
            data.setColour_hint_label_background(Color.SKY);
            data.getColour_hint_label().setText(get_hint("shortest path"));
        }

        if (mouse_y > 50 && mouse_y < 85) {
            data.getColour_hint_popup().setPosition(355, 93);
            data.setColour_hint_label_background(Color.PURPLE);
            data.getColour_hint_label().setText(get_hint("fully explored"));
        }

        if (mouse_y > 85 && mouse_y < 120) {
            data.getColour_hint_popup().setPosition(373, 86);
            data.setColour_hint_label_background(Color.YELLOW);
            data.getColour_hint_label().setText(get_hint("discovered"));
        }

        if (mouse_y > 120 && mouse_y < 155) {
            data.getColour_hint_popup().setPosition(372, 93);
            data.setColour_hint_label_background(Color.CYAN);
            data.getColour_hint_label().setText(get_hint("current"));
        }

        if (mouse_y > 155 && mouse_y < 190) {
            data.getColour_hint_popup().setPosition(373,86);
            data.setColour_hint_label_background(Color.ORANGE);
            data.getColour_hint_label().setText(get_hint("visited"));
        }

        if (mouse_y > 190 && mouse_y < 225) {
            data.getColour_hint_popup().setPosition(373, 80);
            data.setColour_hint_label_background(Color.RED);
            data.getColour_hint_label().setText(get_hint("end"));
        }

        if (mouse_y > 225 && mouse_y < 260) {
            data.getColour_hint_popup().setPosition(373, 93);
            data.setColour_hint_label_background(Color.GREEN);
            data.getColour_hint_label().setText(get_hint("start"));
        }
    }

    // Check if the node is close enough to the clicked position, then calculate the distance to the centre of the node
    private static int distance_check(int pos_x,  int pos_y, int mouse_x, int mouse_y, Runtime_Data data) {
        float offset_multiplier = 1;
        if (data.getChange_edge_weight_popup().isVisible()) {
            offset_multiplier = 1.5f; // Bounds check for the change edge weight popup specifically
        }
        if (pos_x > mouse_x + 100 * offset_multiplier || pos_x < mouse_x - 100 * offset_multiplier) return (int) Double.POSITIVE_INFINITY;
        if (pos_y > mouse_y + 100 * offset_multiplier || pos_y < mouse_y - 100 * offset_multiplier) return (int) Double.POSITIVE_INFINITY; // Skip checking the node if it is too far away from the clicked position

        float dx = mouse_x - pos_x;
        float dy = mouse_y - pos_y;
        return (int) Math.sqrt(dx * dx + dy * dy); // Euclidean distance calculation between mouse x,y and pos x,y
    }

    // Switch case the chosen traversal option
    public static void start_traversal(Runtime_Data data) {
        data.getError_popup_label().setText("");
        data.getError_popup().setVisible(false);
        switch (data.getSelected_traversal()) {
            case "Depth-First Search":
                Traversals.dfs(data);
                clear_error_display(data);
                break;
            case "Breadth-First Search":
                Traversals.bfs(data);
                clear_error_display(data);
                break;
            case "Bidirectional Search":
                Traversals.bidirectional(data);
                clear_error_display(data);
            case "Dijkstra's":
                Traversals.dijkstra(data);
                clear_error_display(data);
                break;
            case "A*":
                Traversals.A_star(data);
                clear_error_display(data);
                break;
            case "Bellman-Ford":
                Traversals.Bellman_Ford(data);
                clear_error_display(data);
                break;
        }
    }

    // Switch case the different hints for the mouse over portion of the colour key
    private static String get_hint(String hint_type){
        String hint = "";
        switch (hint_type){
            case "shortest path":
                hint = "This node forms a part of the\nshortest path discovered\nby one of the weighted\ntraversal algorithms";
                break;
            case "fully explored":
                hint = "This node, as well as all its\nneighbours, have been a\ncurrent node.";
                break;
            case "discovered":
                hint = "This node is the neighbour of a\nnode that has been a current\nnode, but has not been a\ncurrent node itself.";
                break;
            case "current":
                hint = "This is the node the chosen\ntraversal algorithm currently\n has selected and is examining.";
                break;
            case "visited":
                hint = "This node has been the current\nnode previously, but all its\nneighbours haven't been\nthe current node yet.";
                break;
            case "end":
                hint = "This is the end node chosen by\nthe user, where the chosen\ntraversal will attempt to reach.\nAlso the reverse start node for\na bidirectional search.";
                break;
            case "start":
                hint = "This is the start node chosen by\nthe user, where the chosen\ntraversal will begin from.";
                break;
        }
        return hint;
    }

    // Clear any error messages that the user may have caused upon the completion / termination of a traversal
    private static void clear_error_display(Runtime_Data data) {
        data.getError_popup_label().setText("");
        data.getError_popup_label().setVisible(false);
        data.getError_popup().setVisible(false);
    }
}