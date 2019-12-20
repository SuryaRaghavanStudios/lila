package lila.blog

import com.softwaremill.macwire._
import io.methvin.play.autoconfig._
import play.api.Configuration
import scala.concurrent.duration.FiniteDuration

private class BlogConfig(
    @ConfigName("prismic.api_url") val apiUrl: String,
    val collection: String,
    @ConfigName("last_post_cache.ttl") val lastPostTtl: FiniteDuration
)

@Module
final class Env(
    appConfig: Configuration,
    asyncCache: lila.memo.AsyncCache.Builder,
    timelineApi: lila.timeline.EntryApi
)(
    implicit ec: scala.concurrent.ExecutionContext,
    system: akka.actor.ActorSystem,
    ws: play.api.libs.ws.WSClient
) {

  private val config = appConfig.get[BlogConfig]("blog")(AutoConfig.loader)

  lazy val api = wire[BlogApi]

  private lazy val notifier = wire[Notifier]

  lazy val lastPostCache = wire[LastPostCache]
}
