package factory

import javax.sql.DataSource
import org.springframework.beans.factory.FactoryBean
import play.api.Play.current
import play.api.db.DB

class DataSourceFactoryBean extends FactoryBean[DataSource]{
  
  def getObject: DataSource = {
    DB.getDataSource()
  }

  def getObjectType: Class[_] = classOf[DataSource]

  def isSingleton: Boolean = false
}
