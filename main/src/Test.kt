import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import java.util.Collections.addAll



class Test : Application() {
    override fun start(primaryStage: Stage) {
        val btn1 = Button("Button 1 on bottom ")
        btn1.setMinSize(150.0,100.0)
        val btn2 = Button("Button 2 on top")
        btn2.setMinSize(150.0,60.0)
        val root = GridPane()
        val scene = Scene(root, 200.0, 200.0)
        root.add(btn1, 0, 1)
        root.add(btn2, 0, 0)
        primaryStage.scene = scene
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(Test::class.java)
}