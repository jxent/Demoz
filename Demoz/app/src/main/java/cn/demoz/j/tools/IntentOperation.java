package cn.demoz.j.tools;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cn.demoz.j.demoactivity.HugeImageViewActivity;

public class IntentOperation {

    public static void toDemosFragmentOperation(Context context, int key){

        switch (key){
            case 1001:
                context.startActivity(new Intent(UiUtils.getContext(), HugeImageViewActivity.class));
                break;
            case 1002:
                Toast.makeText(context, key+"", Toast.LENGTH_SHORT).show();
                break;
            case 1003:
                Toast.makeText(context, key+"", Toast.LENGTH_SHORT).show();
                break;
            case 1004:
                Toast.makeText(context, key+"", Toast.LENGTH_SHORT).show();
                break;
            case 1005:
                Toast.makeText(context, key+"", Toast.LENGTH_SHORT).show();
                break;
            case 1006 :
                Toast.makeText(context, key+"", Toast.LENGTH_SHORT).show();
                break;
            default:

                break;
        }

    }
}
