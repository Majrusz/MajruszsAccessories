package com.majruszsaccessories.common;

import com.majruszlibrary.client.ClientHelper;
import com.majruszlibrary.data.SerializableClass;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.events.OnItemDecorationsRendered;
import com.majruszlibrary.text.TextHelper;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.events.OnAccessoryTooltip;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.items.BoosterItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class BonusHandler< Type extends Item > {
	protected final List< BonusComponent< Type > > components = new ArrayList<>();
	protected final Supplier< Type > item;
	protected final Class< ? > clazz;
	protected final String id;

	public BonusHandler( Supplier< Type > item, Class< ? > clazz, String id ) {
		this.item = item;
		this.clazz = clazz;
		this.id = id;
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

	public SerializableClass< ? > getConfig() {
		return Serializables.getStatic( this.clazz );
	}

	public String getId() {
		return this.id;
	}

	protected void addTooltip( OnAccessoryTooltip data ) {
		this.components.stream()
			.map( BonusComponent::getTooltipProviders )
			.flatMap( List::stream )
			.map( provider->{
				if( data.holder.hasBonusRangeDefined() && !data.holder.hasBonusDefined() ) {
					return provider.getRangeTooltip( data.holder );
				} else if( ClientHelper.isShiftDown() ) {
					return provider.getDetailedTooltip( data.holder );
				} else {
					return provider.getTooltip( data.holder );
				}
			} )
			.map( component->{
				if( data.holder.isBonusDisabled() && this.item.get() instanceof AccessoryItem ) {
					return TextHelper.literal( component.getString() ).withStyle( ChatFormatting.DARK_GRAY, ChatFormatting.STRIKETHROUGH );
				} else {
					return component.withStyle( ChatFormatting.GRAY );
				}
			} )
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

		AccessoryHolder holder = AccessoryHolder.getOrCreate( itemStack );
		return switch( holder.getBoosters().size() ) {
			case 1 -> new ItemStack( MajruszsAccessories.BOOSTER_OVERLAY_SINGLE.get() );
			case 2 -> new ItemStack( MajruszsAccessories.BOOSTER_OVERLAY_DOUBLE.get() );
			case 3 -> new ItemStack( MajruszsAccessories.BOOSTER_OVERLAY_TRIPLE.get() );
			default -> ItemStack.EMPTY;
		};
	}
}
