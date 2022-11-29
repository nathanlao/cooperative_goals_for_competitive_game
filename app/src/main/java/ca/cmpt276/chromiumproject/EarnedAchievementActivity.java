package ca.cmpt276.chromiumproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import ca.cmpt276.chromiumproject.model.Achievement;
import ca.cmpt276.chromiumproject.model.GameConfig;
import ca.cmpt276.chromiumproject.model.GameManager;
import ca.cmpt276.chromiumproject.model.GameRecord;

/**EarnedAchievementActivity is displayed after a user saves a new game record and earns an achievement level. A congratulatory message appears alongside a
 * sound effect, image, animation, and the achievement level earned.
 * Background photo from https://unsplash.com/photos/xplrF8WMitE, firework animation from cupids@aphrodite.global
 * Sound from https://pixabay.com/sound-effects/pleased-crowdflac-14484/
 */
public class EarnedAchievementActivity extends AppCompatActivity {

    public static final int ANIMATION_DURATION = 5000;
    public static final int FIREWORKS1_X_TRANSLATION = 600;
    public static final int FIREWORKS1_Y_TRANSLATION = -600;
    public static final int FIREWORKS2_X_TRANSLATION = -600;
    public static final int FIREWORKS2_Y_TRANSLATION = -600;
    public static final String GAME_CONFIG_POSITION = "game config position";
    private static final String EXTRA_RECORD_GAME_POSITION = "String" ;
    public static final int DEFAULT_VALUE = 0;
    private static int SPLASH_TIME = 10*1000;

    private TextView earnedAchievementTxt;
    private String[] achievementCollections;

    private GameManager gameManager;
    private GameRecord gameRecord;
    private GameConfig gameConfig;
    MediaPlayer cheeringAudio;
    private int gameConfigPosition;

    public static Intent makeEarnedAchievementIntent(Context c, int gameConfigPosition) {
        Intent intent = new Intent(c, EarnedAchievementActivity.class);
        intent.putExtra(GAME_CONFIG_POSITION, gameConfigPosition);
        return intent;
    }

    private int extractDataFromIntent(){
        Intent intent = getIntent();
        int position = intent.getIntExtra(GAME_CONFIG_POSITION, DEFAULT_VALUE);
        return position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earned_achievement);
        setTitle(getString(R.string.earned_achievement));

        gameConfigPosition = extractDataFromIntent();
        setText(gameConfigPosition);


        ImageView fireworks1 = findViewById(R.id.fireworksAnimation);
        ImageView fireworks2 = findViewById(R.id.fireworksAnimation2);

        runAnimation(fireworks1, FIREWORKS1_X_TRANSLATION, FIREWORKS1_Y_TRANSLATION);
        runAnimation(fireworks2, FIREWORKS2_X_TRANSLATION, FIREWORKS2_Y_TRANSLATION);
        cheeringAudio = MediaPlayer.create(EarnedAchievementActivity.this, R.raw.cheering_audio);
        cheeringAudio.start();

        setUpSkipBtn();

    }

    private void setUpTimeLimit() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, SPLASH_TIME);
    }

    private void setUpSkipBtn() {
        Button skipBtn = (Button)findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cheeringAudio.stop();
                finish();
            }
        });
    }

    private void setText(int gameConfigPosition) {
       earnedAchievementTxt = findViewById(R.id.earnedAchievementTxt);
       earnedAchievementTxt.setText(getResources().getString(R.string.achievement_level_congratulations_message, getAchievementLevel(gameConfigPosition)));
    }
    

    private String getAchievementLevel(int gameConfigPosition) {
        String theme = AchievementSettingsActivity.getTheme(EarnedAchievementActivity.this);
        String[] achievementNames = getAchievementNames(theme);

        gameManager = GameManager.getInstance();
        gameConfig = gameManager.getGameConfigByIndex(gameConfigPosition);
        int gameRecordPosition = gameConfig.getNumGameRecords();
        gameRecord = gameConfig.getGameRecords().get(gameRecordPosition-1);
        int achievementLevel = gameRecord.getAchievementLevel();
        String achievementTitle = null;
        // check if Special Worst Achievement

        if (achievementLevel == Achievement.SPECIAL_WORST_ACHIEVE) {
                String[] themeOptions = getResources().getStringArray(R.array.theme_names);

                //using if statements rather than case due to == operator not working properly with strings
                if (Objects.equals(theme, themeOptions[0])) {
                    achievementTitle = getString(R.string.special_achievement);
                } else if (Objects.equals(theme, themeOptions[1])) {
                    achievementTitle = getString(R.string.enchanted_forest_special_achievement);
                } else if (Objects.equals(theme, themeOptions[2])) {
                    achievementTitle = getString(R.string.dark_tribe_special_achievement);
                }
        } else {
            achievementTitle = achievementNames[achievementLevel];
        }

        return achievementTitle;
    }

    private String[] getAchievementNames(String theme) {
        String[] themeOptions = getResources().getStringArray(R.array.theme_names);
        switch (theme) {
            case "Adventurer":
                achievementCollections = getResources().getStringArray(R.array.achievement_names);
                return achievementCollections;

            case "Enchanted Forest":
                achievementCollections = getResources().getStringArray(R.array.enchanted_forest_achievement_names);
                return achievementCollections;

            case "Dark Tribe":
                achievementCollections = getResources().getStringArray(R.array.achievement_dark_tribe);
                return achievementCollections;
        }
        return achievementCollections;
    }

    private void runAnimation(ImageView fireworks, int xTranslation, int yTranslation) {
        //animation tutorial: https://stackoverflow.com/questions/12983681/translateanimation-on-imageview-android
        //and https://stackoverflow.com/questions/20608073/how-to-rotate-imageview-from-its-centre-position

        int xPosition = fireworks.getLeft();
        int yPosition = fireworks.getTop();

        AnimationSet animationSet = new AnimationSet(false);

        //translation animation
        Animation translateAnimation = new TranslateAnimation(xPosition, xPosition+ xTranslation, yPosition, yPosition+ yTranslation);
        translateAnimation.setDuration(ANIMATION_DURATION);
        translateAnimation.setFillAfter(true);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationSet.addAnimation(translateAnimation);


        fireworks.startAnimation(animationSet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_earned_achievement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {

            case R.id.action_achievement_settings:
                Intent i = AchievementSettingsActivity.makeAchievementSettingsIntent(EarnedAchievementActivity.this);
                startActivity(i);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onResume() {
        super.onResume();
        setText(gameConfigPosition);


    }
}