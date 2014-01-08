package domain

import models.Glossary

//domain generic class for pageable requests
class PageResponse[T](content: Seq[T], startRow: Int = 0, pageSize: Int = 0,
                      totalElements: Int)

//concrete class for pageable requests
case class GlossaryPageResponse(content: Seq[Glossary],
                                startRow: Int = 0,
                                pageSize: Int = 0,
                                totalElements: Int)
  extends PageResponse[Glossary](content, startRow, pageSize, totalElements)