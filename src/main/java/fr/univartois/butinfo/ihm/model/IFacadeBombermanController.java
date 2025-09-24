package fr.univartois.butinfo.ihm.model;

import fr.univartois.butinfo.ihm.model.AbstractCharacter;
import fr.univartois.butinfo.ihm.model.Player;

public interface IFacadeBombermanController {
	void initImage(int row,int col);
	void mettreAJour(AbstractCharacter player, int oldRow, int oldCol);
	void lierPersonnageAffichage(AbstractCharacter character, String imagePath);
}
