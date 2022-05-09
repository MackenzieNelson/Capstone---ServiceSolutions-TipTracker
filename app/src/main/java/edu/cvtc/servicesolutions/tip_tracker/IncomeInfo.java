package edu.cvtc.servicesolutions.tip_tracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class IncomeInfo implements Parcelable {

    private double iHourlyWage;
    private double iHoursWorked;
    private double iCashTip;
    private double iCreditTip;
    private String iDate;
    private int iId;

    protected IncomeInfo(double hourlyWage, double hoursWorked, double cashTip, double creditTip, String date) {
        iHourlyWage = hourlyWage;
        iHoursWorked = hoursWorked;
        iCashTip = cashTip;
        iCreditTip = creditTip;
        iDate = date;
    }

    protected IncomeInfo(Parcel parcel) {
        iHourlyWage = parcel.readDouble();
        iHoursWorked = parcel.readDouble();
        iCashTip = parcel.readDouble();
        iCreditTip = parcel.readDouble();
        iDate = parcel.readString();
    }

    public IncomeInfo(int id, double hourlyWage, double hoursWorked, double cashTip, double creditTip, String date) {
        iId = id;
        iHourlyWage = hourlyWage;
        iHoursWorked = hoursWorked;
        iCashTip = cashTip;
        iCreditTip = creditTip;
        iDate = date;
    }


    public double getHourlyWage() {
        return iHourlyWage;
    }

    public void setHourlyWage(double hourlyWage) {
        this.iHourlyWage = hourlyWage;
    }

    public double getHoursWorked() {
        return iHoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.iHoursWorked = hoursWorked;
    }

    public double getCashTip() {
        return iCashTip;
    }

    public void setCashTip(double cashTip) {
        this.iCashTip = cashTip;
    }

    public double getCreditTip() {
        return iCreditTip;
    }

    public void setCreditTip(double creditTip) {
        this.iCreditTip = creditTip;
    }

    public String getDate() {
        return iDate;
    }

    public void setDate(String date) {
        this.iDate = date;
    }

    @Override
    public int describeContents () {
        return 0;
    }


    private String getCompareKey() { return iHourlyWage + "|" + iHoursWorked + "|" + iCashTip + "|" + iCreditTip + "|" + iDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncomeInfo that = (IncomeInfo) o;
        return getCompareKey().equals(that.getCompareKey());
    }

    @Override
    public int hashCode() { return getCompareKey().hashCode(); }

    @Override
    public String toString() { return getCompareKey(); }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(iHourlyWage);
        parcel.writeDouble(iHoursWorked);
        parcel.writeDouble(iCashTip);
        parcel.writeDouble(iCreditTip);
        parcel.writeString(iDate);
    }

    public static final Creator<IncomeInfo> CREATOR = new Creator<IncomeInfo>() {

        @Override
        public IncomeInfo createFromParcel (Parcel in){
            return new IncomeInfo(in);
        }

        @Override
        public IncomeInfo[] newArray ( int size){
            return new IncomeInfo[size];
        }
    };
}
