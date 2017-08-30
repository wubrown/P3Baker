package org.teamhis.nano.p3v2_baker;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill on 8/25/2017. Provides service and adapter (factory) needed to
 * create views for the ListView of Ingredients in the widget. Based on example:
 * https://android.googlesource.com/platform/development/+/master/samples/StackWidget/src/com/example/android/stackwidget/StackWidgetService.java
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent){
        return new ListRemoteViewsFactory(this.getApplicationContext(),intent);
    }
}
class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static int mCount=0;
    private List<String> mIngredients = new ArrayList<String>();
    private Context mContext;
    private int mAppWidgetId;

    public ListRemoteViewsFactory(Context context,Intent intent){
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    public void onCreate(){
        if(Recipes.getCurrentIngredientList()!= null){
        for(String i : Recipes.getCurrentIngredientList()){
            mIngredients.add(i);
        }}
        mCount = mIngredients.size();
    }
    public void onDestroy(){
        mIngredients.clear();
    }
    public int getCount() {
        return mCount;
    }
    public RemoteViews getViewAt(int position){
        RemoteViews rv = new RemoteViews(mContext.getPackageName(),R.layout.ingredient_item);
        rv.setTextViewText(R.id.ingredient_item,mIngredients.get(position));
        return rv;
    }
    public RemoteViews getLoadingView(){
        return null;
    }
    public int getViewTypeCount(){
        return 1;
    }
    public long getItemId(int position){
        return position;
    }
    public boolean hasStableIds(){
        return true;
    }
    public void onDataSetChanged(){
        mIngredients.clear();
        mCount = 0;
        if(Recipes.getCurrentIngredientList()!= null){
            for(String i : Recipes.getCurrentIngredientList()){
                mIngredients.add(i);
            }}
        mCount = mIngredients.size();
    }
}