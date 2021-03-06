
package com.nano.lanshare.components.operation;

import com.nano.lanshare.R;

public class PopupMenuUtil {

    public static final int MENU_TRANSPORT = 0;
    public static final int MENU_ACTION = 1;
    public static final int MENU_PROPARTY = 2;
    public static final int MENU_OPERATION = 3;
    
    public static final int FILE_SEND=0;
    public static final int FILE_SHARE=1;
    public static final int FILE_OPEN=2;
    public static final int FILE_DELETE=3;
    public static final int FILE_DETAIL=4;
    

    public static final int[] HISTORY_OPERATER = new int[] {
            R.drawable.zapya_data_downmenu_send,
            R.drawable.zapya_data_downmenu_share,
            R.drawable.zapya_data_downmenu_open,
            R.drawable.zapya_data_downmenu_delete,
            R.drawable.zapya_data_downmenu_detail
    };

    public static final int[] HISTORY_OPERATER_NAME = new int[] {
            R.string.menu_transport,
            R.string.menu_share,
            R.string.menu_open,
            R.string.dm_menu_delete,
            R.string.menu_property,
    };

    public static int[] FILE_POPUP_IAMGES = new int[] {
            R.drawable.zapya_data_downmenu_send,
            R.drawable.zapya_data_downmenu_open,
            R.drawable.zapya_data_downmenu_detail,
            R.drawable.zapya_data_downmenu_operating
    };

    public static int[] FILE_POPUP_TEXT = new int[] {
            R.string.menu_transport,
            R.string.menu_open, R.string.menu_property, R.string.menu_operation
    };

    public static int[] AUDIO_POPUP_TEXT = new int[] {
            R.string.menu_transport,
            R.string.menu_play, R.string.menu_property, R.string.menu_operation
    };

    public static int[] IMAGE_POPUP_TEXT = new int[] {
            R.string.menu_transport,
            R.string.menu_view, R.string.menu_property, R.string.menu_operation
    };
}
