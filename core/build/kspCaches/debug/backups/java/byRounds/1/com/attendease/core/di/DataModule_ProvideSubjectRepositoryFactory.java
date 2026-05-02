package com.attendease.core.di;

import com.attendease.core.data.database.dao.SubjectDao;
import com.attendease.core.domain.repository.SubjectRepository;
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
public final class DataModule_ProvideSubjectRepositoryFactory implements Factory<SubjectRepository> {
  private final Provider<SubjectDao> subjectDaoProvider;

  public DataModule_ProvideSubjectRepositoryFactory(Provider<SubjectDao> subjectDaoProvider) {
    this.subjectDaoProvider = subjectDaoProvider;
  }

  @Override
  public SubjectRepository get() {
    return provideSubjectRepository(subjectDaoProvider.get());
  }

  public static DataModule_ProvideSubjectRepositoryFactory create(
      Provider<SubjectDao> subjectDaoProvider) {
    return new DataModule_ProvideSubjectRepositoryFactory(subjectDaoProvider);
  }

  public static SubjectRepository provideSubjectRepository(SubjectDao subjectDao) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideSubjectRepository(subjectDao));
  }
}
