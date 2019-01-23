package misc

import scala.collection.mutable._
import misc.Score


object Results {
	var username: String = _
	var topScores: MutableList[Score] = MutableList()
	val maxScores: Int = 5
	var currentScore: Score = _

	// def addTopScore(score: Score) = {
	// 	this.topScores += score
	// 	this.topScores = this.topScores.sortBy(r => (-r.getPoints(), r.getTime()))

	// 	if (this.topScores.length > this.maxScores){
	// 		this.topScores = this.topScores.dropRight(1)
	// 	}
	// }

	def setTopScores(scores: MutableList[Score]) = {
		this.topScores = scores
	}

	def getTopScores():MutableList[Score]  = {
		return this.topScores
	}

	def setCurrentScore(score: Score) = {
		this.currentScore = score
	}

	def getCurrentScore(): Score = {
		return this.currentScore
	}

	def setUserName(username: String) = {
		this.username = username
	}

	def getUserName(): String = {
		return this.username
	}
}