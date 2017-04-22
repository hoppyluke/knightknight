package com.supergreenowl.tunnel.ui;

/**
 * Super type for types that have horizontal and vertical alignment settings. Manages a default alignment setting.
 * @author luke
 *
 */
public class Aligned {

	protected VerticalAlign defaultVerticalAlign;
	protected HorizontalAlign defaultHorizontalAlign;

	public Aligned() {
		defaultHorizontalAlign = HorizontalAlign.Left;
		defaultVerticalAlign = VerticalAlign.Baseline;
	}

	/**
	 * Sets the default alignment.
	 * @param hAlign Default horizontal alignment.
	 * @param vAlign Default vertical alignment.
	 */
	public void setDefaultAlignment(HorizontalAlign hAlign, VerticalAlign vAlign) {
		this.defaultHorizontalAlign = hAlign;
		this.defaultVerticalAlign = vAlign;
	}

}