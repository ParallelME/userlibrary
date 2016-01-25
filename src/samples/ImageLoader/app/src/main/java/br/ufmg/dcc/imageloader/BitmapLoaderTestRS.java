package br.ufmg.dcc.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import android.support.v8.renderscript.*;

public class BitmapLoaderTestRS {
    public Bitmap load(Resources res, int resource) {
        RenderScript mRS = RenderScript.create(MainActivity.getAppContext());
Allocation arrayIn, arrayOperational;
Type arrayDataType;
ScriptC_function0 function0_script = new ScriptC_function0(mRS);
ScriptC_function1 function1_script = new ScriptC_function1(mRS);
ScriptC_function2 function2_script = new ScriptC_function2(mRS);
ScriptC_function3 function3_script = new ScriptC_function3(mRS);
BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(res, resource, options);

        ;
        arrayIn = Allocation.createFromBitmap(mRS, bitmap,
	Allocation.MipmapControl.MIPMAP_NONE,
	Allocation.USAGE_SCRIPT | Allocation.USAGE_SHARED);
arrayDataType = new Type.Builder(mRS, Element.F32_3(mRS)).setX(arrayIn.getType().getX()).setY(arrayIn.getType().getY()).create();
arrayOperational = Allocation.createTyped(mRS, arrayDataType);
function0_script.forEach_root(arrayIn, arrayOperational);; 
        
        function1_script.forEach_root(arrayOperational, arrayOperational);;
        
        function2_script.forEach_root(arrayOperational, arrayOperational);;
        function3_script.forEach_root(arrayOperational, arrayIn);
arrayIn.copyTo(bitmap);
; 

        return bitmap;
    }
}
