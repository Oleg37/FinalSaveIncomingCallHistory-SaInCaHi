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

package es.miapp.ad.ej1saincahi.util.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permissions {
    private final int PERMISSION_ALL = 1;
    /**
     * Pedimos todos nuestros permisos.
     */
    private final String[] PERMISSIONS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private Context context;
    private Activity activity;

    public Permissions(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * Método para ver los permisos en forma de bucle (Método, más corto existente), noi hace falta
     * uso de onRequestPermissionResult. Solo se pedirán los permisos que están en el archivo de
     * manifiesto y son completamente requeridos por nuestra aplicación.
     *
     * @param permissions Permisos pasados por un array propio de permisos.
     *
     * @return Los permisos garantizados.
     */
    public boolean hasAllPerms(String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Método muy parecido a {@link #hasAllPerms(String...)} solo que en este contamos los
     * permisos que son denegados por el usuario o no los tiene activados aún.
     *
     * @param permissions Permisos pasados por un array propio de permisos.
     *
     * @return Número de permisos denegados.
     */
    public int permissionCount(String... permissions) {
        int permissionCount = 0;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                permissionCount++;
            }
        }
        return permissionCount;
    }

    /**
     * Método para explicar los permisos necesarios para ejecutar la aplicación 1 a 1.
     *
     * @param permissions Permisos pasados por un array propio de permisos.
     */
    private void explainPermission(String... permissions) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);

        builder.setTitle("Se necesitan " + permissionCount(permissions) + " permisos...");

        StringBuilder perms = new StringBuilder();
        for (String permission : permissions) {
            switch (permission) {
                case Manifest.permission.READ_CONTACTS:
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
                        perms.append("- Permiso para leer los contactos\n");
                    }
                    break;
                case Manifest.permission.READ_PHONE_STATE:
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                        perms.append("- Permiso para leer el estado del teléfono\n");
                    }
                    break;
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        perms.append("- Permiso para escribir en memoria externa\n");
                    }
                    break;
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        perms.append("- Permiso para leer en memoria externa\n");
                    }
            }
        }
        builder.setMessage("Se necesitan los siguiente permisos para un funcionamiento correcto de la App:\n\n" + perms);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
                ActivityCompat.requestPermissions(activity, permissions, PERMISSION_ALL));
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    /**
     * Método público general para pedir los permisos.
     */
    public void pedirPermisos() {
        if (hasAllPerms(PERMISSIONS)) {
            explainPermission(PERMISSIONS);
        }
    }
}