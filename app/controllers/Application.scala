package controllers

import javax.inject.{Singleton, Inject}
import play.api.libs.json.Json
import services.UUIDGenerator
import org.slf4j.{LoggerFactory, Logger}
import play.api.mvc._

/**
 * Instead of declaring an object of Application as per the template project, we must declare a class given that
 * the application context is going to be responsible for creating it and wiring it up with the UUID generator service.
 * @param uuidGenerator the UUID generator service we wish to receive.
 */
@Singleton
class Application @Inject() (uuidGenerator: UUIDGenerator) extends Controller {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])

  def index = Action {
    logger.info("Serving index page...")
    Ok(views.html.index())
  }

  def randomUUID = Action {
    logger.info("calling UUIDGenerator...")
    Ok(uuidGenerator.generate.toString)
  }

  def starter = Action {
    Ok(views.html.starter())
  }

  def getSchemaNames = Action {
    val s1 = Seq(
      "s1",
      "s2",
      "s3"
    )
    val jsonArray = Json.toJson(s1)
    Ok(jsonArray)
  }

  def getVersionsBySchemaName(schemaName: String) = Action {
    val s1 = Seq(
      "-1",
      "-2",
      "-3"
    )
    val s2 = s1.map( schemaName + _ )
    val jsonArray = Json.toJson(s2)
    Ok(jsonArray)
  }

  def getSchema(schemaName: String, version: String) = Action {
    val s1 = Seq(
      Map(
        "name" -> "header",
        "type" -> "record"
      ),
      Map(
        "name" -> "userid",
        "type" -> "string"
      ),
      Map(
        "name" -> "starttime",
        "type" -> "string"
      )
    )
    val jsonArray = Json.toJson(s1)
    Ok(jsonArray)
  }

  def newSchema(schemaName: String) = Action { request =>
    val body = request.body
    logger.info(body.toString)
    val s1 = Map(
      "type" -> "success",
      "msg" -> "提交成功！"
    )
    val jsonArray = Json.toJson(s1)
    Ok(jsonArray)
  }

}
