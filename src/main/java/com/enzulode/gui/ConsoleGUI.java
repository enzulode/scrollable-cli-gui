package com.enzulode.gui;

import com.enzulode.gui.exception.GuiInterruptionException;
import com.enzulode.gui.util.KeyMapping;
import com.enzulode.gui.util.Page;
import com.enzulode.gui.util.sctruct.Winsize;
import com.enzulode.gui.util.libs.LibC;
import com.enzulode.gui.util.sctruct.Termios;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Main gui class
 *
 */
public class ConsoleGUI
{
	/**
	 * Termios library instance
	 *
	 */
	private Termios originalAttributes;

	/**
	 * Available terminal rows
	 *
	 */
	private int rows;

	/**
	 * Available terminal columns
	 *
	 */
	private int columns;

	/**
	 * Pages to be opened in a scrolling mode
	 *
	 */
	private List<Page> pages;

	/**
	 * Current page content
	 *
	 */
	private List<String> pageContent;

	/**
	 * Current page offset
	 *
	 */
	private int offsetY = 0;

	/**
	 * Current cursor pointer
	 *
	 */
	private int cursorY = 0;

	/**
	 * Current page pointer
	 *
	 */
	private int pagePointer = 0;

	/**
	 * Console GUI controller
	 *
	 */
	public ConsoleGUI()
	{
	}

	/**
	 * GUI initialisation
	 *
	 */
	private void initGUI()
	{
//		Getting current terminal size
		Winsize winsize = getWindowSize();
		rows = winsize.ws_row - 1;
		columns = winsize.ws_col;
	}

	/**
	 * Paged output
	 *
	 * @param pages pages list to be opened in scrolling mode
	 */
	public void pagedPrint(List<Page> pages)
	{
		enableRawMode();
		initGUI();
		this.pages = pages;

		while (true)
		{
			try
			{
				scroll();
				refreshScreen();
				int key = readKey();
				handleKey(key);
			}
			catch (GuiInterruptionException e)
			{
				break;
			}
		}
	}

	/**
	 * Common print method
	 *
	 * @param printable object to be printed
	 */
	public void print(Object printable)
	{
		Objects.requireNonNull(printable, "Printable object cannot be null");

		System.out.print(printable.toString());
	}

	/**
	 * Common println method
	 *
	 * @param printable object to be printed
	 */
	public void println(Object printable)
	{
		Objects.requireNonNull(printable, "Printable object cannot be null");

		System.out.println(printable.toString());
	}

	/**
	 * Common list printing method
	 *
	 * @param printable list to be printed
	 */
	public void printList(List<?> printable)
	{
		Objects.requireNonNull(printable, "Printable object cannot be null");

		if (printable.size() == 0)
		{
			println("Empty :c");
			return;
		}

		printable.forEach(System.out::println);
	}

	/**
	 * Screen refreshing method
	 *
	 */
	private void refreshScreen()
	{
//		Screen refreshing preparation
		StringBuilder builder = new StringBuilder();

		moveCursorToTopLeft(builder);
		drawContent(builder);
		drawStatusBar(builder);
		drawCursor(builder);

//		Refreshing screen
		System.out.println(builder);
	}

	/**
	 * Method moves cursor to the top left position
	 *
	 * @param builder string builder instance
	 */
	private void moveCursorToTopLeft(StringBuilder builder)
	{
		builder.append("\033[H");
	}

	/**
	 * Method draws content
	 *
	 * @param builder string builder instance
	 */
	private void drawContent(StringBuilder builder)
	{
		pageContent = Arrays.stream(pages.get(pagePointer).data().split("\n")).toList();
		for (int i = 0; i < rows; i++)
		{
			int contentI = offsetY + i;

			if (contentI >= pageContent.size())
				builder.append("~");
			else
				builder.append(pageContent.get(contentI));

			builder.append("\033[K\r\n");
		}
	}

	/**
	 * Method draws a status bar
	 *
	 * @param builder string builder instance
	 */
	private void drawStatusBar(StringBuilder builder)
	{
		String statusMessage = "Lab 6: enzulode  |  Y: " + cursorY + "  |  Page: " + (pagePointer + 1);
		builder.append("\033[7m")
				.append(statusMessage)
				.append(" ".repeat(Math.max(0, columns - statusMessage.length())))
				.append("\033[0m");
	}

	/**
	 * Method draws a cursor
	 *
	 * @param builder string builder instance
	 */
	private void drawCursor(StringBuilder builder)
	{
//		Setting cursor position
		builder.append(String.format("\033[%d;0H", cursorY - offsetY + 1));
	}

