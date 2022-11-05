package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ViewAchievementActivity gets list of possible achievements you can achieve,
 * as well as potential achievable point list passed down from intent,
 * and uses these passed down lists to show up as view in the screen.
 */
public class ViewAchievementActivity extends AppCompatActivity {
    private static final String ACHIEVEMENT_COLLECTION_LIST =
            "ca.cmpt276.chromiumproject - List of collections of available Achievements";
    private static final String POTENTIAL_SCORE_LIST =
            "ca.cmpt276.chromiumproject - List of potential achievable scores";

    private String[] achievementCollections = {};
    private List<String> actualAchievementList;

    private int[] potentialScoreCollections = {};
    private List<Integer> actualScoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_achievement);

        getSupportActionBar().hide();

        extractDataFromIntent();
        includeSpecialAchievement();

        populateAchievements();
    }

    private void includeSpecialAchievement() {
        actualAchievementList = new ArrayList<String>(Arrays.asList(achievementCollections));
        actualAchievementList.add(0, getString(R.string.special_achievement));
        actualScoreList =
                new ArrayList<Integer>(Arrays.stream(potentialScoreCollections).boxed().collect(Collectors.toList()));
        actualScoreList.add(0, potentialScoreCollections[0]);
    }

    public static Intent makeIntent (Context context, String[] achieveList, int[] scoreList) {
        Intent intent = new Intent(context, ViewAchievementActivity.class);

        intent.putExtra(ACHIEVEMENT_COLLECTION_LIST, achieveList);
        intent.putExtra(POTENTIAL_SCORE_LIST, scoreList);

        return intent;
    }
    private void extractDataFromIntent() {
        Intent intent = getIntent();

        achievementCollections = intent.getStringArrayExtra(ACHIEVEMENT_COLLECTION_LIST);
        potentialScoreCollections = intent.getIntArrayExtra(POTENTIAL_SCORE_LIST);
    }

    private void populateAchievements() {
        ArrayAdapter<String> adapterAchieve = new MyListAdapter();

        ListView achieveList = findViewById(R.id.listViewAchieveCollection);
        achieveList.setAdapter(adapterAchieve);
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        public MyListAdapter() {
            super(ViewAchievementActivity.this, R.layout.achievement_view, actualAchievementList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.achievement_view, parent, false);
            }

            String curAchieve = actualAchievementList.get(position);

            TextView achieveText = itemView.findViewById(R.id.item_achievement);
            achieveText.setText(curAchieve);
            TextView reqText = itemView.findViewById(R.id.item_req);
            reqText.setText(getString(R.string.display_required_score));

            if (position == 0) {
                int curAchScore = actualScoreList.get(position);
                TextView scoreText = itemView.findViewById(R.id.item_achieveScore);
                scoreText.setText("" + "<" + curAchScore);

                reqText.setText(getString(R.string.display_earned_by_score));
                achieveText.setText(getString(R.string.display_special_achievement) + curAchieve);
            }
            if (position != 0) {
                int curAchScore = actualScoreList.get(position);
                TextView scoreText = itemView.findViewById(R.id.item_achieveScore);
                scoreText.setText("" + curAchScore);
            }

            return itemView;
        }
    }
}