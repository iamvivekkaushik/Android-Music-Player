package com.vivek.musicplayer;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.vivek.musicplayer.fragments.AlbumFragment;
import com.vivek.musicplayer.fragments.ArtistFragment;
import com.vivek.musicplayer.fragments.GenreFragment;
import com.vivek.musicplayer.fragments.PlaylistFragment;
import com.vivek.musicplayer.fragments.SongsFragment;
import com.vivek.musicplayer.player.PlayerState;


public class MainActivity extends AppCompatActivity {
    // transition properties
    public static String PROPNAME_SCREENLOCATION_LEFT = "rsspace:location:left";
    public static String PROPNAME_SCREENLOCATION_TOP = "rsspace:location:top";
    public static String PROPNAME_WIDTH = "rsspace:width";
    public static String PROPNAME_HEIGHT = "rsspace:height";
    public static final String VIEW_INFO_EXTRA = "view_info";

    private ImageView playButton;
    ImageView songImage;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Music library");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Songs"));
        tabLayout.addTab(tabLayout.newTab().setText("Artists"));
        tabLayout.addTab(tabLayout.newTab().setText("Albums"));
        tabLayout.addTab(tabLayout.newTab().setText("Playlists"));
        tabLayout.addTab(tabLayout.newTab().setText("Genres"));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        playButton = findViewById(R.id.playButton);
        songImage = findViewById(R.id.songImage);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(PlayerState.isMusicPlaying()){
            playButton.setImageResource(R.drawable.ic_pause_button_light_black);
        } else {
            playButton.setImageResource(R.drawable.ic_play_button_light_black);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(this, SearchableActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        return super.onCreateOptionsMenu(menu);
    }

    public void playButton(View view) {
        if(PlayerState.isMusicPlaying()){
            //Pause the music and set the icon to play
            PlayerState.setMusicPlaying(false);
            playButton.setImageResource(R.drawable.ic_play_button_light_black);
        } else {
            // Play the music and set the icon to pause
            PlayerState.setMusicPlaying(true);
            playButton.setImageResource(R.drawable.ic_pause_button_light_black);
        }
    }

    public void playerActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
        intent.putExtra(VIEW_INFO_EXTRA, /* start values */ captureValues(songImage));
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private Bundle captureValues(@NonNull View view) {
        Bundle b = new Bundle();
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        b.putInt(PROPNAME_SCREENLOCATION_LEFT, screenLocation[0]);
        b.putInt(PROPNAME_SCREENLOCATION_TOP, screenLocation[1]);
        b.putInt(PROPNAME_WIDTH, view.getWidth());
        b.putInt(PROPNAME_HEIGHT, view.getHeight());
        return b;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new SongsFragment();
                case 1:
                    return new ArtistFragment();
                case 2:
                    return new AlbumFragment();
                case 3:
                    return new PlaylistFragment();
                case 4:
                    return new GenreFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }
    }
}
