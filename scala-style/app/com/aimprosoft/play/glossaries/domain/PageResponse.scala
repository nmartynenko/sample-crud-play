package com.aimprosoft.play.glossaries.domain

import com.aimprosoft.play.glossaries.models.Glossary

//domain generic class for pageable requests
class PageResponse[T](val content: Seq[T],
                      val startRow: Int = 0,
                      val pageSize: Int = 0,
                      val totalElements: Int)

//concrete class for pageable requests
case class GlossaryPageResponse(override val content: Seq[Glossary],
                                override val startRow: Int = 0,
                                override val pageSize: Int = 0,
                                override val totalElements: Int)
  extends PageResponse[Glossary](content, startRow, pageSize, totalElements)