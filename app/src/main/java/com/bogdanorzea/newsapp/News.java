package com.bogdanorzea.newsapp;

import java.util.ArrayList;
import java.util.List;

class News {
    private String mId;
    private String mSectionName;
    private String mHeadline;
    private String mTrailText;
    private String mThumbnail;
    private String mPublicationDate;
    private String mUrl;
    private List<String> mContributors = new ArrayList<>();

    News(String id, String url, String publicationDate) {
        this.mId = id;
        this.mPublicationDate = publicationDate;
        this.mUrl = url;
    }

    public void setThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public void setSectionName(String sectionName) {
        this.mSectionName = sectionName;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getContributors() {
        String contributors = "";
        int size = mContributors.size();

        if (size >= 1) {
            contributors = mContributors.get(0);
            for (int i = 1; i < size; i++) {
                contributors = contributors + ", " + mContributors.get(i);
            }
        }

        return contributors;
    }

    public void setContributors(List<String> contributors) {
        this.mContributors = contributors;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public void setHeadline(String headline) {
        this.mHeadline = headline;
    }

    public String getTrailText() {
        return mTrailText;
    }

    public void setTrailText(String trailText) {
        this.mTrailText = trailText;
    }
}
