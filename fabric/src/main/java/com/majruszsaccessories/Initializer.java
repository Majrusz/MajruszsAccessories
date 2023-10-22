package com.majruszsaccessories;

import net.fabricmc.api.ModInitializer;

public class Initializer implements ModInitializer {
	@Override
	public void onInitialize() {
		MajruszsAccessories.HELPER.register();
	}
}
