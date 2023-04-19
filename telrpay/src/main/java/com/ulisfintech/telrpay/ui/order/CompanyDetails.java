package com.ulisfintech.telrpay.ui.order;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

public class CompanyDetails implements Parcelable {

    @Expose
    private String fav_icon;
    @Expose
    private String logo;
    @Expose
    private String letter_head;
    @Expose
    private String footer_banner;
    @Expose
    private String title;

    public CompanyDetails() {
    }

    protected CompanyDetails(Parcel in) {
        fav_icon = in.readString();
        logo = in.readString();
        letter_head = in.readString();
        footer_banner = in.readString();
        title = in.readString();
    }

    public static final Creator<CompanyDetails> CREATOR = new Creator<CompanyDetails>() {
        @Override
        public CompanyDetails createFromParcel(Parcel in) {
            return new CompanyDetails(in);
        }

        @Override
        public CompanyDetails[] newArray(int size) {
            return new CompanyDetails[size];
        }
    };

    public String getFav_icon() {
        return fav_icon;
    }

    public void setFav_icon(String fav_icon) {
        this.fav_icon = fav_icon;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLetter_head() {
        return letter_head;
    }

    public void setLetter_head(String letter_head) {
        this.letter_head = letter_head;
    }

    public String getFooter_banner() {
        return footer_banner;
    }

    public void setFooter_banner(String footer_banner) {
        this.footer_banner = footer_banner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(fav_icon);
        dest.writeString(logo);
        dest.writeString(letter_head);
        dest.writeString(footer_banner);
        dest.writeString(title);
    }
}
