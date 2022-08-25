package com.majruszsaccessories.gamemodifiers;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.gamemodifiers.configs.IAccessoryConfig;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.Random;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.data.OnLootData;
import com.mlib.text.FormattedTranslatable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AccessoryModifier extends GameModifier {
	protected final Supplier< ? extends AccessoryItem > item;
	protected final List< BiConsumer< List< Component >, AccessoryHandler > > tooltip = new ArrayList<>();
	protected final List< BiConsumer< List< Component >, AccessoryHandler > > detailedTooltip = new ArrayList<>();

	public AccessoryModifier( Supplier< ? extends AccessoryItem > item, String configKey, String configName, String configComment ) {
		super( configKey, configName, configComment );
		this.item = item;
	}

	public AccessoryModifier addTooltip( String key ) {
		MutableComponent tooltip = new FormattedTranslatable( key ).create().withStyle( IAccessoryConfig.DEFAULT_FORMAT );
		this.tooltip.add( ( components, handler )->components.add( tooltip ) );
		this.detailedTooltip.add( ( components, handler )->components.add( tooltip ) );

		return this;
	}

	public AccessoryModifier addTooltip( IAccessoryConfig config, String key ) {
		this.tooltip.add( ( components, handler )->config.addTooltip( key, components, handler ) );
		this.detailedTooltip.add( ( components, handler )->config.addDetailedTooltip( key, components, handler ) );

		return this;
	}

	public void buildTooltip( List< Component > components, AccessoryHandler handler ) {
		this.tooltip.forEach( consumer->consumer.accept( components, handler ) );
	}

	public void buildDetailedTooltip( List< Component > components, AccessoryHandler handler ) {
		this.detailedTooltip.forEach( consumer->consumer.accept( components, handler ) );
	}

	protected void addToGeneratedLoot( OnLootData data ) {
		data.generatedLoot.add( AccessoryHandler.construct( this.item.get() ) );
	}

	protected < DataType extends ContextData > Consumer< DataType > toAccessoryConsumer( BiConsumer< DataType, AccessoryHandler > consumer, AccessoryPercent... chances ) {
		return data->{
			AccessoryHandler handler = AccessoryHandler.tryToCreate( data.entity, this.item.get() );
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
}
