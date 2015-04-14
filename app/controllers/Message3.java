package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.avro.AvroFieldWrapper;
import controllers.avro.AvroSchemaWrapper;
import controllers.avro.MockCsvAvroLog;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.WebSocket;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lu.kun on 2015/4/8.
 */
public class Message3 extends Controller {

    public static WebSocket<JsonNode> socket() {



        return new WebSocket<JsonNode>() {
            @Override
            public void onReady(In<JsonNode> in, final Out<JsonNode> out) {

                in.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                        System.out.println(jsonNode.toString());
                    }
                });

                Akka.system().scheduler().schedule(
                        Duration.create(0, TimeUnit.SECONDS),
                        Duration.create(10, TimeUnit.SECONDS),
                        new Runnable() {
                            private int cnt = 0;
                            @Override
                            public void run() {
                                Map<String, String> innerMap = new HashMap<String, String>();
                                Map<String, List<Map<String, String>>> innerMessageMap = new HashMap<String, List<Map<String, String>>>();
                                innerMap.put("event", "message");
                                AvroSchemaWrapper schema = MockCsvAvroLog.avroSchemaWrapper;
                                GenericData.Record record = MockCsvAvroLog.record;
                                // 获得头信息(Label)
                                List<Map<String, String>> headerLabelList = new ArrayList<Map<String, String>>();
                                Map<String, String> headerLabelMap1 = new HashMap<String, String>();
                                Map<String, String> headerLabelMap2 = new HashMap<String, String>();
                                Map<String, String> headerLabelMap3 = new HashMap<String, String>();
                                Map<String, String> headerLabelMap4 = new HashMap<String, String>();
                                headerLabelMap1.put("displayName", "版本");
                                headerLabelMap1.put("field", "version");
                                headerLabelList.add(headerLabelMap1);
                                headerLabelMap2.put("displayName", "主题");
                                headerLabelMap2.put("field", "service");
                                headerLabelList.add(headerLabelMap2);
                                headerLabelMap3.put("displayName", "服务器");
                                headerLabelMap3.put("field", "server");
                                headerLabelList.add(headerLabelMap3);
                                headerLabelMap4.put("displayName", "时间戳");
                                headerLabelMap4.put("field", "timestamp");
                                headerLabelList.add(headerLabelMap4);
                                innerMessageMap.put("headerlabel", headerLabelList);
                                // 构建头信息
                                List<Map<String, String>> headerDataList = new ArrayList<Map<String, String>>();
                                Map<String, String> headerMap = new HashMap<String, String>();
                                headerMap.put("version", ((GenericData.Record)MockCsvAvroLog.record.get("header")).get("version").toString());
                                headerMap.put("server", ((GenericData.Record)MockCsvAvroLog.record.get("header")).get("server").toString());
                                headerMap.put("service", ((GenericData.Record)MockCsvAvroLog.record.get("header")).get("service").toString());
                                headerMap.put("timestamp", ((GenericData.Record)MockCsvAvroLog.record.get("header")).get("timestamp").toString());
                                headerDataList.add(headerMap);
                                innerMessageMap.put("headerdata", headerDataList);
                                // 构建数据Label信息
                                List<Map<String, String>> dataLabelList = new ArrayList<Map<String, String>>();
                                Map<String, String> dataLabelMap1 = new HashMap<String, String>();
                                Map<String, String> dataLabelMap2 = new HashMap<String, String>();
                                dataLabelMap1.put("displayName", "字段");
                                dataLabelMap1.put("field", "field");
                                dataLabelList.add(dataLabelMap1);
                                dataLabelMap2.put("displayName", "值");
                                dataLabelMap2.put("field", "value");
                                dataLabelList.add(dataLabelMap2);
                                innerMessageMap.put("datalabel", dataLabelList);
                                // 构建数据信息
                                List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
                                for(AvroFieldWrapper field : MockCsvAvroLog.avroSchemaWrapper.getFieldList()) {
                                    Map<String, String> dataMap = new HashMap<String, String>();
                                    String fieldName = field.getName();
                                    String fieldValue = field.convertToString(MockCsvAvroLog.record.get(field.getName()));
                                    dataMap.put("field", fieldName);
                                    dataMap.put("value", fieldValue);
                                    dataList.add(dataMap);
                                }
                                innerMessageMap.put("data", dataList);
                                // Json --> String
                                String message = Json.toJson(innerMessageMap).toString();
                                // 输出数据信息
                                innerMap.put("data", message);

                                try {
                                    System.out.println(innerMap.toString());
                                    out.write(Json.toJson(innerMap));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        Akka.system().dispatcher()
                );

            }
        };
    }

}
