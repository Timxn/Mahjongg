package de.timon.mahjongg.ui.table;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.timon.mahjongg.databinding.FragmentTableBinding;

public class TableFragment extends Fragment {

    private FragmentTableBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TableViewModel tableViewModel =
                new ViewModelProvider(this).get(TableViewModel.class);

        binding = FragmentTableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}