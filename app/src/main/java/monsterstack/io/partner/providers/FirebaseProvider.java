package monsterstack.io.partner.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        FirebaseOptions.Builder builder = new FirebaseOptions.Builder()
                .setApplicationId(getContext().getResources().getString(monsterstack.io.api.R.string.firebase_app_id))
                .setApiKey(getContext().getResources().getString(monsterstack.io.api.R.string.firebase_api_key))
                .setDatabaseUrl(getContext().getResources().getString(monsterstack.io.api.R.string.firebase_database_url));
        FirebaseApp.initializeApp(getContext(), builder.build());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
