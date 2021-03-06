package com.majruszsaccessories;

import com.majruszsaccessories.items.FishermanEmblemItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;

/** Class that handles integration with optional dependencies. */
public class Integration {
	private static boolean IS_CURIOS_INSTALLED, IS_PROGRESSIVE_DIFFICULTY_INSTALLED;

	/** Initializes all required information about optional dependencies. */
	public static void initialize() {
		ModList modList = ModList.get();

		IS_CURIOS_INSTALLED = modList.isLoaded( "curios" );
		IS_PROGRESSIVE_DIFFICULTY_INSTALLED = modList.isLoaded( "majruszs_difficulty" );

		IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
		if( Integration.isProgressiveDifficultyInstalled() )
			forgeEventBus.addListener( FishermanEmblemItem::addToTreasureBag );
	}

	/** Returns whether Curios mod is installed. */
	public static boolean isCuriosInstalled() {
		return IS_CURIOS_INSTALLED;
	}

	/** Returns whether Majrusz's Progressive Difficulty mod is installed. */
	public static boolean isProgressiveDifficultyInstalled() {
		return IS_PROGRESSIVE_DIFFICULTY_INSTALLED;
	}

	/**
	 Splits words in string, adds underscore between them and converts to lower case.
	 For example: "AbcDefGhi" -> "abc_def_ghi"
	 */
	public static String splitWords( String name ) {
		String lowerCaseName = name.toLowerCase();
		StringBuilder output = new StringBuilder();
		for( int i = 0; i < name.length(); ++i ) {
			output.append( lowerCaseName.charAt( i ) );
			if( i + 1 < name.length() && lowerCaseName.charAt( i ) == name.charAt( i ) && lowerCaseName.charAt( i + 1 ) != name.charAt( i + 1 ) )
				output.append( "_" );
		}

		return output.toString();
	}
}
