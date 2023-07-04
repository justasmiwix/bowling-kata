package com.kata.bowling

/**
 * Bowling Game class
 *
 * You can change anything here. These two methods are just an example.
 */
class Game(rolls: Seq[Roll]) {

  private val (frames, (extraRoll1, extraRoll2)): (Seq[Frame], (Option[Roll], Option[Roll])) = calculateFrames
  def roll(pinsDown: Int): Game = {
    new Game(rolls :+ Roll(pinsDown))
  }

  def score: Int = {
    val (mainFramesScore, _, _, _) = frames.foldLeft(0, false, false, false) {
      case ((score, previousFrameWasSpare, previousPreviousFrameWasStrike, previousFrameWasStrike), currentFrame) => {
        val newPreviousPreviousFrameWasStrike = if(currentFrame.isStrike) previousFrameWasStrike else false
        val newPreviousFrameWasStrike = currentFrame.isStrike

        val currentFrameScore = {
          if(previousPreviousFrameWasStrike || previousFrameWasStrike)
            currentFrame.scoreWithStrikeBonus(previousPreviousFrameWasStrike, previousFrameWasStrike)
          else if (previousFrameWasSpare) currentFrame.scoreWithSpareBonus
          else currentFrame.score
        }
        val newScore = score + currentFrameScore

        (newScore, currentFrame.isSpare, newPreviousPreviousFrameWasStrike, newPreviousFrameWasStrike)
      }
    }

    val extraRollsScore = extraRoll1.getOrElse(Roll(0)).score + extraRoll2.getOrElse(Roll(0)).score

    println(mainFramesScore + " + " + extraRollsScore)
    mainFramesScore + extraRollsScore
  }

  private def calculateFrames: (Seq[Frame], (Option[Roll], Option[Roll])) = {
    val (previousValue, list) = rolls.foldLeft((0, Seq()): (Int, Seq[Frame])) { case ((previousValue, list), Roll(value)) =>
      if(list.length == 10) 0 -> list
      else {
        if (previousValue == 0) { // first roll of the frame
          if (value == 10) 0 -> (list :+ Frame(Roll(value), None))
          else value -> list
        }
        else 0 -> (list :+ Frame(Roll(previousValue), Some(Roll(value))))
      }
    }

    val paddedList =
      if(previousValue != 0 && list.length < 10) list :+ Frame(Roll(previousValue), None)
      else list

    val extraRollsNeeded =
      if(paddedList.length == 10) {
        if(paddedList.last.isSpare) 1
        else if(paddedList.last.isStrike) 2
        else 0
      }
      else 0

    val rollsInMainFrames = rollCountInFrames(paddedList)

    val extraRolls = rolls.drop(rollsInMainFrames)

    if(extraRolls.length > extraRollsNeeded) {
      if(extraRollsNeeded == 0) throw new IllegalArgumentException("Cannot play over 10 frames")
      else throw new IllegalArgumentException("Invalid amount of rolls")
    }

    val (extraRoll1, extraRoll2): (Option[Roll], Option[Roll]) = {
      if(extraRollsNeeded == 0) (None, None)
      else if(extraRollsNeeded == 1) (Some(nthSeqElementOption(extraRolls, 0, Roll(0))), None)
      else (Some(nthSeqElementOption(extraRolls, 0, Roll(0))), Some(nthSeqElementOption(extraRolls, 1, Roll(0))))
    }

    (paddedList, (extraRoll1, extraRoll2))
  }

  private def nthSeqElementOption[A](list : Seq[A], index: Int, defaultVal: A) = {
    val firstNElements = list.take(index + 1)
    val nthElementOption = firstNElements.reverse.headOption.getOrElse(defaultVal)
    nthElementOption
  }
  private def rollCountInFrames(frames: Seq[Frame]): Int =
    frames.foldLeft(0)((acc, frame) => {
      if (frame.isStrike) acc + 1
      else acc + 2
    })
}
case class Roll(pinsDown: Int) {
  def score = pinsDown
}

case class Frame(roll1: Roll, roll2: Option[Roll]) {
  def score = roll1.score + roll2.getOrElse(Roll(0)).score
  def scoreWithSpareBonus = roll1.score * 2 + roll2.getOrElse(Roll(0)).score
  def scoreWithStrikeBonus(previousPreviousFrameWasStrike: Boolean, previousFrameWasStrike: Boolean): Int = {
    val roll1WithStrikeBonus = (Seq(previousPreviousFrameWasStrike, previousFrameWasStrike).filter(a => a == true).length + 1) * roll1.score
    val roll2WithStrikeBonus = if(previousFrameWasStrike) roll2.getOrElse(Roll(0)).score * 2 else roll2.getOrElse(Roll(0)).score
    roll1WithStrikeBonus + roll2WithStrikeBonus
  }

  def isSpare: Boolean = roll1.score + roll2.getOrElse(Roll(0)).score == 10 && roll1.score != 10
  def isStrike: Boolean = roll1.score == 10
}