/*
 * Copyright (c) 2021 the Newtypes contributors.
 * See the project homepage at: https://newtypes.monix.io/
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

package monix.newtypes

private[newtypes] trait NewEncodingK[Src[_]] {
  type Type[A]

  extension [A](self: Type[A]) {
    inline final def value: Src[A] = self.asInstanceOf[Src[A]]
  }

  protected inline final def unsafeBuild[A](value: Src[A]): Type[A] =
    value.asInstanceOf[Type[A]]

  protected inline final def derive[F[_], A](implicit ev: F[Src[A]]): F[Type[A]] =
    ev.asInstanceOf[F[Type[A]]]

  protected inline final def deriveK[F[_[_]]](implicit ev: F[Src]): F[Type] =
    ev.asInstanceOf[F[Type]]

  implicit final def extractor[A]: NewExtractor.Aux[Type[A], Src[A]] =
    new NewExtractor[Type[A]] {
      type Source = Src[A]
      def extract(value: Type[A]) = value.value
    }
}

private[newtypes] trait NewtypeTraitK[Src[_]] extends NewEncodingK[Src] { 
  override opaque type Type[A] = Src[A]
}

private[newtypes] trait NewtypeCovariantTraitK[Src[+_]] extends NewEncodingK[Src] {
  override opaque type Type[+A] = Src[A]
}

private[newtypes] trait NewsubtypeTraitK[Src[_]] extends NewEncodingK[Src] { 
  override opaque type Type[A] <: Src[A] = Src[A]
}

private[newtypes] trait NewsubtypeCovariantTraitK[Src[+_]] extends NewEncodingK[Src] {
  override opaque type Type[+A] <: Src[A] = Src[A]
}