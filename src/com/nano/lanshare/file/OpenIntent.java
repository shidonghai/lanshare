
package com.nano.lanshare.file;

import java.io.File;

import android.content.Intent;
import android.net.Uri;

public class OpenIntent {
    public static Intent get(String type, String mimetype, File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri uri = Uri.fromFile(file);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory("android.intent.category.DEFAULT");
        if (type.equals("web")) {
            uri = Uri.parse(file.toString()).buildUpon()
                    .encodedAuthority("com.android.htmlfileprovider")
                    .scheme("content").encodedPath(file.toString()).build();
        } else if (type.equals("video") || type.equals("audio")) {
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
        }
        intent.setDataAndType(uri,
                mimetype);
        return intent;
    }
}
