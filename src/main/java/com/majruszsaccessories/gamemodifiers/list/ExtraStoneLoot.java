package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.Random;
import com.mlib.Utility;
import com.mlib.config.DoubleConfig;
import com.mlib.effects.ParticleHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnLootContext;
import com.mlib.gamemodifiers.data.OnLootData;
import com.mlib.levels.LevelHelper;
import com.mlib.text.FormattedTranslatable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.function.Supplier;

public class ExtraStoneLoot extends AccessoryModifier {
	public static final ResourceLocation LOOT_OVERWORLD = Registries.getLocation( "gameplay/lucky_rock_overworld" );
	public static final ResourceLocation LOOT_THE_NETHER = Registries.getLocation( "gameplay/lucky_rock_nether" );
	public static final ResourceLocation LOOT_THE_END = Registries.getLocation( "gameplay/lucky_rock_end" );
	static final String BONUS_KEY = "majruszsaccessories.bonuses.extra_stone_loot";
	final DoubleConfig chance = new DoubleConfig( "extra_loot_chance", "Chance to drop extra items when mining stone.", false, 0.03, 0.0, 1.0 );

	public ExtraStoneLoot( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		OnLootContext onLoot = new OnLootContext( this::addExtraLoot );
		onLoot.addCondition( new Condition.IsServer() )
			.addCondition( data->data.blockState != null && data.blockState.getMaterial() == Material.STONE )
			.addCondition( OnLootContext.HAS_ENTITY )
			.addCondition( OnLootContext.HAS_ORIGIN )
			.addConfig( this.chance );

		this.addContext( onLoot );
	}

	@Override
	public void addTooltip( List< Component > components, AccessoryHandler handler ) {
		FormattedTranslatable component = new FormattedTranslatable( BONUS_KEY, DEFAULT_FORMAT );
		component.addParameter( this.getPercentBonus( this::getDefaultChance, this::getChance, handler ) ).insertInto( components );
	}

	@Override
	public void addDetailedTooltip( List< Component > components, AccessoryHandler handler ) {
		if( Math.abs( this.getDefaultChance() - this.getChance( handler ) ) < 0.001 ) {
			this.addTooltip( components, handler );
			return;
		}

		FormattedTranslatable component = new FormattedTranslatable( BONUS_KEY, DEFAULT_FORMAT );
		component.addParameter( this.getPercentFormula( this::getDefaultChance, this::getChance, handler ) ).insertInto( components );
	}

	private void addExtraLoot( OnLootData data ) {
		AccessoryHandler handler = AccessoryHandler.tryToCreate( ( LivingEntity )data.entity, this.item.get() );
		if( handler == null || !Random.tryChance( this.getChance( handler ) ) ) {
			return;
		}

		data.generatedLoot.addAll( generateLoot( ( LivingEntity )data.entity ) );
		ParticleHandler.AWARD.spawn( data.level, data.origin, 5 );
	}

	private float getDefaultChance() {
		return this.chance.asFloat();
	}

	private float getChance( AccessoryHandler handler ) {
		return handler != null ? ( 1.0f + handler.getBonus() ) * this.getDefaultChance() : 0.0f;
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
}
