/*
 * Copyright (c) 2021. ArseneLupin0.
 *
 * Licensed under the GNU General Public License v3.0
 *
 * https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Permissions of this strong copyleft license are conditioned on making available complete source
 * code of licensed works and modifications, which include larger works using a licensed work,
 * under the same license. Copyright and license notices must be preserved. Contributors provide
 * an express grant of patent rights.
 */

package es.miapp.ad.ej1saincahi.util.operations;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import lombok.Getter;

@Getter
public class GetNameOldApi implements Runnable {

    private final String incomingNumber;
    private final Context context;
    private String name;

    public GetNameOldApi(String name, String incomingNumber, Context context) {
        this.name = name;
        this.incomingNumber = incomingNumber;
        this.context = context;
    }

    @Override
    public void run() {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));

        Cursor cursor = context.getContentResolver().query(uri, new String[]{
                        ContactsContract.Contacts.DISPLAY_NAME},
                null, null, null);

        if (cursor == null) {
            return;
        }

        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();
    }
}