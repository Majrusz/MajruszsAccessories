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
import com.mlib.text.TextHelper;
import net.minecraft.ChatFormatting;
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
		int bonus = this.getLuckBonus( handler );

		FormattedTranslatable component = new FormattedTranslatable( BONUS_KEY, ChatFormatting.GRAY );
		component.addParameter( String.format( "%d", bonus ), bonus != this.luck.get() ? handler.getBonusFormatting() : ChatFormatting.GRAY )
			.insertInto( components );
	}

	@Override
	public void addDetailedTooltip( List< Component > components, AccessoryHandler handler ) {
		int defaultBonus = this.luck.get();
		int bonus = this.getLuckBonus( handler );
		if( bonus == defaultBonus ) {
			this.addTooltip( components, handler );
			return;
		}

		FormattedTranslatable formula = new FormattedTranslatable( FORMULA_KEY, ChatFormatting.GRAY );
		formula.addParameter( String.format( "%d", defaultBonus ) )
			.addParameter( TextHelper.signed( bonus - defaultBonus ), handler.getBonusFormatting() );
		FormattedTranslatable component = new FormattedTranslatable( BONUS_KEY, ChatFormatting.GRAY );
		component.addParameter( formula.create() ).insertInto( components );
	}

	private void updateLuck( OnPlayerTickData data ) {
		AccessoryHandler handler = AccessoryHandler.tryToCreate( data.player, this.item.get() );
		int luckBonus = data.player.fishing != null ? this.getLuckBonus( handler ) : 0;
		LUCK.setValueAndApply( data.player, luckBonus );
	}

	private int getLuckBonus( AccessoryHandler handler ) {
		return handler != null ? Math.round( ( 1.0f + handler.getBonus() ) * this.luck.get() ) : 0;
	}
}
