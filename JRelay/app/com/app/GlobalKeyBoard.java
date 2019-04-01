package com.app;

import java.awt.AWTException;
import java.util.Map.Entry;

import com.move.models.VirtualKeyBoard;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class GlobalKeyBoard {
	public static VirtualKeyBoard sendKeys;
	private static boolean run = true;
	public static boolean goNorth;
	public static boolean goSouth;
	public static boolean goWest;
	public static boolean goEast;

	public static void hookKeys() {
		// Might throw a UnsatisfiedLinkError if the native library fails to load or a
		// RuntimeException if hooking fails
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(false); // Use false here to switch to hook instead of
		try {
			sendKeys = new VirtualKeyBoard();
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}														// raw input

		System.out.println(
				"Global keyboard hook successfully started, press [escape] key to shutdown. Connected keyboards:");

		for (Entry<Long, String> keyboard : GlobalKeyboardHook.listKeyboards().entrySet()) {
			System.out.format("%d: %s\n", keyboard.getKey(), keyboard.getValue());
		}

		keyboardHook.addKeyListener(new GlobalKeyAdapter() {

			@Override
			public void keyPressed(GlobalKeyEvent event) {
				System.out.print(event.getKeyChar());
				switch (event.getKeyChar()) {
				case 'w':
					goNorth = true;
					break;
				case 's':
					goSouth = true;
					break;
				case 'a':
					goWest = true;
					break;
				case 'd':
					goEast = true;
					break;

				}
				if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE) {
					run = false;
				}

			}

			@Override
			public void keyReleased(GlobalKeyEvent event) {
				switch (event.getKeyChar()) {
				case 'w':
					goNorth = false;
					break;
				case 's':
					goSouth = false;
					break;
				case 'a':
					goWest = false;
					break;
				case 'd':
					goEast = false;
					break;
				}
			}
		});

		try {
			while (run) {
				Thread.sleep(128);
			}
		} catch (InterruptedException e) {
			// Do nothing
		} finally {
			keyboardHook.shutdownHook();
		}
	}
	public static void main(String[] args) {
		GlobalKeyBoard.hookKeys();
	}
}
