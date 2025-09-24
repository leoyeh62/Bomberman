package fr.univartois.butinfo.ihm.model;

import java.util.Random;

import fr.univartois.butinfo.ihm.model.FacadeBombermanController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * La classe Enemy représente un adversaire du joueur dans le jeu du Bomberman.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public class Enemy extends AbstractCharacter {

    /**
     * Le nom de ce personnage.
     */
    private final String name;
    private Timeline timeline;  
    private Random rand = new Random();

    /**
     * Construit un nouvel Enemy.
     *
     * @param name Le nom du personnage.
     */
    public Enemy(String name, FacadeBombermanController facade) {
        super(1, facade);
        this.name = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.ihm.bomberman.model.AbstractCharacter#getName()
     */
    @Override
    public String getName() {
        return name;
    }
    public void moveRandomly() {
        int direction = rand.nextInt(4); 
        switch (direction) {
            case 0:
            	getFacade().move(this, "UP");
                break;
            case 1:
            	getFacade().move(this, "DOWN");
                break;
            case 2:
            	getFacade().move(this, "LEFT");
                break;
            case 3:
            	getFacade().move(this, "RIGHT");
                break;
        }
    }

    public void animate() {
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> moveRandomly())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
   

}
