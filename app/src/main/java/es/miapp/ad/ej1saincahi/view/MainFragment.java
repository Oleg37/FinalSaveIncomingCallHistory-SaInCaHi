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

package es.miapp.ad.ej1saincahi.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import es.miapp.ad.ej1saincahi.databinding.FragmentMainBinding;
import es.miapp.ad.ej1saincahi.util.permissions.Permissions;
import es.miapp.ad.ej1saincahi.viewmodel.ViewModel;

public class MainFragment extends Fragment {

    public static String memoryType;
    private FragmentMainBinding b;
    private ViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentMainBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar;
        actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(false);

        SharedPreferences sharedPreferenceMemoryType = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        memoryType = sharedPreferenceMemoryType.getString("memorytype", "interna");

        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        viewModel.readContacts(getActivity(), getContext(), b);

        init();
    }

    private void init() {
        b.btLeer.setOnClickListener(v -> {
            b.tvHistory.setText("");

            Permissions permissions = new Permissions(requireContext(), requireActivity());

            if (permissions.hasAllPerms(permissions.getPERMISSIONS())) {
                permissions.pedirPermisos();
                return;
            }

            viewModel.readContacts(getActivity(), getContext(), b);
        });
    }
}