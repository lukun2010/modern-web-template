package controllers.avro;

import org.apache.avro.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lu.kun on 2015/4/12.
 */
public class AvroSchemaWrapper {

    private List<AvroFieldWrapper> headList;
    private List<AvroFieldWrapper> fieldList;
    private AvroFieldWrapper header;
    private Schema rawSchema;
    private Schema headSchema;

    public AvroSchemaWrapper() {
        this.headList = null;
        this.fieldList = null;
        this.rawSchema = null;
        this.headSchema = null;
        this.header = null;
    }

    public AvroSchemaWrapper(Schema schema) {
        this.rawSchema = schema;
        this.headList = new ArrayList<AvroFieldWrapper>();
        this.fieldList = new ArrayList<AvroFieldWrapper>();
        List<Schema.Field> fields = this.rawSchema.getFields();
        for (int idx = 0; idx < fields.size(); idx++) {
            Schema.Field field = fields.get(idx);
            String fieldName = field.name();
            Schema.Type fieldType = field.schema().getType();
            if (fieldName.equals("header") && fieldType.equals(Schema.Type.RECORD)) {
                this.header = new AvroFieldWrapper(field);
                this.headSchema = field.schema();
                List<Schema.Field> headFields = field.schema().getFields();
                int headFieldsCnt = headFields.size();
                for (int i = 0; i < headFieldsCnt; i++) {
                    headList.add(new AvroFieldWrapper(headFields.get(i)));
                }
            } else {
                fieldList.add(new AvroFieldWrapper(fields.get(idx)));
            }
        }
    }

    public AvroFieldWrapper getHeader() {
        return header;
    }

    public void setHeader(AvroFieldWrapper header) {
        this.header = header;
    }

    public List<AvroFieldWrapper> getHeadList() {
        return headList;
    }

    public void setHeadList(List<AvroFieldWrapper> headList) {
        this.headList = headList;
    }

    public List<AvroFieldWrapper> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<AvroFieldWrapper> fieldList) {
        this.fieldList = fieldList;
    }

    public Schema getRawSchema() {
        return rawSchema;
    }

    public void setRawSchema(Schema rawSchema) {
        this.rawSchema = rawSchema;
    }

    public Schema getHeadSchema() {
        return headSchema;
    }

    public void setHeadSchema(Schema headSchema) {
        this.headSchema = headSchema;
    }
}
