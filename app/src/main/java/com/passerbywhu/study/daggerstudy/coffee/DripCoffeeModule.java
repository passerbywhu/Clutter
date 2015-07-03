package com.passerbywhu.study.daggerstudy.coffee;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(includes = PumpModule.class)
class DripCoffeeModule {
  @Provides @Singleton Heater provideHeater() {
    return new ElectricHeater();
  }
}
