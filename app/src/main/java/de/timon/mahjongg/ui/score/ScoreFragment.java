package de.timon.mahjongg.ui.score;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.timon.mahjongg.databinding.FragmentScoreBinding;

public class ScoreFragment extends Fragment {

    private FragmentScoreBinding binding;
    private ScoreViewModel scoreViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);
        binding = FragmentScoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        switch (scoreViewModel.view) {
            case INGAME:
                binding.ingameLayout.getRoot().setVisibility(View.VISIBLE);
                break;
            case NEWGAME:
                binding.newGameLayout.getRoot().setVisibility(View.VISIBLE);
                bindButtons();
                break;
            case INPUTNAMES:
                binding.inputNamesLayout.getRoot().setVisibility(View.VISIBLE);
                bindButtons();
                fixInputFields();
                checkInputs();
                break;
        }

        return root;
    }

    /**
     * - only allow four names to be input if first three are written and if on of first 3 is empty delete the fourth name again
     * - only allow fourth name to be east if the name is set, if name removed remove the check from east
     * - also only allow east 3 button to be clickable if name 3 is set
     * - only allow start game if first 3 names are set and an random east is selected
     * - the fourth name can be inputted although no east is selected
     */
    private void checkInputs() {

        TextWatcher textWatcher = new TextWatcher() {

            boolean isEmptyName0;
            boolean isEmptyName1;
            boolean isEmptyName2;
            boolean isEmptyName3;
            boolean isEastSelected;
            int eastId;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validate();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                validate();
            }

            private void validate() {
                isEmptyName0 = binding.inputNamesLayout.name0.getText().toString().equals("");
                isEmptyName1 = binding.inputNamesLayout.name1.getText().toString().equals("");
                isEmptyName2 = binding.inputNamesLayout.name2.getText().toString().equals("");
                isEmptyName3 = binding.inputNamesLayout.name3.getText().toString().equals("");
                isEastSelected = binding.inputNamesLayout.east.getCheckedRadioButtonId() != -1;
                eastId = binding.inputNamesLayout.east.getCheckedRadioButtonId();

                if (isEmptyName0 || isEmptyName1 || isEmptyName2) {
                    binding.inputNamesLayout.name3.setEnabled(false);
                    binding.inputNamesLayout.east3.setEnabled(false);
                    scoreViewModel.tempValue = 0;
                } else {
                    binding.inputNamesLayout.name3.setEnabled(true);
                    scoreViewModel.tempValue = 2;
                }
                if (isEmptyName3) {
                    binding.inputNamesLayout.east3.setEnabled(false);
                    binding.inputNamesLayout.east3.setChecked(false);
                    deselectEast();
                } else {
                    binding.inputNamesLayout.east3.setEnabled(true);
                    scoreViewModel.tempValue = 3;
                }
                if (!isEmptyName0 && !isEmptyName1 && !isEmptyName2 && isEastSelected) {
                    binding.inputNamesLayout.startGame.setEnabled(true);
                    scoreViewModel.tempValue = 1;
                } else {
                    binding.inputNamesLayout.startGame.setEnabled(false);
                }
            }

            private void deselectEast() {
                if (eastId == binding.inputNamesLayout.east3.getId() && isEastSelected) {
                    binding.inputNamesLayout.east.clearCheck();
                }
            }
        };

        // Add the TextWatcher to each EditText input
        binding.inputNamesLayout.name0.addTextChangedListener(textWatcher);
        binding.inputNamesLayout.name1.addTextChangedListener(textWatcher);
        binding.inputNamesLayout.name2.addTextChangedListener(textWatcher);
        binding.inputNamesLayout.name3.addTextChangedListener(textWatcher);
        binding.inputNamesLayout.east.setOnCheckedChangeListener((group, checkedId) -> textWatcher.onTextChanged(null, 0, 0, 0));
    }

    private void bindButtons() {
        binding.newGameLayout.createGame.setOnClickListener(v -> {
            binding.newGameLayout.getRoot().setVisibility(View.GONE);
            fixInputFields();
            checkInputs();
            binding.inputNamesLayout.getRoot().setVisibility(View.VISIBLE);
            scoreViewModel.view = ScoreViewModel.View.INPUTNAMES;
        });

        binding.newGameLayout.loadGame.setEnabled(false); //deactivated for now
        binding.newGameLayout.loadGame.setOnClickListener(v -> {
            //TODO: code to load a saved game
        });

        binding.inputNamesLayout.startGame.setOnClickListener(v1 -> {
            //scoreViewModel.gameInProgress = true;
            scoreViewModel.names[0] = binding.inputNamesLayout.name0.getText().toString();
            scoreViewModel.names[1] = binding.inputNamesLayout.name1.getText().toString();
            scoreViewModel.names[2] = binding.inputNamesLayout.name2.getText().toString();
            if (binding.inputNamesLayout.name3.getText().toString().equals("")) {
                scoreViewModel.playerCount = 3;
            }
            else {
                scoreViewModel.names[3] = binding.inputNamesLayout.name3.getText().toString();
            }
            scoreViewModel.east = binding.inputNamesLayout.east.getCheckedRadioButtonId();

            binding.inputNamesLayout.getRoot().setVisibility(View.GONE);
            binding.ingameLayout.getRoot().setVisibility(View.VISIBLE);
            scoreViewModel.view = ScoreViewModel.View.INGAME;
        });

        binding.inputNamesLayout.exitInput.setOnClickListener(v1 -> {
            binding.inputNamesLayout.getRoot().setVisibility(View.GONE);
            binding.newGameLayout.getRoot().setVisibility(View.VISIBLE);
            scoreViewModel.view = ScoreViewModel.View.NEWGAME;
        });
    }

    private void fixInputFields() {
        if (scoreViewModel.tempValue == 1) {
            binding.inputNamesLayout.name3.setEnabled(true);
            binding.inputNamesLayout.east3.setEnabled(true);
            binding.inputNamesLayout.startGame.setEnabled(true);
        } else if (scoreViewModel.tempValue == 2) {
            binding.inputNamesLayout.name3.setEnabled(true);
        } else if (scoreViewModel.tempValue == 3) {
            binding.inputNamesLayout.name3.setEnabled(true);
            binding.inputNamesLayout.east3.setEnabled(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}