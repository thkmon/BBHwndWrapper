package com.thkmon.hwndwrapper.main;

import java.awt.event.KeyEvent;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.thkmon.hwndwrapper.controller.HwndFinder;
import com.thkmon.hwndwrapper.prototype.Handle;
import com.thkmon.hwndwrapper.util.HwndUtil;

public class MainClass {

	public static void main(String[] args) {
		
		System.out.println("시작");
		
		// 현재 실행 중인 메모장 윈도우 핸들을 열어서 
		try {
			String hwndText = "메모장";
			
			HwndFinder hwndFinder = new HwndFinder();
			Handle handle = hwndFinder.getHandle(hwndText);
			HWND hwnd = null;
			if (handle != null) {
				hwnd = handle.gethwnd();
			}
			
			if (hwnd != null && HwndUtil.setFocusHandle(hwnd)) {
				HwndUtil.inputKey(hwnd, KeyEvent.VK_H); sleep(100);
				HwndUtil.inputKey(hwnd, KeyEvent.VK_E); sleep(100);
				HwndUtil.inputKey(hwnd, KeyEvent.VK_L); sleep(100);
				HwndUtil.inputKey(hwnd, KeyEvent.VK_L); sleep(100);
				HwndUtil.inputKey(hwnd, KeyEvent.VK_O); sleep(100);
				HwndUtil.inputKey(hwnd, KeyEvent.VK_SPACE); sleep(100);
				HwndUtil.inputKey(hwnd, KeyEvent.VK_W); sleep(100);
				HwndUtil.inputKey(hwnd, KeyEvent.VK_O); sleep(100);
				HwndUtil.inputKey(hwnd, KeyEvent.VK_R); sleep(100);
				HwndUtil.inputKey(hwnd, KeyEvent.VK_L); sleep(100);
				HwndUtil.inputKey(hwnd, KeyEvent.VK_D); sleep(100);
				
				sleep(100);
				
			} else {
				System.out.println("윈도우 핸들(" + hwndText + ")을 찾을 수 없습니다.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("끝");
	}
	
	
	private static void sleep(int i) {
		try {
			if (i > 0) {
				Thread.sleep(i);
			}
		} catch (InterruptedException e) {
		} catch (Exception e) {}
	}
}
