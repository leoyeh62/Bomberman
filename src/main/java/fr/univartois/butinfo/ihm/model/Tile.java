package fr.univartois.butinfo.ihm.model;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class Tile {

    /**
     * La ligne où cette tuile est positionnée sur la carte.
     */
    private final int row;

    /**
     * La colonne où cette tuile est positionnée sur la carte.
     */
    private final int column;
    
    private final BooleanProperty exploding = new SimpleBooleanProperty(false);

    /**
     * Le contenu de cette tuile.
     */
    private TileContent content;
    private final ObjectProperty<TileContent> contentProp = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> imageProp = new SimpleObjectProperty<>();

	private AbstractCharacter character;
	
	public BooleanProperty explodingProperty() {
        return exploding;
    }
    /**
     * Construit une nouvelle instance de Tile.
     * 
     * @param row La ligne où la tuile est positionnée sur la map.
     * @param column La colonne où la tuile est positionnée sur la map.
     */
    public Tile(int row, int column) {
        this.row = row;
        this.column = column;
        setContent(TileContent.LAWN);
        imageProp.bind(Bindings.createObjectBinding(() -> chargerImage(contentProp.get()), contentProp));
    }
    
    public void explode() {
        if (content.isDestroyableByExplosion()) {
            exploding.set(true);      
            setContent(TileContent.LAWN);
        }
    }
    
    
    /**
     * Donne la ligne où cette tuile est positionnée sur la map.
     *
     * @return La ligne où cette tuile est positionnée sur la map.
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Donne la colonne où cette tuile est positionnée sur la map.
     *
     * @return La colonne où cette tuile est positionnée sur la map.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Donne le contenu de cette tuile.
     *
     * @return Le contenu de cette tuile.
     */
    public TileContent getContent() {
        return content;
    }
    public ObjectProperty<Image> getImageProp(){
    	return imageProp;
    }
    public ObjectProperty<TileContent> getContentProperty() {
        return contentProp;
    }
    

    /**
     * Change le contenu de cette tuile.
     *
     * @param content Le nouveau contenu de cette tuile.
     */
    public void setContent(TileContent content) {
    	this.content=content;
    	contentProp.set(content);
    	}

    /**
     * Vérifie si cette tuile est vide, c'est-à-dire que son contenu est vide.
     *
     * @return Si cette tuile est vide.
     *
     * @see ITileContent#isEmpty()
     */
    public boolean isEmpty() {
        return content.isEmpty() && character == null;
    }

    private Image chargerImage(TileContent content) {
    	if(content == null) {
    		return null;
    	}
    	String chemin = "/images/"+content.getName()+".png";
    	return new Image(getClass().getResourceAsStream(chemin));
    }


	public void setCharacter(AbstractCharacter character) {
		this.character=character;
		
	}
	public AbstractCharacter getCharacter() {
	    return character;
	}
}
