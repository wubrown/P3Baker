/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.teamhis.nano.p3v2_baker;

import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;


import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Step detail pain within a larger activity viewgroup.
 */
public class StepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String STEP_DESCRIPTION = "step_description";
    public static final String STEP_VIDEO = "step_video";
    public static final String VIDEO_POSITION = "video_position";
    public static final String STEP_IMAGE = "step_image";
    public static final String STEP_IS_INGREDIENTS = "step_is_ingredients";
    private String mStepDescription;
    private String mStepVideoUrl;
    private String mStepImage;
    private boolean mStepIsIngredients;
    private boolean mIsTablet;
    private boolean mIsPortrait;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.updateConfig(getContext());
        mIsTablet = Utility.isTablet();
        mIsPortrait = Utility.isPortrait();

        if (getArguments().containsKey(STEP_DESCRIPTION)) {
            mStepDescription = getArguments().getString(STEP_DESCRIPTION);
            mStepVideoUrl = getArguments().getString(STEP_VIDEO);
            mStepImage = getArguments().getString(STEP_IMAGE);
            mStepIsIngredients = getArguments().getBoolean(STEP_IS_INGREDIENTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        mPlayerView = ((SimpleExoPlayerView)rootView.findViewById(R.id.player_view));

        Uri uri = Uri.parse(mStepVideoUrl);
//        ((TextView) rootView.findViewById(R.id.step_detail_video)).setText(mStepVideoUrl);
        Handler handler = new Handler();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(handler,null);
        MediaSource sampleSource = new ExtractorMediaSource(
                uri,
                new DefaultDataSourceFactory(getContext(),"P3v2_Baker",bandwidthMeter),
                new DefaultExtractorsFactory(),null,null);

        if(mExoPlayer == null) {
            Handler mainHandler = new Handler();
            // Was unable to import AdaptiveTrackSelection as used in examples...
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            DefaultLoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        }
        long videoPosition = C.TIME_UNSET;
        if(savedInstanceState != null){
            videoPosition = savedInstanceState.getLong(VIDEO_POSITION, C.TIME_UNSET);
        }
        if(videoPosition != C.TIME_UNSET)
            mExoPlayer.seekTo(videoPosition);
        mExoPlayer.prepare(sampleSource);
        mPlayerView.setPlayer(mExoPlayer);

        ImageView iv = ((ImageView)rootView.findViewById(R.id.step_image));
        if(mStepImage.length()==0){
            iv.setVisibility(View.GONE);
        } else if(mIsPortrait || mIsTablet){
            Picasso.with(getContext()).load(mStepImage).into(iv);
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.GONE);
        }
        FrameLayout fl = (FrameLayout) rootView.findViewById(R.id.step_detail_media_frame);
        fl.setVisibility(mStepIsIngredients? View.GONE : View.VISIBLE);
        TextView tv = ((TextView)rootView.findViewById(R.id.step_detail_text));
        tv.setText(mStepDescription);
        tv.setVisibility(mIsPortrait || mIsTablet || mStepIsIngredients? View.VISIBLE : View.GONE);
        return rootView;
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            mExoPlayer.release();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putLong(VIDEO_POSITION,mExoPlayer.getCurrentPosition());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            mExoPlayer.release();
        }
    }
}
