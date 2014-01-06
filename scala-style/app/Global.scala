import play.api.{Logger, Application, GlobalSettings}
import samples._

object Global extends GlobalSettings{

  override def onStart(app: Application): Unit = {
    Logger.info("Pre-fill some data")

    //it might be better to improve scanning
    val listeners = List(CreateAdminListener, CreateUserListener, CreateGlossaryDataListener)
    for(listener <- listeners){
      listener.init()
    }

    Logger.info("Initialization has ended")
  }
}
