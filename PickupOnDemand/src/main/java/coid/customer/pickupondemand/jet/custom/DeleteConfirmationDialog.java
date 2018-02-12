package coid.customer.pickupondemand.jet.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import coid.customer.pickupondemand.jet.R;

public abstract class DeleteConfirmationDialog extends AlertDialog
{
    private String mTitle;
    private String mMessage;

    public DeleteConfirmationDialog(Context context, String title, String message)
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
                dismiss();
            }
        });
        setTitle(mTitle);
        setMessage(mMessage);
    }

    public abstract void onOKClicked();
}
