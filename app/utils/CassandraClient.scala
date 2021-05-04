package utils

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.policies.ConstantReconnectionPolicy

object CassandraClient {

  private val reconnectionPolicy = new ConstantReconnectionPolicy(1000L)

  private val cluster = Cluster.builder()
    .addContactPoint("10.0.1.53")
    .withReconnectionPolicy(reconnectionPolicy)
    .build()

  val session = cluster.connect()
}
