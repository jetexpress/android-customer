package coid.customer.pickupondemand.jet.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

import coid.customer.pickupondemand.jet.Navigator;
import coid.customer.pickupondemand.jet.R;


public class BaseDialogFragment extends DialogFragment
{
    private ProgressDialog mProgressDialog;
    public Context mContext;
    public View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null)
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected View findViewById(Integer id)
    {
        return mView.findViewById(id);
    }

    protected BaseActivity getBaseActivity()
    {
        return (BaseActivity) getActivity();
    }

    protected Navigator getNavigator()
    {
        return getBaseActivity().getNavigator();
    }

    public void showLoadingDialog()
    {
        mProgressDialog = new ProgressDialog(mContext, R.style.ProgressDialog);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public void showLoadingDialog(String message)
    {
        if (message == null) message = getString(R.string.pod_loading);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void hideLoadingDialog()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.hide();
    }

    protected void showToast(String toastMessage)
    {
        Toast.makeText(mContext, toastMessage, Toast.LENGTH_LONG).show();
    }

}
