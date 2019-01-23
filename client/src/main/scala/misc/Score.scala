package misc

class Score(username: String, points: Int, time: Int) {

	def getUsername(): String = { return this.username }
	def getPoints(): Int = { return this.points }
	def getTime(): Int = { return this.time }

	override def toString(): String = {
		return s"$username - SCORES: $points TIME: $time"
	}
}
