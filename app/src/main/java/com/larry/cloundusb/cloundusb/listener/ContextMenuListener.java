package com.larry.cloundusb.cloundusb.listener;

import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.larry.cloundusb.R;

/**
 * Created by Larry on 6/17/2016.
 */
public class ContextMenuListener implements View.OnCreateContextMenuListener {
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 0, 1, R.string.copy);
        menu.add(0, 1, 2, R.string.paste);
        menu.add(0, 2, 3, R.string.rename);
        menu.add(0, 3, 4, R.string.delete);
        menu.add(0, 4, 5, R.string.property);

    }

    // @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //info.id得到listview中选择的条目绑定的id
        String id = String.valueOf(info.id);
        switch (item.getItemId()) {
            case 0:

                return true;
            case 1:

                return true;
            default:
                //    return super.onContextItemSelected(item);
        }
         return true;

    }
}
