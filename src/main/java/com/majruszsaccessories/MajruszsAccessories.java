package com.majruszsaccessories;

import com.mlib.config.ConfigHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod( MajruszsAccessories.MOD_ID )
public class MajruszsAccessories {
	public static final String MOD_ID = "majruszsaccessories";
	public static final String NAME = "Majrusz's Accessories";
	public static final ConfigHandler CONFIG_HANDLER = new ConfigHandler( ModConfig.Type.COMMON, "common.toml", MOD_ID );

	public MajruszsAccessories() {
		Integration.initialize();
		com.majruszsaccessories.Registries.initialize();
		MinecraftForge.EVENT_BUS.register( this );
	}
}
