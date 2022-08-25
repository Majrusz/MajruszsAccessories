package com.majruszsaccessories.gamemodifiers.list;

import com.majruszsaccessories.AccessoryHandler;
import com.majruszsaccessories.gamemodifiers.AccessoryModifier;
import com.majruszsaccessories.gamemodifiers.configs.AccessoryPercent;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.blocks.BlockHelper;
import com.mlib.effects.ParticleHandler;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnLootContext;
import com.mlib.gamemodifiers.data.OnLootData;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.function.Supplier;

public class DoubleCrops extends AccessoryModifier {
	final AccessoryPercent chance = new AccessoryPercent( "double_crops_chance", "Chance to double crops when harvesting.", false, 0.35, 0.0, 1.0 );

	public DoubleCrops( Supplier< ? extends AccessoryItem > item, String configKey ) {
		super( item, configKey, "", "" );

		OnLootContext onLoot = new OnLootContext( this.toAccessoryConsumer( this::doubleLoot, this.chance ) );
		onLoot.addCondition( new Condition.IsServer() )
			.addCondition( OnLootContext.HAS_ORIGIN )
			.addCondition( data->data.blockState != null && BlockHelper.isCropAtMaxAge( data.blockState ) )
			.addCondition( data->data.entity instanceof LivingEntity );

		this.addTooltip( this.chance, "majruszsaccessories.bonuses.double_crops" );
	}

	private void doubleLoot( OnLootData data, AccessoryHandler handler ) {
		ParticleHandler.AWARD.spawn( data.level, data.origin, 6 );
		data.generatedLoot.addAll( new ArrayList<>( data.generatedLoot ) );
	}
}
