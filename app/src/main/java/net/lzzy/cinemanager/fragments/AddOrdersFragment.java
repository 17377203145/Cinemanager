package net.lzzy.cinemanager.fragments;

import android.content.Context;

import net.lzzy.cinemanager.R;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddOrdersFragment extends BaseFragment {
    private OnFragmentInterctionListener listener;

    public AddOrdersFragment(){}

    @Override
    protected void populate() {
        listener. hidesSearch();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.add_fragment_orders;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onResume() {
        super.onResume();
        listener.hidesSearch();
    }

    //4.赋值和销毁
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener=(OnFragmentInterctionListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现OnFragmentInterctionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener.hidesSearch();
    }
}
