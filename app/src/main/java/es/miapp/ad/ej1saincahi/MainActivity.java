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

package es.miapp.ad.ej1saincahi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;

import es.miapp.ad.ej1saincahi.databinding.ActivityMainBinding;
import es.miapp.ad.ej1saincahi.util.permissions.Permissions;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private Permissions permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        init();
    }

    private void init() {

        permissions = new Permissions(this, this);

        launchingActivity();
        File f = new File(getFilesDir(), "AllHistory.csv");
        try {
            boolean sucess = f.createNewFile();
            if (!sucess) {
                throw new IOException("Unable to create file at specified path.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        broadcastReceiver();
    }

    private void launchingActivity() {
        SharedPreferences SPFirstStart = getSharedPreferences("SPFirstStart", MODE_PRIVATE);
        boolean firstStart = SPFirstStart.getBoolean("firstStart", true);

        if (firstStart) {
            firstTimePermissions();
        } else {
            if (permissions.hasAllPerms(permissions.getPERMISSIONS())) {
                permissionsApp();
            }
        }
    }

    private void permissionsApp() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Se requieren permisos")
                .setMessage("Se necesitan una serie de permisos para poder seguir con la aplicación." +
                        "En caso contrario la aplicación procederá a cerrarse.")
                .setPositiveButton("¡Perfecto!", (dialog, which) -> {
                    if (permissions.hasAllPerms(permissions.getPERMISSIONS())) {
                        permissions.pedirPermisos();
                    } else {
                        Snackbar.make(b.getRoot().getRootView(), "Tienes todos los permisos", Snackbar.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }).setNegativeButton("No, no continuar", (dialog, which) -> System.exit(0))
                .create().show();
    }

    private void firstTimePermissions() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Primera vez en abrir la App")
                .setMessage("Parece que es la primera vez que abres la aplicación." +
                        "\n\nNecesita aceptar una serie de permisos para poder continuar, si no la" +
                        " aplicación no podrá hacer las operaciones correctamente.")
                .setPositiveButton("¡Vale!", (dialog, which) -> {

                    if (permissions.hasAllPerms(permissions.getPERMISSIONS())) {
                        permissions.pedirPermisos();
                    } else {
                        Snackbar.make(b.getRoot().getRootView(), "Tienes todos los permisos", Snackbar.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
                }).setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel())
                .create().show();

        SharedPreferences prefs = getSharedPreferences("SPFirstStart", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    /*public void broadcastReceiver() {
        BroadcastIncomingCall broadcastIncomingCall = new BroadcastIncomingCall();
        broadcastIncomingCall.setMainActivityHandler(); // En caso de que queramos pasar la Activity
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
        registerReceiver(broadcastIncomingCall, intentFilter);
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        if (id == R.id.fileSettings) {
            Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigate(R.id.settingsFragment);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}