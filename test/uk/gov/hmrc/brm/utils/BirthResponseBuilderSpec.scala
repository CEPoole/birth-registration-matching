/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.brm.utils

import uk.gov.hmrc.play.test.UnitSpec

/**
  * Created by user on 23/08/16.
  */
class BirthResponseBuilderSpec extends UnitSpec {


  "BirthResponseBuilder" should {
    "return validated true with Match" in {
      BirthResponseBuilder.withMatch().validated shouldBe true
    }

    "return validated false with Match" in {
      BirthResponseBuilder.withNoMatch().validated  shouldBe false
    }

    "return validated true with input value as true" in {
      BirthResponseBuilder.getResponse(true).validated  shouldBe true
    }
  }

}