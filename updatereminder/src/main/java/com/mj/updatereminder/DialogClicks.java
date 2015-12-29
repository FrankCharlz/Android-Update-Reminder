package com.mj.updatereminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Frank on 12/29/2015.
 */
public class DialogClicks  implements DialogInterface.OnClickListener{

    private final Context context;


    public DialogClicks(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int buttonId) {

        if (buttonId == DialogInterface.BUTTON_POSITIVE)
            startMarketActivity();
        else
            remindHerLater();

        dialogInterface.dismiss();

    }

    private void remindHerLater() {
        //UpdateReminder.decrementLaunches();
    }

    private void startMarketActivity() {
        Uri marketUri = Uri.parse("market://details?id="+UpdateReminder.package_name);
        Intent intent = new Intent(Intent.ACTION_VIEW, marketUri);
        context.startActivity(intent);
    }
}
