package com.majruszsaccessories.boosters.components;

import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.OnBoosterCompatibilityGet;
import com.majruszsaccessories.items.BoosterItem;
import com.mlib.contexts.base.Condition;

import java.util.function.Supplier;

public class BoosterIncompatibility extends BonusComponent< BoosterItem > {
	public static ISupplier< BoosterItem > create( Supplier< BoosterItem > booster ) {
		return handler->new BoosterIncompatibility( handler, booster );
	}

	protected BoosterIncompatibility( BonusHandler< BoosterItem > handler, Supplier< BoosterItem > booster ) {
		super( handler );

		OnBoosterCompatibilityGet.listen( OnBoosterCompatibilityGet::makeIncompatible )
			.addCondition( this.matches( booster ) );
	}

	private Condition< OnBoosterCompatibilityGet > matches( Supplier< BoosterItem > booster ) {
		return Condition.predicate( data->{
			return data.a.equals( this.getItem() ) && data.b.equals( booster.get() )
				|| data.a.equals( booster.get() ) && data.b.equals( this.getItem() );
		} );
	}
}
