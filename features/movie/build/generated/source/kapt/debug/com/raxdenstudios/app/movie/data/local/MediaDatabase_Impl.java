package com.raxdenstudios.app.movie.data.local;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MediaDatabase_Impl extends MediaDatabase {
  private volatile MovieDao _movieDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `movie` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `release` INTEGER NOT NULL, `watch_list` INTEGER NOT NULL, `backdrop_thumbnail_url` TEXT, `backdrop_thumbnail_type` TEXT, `backdrop_original_url` TEXT, `backdrop_original_type` TEXT, `poster_thumbnail_url` TEXT, `poster_thumbnail_type` TEXT, `poster_original_url` TEXT, `poster_original_type` TEXT, `vote_average` REAL NOT NULL, `vote_count` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ca4ca185b800dd495036856c6e0b581e')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `movie`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsMovie = new HashMap<String, TableInfo.Column>(14);
        _columnsMovie.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("release", new TableInfo.Column("release", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("watch_list", new TableInfo.Column("watch_list", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("backdrop_thumbnail_url", new TableInfo.Column("backdrop_thumbnail_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("backdrop_thumbnail_type", new TableInfo.Column("backdrop_thumbnail_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("backdrop_original_url", new TableInfo.Column("backdrop_original_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("backdrop_original_type", new TableInfo.Column("backdrop_original_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("poster_thumbnail_url", new TableInfo.Column("poster_thumbnail_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("poster_thumbnail_type", new TableInfo.Column("poster_thumbnail_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("poster_original_url", new TableInfo.Column("poster_original_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("poster_original_type", new TableInfo.Column("poster_original_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("vote_average", new TableInfo.Column("vote_average", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMovie.put("vote_count", new TableInfo.Column("vote_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMovie = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMovie = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMovie = new TableInfo("movie", _columnsMovie, _foreignKeysMovie, _indicesMovie);
        final TableInfo _existingMovie = TableInfo.read(_db, "movie");
        if (! _infoMovie.equals(_existingMovie)) {
          return new RoomOpenHelper.ValidationResult(false, "movie(com.raxdenstudios.app.movie.data.local.model.MovieEntity).\n"
                  + " Expected:\n" + _infoMovie + "\n"
                  + " Found:\n" + _existingMovie);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ca4ca185b800dd495036856c6e0b581e", "14e11075415d1b3a0405517df743edec");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "movie");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `movie`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public MovieDao watchListDao() {
    if (_movieDao != null) {
      return _movieDao;
    } else {
      synchronized(this) {
        if(_movieDao == null) {
          _movieDao = new MovieDao_Impl(this);
        }
        return _movieDao;
      }
    }
  }
}
