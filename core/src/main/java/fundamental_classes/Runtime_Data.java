package fundamental_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Runtime_Data {
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    private Graph graph = new Graph();
    private Table error_popup = new Table();
    private TextField start_node_input = new TextField("", skin);;
    private TextField end_node_input = new TextField("", skin);;
    private Slider traversal_speed_slider = new Slider(0.1f,10.0f,0.1f,false, skin);
    private SelectBox<String> traversal_options = new SelectBox<>(skin);
    private Label error_popup_label = new Label("", skin);
    private Label traversal_speed_label = new Label(String.format("Traversal speed: %.1f", 1.0f), skin);
    private int start_node, end_node;
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

    public void setEnd_node_input(TextField end_node_input) {
        this.end_node_input = end_node_input;
    }

    public Table getError_popup() {
        return error_popup;
    }

    public void setError_popup(Table error_popup) {
        this.error_popup = error_popup;
    }

    public Label getError_popup_label() {
        return error_popup_label;
    }

    public void setError_popup_label(Label error_popup_label) {
        this.error_popup_label = error_popup_label;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
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

    public void setStart_node_input(TextField start_node_input) {
        this.start_node_input = start_node_input;
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

    public void setTraversal_options(SelectBox<String> traversal_options) {
        this.traversal_options = traversal_options;
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

    public void setTraversal_speed_label(Label traversal_speed_label) {
        this.traversal_speed_label = traversal_speed_label;
    }

    public Slider getTraversal_speed_slider() {
        return traversal_speed_slider;
    }

    public void setTraversal_speed_slider(Slider traversal_speed_slider) {
        this.traversal_speed_slider = traversal_speed_slider;
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

    public void setSkin(Skin skin) {
        this.skin = skin;
    }
}
