package de.mplieske.projektwoche.controller;

import de.mplieske.projektwoche.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {

   private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
   private static final Model MODEL = new Model();

   public Model getModel() {
      return MODEL;
   }

}
