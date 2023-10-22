package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.collection.DefaultMap;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.data.Serializable;
import com.mlib.emitter.ParticleEmitter;
import com.mlib.item.LootHelper;
import com.mlib.math.Range;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;
import java.util.function.Consumer;

public class MiningExtraItem extends BonusComponent< AccessoryItem > {
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );
	Map< String, ResourceLocation > lootIds = DefaultMap.of(
		DefaultMap.entry( "default", MajruszsAccessories.HELPER.getLocation( "gameplay/lucky_rock_default" ) ),
		DefaultMap.entry( "minecraft:the_nether", MajruszsAccessories.HELPER.getLocation( "gameplay/lucky_rock_nether" ) ),
		DefaultMap.entry( "minecraft:the_end", MajruszsAccessories.HELPER.getLocation( "gameplay/lucky_rock_end" ) )
	);

	public static ISupplier< AccessoryItem > create( float chance ) {
		return handler->new MiningExtraItem( handler, chance );
	}

	protected MiningExtraItem( BonusHandler< AccessoryItem > handler, float chance ) {
		super( handler );

		this.chance.set( chance, Range.CHANCE );

		OnStoneMined.listen( this::addExtraLoot )
			.addCondition( CustomConditions.chance( this::getItem, data->( LivingEntity )data.entity, holder->holder.apply( this.chance ) ) );

		this.addTooltip( "majruszsaccessories.bonuses.extra_stone_loot", TooltipHelper.asPercent( this.chance ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "extra_mining_item", subconfig->{
			this.chance.define( subconfig );
			subconfig.defineLocation( "loot_ids", ()->this.lootIds, x->this.lootIds = DefaultMap.of( x ) );
		} );
	}

	private void addExtraLoot( OnLootGenerated data ) {
		LivingEntity entity = ( LivingEntity )data.entity;
		ResourceLocation id = this.lootIds.get( entity.level().dimension().location().toString() );

		data.generatedLoot.addAll( LootHelper.getLootTable( id ).getRandomItems( LootHelper.toGiftParams( entity ) ) );
		this.spawnEffects( data );
	}

	private void spawnEffects( OnLootGenerated data ) {
		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 3 )
			.offset( ParticleEmitter.offset( 0.2f ) )
			.emit( data.getServerLevel(), data.origin );
	}

	public static class OnStoneMined {
		public static Context< OnLootGenerated > listen( Consumer< OnLootGenerated > consumer ) {
			return OnLootGenerated.listen( consumer )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( data->data.blockState != null )
				.addCondition( data->data.blockState.is( BlockTags.BASE_STONE_OVERWORLD ) || data.blockState.is( BlockTags.BASE_STONE_NETHER ) || data.blockState.is( Blocks.END_STONE ) )
				.addCondition( data->data.entity instanceof LivingEntity )
				.addCondition( data->data.origin != null );
		}
	}
}
