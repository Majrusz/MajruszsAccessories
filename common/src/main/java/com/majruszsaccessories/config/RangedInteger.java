package com.majruszsaccessories.config;

import com.mlib.data.Serializable;
import com.mlib.math.Range;

public class RangedInteger {
	private Integer value;
	private Range< Integer > range;
	private String id = "value";
	private Range< Integer > maxRange = null;

	public RangedInteger id( String id ) {
		this.id = id;

		return this;
	}

	public RangedInteger maxRange( Range< Integer > range ) {
		this.maxRange = range;

		return this;
	}

	public void set( int value, Range< Integer > range ) {
		this.value = value;
		this.range = range;
	}

	public void define( Serializable serializable ) {
		serializable.defineInteger( this.id, ()->this.value, x->this.value = this.range.clamp( x ) );
		if( this.maxRange != null ) {
			serializable.defineIntegerRange( "%s_range".formatted( this.id ), ()->this.range, x->this.range = this.maxRange.clamp( x ) );
		}
	}

	public int get() {
		return this.value;
	}

	public Range< Integer > getRange() {
		return this.range;
	}
}
