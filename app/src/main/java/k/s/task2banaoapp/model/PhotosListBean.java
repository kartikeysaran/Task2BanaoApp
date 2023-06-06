package k.s.task2banaoapp.model;

import java.util.ArrayList;

public class PhotosListBean {
    private int page;
    private int pages;
    private int perpage;
    private int total;
    private ArrayList<PhotosBean> photo;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<PhotosBean> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<PhotosBean> photo) {
        this.photo = photo;
    }
}
