/**
 * Copyright 2011-2013 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.core

import io.gatling.core.validation.{ FailureWrapper, SuccessWrapper, Validation }

package object check {

	type Preparer[R, P] = R => Validation[P]

	trait Extractor[P, T, X] {
		def name: String
		def apply(prepared: P, criterion: T): Validation[Option[X]]
	}

	type CheckFactory[C <: Check[R], R] = Check[R] => C
}