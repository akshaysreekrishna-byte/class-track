package com.attendease.core.domain.usecase;

import com.attendease.core.domain.repository.AttendanceRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class MarkAttendanceUseCase_Factory implements Factory<MarkAttendanceUseCase> {
  private final Provider<AttendanceRepository> attendanceRepositoryProvider;

  public MarkAttendanceUseCase_Factory(
      Provider<AttendanceRepository> attendanceRepositoryProvider) {
    this.attendanceRepositoryProvider = attendanceRepositoryProvider;
  }

  @Override
  public MarkAttendanceUseCase get() {
    return newInstance(attendanceRepositoryProvider.get());
  }

  public static MarkAttendanceUseCase_Factory create(
      Provider<AttendanceRepository> attendanceRepositoryProvider) {
    return new MarkAttendanceUseCase_Factory(attendanceRepositoryProvider);
  }

  public static MarkAttendanceUseCase newInstance(AttendanceRepository attendanceRepository) {
    return new MarkAttendanceUseCase(attendanceRepository);
  }
}
