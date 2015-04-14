package controllers.avro;

import org.apache.avro.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lu.kun on 2015/4/8.
 */
public class AvroFieldWrapper {

    private String name;
    private List<Schema.Type> valueTypes;
    private Schema.Field rawField;

    public AvroFieldWrapper() {
        this.name = "";
        this.valueTypes = new ArrayList<Schema.Type>();
        this.rawField = null;
    }

    public AvroFieldWrapper(Schema.Field field) {
        this.name = field.name();

        Schema fieldSchema = field.schema();
        this.valueTypes = new ArrayList<Schema.Type>();

        if (fieldSchema.getType().equals(Schema.Type.UNION)) {
            for (Schema schema : fieldSchema.getTypes()) {
                if (!schema.getType().equals(Schema.Type.NULL)) {
                    this.valueTypes.add(schema.getType());
                }
            }
        } else {
            this.valueTypes.add(fieldSchema.getType());
        }

        this.rawField = field;
    }

    public String convertToString(Object fieldValue) {
        if (fieldValue == null) {
            return null;
        }

        return fieldValue.toString();
    }

    public Object convertFromString(String fieldValue) throws ConvertException {
        if (fieldValue == null) {
            return fieldValue;
        }

        if (this.valueTypes.size() > 1) {
            throw new ConvertException("too many types to convert to");
        }

        Schema.Type fieldType = this.valueTypes.get(0);
        switch (fieldType) {
            case INT:
                return Integer.valueOf(fieldValue);
            case LONG:
                return Long.valueOf(fieldValue);
            case FLOAT:
                return Float.valueOf(fieldValue);
            case DOUBLE:
                return Double.valueOf(fieldValue);
            case STRING:
                return fieldValue;
            default:
                throw new ConvertException("can not convert to type " + fieldType.getName());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Schema.Type> getValueTypes() {
        return valueTypes;
    }

    public void setValueTypes(List<Schema.Type> valueType) {
        this.valueTypes = valueType;
    }

    public Schema.Field getRawField() {
        return rawField;
    }

    public void setRawField(Schema.Field rawField) {
        this.rawField = rawField;
    }
}
