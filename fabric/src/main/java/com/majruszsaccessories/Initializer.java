package com.majruszsaccessories;

import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.contexts.OnGameInitialized;
import com.mlib.registry.Registries;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.Item;

public class Initializer implements ModInitializer {
	@Override
	public void onInitialize() {
		MajruszsAccessories.HELPER.register();
		OnGameInitialized.listen( this::register );
	}

	private void register( OnGameInitialized data ) {
		for( Item item : Registries.getItems() ) {
			if( item instanceof AccessoryItem ) {
				TrinketsApi.registerTrinket( item, new Trinket() {} );
			}
		}
	}
}
