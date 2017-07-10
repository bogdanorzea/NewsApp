package com.bogdanorzea.newsapp;

public class News {
    String mSectionId;
    String mPublicationDate;
    String mTitle;
    String mUrl;

    public News(String title, String url, String sectionId, String publicationDate) {
        this.mSectionId = sectionId;
        this.mPublicationDate = publicationDate;
        this.mTitle = title;
        this.mUrl = url;
    }
}
