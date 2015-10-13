package com.passerbywhu.study.daggerstudy.coffee;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module //(includes = PumpModule.class)
class DripCoffeeModule {
  @Provides @Singleton Heater provideHeater() {
    return new ElectricHeater();
  }

  @Provides Pump providePump(Thermosiphon pump) {
    return pump;
  }
}
