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

import javax.inject

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import uk.gov.hmrc.brm.audit.{BRMDownstreamAPIAudit, NorthernIrelandAudit, ScotlandAudit}
import uk.gov.hmrc.play.audit.http.config.LoadAuditingConfig
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.config.{AppName, ControllerConfig, RunMode, ServicesConfig}
import uk.gov.hmrc.play.http.HttpPost
import uk.gov.hmrc.play.http.hooks.HttpHook
import uk.gov.hmrc.play.http.ws._

@inject.Singleton
class WSHttp extends WSGet with WSPut with WSPost with WSDelete with WSPatch with AppName {
  override val hooks: Seq[HttpHook] = NoneRequired
}

@inject.Singleton
class MicroserviceAuditConnector extends AuditConnector with RunMode {
  override lazy val auditingConfig = LoadAuditingConfig(s"auditing")
}

class GuiceModule() extends AbstractModule with ServicesConfig {

  def configure() : Unit = {
    bind(classOf[HttpPost]).to(classOf[WSHttp])
    bind(classOf[AuditConnector]).to(classOf[MicroserviceAuditConnector])
    bind(classOf[AuditConnector]).to(classOf[MicroserviceAuditConnector])
    bind(classOf[ControllerConfig]).to(classOf[ControllerConfiguration])
//    bind(classOf[ServicesConfig]).toInstance(MicroserviceGlobal)

    bind(classOf[BRMDownstreamAPIAudit]).annotatedWith(Names.named("ni-auditor"))
      .to(classOf[NorthernIrelandAudit])
    bind(classOf[BRMDownstreamAPIAudit]).annotatedWith(Names.named("nrs-auditor"))
      .to(classOf[ScotlandAudit])
  }

}
