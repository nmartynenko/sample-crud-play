package com.aimprosoft.play

import java.io.Closeable
import org.springframework.context.ApplicationContext

package object glossaries {
  type PlaySpringContext = ApplicationContext with Closeable
}
