package com.majruszsaccessories.config;

import com.mlib.config.UserConfig;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Supplier;

public abstract class IntegrationConfig< Type > extends UserConfig implements Supplier< Type > {
	final UserConfig config;
	final Supplier< Type > supplier;

	public < ConfigType extends UserConfig & Supplier< Type > > IntegrationConfig( ConfigType config ) {
		super( "", "" );

		this.config = config;
		this.supplier = config;
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		this.config.build( builder );
	}

	@Override
	public Type get() {
		return this.supplier.get();
	}
}
