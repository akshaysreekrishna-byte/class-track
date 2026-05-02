package com.attendease.core.data.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.attendease.core.data.database.entity.SubjectEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SubjectDao_Impl implements SubjectDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SubjectEntity> __insertionAdapterOfSubjectEntity;

  private final EntityDeletionOrUpdateAdapter<SubjectEntity> __deletionAdapterOfSubjectEntity;

  private final EntityDeletionOrUpdateAdapter<SubjectEntity> __updateAdapterOfSubjectEntity;

  public SubjectDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSubjectEntity = new EntityInsertionAdapter<SubjectEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `subjects` (`id`,`name`,`typeOrdinal`,`requiredPercentage`,`geofenceLat`,`geofenceLng`,`geofenceRadius`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubjectEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getTypeOrdinal());
        statement.bindDouble(4, entity.getRequiredPercentage());
        if (entity.getGeofenceLat() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getGeofenceLat());
        }
        if (entity.getGeofenceLng() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getGeofenceLng());
        }
        if (entity.getGeofenceRadius() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getGeofenceRadius());
        }
      }
    };
    this.__deletionAdapterOfSubjectEntity = new EntityDeletionOrUpdateAdapter<SubjectEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `subjects` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubjectEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSubjectEntity = new EntityDeletionOrUpdateAdapter<SubjectEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `subjects` SET `id` = ?,`name` = ?,`typeOrdinal` = ?,`requiredPercentage` = ?,`geofenceLat` = ?,`geofenceLng` = ?,`geofenceRadius` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubjectEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getTypeOrdinal());
        statement.bindDouble(4, entity.getRequiredPercentage());
        if (entity.getGeofenceLat() == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.getGeofenceLat());
        }
        if (entity.getGeofenceLng() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getGeofenceLng());
        }
        if (entity.getGeofenceRadius() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getGeofenceRadius());
        }
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insertSubject(final SubjectEntity subject,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSubjectEntity.insertAndReturnId(subject);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSubject(final SubjectEntity subject,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSubjectEntity.handle(subject);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSubject(final SubjectEntity subject,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSubjectEntity.handle(subject);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SubjectEntity>> getAllSubjects() {
    final String _sql = "SELECT * FROM subjects";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"subjects"}, new Callable<List<SubjectEntity>>() {
      @Override
      @NonNull
      public List<SubjectEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTypeOrdinal = CursorUtil.getColumnIndexOrThrow(_cursor, "typeOrdinal");
          final int _cursorIndexOfRequiredPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "requiredPercentage");
          final int _cursorIndexOfGeofenceLat = CursorUtil.getColumnIndexOrThrow(_cursor, "geofenceLat");
          final int _cursorIndexOfGeofenceLng = CursorUtil.getColumnIndexOrThrow(_cursor, "geofenceLng");
          final int _cursorIndexOfGeofenceRadius = CursorUtil.getColumnIndexOrThrow(_cursor, "geofenceRadius");
          final List<SubjectEntity> _result = new ArrayList<SubjectEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubjectEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpTypeOrdinal;
            _tmpTypeOrdinal = _cursor.getInt(_cursorIndexOfTypeOrdinal);
            final double _tmpRequiredPercentage;
            _tmpRequiredPercentage = _cursor.getDouble(_cursorIndexOfRequiredPercentage);
            final Double _tmpGeofenceLat;
            if (_cursor.isNull(_cursorIndexOfGeofenceLat)) {
              _tmpGeofenceLat = null;
            } else {
              _tmpGeofenceLat = _cursor.getDouble(_cursorIndexOfGeofenceLat);
            }
            final Double _tmpGeofenceLng;
            if (_cursor.isNull(_cursorIndexOfGeofenceLng)) {
              _tmpGeofenceLng = null;
            } else {
              _tmpGeofenceLng = _cursor.getDouble(_cursorIndexOfGeofenceLng);
            }
            final Float _tmpGeofenceRadius;
            if (_cursor.isNull(_cursorIndexOfGeofenceRadius)) {
              _tmpGeofenceRadius = null;
            } else {
              _tmpGeofenceRadius = _cursor.getFloat(_cursorIndexOfGeofenceRadius);
            }
            _item = new SubjectEntity(_tmpId,_tmpName,_tmpTypeOrdinal,_tmpRequiredPercentage,_tmpGeofenceLat,_tmpGeofenceLng,_tmpGeofenceRadius);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<SubjectEntity> getSubjectById(final long id) {
    final String _sql = "SELECT * FROM subjects WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"subjects"}, new Callable<SubjectEntity>() {
      @Override
      @Nullable
      public SubjectEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTypeOrdinal = CursorUtil.getColumnIndexOrThrow(_cursor, "typeOrdinal");
          final int _cursorIndexOfRequiredPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "requiredPercentage");
          final int _cursorIndexOfGeofenceLat = CursorUtil.getColumnIndexOrThrow(_cursor, "geofenceLat");
          final int _cursorIndexOfGeofenceLng = CursorUtil.getColumnIndexOrThrow(_cursor, "geofenceLng");
          final int _cursorIndexOfGeofenceRadius = CursorUtil.getColumnIndexOrThrow(_cursor, "geofenceRadius");
          final SubjectEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpTypeOrdinal;
            _tmpTypeOrdinal = _cursor.getInt(_cursorIndexOfTypeOrdinal);
            final double _tmpRequiredPercentage;
            _tmpRequiredPercentage = _cursor.getDouble(_cursorIndexOfRequiredPercentage);
            final Double _tmpGeofenceLat;
            if (_cursor.isNull(_cursorIndexOfGeofenceLat)) {
              _tmpGeofenceLat = null;
            } else {
              _tmpGeofenceLat = _cursor.getDouble(_cursorIndexOfGeofenceLat);
            }
            final Double _tmpGeofenceLng;
            if (_cursor.isNull(_cursorIndexOfGeofenceLng)) {
              _tmpGeofenceLng = null;
            } else {
              _tmpGeofenceLng = _cursor.getDouble(_cursorIndexOfGeofenceLng);
            }
            final Float _tmpGeofenceRadius;
            if (_cursor.isNull(_cursorIndexOfGeofenceRadius)) {
              _tmpGeofenceRadius = null;
            } else {
              _tmpGeofenceRadius = _cursor.getFloat(_cursorIndexOfGeofenceRadius);
            }
            _result = new SubjectEntity(_tmpId,_tmpName,_tmpTypeOrdinal,_tmpRequiredPercentage,_tmpGeofenceLat,_tmpGeofenceLng,_tmpGeofenceRadius);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
