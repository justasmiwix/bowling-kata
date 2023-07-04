package com.kata.bowling

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class GameTest extends SpecWithJUnit {
  "Bowling Game" should {
    /*"score one roll" in new ctx {
      val score = 6
      game.roll(score).score mustEqual score
    }

    "score two rolls" in new ctx {
      val roll1 = 2
      val roll2 = 3

      game.roll(roll1).roll(roll2).score mustEqual roll1 + roll2
    }

    /*"fail to play over 10 frames" in new ctx {
      Seq.fill(20)(1).foldLeft(game)((game, roll) => {
        game.roll(roll)
      })
        .roll(1) must throwA[IllegalArgumentException]("Cannot play over 10 frames")
    }*/

    "fail to play over 10 frames when last frame is not spare or strike" in new ctx {
      Seq.fill(20)(1).foldLeft(game)((game, roll) => {
        game.roll(roll)
      })
        .roll(1) must throwA[IllegalArgumentException]("Cannot play over 10 frames")
    }

    "score spare" in new ctx {
      game.roll(8).roll(2).roll(6)
        .score mustEqual 8 + 2 + 6 * 2
    }

    "score strike" in new ctx {
      game.roll(10).roll(2).roll(3)
        .score mustEqual 10 + (2 + 3) + (2 + 3)
    }

    "score 2 strikes in a row" in new ctx {
      game.roll(10).roll(10).roll(2).roll(3)
        .score mustEqual 10 + 10 + 10 + 2 + 2 + 2 + 3 + 3
    }

    "score 1 extra roll" in new ctx {
      Seq.fill(18)(1).foldLeft(game)((game, roll) => {
        game.roll(roll)
      })
        .roll(5).roll(5).roll(5).score mustEqual 18 + 5 + 5 + 5
    }

    "score 2 extra rolls" in new ctx {
      Seq.fill(18)(1).foldLeft(game)((game, roll) => {
        game.roll(roll)
      })
        .roll(10).roll(5).roll(5).score mustEqual 18 + 10 + 5 + 5
    }*/

    "score 12 strikes" in new ctx {
      Seq.fill(12)(10).foldLeft(game)((game, roll) => {
        game.roll(roll)
      })
        .score mustEqual 300
    }

  }


  trait ctx extends Scope {
    val game = new Game(Seq())
  }
}
