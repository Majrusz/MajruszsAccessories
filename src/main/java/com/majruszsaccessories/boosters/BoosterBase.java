package com.majruszsaccessories.boosters;

import com.majruszsaccessories.boosters.components.BoosterComponent;
import com.majruszsaccessories.common.ItemBase;
import com.majruszsaccessories.gamemodifiers.contexts.OnAccessoryTooltip;
import com.mlib.gamemodifiers.Condition;
import net.minecraftforge.registries.RegistryObject;

public class BoosterBase extends ItemBase< BoosterItem, BoosterComponent, BoosterComponent.ISupplier > {
	public BoosterBase( RegistryObject< BoosterItem > item ) {
		super( item );

		OnAccessoryTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->data.holder.hasBoosterTag( item.get() ) ) )
			.insertTo( this.group );
	}
}
