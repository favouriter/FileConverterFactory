# FileConverterFactory使用说明
## 我为什么要写这个文件转换
我们在使用retrofit的时候，创建请求参数是不是很繁琐，尤其是在使用@Part的时候不能直接使用原始类型，因为我们使用GsonConverterFactory转换工厂后，所有的类型都加上双引号，比如：@Part("username")String username，我们传个admin，请求就被GsonConverterFactory转成"admin"，
后来找了很多资料发现可以添加转换工厂，在GsonConverterFactory上面添加一个ScalarsConverterFactory转换工厂即可，说不明白了，贴段代码
```java
@Override public Converter<?, RequestBody> requestBodyConverter(Type type,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    if (type == String.class
        || type == boolean.class
        || type == Boolean.class
        || type == byte.class
        || type == Byte.class
        || type == char.class
        || type == Character.class
        || type == double.class
        || type == Double.class
        || type == float.class
        || type == Float.class
        || type == int.class
        || type == Integer.class
        || type == long.class
        || type == Long.class
        || type == short.class
        || type == Short.class) {
      return ScalarRequestBodyConverter.INSTANCE;
    }
    return null;
  }
```
由此我们可以发现，优先级里面能处理的，即时处理，处理不了的，返回NUll然后交给下一个转换器，这样就可以把基本类型给转换了，使用很方面，不用每个参数进行RequestBody创建了
## 但是新的问题来了
基本类型支持，但是他并不支持File直接上传，我们理想中的参数是这样的@Part("image")File image,但是这个没人处理，那么我们就可以通过ScalarsConverterFactory源码自己写一个File转换器，其实很简单，首先我们也创建一个转换器，继承Converter.Factory，又耍赖，直接贴代码
```java
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
        // 来不及了，文件下载就不写了
        return null;
    }
}
```
单一个转换器是不行地，转换器作用就是转换参数，转换器是拦截作用，处理还要有一个请求参数处理，再贴一次代码，最后一次了
```java
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
```
大功告成，这样我们就可以完美的使用所有请求参数直接转换了，String、int、boolean、File，至于其余的对象通过GSON转成json对象了，这样请求是不是贼爽
## 使用
使用很简单了，就这两个类copy进项目里面就行了，然后client这样设置，对不起这次真的是最后一次
```java
if(mRetrofit ==null ){
	    mRetrofit = new Retrofit.Builder()
	    .baseUrl(getBaseUrl())
	    .addConverterFactory(ScalarsConverterFactory.create())
	    .addConverterFactory(FileConverterFactory.create())
	    .addConverterFactory(GsonConverterFactory.create())
	    .client(initOkHttpClient())
	    .build();
	}
```