package com.majruszsaccessories.commands;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.common.AccessoryHolder;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.command.Command;
import com.mlib.command.CommandData;
import com.mlib.command.IParameter;
import com.mlib.contexts.OnGameInitialized;
import com.mlib.item.ItemHelper;
import com.mlib.math.Range;
import com.mlib.registry.Registries;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AutoInstance
public class SpawnAccessoryCommand {
	static final List< ResourceLocation > ID_SUGGESTIONS = new ArrayList<>();
	static final IParameter< List< ? extends Entity > > ENTITIES = Command.entities().named( "entity" );
	static final IParameter< ResourceLocation > ID = Command.resource().named( "id" ).suggests( ()->ID_SUGGESTIONS );
	static final IParameter< Float > BONUS = Command.number().named( "bonus" );
	static final IParameter< Integer > COUNT = Command.integer( Range.of( 1, 10 ) ).named( "count" );

	public SpawnAccessoryCommand() {
		Command.create()
			.literal( "accessory" )
			.hasPermission( 4 )
			.literal( "create" )
			.parameter( ENTITIES )
			.parameter( ID )
			.execute( this::giveAccessory )
			.parameter( COUNT )
			.execute( this::giveAccessory )
			.parameter( BONUS )
			.execute( this::giveAccessory )
			.register();

		OnGameInitialized.listen( this::createSuggestions );
	}

	private int giveAccessory( CommandData data ) throws CommandSyntaxException {
		List< ? extends Entity > entities = data.get( ENTITIES );
		Item item = Registries.getItem( data.get( ID ) );
		Optional< Float > bonus = data.getOptional( BONUS );
		int count = data.getOptional( COUNT ).orElse( 1 );
		for( Entity entity : entities ) {
			if( entity instanceof Player player ) {
				for( int i = 0; i < count; ++i ) {
					AccessoryHolder holder = AccessoryHolder.create( new ItemStack( item ) );
					holder.setBonus( bonus.orElseGet( ()->MajruszsAccessories.CONFIG.efficiency.getRandom() ) );

					ItemHelper.giveToPlayer( holder.getItemStack(), player, player.level() );
				}
			}
		}

		return 0;
	}

	private void createSuggestions( OnGameInitialized data ) {
		for( Item item : Registries.getItems() ) {
			if( item instanceof AccessoryItem ) {
				ID_SUGGESTIONS.add( Registries.get( item ) );
			}
		}
	}
}
