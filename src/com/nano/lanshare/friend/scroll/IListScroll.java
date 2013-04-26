/**
 * 文件名称 : IFriendFastScroll.java
 * <p>
 * 作者信息 : Yang Ke
 * <p>
 * 创建时间 : Oct 11, 2011, 3:06:25 PM
 * <p>
 * 版权声明 : Copyright (c) 2009-2012 CIeNET Ltd. All rights reserved
 * <p>
 * 评审记录 :
 * <p>
 */

package com.nano.lanshare.friend.scroll;

/**
 * 快速滚动的接口
 * <p>
 */
public interface IListScroll
{
    /**
     * 根据索引，跳转到对应的位置
     * 
     * @param aKey
     *            索引
     */
    void scrolltoIndexByKey(String aKey);
    
    /**
     * 滚动到aIndex对应的位置
     * 
     * @param aIndex
     *            位置
     */
    void scrolltoIndex(int aIndex);
}
