package com.enzulode.gui.util.sctruct;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

/**
 * Winsize structure
 *
 */
@FieldOrder(value = {"ws_row", "ws_col", "ws_xpixel", "ws_ypixel"})
public class Winsize extends Structure
{
	/**
	 * Rows
	 *
	 */
	public short ws_row;

	/**
	 * Columns
	 *
	 */
	public short ws_col;

	/**
	 * X pixels
	 *
	 */
	public short ws_xpixel;

	/**
	 * Y pixels
	 *
	 */
	public short ws_ypixel;
}
