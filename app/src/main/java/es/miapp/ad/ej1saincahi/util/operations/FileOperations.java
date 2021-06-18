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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.miapp.ad.ej1saincahi.databinding.FragmentMainBinding;
import es.miapp.ad.ej1saincahi.model.pojo.Call;

public class FileOperations {

    File f;

    public FileOperations(Context context) {
        f = new File(context.getFilesDir(), "AllHistory.csv");
    }

    public static void removeContent(Context context) {
        File fInternal = new File(context.getFilesDir(), "InternalMemory.csv");
        File fExternal = new File(context.getExternalFilesDir(null), "ExternalMemory.csv");

        fInternal.delete();
        fExternal.delete();

        try {
            fInternal.createNewFile();
            fExternal.createNewFile();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private File getMemoryType(Activity activity, Context context) {
        SharedPreferences sharedPreferenceMemoryType = PreferenceManager.getDefaultSharedPreferences(activity);
        String memoryType = sharedPreferenceMemoryType.getString("memorytype", "interna");

        File f;

        if (memoryType.equals("externa")) {
            f = new File(context.getExternalFilesDir(null), "ExternalMemory.csv");
        } else {
            f = new File(context.getFilesDir(), "InternalMemory.csv");
        }

        return f;
    }

    public void saveInternalMemory(Map<Call, Long> contacto2, Context context) {
        File f = new File(context.getFilesDir(), "InternalMemory.csv");

        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));

            contacto2.forEach((call, aLong) -> {
                try {
                    bw.write(call.toCSV().replaceFirst(".$", "").concat(String.valueOf(aLong)));
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        saveExternalMemory(context);
    }

    public void saveExternalMemory(Context context) {
        File f = new File(context.getExternalFilesDir(null), "ExternalMemory.csv");

        List<Call> callImport = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(context.getFilesDir(), "InternalMemory.csv")));

            String line = br.readLine();
            Call c;

            while (line != null) {
                c = Call.CSVToStringDate(line, ";");
                if (c != null) {
                    callImport.add(c);
                }
                line = br.readLine();
            }

            br.close();

            callImport.sort(Call::compareFecha);

            BufferedWriter bw = new BufferedWriter(new FileWriter(f));

            for (Call cal : callImport) {
                bw.write(cal.toCSVv2());
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readContacts(Activity activity, Context context, FragmentMainBinding b) {
        File f = getMemoryType(activity, context);

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));

            b.tvHistory.setText("");

            String line = br.readLine();

            while (line != null) {
                b.tvHistory.append("\n" + line);
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Call> readListContacts(Context context) {
        List<Call> list = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(context.getFilesDir(), "InternalMemory.csv")));

            String line = br.readLine();
            Call c;

            while (line != null) {
                c = Call.CSVToStringDate(line, ";");
                if (c != null) {
                    list.add(c);
                }
                line = br.readLine();
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Call> readFixedListContacts(Context context) {
        List<Call> list = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));

            String line = br.readLine();
            Call c;

            while (line != null) {
                c = Call.CSVToStringDate(line, ";");
                if (c != null) {
                    list.add(c);
                }
                line = br.readLine();
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveFixedInternalMemory(Call contacto, Context context) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));

            bw.write(contacto.toCSV());
            bw.newLine();

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}