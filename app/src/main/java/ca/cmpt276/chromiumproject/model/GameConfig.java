package ca.cmpt276.chromiumproject.model;

import java.util.ArrayList;
import java.util.List;

/**
 *  GameConfig contains information about a type of game (e.g. chess, monopoly)
 *  Fields include name of game, and what constitutes as a Poor Score and a Great Score
 *  Has a list of GameRecords. getGameRecordStrings() returns a String array with data about these games
 *  Use setConfigValues() to set its fields
 */

public class GameConfig {
    private String name;
    private int poorScore;
    private int greatScore;
    private List<GameRecord> gameRecords;

    // TODO: might be useful to have empty constructor depending on requirements
    public GameConfig(String name, int poorScore, int greatScore) {
        setConfigValues(name, poorScore, greatScore);
        gameRecords = new ArrayList<>();
    }

    public void setConfigValues(String name, int poorScore, int greatScore) {
        // TODO: add more error handling depending on customer requirements
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Game Config name is empty");
        }
        this.name = name;
        this.poorScore = poorScore;
        this.greatScore = greatScore;
    }

    public String getName() {
        return name;
    }

    public int getPoorScore() {
        return poorScore;
    }

    public int getGreatScore() {
        return greatScore;
    }

    public void addGameRecord(GameRecord gameRecord) {
        gameRecords.add(gameRecord);
    }

    public int getNumGameRecords() {
        return gameRecords.size();
    }

    public List<GameRecord> getGameRecords() {
        return gameRecords;
    }
}
