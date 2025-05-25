package dev.joee.btagraves;

import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

public class Config {
	private final TomlConfigHandler handler;

	public Config() {
		Toml defaultConfig = new Toml("BTA Graves configuration file.");

		int startingBlockId = 5678;

		defaultConfig
			.addCategory("BlockIDs")
			.addEntry("graveId", startingBlockId++);

		this.handler = new TomlConfigHandler(BtaGraves.MOD_ID, defaultConfig);
	}

	public int getBlockId(String key) {
		return this.handler.getInt(String.format("BlockIDs.%s", key));
	}
}
