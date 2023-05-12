package de.timon.mahjongg.ui.score;

import static de.timon.mahjongg.ApplicationGlobals.gameInProgress;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.timon.mahjongg.databinding.FragmentScoreBinding;

public class ScoreFragment extends Fragment {

    private FragmentScoreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScoreViewModel scoreViewModel =
                new ViewModelProvider(this).get(ScoreViewModel.class);

        binding = FragmentScoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (!gameInProgress) {
            binding.newGameLayout.getRoot().setVisibility(View.VISIBLE);
            binding.newGameLayout.createGame.setOnClickListener(v -> {
                binding.newGameLayout.getRoot().setVisibility(View.GONE);
                binding.inputNamesLayout.getRoot().setVisibility(View.VISIBLE);
                binding.inputNamesLayout.startGame.setEnabled(false);
                binding.inputNamesLayout.name3.setEnabled(false);
                binding.inputNamesLayout.east3.setEnabled(false);

                // Check if any of the EditText inputs are empty or no RadioButton is selected
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Do nothing
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        boolean isEmptyName0 = binding.inputNamesLayout.name0.getText().toString().equals("");
                        boolean isEmptyName1 = binding.inputNamesLayout.name1.getText().toString().equals("");
                        boolean isEmptyName2 = binding.inputNamesLayout.name2.getText().toString().equals("");
                        boolean isEmptyName3 = binding.inputNamesLayout.name3.getText().toString().equals("");

                        /*
                         * - only allow four names to be input if first three are written and if on of first 3 is empty delete the fourth name again
                         * - only allow fourth name to be east if the name is set, if name removed remove the check from east
                         * - also only allow east 3 button to be clickable if name 3 is set
                         * - only allow start game if first 3 names are set and an random east is selected
                         * - the fourth name can be inputted although no east is selected
                         */
                        // Check if any of the first three EditText inputs are empty or no RadioButton is selected
                        if (isEmptyName0 || isEmptyName1 || isEmptyName2 || binding.inputNamesLayout.east.getCheckedRadioButtonId() == -1) {
                            binding.inputNamesLayout.startGame.setEnabled(false);
                            binding.inputNamesLayout.name3.setEnabled(false);
                            binding.inputNamesLayout.east3.setEnabled(false);
                            if (isEmptyName0 || isEmptyName1 || isEmptyName2) {
                                //binding.inputNamesLayout.name3.setText("");
                                binding.inputNamesLayout.east3.setChecked(false);
                            }
                        } else {
                            binding.inputNamesLayout.startGame.setEnabled(true);
                            binding.inputNamesLayout.name3.setEnabled(true);
                            if (!isEmptyName3) {
                                binding.inputNamesLayout.east3.setEnabled(true);
                            } else {
                                binding.inputNamesLayout.east3.setChecked(false);
                                binding.inputNamesLayout.east3.setEnabled(false);
                                if (binding.inputNamesLayout.east.getCheckedRadioButtonId() == -1) {
                                    binding.inputNamesLayout.startGame.setEnabled(false);
                                }
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // Do nothing
                    }
                };


                // Add the TextWatcher to each EditText input
                binding.inputNamesLayout.name0.addTextChangedListener(textWatcher);
                binding.inputNamesLayout.name1.addTextChangedListener(textWatcher);
                binding.inputNamesLayout.name2.addTextChangedListener(textWatcher);
                binding.inputNamesLayout.name3.addTextChangedListener(textWatcher);
                binding.inputNamesLayout.east.setOnCheckedChangeListener((group, checkedId) -> textWatcher.onTextChanged(null, 0, 0, 0));

                binding.inputNamesLayout.startGame.setOnClickListener(v1 -> {
                    //TODO: code to start a new game
                });

                binding.inputNamesLayout.exitInput.setOnClickListener(v1 -> {
                    binding.inputNamesLayout.getRoot().setVisibility(View.GONE);
                    binding.newGameLayout.getRoot().setVisibility(View.VISIBLE);
                });
            });

            binding.newGameLayout.loadGame.setEnabled(false); //deactivated for now
            binding.newGameLayout.loadGame.setOnClickListener(v -> {
                //TODO: code to load a saved game
            });
        } else {
            binding.newGameLayout.getRoot().setVisibility(View.GONE);
        }


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}