package com.majruszsaccessories.common;

import com.majruszlibrary.client.ClientHelper;
import com.majruszlibrary.data.SerializableClass;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.events.OnItemDecorationsRendered;
import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.events.OnAccessoryTooltip;
import com.majruszsaccessories.items.BoosterItem;
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
			float blitOffset = data.gui.blitOffset;
			data.gui.blitOffset = 200.0f;
			data.gui.renderGuiItem( overlay, data.x, data.y );
			data.gui.blitOffset = blitOffset;
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
