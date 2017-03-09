package com.example.android.news;

/**
 * {@link Result} represents a news.
 */
public class Result {
    /** Result id */
    private String mId;

    /** Result section name */
    private String mSectionName;

    /** Result web title */
    private String mWebTitle;

    /** Result web publication date */
    private String mWebPublicationDate;

    /** Result web url **/
    private String mWebUrl;

    /**
     * Create a new Result object.
     *
     * @param id is the result id
     * @param webTitle is the result web title
     * @param sectionName is the result section name
     * @param webPublicationDate is the result date of publication
     * @param webUrl is the result web url
     */
    public Result(String id,  String webTitle, String sectionName, String webPublicationDate, String webUrl) {
        mId = id;
        mWebTitle = webTitle;
        mSectionName = sectionName;
        mWebPublicationDate = webPublicationDate;
        mWebUrl = webUrl;
    }

    /**
     * Get the result id.
     */
    public String getId() {
        return mId;
    }

    /**
     * Get the result section name.
     */
    public String getSectionName() {
        return mSectionName;
    }

    /**
     * Get the result web title.
     */
    public String getWebTitle() {
        return mWebTitle;
    }

    /**
     * Get the result web publication date.
     */
    public String getWebPublicationDate() {
        return mWebPublicationDate;
    }

    /**
     * Get the result web url.
     */
    public String getWebUrl() {
        return mWebUrl;
    }
}