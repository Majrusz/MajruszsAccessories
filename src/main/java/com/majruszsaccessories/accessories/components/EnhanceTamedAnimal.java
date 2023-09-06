package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.effects.ParticleHandler;
import com.mlib.contexts.OnAnimalTame;
import com.mlib.math.Range;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;

import java.util.function.Supplier;

public class EnhanceTamedAnimal extends AccessoryComponent {
	static final AttributeHandler HEALTH = new AttributeHandler( "ec10e191-9ab4-40e5-a757-91e57104d3ab", "CoTHealthMultiplier", Attributes.MAX_HEALTH, AttributeModifier.Operation.MULTIPLY_BASE );
	static final AttributeHandler DAMAGE = new AttributeHandler( "f1d3671c-9474-4ffd-a440-902d69bd3bd9", "CoTDamageMultiplier", Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE );
	static final AttributeHandler SPEED = new AttributeHandler( "ed1c5feb-1017-4dc2-8a8b-7a64388f0dea", "CoTSpeedMultiplier", Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE );
	static final AttributeHandler JUMP_HEIGHT = new AttributeHandler( "8c065a4c-98de-4ce4-adda-41ae7a20abfb", "CoTJumpHeightMultiplier", Attributes.JUMP_STRENGTH, AttributeModifier.Operation.MULTIPLY_BASE );
	final DoubleConfig bonus;

	public static AccessoryComponent.ISupplier create( double bonus ) {
		return ( item, group )->new EnhanceTamedAnimal( item, group, bonus );
	}

	public static AccessoryComponent.ISupplier create() {
		return create( 0.2 );
	}

	protected EnhanceTamedAnimal( Supplier< AccessoryItem > item, ConfigGroup group, double bonus ) {
		super( item );

		this.bonus = new DoubleConfig( bonus, new Range<>( 0.0, 10.0 ) );

		OnAnimalTame.listen( this::enhanceAnimal )
			.addCondition( CustomConditions.hasAccessory( item, data->data.tamer ) )
			.addConfig( this.bonus.name( "animal_bonus" ).comment( "Bonus health, damage, movement speed and jump height for tamed animals." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.animal_health", TooltipHelper.asPercent( this.bonus ) )
			.addTooltip( "majruszsaccessories.bonuses.animal_damage", TooltipHelper.asPercent( this.bonus ) )
			.addTooltip( "majruszsaccessories.bonuses.animal_speed", TooltipHelper.asPercent( this.bonus ) )
			.addTooltip( "majruszsaccessories.bonuses.animal_jump_height", TooltipHelper.asPercent( this.bonus ) );
	}

	private void enhanceAnimal( OnAnimalTame.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.tamer, this.item.get() );
		float bonus = holder.apply( this.bonus );
		HEALTH.setValue( bonus ).apply( data.animal );
		if( AttributeHandler.hasAttribute( data.animal, Attributes.ATTACK_DAMAGE ) ) {
			DAMAGE.setValue( bonus ).apply( data.animal );
		}
		if( data.animal instanceof Horse horse ) {
			JUMP_HEIGHT.setValue( bonus ).apply( horse );
			SPEED.setValue( bonus ).apply( horse );
		}
		data.animal.setHealth( data.animal.getMaxHealth() );
		if( data.getLevel() instanceof ServerLevel level ) {
			ParticleHandler.AWARD.spawn( level, data.animal.position().add( 0.0, 0.5, 0.0 ), 8, ParticleHandler.offset( 2.0f ) );
		}
	}
}
