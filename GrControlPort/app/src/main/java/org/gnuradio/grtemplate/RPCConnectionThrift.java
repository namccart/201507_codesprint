package org.gnuradio.grtemplate;

import org.gnuradio.controlport.BaseTypes;
import org.gnuradio.controlport.Knob;
import org.gnuradio.controlport.KnobProp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by trondeau on 7/22/15.
 */
public class RPCConnectionThrift {

    private ThriftRadioClient client;

    public class KnobInfo {
        public String key;
        public Object value;
        public BaseTypes ktype;

        KnobInfo(String _key, Object _value, BaseTypes _ktype) {
            key = _key;
            value = _value;
            ktype = _ktype;
        }
    }

    public RPCConnectionThrift(String host, Integer port)
    {
        // Get port from prefs

        newConnection(host, port);
    }

    private void newConnection(String host, Integer port) {
        client = new ThriftRadioClient(host, port);
    }

    private KnobInfo unpackKnob(String key, Knob knob) {
        KnobInfo x;

        if(knob.getType() == BaseTypes.BOOL) {
            x = new KnobInfo(key, knob.getValue().a_bool, BaseTypes.BOOL);
        }
        else if(knob.getType() == BaseTypes.BYTE) {
            x = new KnobInfo(key, knob.getValue().a_byte, BaseTypes.BYTE);
        }
        else if(knob.getType() == BaseTypes.SHORT) {
            x = new KnobInfo(key, knob.getValue().a_short, BaseTypes.SHORT);
        }
        else if(knob.getType() == BaseTypes.INT) {
            x = new KnobInfo(key, knob.getValue().a_int, BaseTypes.INT);
        }
        else if(knob.getType() == BaseTypes.LONG) {
            x = new KnobInfo(key, knob.getValue().a_long, BaseTypes.LONG);
        }
        else if(knob.getType() == BaseTypes.DOUBLE) {
            x = new KnobInfo(key, knob.getValue().a_double, BaseTypes.DOUBLE);
        }
        else if(knob.getType() == BaseTypes.STRING) {
            x = new KnobInfo(key, knob.getValue().a_string, BaseTypes.STRING);
        }
        else if(knob.getType() == BaseTypes.COMPLEX) {
            x = new KnobInfo(key, knob.getValue().a_complex, BaseTypes.COMPLEX);
        }
        else if(knob.getType() == BaseTypes.F32VECTOR) {
            x = new KnobInfo(key, knob.getValue().a_f32vector, BaseTypes.F32VECTOR);
        }
        else if(knob.getType() == BaseTypes.F64VECTOR) {
            x = new KnobInfo(key, knob.getValue().a_f64vector, BaseTypes.F64VECTOR);
        }
        else if(knob.getType() == BaseTypes.S64VECTOR) {
            x = new KnobInfo(key, knob.getValue().a_s64vector, BaseTypes.S64VECTOR);
        }
        else if(knob.getType() == BaseTypes.S32VECTOR) {
            x = new KnobInfo(key, knob.getValue().a_s32vector, BaseTypes.S32VECTOR);
        }
        else if(knob.getType() == BaseTypes.S16VECTOR) {
            x = new KnobInfo(key, knob.getValue().a_s16vector, BaseTypes.S16VECTOR);
        }
        else if(knob.getType() == BaseTypes.S8VECTOR) {
            x = new KnobInfo(key, knob.getValue().a_s8vector, BaseTypes.S8VECTOR);
        }
        else if(knob.getType() == BaseTypes.C32VECTOR) {
            x = new KnobInfo(key, knob.getValue().a_c32vector, BaseTypes.C32VECTOR);
        }
        else {
            throw new RuntimeException("Unknown Knob Base Type.");
        }
        return x;
    }

    public Map<String, KnobProp> properties(List<String> args) {
        Map<String, KnobProp> knobprops = new HashMap<String, KnobProp>();
        try {
            knobprops = client.getRadio().properties(args);
            for (Map.Entry<String, KnobProp> entry : knobprops.entrySet()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return knobprops;
    }

    public Map<String, KnobInfo> getKnobs(List<String> args)
    {
        Map<String, KnobInfo> results = new HashMap<String, KnobInfo>();
        try {
            for (Map.Entry<String, Knob> entry : client.getRadio().getKnobs(args).entrySet()) {
                results.put(entry.getKey(), unpackKnob(entry.getKey(), entry.getValue()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public Map<String, KnobInfo>  getRe(List<String> args)
    {
        Map<String, KnobInfo> results = new HashMap<String, KnobInfo>();
        try {
            for (Map.Entry<String, Knob> entry : client.getRadio().getRe(args).entrySet()) {
                results.put(entry.getKey(), unpackKnob(entry.getKey(), entry.getValue()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
