package de.mplieske.projektwoche.model;

import javafx.beans.property.SimpleBooleanProperty;

public class Model {

   private final SimpleBooleanProperty labelVisible = new SimpleBooleanProperty(false);

   public boolean isLabelVisible() {
      return labelVisible.getValue();
   }

   public void setLabelVisible(final boolean labelVisible) {
      this.labelVisible.setValue(labelVisible);
   }

   public final SimpleBooleanProperty labelVisibleProperty() {
      return labelVisible;
   }
}
