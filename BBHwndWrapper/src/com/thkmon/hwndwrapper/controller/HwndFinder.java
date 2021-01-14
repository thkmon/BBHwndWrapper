package com.thkmon.hwndwrapper.controller;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.thkmon.hwndwrapper.prototype.Handle;
import com.thkmon.hwndwrapper.prototype.HandleList;

public class HwndFinder {
	
	private Handle handle = null;
	private HandleList handleList = null;
	
	
	/**
	 * 핸들(Hwnd) 1개 가져오기
	 * 
	 * @param nameOrClass
	 * @return
	 * @throws NullPointerException
	 * @throws Exception
	 */
	public Handle getHandle(String nameOrClass) throws NullPointerException, Exception {
		handle = null;
		
		setHandle(nameOrClass, true);
		if (handle != null) {
			return handle;
		}
		
		setHandle(nameOrClass, false);
		if (handle != null) {
			return handle;
		}
		
		return null;
	}
	
	
	/**
	 * 핸들(Hwnd) n개 가져오기
	 * 
	 * @param hwndClassName
	 * @param hwndText
	 * @return
	 * @throws Exception
	 */
	public HandleList getHandleList(String hwndClassName, String hwndText) throws Exception {
		if (hwndClassName == null || hwndClassName.length() == 0) {
			return null;
		}
		
		if (hwndText == null || hwndText.length() == 0) {
			return null;
		}
		
		handleList = null;
		
		setWindowHandleList(hwndClassName, hwndText);
		if (handleList != null) {
			return handleList;
		}
		
		return null;
	}
	
	
	private void setWindowHandleList(final String hwndClassName, final String hwndText) throws Exception {
		
		try {
			User32.INSTANCE.EnumWindows(new WNDENUMPROC() {
				public boolean callback(HWND hwnd, Pointer arg1) {
					char[] windowText = new char[512];
					User32.INSTANCE.GetWindowText(hwnd, windowText, 512);
					String wText = Native.toString(windowText);
					
					RECT rectangle = new RECT();
					User32.INSTANCE.GetWindowRect(hwnd, rectangle);
					
					// 숨겨져 있는 창은 제외하고 찾는다. 최소화 되어있는 창은 포함한다.
					// rectangle.left값이 -32000일 경우 최소화되어 있는 창이다.
					// if (wText.isEmpty() || !(User32.INSTANCE.IsWindowVisible(hwnd) && rectangle.left > -32000)) {
					if (wText.isEmpty() || !(User32.INSTANCE.IsWindowVisible(hwnd))) {
						return true;
					}

					// 핸들의 클래스 네임 얻기
					char[] c = new char[512];
					User32.INSTANCE.GetClassName(hwnd, c, 512);
					String clsName = String.valueOf(c).trim();

					// int count = 0;
					// System.out.println(
					// 		// "hwnd:"+hwnd+","+
					// 		"번호:" + (++count) + ",텍스트:" + wText + "," + "위치:(" + rectangle.left + "," + rectangle.top
					// 				+ ")~(" + rectangle.right + "," + rectangle.bottom + ")," + "클래스네임:" + clsName);
					
					boolean bFound = false;
					
					// 클래스명이 일치하고 윈도우 텍스트가 포함관계인 핸들 가져오기
					if (clsName != null && clsName.equals(hwndClassName)) {
						if (wText != null && wText.indexOf(hwndText) > -1) {
							bFound = true;
						}
					}
					
					if (!bFound) {
						return true;
					}
					
					if (hwnd != null) {
						if (handleList == null) {
							handleList = new HandleList();
						}
						
						Handle handle = new Handle();
						handle.sethwnd(hwnd);
						handle.setClassName(clsName);
						handle.setWindowText(wText);
						
						handleList.add(handle);
					}

					return true;
				}
			}, null);

		} catch (Exception e) {
			throw e;
		}
	}
	
	
	private void setHandle(final String nameOrClass, final boolean bEquals) throws NullPointerException, Exception {
		
		try {
			User32.INSTANCE.EnumWindows(new WNDENUMPROC() {
				public boolean callback(HWND hwnd, Pointer arg1) {
					
					// 이미 찾았으면 스킵
					if (handle != null) {
						return true;
					}
					
					char[] windowText = new char[512];
					User32.INSTANCE.GetWindowText(hwnd, windowText, 512);
					String wText = Native.toString(windowText);
					
					RECT rectangle = new RECT();
					User32.INSTANCE.GetWindowRect(hwnd, rectangle);
					
					// 숨겨져 있는 창은 찾지 않는다.
					// 단, 최소화 되어있는 창은 찾는다. rectangle.left값이 -32000일 경우 최소화되어 있는 창이다.
					// if (wText.isEmpty() || !(User32.INSTANCE.IsWindowVisible(hWnd) && rectangle.left > -32000)) {
					if (wText.isEmpty() || !(User32.INSTANCE.IsWindowVisible(hwnd))) {
						return true;
					}

					// 핸들의 클래스 네임 얻기
					char[] c = new char[512];
					User32.INSTANCE.GetClassName(hwnd, c, 512);
					String clsName = String.valueOf(c).trim();

					// int count = 0;
					// System.out.println(
					// 		// "hwnd:"+hWnd+","+
					// 		"번호:" + (++count) + ",텍스트:" + wText + "," + "위치:(" + rectangle.left + "," + rectangle.top
					// 				+ ")~(" + rectangle.right + "," + rectangle.bottom + ")," + "클래스네임:" + clsName);
					
					boolean bFound = false;
					
					if (bEquals) {
						if (clsName != null && clsName.equals(nameOrClass)) {
							bFound = true;
						}
						
						if (wText != null && wText.equals(nameOrClass)) {
							bFound = true;
						}
					} else {
						if (clsName != null && clsName.indexOf(nameOrClass) > -1) {
							bFound = true;
						}
						
						if (wText != null && wText.indexOf(nameOrClass) > -1) {
							bFound = true;
						}
					}
					
					if (!bFound) {
						return true;
					}
					
					if (hwnd != null) {
						Handle resultHandle = new Handle();
						resultHandle.sethwnd(hwnd);
						resultHandle.setClassName(clsName);
						resultHandle.setWindowText(wText);
						
						handle = resultHandle;
					}

					return true;
				}
			}, null);

		} catch (Exception e) {
			throw e;
		}
	}
}