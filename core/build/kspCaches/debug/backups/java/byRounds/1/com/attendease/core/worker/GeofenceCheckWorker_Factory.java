package com.attendease.core.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.attendease.core.domain.repository.AttendanceRepository;
import com.attendease.core.domain.repository.SubjectRepository;
import com.attendease.core.domain.usecase.MarkAttendanceUseCase;
import dagger.internal.DaggerGenerated;
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
public final class GeofenceCheckWorker_Factory {
  private final Provider<SubjectRepository> subjectRepositoryProvider;

  private final Provider<AttendanceRepository> attendanceRepositoryProvider;

  private final Provider<MarkAttendanceUseCase> markAttendanceUseCaseProvider;

  public GeofenceCheckWorker_Factory(Provider<SubjectRepository> subjectRepositoryProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider,
      Provider<MarkAttendanceUseCase> markAttendanceUseCaseProvider) {
    this.subjectRepositoryProvider = subjectRepositoryProvider;
    this.attendanceRepositoryProvider = attendanceRepositoryProvider;
    this.markAttendanceUseCaseProvider = markAttendanceUseCaseProvider;
  }

  public GeofenceCheckWorker get(Context appContext, WorkerParameters workerParams) {
    return newInstance(appContext, workerParams, subjectRepositoryProvider.get(), attendanceRepositoryProvider.get(), markAttendanceUseCaseProvider.get());
  }

  public static GeofenceCheckWorker_Factory create(
      Provider<SubjectRepository> subjectRepositoryProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider,
      Provider<MarkAttendanceUseCase> markAttendanceUseCaseProvider) {
    return new GeofenceCheckWorker_Factory(subjectRepositoryProvider, attendanceRepositoryProvider, markAttendanceUseCaseProvider);
  }

  public static GeofenceCheckWorker newInstance(Context appContext, WorkerParameters workerParams,
      SubjectRepository subjectRepository, AttendanceRepository attendanceRepository,
      MarkAttendanceUseCase markAttendanceUseCase) {
    return new GeofenceCheckWorker(appContext, workerParams, subjectRepository, attendanceRepository, markAttendanceUseCase);
  }
}
