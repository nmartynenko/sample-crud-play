package vo

import models.Glossary

//todo review
case class PageResponse(content: Seq[Glossary], startRow: Int = 0, pageSize: Int = 0)