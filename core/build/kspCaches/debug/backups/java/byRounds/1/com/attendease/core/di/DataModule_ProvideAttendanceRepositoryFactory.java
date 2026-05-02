package com.attendease.core.di;

import com.attendease.core.data.database.dao.AttendanceDao;
import com.attendease.core.domain.repository.AttendanceRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DataModule_ProvideAttendanceRepositoryFactory implements Factory<AttendanceRepository> {
  private final Provider<AttendanceDao> attendanceDaoProvider;

  public DataModule_ProvideAttendanceRepositoryFactory(
      Provider<AttendanceDao> attendanceDaoProvider) {
    this.attendanceDaoProvider = attendanceDaoProvider;
  }

  @Override
  public AttendanceRepository get() {
    return provideAttendanceRepository(attendanceDaoProvider.get());
  }

  public static DataModule_ProvideAttendanceRepositoryFactory create(
      Provider<AttendanceDao> attendanceDaoProvider) {
    return new DataModule_ProvideAttendanceRepositoryFactory(attendanceDaoProvider);
  }

  public static AttendanceRepository provideAttendanceRepository(AttendanceDao attendanceDao) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideAttendanceRepository(attendanceDao));
  }
}
