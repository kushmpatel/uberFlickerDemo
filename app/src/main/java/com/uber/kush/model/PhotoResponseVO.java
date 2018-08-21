package com.uber.kush.model;

import java.util.ArrayList;
import java.util.List;

public class PhotoResponseVO {

    private int page;
    private int pages;
    private int perpage;
    private String total;
    private List<PhotoVO> photo = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<PhotoVO> getPhoto() {
        return photo;
    }

    public void setPhoto(List<PhotoVO> photo) {
        this.photo = photo;
    }
}
