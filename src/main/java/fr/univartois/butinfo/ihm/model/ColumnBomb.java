package fr.univartois.butinfo.ihm.model;

import fr.univartois.butinfo.ihm.model.FacadeBombermanController;

public class ColumnBomb extends AbstractBomb {

    public ColumnBomb(FacadeBombermanController game) {
        super(game);
    }

    @Override
    public String getName() {
        return "column-bomb";
    }

    @Override
    public String getDescription() {
        return "Cette bombe fait exploser le contenu des tuiles voisines situées sur la même colonne.";
    }

    @Override
    public int getDelay() {
        return 2;
    }

    @Override
    public void explode(GameMap gameMap, FacadeBombermanController facade) {
        exploded.set(true);
        for (int i = -1; i <= 1; i++) {
        	int targetRow = this.row + i;
            int targetCol = this.column;
            if (gameMap.isOnMap(targetRow, targetCol)) {
                Tile targetTile = gameMap.get(targetRow, targetCol);
                facade.showExplosionAt(targetRow, targetCol);
                targetTile.explode();
            }
        }
    }
}
