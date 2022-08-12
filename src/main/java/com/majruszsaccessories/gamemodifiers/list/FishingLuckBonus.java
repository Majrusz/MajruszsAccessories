package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.MajruszLibrary;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.IntegerConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnPlayerTickContext;
import com.mlib.gamemodifiers.data.OnPlayerTickData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;

import java.util.List;
import java.util.function.Supplier;

public class FishingLuckBonus extends AccessoryModifier {
	static final AttributeHandler LUCK = new AttributeHandler( "4010270c-9d57-4273-8a41-00985f1e4781", "FishingLuckBonus", Attributes.LUCK, AttributeModifier.Operation.ADDITION );
	static final String BONUS_KEY = "majruszsaccessories.bonuses.fishing_luck";
	final IntegerConfig luck = new IntegerConfig( "fishing_luck", "Luck bonus during fishing.", false, 3, 1, 10 );

	public FishingLuckBonus( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		OnPlayerTickContext onTick = new OnPlayerTickContext( this::updateLuck );
		onTick.addCondition( new Condition.Cooldown( 4, Dist.DEDICATED_SERVER, false ) );

		this.addContext( onTick );
		this.addConfig( this.luck );
	}

	@Override
	public void addTooltip( List< MutableComponent > components, AccessoryHandler handler ) {
		components.add( Component.translatable( BONUS_KEY, this.getLuckBonus( handler ) ) );
	}

	private void updateLuck( OnPlayerTickData data ) {
		AccessoryHandler handler = AccessoryHandler.tryToCreate( data.player, this.item.get() );
		MajruszLibrary.log( "%d", this.getLuckBonus( handler ) );
		LUCK.setValueAndApply( data.player, this.getLuckBonus( handler ) );
	}

	private int getLuckBonus( AccessoryHandler handler ) {
		return handler != null ? Math.round( ( 1.0f + handler.getBonus() ) * this.luck.get() ) : 0;
	}
}
