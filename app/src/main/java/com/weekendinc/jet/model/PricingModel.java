package com.weekendinc.jet.model;

import com.weekendinc.jet.model.pojo.Location;
import com.weekendinc.jet.model.pojo.Price;
import com.weekendinc.jet.network.APIJet;

import java.util.List;

import rx.Observable;

public class PricingModel {
    static private PricingModel instance;

    static public PricingModel getInstance() {
        if (instance == null) {
            instance = new PricingModel();
        }
        return instance;
    }

    public Observable<List<Location>> getLocations(String keyword) {
        return APIJet.getInstance().submitLocations(keyword);
    }

    public Observable<List<Price>> getPrice(String originValue, String destinationValue, int weight) {
        return APIJet.getInstance().submitPrice(originValue, destinationValue, weight);
    }
}
