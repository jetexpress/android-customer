package coid.customer.pickupondemand.jet.custom;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.activeandroid.query.Select;

import java.util.List;

import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.model.NotificationPayload;
import coid.customer.pickupondemand.jet.model.db.DBQuery;
import coid.customer.pickupondemand.jet.model.db.ModelLoader;

public abstract class PickupNotificationPagingLoader
{
    private Context mContext;
    private int mPage;
    private int mTotalPage;
    private int mCount;
    private boolean mHasExecuted;
    private boolean mIsExecuting;

    public PickupNotificationPagingLoader(Context context)
    {
        mContext = context;
        mHasExecuted = false;
        mIsExecuting = false;
        mCount = DBQuery.count(NotificationPayload.class);

        long size = AppConfig.DEFAULT_PAGING_SIZE;
        mTotalPage = (int) ((mCount + size - 1)/size);
    }

//    public void execute(LoaderManager loaderManager)
//    {
//        onStart();
//        mIsExecuting = true;
//        if (loaderManager.getLoader(AppConfig.LoaderId.NOTIFICATION_PAYLOAD_LOADER_ID) != null)
//            loaderManager.restartLoader(AppConfig.LoaderId.NOTIFICATION_PAYLOAD_LOADER_ID, null, mNotificationLoader);
//        else
//            loaderManager.initLoader(AppConfig.LoaderId.NOTIFICATION_PAYLOAD_LOADER_ID, null, mNotificationLoader);
//    }

    public void execute()
    {
        mPage = 0;
        this.execute(null);
    }

    public void execute(@Nullable Long lastItemId)
    {
        onStart();
        mPage += 1;
        mIsExecuting = true;
        Utility.Executor.execute(new PagedNotificationPayloadListRunnable(lastItemId)
        {
            @Override
            void run(Long mLastItemId)
            {
                final List<NotificationPayload> notificationPayloadList = NotificationPayload.getPagedNotificationPayloadList(mLastItemId);
                if (mContext instanceof FragmentActivity)
                    ((FragmentActivity) mContext).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            onSuccess(notificationPayloadList);
                            mHasExecuted = true;
                            mIsExecuting = false;
                        }
                    });
            }
        });
    }

    public boolean hasExecuted()
    {
        return mHasExecuted;
    }

    public boolean isExecuting()
    {
        return mIsExecuting;
    }

    public boolean isIdle()
    {
        return !mIsExecuting;
    }

    public int getPage()
    {
        return mPage;
    }

    public boolean isLastPage()
    {
        return mPage >= mTotalPage;
    }

    public LoaderManager.LoaderCallbacks<List<NotificationPayload>> getLoader()
    {
        return mNotificationLoader;
    }

    private LoaderManager.LoaderCallbacks<List<NotificationPayload>> mNotificationLoader = new LoaderManager.LoaderCallbacks<List<NotificationPayload>>()
    {
        @Override
        public Loader<List<NotificationPayload>> onCreateLoader(int id, Bundle args)
        {
            return new ModelLoader<>(mContext,
                    NotificationPayload.class,
                    new Select().from(NotificationPayload.class).orderBy("ID DESC"),
                    false);
        }

        @Override
        public void onLoadFinished(Loader<List<NotificationPayload>> loader, List<NotificationPayload> data)
        {
            if (data.size() > mCount)
            {
                int loop = data.size() - mCount;
                for (int i = loop - 1; i >= 0; i--)
                {
                    onNotificationReceived(data.get(i));
                }
            }

            if (data.size() == 0)
            {
                onAllNotificationCleared();
            }

            mCount = data.size();
        }

        @Override
        public void onLoaderReset(Loader<List<NotificationPayload>> loader)
        {

        }
    };

    public abstract void onStart();
    public abstract void onSuccess(List<NotificationPayload> data);
    public abstract void onNotificationReceived(NotificationPayload notificationPayload);
    public abstract void onAllNotificationCleared();

    private abstract class PagedNotificationPayloadListRunnable implements Runnable
    {
        Long mLastItemId;

        PagedNotificationPayloadListRunnable(@Nullable Long lastItemId)
        {
            mLastItemId = lastItemId;
        }

        @Override
        public void run()
        {
            run(mLastItemId);
        }

        abstract void run(Long mLastItemId);
    }
}
