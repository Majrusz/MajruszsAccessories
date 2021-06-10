package com.majruszsaccessories.config;

import com.majruszs_difficulty.config.GameStateIntegerConfig;
import com.majruszsaccessories.Integration;
import com.mlib.config.IConfig;
import com.mlib.config.IConfigType;
import com.mlib.config.IntegerConfig;
import net.minecraftforge.common.ForgeConfigSpec;

/** Config with integer value. (or integer value depending on current game state if Majrusz's Progressive Difficulty mod is installed) */
public class IntegrationIntegerConfig implements IConfig {
	protected final IConfigType< Integer > integerConfig;

	public IntegrationIntegerConfig( String name, String comment, int defaultValueNormal, int defaultValueExpert, int defaultValueMaster,
		int minimumValue, int maximumValue
	) {
		if( Integration.isProgressiveDifficultyInstalled() )
			this.integerConfig = new GameStateIntegerConfig( name, comment, defaultValueNormal, defaultValueExpert, defaultValueMaster, minimumValue,
				maximumValue
			);
		else
			this.integerConfig = new IntegerConfig( name, comment, false, defaultValueNormal, minimumValue, maximumValue );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		this.integerConfig.build( builder );
	}

	/** Returns integer value. (depending on current game state if Majrusz's Progressive Difficulty mod is installed) */
	public double getValue() {
		return this.integerConfig.get();
	}
}
