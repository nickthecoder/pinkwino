package uk.co.nickthecoder.pinkwino.util;

import java.util.HashMap;

import uk.co.nickthecoder.pinkwino.WikiConstants;

public class BooleanParameterDescription extends ParameterDescription
{

    public static HashMap<String,Boolean> valuesMap;
    
    static {
        valuesMap = new HashMap<String,Boolean>();
        valuesMap.put("true", true );
        valuesMap.put("1", true );
        valuesMap.put("yes", true );
    }
    
    public BooleanParameterDescription(String name)
    {
        this(name, false, "false", WikiConstants.USAGE_GENERAL);
    }

    public BooleanParameterDescription(String name, boolean required, String defaultValue, String usage)
    {
        super(name, required, defaultValue, usage);
    }

    public Parameter allowValue(String requestedValue)
    {
        if ( valuesMap.containsKey(requestedValue) ) {
            return new BooleanParameter( this, valuesMap.get( requestedValue ) );
        } else {
            return null;
        }
    }

    public String getSummary()
    {
        return "boolean";
    }

    public ParameterDescription required()
    {
        return new BooleanParameterDescription(getName(), true, getDefaultValue(), getUsage());
    }

    public ParameterDescription defaultValue(String defaultValue)
    {
        return new BooleanParameterDescription(getName(), getRequired(), defaultValue, getUsage());
    }
}
