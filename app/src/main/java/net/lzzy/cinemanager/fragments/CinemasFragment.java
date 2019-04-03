package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.lifecycle.GeneratedAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MethodCallsLogger;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.utils.ViewUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.nio.channels.CancelledKeyException;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class CinemasFragment extends BaseFragment {
    private static final CinemaFactory OUR_INSTANCE = new CinemaFactory();
    public static final String CINEMA = "cinema";
    private ListView lv;
    private List<Cinema>cinemas;
    private CinemaFactory factory=CinemaFactory.getInstance();
    private GenericAdapter<Cinema> adapter;
    private Cinema cinema;
    private OnCinemaSelectedListenrt listener;
    public static CinemaFactory getInstance() {
        return OUR_INSTANCE;
    }

    //静态的方法参数
    public static CinemasFragment newInstance(Cinema cinema){
        CinemasFragment fragment=new CinemasFragment();
        Bundle args=new Bundle();
        args.putParcelable(CINEMA,cinema);
        fragment.setArguments(args);
        return fragment;

    }
    //静态的方法传递
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            Cinema cinema=getArguments().getParcelable(CINEMA);
            this.cinema=cinema;
        }
    }

    @Override
    protected void populate() {
       lv=find(R.id.activity_cinema_lv);
       View empty=find(R.id.activity_cinemas_tv_none);
       lv.setEmptyView(empty);
        cinemas=factory.get();
        adapter = new GenericAdapter<Cinema>(getActivity(),
                R.layout.cinemas_item,cinemas)  {
            @Override
            public void populate(ViewHolder holder, Cinema cinema) {
                holder.setTextView(R.id.cinemas_items_tv_name,cinema.getName())
                        .setTextView(R.id.cinemas_items_tv_location,cinema.getLocation());
            }

            @Override
            public boolean persistInsert(Cinema cinema) {
                return factory.addCinema(cinema);
            }

            @Override
            public boolean persistDelete(Cinema cinema) {
                return factory.deleteCinema(cinema);
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onCinemaSelected(adapter.getItem(position).getId().toString());

            }
        });
        if (cinema!=null){
            save(cinema);
        }


    }

    public void  save(Cinema cinema){
        adapter.add(cinema);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_cinemas;
    }

    @Override
    public void search(String kw) {
        cinemas.clear();
        if (TextUtils.isEmpty(kw)){
            cinemas.addAll(factory.get());
        }else {
            cinemas.addAll(factory.searchCinemas(kw));
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  OnCinemaSelectedListenrt)
        try{
            listener=(OnCinemaSelectedListenrt)context;
        }catch (CancelledKeyException e){
            throw new ClassCastException(context.toString()+"必须实现OnCinemaSelectedListenrt");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    public interface  OnCinemaSelectedListenrt{
        void onCinemaSelected(String cinemaId);
    }


}