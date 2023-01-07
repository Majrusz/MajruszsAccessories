package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryInteger;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.attributes.AttributeHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnPlayerTick;
import com.mlib.math.Range;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;

import java.util.function.Supplier;

public class FishingLuckBonus extends AccessoryModifier {
	static final AttributeHandler LUCK_ATTRIBUTE = new AttributeHandler( "4010270c-9d57-4273-8a41-00985f1e4781", "FishingLuckBonus", Attributes.LUCK, AttributeModifier.Operation.ADDITION );
	final AccessoryInteger luck;

	public FishingLuckBonus( Supplier< ? extends AccessoryItem > item, String configKey ) {
		this( item, configKey, 3 );
	}

	public FishingLuckBonus( Supplier< ? extends AccessoryItem > item, String configKey, int luck ) {
		super( item, configKey );

		this.luck = new AccessoryInteger( luck, new Range<>( 1, 10 ) );

		new OnPlayerTick.Context( this::updateLuck )
			.addCondition( new Condition.Cooldown< OnPlayerTick.Data >( 4, Dist.DEDICATED_SERVER ).configurable( false ) )
			.addConfig( this.luck.name( "fishing_luck" ).comment( "Luck bonus during fishing." ) )
			.insertTo( this );

		this.addTooltip( this.luck, "majruszsaccessories.bonuses.fishing_luck" );
	}

	private void updateLuck( OnPlayerTick.Data data ) {
		LUCK_ATTRIBUTE.setValueAndApply( data.player, this.getLuck( data.player ) );
	}

	private int getLuck( Player player ) {
		if( player.fishing == null ) {
			return 0;
		}

		AccessoryHandler handler = AccessoryHandler.tryToCreate( player, this.item.get() );
		return handler != null ? this.luck.getValue( handler ) : 0;
	}
}
