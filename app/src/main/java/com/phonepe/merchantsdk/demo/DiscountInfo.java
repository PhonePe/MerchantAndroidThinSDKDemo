package com.phonepe.merchantsdk.demo;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DiscountInfo implements Serializable {
    @SerializedName("offediscountIdrId")
    private String discountId;

    @SerializedName("discountDescription")
    private String discountDescription;

    @SerializedName("someInfo")
    private SomeInfo someInfo;

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public void setDiscountDescription(String discountDescription) {
        this.discountDescription = discountDescription;
    }

    public DiscountInfo(String discountId, String discountDescription, String someInfo) {
        this.discountId = discountId;
        this.discountDescription = discountDescription;
        this.someInfo = new SomeInfo(someInfo);
    }

    @Override
    public String toString() {
        return "DiscountInfo{" +
                "discountId='" + discountId + '\'' +
                ", discountDescription='" + discountDescription + '\'' +
                ", someInfo=" + someInfo +
                '}';
    }

    private class SomeInfo implements Serializable{
        @SerializedName("someI")
        private String someI;

        public SomeInfo(String someI) {
            this.someI = someI;
        }

        public void setSomeI(String someI) {
            this.someI = someI;
        }

        @Override
        public String toString() {
            return "SomeInfo{" +
                    "someI='" + someI + '\'' +
                    '}';
        }
    }
}
