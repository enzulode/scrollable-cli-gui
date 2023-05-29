package com.enzulode.gui.util;

import java.util.Objects;

/**
 * GUI page
 *
 * @param data page data to be printed
 */
public record Page(String data)
{
	/**
	 * Page constructor
	 *
	 * @param data the data to be inserted into gui page
	 */
	public Page
	{
		Objects.requireNonNull(data, "Page data cannot be null");
	}

}
