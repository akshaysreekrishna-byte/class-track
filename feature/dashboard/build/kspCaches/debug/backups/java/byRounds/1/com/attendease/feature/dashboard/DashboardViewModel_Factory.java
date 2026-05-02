package com.attendease.feature.dashboard;

import com.attendease.core.domain.usecase.GetAttendanceSummaryUseCase;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<GetAttendanceSummaryUseCase> getAttendanceSummaryUseCaseProvider;

  public DashboardViewModel_Factory(
      Provider<GetAttendanceSummaryUseCase> getAttendanceSummaryUseCaseProvider) {
    this.getAttendanceSummaryUseCaseProvider = getAttendanceSummaryUseCaseProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(getAttendanceSummaryUseCaseProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<GetAttendanceSummaryUseCase> getAttendanceSummaryUseCaseProvider) {
    return new DashboardViewModel_Factory(getAttendanceSummaryUseCaseProvider);
  }

  public static DashboardViewModel newInstance(
      GetAttendanceSummaryUseCase getAttendanceSummaryUseCase) {
    return new DashboardViewModel(getAttendanceSummaryUseCase);
  }
}
