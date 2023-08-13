package com.majruszsaccessories;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod( MajruszsAccessories.MOD_ID )
public class MajruszsAccessories {
	public static final String MOD_ID = "majruszsaccessories";
	public static final String NAME = "Majrusz's Accessories";

	public MajruszsAccessories() {
		Integration.initialize();
		com.majruszsaccessories.Registries.initialize();
		MinecraftForge.EVENT_BUS.register( this );
	}
}
