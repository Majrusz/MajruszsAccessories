package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.accessories.AccessoryItem;
import com.majruszsaccessories.gamemodifiers.CustomConditions;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.ConfigGroup;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.OnAnimalTame;
import com.mlib.effects.ParticleHandler;
import com.mlib.math.Range;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;

import java.util.function.Supplier;

public class EnhanceTamedAnimal extends AccessoryComponent {
	final AttributeHandler health;
	final AttributeHandler damage;
	final AttributeHandler speed;
	final AttributeHandler jumpHeight;
	final DoubleConfig bonus;

	public static AccessoryComponent.ISupplier create( double bonus ) {
		return ( item, group )->new EnhanceTamedAnimal( item, group, bonus );
	}

	public static AccessoryComponent.ISupplier create() {
		return create( 0.2 );
	}

	protected EnhanceTamedAnimal( Supplier< AccessoryItem > item, ConfigGroup group, double bonus ) {
		super( item );

		this.health = new AttributeHandler( "%sHealthMultiplier".formatted( group.getName() ), Attributes.MAX_HEALTH, AttributeModifier.Operation.MULTIPLY_BASE );
		this.damage = new AttributeHandler( "%sDamageMultiplier".formatted( group.getName() ), Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE );
		this.speed = new AttributeHandler( "%sSpeedMultiplier".formatted( group.getName() ), Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE );
		this.jumpHeight = new AttributeHandler( "%sJumpHeightMultiplier".formatted( group.getName() ), Attributes.JUMP_STRENGTH, AttributeModifier.Operation.MULTIPLY_BASE );
		this.bonus = new DoubleConfig( bonus, new Range<>( 0.0, 10.0 ) );

		OnAnimalTame.listen( this::enhanceAnimal )
			.addCondition( CustomConditions.hasAccessory( item, data->data.tamer ) )
			.addConfig( this.bonus.name( "animal_bonus" ).comment( "Bonus health, damage, movement speed, and jump height for tamed animals." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.bonuses.animal_attributes", TooltipHelper.asPercent( this.bonus ) );
	}

	private void enhanceAnimal( OnAnimalTame.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.tamer, this.item.get() );
		float bonus = holder.apply( this.bonus );
		this.health.setValue( bonus ).apply( data.animal );
		if( AttributeHandler.hasAttribute( data.animal, Attributes.ATTACK_DAMAGE ) ) {
			this.damage.setValue( bonus ).apply( data.animal );
		}
		if( data.animal instanceof Horse horse ) {
			this.jumpHeight.setValue( bonus ).apply( horse );
			this.speed.setValue( bonus ).apply( horse );
		}
		data.animal.setHealth( data.animal.getMaxHealth() );
		if( data.getLevel() instanceof ServerLevel level ) {
			ParticleHandler.AWARD.spawn( level, data.animal.position().add( 0.0, 0.5, 0.0 ), 8, ParticleHandler.offset( 2.0f ) );
		}
	}
}
