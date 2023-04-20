package com.nhatsux.upload.utils;

import java.io.InputStream;
import javax.ws.rs.FormParam;

public class FileUploadFormChunks {

    @FormParam("file")
    private byte[] fileData;

    @FormParam("index")
    private int index;

    @FormParam("name")
    private String name;

    @FormParam("totalChunks")
    private int totalChunks;

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the totalChunks
     */
    public int getTotalChunks() {
        return totalChunks;
    }

    /**
     * @param totalChunks the totalChunks to set
     */
    public void setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
    }

    /**
     * @return the fileData
     */
    public byte[] getFileData() {
        return fileData;
    }

    /**
     * @param fileData the fileData to set
     */
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "{"
                + "fileName:" + this.name + ","
                + "index:" + this.index + ","
                + "file:" + this.fileData + ","
                + "}";
    }

}
