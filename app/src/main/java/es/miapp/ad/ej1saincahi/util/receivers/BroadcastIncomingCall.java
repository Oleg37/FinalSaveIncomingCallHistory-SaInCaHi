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

package es.miapp.ad.ej1saincahi.util.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import es.miapp.ad.ej1saincahi.model.Repository;
import es.miapp.ad.ej1saincahi.model.pojo.Call;
import es.miapp.ad.ej1saincahi.util.operations.FileOperations;
import es.miapp.ad.ej1saincahi.util.operations.GetNameOldApi;

import static android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED;
import static android.telephony.TelephonyManager.EXTRA_STATE;
import static android.telephony.TelephonyManager.EXTRA_STATE_RINGING;

public class BroadcastIncomingCall extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Repository repository = new Repository(context);
        Toast.makeText(context, "Llamada entrante", Toast.LENGTH_SHORT).show();

        if (!intent.getAction().equals(ACTION_PHONE_STATE_CHANGED)) {
            return;
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            if (intent.getStringExtra(EXTRA_STATE).equalsIgnoreCase(EXTRA_STATE_RINGING)) {
                Log.v("XYZ", "primera llamada");
                addNumber(repository, context, intent);
            }
        } else {
            if (intent.getStringExtra(EXTRA_STATE).equalsIgnoreCase(EXTRA_STATE_RINGING)) {

                TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                telephony.listen(new PhoneStateListener() {
                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        super.onCallStateChanged(state, incomingNumber);
                        String number = intent.getExtras().getString("incoming_number");

                        GetNameOldApi name = new GetNameOldApi("Desconocido", number, context);
                        name.run();

                        Call call = new Call(new Date(), number, name.getName(), 0);
//                        repository.writeCallText(call, context);
                    }
                }, PhoneStateListener.LISTEN_CALL_STATE);
            }
        }
    }

    public void addNumber(Repository repository, Context context, Intent intent) {
        String number = intent.getExtras().getString("incoming_number");

        GetNameOldApi name = new GetNameOldApi("Desconocido", intent.getExtras().getString("incoming_number"), context);
        name.run();

        Call call2 = new Call(new Date(), number, name.getName(), 1);
        repository.saveFixedInternalMemory(call2, context);

        List<Call> list = repository.readFixedListContacts(context);

        if (list.isEmpty()) {
            list.add(new Call(new Date(), number, name.getName(), 1));
        }

        list.forEach(call -> Log.v("XYZ", call.toCSV()));

        Log.v("XYZ - SIZE", String.valueOf(list.size()));

        FileOperations.removeContent(context);

        Map<Call, Long> counted = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//        Map<String, Map<String, Long>> counted = list.stream().collect(Collectors.groupingBy(Call::getNumber, Collectors.groupingBy(Call::toCSV, Collectors.counting())));
        repository.writeCallText(counted, context);
    }
}