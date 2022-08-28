package com.majruszsaccessories;

import com.mlib.config.ConfigHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod( MajruszsAccessories.MOD_ID )
public class MajruszsAccessories {
	public static final String MOD_ID = "majruszsaccessories";
	public static final String NAME = "Majrusz's Accessories";
	public static final ConfigHandler SERVER_CONFIG = new ConfigHandler( ModConfig.Type.SERVER );

	public MajruszsAccessories() {
		Integration.initialize();
		com.majruszsaccessories.Registries.initialize();
		MinecraftForge.EVENT_BUS.register( this );
	}
}
