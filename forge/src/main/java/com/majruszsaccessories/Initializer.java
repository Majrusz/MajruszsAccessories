package com.majruszsaccessories;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod( MajruszsAccessories.MOD_ID )
public class Initializer {
	public Initializer() {
		MajruszsAccessories.HELPER.register();
		MinecraftForge.EVENT_BUS.register( this );
	}
}
