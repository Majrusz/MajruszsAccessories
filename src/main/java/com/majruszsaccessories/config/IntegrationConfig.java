package com.majruszsaccessories.config;

import com.mlib.config.IConfigurable;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Supplier;

public abstract class IntegrationConfig< Type > implements IConfigurable, Supplier< Type > {
	final IConfigurable config;
	final Supplier< Type > supplier;

	public < ConfigType extends IConfigurable & Supplier< Type > > IntegrationConfig( ConfigType config ) {
		this.config = config;
		this.supplier = config;
	}

	@Override
	public String getName() {
		return this.config.getName();
	}

	@Override
	public String getComment() {
		return this.config.getComment();
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
