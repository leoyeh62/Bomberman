package fr.univartois.butinfo.ihm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.univartois.butinfo.ihm.controller.BombermanController;
import fr.univartois.butinfo.ihm.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableIntegerValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FacadeBombermanController {
    private GameMap gameMap;
    private Player player;
    private static final int MAX_ENEMIES = 5;
    private List<Enemy> enemies = new ArrayList<>();
    private Scene scene;
    private BombermanController bombermanController;
    private int oldRow = -1;
    private int oldCol = -1;
    private FacadeBombermanController BombermanInterface;
    private FacadeBombermanController controller;

    public void setBombermanController(BombermanController bombermanController) {
        this.bombermanController = bombermanController;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        this.scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> move(player, "UP");
                case DOWN -> move(player, "DOWN");
                case LEFT -> move(player, "LEFT");
                case RIGHT -> move(player, "RIGHT");
                case KeyCode.SPACE -> dropBombe(event, bombermanController.getPanes());
                default -> {}
            }
        });
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public void setBombermanInterface(FacadeBombermanController BombermanInterface) {
        this.BombermanInterface = BombermanInterface;
    }

    public void dropBombe(KeyEvent event, StackPane[][] panes) {
        if (player.getNbBombe().get() == 0) {
            return;
        }

        int row = player.getRow();
        int column = player.getColumn();
        AbstractBomb bomb = player.getPlayerbomb().get(0);
        bomb.setPosition(row, column);
        bombermanController.afficherBombe(bomb, getPlayer());
        player.retirerBombe(0);

        StackPane pane = panes[row][column];
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(bomb.getDelay()), e -> {
            bomb.explode(gameMap, this);
            if (bomb.explodedProperty().get()) {
                pane.getChildren().removeIf(node -> node instanceof ImageView && "bomb".equals(node.getId()));
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void explode(int row, int column) {
        if (gameMap != null && gameMap.isOnMap(row, column)) {
            Tile tile = gameMap.get(row, column);
            tile.explode();
        }
    }

    public void placeCharacter(AbstractCharacter character) {
        List<Tile> candidates = gameMap.getEmptyTiles();
        if (candidates.isEmpty()) {
            throw new IllegalStateException("Pas de place libre pour placer le personnage !");
        }
        Tile chosenTile = candidates.get(new Random().nextInt(candidates.size()));
        character.setPosition(chosenTile.getRow(), chosenTile.getColumn());
        chosenTile.setCharacter(character);
    }

    public void showExplosionAt(int row, int col) {
        if (row < 0 || row >= gameMap.getHeight() || col < 0 || col >= gameMap.getWidth()) {
            return;
        }

        StackPane pane = bombermanController.getPanes()[row][col];
        Tile tileContent = gameMap.get(row, col);
        if (tileContent.getContent().equals(TileContent.SOLID_WALL)) {
            return;
        }

        if (tileContent.getContent().equals(TileContent.BRICK_WALL)) {
            Label scoreLabel = bombermanController.getScoreLabel();
            Label highScoreLabel = bombermanController.getHighScoreLabel();

            int currentScore = Integer.parseInt(scoreLabel.getText());
            int newScore = currentScore + 20;
            scoreLabel.setText(String.valueOf(newScore));

            int currentHighScore = Integer.parseInt(highScoreLabel.getText());
            if (newScore > currentHighScore) {
                highScoreLabel.setText(String.valueOf(newScore));
            }
        }

        Image explosionImage = new Image(getClass().getResourceAsStream("/images/explosion.png"));
        ImageView explosionView = new ImageView(explosionImage);
        explosionView.setFitWidth(50);
        explosionView.setFitHeight(50);
        explosionView.setId("explosion");

        pane.getChildren().add(explosionView);
        Timeline removeTimeline = new Timeline(new KeyFrame(Duration.millis(800), e -> {
            pane.getChildren().remove(explosionView);
        }));
        removeTimeline.setCycleCount(1);
        removeTimeline.play();
    }

    public void move(AbstractCharacter character, String direction) {
        int row = character.getRow();
        int col = character.getColumn();
        int newRow = row;
        int newCol = col;

        switch (direction) {
            case "UP" -> newRow--;
            case "DOWN" -> newRow++;
            case "LEFT" -> newCol--;
            case "RIGHT" -> newCol++;
        }

        if (newRow >= 0 && newRow < gameMap.getHeight() && newCol >= 0 && newCol < gameMap.getWidth() && gameMap.get(newRow, newCol).isEmpty()) {
            int oldRow = row;
            int oldCol = col;

            gameMap.get(row, col).setCharacter(null);
            character.setPosition(newRow, newCol);
            gameMap.get(newRow, newCol).setCharacter(character);

            if (character instanceof Player) {
                bombermanController.mettreAJour(character, oldRow, oldCol);
                bombermanController.lierPersonnageAffichage(character, "/images/guy.png");
            } else if (character instanceof Enemy enemy) {
                bombermanController.mettreAJour(character, oldRow, oldCol);
                bombermanController.lierPersonnageAffichage(character, "/images/" + enemy.getName() + ".png");
            }
        }
    }

    public void setController(FacadeBombermanController controller) {
        this.controller = controller;
    }

    private void setupGame(BombermanController bombermanController) {
        placeCharacter(player);
        placeEnemies(MAX_ENEMIES);
        bombermanController.lierPersonnageAffichage(player, "/images/guy.png");
        for (Enemy enemy : enemies) {
            bombermanController.lierPersonnageAffichage(enemy, "/images/" + enemy.getName() + ".png");
            enemy.animate();
        }
        bombermanController.afficherNbBombe();
        Label NbEnnemies = bombermanController.getEnnemiesLeft();
        int nbE = enemies.size();
        NbEnnemies.setText(String.valueOf(nbE));
        int health = player.getHealth();
        bombermanController.getHp().setText("HP : "+String.valueOf(health));
    }

    public void placeEnemies(int max) {
        final String[] enemyNames = {"agent", "goblin", "minotaur", "punker", "rourke"};
        enemies.clear();
        List<Tile> emptyTiles = gameMap.getEmptyTiles();
        Random random = new Random();
        for (int i = 0; i < max; i++) {
            if (emptyTiles.isEmpty()) {
                break;
            }
            int index = random.nextInt(emptyTiles.size());
            Tile tile = emptyTiles.remove(index);
            String enemyName = enemyNames[i];
            Enemy enemy = new Enemy(enemyName, this);
            enemy.setPosition(tile.getRow(), tile.getColumn());
            tile.setCharacter(enemy);
            enemies.add(enemy);
        }
    }

    public void initialiser(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/univartois/butinfo/ihm/view/bomberman-view.fxml"));
        Parent viewContent = fxmlLoader.load();
        BombermanController bombermanController = fxmlLoader.getController();
        setBombermanController(bombermanController);
        bombermanController.setFacadeBombermanController(this);
        bombermanController.chargerImage();
        setGameMap(bombermanController.getGameMap());
        player = new Player(this);
        bombermanController.setPlayer(player);
        enemies = new ArrayList<>();
        setupGame(bombermanController);
        Scene scene = new Scene(viewContent, 1150, 950);
        bombermanController.setScene(scene);
        setScene(scene);
        stage.setScene(scene);
        viewContent.setFocusTraversable(true);
        viewContent.setOnMouseClicked(e -> viewContent.requestFocus());
        stage.setTitle("Bomberman v1.0");
        stage.show();
        Platform.runLater(viewContent::requestFocus);
    }
}
