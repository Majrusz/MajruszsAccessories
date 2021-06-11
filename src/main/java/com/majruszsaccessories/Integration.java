package com.majruszsaccessories;

import net.minecraftforge.fml.ModList;

/** Class that handles integration with optional dependencies. */
public class Integration {
	private static boolean IS_CURIOS_INSTALLED, IS_PROGRESSIVE_DIFFICULTY_INSTALLED;

	/** Initializes all required information about optional dependencies. */
	public static void initialize() {
		ModList modList = ModList.get();

		IS_CURIOS_INSTALLED = modList.isLoaded( "curios" );
		IS_PROGRESSIVE_DIFFICULTY_INSTALLED = modList.isLoaded( "majruszs_difficulty" );
	}

	/** Returns whether Curios mod is installed. */
	public static boolean isCuriosInstalled() {
		return IS_CURIOS_INSTALLED;
	}

	/** Returns whether Majrusz's Progressive Difficulty mod is installed. */
	public static boolean isProgressiveDifficultyInstalled() {
		return IS_PROGRESSIVE_DIFFICULTY_INSTALLED;
	}
}
