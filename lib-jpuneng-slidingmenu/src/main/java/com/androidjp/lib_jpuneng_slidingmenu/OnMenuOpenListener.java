package com.androidjp.lib_jpuneng_slidingmenu;

/**
 * Created by androidjp on 2016/12/24.
 */

public interface OnMenuOpenListener {
    /**
     * 菜单开关时的操作
     * @param isOpen 是否开启了菜单
     * @param flag 左侧 、 右侧
     */
    void onMenuOpen(boolean isOpen, int flag/*左侧（LEFT）或者后侧（RIGHT）*/);
}
