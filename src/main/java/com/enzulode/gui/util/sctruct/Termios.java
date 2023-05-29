package com.enzulode.gui.util.sctruct;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import java.util.Arrays;

/**
 * Termios structure
 *
 */
@FieldOrder(value = {"c_iflag", "c_oflag", "c_cflag", "c_lflag", "c_cc"})
public class Termios extends Structure
{
	/**
	 * Input modes
	 *
	 */
	public int c_iflag;

	/**
	 * Output modes
	 *
	 */
	public int c_oflag;

	/**
	 * Control modes
	 *
	 */
	public int c_cflag;

	/**
	 * Local modes
	 *
	 */
	public int c_lflag;

	/**
	 * Special characters
	 *
	 */
	public byte[] c_cc = new byte[19];

	/**
	 * Termios constructor
	 *
	 */
	public Termios()
	{
	}

	/**
	 * Termios static constructor (via copy)
	 *
	 * @param t termios instance
	 * @return termios copy
	 */
	public static Termios of(Termios t)
	{
		Termios copy = new Termios();
		copy.c_iflag = t.c_iflag;
		copy.c_oflag = t.c_oflag;
		copy.c_cflag = t.c_cflag;
		copy.c_lflag = t.c_lflag;
		copy.c_cc = t.c_cc.clone();
		return copy;
	}

	/**
	 * Method generates termios string representation
	 *
	 * @return Termios string representation
	 */
	@Override
	public String toString()
	{
		return "Termios{" +
				"c_iflag=" + c_iflag +
				", c_oflag=" + c_oflag +
				", c_cflag=" + c_cflag +
				", c_lflag=" + c_lflag +
				", c_cc=" + Arrays.toString(c_cc) +
				'}';
	}

}
