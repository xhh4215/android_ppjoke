package xh.org.video.android_ppjoke.model;


public class Tab {
    private boolean enable;
    private int index;
    private String pageUrl;
    private int size;
    private String tintColor;
    private String title;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTintColor() {
        return tintColor;
    }

    public void setTintColor(String tintColor) {
        this.tintColor = tintColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
