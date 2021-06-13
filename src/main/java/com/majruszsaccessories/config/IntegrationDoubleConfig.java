package com.majruszsaccessories.config;

import com.majruszs_difficulty.config.GameStateDoubleConfig;
import com.majruszsaccessories.Integration;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IConfig;
import com.mlib.config.IConfigType;
import net.minecraftforge.common.ForgeConfigSpec;

/** Config with double value. (or double value depending on current game state if Majrusz's Progressive Difficulty mod is installed) */
public class IntegrationDoubleConfig implements IConfig {
	protected final IConfigType< Double > doubleConfig;

	public IntegrationDoubleConfig( String name, String comment, double defaultValueNormal, double defaultValueExpert,
		double defaultValueMaster, double minimumValue, double maximumValue
	) {
		if( Integration.isProgressiveDifficultyInstalled() )
			this.doubleConfig = new GameStateDoubleConfig( name, comment, defaultValueNormal, defaultValueExpert, defaultValueMaster, minimumValue,
				maximumValue
			);
		else
			this.doubleConfig = new DoubleConfig( Integration.splitWords( name ), comment, false, defaultValueNormal, minimumValue, maximumValue );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		this.doubleConfig.build( builder );
	}

	/** Returns double value. (depending on current game state if Majrusz's Progressive Difficulty mod is installed) */
	public double getValue() {
		return this.doubleConfig.get();
	}
}
