package de.mplieske.projektwoche.view;

import de.mplieske.projektwoche.controller.Controller;
import de.mplieske.projektwoche.enums.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {

   private static Stage primaryStage;
   private static Controller controller;

   public static void setPrimaryStage(final Stage primaryStage) {
      SceneLoader.primaryStage = primaryStage;
   }

   public static void setController(final Controller controller) {
      SceneLoader.controller = controller;
   }

   public static void loadScene(final Resources layout) throws IOException {
      final FXMLLoader loader = new FXMLLoader(layout.getResource());
      final Parent root = loader.load();
      primaryStage.setScene(new Scene(root));
      final ViewController viewController = loader.getController();
      viewController.setController(controller);
      viewController.dataInit();
      primaryStage.show();
   }

}
