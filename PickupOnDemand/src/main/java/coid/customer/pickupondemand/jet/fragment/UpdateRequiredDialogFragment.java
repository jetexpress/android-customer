package coid.customer.pickupondemand.jet.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.base.BaseDialogFragment;

public class UpdateRequiredDialogFragment extends BaseDialogFragment
{
    private static final String IS_FORCE_UPDATE_ARGS_PARAM = "IsForceUpdateParam";
    private static final String MESSAGE_ARGS_PARAM = "MessageParam";

    private TextView tv_title, tv_message;
    private Button btn_ok, btn_cancel;

    private boolean mIsForceUpdate;
    private String mMessage;

    public UpdateRequiredDialogFragment()
    {

    }

    public static UpdateRequiredDialogFragment newInstance(boolean isForceUpdate, String message)
    {
        UpdateRequiredDialogFragment dialogFragment = new UpdateRequiredDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_FORCE_UPDATE_ARGS_PARAM, isForceUpdate);
        args.putString(MESSAGE_ARGS_PARAM, message);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog);
        if (getArguments() != null)
        {
            mIsForceUpdate = getArguments().getBoolean(IS_FORCE_UPDATE_ARGS_PARAM);
            mMessage = getArguments().getString(MESSAGE_ARGS_PARAM);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_required_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setView();
        setValue();
        setEvent();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog d = getDialog();
        if (d != null && d.getWindow() != null)
        {
            d.getWindow().setGravity(Gravity.CENTER);
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            d.setCancelable(!mIsForceUpdate);
            d.setCanceledOnTouchOutside(!mIsForceUpdate);
        }
    }

    private void setView()
    {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_message = (TextView) findViewById(R.id.tv_message);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    private void setValue()
    {
        tv_message.setText(mMessage);
    }

    private void setEvent()
    {
        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+ mContext.getPackageName()));
                startActivity(intent);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mIsForceUpdate)
                {
                    getActivity().finish();
                    System.exit(0);
                }
                else
                    dismiss();
            }
        });
    }
}
