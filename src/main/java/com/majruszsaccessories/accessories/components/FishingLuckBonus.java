package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.ConfigGroup;
import com.mlib.config.IntegerConfig;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.OnPlayerTick;
import com.mlib.math.Range;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;

import java.util.function.Supplier;

public class FishingLuckBonus extends AccessoryComponent {
	static final AttributeHandler LUCK_ATTRIBUTE = new AttributeHandler( "4010270c-9d57-4273-8a41-00985f1e4781", "FishingLuckBonus", Attributes.LUCK, AttributeModifier.Operation.ADDITION );
	final IntegerConfig luck;

	public static AccessoryComponent.ISupplier create( int luck ) {
		return ( item, group )->new FishingLuckBonus( item, group, luck );
	}

	public static AccessoryComponent.ISupplier create() {
		return create( 3 );
	}

	protected FishingLuckBonus( Supplier< AccessoryItem > item, ConfigGroup group, int luck ) {
		super( item );

		this.luck = new IntegerConfig( luck, new Range<>( 1, 10 ) );

		OnPlayerTick.listen( this::updateLuck )
			.addCondition( Condition.< OnPlayerTick.Data > cooldown( 4, Dist.DEDICATED_SERVER ).configurable( false ) )
			.addConfig( this.luck.name( "fishing_luck" ).comment( "Luck bonus when fishing." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.fishing_luck", TooltipHelper.asValue( this.luck ) );
	}

	private void updateLuck( OnPlayerTick.Data data ) {
		LUCK_ATTRIBUTE.setValue( this.getLuck( data.player ) ).apply( data.player );
	}

	private int getLuck( Player player ) {
		if( player.fishing == null ) {
			return 0;
		}

		AccessoryHolder holder = AccessoryHolder.find( player, this.item.get() );
		return holder.isValid() ? holder.apply( this.luck ) : 0;
	}
}
