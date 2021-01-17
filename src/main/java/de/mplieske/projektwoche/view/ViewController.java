package de.mplieske.projektwoche.view;

import de.mplieske.projektwoche.controller.Controller;

public interface ViewController {

   void setController(Controller controller);

   void dataInit();

}
