package controllers.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;

/**
 * Created by lu.kun on 2015/4/12.
 */
public class MockCsvAvroLog {

    public static String schemaStr;
    public static String csvLog;
    public static AvroSchemaWrapper avroSchemaWrapper;
    public static GenericData.Record record;

    static {
            schemaStr = "{\n" +
                    "  \"namespace\": \"com.bestv.bdp.schema\",\n" +
                    "  \"name\": \"OTT_TPLAY\",\n" +
                    "  \"type\": \"record\",\n" +
                    "  \"fields\": [\n" +
                    "    {\n" +
                    "      \"name\": \"header\",\n" +
                    "      \"type\": {\n" +
                    "        \"name\": \"header_record\",\n" +
                    "        \"type\": \"record\",\n" +
                    "        \"fields\": [\n" +
                    "          {\n" +
                    "            \"name\": \"version\",\n" +
                    "            \"type\": \"int\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"server\",\n" +
                    "            \"type\": \"string\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"service\",\n" +
                    "            \"type\": \"string\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"timestamp\",\n" +
                    "            \"type\": \"long\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"username\",\n" +
                    "            \"type\": [\n" +
                    "              \"null\",\n" +
                    "              \"string\"\n" +
                    "            ],\n" +
                    "            \"default\": null\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"name\": \"transaction\",\n" +
                    "            \"type\": [\n" +
                    "              \"null\",\n" +
                    "              \"string\"\n" +
                    "            ],\n" +
                    "            \"default\": null\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"_version\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"_host\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"_service\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"_domain\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"log_version\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"log_type\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"firewareversion\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"userid\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"use_string\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"itemcode\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"veidoclipcode\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"begintime\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"endtime\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"pausesumtime\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"pausecount\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"firstloadinttime\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"loadingtime\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"loadingcount\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"downavgrate\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"downmaxrate\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"downminrate\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"downmaxshake\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"action\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"errorcode\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"taskid\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"loadingtype\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"media_detail_code\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"media_file_code\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"format\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"bitrate\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"etl_time\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"playavgrate\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"playmaxrate\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"playminrate\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"playrateshake\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"playrateshakecount\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"nettype\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"cdnsource\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"appcode\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"playtype\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"int\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"channelno\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"start_duration\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"ssid\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"categroycode\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"recid\",\n" +
                    "      \"type\": [\n" +
                    "        \"null\",\n" +
                    "        \"string\"\n" +
                    "      ],\n" +
                    "      \"default\": null\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}\n";
        avroSchemaWrapper = new AvroSchemaWrapper(Schema.parse(schemaStr));
        CsvAvroParser csvAvroParser = new CsvAvroParser(avroSchemaWrapper, "\\|", "OTT_");
            csvLog = "V1|logclt01.ott.jsdx.bestv.com.cn|TPLAY|JSDX_YUEME|001|4|BesTV_OS_FJDXPATCH_2.3.0.10|023263000018281|2015/4/8 0:11:51|1951575|4099283$9378281|2015/4/7 23:30:09|2015/4/8 0:11:51|0|0|3|0|0|277|398|220|31|2||ps01.ott.fjdx.bestv.com.cn_1428420614334_023263000018281|1|9378281|4099283|MP4_HLS|700|2015/4/8|272|280|158|29|3|1|110.84.133.9:8089|||||||";

            try {
            record = csvAvroParser.parse(csvLog);
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConvertException e) {
            e.printStackTrace();
        }
    }

}
