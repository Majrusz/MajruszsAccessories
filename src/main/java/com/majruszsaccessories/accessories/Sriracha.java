package com.majruszsaccessories.accessories;

import com.majruszsaccessories.Registries;
import com.majruszsaccessories.accessories.components.AccessoryComponent;
import com.majruszsaccessories.accessories.components.FishingLureBonus;
import com.majruszsaccessories.accessories.components.SaturationBonus;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnItemFished;
import com.mlib.math.Range;
import com.mlib.modhelper.AutoInstance;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Supplier;

@AutoInstance
public class Sriracha extends AccessoryBase {
	public Sriracha() {
		super( Registries.SRIRACHA );

		this.name( "Sriracha" )
			.add( SaturationBonus.create() );
	}
}
