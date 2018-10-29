package com.github.favouriter.fileconverterfactory;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * @author favouriter
 * @date 2018-10-27
 */
public class FileRequestBodyConverter<T> implements Converter<T, RequestBody> {

    static final FileRequestBodyConverter<Object> INSTANCE = new FileRequestBodyConverter<>();

    private static final MediaType MEDIA_TYPE = MediaType.parse("multipart/form-data; charset=UTF-8");

    private FileRequestBodyConverter() {
    }

    @Override
    public RequestBody convert(T file) throws IOException {
        // TODO Auto-generated method stub
        return RequestBody.create(MEDIA_TYPE, (File) file);
    }

}
