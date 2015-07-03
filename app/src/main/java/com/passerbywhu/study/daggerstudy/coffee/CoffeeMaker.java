package com.passerbywhu.study.daggerstudy.coffee;

import dagger.Lazy;
import javax.inject.Inject;

class CoffeeMaker {
  private final Lazy<Heater> heater; // Create a possibly costly heater only when we use it.
  private final Pump pump;

  @Inject CoffeeMaker(Lazy<Heater> heater, Pump pump) {
    this.heater = heater;
    this.pump = pump;
  }

  public String brew() {
    StringBuffer sb = new StringBuffer();
    sb.append(heater.get().on() + "\n");
    sb.append(pump.pump() + "\n");
    sb.append(" [_]P coffee! [_]P ");
    heater.get().off();
    return sb.toString();
  }
}
