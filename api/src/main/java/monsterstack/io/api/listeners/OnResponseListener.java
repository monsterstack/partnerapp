package monsterstack.io.api.listeners;

@FunctionalInterface
public interface OnResponseListener<RESPONSE, ERROR> {
    void onResponse(RESPONSE response, ERROR error);
}
