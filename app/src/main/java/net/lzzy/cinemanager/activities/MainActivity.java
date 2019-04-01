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
import net.lzzy.cinemanager.fragments.AddCinemasFragment;
import net.lzzy.cinemanager.fragments.AddOrdersFragment;
import net.lzzy.cinemanager.fragments.BaseFragment;
import net.lzzy.cinemanager.fragments.CinemasFragment;
import net.lzzy.cinemanager.fragments.OnFragmentInterctionListener;
import net.lzzy.cinemanager.fragments.OrdersFragment;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.utils.ViewUtils;

/**
 * @author Administrator
 */ //2.实现接口
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        OnFragmentInterctionListener, AddCinemasFragment.OnCinemasCreatedListener {
    private FragmentManager manager=getSupportFragmentManager();


    private LinearLayout layoutMenu;
    private TextView tvTitle;
    private SearchView search;
    private TextView textView;
    private SparseArray<String>titleArry=new SparseArray<>();
    private SparseArray<Fragment>fragmentArray=new SparseArray<>();
    public static final String EXTRA_CINEMA_ID="cinema";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 去掉标题栏 **/
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();
        search.setOnQueryTextListener(new ViewUtils.AbstractQueryHandler() {
            @Override
            public boolean handleQuery(String kw) {
                Fragment fragment=manager.findFragmentById(R.id.fragment_container);
                if (fragment!=null){
                    if (fragment instanceof BaseFragment){
                        ((BaseFragment)fragment).search(kw);
                    }
                }
                return true;
            }

        });
    }


    /** 自定义标题栏 **/
    private void setTitleMenu() {
        titleArry.put(R.id.bar_title_tv_add_cinema,"添加影院");
        titleArry.put(R.id.bar_title_tv_view_cinema,"影院列表");
        titleArry.put(R.id.bar_title_tv_add_order,"添加订单");
        titleArry.put(R.id.bar_title_tv_view_order,"订单列表");
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
        search.setVisibility(View.VISIBLE);
        layoutMenu.setVisibility(View.GONE);
        tvTitle.setText(titleArry.get(v.getId()));
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = fragmentArray.get(v.getId());
        if (fragment == null) {
            fragment = createFragment(v.getId());
            fragmentArray.put(v.getId(), fragment);
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
                return new AddCinemasFragment();

            case R.id.bar_title_tv_view_cinema:
               return new CinemasFragment();

            case R.id.bar_title_tv_add_order:
               return new AddOrdersFragment();

            case R.id.bar_title_tv_view_order:
                return new OrdersFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public void hidesSearch() {
        search.setVisibility(View.GONE);
    }

    @Override
    public void cancelAddCinema() {
        Fragment addCinemaFragment=fragmentArray.get(R.id.bar_title_tv_add_cinema);
        if (addCinemaFragment==null){
            return;
        }
        Fragment cinemasFragment=fragmentArray.get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction=manager.beginTransaction();
        if (cinemasFragment==null){
            cinemasFragment=new CinemasFragment();
            fragmentArray.put(R.id.bar_title_tv_view_cinema,cinemasFragment);
            transaction.add(R.id.fragment_container,cinemasFragment);
        }
        transaction.hide(addCinemaFragment).show(cinemasFragment ).commit();
        tvTitle.setText(titleArry.get(R.id.bar_title_tv_add_cinema));
    }

    @Override
    public void saveCineam(Cinema cinema) {
        Fragment addCinemaFragment=fragmentArray.get(R.id.bar_title_tv_add_cinema);
        if (addCinemaFragment==null){
            return;
        }
        Fragment cinemasFragment=fragmentArray.get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction=manager.beginTransaction();
        if (cinemasFragment==null){
            cinemasFragment=new CinemasFragment(cinema);
            fragmentArray.put(R.id.bar_title_tv_view_cinema,cinemasFragment);
            transaction.add(R.id.fragment_container,cinemasFragment);
        }else {
            ((CinemasFragment)cinemasFragment).save(cinema);
        }
        transaction.hide(addCinemaFragment).show(cinemasFragment ).commit();
        tvTitle.setText(titleArry.get(R.id.bar_title_tv_add_cinema));
        }
    }

