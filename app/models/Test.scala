package models

import utils.CassandraClient

import javax.inject.{Inject, Singleton}

@Singleton
class Test {
  private val session = CassandraClient.session
  private val table = "test"
  private val keysapace = "arun"

  private val insertPS = session.prepare(s"INSERT INTO $keysapace.$table(name , salary) values(?, ?)")

  def insert(name: String, salary: Double): Boolean = {
    session.execute(insertPS.bind(name, salary))
      .wasApplied()
  }
}
