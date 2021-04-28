package com.walker.ui.group.recyclerview.slidecard;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.walker.core.base.mvc.BaseFragment;
import com.walker.ui.R;
import com.walker.ui.group.recyclerview.adapter.UniversalAdapter;
import com.walker.ui.group.recyclerview.adapter.ViewHolder;

import java.util.List;

public class SlideCardFragment extends BaseFragment {

    public static final String KEY_ID = "key_ui_slide_card_fragment";

    private RecyclerView rv;
    private UniversalAdapter<SlideCardBean> adapter;
    private List<SlideCardBean> mDatas;

    public static Fragment getInstance() {
        Fragment fragment = new SlideCardFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDatas = SlideCardBean.initDatas();
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        rv = baseView.findViewById(R.id.rv);
        rv.setLayoutManager(new SlideCardLayoutManager());
        adapter = new UniversalAdapter<SlideCardBean>(getHoldContext(), mDatas, R.layout.item_ui_slide_card) {

            @Override
            public void convert(ViewHolder viewHolder, SlideCardBean slideCardBean) {
                viewHolder.setText(R.id.tvName, slideCardBean.getName());
                viewHolder.setText(R.id.tvPrecent, slideCardBean.getPostition() + "/" + mDatas.size());
                Glide.with(getHoldContext())
                        .load(slideCardBean.getUrl())
                        .into((ImageView) viewHolder.getView(R.id.iv));
            }
        };
        rv.setAdapter(adapter);
        // 初始化数据
        CardConfig.initConfig(getHoldContext());
        SlideCallback slideCallback = new SlideCallback(rv, adapter, mDatas);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(slideCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ui_slide_card;
    }
}
