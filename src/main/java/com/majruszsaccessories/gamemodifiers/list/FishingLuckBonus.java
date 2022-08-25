package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryInteger;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.attributes.AttributeHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnPlayerTickContext;
import com.mlib.gamemodifiers.data.OnPlayerTickData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;

import java.util.function.Supplier;

public class FishingLuckBonus extends AccessoryModifier {
	static final AttributeHandler LUCK_ATTRIBUTE = new AttributeHandler( "4010270c-9d57-4273-8a41-00985f1e4781", "FishingLuckBonus", Attributes.LUCK, AttributeModifier.Operation.ADDITION );
	final AccessoryInteger luck = new AccessoryInteger( "fishing_luck", "Luck bonus during fishing.", false, 3, 1, 10 );

	public FishingLuckBonus( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		OnPlayerTickContext onTick = new OnPlayerTickContext( this.toAccessoryConsumer( this::updateLuck ) );
		onTick.addCondition( new Condition.Cooldown( 4, Dist.DEDICATED_SERVER, false ) ).addConfig( this.luck );

		this.addContext( onTick );
		this.addTooltip( this.luck, "majruszsaccessories.bonuses.fishing_luck" );
	}

	private void updateLuck( OnPlayerTickData data, AccessoryHandler handler ) {
		int luckBonus = data.player.fishing != null ? this.luck.getValue( handler ) : 0;
		LUCK_ATTRIBUTE.setValueAndApply( data.player, luckBonus );
	}
}
