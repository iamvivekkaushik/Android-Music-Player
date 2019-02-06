package com.vivek.musicplayer.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.vivek.musicplayer.R;
import com.vivek.musicplayer.helper.Song;
import com.vivek.musicplayer.player.PlayerState;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service {
    private static final String TAG = "MusicPlayerService";

    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";

    private MediaPlayer mMediaPlayer;
    private MediaSessionManager mManager;
    private MediaSessionCompat mSession;
    private MediaControllerCompat mController;

    private AudioManager mAudioManager;
    int focusRequest;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void handleIntent( Intent intent ) {
        if( intent == null || intent.getAction() == null )
            return;

        String action = intent.getAction();

        if( action.equalsIgnoreCase(ACTION_PLAY)) {
            mController.getTransportControls().play();
        } else if( action.equalsIgnoreCase(ACTION_PAUSE)) {
            mController.getTransportControls().pause();
        } else if( action.equalsIgnoreCase(ACTION_FAST_FORWARD)) {
            mController.getTransportControls().fastForward();
        } else if( action.equalsIgnoreCase(ACTION_REWIND)) {
            mController.getTransportControls().rewind();
        } else if( action.equalsIgnoreCase(ACTION_PREVIOUS)) {
            mController.getTransportControls().skipToPrevious();
        } else if( action.equalsIgnoreCase(ACTION_NEXT)) {
            mController.getTransportControls().skipToNext();
        } else if( action.equalsIgnoreCase(ACTION_STOP)) {
            mController.getTransportControls().stop();
        }
    }

    private NotificationCompat.Action generateAction(int icon, String title, String intentAction ) {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        return new NotificationCompat.Action.Builder(icon, title, pendingIntent).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initMediaSessions();
    }

    private void playMusic() {

        switch (focusRequest) {
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                // don't start playback
                break;
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                // actually start playback
                if(mMediaPlayer != null) {
                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(getSongFromPlaylist(PlayerState.currentSongId).getSongPath());
                        mMediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "playMusic: IOException occured");
                    }
                    mMediaPlayer.seekTo(PlayerState.songCurrentTime);
                    mMediaPlayer.start();
                    PlayerState.setMusicPlaying(true);
                    timeBroadcast();
                } else {
                    initMediaSessions();
                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(getSongFromPlaylist(PlayerState.currentSongId).getSongPath());
                        mMediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "playMusic: IOException occured");
                    }
                    mMediaPlayer.seekTo(PlayerState.songCurrentTime);
                    mMediaPlayer.start();
                    PlayerState.setMusicPlaying(true);
                    timeBroadcast();
                }
                break;
        }
    }

    private void notificationBuilder(NotificationCompat.Action action) {
        // Given a media session and its context (usually the component containing the session)
        // Create a NotificationCompat.Builder

        Song currentSong = getSongFromPlaylist(PlayerState.currentSongId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Skull");

        builder
                // Make the transport controls visible on the lockscreen
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                // Add an app icon and set its accent color
                // Be careful about the color
                .setSmallIcon(R.drawable.ic_app_icon)
                //.setColor(ContextCompat.getColor(this, R.color.hoki))

                // Add the metadata for the currently playing track
                .setContentTitle(currentSong.getSongTitle())
                .setContentText(currentSong.getSongArtist())
                .setLargeIcon(getSongArt(currentSong.getSongArt()))

                // Enable launching the player by clicking the notification
                .setContentIntent(mController.getSessionActivity())

                // Stop the service when the notification is swiped away
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                        PlaybackStateCompat.ACTION_STOP))

                // Add a Music Control buttons
                .addAction(generateAction(R.drawable.ic_notification_previous_button, "Previous", ACTION_PREVIOUS)) // #0
                .addAction(action) // #1
                .addAction(generateAction(R.drawable.ic_notification_next_button, "Next", ACTION_NEXT)) // #2
                // Take advantage of MediaStyle features
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2)

                        // Add a cancel button
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                                PlaybackStateCompat.ACTION_STOP)));

        // Display the notification and place the service in the foreground
        startForeground(1, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaSessions() {
        mMediaPlayer = new MediaPlayer();

        mSession = new MediaSessionCompat(getApplicationContext(), TAG);
        try {
            mController =new MediaControllerCompat(getApplicationContext(), mSession.getSessionToken());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        focusRequest = mAudioManager.requestAudioFocus(
                new AudioFocusHelper(), // Need to implement listener
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        mSession.setCallback(new MediaSessionCompat.Callback(){
                                 @Override
                                 public void onPlay() {
                                     super.onPlay();
                                     //MediaService onPlay
                                     notificationBuilder(generateAction(R.drawable.ic_pause_button, "Pause", ACTION_PAUSE));
                                     playMusic();
                                 }

                                 @Override
                                 public void onPause() {
                                     super.onPause();
                                     // MediaService onPause
                                     notificationBuilder(generateAction(R.drawable.ic_play_button, "Play", ACTION_PLAY));
                                     mMediaPlayer.pause();
                                     PlayerState.setMusicPlaying(false);
                                 }

                                 @Override
                                 public void onSkipToNext() {
                                     super.onSkipToNext();
                                     //Change media here

                                     Song currentSong = PlayerState.nowPlaying.get(PlayerState.songNumberInPlaylist+1);
                                     PlayerState.currentSongId = currentSong.getSongId();
                                     PlayerState.songCurrentTime = 0;
                                     playMusic();
                                     PlayerState.setMusicPlaying(true);
                                     notificationBuilder(generateAction(R.drawable.ic_pause_button, "Pause", ACTION_PAUSE));
                                 }

                                 @Override
                                 public void onSkipToPrevious() {
                                     super.onSkipToPrevious();
                                     //Change media here
                                     Song currentSong = PlayerState.nowPlaying.get(PlayerState.songNumberInPlaylist-1);
                                     PlayerState.currentSongId = currentSong.getSongId();
                                     PlayerState.songCurrentTime = 0;
                                     playMusic();
                                     PlayerState.setMusicPlaying(true);
                                     notificationBuilder(generateAction(R.drawable.ic_pause_button, "Pause", ACTION_PAUSE));
                                 }

                                 @Override
                                 public void onStop() {
                                     super.onStop();
                                     //Stop media player here
                                     NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                     notificationManager.cancel( 1 );
                                     Intent intent = new Intent( getApplicationContext(), MusicPlayerService.class );
                                     stopService( intent );
                                 }

                                 @Override
                                 public void onSeekTo(long pos) {
                                     super.onSeekTo(pos);
                                 }
                             }
        );
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mSession.release();
        return super.onUnbind(intent);
    }


    private Song getSongFromPlaylist(String songId) {
        ArrayList<Song> playlist = PlayerState.nowPlaying;
        int i = 0;
        for (Song song : playlist){
            i++;
            if(song.getSongId().equals(songId)) {
                PlayerState.songNumberInPlaylist = i-1;
                return song;
            }
        }
        return null;
    }

    private Bitmap getSongArt (String albumArt) {
        if(albumArt == null) {
            // Do something
            return BitmapFactory.decodeResource(getResources(), R.drawable.reputation_art);
        } else {
            return BitmapFactory.decodeFile(albumArt);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public void timeBroadcast(){
        final Handler h = new Handler();

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                if(mMediaPlayer != null)
                PlayerState.songCurrentTime = mMediaPlayer.getCurrentPosition();

                if(PlayerState.isMusicPlaying()){
                    h.postDelayed(this, 1);
                }
            }
        };

        h.postDelayed(r, 0);
    }

    private final class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {

        private boolean mPlayOnAudioFocus;

        private void abandonAudioFocus() {
            mAudioManager.abandonAudioFocus(this);
        }

        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    if (mPlayOnAudioFocus && !mMediaPlayer.isPlaying()) {
                        playMusic();
                    } else if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.setVolume(1.0f, 1.0f);
                    }
                    mPlayOnAudioFocus = false;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mMediaPlayer.setVolume(0.2f, 0.2f);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (mMediaPlayer.isPlaying()) {
                        mPlayOnAudioFocus = true;
                        mMediaPlayer.pause();
                        PlayerState.setMusicPlaying(false);
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    mAudioManager.abandonAudioFocus(this);
                    mPlayOnAudioFocus = false;
                    mMediaPlayer.stop();
                    mMediaPlayer = null;
                    PlayerState.setMusicPlaying(false);
                    break;
            }
        }
    }
}