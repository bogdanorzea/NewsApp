package com.bogdanorzea.newsapp;

class News {
    private String mSectionId;
    private String mPublicationDate;
    private String mTitle;
    private String mUrl;

    News(String title, String url, String sectionId, String publicationDate) {
        this.mSectionId = sectionId;
        this.mPublicationDate = publicationDate;
        this.mTitle = title;
        this.mUrl = url;
    }

    public String getSectionId() {
        return mSectionId;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }
}
