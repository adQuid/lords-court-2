package ui;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class MyAnchorPane {
    private AnchorPane pane;

    public MyAnchorPane(Node... children){
        pane = new AnchorPane(children);
    }

    public void setLeftAnchor(Node child, double value){
        pane.setLeftAnchor(child, value);
    }
    public void setRightAnchor(Node child, double value){
        pane.setRightAnchor(child, value);
    }
    public void setTopAnchor(Node child, double value){
        pane.setTopAnchor(child, value);
    }
    public void setBottomAnchor(Node child, double value){
        pane.setBottomAnchor(child, value);
    }
    public AnchorPane getRealPane(){
        return pane;
    }
}
