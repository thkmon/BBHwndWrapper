package com.thkmon.hwndwrapper.prototype;

import com.sun.jna.platform.win32.WinDef.HWND;

public class Handle {
	private HWND hwnd = null;
	private String className = "";
	private String windowText = "";
	
	public HWND gethwnd() {
		return hwnd;
	}

	public void sethwnd(HWND hwnd) {
		this.hwnd = hwnd;
	}

	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}

	public String getWindowText() {
		return windowText;
	}

	public void setWindowText(String windowText) {
		this.windowText = windowText;
	}
}