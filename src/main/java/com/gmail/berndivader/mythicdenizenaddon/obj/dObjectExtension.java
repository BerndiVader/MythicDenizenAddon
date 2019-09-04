package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;

public 
class 
dObjectExtension
implements
Property
{

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return getClass().getSimpleName();
    }

    @Override
    public String getAttribute(Attribute attribute) {
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }

}