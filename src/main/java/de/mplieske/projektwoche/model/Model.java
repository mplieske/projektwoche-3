package de.mplieske.projektwoche.model;

import de.mplieske.projektwoche.enums.FieldStatus;
import javafx.beans.property.SimpleObjectProperty;

public class Model {
   private final SimpleObjectProperty<FieldStatus>[][] board = new SimpleObjectProperty[8][8];
   private final SimpleObjectProperty<FieldStatus> currentPlayer = new SimpleObjectProperty<>();
   private final SimpleObjectProperty<FieldStatus> winner = new SimpleObjectProperty<>(FieldStatus.EMPTY);

   public Model() {
      for (int i = 0; i < 8; i++) {
         for (int j = 0; j < 8; j++) {
            board[i][j] = new SimpleObjectProperty<FieldStatus>(FieldStatus.EMPTY);
         }
      }
   }

   public SimpleObjectProperty<FieldStatus>[][] getBoard() {
      return board;
   }

   public FieldStatus getCurrentPlayer() {
      return currentPlayer.getValue();
   }

   public void setCurrentPlayer(final FieldStatus currentPlayer) {
      this.currentPlayer.setValue(currentPlayer);
   }

   public SimpleObjectProperty<FieldStatus> currentPlayerProperty() {
      return currentPlayer;
   }

   public FieldStatus getWinner() {
      return winner.getValue();
   }

   public void setWinner(final FieldStatus winner) {
      this.winner.setValue(winner);
   }

   public SimpleObjectProperty<FieldStatus> winnerProperty() {
      return winner;
   }
}
