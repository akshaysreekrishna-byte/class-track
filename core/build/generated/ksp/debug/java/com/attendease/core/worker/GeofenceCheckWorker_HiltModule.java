package com.attendease.core.worker;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = GeofenceCheckWorker.class
)
public interface GeofenceCheckWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.attendease.core.worker.GeofenceCheckWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(
      GeofenceCheckWorker_AssistedFactory factory);
}
