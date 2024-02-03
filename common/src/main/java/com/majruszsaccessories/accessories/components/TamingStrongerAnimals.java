package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.entity.AttributeHandler;
import com.majruszlibrary.events.OnAnimalTamed;
import com.majruszlibrary.events.OnBabySpawned;
import com.majruszlibrary.events.base.Condition;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.common.AccessoryHolders;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;

public class TamingStrongerAnimals extends BonusComponent< AccessoryItem > {
	final AttributeHandler health;
	final AttributeHandler damage;
	final AttributeHandler speed;
	final AttributeHandler jumpHeight;
	RangedFloat bonus = new RangedFloat().id( "bonus" ).maxRange( Range.of( 0.0f, 10.0f ) );

	public static ISupplier< AccessoryItem > create( float bonus ) {
		return handler->new TamingStrongerAnimals( handler, bonus );
	}

	protected TamingStrongerAnimals( BonusHandler< AccessoryItem > handler, float bonus ) {
		super( handler );

		this.health = new AttributeHandler( "%s_health_multiplier".formatted( handler.getId() ), ()->Attributes.MAX_HEALTH, AttributeModifier.Operation.MULTIPLY_BASE );
		this.damage = new AttributeHandler( "%s_damage_multiplier".formatted( handler.getId() ), ()->Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE );
		this.speed = new AttributeHandler( "%s_speed_multiplier".formatted( handler.getId() ), ()->Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.MULTIPLY_BASE );
		this.jumpHeight = new AttributeHandler( "%s_jump_height_multiplier".formatted( handler.getId() ), ()->Attributes.JUMP_STRENGTH, AttributeModifier.Operation.MULTIPLY_BASE );
		this.bonus.set( bonus, Range.of( 0.0f, 1.0f ) );

		OnAnimalTamed.listen( this::applyBonuses )
			.addCondition( Condition.isLogicalServer() );

		OnBabySpawned.listen( this::applyBonuses )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( data->this.hasModifier( data.parentA ) || this.hasModifier( data.parentB ) );

		this.addTooltip( "majruszsaccessories.bonuses.animal_attributes", TooltipHelper.asPercent( this.bonus ) );

		handler.getConfig()
			.define( "animal_bonus", this.bonus::define );
	}

	private void applyBonuses( OnAnimalTamed data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.tamer ).get( this::getItem );
		if( !holder.isValid() ) {
			return;
		}

		this.applyBonuses( holder.apply( this.bonus ), data.animal );
		this.spawnEffects( data, holder );
	}

	private void applyBonuses( OnBabySpawned data ) {
		this.applyBonuses( ( float )Math.max( this.getModifierValue( data.parentA ), this.getModifierValue( data.parentB ) ), data.child );
	}

	private void applyBonuses( float bonus, LivingEntity entity ) {
		this.health.setValue( bonus ).apply( entity );
		if( this.damage.hasAttribute( entity ) ) {
			this.damage.setValue( bonus ).apply( entity );
		}
		if( entity instanceof Horse horse ) {
			this.jumpHeight.setValue( bonus ).apply( horse );
			this.speed.setValue( bonus ).apply( horse );
		}
		entity.setHealth( entity.getMaxHealth() );
	}

	private void spawnEffects( OnAnimalTamed data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 4 )
			.sizeBased( data.animal )
			.emit( data.getServerLevel() );
	}

	private double getModifierValue( LivingEntity entity ) {
		return entity.getAttributes().getModifierValue( Attributes.MAX_HEALTH, this.health.getUUID() );
	}

	private boolean hasModifier( LivingEntity entity ) {
		return entity.getAttributes().hasModifier( Attributes.MAX_HEALTH, this.health.getUUID() );
	}
}
