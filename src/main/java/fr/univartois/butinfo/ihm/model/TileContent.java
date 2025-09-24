package fr.univartois.butinfo.ihm.model;
public enum TileContent {

    /**
     * Le contenu d'une tuile vide, sur laquelle un personnage peut se déplacer.
     * Il s'agit de pelouse ici.
     */
    LAWN("lawn", true, true),

    /**
     * Le contenu d'une tuile comportant un mur de briques, qui peut être
     * détruit par une explosion.
     */
    BRICK_WALL("bricks", false, true),

    /**
     * Le contenu d'une tuile comportant un mur de pierre, qui ne peut pas être
     * détruit par une explosion.
     */
    SOLID_WALL("wall", false, false);

    /**
     * Le nom de cet élément de la carte.
     */
    private final String name;

    /**
     * Indique si cet élément est considéré comme vide.
     * Un élément vide est un élément sur lequel un personnage peut se déplacer.
     */
    private final boolean empty;

    /**
     * Indique si une explosion peut détruire cet élément.
     * Un élément qui peut être détruit par une explosion est un élément
     * qui peut être détruit par une bombe posée par un personnage.
     */
    private final boolean destroyableByExplosion;

    /**
     * Crée une nouvelle instance de TileContent.
     *
     * @param name Le nom de cet élément de la carte.
     * @param empty Indique si cet élément est considéré comme vide.
     * @param destroyableByExplosion Indique si une explosion peut détruire cet élément.
     */
    TileContent(String name, boolean empty, boolean destroyableByExplosion) {
        this.name = name;
        this.empty = empty;
        this.destroyableByExplosion = destroyableByExplosion;
    }

    /**
     * Donne le nom de cet élément de la map.
     *
     * @return Le nom de cet élément.
     */
    public String getName() {
        return name;
    }

    /**
     * Vérifie si cet élement est considéré comme vide.
     * Un élément vide est un élément sur lequel un personnage peut se déplacer.
     *
     * @return Si cet élement est vide.
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Détermine si une explosion peut détruire cet élément.
     *
     * @return Si une explosion a un effet sur cet élément.
     */
    public boolean isDestroyableByExplosion() {
        return destroyableByExplosion;
    }
    public String getImagePath() {
        return "/images/" + name + ".png";
    }

}

