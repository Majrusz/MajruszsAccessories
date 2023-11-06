package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnItemBrushed;
import com.mlib.data.Serializable;
import com.mlib.item.LootHelper;
import com.mlib.level.LevelHelper;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BrushingExtraItem extends BonusComponent< AccessoryItem > {
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );

	public static ISupplier< AccessoryItem > create( float chance ) {
		return handler->new BrushingExtraItem( handler, chance );
	}

	protected BrushingExtraItem( BonusHandler< AccessoryItem > handler, float chance ) {
		super( handler );

		this.chance.set( chance, Range.CHANCE );

		OnItemBrushed.listen( this::addExtraLoot )
			.addCondition( CustomConditions.chance( this::getItem, data->data.player, holder->holder.apply( this.chance ) ) );

		this.addTooltip( "majruszsaccessories.bonuses.extra_archaeology_item", TooltipHelper.asPercent( this.chance ) );

		Serializable< ? > config = handler.getConfig();
		config.define( "extra_archaeology_item", this.chance::define );
	}

	private void addExtraLoot( OnItemBrushed data ) {
		float width = EntityType.ITEM.getWidth();
		float height = EntityType.ITEM.getHeight();
		Vec3 itemOffset = AnyPos.from( data.direction ).mul( width, height, width ).mul( 0.5f ).vec3();
		Vec3 start = AnyPos.from( data.blockEntity.getBlockPos() ).center().add( data.direction ).add( itemOffset ).vec3();
		Vec3 end = AnyPos.from( data.blockEntity.getBlockPos() ).center().add( AnyPos.from( data.direction ).mul( 1.5 ) ).vec3();
		List< ItemStack > extraItems = LootHelper.getLootTable( data.location ).getRandomItems( LootHelper.toGiftParams( data.player ) );

		for( ItemStack itemStack : extraItems ) {
			LevelHelper.spawnItemEntityFlyingTowardsDirection( itemStack, data.getLevel(), start, end );
		}
		this.spawnEffects( data );
	}

	private void spawnEffects( OnItemBrushed data ) {
		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 8 )
			.position( AnyPos.from( data.blockEntity.getBlockPos() ).center().add( data.direction ).vec3() )
			.emit( data.getServerLevel() );
	}
}
