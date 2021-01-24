package de.mplieske.projektwoche.view.controller;

import de.mplieske.projektwoche.controller.Controller;
import de.mplieske.projektwoche.enums.FieldStatus;
import de.mplieske.projektwoche.enums.Resources;
import de.mplieske.projektwoche.model.Model;
import de.mplieske.projektwoche.view.SceneLoader;
import de.mplieske.projektwoche.view.ViewController;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainView implements ViewController {

   private static final Logger LOGGER = LoggerFactory.getLogger(MainView.class);
   private static final Image playerBlackImage = new Image(Resources.PLAYER_BLACK_IMAGE.getLocation(), 50, 50, false, true);
   private static final Image playerWhiteImage = new Image(Resources.PLAYER_WHITE_IMAGE.getLocation(), 50, 50, false, true);

   private final Map<SimpleObjectProperty<FieldStatus>, ImageView> fieldStatusPropertyImageMap = new HashMap<>();
   private final Map<Pane, Point> panePointMap = new HashMap<>();


   private Controller controller;
   private Model model;

   @FXML
   private Button newGameButton;

   @FXML
   private GridPane boardGridPane;

   @FXML
   private ImageView currentPlayerImageView;

   @FXML
   private Label playerNameLabel;

   @FXML
   private void initialize() {
      LOGGER.info("Initialize main view.");
      newGameButton.setOnAction(actionEvent -> {
         try {
            SceneLoader.loadScene(Resources.MENU_VIEW);
         } catch (final IOException e) {
            LOGGER.error("Could not load view of menu '{}'.", Resources.MENU_VIEW.getLocation(), e);
            Platform.exit();
         }
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

      // TODO: refactor me please 😢😢😢😢😢😢😢:(

      model.winnerProperty().addListener((observable, oldValue, newValue) -> {
         boardGridPane.setDisable(true);

         final Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("Game Over");

         switch (newValue) {
            case PLAYER_WHILE:
               alert.setHeaderText("We Have A Winner!!!");
               alert.setContentText("The winner is: '" + model.getPlayerWhiteName() + "'");
               break;
            case PLAYER_BLACK:
               alert.setHeaderText("We Have A Winner!!!");
               alert.setContentText("The winner is: '" + model.getPlayerBlackName() + "'");
               break;
            default:
               alert.setHeaderText("thr s no wnnr :(");
               alert.setContentText("Both Black and White have the same amount of pieces on the board.");
               break;
         }
         alert.showAndWait();
      });

      // TODO: duplicate
      if (model.getCurrentPlayer() == FieldStatus.PLAYER_BLACK) {
         currentPlayerImageView.setImage(playerBlackImage);
         playerNameLabel.setText("(" + model.getPlayerBlackName() + ")");
      } else if (model.getCurrentPlayer() == FieldStatus.PLAYER_WHILE) {
         currentPlayerImageView.setImage(playerWhiteImage);
         playerNameLabel.setText("(" + model.getPlayerWhiteName() + ")");
      } else {
         // this should not happen.
         throw new RuntimeException("Invalid value for player: '" + model.getCurrentPlayer() + "'.");
      }

      model.currentPlayerProperty().addListener((observable, oldValue, newValue) -> {
         if (model.getCurrentPlayer() == FieldStatus.PLAYER_BLACK) {
            currentPlayerImageView.setImage(playerBlackImage);
            playerNameLabel.setText("(" + model.getPlayerBlackName() + ")");
         } else if (model.getCurrentPlayer() == FieldStatus.PLAYER_WHILE) {
            currentPlayerImageView.setImage(playerWhiteImage);
            playerNameLabel.setText("(" + model.getPlayerWhiteName() + ")");
         } else {
            // this should not happen.
            throw new RuntimeException("Invalid value for player: '" + newValue + "'.");
         }
      });

      for (int i = 0; i < 8; i++) {
         for (int j = 0; j < 8; j++) {
            final SimpleObjectProperty<FieldStatus> statusProperty = model.getBoard()[i][j];

            final ImageView imageView = new ImageView();
            final Pane pane = new Pane();
            panePointMap.put(pane, new Point(i, j));
            pane.setOnMouseClicked(mouseEvent -> {
               final Point point = panePointMap.get(mouseEvent.getSource());
               controller.move(point.x, point.y);
            });
            pane.backgroundProperty().setValue(new Background(new BackgroundFill(Color.rgb(255, 165, 79), CornerRadii.EMPTY, Insets.EMPTY)));
            pane.getChildren().add(imageView);
            if (statusProperty.getValue() == FieldStatus.PLAYER_BLACK) {
               imageView.setImage(playerBlackImage);
               imageView.setVisible(true);
            } else if (statusProperty.getValue() == FieldStatus.PLAYER_WHILE) {
               imageView.setImage(playerWhiteImage);
               imageView.setVisible(true);
            } else {
               imageView.setVisible(false);
            }

            boardGridPane.add(pane, i, j);
            fieldStatusPropertyImageMap.put(statusProperty, imageView);

            statusProperty.addListener((observable, oldValue, newValue) -> {
               LOGGER.debug("observable '{}' changed from '{}' to '{}'.", observable, oldValue, newValue);
               final ImageView toBeChangedImageView = fieldStatusPropertyImageMap.get(observable);
               switch (newValue) {
                  case EMPTY:
                     toBeChangedImageView.setVisible(false);
                     break;
                  case PLAYER_BLACK:
                     toBeChangedImageView.setImage(playerBlackImage);
                     toBeChangedImageView.setVisible(true);
                     break;
                  case PLAYER_WHILE:
                     toBeChangedImageView.setImage(playerWhiteImage);
                     toBeChangedImageView.setVisible(true);
                     break;
                  default:
                     // never happens.
                     break;
               }
            });
         }
      }
   }

}
