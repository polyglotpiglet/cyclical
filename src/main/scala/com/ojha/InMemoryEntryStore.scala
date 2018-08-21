package com.ojha

import com.ojha.model.Entry

class InMemoryEntryStore extends Store[Entry] {

  private var data: List[Entry] = List()

  override def put(t: Entry): Unit = data = data.+:(t)

  override def getAll: Seq[Entry] =  data
}
