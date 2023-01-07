package models;

public class ImageUploadItem {
    private String fileName;

    public ImageUploadItem() {
    }

    public ImageUploadItem(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
