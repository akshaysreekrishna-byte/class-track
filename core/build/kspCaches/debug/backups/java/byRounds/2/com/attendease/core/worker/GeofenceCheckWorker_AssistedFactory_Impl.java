package com.attendease.core.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class GeofenceCheckWorker_AssistedFactory_Impl implements GeofenceCheckWorker_AssistedFactory {
  private final GeofenceCheckWorker_Factory delegateFactory;

  GeofenceCheckWorker_AssistedFactory_Impl(GeofenceCheckWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public GeofenceCheckWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<GeofenceCheckWorker_AssistedFactory> create(
      GeofenceCheckWorker_Factory delegateFactory) {
    return InstanceFactory.create(new GeofenceCheckWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<GeofenceCheckWorker_AssistedFactory> createFactoryProvider(
      GeofenceCheckWorker_Factory delegateFactory) {
    return InstanceFactory.create(new GeofenceCheckWorker_AssistedFactory_Impl(delegateFactory));
  }
}
