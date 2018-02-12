package coid.customer.pickupondemand.jet.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.config.AppConfig;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseItemSelectDialogFragment <T extends IBaseItemSelectModel> extends BaseHasBasicLayoutDialogFragment
{
    public static final String SELECTED_ITEM_CODE_ARGS_PARAM = "selectedItemCodeParam";
    public static final String SELECTED_ITEM_DESCRIPTION_ARGS_PARAM = "selectedItemDescriptionParam";

    private RelativeLayout rl_filter_container;
    private EditText et_filter;
    private TextView tv_title;
    private ListView list_view_item_select;
    private ProgressBar progress_bar_filter;

    private List<T> mDataList;
    private BaseItemSelectListAdapter<T> mAdapter;
    private Handler mHandler;
    private String mKeyword;
    private long mFilterDelay = 2000;

    public BaseItemSelectDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.JETBaseDialog);
        mDataList = new ArrayList<>();
        mAdapter = new BaseItemSelectListAdapter<>(mContext, mDataList);
        mHandler = new Handler();
        mKeyword = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.base_dialog_fragment_item_select, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setValue();
        setEvent();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null && d.getWindow() != null)
        {
            d.getWindow().setGravity(Gravity.CENTER);
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            d.setCancelable(true);
            d.setCanceledOnTouchOutside(true);
        }
    }

    @Override
    public void onDestroy() {
        Utility.UI.hideKeyboard(mView);

        if (mHandler != null)
        {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }

    @Override
    protected View getBaseContentLayout()
    {
        return list_view_item_select;
    }

    private void setView()
    {
        rl_filter_container = (RelativeLayout) findViewById(R.id.rl_filter_container);
        progress_bar_filter = (ProgressBar) findViewById(R.id.progress_bar_filter);
        et_filter = (EditText) findViewById(R.id.et_filter);
        tv_title = (TextView) findViewById(R.id.tv_title);
        list_view_item_select = (ListView) findViewById(R.id.list_view_item_select);
    }

    private void setValue()
    {
        tv_title.setText(getTitle());
        list_view_item_select.setAdapter(mAdapter);
    }

    private void setEvent()
    {
        list_view_item_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                T model =  mDataList.get(position);
                Intent intent = new Intent();
                intent.putExtra(SELECTED_ITEM_CODE_ARGS_PARAM, model.getItemSelectCode());
                intent.putExtra(SELECTED_ITEM_DESCRIPTION_ARGS_PARAM, model.getItemSelectDescription());
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });
        et_filter.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                mKeyword = s.toString();
                mHandler.postDelayed(filterRunnable, mFilterDelay);
            }
        });
    }

    private Runnable filterRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            onRunFilter(mKeyword);
        }
    };

    protected void updateData(List<T> dataList)
    {
        mAdapter.updateData(dataList);
        showContent(dataList.size() > 0);
    }


    protected void clearData()
    {
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
        showContent();
    }

    protected void showFilter()
    {
        rl_filter_container.setVisibility(View.VISIBLE);
    }

    protected void hideFilter()
    {
        rl_filter_container.setVisibility(View.GONE);
    }

    protected void setFilterDelay(long delayInMillis)
    {
        mFilterDelay = delayInMillis;
    }

    protected void showFilterProgressBar()
    {
        progress_bar_filter.setVisibility(View.VISIBLE);
    }

    protected void hideFilterProgressBar()
    {
        progress_bar_filter.setVisibility(View.GONE);
    }

    protected void onRunFilter(String keyword){}
    protected abstract String getTitle();
}
