package com.nhatsux.upload.controllers;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.nhatsux.upload.utils.FileDownloadChunks;
import com.nhatsux.upload.utils.FileUploadFormChunks;
import com.nhatsux.upload.utils.FilesUtils;
import static com.nhatsux.upload.utils.FilesUtils.createCredentials;
import java.io.InputStream;
import java.nio.channels.Channels;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

@Path("files")
public class FilesControllers {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@MultipartForm FileUploadFormChunks form) {
        System.out.println(form.toString());
        try {
            FilesUtils.uploadChunks(form);
            return Response.status(Response.Status.CREATED).header("Access-Control-Allow-Origin", "*").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.ACCEPTED).header("Access-Control-Allow-Origin", "*").entity(e).build();
        }
    }

    @GET
    @Path("/download/{fileName}")
    public Response downloadFile(@PathParam("fileName") String fileName, @HeaderParam("Range") String rangeHeader) {

        try {
            FileDownloadChunks file = FilesUtils.buildInputStream(fileName);
            InputStream inputStream = file.getFile();
            if (rangeHeader != null) {
                String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
                long start = Long.parseLong(ranges[0]);
                long end = file.getBlob().getSize() - 1;
                if (ranges.length == 2) {
                    end = Long.parseLong(ranges[1]);
                }
                inputStream.skip(start);
                long contentLength = end - start + 1;
                // Set up the response
                Response.ResponseBuilder responseBuilder = Response.ok(inputStream);
                responseBuilder.header("Content-Type", file.getBlob().getContentType());
                responseBuilder.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                responseBuilder.header("Content-Range", "bytes " + start + "-" + end + "/" + file.getBlob().getSize());
                responseBuilder.header("Accept-Ranges", "bytes");
                responseBuilder.header("Content-Length", contentLength);

                return responseBuilder.build();

            } else {
                Response.ResponseBuilder responseBuilder = Response.ok(inputStream);
                responseBuilder.header("Content-Type", file.getBlob().getContentType());
                responseBuilder.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                responseBuilder.header("Content-Length", file.getBlob().getSize());

                return responseBuilder.build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

}
