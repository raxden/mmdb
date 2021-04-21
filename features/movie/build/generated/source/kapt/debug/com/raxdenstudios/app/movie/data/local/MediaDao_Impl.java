package com.raxdenstudios.app.movie.data.local;

import android.database.Cursor;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.raxdenstudios.app.movie.data.local.model.MediaEntity;
import com.raxdenstudios.app.movie.data.local.model.PictureEntity;
import com.raxdenstudios.app.movie.data.local.model.SizeEntity;
import com.raxdenstudios.app.movie.data.local.model.VoteEntity;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MediaDao_Impl implements MediaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MediaEntity> __insertionAdapterOfMovieEntity;

  public MediaDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMovieEntity = new EntityInsertionAdapter<MediaEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `movie` (`id`,`title`,`release`,`watch_list`,`backdrop_thumbnail_url`,`backdrop_thumbnail_type`,`backdrop_original_url`,`backdrop_original_type`,`poster_thumbnail_url`,`poster_thumbnail_type`,`poster_original_url`,`poster_original_type`,`vote_average`,`vote_count`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MediaEntity value) {
        stmt.bindLong(1, value.getId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        stmt.bindLong(3, value.getRelease());
        final int _tmp;
        _tmp = value.getWatchList() ? 1 : 0;
        stmt.bindLong(4, _tmp);
        final PictureEntity _tmpBackdrop = value.getBackdrop();
        if(_tmpBackdrop != null) {
          final SizeEntity _tmpThumbnail = _tmpBackdrop.getThumbnail();
          if(_tmpThumbnail != null) {
            if (_tmpThumbnail.getUrl() == null) {
              stmt.bindNull(5);
            } else {
              stmt.bindString(5, _tmpThumbnail.getUrl());
            }
            if (_tmpThumbnail.getType() == null) {
              stmt.bindNull(6);
            } else {
              stmt.bindString(6, _tmpThumbnail.getType());
            }
          } else {
            stmt.bindNull(5);
            stmt.bindNull(6);
          }
          final SizeEntity _tmpOriginal = _tmpBackdrop.getOriginal();
          if(_tmpOriginal != null) {
            if (_tmpOriginal.getUrl() == null) {
              stmt.bindNull(7);
            } else {
              stmt.bindString(7, _tmpOriginal.getUrl());
            }
            if (_tmpOriginal.getType() == null) {
              stmt.bindNull(8);
            } else {
              stmt.bindString(8, _tmpOriginal.getType());
            }
          } else {
            stmt.bindNull(7);
            stmt.bindNull(8);
          }
        } else {
          stmt.bindNull(5);
          stmt.bindNull(6);
          stmt.bindNull(7);
          stmt.bindNull(8);
        }
        final PictureEntity _tmpPoster = value.getPoster();
        if(_tmpPoster != null) {
          final SizeEntity _tmpThumbnail_1 = _tmpPoster.getThumbnail();
          if(_tmpThumbnail_1 != null) {
            if (_tmpThumbnail_1.getUrl() == null) {
              stmt.bindNull(9);
            } else {
              stmt.bindString(9, _tmpThumbnail_1.getUrl());
            }
            if (_tmpThumbnail_1.getType() == null) {
              stmt.bindNull(10);
            } else {
              stmt.bindString(10, _tmpThumbnail_1.getType());
            }
          } else {
            stmt.bindNull(9);
            stmt.bindNull(10);
          }
          final SizeEntity _tmpOriginal_1 = _tmpPoster.getOriginal();
          if(_tmpOriginal_1 != null) {
            if (_tmpOriginal_1.getUrl() == null) {
              stmt.bindNull(11);
            } else {
              stmt.bindString(11, _tmpOriginal_1.getUrl());
            }
            if (_tmpOriginal_1.getType() == null) {
              stmt.bindNull(12);
            } else {
              stmt.bindString(12, _tmpOriginal_1.getType());
            }
          } else {
            stmt.bindNull(11);
            stmt.bindNull(12);
          }
        } else {
          stmt.bindNull(9);
          stmt.bindNull(10);
          stmt.bindNull(11);
          stmt.bindNull(12);
        }
        final VoteEntity _tmpVote = value.getVote();
        if(_tmpVote != null) {
          stmt.bindDouble(13, _tmpVote.getAverage());
          stmt.bindLong(14, _tmpVote.getCount());
        } else {
          stmt.bindNull(13);
          stmt.bindNull(14);
        }
      }
    };
  }

  @Override
  public Object insert(final List<MediaEntity> data, final Continuation<? super Unit> p1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMovieEntity.insert(data);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, p1);
  }

  @Override
  public Object insert(final MediaEntity movie, final Continuation<? super Unit> p1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMovieEntity.insert(movie);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, p1);
  }

  @Override
  public Object watchList(final Continuation<? super List<MediaEntity>> p0) {
    final String _sql = "SELECT * FROM movie WHERE watch_list = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.execute(__db, false, new Callable<List<MediaEntity>>() {
      @Override
      public List<MediaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfRelease = CursorUtil.getColumnIndexOrThrow(_cursor, "release");
          final int _cursorIndexOfWatchList = CursorUtil.getColumnIndexOrThrow(_cursor, "watch_list");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "backdrop_thumbnail_url");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "backdrop_thumbnail_type");
          final int _cursorIndexOfUrl_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "backdrop_original_url");
          final int _cursorIndexOfType_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "backdrop_original_type");
          final int _cursorIndexOfUrl_2 = CursorUtil.getColumnIndexOrThrow(_cursor, "poster_thumbnail_url");
          final int _cursorIndexOfType_2 = CursorUtil.getColumnIndexOrThrow(_cursor, "poster_thumbnail_type");
          final int _cursorIndexOfUrl_3 = CursorUtil.getColumnIndexOrThrow(_cursor, "poster_original_url");
          final int _cursorIndexOfType_3 = CursorUtil.getColumnIndexOrThrow(_cursor, "poster_original_type");
          final int _cursorIndexOfAverage = CursorUtil.getColumnIndexOrThrow(_cursor, "vote_average");
          final int _cursorIndexOfCount = CursorUtil.getColumnIndexOrThrow(_cursor, "vote_count");
          final List<MediaEntity> _result = new ArrayList<MediaEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MediaEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final long _tmpRelease;
            _tmpRelease = _cursor.getLong(_cursorIndexOfRelease);
            final boolean _tmpWatchList;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWatchList);
            _tmpWatchList = _tmp != 0;
            final PictureEntity _tmpBackdrop;
            if (! (_cursor.isNull(_cursorIndexOfUrl) && _cursor.isNull(_cursorIndexOfType) && _cursor.isNull(_cursorIndexOfUrl_1) && _cursor.isNull(_cursorIndexOfType_1))) {
              final SizeEntity _tmpThumbnail;
              if (! (_cursor.isNull(_cursorIndexOfUrl) && _cursor.isNull(_cursorIndexOfType))) {
                final String _tmpUrl;
                _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
                final String _tmpType;
                _tmpType = _cursor.getString(_cursorIndexOfType);
                _tmpThumbnail = new SizeEntity(_tmpUrl,_tmpType);
              }  else  {
                _tmpThumbnail = null;
              }
              final SizeEntity _tmpOriginal;
              if (! (_cursor.isNull(_cursorIndexOfUrl_1) && _cursor.isNull(_cursorIndexOfType_1))) {
                final String _tmpUrl_1;
                _tmpUrl_1 = _cursor.getString(_cursorIndexOfUrl_1);
                final String _tmpType_1;
                _tmpType_1 = _cursor.getString(_cursorIndexOfType_1);
                _tmpOriginal = new SizeEntity(_tmpUrl_1,_tmpType_1);
              }  else  {
                _tmpOriginal = null;
              }
              _tmpBackdrop = new PictureEntity(_tmpThumbnail,_tmpOriginal);
            }  else  {
              _tmpBackdrop = null;
            }
            final PictureEntity _tmpPoster;
            if (! (_cursor.isNull(_cursorIndexOfUrl_2) && _cursor.isNull(_cursorIndexOfType_2) && _cursor.isNull(_cursorIndexOfUrl_3) && _cursor.isNull(_cursorIndexOfType_3))) {
              final SizeEntity _tmpThumbnail_1;
              if (! (_cursor.isNull(_cursorIndexOfUrl_2) && _cursor.isNull(_cursorIndexOfType_2))) {
                final String _tmpUrl_2;
                _tmpUrl_2 = _cursor.getString(_cursorIndexOfUrl_2);
                final String _tmpType_2;
                _tmpType_2 = _cursor.getString(_cursorIndexOfType_2);
                _tmpThumbnail_1 = new SizeEntity(_tmpUrl_2,_tmpType_2);
              }  else  {
                _tmpThumbnail_1 = null;
              }
              final SizeEntity _tmpOriginal_1;
              if (! (_cursor.isNull(_cursorIndexOfUrl_3) && _cursor.isNull(_cursorIndexOfType_3))) {
                final String _tmpUrl_3;
                _tmpUrl_3 = _cursor.getString(_cursorIndexOfUrl_3);
                final String _tmpType_3;
                _tmpType_3 = _cursor.getString(_cursorIndexOfType_3);
                _tmpOriginal_1 = new SizeEntity(_tmpUrl_3,_tmpType_3);
              }  else  {
                _tmpOriginal_1 = null;
              }
              _tmpPoster = new PictureEntity(_tmpThumbnail_1,_tmpOriginal_1);
            }  else  {
              _tmpPoster = null;
            }
            final VoteEntity _tmpVote;
            if (! (_cursor.isNull(_cursorIndexOfAverage) && _cursor.isNull(_cursorIndexOfCount))) {
              final float _tmpAverage;
              _tmpAverage = _cursor.getFloat(_cursorIndexOfAverage);
              final int _tmpCount;
              _tmpCount = _cursor.getInt(_cursorIndexOfCount);
              _tmpVote = new VoteEntity(_tmpAverage,_tmpCount);
            }  else  {
              _tmpVote = null;
            }
            _item = new MediaEntity(_tmpId,_tmpTitle,_tmpBackdrop,_tmpPoster,_tmpRelease,_tmpVote,_tmpWatchList);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, p0);
  }

  @Override
  public Object find(final long movieId, final Continuation<? super MediaEntity> p1) {
    final String _sql = "SELECT * FROM movie WHERE id == ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, movieId);
    return CoroutinesRoom.execute(__db, false, new Callable<MediaEntity>() {
      @Override
      public MediaEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfRelease = CursorUtil.getColumnIndexOrThrow(_cursor, "release");
          final int _cursorIndexOfWatchList = CursorUtil.getColumnIndexOrThrow(_cursor, "watch_list");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "backdrop_thumbnail_url");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "backdrop_thumbnail_type");
          final int _cursorIndexOfUrl_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "backdrop_original_url");
          final int _cursorIndexOfType_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "backdrop_original_type");
          final int _cursorIndexOfUrl_2 = CursorUtil.getColumnIndexOrThrow(_cursor, "poster_thumbnail_url");
          final int _cursorIndexOfType_2 = CursorUtil.getColumnIndexOrThrow(_cursor, "poster_thumbnail_type");
          final int _cursorIndexOfUrl_3 = CursorUtil.getColumnIndexOrThrow(_cursor, "poster_original_url");
          final int _cursorIndexOfType_3 = CursorUtil.getColumnIndexOrThrow(_cursor, "poster_original_type");
          final int _cursorIndexOfAverage = CursorUtil.getColumnIndexOrThrow(_cursor, "vote_average");
          final int _cursorIndexOfCount = CursorUtil.getColumnIndexOrThrow(_cursor, "vote_count");
          final MediaEntity _result;
          if(_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final long _tmpRelease;
            _tmpRelease = _cursor.getLong(_cursorIndexOfRelease);
            final boolean _tmpWatchList;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWatchList);
            _tmpWatchList = _tmp != 0;
            final PictureEntity _tmpBackdrop;
            if (! (_cursor.isNull(_cursorIndexOfUrl) && _cursor.isNull(_cursorIndexOfType) && _cursor.isNull(_cursorIndexOfUrl_1) && _cursor.isNull(_cursorIndexOfType_1))) {
              final SizeEntity _tmpThumbnail;
              if (! (_cursor.isNull(_cursorIndexOfUrl) && _cursor.isNull(_cursorIndexOfType))) {
                final String _tmpUrl;
                _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
                final String _tmpType;
                _tmpType = _cursor.getString(_cursorIndexOfType);
                _tmpThumbnail = new SizeEntity(_tmpUrl,_tmpType);
              }  else  {
                _tmpThumbnail = null;
              }
              final SizeEntity _tmpOriginal;
              if (! (_cursor.isNull(_cursorIndexOfUrl_1) && _cursor.isNull(_cursorIndexOfType_1))) {
                final String _tmpUrl_1;
                _tmpUrl_1 = _cursor.getString(_cursorIndexOfUrl_1);
                final String _tmpType_1;
                _tmpType_1 = _cursor.getString(_cursorIndexOfType_1);
                _tmpOriginal = new SizeEntity(_tmpUrl_1,_tmpType_1);
              }  else  {
                _tmpOriginal = null;
              }
              _tmpBackdrop = new PictureEntity(_tmpThumbnail,_tmpOriginal);
            }  else  {
              _tmpBackdrop = null;
            }
            final PictureEntity _tmpPoster;
            if (! (_cursor.isNull(_cursorIndexOfUrl_2) && _cursor.isNull(_cursorIndexOfType_2) && _cursor.isNull(_cursorIndexOfUrl_3) && _cursor.isNull(_cursorIndexOfType_3))) {
              final SizeEntity _tmpThumbnail_1;
              if (! (_cursor.isNull(_cursorIndexOfUrl_2) && _cursor.isNull(_cursorIndexOfType_2))) {
                final String _tmpUrl_2;
                _tmpUrl_2 = _cursor.getString(_cursorIndexOfUrl_2);
                final String _tmpType_2;
                _tmpType_2 = _cursor.getString(_cursorIndexOfType_2);
                _tmpThumbnail_1 = new SizeEntity(_tmpUrl_2,_tmpType_2);
              }  else  {
                _tmpThumbnail_1 = null;
              }
              final SizeEntity _tmpOriginal_1;
              if (! (_cursor.isNull(_cursorIndexOfUrl_3) && _cursor.isNull(_cursorIndexOfType_3))) {
                final String _tmpUrl_3;
                _tmpUrl_3 = _cursor.getString(_cursorIndexOfUrl_3);
                final String _tmpType_3;
                _tmpType_3 = _cursor.getString(_cursorIndexOfType_3);
                _tmpOriginal_1 = new SizeEntity(_tmpUrl_3,_tmpType_3);
              }  else  {
                _tmpOriginal_1 = null;
              }
              _tmpPoster = new PictureEntity(_tmpThumbnail_1,_tmpOriginal_1);
            }  else  {
              _tmpPoster = null;
            }
            final VoteEntity _tmpVote;
            if (! (_cursor.isNull(_cursorIndexOfAverage) && _cursor.isNull(_cursorIndexOfCount))) {
              final float _tmpAverage;
              _tmpAverage = _cursor.getFloat(_cursorIndexOfAverage);
              final int _tmpCount;
              _tmpCount = _cursor.getInt(_cursorIndexOfCount);
              _tmpVote = new VoteEntity(_tmpAverage,_tmpCount);
            }  else  {
              _tmpVote = null;
            }
            _result = new MediaEntity(_tmpId,_tmpTitle,_tmpBackdrop,_tmpPoster,_tmpRelease,_tmpVote,_tmpWatchList);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, p1);
  }
}
