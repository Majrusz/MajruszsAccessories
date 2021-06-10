package com.majruszsaccessories.items;

import com.majruszsaccessories.Instances;
import com.majruszsaccessories.config.IntegrationIntegerConfig;
import com.mlib.attributes.AttributeHandler;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Emblem that increases luck for the player that is currently fishing. */
@Mod.EventBusSubscriber
public class FishermanEmblemItem extends AccessoryItem {
	protected static final AttributeHandler LUCK_ATTRIBUTE = new AttributeHandler( "4010270c-9d57-4273-8a41-00985f1e4781", "FishermanEmblemLuckBonus",
		Attributes.LUCK, AttributeModifier.Operation.ADDITION
	);
	protected final IntegrationIntegerConfig luck;

	public FishermanEmblemItem() {
		super( "Fisherman Emblem", "fisherman_emblem" );

		String luckComment = "Luck bonus when fishing.";
		this.luck = new IntegrationIntegerConfig( "Luck", luckComment, 3, 4, 5, 1, 100 );
		this.group.addConfig( this.luck );
	}

	@SubscribeEvent
	public static void increaseLuck( TickEvent.PlayerTickEvent event ) {
		PlayerEntity player = event.player;

		LUCK_ATTRIBUTE.setValueAndApply( player, Instances.FISHERMAN_EMBLEM_ITEM.getEmblemLuckBonus( player ) );
	}

	/** Returns current luck bonus. (whether player has emblem and is fishing or not) */
	public int getEmblemLuckBonus( PlayerEntity player ) {
		return player.fishing != null && hasAny( player ) ? getLuckBonus( player ) : 0;
	}

	/** Returns total luck bonus. */
	public int getLuckBonus( PlayerEntity player ) {
		return ( int )Math.round( this.luck.getValue() * ( 1.0 + getHighestEffectiveness( player ) ) );
	}
}
