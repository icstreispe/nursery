package ro.ics.neural;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X13 on 10.02.2017.
 */
public class Net {
    List<Float> i1 = new ArrayList();
    List<Float> i2 = new ArrayList();
    List<Float> o = new ArrayList();
    float w1, w2;


    public void learn(float i1, float i2, float o){
        this.i1.add(i1);
        this.i2.add(i2);
        this.o.add(o);
    }

    public boolean output (float i1, float i2){
        return false;
    }
}
