package com.majruszsaccessories.common;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryTooltip;
import com.mlib.client.ClientHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.contexts.base.ModConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ItemBase< ItemType extends Item, ComponentType extends ComponentBase< ItemType >, SupplierType extends BiFunction< Supplier< ItemType >, ConfigGroup, ComponentType > > {
	protected final List< ComponentType > components = new ArrayList<>();
	protected final Supplier< ItemType > item;
	protected final ConfigGroup group;

	public ItemBase( RegistryObject< ItemType > item, String groupId ) {
		this.item = item;
		this.group = ModConfigs.init( groupId, item.getId().toString() );
	}

	public ItemBase< ItemType, ComponentType, SupplierType > name( String name ) {
		this.group.name( name );

		return this;
	}

	public ItemBase< ItemType, ComponentType, SupplierType > add( SupplierType supplier ) {
		this.components.add( supplier.apply( this.item, this.group ) );

		return this;
	}

	public List< ComponentType > getComponents() {
		return Collections.unmodifiableList( this.components );
	}

	public Supplier< ItemType > getItem() {
		return this.item;
	}

	protected void addTooltip( OnAccessoryTooltip.Data data ) {
		this.components.stream()
			.map( ComponentBase::getTooltipProviders )
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
			.map( component->( Component )component.withStyle( ChatFormatting.GRAY ) )
			.forEach( data.components::add );
	}

	protected boolean renderBoosterIcon( int xOffset, int yOffset, GuiGraphics guiGraphics ) {
		return DistExecutor.unsafeCallWhenOn( Dist.CLIENT, ()->()->{
			guiGraphics.renderItem( new ItemStack( Registries.BOOSTER_OVERLAY.get() ), xOffset, yOffset );

			return true;
		} );
	}
}
