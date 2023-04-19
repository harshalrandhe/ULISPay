package com.ulisfintech.telrpay.ui.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class MerchantDetails implements Parcelable {

    @Expose
    private String theme;
    @Expose
    private String icon;
    @Expose
    private String logo;
    @Expose
    private String use_logo;
    @Expose
    private String we_accept_image;
    @Expose
    private String brand_color;
    @Expose
    private String accent_color;
    @Expose
    private String branding_language;
    @Expose
    private String merchant_name;
    @Expose
    private CompanyDetails company_details;

    public MerchantDetails() {
    }

    protected MerchantDetails(Parcel in) {
        theme = in.readString();
        icon = in.readString();
        logo = in.readString();
        use_logo = in.readString();
        we_accept_image = in.readString();
        brand_color = in.readString();
        accent_color = in.readString();
        branding_language = in.readString();
        merchant_name = in.readString();
        company_details = in.readParcelable(CompanyDetails.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(theme);
        dest.writeString(icon);
        dest.writeString(logo);
        dest.writeString(use_logo);
        dest.writeString(we_accept_image);
        dest.writeString(brand_color);
        dest.writeString(accent_color);
        dest.writeString(branding_language);
        dest.writeString(merchant_name);
        dest.writeParcelable(company_details, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MerchantDetails> CREATOR = new Creator<MerchantDetails>() {
        @Override
        public MerchantDetails createFromParcel(Parcel in) {
            return new MerchantDetails(in);
        }

        @Override
        public MerchantDetails[] newArray(int size) {
            return new MerchantDetails[size];
        }
    };

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUse_logo() {
        return use_logo;
    }

    public void setUse_logo(String use_logo) {
        this.use_logo = use_logo;
    }

    public String getWe_accept_image() {
        return we_accept_image;
    }

    public void setWe_accept_image(String we_accept_image) {
        this.we_accept_image = we_accept_image;
    }

    public String getBrand_color() {
        return brand_color;
    }

    public void setBrand_color(String brand_color) {
        this.brand_color = brand_color;
    }

    public String getAccent_color() {
        return accent_color;
    }

    public void setAccent_color(String accent_color) {
        this.accent_color = accent_color;
    }

    public String getBranding_language() {
        return branding_language;
    }

    public void setBranding_language(String branding_language) {
        this.branding_language = branding_language;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public CompanyDetails getCompany_details() {
        return company_details;
    }

    public void setCompany_details(CompanyDetails company_details) {
        this.company_details = company_details;
    }
}
