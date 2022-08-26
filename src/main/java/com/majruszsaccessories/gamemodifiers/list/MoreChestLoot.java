package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.gamemodifiers.configs.IAccessoryTooltip;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.MajruszLibrary;
import com.mlib.Random;
import com.mlib.blocks.BlockHelper;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnLootContext;
import com.mlib.gamemodifiers.data.OnLootData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

import java.util.List;
import java.util.function.Supplier;

public class MoreChestLoot extends AccessoryModifier {
	static final int BLOCKS_DISTANCE = 4800;
	final ChestChance sizeMultiplier = new ChestChance( "chest_size_bonus", "Extra multiplier for number of items acquired from chests.", false, 1.2, 0.0, 10.0 );

	public MoreChestLoot( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		OnLootContext onLoot = new OnLootContext( this.toAccessoryConsumer( this::increaseLoot ) );
		onLoot.addCondition( new Condition.IsServer() )
			.addCondition( OnLootContext.HAS_ORIGIN )
			.addCondition( data->BlockHelper.getBlockEntity( data.level, data.origin ) instanceof RandomizableContainerBlockEntity )
			.addCondition( data->data.entity instanceof LivingEntity )
			.addConfig( this.sizeMultiplier );

		this.addContext( onLoot );
		this.addTooltip( this.sizeMultiplier, "majruszsaccessories.bonuses.more_chest_loot" );
	}

	private void increaseLoot( OnLootData data, AccessoryHandler handler ) {
		for( ItemStack itemStack : data.generatedLoot ) {
			int count = Random.roundRandomly( ( 1.0f + this.sizeMultiplier.getValue( handler ) ) * itemStack.getCount() );
			MajruszLibrary.log( "%s -> %d ()", itemStack.toString(), count );
			itemStack.setCount( Math.min( count, itemStack.getMaxStackSize() ) );
		}
	}

	static class ChestChance extends AccessoryPercent {
		public ChestChance( String name, String comment, boolean worldRestartRequired, double defaultValue, double min, double max ) {
			super( name, comment, worldRestartRequired, defaultValue, min, max );
		}

		@Override
		public void addTooltip( String key, List< Component > components, AccessoryHandler handler ) {
			int blocksPerPercent = Math.round( BLOCKS_DISTANCE / ( this.getDefaultValue() * 100.0f ) );
			IAccessoryTooltip.build( key, DEFAULT_FORMAT )
				.addParameter( this.getPercentBonus( ()->0.01f, _handler->( 1.0f + handler.getBonus() )/100.0f, handler ) )
				.addParameter( Component.literal( "" + blocksPerPercent ).withStyle( DEFAULT_FORMAT ) )
				.addParameter( this.getPercentBonus( this::getDefaultValue, this::getValue, handler ) )
				.insertInto( components );
		}

		@Override
		public void addDetailedTooltip( String key, List< Component > components, AccessoryHandler handler ) {
			int blocksPerPercent = Math.round( BLOCKS_DISTANCE / ( this.getDefaultValue() * 100.0f ) );
			IAccessoryTooltip.build( key, DEFAULT_FORMAT )
				.addParameter( this.getPercentFormula( ()->0.01f, _handler->( 1.0f + handler.getBonus() )/100.0f, handler ) )
				.addParameter( Component.literal( "" + blocksPerPercent ).withStyle( DEFAULT_FORMAT ) )
				.addParameter( this.getPercentFormula( this::getDefaultValue, this::getValue, handler ) )
				.insertInto( components );
		}
	}
}
