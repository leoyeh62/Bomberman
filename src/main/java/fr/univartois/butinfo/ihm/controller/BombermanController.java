package fr.univartois.butinfo.ihm.controller;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import fr.univartois.butinfo.ihm.model.*;

public class BombermanController implements IFacadeBombermanController {

    @FXML 
    private Label ennemiesLeft;
    
    @FXML 
    private GridPane grid;
    
    @FXML 
    private Label highScore;
    
    @FXML 
    private Label hp;
    
    @FXML 
    private Label nbBombe;
    
    @FXML 
    private Button inventory;
    
    @FXML 
    private Button restart;
    
    @FXML 
    private Label round;
    
    @FXML
    private GridPane lawnGrid;
    
    @FXML 
    private Label score;

    private final int HEIGHT = 15;
    private final int WIDTH = 15;

    private Scene scene;
    private Player player;
    private GameMap map = new GameMap(WIDTH, HEIGHT);
    private StackPane[][] panes = new StackPane[HEIGHT][WIDTH];
    private FacadeBombermanController facade;

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setFacadeBombermanController(FacadeBombermanController facade) {
        this.facade = facade;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameMap getGameMap() {
        return map;
    }

    public StackPane[][] getPanes() {
        return panes;
    }

    public Label getScoreLabel() {
        return score;
    }

    public Label getHighScoreLabel() {
        return highScore;
    }

    public Label getEnnemiesLeft() {
        return ennemiesLeft;
    }
    public Label getHp() {
        return hp;
    }

    @FXML
    void initialize() {
       
    }

    private Image chargerImage(TileContent content) {
        String path = switch (content) {
            case LAWN -> "/images/lawn.png";
            case BRICK_WALL -> "/images/bricks.png";
            case SOLID_WALL -> "/images/wall.png";
            default -> "/images/empty.png";
        };
        return new Image(getClass().getResourceAsStream(path));
    }

    public void chargerImage() {
        lawnGrid.getChildren().clear();
        lawnGrid.getRowConstraints().clear();
        lawnGrid.getColumnConstraints().clear();

        map = GameMapFactory.createMapWithRandomBrickWalls(HEIGHT, WIDTH, 12);

        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                Tile tile = map.get(i, j);
                ImageView fond = new ImageView();
                fond.setFitWidth(50);
                fond.setFitHeight(50);
                fond.imageProperty().bind(Bindings.createObjectBinding(() -> chargerImage(tile.getContent()), tile.getContentProperty()));
                StackPane cell = new StackPane(fond);
                cell.setPrefSize(50, 50);
                panes[i][j] = cell;
                lawnGrid.add(cell, j, i);
            }
        }

        grid.setGridLinesVisible(true);
        lawnGrid.setGridLinesVisible(true);
        lawnGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        round.setText("1");
        ennemiesLeft.setText("0");
        score.setText("0");
    }

    @FXML
	public void afficherNbBombe() {
        nbBombe.textProperty().bind(Bindings.concat("Bombs : ").concat(player.getNbBombe()));
    }

    @FXML
    void OnClick(ActionEvent event) {
        Button id = (Button) event.getSource();
        if ("restart".equals(id.getId())) {
            chargerImage();
            facade.setGameMap(map);
            facade.placeEnemies(5);
            facade.placeCharacter(facade.getPlayer());
            for (Enemy enemy : facade.getEnemies()) {
                lierPersonnageAffichage(enemy, "/images/" + enemy.getName() + ".png");
            }
            lierPersonnageAffichage(facade.getPlayer(), "/images/guy.png");
            facade.getPlayer().giveBomb();
            afficherNbBombe();
        }
    }

    @Override
    public void initImage(int row, int col) {
        ImageView lawnImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/lawn.png")));
        lawnImageView.setFitWidth(50);
        lawnImageView.setFitHeight(50);
        lawnGrid.add(lawnImageView, col, row);
    }

    public void mettreAJour(AbstractCharacter character, int oldRow, int oldCol) {
        StackPane oldPane = panes[oldRow][oldCol];
        StackPane newPane = panes[character.getRow()][character.getColumn()];
        String characterId;
        if (character instanceof Player) {
            characterId = "player";
        } else {
            characterId = "enemy";
        }
        oldPane.getChildren().removeIf(node -> node instanceof ImageView && characterId.equals(node.getId()));
        newPane.getChildren().removeIf(node -> node instanceof ImageView && characterId.equals(node.getId()));
        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/images/" + character.getName() + ".png")));
        image.setFitWidth(50);
        image.setFitHeight(50);
        image.setId(characterId);
        newPane.getChildren().add(image);
    }

    public void removeCharacter(int row, int col) {
        StackPane pane = panes[row][col];
        pane.getChildren().removeIf(node -> node instanceof ImageView && "player".equals(node.getId()));
    }

    @Override
    public void lierPersonnageAffichage(AbstractCharacter character, String imagePath) {
        ImageView personnage = new ImageView(new Image(imagePath));
        personnage.setFitWidth(50);
        personnage.setFitHeight(50);
        personnage.setId((character instanceof Player) ? "player" : "enemy");
        panes[character.getRow()][character.getColumn()].getChildren().add(personnage);
    }

    public void afficherBombe(AbstractBomb bomb, AbstractCharacter character) {
        String path = switch (bomb.getName()) {
            case "bomb" -> "/images/bomb.png";
            case "column-bomb" -> "/images/column-bomb.png";
            case "row-bomb" -> "/images/row-bomb.png";
            case "large-bomb" -> "/images/large-bomb.png";
            default -> null;
        };

        if (path != null) {
            ImageView vueBombe = new ImageView(new Image(getClass().getResourceAsStream(path)));
            vueBombe.setFitWidth(50);
            vueBombe.setFitHeight(50);
            vueBombe.setId("bomb");
            int row = character.getRow();
            int col = character.getColumn();
            if (row >= 0 && row < panes.length && col >= 0 && col < panes[0].length) {
                panes[row][col].getChildren().add(vueBombe);
            }
        }
    }
} 
