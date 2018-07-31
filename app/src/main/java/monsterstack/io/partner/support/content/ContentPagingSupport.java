package monsterstack.io.partner.support.content;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;


public abstract class ContentPagingSupport<T> {

    public Builder builder() {
        return new Builder();
    }

    public abstract T map(Cursor cursor);

    public class Builder {
        private Uri contentUri;
        private String selection;
        private String[] projection;
        private String[] selectionArgs;
        private String sortOrder;
        private ContentResolver contentResolver;

        Builder() {}

        public Builder contentResolver(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
            return this;
        }

        public Builder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Builder projection(String[] projection) {
            this.projection = projection;
            return this;
        }

        public Builder selection(String selection) {
            this.selection = selection;
            return this;
        }

        public Builder selectionArgs(String[] selectionArgs) {
            this.selectionArgs = selectionArgs;
            return this;
        }

        public Builder contentUri(Uri contentUri) {
            this.contentUri = contentUri;
            return this;
        }

        public ContentQuery build() {
            return new ContentQuery(contentResolver,
                    contentUri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder);
        }
    }

    @AllArgsConstructor
    public class ContentQuery {
        private ContentResolver contentResolver;
        private Uri contentUri;
        private String[] projection;
        private String selection;
        private String[] selectionArgs;
        private String sortOrder;

        ContentQuery() {}

        public List<T> query() {
            List<T> result = new ArrayList<>();
            Cursor cursor = contentResolver.query(
                    contentUri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder);

            if (null != cursor) {
                while (cursor.moveToNext()) {
                    result.add(mapNext(cursor));
                }
                cursor.close();
            }
            return result;
        }

        public T mapNext(Cursor cursor) {
            return ContentPagingSupport.this.map(cursor);
        }
    }
}
