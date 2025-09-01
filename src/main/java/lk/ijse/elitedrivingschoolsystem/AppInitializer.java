package lk.ijse.elitedrivingschoolsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.elitedrivingschoolsystem.config.FactoryConfiguration;

public class AppInitializer extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent loadingScreen = FXMLLoader.load(getClass().getResource("/View/LoadingScreen.fxml"));
        Scene scene = new Scene(loadingScreen);


        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            FactoryConfiguration.getInstance().getSession();
            System.out.println("Session Established");
        }catch (Exception e){
            System.out.println("Session Not Established");
        }finally {
            FactoryConfiguration.getInstance().getSession().close();
            System.out.println("Session Closed");
        }

        launch(args);
    }
    }

