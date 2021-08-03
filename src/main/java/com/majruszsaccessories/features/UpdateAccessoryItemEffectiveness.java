package com.majruszsaccessories.features;

import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.events.AnyLootModificationEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Updates effectiveness for accessory items acquired from crafting, loot etc. */
@Mod.EventBusSubscriber
public class UpdateAccessoryItemEffectiveness {
	@SubscribeEvent
	public static void onCrafting( PlayerEvent.ItemCraftedEvent event ) {
		updateEffectiveness( event.getCrafting() );
	}

	@SubscribeEvent
	public static void onAnyLoot( AnyLootModificationEvent event ) {
		for( ItemStack itemStack : event.generatedLoot )
			updateEffectiveness( itemStack );
	}

	/** Sets random effectiveness if item stack does not have any. */
	private static void updateEffectiveness( ItemStack itemStack ) {
		if( !( itemStack.getItem() instanceof AccessoryItem ) )
			return;

		AccessoryItem accessoryItem = ( AccessoryItem )itemStack.getItem();
		if( !AccessoryItem.hasEffectivenessTag( itemStack ) )
			accessoryItem.setRandomEffectiveness( itemStack );
	}
}
