package com.github.favouriter.fileconverterfactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author favouriter
 * @date 2018-10-27
 */

public final class FileConverterFactory extends Converter.Factory {

    public static FileConverterFactory create() {
        return new FileConverterFactory();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        // TODO Auto-generated method stub
        if (type == File.class) {
            return FileRequestBodyConverter.INSTANCE;
        }
        return null;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        // if (type == File.class) {
        // return FileResponseBodyConverter.INSTANCE;
        // }
        return null;
    }
}
