package coid.customer.pickupondemand.jet.custom;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import coid.customer.pickupondemand.jet.R;

public abstract class ConfirmationDialog extends AlertDialog
{
    private String mTitle;
    private String mMessage;

    public ConfirmationDialog(Context context, String title, String message)
    {
        super(context);
        mTitle = title;
        mMessage = message;
        initialize(context);
    }

    private void initialize(Context context)
    {
        setButton(BUTTON_POSITIVE, context.getString(R.string.pod_yes), new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                onOKClicked();
            }
        });
        setButton(BUTTON_NEGATIVE, context.getString(R.string.pod_cancel), new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                onCancelClicked();
            }
        });
        setTitle(mTitle);
        setMessage(mMessage);
//        setOnShowListener(new DialogInterface.OnShowListener()
//        {
//            @Override
//            public void onShow(DialogInterface dialog)
//            {
//                Button buttonPositive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
//                buttonPositive.setTextColor(Color.BLACK);
//                Button buttonNegative = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
//                buttonNegative.setTextColor(Color.BLACK);
//            }
//        });
    }

    public void onCancelClicked()
    {
        dismiss();
    }
    public abstract void onOKClicked();
}