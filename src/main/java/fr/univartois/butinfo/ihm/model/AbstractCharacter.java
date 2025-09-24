package fr.univartois.butinfo.ihm.model;

import fr.univartois.butinfo.ihm.model.FacadeBombermanController;

/**
 * La classe AbstractCharacter est la classe parente des différents personnages pouvant se
 * déplacer dans le jeu du Bomberman.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public abstract class AbstractCharacter {

    /**
     * La ligne où se trouve ce personnage.
     */
    private int row;

    /**
     * La colonne où se trouve ce personnage.
     */
    private int column;

    /**
     * Les points de vie restants pour ce personnage.
     */
    private int health;
    
    private final FacadeBombermanController facade;
    /**
     * Crée une nouvelle instace de AbstractCharacter.
     *
     * @param initialHealth Les points de vie initiaux du personnage.
     */
    protected AbstractCharacter(int initialHealth, FacadeBombermanController facade) {
        this.health = initialHealth;
        this.facade = facade;
    }

    /**
     * Donne le nom de ce personnage.
     *
     * @return Le nom de ce personnage.
     */
    public abstract String getName();

    /**
     * Donne la ligne où se trouve ce personnage.
     *
     * @return La ligne où se trouve ce personnage.
     */
    public int getRow() {
        return row;
    }

    /**
     * Donne la colonne où se trouve ce personnage.
     *
     * @return La colonne où se trouve ce personnage.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Modifie la position de ce personnage.
     *
     * @param row La ligne où se trouve maintenant ce personnage.
     * @param column La colonne où se trouve maintenant ce personnage.
     */
    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Donne les points de vie restants pour ce personnage.
     *
     * @return Les points de vie restants pour ce personnage.
     */
    public int getHealth() {
        return health;
    }
    public FacadeBombermanController getFacade() {
        return facade;
    }

    /**
     * Augmente les points de vie de ce personnage.
     */
    public void incHealth() {
        health++;
    }

    /**
     * Diminue les points de vie de ce personnage.
     */
    public void decHealth() {
        health--;
    }

}
