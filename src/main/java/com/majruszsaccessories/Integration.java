package com.majruszsaccessories;

import net.minecraftforge.fml.ModList;

public class Integration {
	static boolean IS_CURIOS_INSTALLED, IS_PROGRESSIVE_DIFFICULTY_INSTALLED;

	public static void initialize() {
		ModList modList = ModList.get();

		IS_CURIOS_INSTALLED = modList.isLoaded( "curios" );
		IS_PROGRESSIVE_DIFFICULTY_INSTALLED = modList.isLoaded( "majruszsdifficulty" );
	}

	public static boolean isCuriosInstalled() {
		return false && IS_CURIOS_INSTALLED;
	}

	public static boolean isProgressiveDifficultyInstalled() {
		return IS_PROGRESSIVE_DIFFICULTY_INSTALLED;
	}
}
