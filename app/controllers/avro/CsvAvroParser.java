package controllers.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by lu.kun on 2015/4/7.
 */
public class CsvAvroParser {

    private String delimiter;
    private String topicPrefix;

    private AvroSchemaWrapper avroSchemaWrapper;

    public CsvAvroParser(AvroSchemaWrapper avroSchemaWrapper, String delimiter, String topicPrefix) {
        this.avroSchemaWrapper = avroSchemaWrapper;
        this.delimiter = delimiter;
        this.topicPrefix = topicPrefix;
    }

    public GenericData.Record parse(String log) throws ParserException, IOException, ConvertException {
        String[] logSplitted = log.split(this.delimiter);
        int logFieldsCnt = logSplitted.length;
        if (logFieldsCnt < 4) {
            throw new ParserException("below 4 fields");
        }

        Schema avroSchema = this.avroSchemaWrapper.getRawSchema();
        Schema headSchema = this.avroSchemaWrapper.getHeadSchema();

        EncoderFactory encoderFactory = EncoderFactory.get();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = encoderFactory.directBinaryEncoder(out, null);
        DatumWriter<GenericData.Record> writer = new GenericDatumWriter<GenericData.Record>(avroSchema);

        GenericData.Record record = new GenericData.Record(avroSchema);
        GenericData.Record header_record = new GenericData.Record(headSchema);

        // Header
        long curTimestamp = new Timestamp(System.currentTimeMillis()).getTime();
        header_record.put("version", 1);
        header_record.put("server", logSplitted[1]);
        header_record.put("service", this.topicPrefix + logSplitted[2]);
        header_record.put("timestamp", curTimestamp);
        record.put("header", header_record);

        // Body
        List<AvroFieldWrapper> fieldList = this.avroSchemaWrapper.getFieldList();
        for (int i = 0; i < logFieldsCnt; i++) {
            AvroFieldWrapper avroFieldWrapper = fieldList.get(i);
            record.put(avroFieldWrapper.getName(),
                    avroFieldWrapper.convertFromString(logSplitted[i]));
        }

        // Output
        writer.write(record, encoder);
        return record;
    }

}
