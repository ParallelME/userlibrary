package br.ufmg.dcc.tonemapreinhard.formats;

/**
 * @author Wilson de Carvalho
 */
public class RGB implements Cloneable {
    public float red = 0;
    public float green = 0;
    public float blue = 0;

    public RGB clone() {
        try {
            return (RGB) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
