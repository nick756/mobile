package com.nova.sme.sme01.miscellanea;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by User on 19.08.2015.
 */
public class ButtonsSupport implements Serializable {
    public Vector<Integer> positions = new Vector<Integer>();
    public int             selected;

    public ButtonsSupport() {

    }

}
