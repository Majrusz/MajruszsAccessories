package com.majruszsaccessories.common;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.contexts.OnAccessoryTooltip;
import com.majruszsaccessories.items.BoosterItem;
import com.mlib.client.ClientHelper;
import com.mlib.contexts.OnItemDecorationsRendered;
import com.mlib.data.Serializable;
import com.mlib.data.Serializables;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class BonusHandler< Type extends Item > {
	protected final List< BonusComponent< Type > > components = new ArrayList<>();
	protected final Supplier< Type > item;
	protected final Serializable< ? > config;
	protected final String id;

	public BonusHandler( Supplier< Type > item, Class< ? > config, String id ) {
		this.item = item;
		this.config = new Serializable<>();
		this.id = id;

		Serializables.get( config )
			.defineCustom( id, ()->this.config );
	}

	public BonusHandler< Type > add( BonusComponent.ISupplier< Type > supplier ) {
		this.components.add( supplier.apply( this ) );

		return this;
	}

	public List< ? extends BonusComponent< Type > > getComponents() {
		return Collections.unmodifiableList( this.components );
	}

	public Type getItem() {
		return this.item.get();
	}

	public Serializable< ? > getConfig() {
		return this.config;
	}

	public String getId() {
		return this.id;
	}

	protected void addTooltip( OnAccessoryTooltip data ) {
		this.components.stream()
			.map( BonusComponent::getTooltipProviders )
			.flatMap( List::stream )
			.map( provider->{
				if( data.holder.hasBonusRangeDefined() ) {
					return provider.getRangeTooltip( data.holder );
				} else if( ClientHelper.isShiftDown() ) {
					return provider.getDetailedTooltip( data.holder );
				} else {
					return provider.getTooltip( data.holder );
				}
			} )
			.map( component->component.withStyle( ChatFormatting.GRAY ) )
			.forEach( data.components::add );
	}

	protected void addBoosterIcon( OnItemDecorationsRendered data ) {
		ItemStack overlay = BonusHandler.getOverlay( data.itemStack );
		if( !overlay.isEmpty() ) {
			data.gui.renderItem( overlay, data.x, data.y );
		}
	}

	private static ItemStack getOverlay( ItemStack itemStack ) {
		if( itemStack.getItem() instanceof BoosterItem ) {
			return new ItemStack( MajruszsAccessories.BOOSTER_OVERLAY_SINGLE.get() );
		}

		AccessoryHolder holder = AccessoryHolder.create( itemStack );
		return switch( holder.getBoosters().size() ) {
			case 1 -> new ItemStack( MajruszsAccessories.BOOSTER_OVERLAY_SINGLE.get() );
			case 2 -> new ItemStack( MajruszsAccessories.BOOSTER_OVERLAY_DOUBLE.get() );
			case 3 -> new ItemStack( MajruszsAccessories.BOOSTER_OVERLAY_TRIPLE.get() );
			default -> ItemStack.EMPTY;
		};
	}
}
