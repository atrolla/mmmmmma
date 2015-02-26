package org.atrolla.game.engine;

import org.atrolla.game.ai.AIManager;
import org.atrolla.game.characters.*;
import org.atrolla.game.configuration.ConfigurationConstants;
import org.atrolla.game.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class Game {
	private final Stage stage;
	private final Collection<GameCharacter> characters;
	private final AIManager aiManager;
	private int time;


	public Game(Stage stage) {
		this.stage = stage;
		this.characters = new ArrayList<>(ConfigurationConstants.GAME_CHARACTERS);
		this.aiManager = new AIManager(ConfigurationConstants.GAME_CHARACTERS);
		this.time = 0;
		initCharacters();
	}

	public Game() {
		this.stage = new Stage();
		this.characters = new ArrayList<>(ConfigurationConstants.GAME_CHARACTERS);
		this.aiManager = new AIManager(ConfigurationConstants.GAME_CHARACTERS);
		this.time = 0;
		initCharacters();
	}

	public Stage getStage() {
		return stage;
	}

	public Collection<GameCharacter> getCharacters() {
		return characters;
	}

	public void initCharacters() {
		final int endInclusive = ConfigurationConstants.GAME_CHARACTERS / 4;
		IntStream
				.rangeClosed(1, endInclusive)
				.forEach(i -> {
							characters.add(new Archer(new Coordinates(i * 50, 50), Player.BOT));
							characters.add(new Bomber(new Coordinates(i * 50, 100), Player.BOT));
							characters.add(new Knight(new Coordinates(i * 50, 150), Player.BOT));
							characters.add(new Mage(new Coordinates(i * 50, 200), Player.BOT));
						}
				);
	}

	public void update() {
		aiManager.updateBots(characters);
		// TODO : update time and commands
		time++;
	}

	public AIManager getAIManager() {
		return aiManager;
	}
}
