package com.passerbywhu.study.daggerstudy.coffee;

import javax.inject.Inject;

class Thermosiphon implements Pump {
  private final Heater heater;

  @Inject
  Thermosiphon(Heater heater) {
    this.heater = heater;
  }

  @Override public String pump() {
    if (heater.isHot()) {
      return "=> => pumping => =>";
    }
    return "";
  }
}
