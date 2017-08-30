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

import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

/**
 * Utility class is envisioned to hold static methods useful in multiple classes
 * in the Udacity Nano Degree P3_Baker project. See Sunshine Utility class for
 * parallel design.
 * Created by Bill on 7/26/2017.
 */

public class Utility {
    // Config tools assume updateConfig() is run during each onCreate() in Activities
    // and each onCreateView() in Fragments
    public static final int MOBILE_TABLET_BOUNDARY = 720;
    private static boolean mIsPortrait = true;
    private static boolean mIsTablet = false;
    private static int mSmallestScreenWidthDp;

    public static void updateConfig(Context context) {
        Configuration config = context.getResources().getConfiguration();
        mIsPortrait = config.orientation == config.ORIENTATION_PORTRAIT;
        mSmallestScreenWidthDp = config.smallestScreenWidthDp;
        mIsTablet = mSmallestScreenWidthDp >= MOBILE_TABLET_BOUNDARY;
    }
    public static boolean isPortrait(){
        return mIsPortrait;
    }
    public static boolean isTablet() {
        return mIsTablet;
    }
}
