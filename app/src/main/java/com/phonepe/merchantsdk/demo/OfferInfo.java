package com.phonepe.merchantsdk.demo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OfferInfo implements Serializable {
    @SerializedName("offerId")
    private String offerId;

    @SerializedName("offerDescription")
    private String offerDescription;

    public OfferInfo(String offerId, String offerDescription) {
        this.offerId = offerId;
        this.offerDescription = offerDescription;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    @Override
    public String toString() {
        return "OfferInfo{" +
                "offerId='" + offerId + '\'' +
                ", offerDescription='" + offerDescription + '\'' +
                '}';
    }
}
