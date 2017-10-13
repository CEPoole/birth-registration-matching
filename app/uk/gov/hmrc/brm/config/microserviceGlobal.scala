/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.brm.config

import javax.inject.Inject

import net.ceedubs.ficus.Ficus._
import com.google.inject.Singleton
import com.typesafe.config.Config
import play.api.mvc.EssentialFilter
import play.api.{Application, Configuration}
import uk.gov.hmrc.play.audit.filters.AuditFilter
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.config.{AppName, ControllerConfig, RunMode}
import uk.gov.hmrc.play.filters.MicroserviceFilterSupport
import uk.gov.hmrc.play.filters.frontend.HeadersFilter
import uk.gov.hmrc.play.http.logging.filters.LoggingFilter
import uk.gov.hmrc.play.microservice.bootstrap.{DefaultMicroserviceGlobal, MicroserviceFilters}

@Singleton
class ControllerConfiguration @Inject()(configuration: Configuration) extends ControllerConfig {
  lazy val controllerConfigs = configuration.underlying.as[Config]("controllers")
}

@Singleton
class MicroserviceAuditFilter @Inject()(override val auditConnector: AuditConnector,
                                         controllerConfig: ControllerConfig
                                       ) extends AuditFilter with AppName with MicroserviceFilterSupport  {
  override def controllerNeedsAuditing(controllerName: String) = controllerConfig
    .paramsForController(controllerName).needsAuditing
}

@Singleton
class MicroserviceLoggingFilter @Inject()(controllerConfig: ControllerConfig
                                         ) extends LoggingFilter with MicroserviceFilterSupport {
  override def controllerNeedsLogging(controllerName: String) = controllerConfig
    .paramsForController(controllerName).needsLogging
}

@Singleton
case class MicroserviceGlobal @Inject() (
                                        auditConnector: MicroserviceAuditConnector,
                                        loggingFilter: MicroserviceLoggingFilter,
                                        microserviceAuditFilter: MicroserviceAuditFilter
                                        )
  extends DefaultMicroserviceGlobal
    with RunMode
    with MicroserviceFilters {

//  override def auditConnector = new MicroserviceAuditConnector

  override def microserviceMetricsConfig(implicit app: Application): Option[Configuration]
  = app.configuration.getConfig(s"microservice.metrics")

//  override def loggingFilter = new MicroserviceLoggingFilter

//  override def microserviceAuditFilter = new MicroserviceAuditFilter

  override def authFilter = None

  override def microserviceFilters: Seq[EssentialFilter] = {
    defaultMicroserviceFilters ++ Seq(HeadersFilter)
  }

}
