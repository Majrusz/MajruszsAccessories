package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.IntegerConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnPlayerTickContext;
import com.mlib.gamemodifiers.data.OnPlayerTickData;
import com.mlib.text.FormattedTranslatable;
import net.minecraft.network.chat.Component;
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
	public void addTooltip( List< Component > components, AccessoryHandler handler ) {
		FormattedTranslatable component = new FormattedTranslatable( BONUS_KEY, DEFAULT_FORMAT );
		component.addParameter( this.getBonus( this::getDefaultLuck, this::getLuck, handler ) ).insertInto( components );
	}

	@Override
	public void addDetailedTooltip( List< Component > components, AccessoryHandler handler ) {
		if( this.getDefaultLuck() == this.getLuck( handler ) ) {
			this.addTooltip( components, handler );
			return;
		}

		FormattedTranslatable component = new FormattedTranslatable( BONUS_KEY, DEFAULT_FORMAT );
		component.addParameter( this.getFormula( this::getDefaultLuck, this::getLuck, handler ) ).insertInto( components );
	}

	private void updateLuck( OnPlayerTickData data ) {
		AccessoryHandler handler = AccessoryHandler.tryToCreate( data.player, this.item.get() );
		int luckBonus = data.player.fishing != null ? this.getLuck( handler ) : 0;
		LUCK.setValueAndApply( data.player, luckBonus );
	}

	private int getDefaultLuck() {
		return this.luck.get();
	}

	private int getLuck( AccessoryHandler handler ) {
		return handler != null ? Math.round( ( 1.0f + handler.getBonus() ) * this.getDefaultLuck() ) : 0;
	}
}
