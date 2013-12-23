package factory

import org.springframework.beans.factory.FactoryBean
import javax.sql.DataSource
import play.api.db.DB
import play.api.Play.current

class DataSourceFactoryBean extends FactoryBean[DataSource]{
  
  def getObject: DataSource = {
    DB.getDataSource()
  }

  def getObjectType: Class[_] = classOf[DataSource]

  def isSingleton: Boolean = false
}
