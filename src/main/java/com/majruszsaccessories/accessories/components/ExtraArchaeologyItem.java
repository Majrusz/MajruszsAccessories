package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnItemBrushed;
import com.mlib.effects.ParticleHandler;
import com.mlib.levels.LevelHelper;
import com.mlib.loot.LootHelper;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

public class ExtraArchaeologyItem extends AccessoryComponent {
	final DoubleConfig chance;

	public static ISupplier create( double chance ) {
		return ( item, group )->new ExtraArchaeologyItem( item, group, chance );
	}

	public static ISupplier create() {
		return create( 0.1 );
	}

	protected ExtraArchaeologyItem( Supplier< AccessoryItem > item, ConfigGroup group, double chance ) {
		super( item );

		this.chance = new DoubleConfig( chance, Range.CHANCE );

		OnItemBrushed.listen( this::addExtraLoot )
			.addCondition( CustomConditions.chance( item, data->data.player, holder->holder.apply( this.chance ) ) )
			.addConfig( this.chance.name( "extra_archaeology_chance" ).comment( "Chance to drop extra item when brushing suspicious blocks." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.extra_archaeology_item", TooltipHelper.asPercent( this.chance ) );
	}

	private void addExtraLoot( OnItemBrushed.Data data ) {
		Vec3 itemOffset = AnyPos.from( data.direction )
			.mul( EntityType.ITEM.getWidth(), EntityType.ITEM.getHeight(), EntityType.ITEM.getWidth() )
			.mul( 0.5f )
			.vec3();
		Vec3 start = AnyPos.from( data.blockEntity.getBlockPos() ).center().add( data.direction ).add( itemOffset ).vec3();
		Vec3 end = AnyPos.from( data.blockEntity.getBlockPos() ).center().add( AnyPos.from( data.direction ).mul( 1.5 ) ).vec3();
		List< ItemStack > extraItems = LootHelper.getLootTable( data.location ).getRandomItems( LootHelper.toGiftParams( data.player ) );

		for( ItemStack itemStack : extraItems ) {
			LevelHelper.spawnItemEntityFlyingTowardsDirection( itemStack, data.getLevel(), start, end );
		}
		ParticleHandler.AWARD.spawn( data.getServerLevel(), AnyPos.from( data.blockEntity.getBlockPos() ).center().add( data.direction ).vec3(), 5 );
	}
}
