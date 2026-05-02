package com.attendease.feature.subjects;

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
public final class SubjectsViewModel_Factory implements Factory<SubjectsViewModel> {
  private final Provider<SubjectRepository> subjectRepositoryProvider;

  public SubjectsViewModel_Factory(Provider<SubjectRepository> subjectRepositoryProvider) {
    this.subjectRepositoryProvider = subjectRepositoryProvider;
  }

  @Override
  public SubjectsViewModel get() {
    return newInstance(subjectRepositoryProvider.get());
  }

  public static SubjectsViewModel_Factory create(
      Provider<SubjectRepository> subjectRepositoryProvider) {
    return new SubjectsViewModel_Factory(subjectRepositoryProvider);
  }

  public static SubjectsViewModel newInstance(SubjectRepository subjectRepository) {
    return new SubjectsViewModel(subjectRepository);
  }
}
