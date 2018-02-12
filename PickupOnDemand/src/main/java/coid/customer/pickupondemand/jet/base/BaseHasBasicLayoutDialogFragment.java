package coid.customer.pickupondemand.jet.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;

public abstract class BaseHasBasicLayoutDialogFragment extends BaseDialogFragment
{
    public ProgressBar base_progress_bar;
    public TextView base_text_view_no_data, base_text_view_retry_info;
    public Button base_btn_retry;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        base_progress_bar = (ProgressBar) view.findViewById(R.id.base_progress_bar);
        base_text_view_no_data = (TextView) view.findViewById(R.id.base_text_view_no_data);
        base_text_view_retry_info = (TextView) view.findViewById(R.id.base_text_view_retry_info);
        base_btn_retry = (Button) view.findViewById(R.id.base_btn_retry);

        if (base_btn_retry == null)
            return;

        base_btn_retry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onRetry();
            }
        });
    }

    protected void showContent()
    {
        if (hasBasicLayout())
        {
            getBaseContentLayout().setVisibility(View.VISIBLE);
            base_progress_bar.setVisibility(View.GONE);
            base_text_view_no_data.setVisibility(View.GONE);
            base_text_view_retry_info.setVisibility(View.GONE);
            base_btn_retry.setVisibility(View.GONE);
        }
    }

    protected void showProgressBar()
    {
        if (hasBasicLayout())
        {
            getBaseContentLayout().setVisibility(View.GONE);
            base_progress_bar.setVisibility(View.VISIBLE);
            base_text_view_no_data.setVisibility(View.GONE);
            base_text_view_retry_info.setVisibility(View.GONE);
            base_btn_retry.setVisibility(View.GONE);
        }
    }

    protected void showNoData()
    {
        if (hasBasicLayout())
        {
            getBaseContentLayout().setVisibility(View.GONE);
            base_progress_bar.setVisibility(View.GONE);
            base_text_view_no_data.setVisibility(View.VISIBLE);
            base_text_view_retry_info.setVisibility(View.GONE);
            base_btn_retry.setVisibility(View.GONE);
        }
    }

    protected void showRetry(Integer stringResourceId)
    {
        if (hasBasicLayout())
        {
            getBaseContentLayout().setVisibility(View.GONE);
            base_progress_bar.setVisibility(View.GONE);
            base_text_view_no_data.setVisibility(View.GONE);
            base_text_view_retry_info.setVisibility(View.VISIBLE);
            base_text_view_retry_info.setText(Utility.Message.get(stringResourceId));
            base_btn_retry.setVisibility(View.VISIBLE);
        }
    }

    protected void showRetry(String retryMessageInfo)
    {
        if (hasBasicLayout())
        {
            getBaseContentLayout().setVisibility(View.GONE);
            base_progress_bar.setVisibility(View.GONE);
            base_text_view_no_data.setVisibility(View.GONE);
            base_text_view_retry_info.setVisibility(View.VISIBLE);
            base_text_view_retry_info.setText(retryMessageInfo);
            base_btn_retry.setVisibility(View.VISIBLE);
        }
    }

    protected void showContent(Boolean hasData)
    {
        if (hasData)
            showContent();
        else
            showNoData();
    }

    private Boolean hasBasicLayout()
    {
        return getBaseContentLayout() != null
                && base_progress_bar != null
                && base_text_view_no_data != null
                && base_text_view_retry_info != null
                && base_btn_retry != null;
    }

    protected void onRetry(){}

    protected abstract View getBaseContentLayout();

}
