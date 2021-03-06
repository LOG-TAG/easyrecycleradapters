package com.carlosdelachica.easyrecycleradapters.recycler_view_manager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.carlosdelachica.easyrecycleradapters.R;
import com.carlosdelachica.easyrecycleradapters.adapter.EasyRecyclerAdapter;
import com.carlosdelachica.easyrecycleradapters.decorations.DividerItemDecoration;

import java.util.List;

import static android.support.v7.widget.RecyclerView.LayoutManager;
import static com.carlosdelachica.easyrecycleradapters.adapter.EasyViewHolder.OnItemClickListener;
import static com.carlosdelachica.easyrecycleradapters.adapter.EasyViewHolder.OnItemLongClickListener;

public class EasyRecyclerViewManager {

    private RecyclerView recyclerView;
    private TextView emptyLoadingListTextView;
    private EasyRecyclerAdapter adapter;
    private LayoutManager layoutManager;
    private Context context;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener longClickListener;
    private DividerItemDecoration dividerItemDecoration;
    private String loadingListText;
    private String emptyListText;
    private int loadingTextColor;
    private int emptyListTextColor;
    private boolean clipToPadding;
    private boolean hasFixedSize;

    EasyRecyclerViewManager(Context context,
                            RecyclerView recyclerView,
                            LayoutManager layoutManager,
                            EasyRecyclerAdapter adapter,
                            TextView emptyLoadingListTextView,
                            String emptyListText,
                            int emptyTextColor,
                            String loadingListText,
                            int loadingTextColor,
                            OnItemLongClickListener longClickListener,
                            OnItemClickListener clickListener,
                            DividerItemDecoration dividerDrawableRes,
                            boolean clipToPadding,
                            boolean hasFixedSize) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.layoutManager = layoutManager;
        this.adapter = adapter;
        this.emptyLoadingListTextView = emptyLoadingListTextView;
        this.emptyListText = emptyListText;
        this.emptyListTextColor = emptyTextColor;
        this.loadingListText = loadingListText;
        this.loadingTextColor = loadingTextColor;
        this.longClickListener = longClickListener;
        this.itemClickListener = clickListener;
        this.dividerItemDecoration = dividerDrawableRes;
        this.clipToPadding = clipToPadding;
        this.hasFixedSize = hasFixedSize;
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setClipToPadding(clipToPadding);
        recyclerView.setHasFixedSize(hasFixedSize);
        initRecyclerViewPadding();
        initAdapter();
        initLayoutManager();
        initItemDecorations();
        initEmptyListTextView();
    }

    private void initRecyclerViewPadding() {
        //Apply padding as described in Material Design specs
        int topBottomPadding = context.getResources().getDimensionPixelSize(R.dimen.material_design_list_top_bottom_padding);
        setRecyclerViewPadding(0, topBottomPadding, 0, topBottomPadding);
    }

    private void initAdapter() {
        recyclerView.setAdapter(adapter);
        initListeners();
    }

    private void initListeners() {
        adapter.setOnClickListener(itemClickListener);
        adapter.setOnLongClickListener(longClickListener);
    }

    private void initLayoutManager() {
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initItemDecorations() {
        initDivider();
    }

    private void initDivider() {
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void initEmptyListTextView() {
        updateEmptyListTextView(EmptyLoadingListTextViewState.LOADING);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerViewPadding(int left, int top, int right, int bottom) {
        recyclerView.setPadding(left, top, right, bottom);
    }

    public int getCount() {
        return adapter.getItemCount();
    }

    public void add(Object object, int position) {
        adapter.add(object, position);
        updateLoadedListTextView();
    }

    public void add(Object data) {
        adapter.add(data);
        updateLoadedListTextView();
    }

    public void addAll(List<?> data) {
        adapter.addAll(data);
        updateLoadedListTextView();
    }

    public boolean update(Object data) {
        return adapter.update(data);
    }

    public boolean update(Object data, int position) {
        return adapter.update(data, position);
    }

    public void remove(Object data) {
        adapter.remove(data);
        updateLoadedListTextView();
    }

    public void remove(int position) {
        adapter.remove(position);
        updateLoadedListTextView();
    }

    public Object getItem(int position) {
        return adapter.get(position);
    }

    public void onRefresh() {
        updateLoadingListTextView();
        adapter.clear();
    }

    private void updateLoadedListTextView() {
        updateEmptyListTextView(adapter.getItemCount() > 0 ? EmptyLoadingListTextViewState.HIDDEN : EmptyLoadingListTextViewState.EMPTY);
    }

    private void updateLoadingListTextView() {
        updateEmptyListTextView(EmptyLoadingListTextViewState.LOADING);
    }

    private void updateEmptyListTextView(EmptyLoadingListTextViewState state) {
        switch (state) {
            case HIDDEN:
                setAuxTextViewVisible(false);
                break;
            case EMPTY:
                setAuxTextViewVisible(true);
                setAuxTextViewText(emptyListText);
                setAuxTextViewTextColor(emptyListTextColor);
                break;
            case LOADING:
                setAuxTextViewVisible(true);
                setAuxTextViewText(loadingListText);
                setAuxTextViewTextColor(loadingTextColor);
                break;
        }
    }

    private void setAuxTextViewText(String text) {
        if (emptyLoadingListTextView == null) { return; }
        emptyLoadingListTextView.setText(text);
    }

    private void setAuxTextViewTextColor(int color) {
        if (emptyLoadingListTextView == null) { return; }
        emptyLoadingListTextView.setTextColor(color);
    }

    private void setAuxTextViewVisible(boolean visible) {
        if (emptyLoadingListTextView == null) { return; }
        emptyLoadingListTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static class Builder extends EasyRecyclerViewManagerBuilder {
        public Builder(RecyclerView recyclerView, EasyRecyclerAdapter adapter) {
            super(recyclerView, adapter);
        }
    }

}
