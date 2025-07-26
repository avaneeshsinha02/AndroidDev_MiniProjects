package com.example.invadinghomescreen;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import java.util.ArrayList;
import java.util.List;

public class CollectionRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<String> itemList = new ArrayList<>();
    private final Context context;

    public CollectionRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    @Override public void onCreate() {
        for (int i = 1; i <= 10; i++) itemList.add("Item " + i);
    }

    @Override public int getCount() { return itemList.size(); }

    @Override public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_collection_view_item);
        views.setTextViewText(R.id.item_text, itemList.get(position));
        return views;
    }

    @Override public RemoteViews getLoadingView() { return null; }
    @Override public int getViewTypeCount() { return 1; }
    @Override public long getItemId(int position) { return position; }
    @Override public boolean hasStableIds() { return true; }
    @Override public void onDestroy() {}
    @Override public void onDataSetChanged() {}
}
