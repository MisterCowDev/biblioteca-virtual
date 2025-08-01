package biblioteca_virtual.service;

public interface IDataConverter {
    <T> T getData(String json, Class<T> tClass);
}
