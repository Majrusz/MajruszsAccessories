package com.majruszsaccessories.config;

import com.majruszsaccessories.Integration;
import com.majruszsdifficulty.config.GameStageIntegerConfig;
import com.mlib.config.IntegerConfig;

public class IntegrationIntegerConfig extends IntegrationConfig< Integer > {
	public IntegrationIntegerConfig( String name, String comment, int normal, int expert, int master, int min, int max ) {
		super( Integration.isProgressiveDifficultyInstalled() ? new GameStageIntegerConfig( name, comment, normal, expert, master, min, max ) : new IntegerConfig( Integration.splitWords( name ), comment, false, normal, min, max ) );
	}
}
