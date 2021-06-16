package com.majruszsaccessories;

import com.mlib.config.ConfigGroup;
import com.mlib.config.ConfigHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

/** Main modification class. */
@Mod( MajruszsAccessories.MOD_ID )
public class MajruszsAccessories {
	public static final String MOD_ID = "majruszs_accessories";
	public static final String NAME = "Majrusz's Accessories";
	public static final String VERSION = "0.1.1";
	public static final ConfigHandler CONFIG_HANDLER = new ConfigHandler( ModConfig.Type.COMMON, "majruszs-accessories-common.toml" );
	public static final ConfigGroup ACCESSORIES_GROUP = CONFIG_HANDLER.addConfigGroup( new ConfigGroup( "Accessories", "" ) );

	public MajruszsAccessories() {
		RegistryHandler.initialize();
		Integration.initialize();

		MinecraftForge.EVENT_BUS.register( this );
	}

	/** Returns resource location for register in current modification files. */
	public static ResourceLocation getLocation( String register ) {
		return new ResourceLocation( MOD_ID, register );
	}

	/** Adds tooltip to list if advanced tooltips are enabled. */
	public static void addAdvancedTooltip( List< ITextComponent > tooltip, ITooltipFlag flag, String translationKey ) {
		if( flag.isAdvanced() )
			tooltip.add( new TranslationTextComponent( translationKey ).withStyle( TextFormatting.GRAY ) );
	}

	/** Adds multiple tooltips to list if advanced tooltips are enabled. */
	public static void addAdvancedTooltips( List< ITextComponent > tooltip, ITooltipFlag flag, String... translationKeys ) {
		for( String translationKey : translationKeys )
			addAdvancedTooltip( tooltip, flag, translationKey );
	}
}
