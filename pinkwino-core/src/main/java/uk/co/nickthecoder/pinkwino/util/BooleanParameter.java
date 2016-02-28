package uk.co.nickthecoder.pinkwino.util;

public class BooleanParameter extends Parameter
{
    private boolean _booleanValue;

    public BooleanParameter(ParameterDescription parameterDescription, boolean value)
    {
        super(parameterDescription, Boolean.toString(value));

        _booleanValue = value;
    }

    public boolean getBooleanValue()
    {
        return _booleanValue;
    }
}
