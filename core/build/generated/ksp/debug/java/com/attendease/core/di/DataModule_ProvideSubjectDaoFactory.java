package com.attendease.core.di;

import com.attendease.core.data.database.AppDatabase;
import com.attendease.core.data.database.dao.SubjectDao;
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
public final class DataModule_ProvideSubjectDaoFactory implements Factory<SubjectDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DataModule_ProvideSubjectDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SubjectDao get() {
    return provideSubjectDao(databaseProvider.get());
  }

  public static DataModule_ProvideSubjectDaoFactory create(Provider<AppDatabase> databaseProvider) {
    return new DataModule_ProvideSubjectDaoFactory(databaseProvider);
  }

  public static SubjectDao provideSubjectDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideSubjectDao(database));
  }
}
