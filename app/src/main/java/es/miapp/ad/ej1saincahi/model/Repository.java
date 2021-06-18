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

package es.miapp.ad.ej1saincahi.model;

import android.app.Activity;
import android.content.Context;

import java.util.List;
import java.util.Map;

import es.miapp.ad.ej1saincahi.databinding.FragmentMainBinding;
import es.miapp.ad.ej1saincahi.model.pojo.Call;
import es.miapp.ad.ej1saincahi.util.Threads;
import es.miapp.ad.ej1saincahi.util.operations.FileOperations;

public class Repository {

    private final FileOperations fileOperations;

    public Repository(Context context) {
        fileOperations = new FileOperations(context);
    }

    ///////////////////////////////////////////////////////////////////////////
    // File Operations
    ///////////////////////////////////////////////////////////////////////////

    public static void removeContent(Context context) {
        Threads.threadExecutorPool.execute(() -> FileOperations.removeContent(context));
    }

    public void writeCallText(Map<Call, Long> call, Context context) {
        Threads.threadExecutorPool.execute(() -> fileOperations.saveInternalMemory(call, context));
    }

    public void saveInternalMemory(Map<Call, Long> contacto, Context context) {
        Threads.threadExecutorPool.execute(() -> fileOperations.saveInternalMemory(contacto, context));
    }

    public void saveExternalMemory(Context context) {
        Threads.threadExecutorPool.execute(() -> fileOperations.saveExternalMemory(context));
    }

    public void readContacts(Activity activity, Context context, FragmentMainBinding b) {
        fileOperations.readContacts(activity, context, b);
    }

    public List<Call> readListContacts(Context context) {
        return fileOperations.readListContacts(context);
    }

    public void saveFixedInternalMemory(Call contacto, Context context) {
        Threads.threadExecutorPool.execute(() -> fileOperations.saveFixedInternalMemory(contacto, context));
    }

    public List<Call> readFixedListContacts(Context context) {
        return fileOperations.readFixedListContacts(context);
    }
}