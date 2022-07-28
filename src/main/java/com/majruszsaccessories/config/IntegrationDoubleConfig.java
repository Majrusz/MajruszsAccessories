package com.majruszsaccessories.config;

import com.majruszsaccessories.Integration;
import com.majruszsdifficulty.config.GameStageDoubleConfig;
import com.mlib.config.DoubleConfig;

public class IntegrationDoubleConfig extends IntegrationConfig< Double > {
	public IntegrationDoubleConfig( String name, String comment, double normal, double expert, double master, double min, double max ) {
		super( Integration.isProgressiveDifficultyInstalled() ? new GameStageDoubleConfig( name, comment, normal, expert, master, min, max ) : new DoubleConfig( Integration.splitWords( name ), comment, false, normal, min, max ) );
	}
}
