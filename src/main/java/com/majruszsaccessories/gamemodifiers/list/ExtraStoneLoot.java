package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.effects.ParticleHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.levels.LevelHelper;
import com.mlib.math.Range;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExtraStoneLoot extends AccessoryModifier {
	public static final ResourceLocation LOOT_OVERWORLD = Registries.getLocation( "gameplay/lucky_rock_overworld" );
	public static final ResourceLocation LOOT_THE_NETHER = Registries.getLocation( "gameplay/lucky_rock_nether" );
	public static final ResourceLocation LOOT_THE_END = Registries.getLocation( "gameplay/lucky_rock_end" );
	final AccessoryPercent chance;

	public ExtraStoneLoot( Supplier< ? extends AccessoryItem > item, String configKey ) {
		this( item, configKey, 0.03 );
	}

	public ExtraStoneLoot( Supplier< ? extends AccessoryItem > item, String configKey, double chance ) {
		super( item, configKey );

		this.chance = new AccessoryPercent( chance, Range.CHANCE );

		new OnStoneMinedContext( this.toAccessoryConsumer( this::addExtraLoot, this.chance ) )
			.addConfig( this.chance.name( "extra_loot_chance" ).comment( "Chance to drop extra items when mining stone." ) )
			.insertTo( this );

		this.addTooltip( this.chance, "majruszsaccessories.bonuses.extra_stone_loot" );
	}

	private void addExtraLoot( OnLoot.Data data, AccessoryHandler handler ) {
		data.generatedLoot.addAll( generateLoot( ( LivingEntity )data.entity ) );
		ParticleHandler.AWARD.spawn( data.level, data.origin, 5 );
	}

	private static List< ItemStack > generateLoot( LivingEntity entity ) {
		return ServerLifecycleHooks.getCurrentServer()
			.getLootTables()
			.get( getLootTableLocation( entity ) )
			.getRandomItems( generateLootContext( entity ) );
	}

	private static ResourceLocation getLootTableLocation( LivingEntity entity ) {
		if( LevelHelper.isEntityIn( entity, Level.NETHER ) ) {
			return LOOT_THE_NETHER;
		} else if( LevelHelper.isEntityIn( entity, Level.END ) ) {
			return LOOT_THE_END;
		}

		return LOOT_OVERWORLD;
	}

	private static LootContext generateLootContext( LivingEntity entity ) {
		return new LootContext.Builder( ( ServerLevel )entity.level ).withParameter( LootContextParams.ORIGIN, entity.position() )
			.withParameter( LootContextParams.THIS_ENTITY, entity )
			.create( LootContextParamSets.GIFT );
	}

	public static class OnStoneMinedContext extends OnLoot.Context {
		public OnStoneMinedContext( Consumer< OnLoot.Data > consumer ) {
			super( consumer );

			this.addCondition( new Condition.IsServer<>() )
				.addCondition( data->data.blockState != null && data.blockState.getMaterial() == Material.STONE )
				.addCondition( OnLoot.HAS_ENTITY )
				.addCondition( OnLoot.HAS_ORIGIN );
		}
	}
}
