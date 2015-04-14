package controllers

import javax.inject.{Singleton, Inject}
import com.bestv.bi.schemaclient.AvroRESTCacheRepositoryClient
import controllers.avro.AvroSchemaWrapper
import org.apache.avro.Schema
import play.api.libs.json.{JsValue, Json}
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
    val jsonArray = Json.toJson(c1.subjects().map(_.getName))
    println(jsonArray);
    Ok(jsonArray)
  }

  def getVersionsBySchemaName(schemaName: String) = Action {
    import scala.collection.JavaConversions._
    val subjectEntry = c1.subjects().filter(_.getName.equals(schemaName)).head;
    val jsonArray = Json.toJson(subjectEntry.allEntries().map(_.getId))
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
    val nm = (bodyJs \\ "name").map("\"name\":\"" + _.as[String] + "\"")
    val tp = (bodyJs \\ "type").map("\"type\":" +_.as[String])
    val dv = (bodyJs \\ "defaultval").map("\"default\":" + _.as[String])
    val fields = ((nm zip tp).map((x) => {
      "{" +
        x._1 + "," + x._2
    }) zip dv).map((x) => {
      if (x._2 != null) {
        x._1 + "," + x._2 + "}"
      } else {
        x._1 + "}"
      }
    })

    val schemaStr = "{\"namespace\":\"com.bestv.bdp.schema\",\"name\":\"" + schemaName + "\",\"type\":\"record\",\"fields\":\"" +
      fields.mkString(",") +
      "}"

    println(schemaStr)

    val s1 = Map(
      "type" -> "success",
      "msg" -> "提交成功！"
    )
    val jsonArray = Json.toJson(s1)
    Ok(jsonArray)
  }

}
