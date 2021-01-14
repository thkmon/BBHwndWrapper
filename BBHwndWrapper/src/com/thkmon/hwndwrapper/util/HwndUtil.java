package com.thkmon.hwndwrapper.util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public class HwndUtil {

	
	/**
	 * GetForegroundWindow 메서드, keybd_event 메서드 구현을 위한 인터페이스 정의
	 * 
	 * @author bbmon
	 *
	 */
	public interface CustomUser32 extends StdCallLibrary {
		CustomUser32 INSTANCE = (CustomUser32) Native.loadLibrary("user32", CustomUser32.class);
		HWND GetForegroundWindow();
		void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);
	}
	
	
	/**
	 * 핸들의 텍스트 가져오기
	 * 
	 * @param hwnd
	 * @return
	 */
	public static String getHandleText(HWND hwnd) {
		char[] windowText = new char[512];
		User32.INSTANCE.GetWindowText(hwnd, windowText, 512);
		String wText = Native.toString(windowText);
		return wText;
	}

	
	/**
	 * 핸들의 클래스명 가져오기
	 * @param hwnd
	 * @return
	 */
	public static String getHandleClassName(HWND hwnd) {
		char[] c = new char[512];
		User32.INSTANCE.GetClassName(hwnd, c, 512);
		String clsName = String.valueOf(c).trim();
		return clsName;
	}
	
	
	/**
	 * 핸들의 pid 가져오기
	 * @param hwnd
	 * @return
	 */
	public static int getHandlePid(HWND hwnd) {
		IntByReference pidByRef = new IntByReference(0);
		User32.INSTANCE.GetWindowThreadProcessId(hwnd, pidByRef);
		int pid = pidByRef.getValue();
		return pid;
	}

	
	/**
	 * 핸들 포커스
	 * 
	 * @param hwnd
	 */
	public static boolean setFocusHandle(HWND hwnd) {
		// 최소화 되어있을 경우 복원
		if (isMinimizedHandle(hwnd)) {
			User32.INSTANCE.ShowWindow(hwnd, 9);
		}
		
		User32.INSTANCE.SetForegroundWindow(hwnd);
		
		try {
			Thread.sleep(100);
		} catch (Exception e) {}
		
		// 포커스 되었는지 확인해야 한다.
		HWND foregroundHwnd = CustomUser32.INSTANCE.GetForegroundWindow();
		boolean bFocused = checkHandlesAreSame(hwnd, foregroundHwnd);
		return bFocused;
	}
	
	
	/**
	 * 최소화되어 있는 창인지 검사한다. rectangle.left값이 -32000일 경우 최소화되어 있는 창이다.
	 * 
	 * @param hwnd
	 */
	public static boolean isMinimizedHandle(HWND hwnd) {
		if (hwnd == null) {
			return false;
		}
		
		RECT rectangle = new RECT();
		User32.INSTANCE.GetWindowRect(hwnd, rectangle);
		if (rectangle.left <= -32000) {
			return true;
		}
		
		return false;
	}

	
	/**
	 * 창을 강제로 닫는다.
	 * 
	 * @param hwnd
	 */
	public static void closeHwnd(HWND hwnd) {
		User32.INSTANCE.PostMessage(hwnd, WinUser.WM_CLOSE, null, null);
	}
	
	
	/**
	 * 두 개의 핸들 객체가 일치하는지 확인한다.
	 *  
	 * @param hwnd1
	 * @param hwnd2
	 * @return
	 */
	public static boolean checkHandlesAreSame(HWND hwnd1, HWND hwnd2) {
		if (hwnd1 == null) {
			return false;
		}
		
		if (hwnd2 == null) {
			return false;
		}
		
		// pid가 같아도 핸들은 다를 수 있다. 예를 들어 엑셀 시트 창과 엑셀의 오류 메시지 창은 같은 pid를 갖지만 핸들은 다르다.
		int pid1 = HwndUtil.getHandlePid(hwnd1);
		int pid2 = HwndUtil.getHandlePid(hwnd2);
		if (pid1 != pid2) {
			return false;
		}
		
		String className1 = HwndUtil.getHandleClassName(hwnd1);
		String className2 = HwndUtil.getHandleClassName(hwnd2);
		if (className1 == null || className2 == null || !className1.equals(className2)) {
			return false;
		}
		
		String text1 = HwndUtil.getHandleText(hwnd1);
		String text2 = HwndUtil.getHandleText(hwnd2);
		if (text1 == null || text2 == null || !text1.equals(text2)) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * 특정 키를 누른다.
	 * 
	 * @param customUser32
	 * @param hwnd
	 * @param vkCode
	 * @throws Exception
	 */
	public static void inputKey(HWND hwnd, int vkCode) throws Exception {
		CustomUser32.INSTANCE.keybd_event((byte) vkCode /* KeyEvent.VK_ESCAPE */, (byte) 0, 0, 0);
		CustomUser32.INSTANCE.keybd_event((byte) vkCode /* KeyEvent.VK_ESCAPE */, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);
	}
}