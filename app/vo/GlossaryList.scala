package vo

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonRootName, JsonUnwrapped}
import models.impl.Glossary
import org.springframework.data.domain.Page

/**
 * This simple wrapper for ignoring some properties of org.springframework.data.domain.Page, f.e. "iterator"
 */
@JsonRootName("result")
@JsonIgnoreProperties(Array("iterator"))
case class GlossaryList(@JsonUnwrapped page: Page[Glossary])