package com.weekendinc.jet.model;

import com.weekendinc.jet.model.pojo.ConnoteTrack;
import com.weekendinc.jet.model.pojo.PinWaybill;
import com.weekendinc.jet.model.pojo.UnpinWaybill;
import com.weekendinc.jet.model.pojo.Waybill;
import com.weekendinc.jet.network.APIJet;

import java.util.List;

import rx.Observable;

public class TrackingModel {
    static private TrackingModel instance;

    static public TrackingModel getInstance() {
        if (instance == null)
            instance = new TrackingModel();
        return instance;
    }

    public Observable<List<Waybill>> getWayBills(String accessToken, String... awbNumbers) {
        return APIJet.getInstance().submitWayBills(accessToken, awbNumbers);
    }

    public Observable<List<List<ConnoteTrack>>> getWayBillTracks(String awbNumber, String connoteCode) {
        return APIJet.getInstance().submitWayBillTracks(awbNumber, connoteCode);
    }

    public Observable<PinWaybill> pinWaybill(String accessToken, String awbNumber) {
        return APIJet.getInstance().doPinWaybill(accessToken, awbNumber);
    }

    public Observable<UnpinWaybill> unpinWaybill(String accessToken, String awbNumber) {
        return APIJet.getInstance().doUnpinWaybill(accessToken, awbNumber);
    }

    public Observable<List<Waybill>> getTrackingList(String accessToken, boolean isFinished, long size, long page) {
        return APIJet.getInstance().getTrackingList(accessToken, isFinished, size, page);
    }
}
