package com.kata.bowling

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class GameTest extends SpecWithJUnit {
  "Bowling Game" should {
    "score all 9 throws" in new ctx {
      val game = new Game

      game.score must_== 90
    }
  }

  trait ctx extends Scope
}
