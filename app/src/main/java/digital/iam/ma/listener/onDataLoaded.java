package digital.iam.ma.listener;

public interface onDataLoaded<T> {

    void onResponse(T data);

    void onFailure(String error);

    void onInvalidToken();
}