	/**
	 * Content scrolling
	 *
	 */
	private void scroll()
	{
		if (cursorY >= rows + offsetY)
		{
			offsetY = cursorY - rows + 1;
		}
		else if (cursorY < offsetY)
		{
			offsetY = cursorY;
		}
	}

	/**
	 * Method handles a key
	 *
	 * @param key handled key
	 * @throws GuiInterruptionException if gui was interrupted
	 */
	private void handleKey(int key) throws GuiInterruptionException
	{
		if (key == 'q')
		{
			System.out.println("\033[2J");
			System.out.println("\033[H");
			disableRawMode();
			throw new GuiInterruptionException("Gui successfully interrupted");
		}
		else if (List.of(KeyMapping.ARROW_UP, KeyMapping.ARROW_DOWN).contains(key))
		{
//			Page scrolling
			moveCursor(key);
		}
		else if (List.of(KeyMapping.ARROW_LEFT, KeyMapping.ARROW_RIGHT).contains(key))
		{
//			TODO: implement page changing
			movePage(key);
		}
	}

	/**
	 * Reading a key from user
	 *
	 * @return a key was pressed
	 */
	private int readKey()
	{
		try
		{
			int key = System.in.read();
			if (key != '\033')
				return key;

			int nextKey = System.in.read();
			if (nextKey != '[' && nextKey != 'O')
				return nextKey;

			int anotherKey = System.in.read();

			if (nextKey == '[')
			{
				return switch (anotherKey) {
					case 'A' -> KeyMapping.ARROW_UP;
					case 'B' -> KeyMapping.ARROW_DOWN;
					case 'C' -> KeyMapping.ARROW_RIGHT;
					case 'D' -> KeyMapping.ARROW_LEFT;

					default -> anotherKey;
				};
			}

			return key;
		}
		catch (IOException e)
		{
			return 113;
		}
	}

	/**
	 * Method moves cursor. Relies on the key
	 *
	 * @param key a specific key was pressed
	 */
	private void moveCursor(int key)
	{
		switch (key) {

			case KeyMapping.ARROW_UP -> {
				if (cursorY > 0)
					cursorY--;
			}

			case KeyMapping.ARROW_DOWN -> {
				if (cursorY < pageContent.size() - 1)
					cursorY++;
			}

		}
	}

	/**
	 * Page moving method
	 *
	 * @param key a specific key was pressed
	 */
	private void movePage(int key)
	{
		switch (key) {
			case KeyMapping.ARROW_LEFT -> {
				if (pagePointer > 0)
				{
					cursorY = 0;
					pagePointer--;
				}
			}

			case KeyMapping.ARROW_RIGHT -> {
				if (pagePointer < pages.size() - 1)
				{
					cursorY = 0;
					pagePointer++;
				}
			}
		}
	}

	/**
	 * Turn the terminal raw mode on via syscall
	 *
	 */
	private void enableRawMode()
	{
//		Loading current terminal flags and saving them to restore after gui shutdown
		Termios termios = new Termios();
		int rc = LibC.INSTANCE.tcgetattr(LibC.SYSTEM_OUT_FD, termios);
		originalAttributes = Termios.of(termios);

		if (rc != 0)
		{
			System.out.println("Failed to call tcgetattr");
			System.exit(1);
		}

//		Setting up RAW-mode flags
		termios.c_lflag &= ~(LibC.ECHO | LibC.ICANON | LibC.IEXTEN | LibC.ISIG);
		termios.c_iflag &= ~(LibC.IXON | LibC.ICRNL);
		termios.c_oflag &= ~(LibC.OPOST);

		termios.c_cc[LibC.VMIN] = 0;
		termios.c_cc[LibC.VTIME] = 1;

//		Loading RAW-mode flags
		LibC.INSTANCE.tcsetattr(LibC.SYSTEM_OUT_FD, LibC.TCSAFLUSH, termios);
	}

	/**
	 * Turn the terminal raw mode on via syscall
	 *
	 */
	private void disableRawMode()
	{
		LibC.INSTANCE.tcsetattr(LibC.SYSTEM_OUT_FD, LibC.TCSAFLUSH, originalAttributes);
	}

	/**
	 * Method retrieves current terminal size via syscall
	 *
	 * @return current terminal size
	 */
	private Winsize getWindowSize()
	{
		final Winsize winsize = new Winsize();
		final int rc = LibC.INSTANCE.ioctl(LibC.SYSTEM_OUT_FD, LibC.TIOCGWINSZ, winsize);

		if (rc != 0)
		{
			System.err.println("ioctl failed with return code " + rc);
			System.exit(1);
		}

		return winsize;
	}
}
