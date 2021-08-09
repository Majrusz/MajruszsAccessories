package com.majruszsaccessories;

import com.mlib.config.ConfigGroup;
import com.mlib.config.ConfigHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

/** Main modification class. */
@Mod( MajruszsAccessories.MOD_ID )
public class MajruszsAccessories {
	public static final String MOD_ID = "majruszs_accessories";
	public static final String NAME = "Majrusz's Accessories";
	public static final String VERSION = "0.3.0";
	public static final ConfigHandler CONFIG_HANDLER = new ConfigHandler( ModConfig.Type.COMMON, "majruszs-accessories-common.toml" );
	public static final ConfigGroup ACCESSORIES_GROUP = CONFIG_HANDLER.addConfigGroup( new ConfigGroup( "Accessories", "" ) );

	public MajruszsAccessories() {
		Integration.initialize();
		RegistryHandler.initialize();

		MinecraftForge.EVENT_BUS.register( this );
	}

	/** Returns resource location for register in current modification files. */
	public static ResourceLocation getLocation( String register ) {
		return new ResourceLocation( MOD_ID, register );
	}

	/** Adds tooltip to list if advanced tooltips are enabled. */
	@OnlyIn( Dist.CLIENT )
	public static void addAdvancedTooltip( List< Component > tooltip, TooltipFlag flag, String translationKey ) {
		if( flag.isAdvanced() )
			tooltip.add( new TranslatableComponent( translationKey ).withStyle( ChatFormatting.GRAY ) );
	}

	/** Adds multiple tooltips to list if advanced tooltips are enabled. */
	@OnlyIn( Dist.CLIENT )
	public static void addAdvancedTooltips( List< Component > tooltip, TooltipFlag flag, String... translationKeys ) {
		for( String translationKey : translationKeys )
			addAdvancedTooltip( tooltip, flag, translationKey );
	}
}
