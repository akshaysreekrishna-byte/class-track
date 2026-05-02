package com.attendease.core.domain.usecase;

import com.attendease.core.domain.repository.AttendanceRepository;
import com.attendease.core.domain.repository.SubjectRepository;
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
public final class GetAttendanceSummaryUseCase_Factory implements Factory<GetAttendanceSummaryUseCase> {
  private final Provider<SubjectRepository> subjectRepositoryProvider;

  private final Provider<AttendanceRepository> attendanceRepositoryProvider;

  public GetAttendanceSummaryUseCase_Factory(Provider<SubjectRepository> subjectRepositoryProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider) {
    this.subjectRepositoryProvider = subjectRepositoryProvider;
    this.attendanceRepositoryProvider = attendanceRepositoryProvider;
  }

  @Override
  public GetAttendanceSummaryUseCase get() {
    return newInstance(subjectRepositoryProvider.get(), attendanceRepositoryProvider.get());
  }

  public static GetAttendanceSummaryUseCase_Factory create(
      Provider<SubjectRepository> subjectRepositoryProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider) {
    return new GetAttendanceSummaryUseCase_Factory(subjectRepositoryProvider, attendanceRepositoryProvider);
  }

  public static GetAttendanceSummaryUseCase newInstance(SubjectRepository subjectRepository,
      AttendanceRepository attendanceRepository) {
    return new GetAttendanceSummaryUseCase(subjectRepository, attendanceRepository);
  }
}
