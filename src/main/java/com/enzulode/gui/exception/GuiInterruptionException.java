package com.enzulode.gui.exception;

/**
 * Gui interruption exception
 *
 */
public class GuiInterruptionException extends Exception
{
	/**
	 * GuiInterruptionException constructor without cause
	 *
	 * @param message exception message
	 */
	public GuiInterruptionException(String message)
	{
		super(message, null, false, false);
	}

	/**
	 * GuiInterruptionException constructor
	 *
	 * @param message exception message
	 * @param cause exception cause
	 */
	public GuiInterruptionException(String message, Throwable cause)
	{
		super(message, cause, false, false);
	}
}
