/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhatsux.upload.utils;

import com.google.cloud.storage.Blob;
import java.io.InputStream;

/**
 *
 * @author abryn
 */
public class FileDownloadChunks {
    private InputStream file;
    
    private Blob blob;

    /**
     * @return the file
     */
    public InputStream getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(InputStream file) {
        this.file = file;
    }

    /**
     * @return the blob
     */
    public Blob getBlob() {
        return blob;
    }

    /**
     * @param blob the blob to set
     */
    public void setBlob(Blob blob) {
        this.blob = blob;
    }
    
    
}
