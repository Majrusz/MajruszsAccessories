package com.majruszsaccessories.common;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryTooltip;
import com.mlib.client.ClientHelper;
import com.mlib.config.ConfigGroup;
import com.mlib.gamemodifiers.ModConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
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

import static com.majruszsaccessories.MajruszsAccessories.SERVER_CONFIG;

public class ItemBase< ItemType extends Item, ComponentType extends ComponentBase< ItemType >, SupplierType extends BiFunction< Supplier< ItemType >, ConfigGroup, ComponentType > > {
	protected final List< ComponentType > components = new ArrayList<>();
	protected final Supplier< ItemType > item;
	protected final ConfigGroup group;

	public ItemBase( RegistryObject< ItemType > item ) {
		this.item = item;
		this.group = ModConfigs.init( SERVER_CONFIG, item.getId().toString() );
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
			.map( provider->ClientHelper.isShiftDown() ? provider.getDetailedTooltip( data.holder ) : provider.getTooltip( data.holder ) )
			.map( component->( Component )component.withStyle( ChatFormatting.GRAY ) )
			.forEach( data.components::add );
	}

	protected void renderBoosterIcon( int xOffset, int yOffset, float blitOffset ) {
		DistExecutor.unsafeCallWhenOn( Dist.CLIENT, ()->()->{
			ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
			renderer.blitOffset += blitOffset;
			renderer.renderGuiItem( new ItemStack( Registries.BOOSTER_OVERLAY.get() ), xOffset, yOffset );
			renderer.blitOffset -= blitOffset;

			return true;
		} );
	}
}
