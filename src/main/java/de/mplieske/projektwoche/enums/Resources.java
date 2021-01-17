package de.mplieske.projektwoche.enums;

public enum Resources {

   MAIN_VIEW("/layouts/main-view.fxml");

   private final String location;

   private Resources(final String location) {
      this.location = location;
   }

   public java.net.URL getResource() {
      return Resources.class.getResource(location);
   }

}
