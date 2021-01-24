package de.mplieske.projektwoche.enums;

public enum Resources {

   MAIN_VIEW("/layouts/main-view.fxml"),
   MENU_VIEW("/layouts/menu.fxml"),
   PLAYER_BLACK_IMAGE("/images/player-black.png"),
   PLAYER_WHITE_IMAGE("/images/player-white.png"),
   ;

   private final String location;

   private Resources(final String location) {
      this.location = location;
   }

   public String getLocation() {
      return location;
   }

   public java.net.URL getResource() {
      return Resources.class.getResource(location);
   }

}
