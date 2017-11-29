package project.if26.if26_notepad;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

/**
 * Classe representant l'application
 */
public class NotePad extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public CharSequence getContenuPressePapier() {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = manager.getPrimaryClip();

        String textToPaste = clip.getItemAt(0).coerceToText(this).toString();

        if (TextUtils.isEmpty(textToPaste)) return null;
        return textToPaste;
    }

    public void setContenuPressePapier(CharSequence string) {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text label", string);
        manager.setPrimaryClip(clipData);
    }

}
