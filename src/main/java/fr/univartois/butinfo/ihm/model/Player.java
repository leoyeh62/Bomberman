package fr.univartois.butinfo.ihm.model;

import java.util.ArrayList;
import java.util.Random;

import fr.univartois.butinfo.ihm.model.FacadeBombermanController;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Player extends AbstractCharacter {
	private Bomb bomb;
	private LargeBomb largeBomb;
	private RowBomb rowBomb;
	private ColumnBomb columnBomb;
	private ObservableList<AbstractBomb> playerBomb = FXCollections.observableArrayList();
	
	public ObservableList<AbstractBomb> getPlayerbomb() {
		return playerBomb;
	}
	
	public void giveBomb(){
		for(int i=0;i<20;i++) {
			Random rand = new Random();
			double val=rand.nextDouble();
			if (val>=0.6) {
				int val2 = rand.nextInt(2);
				if(val2==1) {
					columnBomb = new ColumnBomb(getFacade());
					playerBomb.add(i, columnBomb);
				}else {
					rowBomb = new RowBomb(getFacade());
					playerBomb.add(i, rowBomb);
				}
				
			}else if(val<0.6 && val>0.1){
				bomb = new Bomb(getFacade());
				playerBomb.add(i, bomb);
			}
			if (val<=0.1) {
				largeBomb = new LargeBomb(getFacade());
				playerBomb.add(i, largeBomb);
			}
			
		}
	}
	public AbstractBomb retirerBombe(int indice) {
		return playerBomb.remove(indice);
	}
	public ObservableIntegerValue getNbBombe() {
		return Bindings.size(playerBomb);
	}
    /**
     * Construit un nouveau Player.
     */
    public Player(FacadeBombermanController facade) {
        super(3,facade);
        giveBomb();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.ihm.bomberman.model.AbstractCharacter#getName()
     */
    @Override
    public String getName() {
        return "guy";
    }

}
