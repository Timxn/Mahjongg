package de.timon.mahjongg.ui.score;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.atomic.AtomicInteger;

import de.timon.mahjongg.ApplicationGlobals;
import de.timon.mahjongg.R;
import de.timon.mahjongg.Utils;
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
                bindButtons();
                loadNamesAndPoints();
                fixInputPoints();
                binding.ingameLayout.getRoot().setVisibility(View.VISIBLE);
                break;
            case NEWGAME:
                bindButtons();
                binding.newGameLayout.getRoot().setVisibility(View.VISIBLE);
                break;
            case INPUTNAMES:
                bindButtons();
                checkInputs();
                binding.inputNamesLayout.getRoot().setVisibility(View.VISIBLE);
                break;
        }

        return root;
    }

    public void loadNamesAndPoints() {
        binding.ingameLayout.name0.setText(scoreViewModel.getName(0));
        binding.ingameLayout.name1.setText(scoreViewModel.getName(1));
        binding.ingameLayout.name2.setText(scoreViewModel.getName(2));
        if (Utils.is4Players())
            binding.ingameLayout.name3.setText(scoreViewModel.getName(3));
        else
            binding.ingameLayout.name3.setText("");

        binding.ingameLayout.points0.setText(String.valueOf(scoreViewModel.getScore(0)));
        binding.ingameLayout.points1.setText(String.valueOf(scoreViewModel.getScore(1)));
        binding.ingameLayout.points2.setText(String.valueOf(scoreViewModel.getScore(2)));
        if (Utils.is4Players())
            binding.ingameLayout.points3.setText(String.valueOf(scoreViewModel.getScore(3)));
        else
            binding.ingameLayout.points3.setText("");

        binding.ingameLayout.difference0.setText(Utils.addPlusSign(scoreViewModel.getDifference(0)));
        binding.ingameLayout.difference1.setText(Utils.addPlusSign(scoreViewModel.getDifference(1)));
        binding.ingameLayout.difference2.setText(Utils.addPlusSign(scoreViewModel.getDifference(2)));
        if (Utils.is4Players())
            binding.ingameLayout.difference3.setText(Utils.addPlusSign(scoreViewModel.getDifference(3)));
        else
            binding.ingameLayout.difference3.setText("");

        binding.ingameLayout.mahjongg0.setText(String.valueOf(scoreViewModel.getMahjonggCount(0)));
        binding.ingameLayout.mahjongg1.setText(String.valueOf(scoreViewModel.getMahjonggCount(1)));
        binding.ingameLayout.mahjongg2.setText(String.valueOf(scoreViewModel.getMahjonggCount(2)));
        if (Utils.is4Players())
            binding.ingameLayout.mahjongg3.setText(String.valueOf(scoreViewModel.getMahjonggCount(3)));
        else
            binding.ingameLayout.mahjongg3.setText("");

        //TODO: add color to difference/mahjongg and make this dynamic
        if (scoreViewModel.east == 0)
            binding.ingameLayout.name0.setTextColor(Color.RED);
        else
            binding.ingameLayout.name0.setTextColor(Color.WHITE);
        if (scoreViewModel.east == 1)
            binding.ingameLayout.name1.setTextColor(Color.RED);
        else
            binding.ingameLayout.name1.setTextColor(Color.WHITE);
        if (scoreViewModel.east == 2)
            binding.ingameLayout.name2.setTextColor(Color.RED);
        else
            binding.ingameLayout.name2.setTextColor(Color.WHITE);
        if (scoreViewModel.east == 3)
            binding.ingameLayout.name3.setTextColor(Color.RED);
        else
            binding.ingameLayout.name3.setTextColor(Color.WHITE);
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
                } else {
                    binding.inputNamesLayout.name3.setEnabled(true);
                }
                if (isEmptyName3) {
                    binding.inputNamesLayout.east3.setEnabled(false);
                    binding.inputNamesLayout.east3.setChecked(false);
                    deselectEast();
                } else {
                    binding.inputNamesLayout.east3.setEnabled(true);
                }
                if (!isEmptyName0 && !isEmptyName1 && !isEmptyName2 && isEastSelected) {
                    binding.inputNamesLayout.startGame.setEnabled(true);
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
            int east;
            if (binding.inputNamesLayout.east0.isChecked())
                east = 0;
            else if (binding.inputNamesLayout.east1.isChecked())
                east = 1;
            else if (binding.inputNamesLayout.east2.isChecked())
                east = 2;
            else
                east = 3;

            scoreViewModel.startNewGame(binding.inputNamesLayout.name0.getText().toString(), binding.inputNamesLayout.name1.getText().toString(), binding.inputNamesLayout.name2.getText().toString(), binding.inputNamesLayout.name3.getText().toString(), east);
            loadNamesAndPoints();
            binding.inputNamesLayout.getRoot().setVisibility(View.GONE);
            binding.ingameLayout.getRoot().setVisibility(View.VISIBLE);
            scoreViewModel.view = ScoreViewModel.View.INGAME;
        });

        binding.inputNamesLayout.exitInput.setOnClickListener(v1 -> {
            binding.inputNamesLayout.getRoot().setVisibility(View.GONE);
            binding.newGameLayout.getRoot().setVisibility(View.VISIBLE);
            scoreViewModel.view = ScoreViewModel.View.NEWGAME;
        });

        binding.ingameLayout.inputPoints.setOnClickListener(v1 -> {
            binding.inputPointsLayout.name0.setText(scoreViewModel.getName(0));
            binding.inputPointsLayout.name1.setText(scoreViewModel.getName(1));
            binding.inputPointsLayout.name2.setText(scoreViewModel.getName(2));
            binding.inputPointsLayout.name3.setText(scoreViewModel.getName(3));
            if (ApplicationGlobals.playerCount == 3)
                binding.inputPointsLayout.thirdPlayer.setVisibility(View.INVISIBLE);
            else
                binding.inputPointsLayout.thirdPlayer.setVisibility(View.VISIBLE);
            binding.ingameLayout.getRoot().setVisibility(View.GONE);
            binding.inputPointsLayout.getRoot().setVisibility(View.VISIBLE);
        });
        /* //TODO: implement end game button when load game is implemented and also add a are u sure window
        binding.ingameLayout.endGame.setOnClickListener(v1 -> {
            binding.ingameLayout.getRoot().setVisibility(View.GONE);
            binding.newGameLayout.getRoot().setVisibility(View.VISIBLE);
            scoreViewModel.view = ScoreViewModel.View.NEWGAME;
        });*/

        AtomicInteger mahjongg = new AtomicInteger();
        mahjongg.set(-1);

        binding.inputPointsLayout.name0.setOnClickListener(v1 -> {
            mahjongg.set(0);
            setColsNormal();
            binding.inputPointsLayout.name0.setBackgroundColor(getResources().getColor(R.color.dark_red));
        });

        binding.inputPointsLayout.name1.setOnClickListener(v1 -> {
            mahjongg.set(1);
            setColsNormal();
            binding.inputPointsLayout.name1.setBackgroundColor(getResources().getColor(R.color.dark_red));
        });

        binding.inputPointsLayout.name2.setOnClickListener(v1 -> {
            mahjongg.set(2);
            setColsNormal();
            binding.inputPointsLayout.name2.setBackgroundColor(getResources().getColor(R.color.dark_red));
        });

        binding.inputPointsLayout.name3.setOnClickListener(v1 -> {
            mahjongg.set(3);
            setColsNormal();
            binding.inputPointsLayout.name3.setBackgroundColor(getResources().getColor(R.color.dark_red));
        });

        binding.inputPointsLayout.inputPoints.setOnClickListener(v1 -> {
            scoreViewModel.addPoints(validatePoints(), mahjongg.get());
            mahjongg.set(-1);
            fixInputPoints();
            binding.inputPointsLayout.getRoot().setVisibility(View.GONE);
            loadNamesAndPoints();
            binding.ingameLayout.getRoot().setVisibility(View.VISIBLE);
        });

        binding.inputPointsLayout.cancelInput.setOnClickListener(v1 -> {
            binding.inputPointsLayout.getRoot().setVisibility(View.GONE);
            binding.ingameLayout.getRoot().setVisibility(View.VISIBLE);
        });
    }

    private void setColsNormal() { //TODO: make this more efficient
        binding.inputPointsLayout.name0.setBackgroundColor(Color.TRANSPARENT);
        binding.inputPointsLayout.name1.setBackgroundColor(Color.TRANSPARENT);
        binding.inputPointsLayout.name2.setBackgroundColor(Color.TRANSPARENT);
        binding.inputPointsLayout.name3.setBackgroundColor(Color.TRANSPARENT);
    }

    private int[] validatePoints() {
        String p0 = binding.inputPointsLayout.ePoints0.getText().toString();
        int p0i;
        if (!p0.equals(""))
            p0i = Integer.parseInt(p0);
        else
            p0i = 0;
        String p1 = binding.inputPointsLayout.ePoints1.getText().toString();
        int p1i;
        if (!p1.equals(""))
            p1i = Integer.parseInt(p1);
        else
            p1i = 0;
        String p2 = binding.inputPointsLayout.ePoints2.getText().toString();
        int p2i;
        if (!p2.equals(""))
            p2i = Integer.parseInt(p2);
        else
            p2i = 0;
        String p3 = binding.inputPointsLayout.ePoints3.getText().toString();
        int p3i;
        if (!p3.equals(""))
            p3i = Integer.parseInt(p3);
        else
            p3i = 0;

        return new int[] {p0i, p1i, p2i, p3i};
    }

    private void fixInputFields() {
        binding.inputNamesLayout.east.clearCheck();
        binding.inputNamesLayout.name0.setText("");
        binding.inputNamesLayout.name1.setText("");
        binding.inputNamesLayout.name2.setText("");
        binding.inputNamesLayout.name3.setText("");
    }

    private void fixInputPoints() {
        setColsNormal();
        binding.inputPointsLayout.ePoints0.setText("");
        binding.inputPointsLayout.ePoints1.setText("");
        binding.inputPointsLayout.ePoints2.setText("");
        binding.inputPointsLayout.ePoints3.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}