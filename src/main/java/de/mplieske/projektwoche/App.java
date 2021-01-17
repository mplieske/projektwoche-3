package de.mplieske.projektwoche;

import de.mplieske.projektwoche.controller.Controller;
import de.mplieske.projektwoche.enums.Resources;
import de.mplieske.projektwoche.view.SceneLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application {

   private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

   public static void main(final String... args) {
      LOGGER.info("Starting Application ...");
      launch(args);
   }

   @Override
   public void start(final Stage primaryStage) throws Exception {
      LOGGER.info("Starting UI ...");

      final Controller controller = new Controller();
      SceneLoader.setController(controller);
      SceneLoader.setPrimaryStage(primaryStage);

      SceneLoader.loadScene(Resources.MAIN_VIEW);
   }

}
