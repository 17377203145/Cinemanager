package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddCinemasFragment extends BaseFragment {
    private String province="广西壮族自治区";
    private String city="柳州市";
    private String area="鱼峰区";
    //3.定义接口对象
    private OnFragmentInterctionListener listener;
    private OnCinemasCreatedListener cinemLister;
    private EditText edtName;
    private TextView tvArea;




    public AddCinemasFragment(){}

    @Override
    protected void populate() {
        listener.hidesSearch();
        tvArea = find(R.id.dialog_add_tv_area);
        edtName = find(R.id.dialog_add_cinema_edt_name);
        find(R.id.dialog_add_cinema_btn_cancel).setOnClickListener(v -> {
         cinemLister.cancelAddCinema();
        });
        find(R.id.dialog_add_cinema_layout_area).setOnClickListener(v -> {
            JDCityPicker cityPicker = new JDCityPicker();
            cityPicker.init(getActivity());
            cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                @Override
                public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                    AddCinemasFragment.this.province=province.getName();
                    AddCinemasFragment.this.city=city.getName();
                    AddCinemasFragment.this.area=district.getName();
                    String loc=province.getName()+city.getName()+district.getName();
                    tvArea.setText(loc);
                }

                @Override
                public void onCancel() {
                }
            });
            cityPicker.showCityPicker();
        });
        find(R.id.dialog_add_cinema_btn_save).setOnClickListener(v -> {
            String name=edtName.getText().toString();
            Cinema cinema=new Cinema();
            cinema.setName(name);
            cinema.setArea(area);
            cinema.setCity(city);
            cinema.setProvince(province);
            cinema.setLocation(tvArea.getText().toString());
            edtName.setText("");
            cinemLister.saveCineam(cinema);

        });

    }

    @Override
    public int getLayoutRes() {
        return R.layout.add_fragment_cinemas;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            listener.hidesSearch();
        }
    }

    //4.赋值和摧毁
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
        listener=(OnFragmentInterctionListener) context;
        cinemLister= (OnCinemasCreatedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现OnFragmentInterctionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
        cinemLister=null;
    }
     //取消保存
    public interface OnCinemasCreatedListener{
        void cancelAddCinema();
       //保存
        void saveCineam(Cinema cinema);
    }
}
