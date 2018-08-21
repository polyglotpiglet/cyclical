package com.ojha

trait Store[T] {

  def put(t: T): Unit

  def getAll: Seq[T]



}
