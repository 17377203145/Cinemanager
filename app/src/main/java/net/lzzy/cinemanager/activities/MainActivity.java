package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.CinemasFragment;
import net.lzzy.cinemanager.fragments.OrdersFragment;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FragmentManager manager=getSupportFragmentManager();


    private LinearLayout layoutMenu;
    private TextView tvTitle;
    private SearchView search;
    private TextView textView;
    private SparseArray<String>titleArry=new SparseArray<>();
    private SparseArray<Fragment>FragmentArray=new SparseArray<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 去掉标题栏 **/
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();
    }


    /** 自定义标题栏 **/
    private void setTitleMenu() {
        layoutMenu = findViewById(R.id.bar_title_layout_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(R.id.bar_title_img_menu).setOnClickListener(v -> {
            int visible=layoutMenu.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE;
            layoutMenu.setVisibility(visible);
        });
        tvTitle = findViewById(R.id.bar_title_tv_title);
        tvTitle.setText(R.string.bar_title_menu_orders);
        search = findViewById(R.id.main_sv_search);
        findViewById(R.id.bar_title_tv_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_exit).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_exit).setOnClickListener(v -> {
            System.exit(0); });
    }


    /** 对标题栏的点击监听 **/
    @Override
    public void onClick(View v) {
        layoutMenu.setVisibility(View.GONE);
        tvTitle.setText(titleArry.get(v.getId()));
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = FragmentArray.get(v.getId());
        if (fragment == null) {
            fragment = createFragment(v.getId());
            FragmentArray.put(v.getId(), fragment);
            transaction.add(R.id.fragment_container, fragment);
        }
        for (Fragment f : manager.getFragments()) {
            transaction.hide(f);
        }
        transaction.show(fragment).commit();
    }
    private Fragment createFragment(int id){

        switch (id) {
            case R.id.bar_title_tv_add_cinema:
            break;

            case R.id.bar_title_tv_view_cinema:
               return new CinemasFragment();

            case R.id.bar_title_tv_add_order:
                break;

            case R.id.bar_title_tv_view_order:
                return new OrdersFragment();
            default:
                break;
        }
        return null;
    }
}
