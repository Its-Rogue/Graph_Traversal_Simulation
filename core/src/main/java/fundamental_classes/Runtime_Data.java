package fundamental_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import structural_classes.Graph;

public class Runtime_Data {
    private final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    private final Graph graph = new Graph();
    private final Table error_popup = new Table();
    private final Table change_edge_weight_popup = new Table();
    private final TextField start_node_input = new TextField("", skin);
    private final TextField end_node_input = new TextField("", skin);
    private final TextField change_edge_weight_input = new TextField("", skin);
    private final Slider traversal_speed_slider = new Slider(0.1f,10.0f,0.1f,false, skin);
    private final SelectBox<String> traversal_options = new SelectBox<>(skin);
    private final Label error_popup_label = new Label("", skin);
    private final Label traversal_speed_label = new Label(String.format("Traversal speed: %.1f", 1.0f), skin);
    private final Label change_edge_weight_label = new Label("Press ENTER to confirm change", skin);
    private int start_node, end_node;
    private int new_edge_weight;
    private float traversal_speed = 1.0f;
    private boolean valid_setup = false;
    private boolean traversal_in_progress = false;
    private volatile boolean traversal_canceled = false;
    private String selected_traversal = "Breadth-First Search";

    public int getEnd_node() {
        return end_node;
    }

    public void setEnd_node(int end_node) {
        this.end_node = end_node;
    }

    public TextField getEnd_node_input() {
        return end_node_input;
    }

    public Table getError_popup() {
        return error_popup;
    }

    public Label getError_popup_label() {
        return error_popup_label;
    }

    public Graph getGraph() {
        return graph;
    }

    public int getNode_radius() {
        return 30;
    }

    public String getSelected_traversal() {
        return selected_traversal;
    }

    public void setSelected_traversal(String selected_traversal) {
        this.selected_traversal = selected_traversal;
    }

    public int getStart_node() {
        return start_node;
    }

    public void setStart_node(int start_node) {
        this.start_node = start_node;
    }

    public TextField getStart_node_input() {
        return start_node_input;
    }

    public boolean isTraversal_canceled() {
        return traversal_canceled;
    }

    public void setTraversal_canceled(boolean traversal_canceled) {
        this.traversal_canceled = traversal_canceled;
    }

    public boolean isTraversal_in_progress() {
        return traversal_in_progress;
    }

    public void setTraversal_in_progress(boolean traversal_in_progress) {
        this.traversal_in_progress = traversal_in_progress;
    }

    public SelectBox<String> getTraversal_options() {
        return traversal_options;
    }

    public float getTraversal_speed() {
        return traversal_speed;
    }

    public void setTraversal_speed(float traversal_speed) {
        this.traversal_speed = traversal_speed;
    }

    public Label getTraversal_speed_label() {
        return traversal_speed_label;
    }

    public Slider getTraversal_speed_slider() {
        return traversal_speed_slider;
    }

    public boolean isValid_setup() {
        return valid_setup;
    }

    public void setValid_setup(boolean valid_setup) {
        this.valid_setup = valid_setup;
    }

    public Skin getSkin() {
        return skin;
    }

    public Table getChange_edge_weight_popup() {
        return change_edge_weight_popup;
    }

    public TextField getChange_edge_weight_input() {
        return change_edge_weight_input;
    }

    public int getNew_edge_weight() {
        return new_edge_weight;
    }

    public void setNew_edge_weight(int new_edge_weight) {
        this.new_edge_weight = new_edge_weight;
    }

    public Label getChange_edge_weight_label() {
        return change_edge_weight_label;
    }
}
