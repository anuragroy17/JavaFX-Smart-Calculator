import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("views/sample.fxml"));

            primaryStage.setTitle("Calculator");
            primaryStage.getIcons().add(new Image("Images/icon.png"));
            primaryStage.setScene(new Scene(root, 400, 460));
            primaryStage.getScene().getStylesheets().addAll(getClass().getResource("views/style.css").toExternalForm());
            root.requestFocus();
            primaryStage.show();
            primaryStage.setResizable(false);
            primaryStage.sizeToScene();

        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
