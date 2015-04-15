package controllers

import javax.inject.{Singleton, Inject}
import com.bestv.bi.schemaclient.AvroRESTCacheRepositoryClient
import controllers.avro.AvroSchemaWrapper
import org.apache.avro.Schema
import play.api.libs.json.{JsObject, JsArray, JsValue, Json}
import services.UUIDGenerator
import org.slf4j.{LoggerFactory, Logger}
import play.api.mvc._

import scala.collection.mutable

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

  val c1 = new AvroRESTCacheRepositoryClient("http://10.50.122.67:2876/schema-repo/");

  def getSchemaNames = Action {
    import scala.collection.JavaConversions._
    val jsonArray = Json.toJson(c1.subjectNames().toSeq)
    println(jsonArray);
    Ok(jsonArray)
  }

  def getVersionsBySchemaName(schemaName: String) = Action {
    import scala.collection.JavaConversions._
    val subjectEntry = c1.lookupAll(schemaName);
    val jsonArray = Json.toJson(subjectEntry.map(_.getId))
    println(jsonArray)
    Ok(jsonArray)
  }

  def getSchema(schemaName: String, version: String) = Action {
    import scala.collection.JavaConversions._
    val schema = c1.lookupLatest(schemaName).getSchema
    val schemaWrapper = new AvroSchemaWrapper(schema);

    var schemaFields: Seq[Map[String, String]] = null

    schemaFields = schemaWrapper.getFieldList().map((x) => {
      if (x.getRawField.defaultValue() == null)
        Map("name" -> x.getName, "type" -> x.getRawField.schema().toString())
      else
        Map("name" -> x.getName, "type" -> x.getRawField.schema().toString(), "defaultval" -> x.getRawField.defaultValue().toString)
      })

    if (schemaWrapper.getHeader() != null) {
      schemaFields = Map("name" -> schemaWrapper.getHeader().getName, "type" -> schemaWrapper.getHeader().getRawField.schema().toString()) +: schemaFields
    }

    val jsonArray = Json.toJson(schemaFields)
    println(schemaFields)
    Ok(jsonArray)
  }

  def newSchema(schemaName: String) = Action { request =>
    val body = request.body
    val bodyJs = body.asJson.get

    try {
      val fields = bodyJs.as[JsArray].value.map((x) => {
        var part = "{\"name\":\"" + (x \ "name").as[String] + "\",\"type\":" + (x \ "type").as[String]
        if ((x \\ "defaultval").size > 0)
          part = part + ",\"default\":" + (x \ "defaultval").as[String]
        part + "}"
      })
      val schemaStr = "{\"namespace\":\"com.bestv.bdp.schema\",\"name\":\"" + schemaName + "\",\"type\":\"record\",\"fields\":[" +
        fields.mkString(",") +
        "]}"

      println(schemaStr)
      Schema.parse(schemaStr)
      c1.registerSchema(schemaName, schemaStr)

      println(c1.lookupLatest(schemaName).getId)

      val s1 = Map(
        "type" -> "success",
        "msg" -> "提交成功！",
        "versionid" -> c1.lookupLatest(schemaName).getId
      )
      val jsonArray = Json.toJson(s1)
      Ok(jsonArray)
    } catch {
      case ex: Exception => {
        val s1 = Map(
          "type" -> "alert",
          "msg" -> "提交失败！"
        )
        val jsonArray = Json.toJson(s1)
        BadRequest(jsonArray)
      }
    }
  }

}
