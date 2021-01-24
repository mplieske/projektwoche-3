package de.mplieske.projektwoche.controller;

import de.mplieske.projektwoche.enums.FieldStatus;
import de.mplieske.projektwoche.model.Model;
import javafx.beans.property.SimpleObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {

   private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
   private static final Model MODEL = new Model();

   public Model getModel() {
      return MODEL;
   }


   public void startNewGame() {
      // TODO: split me up into smaller methods :)
      if (Math.random() < 0.5) {
         MODEL.setCurrentPlayer(FieldStatus.PLAYER_BLACK);
      } else {
         MODEL.setCurrentPlayer(FieldStatus.PLAYER_WHILE);
      }
      LOGGER.info("Player is '{}'", MODEL.getCurrentPlayer());

      for (int i = 0; i < 8; i++) {
         for (int j = 0; j < 8; j++) {
            MODEL.getBoard()[i][j].setValue(FieldStatus.EMPTY);
         }
      }

      MODEL.getBoard()[3][3].setValue(FieldStatus.PLAYER_WHILE);
      MODEL.getBoard()[4][4].setValue(FieldStatus.PLAYER_WHILE);

      MODEL.getBoard()[3][4].setValue(FieldStatus.PLAYER_BLACK);
      MODEL.getBoard()[4][3].setValue(FieldStatus.PLAYER_BLACK);
   }

   public void move(final int x, final int y) {
      if (!isMoveValid(x, y)) {
         throw new RuntimeException("Invalid move.");
      }

      MODEL.getBoard()[x][y].setValue(MODEL.getCurrentPlayer());

      // TODO: refactor me
      // TODO: split me up into smaller methods
      // TODO: maybe use point and vector
      if (isDirectionValid(x, y, 0, -1)) { // top
         setFieldsInDirection(x, y, 0, -1, MODEL.getCurrentPlayer());
      }
      if (isDirectionValid(x, y, -1, -1)) { // top left
         setFieldsInDirection(x, y, -1, -1, MODEL.getCurrentPlayer());
      }
      if (isDirectionValid(x, y, 1, -1)) { // top right
         setFieldsInDirection(x, y, 1, -1, MODEL.getCurrentPlayer());
      }

      if (isDirectionValid(x, y, -1, 0)) { // left
         setFieldsInDirection(x, y, -1, 0, MODEL.getCurrentPlayer());
      }
      if (isDirectionValid(x, y, 1, 0)) { // right
         setFieldsInDirection(x, y, 1, 0, MODEL.getCurrentPlayer());
      }

      if (isDirectionValid(x, y, 0, 1)) { // bottom
         setFieldsInDirection(x, y, 0, 1, MODEL.getCurrentPlayer());
      }
      if (isDirectionValid(x, y, -1, 1)) { // bottom left
         setFieldsInDirection(x, y, -1, 1, MODEL.getCurrentPlayer());
      }
      if (isDirectionValid(x, y, 1, 1)) { // bottom right
         setFieldsInDirection(x, y, 1, 1, MODEL.getCurrentPlayer());
      }

      // TODO: maybe refactor me
      MODEL.setCurrentPlayer(MODEL.getCurrentPlayer() == FieldStatus.PLAYER_BLACK ? FieldStatus.PLAYER_WHILE : FieldStatus.PLAYER_BLACK);

      if (!checkIfThereAreValidMoves()) {
         final FieldStatus winner = calculateWinner();
         MODEL.setWinner(winner);
      }
   }

   private FieldStatus calculateWinner() {
      int playerBlackCount = 0;
      int playerWhiteCount = 0;

      for (int i = 0; i < 8; i++) {
         for (int j = 0; j < 8; j++) {
            if (MODEL.getBoard()[i][j].getValue() == FieldStatus.PLAYER_BLACK) {
               playerBlackCount++;
            } else if (MODEL.getBoard()[i][j].getValue() == FieldStatus.PLAYER_WHILE) {
               playerWhiteCount++;
            }
         }
      }

      if (playerBlackCount > playerWhiteCount) {
         return FieldStatus.PLAYER_BLACK;
      } else if (playerWhiteCount > playerBlackCount) {
         return FieldStatus.PLAYER_WHILE;
      }
      return FieldStatus.EMPTY;
   }

   private boolean checkIfThereAreValidMoves() {
      boolean mooveIsValid = false;
      for (int i = 0; i < 8; i++) {
         for (int j = 0; j < 8; j++) {
            mooveIsValid |= isMoveValid(i, j);
         }
      }
      return mooveIsValid;
   }

   private boolean isMoveValid(final int x, final int y) {
      final boolean moveValid = false;

      if (MODEL.getCurrentPlayer() == FieldStatus.EMPTY) {
         return moveValid;
      }

      final SimpleObjectProperty<FieldStatus>[][] board = MODEL.getBoard();
      final SimpleObjectProperty<FieldStatus> selectedCell = board[x][y];

      if (selectedCell.getValue() != FieldStatus.EMPTY) {
         return moveValid;
      }

      return isAnyDirectionRight(x, y);
   }

   private boolean isAnyDirectionRight(final int x, final int y) {
      return isDirectionValid(x, y, 0, -1) ||
           isDirectionValid(x, y, -1, -1) ||
           isDirectionValid(x, y, 1, -1) ||
           isDirectionValid(x, y, -1, 0) ||
           isDirectionValid(x, y, 1, 0) ||
           isDirectionValid(x, y, 0, 1) ||
           isDirectionValid(x, y, -1, 1) ||
           isDirectionValid(x, y, 1, 1);
   }

   private boolean isDirectionValid(final int xStart, final int yStart, final int vectorX, final int vectorY) {

      // TODO: refactor me
      // TDOD: split me up into multiple methods

      FieldStatus enemyStatus = FieldStatus.PLAYER_BLACK;
      if (MODEL.getCurrentPlayer() == FieldStatus.PLAYER_BLACK) {
         enemyStatus = FieldStatus.PLAYER_WHILE;
      }

      int multiplier = 1;

      FieldStatus status;
      try {
         status = getStatus(xStart, yStart, vectorX * multiplier, vectorY * multiplier);

         if (status != enemyStatus) {
            return false;
         }
      } catch (final IndexOutOfBoundsException exception) {
         return false;
      }

      for (multiplier = 2; status != FieldStatus.EMPTY; multiplier++) {
         try {
            status = getStatus(xStart, yStart, vectorX * multiplier, vectorY * multiplier);
         } catch (final IndexOutOfBoundsException exception) {
            break;
         }

         if (status == enemyStatus) {
            continue;
         }

         if (status == FieldStatus.EMPTY) {
            break;
         }

         return true;
      }

      return false;
   }

   private FieldStatus getStatus(final int xStart, final int yStart, final int xVector, final int yVector) {
      final SimpleObjectProperty<FieldStatus> statusProperty = MODEL.getBoard()[xStart + xVector][yStart + yVector];
      return statusProperty.getValue();
   }

   private void setFieldsInDirection(final int x, final int y, final int vectorX, final int vectorY, final FieldStatus player) {
      for (int i = 1; getStatus(x, y, i * vectorX, i * vectorY) != player; i++) {
         MODEL.getBoard()[x + vectorX * i][y + vectorY * i].setValue(player);
      }
   }

}
