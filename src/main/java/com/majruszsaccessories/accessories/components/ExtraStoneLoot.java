package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.effects.ParticleHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Priority;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.levels.LevelHelper;
import com.mlib.loot.LootHelper;
import com.mlib.math.Range;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExtraStoneLoot extends AccessoryComponent {
	public static final ResourceLocation LOOT_OVERWORLD = Registries.getLocation( "gameplay/lucky_rock_overworld" );
	public static final ResourceLocation LOOT_THE_NETHER = Registries.getLocation( "gameplay/lucky_rock_nether" );
	public static final ResourceLocation LOOT_THE_END = Registries.getLocation( "gameplay/lucky_rock_end" );
	final DoubleConfig chance;

	public static AccessoryComponent.ISupplier create( double chance ) {
		return ( item, group )->new ExtraStoneLoot( item, group, chance );
	}

	public static AccessoryComponent.ISupplier create() {
		return create( 0.03 );
	}

	protected ExtraStoneLoot( Supplier< AccessoryItem > item, ConfigGroup group, double chance ) {
		super( item );

		this.chance = new DoubleConfig( chance, Range.CHANCE );

		OnStoneMined.listen( this::addExtraLoot )
			.addCondition( CustomConditions.chance( item, data->( LivingEntity )data.entity, holder->holder.apply( this.chance ) ) )
			.addConfig( this.chance.name( "extra_loot_chance" ).comment( "Chance to drop extra items when mining stone." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.extra_stone_loot", TooltipHelper.asPercent( this.chance ) );
	}

	private void addExtraLoot( OnLoot.Data data ) {
		data.generatedLoot.addAll( generateLoot( ( LivingEntity )data.entity ) );
		ParticleHandler.AWARD.spawn( data.getServerLevel(), data.origin, 5 );
	}

	private static List< ItemStack > generateLoot( LivingEntity entity ) {
		return LootHelper.getLootTable( getLootTableLocation( entity ) )
			.getRandomItems( LootHelper.toGiftParams( entity, getLootTableLocation( entity ) ) );
	}

	private static ResourceLocation getLootTableLocation( LivingEntity entity ) {
		if( LevelHelper.isEntityIn( entity, Level.NETHER ) ) {
			return LOOT_THE_NETHER;
		} else if( LevelHelper.isEntityIn( entity, Level.END ) ) {
			return LOOT_THE_END;
		}

		return LOOT_OVERWORLD;
	}

	public static class OnStoneMined {
		public static Context< OnLoot.Data > listen( Consumer< OnLoot.Data > consumer ) {
			return OnLoot.listen( consumer )
				.addCondition( Condition.isServer() )
				.addCondition( Condition.predicate( data->data.blockState != null && ( data.blockState.is( BlockTags.BASE_STONE_OVERWORLD ) || data.blockState.is( BlockTags.BASE_STONE_NETHER ) || data.blockState.is( Blocks.END_STONE ) ) ) )
				.addCondition( Condition.< OnLoot.Data > predicate( data->data.entity instanceof LivingEntity ).priority( Priority.HIGH ) )
				.addCondition( OnLoot.hasOrigin() );
		}
	}
}
