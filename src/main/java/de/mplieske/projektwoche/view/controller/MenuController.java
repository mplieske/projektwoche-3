package de.mplieske.projektwoche.view.controller;

import de.mplieske.projektwoche.controller.Controller;
import de.mplieske.projektwoche.enums.Resources;
import de.mplieske.projektwoche.model.Model;
import de.mplieske.projektwoche.view.SceneLoader;
import de.mplieske.projektwoche.view.ViewController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MenuController implements ViewController {

   private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

   private Controller controller;
   private Model model;

   @FXML
   private TextField playerOneTextField;

   @FXML
   private TextField playerTwoTextField;

   @FXML
   private Button quitButton;

   @FXML
   private Button startButton;

   @FXML
   private void initialize() {
      quitButton.setOnAction(actionEvent -> Platform.exit());

      startButton.setOnAction(actionEvent -> {
         model.setPlayerWhiteName(playerOneTextField.getText());
         model.setPlayerBlackName(playerTwoTextField.getText());
         controller.startNewGame();
         try {
            SceneLoader.loadScene(Resources.MAIN_VIEW);
         } catch (final IOException e) {
            LOGGER.error("Could not load scene of Resources.MAIN_VIEW '{}'.", Resources.MAIN_VIEW.getLocation(), e);
            Platform.exit();
         }
      });

      playerOneTextField.textProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue == null || newValue.isEmpty() || playerTwoTextField.getText() == null || playerTwoTextField.getText().isEmpty() || newValue.equals(playerTwoTextField.getText())) {
            startButton.setDisable(true);
            return;
         }

         startButton.setDisable(false);
      });

      playerTwoTextField.textProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue == null || newValue.isEmpty() || playerOneTextField.getText() == null || playerOneTextField.getText().isEmpty() || newValue.equals(playerOneTextField.getText())) {
            startButton.setDisable(true);
            return;
         }

         startButton.setDisable(false);
      });
   }

   @Override
   public void setController(final Controller controller) {
      this.controller = controller;
      model = controller.getModel();
   }

   @Override
   public void dataInit() {

   }
}
