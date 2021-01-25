package de.mplieske.projektwoche.controller;

import de.mplieske.projektwoche.enums.FieldStatus;
import de.mplieske.projektwoche.model.Model;
import javafx.beans.property.SimpleObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

   private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
   private static final Model MODEL = new Model();

   private final List<Point> directions = new ArrayList<>();

   public Controller() {
      directions.add(new Point(0, 1));
      directions.add(new Point(1, 1));
      directions.add(new Point(-1, 1));
      directions.add(new Point(1, 0));
      directions.add(new Point(-1, 0));
      directions.add(new Point(0, -1));
      directions.add(new Point(1, -1));
      directions.add(new Point(-1, -1));
   }

   public Model getModel() {
      return MODEL;
   }

   public void startNewGame() {
      MODEL.setWinner(null);
      setRandomPlayer();
      clearBoard();
      setMiddlePiecesOnBoard();
   }

   public void move(final int x, final int y) {
      if (!isMoveValid(x, y)) {
         throw new RuntimeException("Invalid move.");
      }

      setFields(x, y);

      MODEL.setCurrentPlayer(MODEL.getCurrentPlayer() == FieldStatus.PLAYER_BLACK ? FieldStatus.PLAYER_WHILE : FieldStatus.PLAYER_BLACK);

      if (!checkIfThereAreValidMoves()) {
         final FieldStatus winner = calculateWinner();
         MODEL.setWinner(winner);
      }
   }

   private void setRandomPlayer() {
      if (Math.random() < 0.5) {
         MODEL.setCurrentPlayer(FieldStatus.PLAYER_BLACK);
      } else {
         MODEL.setCurrentPlayer(FieldStatus.PLAYER_WHILE);
      }
      LOGGER.info("Player is '{}'", MODEL.getCurrentPlayer());
   }

   private void clearBoard() {
      for (int i = 0; i < 8; i++) {
         for (int j = 0; j < 8; j++) {
            MODEL.getBoard()[i][j].setValue(FieldStatus.EMPTY);
         }
      }
   }

   private void setMiddlePiecesOnBoard() {
      MODEL.getBoard()[3][3].setValue(FieldStatus.PLAYER_WHILE);
      MODEL.getBoard()[4][4].setValue(FieldStatus.PLAYER_WHILE);

      MODEL.getBoard()[3][4].setValue(FieldStatus.PLAYER_BLACK);
      MODEL.getBoard()[4][3].setValue(FieldStatus.PLAYER_BLACK);
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
      boolean valid = false;

      for (final Point direction : directions) {
         valid |= isDirectionValid(x, y, direction.x, direction.y);
      }

      return valid;
   }

   private void setFields(final int x, final int y) {
      MODEL.getBoard()[x][y].setValue(MODEL.getCurrentPlayer());

      for (final Point direction : directions) {
         if (isDirectionValid(x, y, direction.x, direction.y)) {
            setFieldsInDirection(x, y, direction.x, direction.y, MODEL.getCurrentPlayer());
         }
      }
   }

   private boolean isDirectionValid(final int xStart, final int yStart, final int vectorX, final int vectorY) {
      final FieldStatus enemyStatus = getEnemyStatus();

      if (!isFirstFieldInDirectionValid(xStart, yStart, vectorX, vectorY)) {
         return false;
      }

      return areOtherFieldsInDirectionValid(xStart, yStart, vectorX, vectorY);
   }

   private boolean isFirstFieldInDirectionValid(final int xStart, final int yStart, final int vectorX, final int vectorY) {
      final FieldStatus status;
      try {
         status = getStatus(xStart, yStart, vectorX, vectorY);

         if (status != getEnemyStatus()) {
            return false;
         }
      } catch (final IndexOutOfBoundsException exception) {
         return false;
      }
      return true;
   }

   private boolean areOtherFieldsInDirectionValid(final int xStart, final int yStart, final int vectorX, final int vectorY) {
      FieldStatus status = getEnemyStatus();
      for (int multiplier = 2; status != FieldStatus.EMPTY; multiplier++) {
         try {
            status = getStatus(xStart, yStart, vectorX * multiplier, vectorY * multiplier);
         } catch (final IndexOutOfBoundsException exception) {
            break;
         }

         if (status == getEnemyStatus()) {
            continue;
         }

         if (status == FieldStatus.EMPTY) {
            break;
         }

         return true;
      }

      return false;
   }

   private FieldStatus getEnemyStatus() {
      FieldStatus enemyStatus = FieldStatus.PLAYER_BLACK;
      if (MODEL.getCurrentPlayer() == FieldStatus.PLAYER_BLACK) {
         enemyStatus = FieldStatus.PLAYER_WHILE;
      }
      return enemyStatus;
   }

   private void setFieldsInDirection(final int x, final int y, final int vectorX, final int vectorY, final FieldStatus player) {
      for (int i = 1; getStatus(x, y, i * vectorX, i * vectorY) != player; i++) {
         MODEL.getBoard()[x + vectorX * i][y + vectorY * i].setValue(player);
      }
   }

   private FieldStatus getStatus(final int xStart, final int yStart, final int xVector, final int yVector) {
      final SimpleObjectProperty<FieldStatus> statusProperty = MODEL.getBoard()[xStart + xVector][yStart + yVector];
      return statusProperty.getValue();
   }

   private boolean checkIfThereAreValidMoves() {
      boolean moveIsValid = false;
      for (int i = 0; i < 8; i++) {
         for (int j = 0; j < 8; j++) {
            moveIsValid |= isMoveValid(i, j);
         }
      }
      return moveIsValid;
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

}
