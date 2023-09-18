package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.config.IntegerConfig;
import com.mlib.contexts.OnExtraFishingLootCheck;
import com.mlib.effects.ParticleHandler;
import com.mlib.loot.LootHelper;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;
import java.util.function.Supplier;

public class ExtraFishingItems extends AccessoryComponent {
	final DoubleConfig chance;
	final IntegerConfig count;

	public static ISupplier create( double chance, int count ) {
		return ( item, group )->new ExtraFishingItems( item, group, chance, count );
	}

	public static ISupplier create() {
		return create( 0.2, 2 );
	}

	protected ExtraFishingItems( Supplier< AccessoryItem > item, ConfigGroup group, double chance, int count ) {
		super( item );

		this.chance = new DoubleConfig( chance, Range.CHANCE );
		this.count = new IntegerConfig( count, new Range<>( 2, 10 ) );

		OnExtraFishingLootCheck.listen( this::addExtraFishes )
			.addCondition( CustomConditions.chance( item, data->data.player, holder->holder.apply( this.chance ) ) )
			.addConfig( this.chance.name( "extra_fishing_item_chance" ).comment( "Chance to get some extra fishes while fishing." ) )
			.addConfig( this.count.name( "extra_fishing_item_count" ).comment( "Number of additional fish that can be caught." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.extra_fishing_items", TooltipHelper.asPercent( this.chance ), TooltipHelper.asValue( this.count ) );
	}

	private void addExtraFishes( OnExtraFishingLootCheck.Data data ) {
		int count = AccessoryHolder.find( data.player, item.get() ).apply( this.count ) - 1;
		for( int idx = 0; idx < count; ++idx ) {
			List< ItemStack > loot = LootHelper.getLootTable( BuiltInLootTables.FISHING_FISH )
				.getRandomItems( LootHelper.toGiftParams( data.player ) );

			data.extraLoot.addAll( loot );
		}
		ParticleHandler.AWARD.spawn( data.getServerLevel(), AnyPos.from( data.hook.position() ).add( 0.0, 1.0, 0.0 ).vec3(), 5 );
	}
}
