package xh.org.video.android_ppjoke.model;

import java.util.List;

public class BottomBar {
    private String activeColor;
    private String inActiveColor;
    private List<Tab> tabs;
    private int selectTab;

    public String getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(String activeColor) {
        this.activeColor = activeColor;
    }

    public String getInActiveColor() {
        return inActiveColor;
    }

    public void setInActiveColor(String inActiveColor) {
        this.inActiveColor = inActiveColor;
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public void setTabs(List<Tab> tabs) {
        this.tabs = tabs;
    }

    public int getSelectTab() {
        return selectTab;
    }

    public void setSelectTab(int selectTab) {
        this.selectTab = selectTab;
    }
}
