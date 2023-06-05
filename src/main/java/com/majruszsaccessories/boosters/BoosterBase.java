package com.majruszsaccessories.boosters;

import com.majruszsaccessories.AccessoryHolder;
import com.majruszsaccessories.boosters.components.BoosterComponent;
import com.majruszsaccessories.common.ComponentBase;
import com.majruszsaccessories.common.ItemBase;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryTooltip;
import com.majruszsaccessories.gamemodifiers.contexts.OnBoosterTooltip;
import com.mlib.gamemodifiers.Condition;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class BoosterBase extends ItemBase< BoosterItem, BoosterComponent, BoosterComponent.ISupplier > {
	public BoosterBase( RegistryObject< BoosterItem > item ) {
		super( item );

		OnAccessoryTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->data.holder.hasBoosterTag( item.get() ) ) )
			.insertTo( this.group );

		OnBoosterTooltip.listen( this::addBoosterTooltip )
			.addCondition( Condition.predicate( data->data.item.equals( item.get() ) ) )
			.insertTo( this.group );
	}

	private void addBoosterTooltip( OnBoosterTooltip.Data data ) {
		this.components.stream()
			.map( ComponentBase::getTooltipProviders )
			.flatMap( List::stream )
			.map( provider->provider.getTooltip( AccessoryHolder.create( ItemStack.EMPTY ) ) )
			.map( component->( Component )component.withStyle( ChatFormatting.GRAY ) )
			.forEach( data.components::add );
	}
}
