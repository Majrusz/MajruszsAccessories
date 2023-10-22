package com.majruszsaccessories.config;

import com.mlib.data.Serializable;
import com.mlib.math.Range;

public class RangedFloat {
	private float value;
	private Range< Float > range;
	private String id = "value";
	private Range< Float > maxRange = null;

	public RangedFloat id( String id ) {
		this.id = id;

		return this;
	}

	public RangedFloat maxRange( Range< Float > range ) {
		this.maxRange = range;

		return this;
	}

	public void set( float value, Range< Float > range ) {
		this.value = value;
		this.range = range;
	}

	public void define( Serializable serializable ) {
		serializable.defineFloat( this.id, ()->this.value, x->this.value = this.range.clamp( x ) );
		if( this.maxRange != null ) {
			serializable.defineFloatRange( "%s_range".formatted( this.id ), ()->this.range, x->this.range = this.maxRange.clamp( x ) );
		}
	}

	public float get() {
		return this.value;
	}

	public Range< Float > getRange() {
		return this.range;
	}
}
