package com.enzulode.gui.util.libs;

import com.enzulode.gui.util.sctruct.Termios;
import com.enzulode.gui.util.sctruct.Winsize;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Native C library contract interface
 *
 */
public interface LibC extends Library
{
	/**
	 * System flags
	 *
	 */
	int SYSTEM_OUT_FD = 0;
	int TIOCGWINSZ = 0x5413;

	/**
	 * When any of the characters INTR, QUIT, SUSP, or DSUSP are received, generate the corresponding signal
	 *
	 */
	int ISIG = 1;

	/**
	 * Enable canonical mode (described below)
	 *
	 */
	int ICANON = 2;

	/**
	 * Echo input characters
	 *
	 */
	int ECHO = 10;

	/**
	 * The  change  occurs  after  all output written to the object referred by fd has been transmitted,
	 * and all input that has been received but not read will be discarded before the change is made
	 *
	 */
	int TCSAFLUSH = 2;

	/**
	 * Enable XON / XOFF flow control on output
	 *
	 */
	int IXON = 2000;

	/**
	 * Translate carriage return to newline on input (unless IGNCR is set)
	 *
	 */
	int ICRNL = 400;

	/**
	 * Enable implementation-defined input processing. This flag, as well as ICANON must be enabled for the
	 * special characters EOL2, LNEXT, REPRINT, WERASE to be interpreted, and for the IUCLC flag to be effective.
	 *
	 */
	int IEXTEN = 100000;

	/**
	 * Enable implementation-defined output processing
	 *
	 */
	int OPOST = 1;

	/**
	 * Minimum number of characters for non-canonical read (MIN)
	 *
	 */
	int VMIN = 6;

	/**
	 * Timeout in deciseconds for non-canonical read (TIME)
	 *
	 */
	int VTIME = 5;

	/**
	 * Native C library instance
	 *
	 */
	LibC INSTANCE = Native.load("c", LibC.class);

	/**
	 * Method gets terminal attributes
	 *
	 * @param fd flag
	 * @param termios termios struct instance
	 * @return return code
	 */
	int tcgetattr(int fd, Termios termios);

	/**
	 * Method sets terminal attributes
	 *
	 * @param fd flag
	 * @param optional_actions actions
	 * @param termios termios struct instance
	 * @return return code
	 */
	int tcsetattr(int fd, int optional_actions, Termios termios);

	/**
	 * IOCtl unix utility
	 *
	 * @param fd flag
	 * @param opt operation
	 * @param winsize window properties
	 * @return return code
	 */
	int ioctl(int fd, int opt, Winsize winsize);
}
