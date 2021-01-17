package de.mplieske.projektwoche.view.controller;

import de.mplieske.projektwoche.controller.Controller;
import de.mplieske.projektwoche.model.Model;
import de.mplieske.projektwoche.view.ViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainView implements ViewController {

   private static final Logger LOGGER = LoggerFactory.getLogger(MainView.class);

   private Controller controller;
   private Model model;

   @FXML
   private Label testLabel;

   @FXML
   private Button quitButton;

   @FXML
   private Button toggleTextButton;

   @FXML
   private void initialize() {
      LOGGER.info("Initialize main view.");
      initializeQuitButton();
      initializeToggleTextButton();
   }

   private void initializeQuitButton() {
      LOGGER.info("Initialize quit button.");
      quitButton.setOnAction(actionEvent -> {
         LOGGER.info("Quit button was clicked.");
         LOGGER.info("Stopping application");
         System.exit(0);
      });
   }

   private void initializeToggleTextButton() {
      LOGGER.info("Initialize toggle text button.");
      toggleTextButton.setOnAction(actionEvent -> {
         LOGGER.info("Toggle text button was clicked.");
         final boolean labelVisible = model.isLabelVisible();
         model.setLabelVisible(!labelVisible);
      });
   }

   @Override
   public void setController(final Controller controller) {
      this.controller = controller;
      model = controller.getModel();
   }

   @Override
   public void dataInit() {
      LOGGER.info("Initialize data.");
      testLabel.visibleProperty().bind(model.labelVisibleProperty());
   }

}
