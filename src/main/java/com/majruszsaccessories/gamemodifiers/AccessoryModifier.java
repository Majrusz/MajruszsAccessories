package com.majruszsaccessories.gamemodifiers;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.Random;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnLoot;
import com.mlib.levels.LevelHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AccessoryModifier extends GameModifier {
	protected final Supplier< ? extends AccessoryItem > item;
	protected final List< Data > dataList = new ArrayList<>();

	public AccessoryModifier( Supplier< ? extends AccessoryItem > item, String configKey, String configName, String configComment ) {
		super( configKey, configName, configComment );
		this.item = item;
	}

	public AccessoryModifier addTooltip( String key ) {
		this.dataList.add( new Data( key ) );

		return this;
	}

	public AccessoryModifier addTooltip( IAccessoryTooltip tooltip, String key ) {
		this.dataList.add( new Data( tooltip, key ) );

		return this;
	}

	public void buildTooltip( List< Component > components, AccessoryHandler handler ) {
		for( Data data : this.dataList ) {
			data.tooltip.addTooltip( data.key, components, handler );
		}
	}

	public void buildDetailedTooltip( List< Component > components, AccessoryHandler handler ) {
		for( Data data : this.dataList ) {
			if( data.tooltip.areTooltipsIdentical( handler ) ) {
				data.tooltip.addTooltip( data.key, components, handler );
			} else {
				data.tooltip.addDetailedTooltip( data.key, components, handler );
			}
		}
	}

	protected ItemStack constructItemStack() {
		return AccessoryHandler.setup( new ItemStack( this.item.get() ), AccessoryHandler.randomBonus() ).getItemStack();
	}

	protected void addToGeneratedLoot( OnLoot.Data data ) {
		data.generatedLoot.add( this.constructItemStack() );
	}

	protected void spawnFlyingItem( Level level, Vec3 from, Vec3 to ) {
		LevelHelper.spawnItemEntityFlyingTowardsDirection( this.constructItemStack(), level, from, to );
	}

	protected < DataType extends ContextData > Consumer< DataType > toAccessoryConsumer( BiConsumer< DataType, AccessoryHandler > consumer,
		Function< DataType, LivingEntity > entityGetter, AccessoryPercent... chances
	) {
		return data->{
			LivingEntity entity = entityGetter.apply( data );
			AccessoryHandler handler = entity != null ? AccessoryHandler.tryToCreate( entity, this.item.get() ) : null;
			if( handler == null ) {
				return;
			}

			for( AccessoryPercent chance : chances ) {
				if( !Random.tryChance( chance.getValue( handler ) ) ) {
					return;
				}
			}

			consumer.accept( data, handler );
		};
	}

	protected < DataType extends ContextData > Consumer< DataType > toAccessoryConsumer( BiConsumer< DataType, AccessoryHandler > consumer,
		AccessoryPercent... chances
	) {
		return this.toAccessoryConsumer( consumer, data->data.entity, chances );
	}

	record Data( IAccessoryTooltip tooltip, String key ) {
		final static IAccessoryTooltip DEFAULT_TOOLTIP = new IAccessoryTooltip() {
			@Override
			public void addTooltip( String key, List< Component > components, AccessoryHandler handler ) {
				IAccessoryTooltip.build( key, IAccessoryTooltip.DEFAULT_FORMAT ).insertInto( components );
			}
		};

		public Data( String key ) {
			this( DEFAULT_TOOLTIP, key );
		}
	}
}
