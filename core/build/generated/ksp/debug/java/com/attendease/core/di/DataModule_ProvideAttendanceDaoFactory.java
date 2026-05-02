package com.attendease.core.di;

import com.attendease.core.data.database.AppDatabase;
import com.attendease.core.data.database.dao.AttendanceDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DataModule_ProvideAttendanceDaoFactory implements Factory<AttendanceDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DataModule_ProvideAttendanceDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AttendanceDao get() {
    return provideAttendanceDao(databaseProvider.get());
  }

  public static DataModule_ProvideAttendanceDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DataModule_ProvideAttendanceDaoFactory(databaseProvider);
  }

  public static AttendanceDao provideAttendanceDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideAttendanceDao(database));
  }
}
