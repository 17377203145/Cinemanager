package net.lzzy.cinemanager.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.cinemanager.utils.ViewUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class OrdersFragment extends BaseFragment {

    public static final String ORDER = "order";
    private ListView lv;
    private List<Order> orders;
    private OrderFactory factory=OrderFactory.getInstance();
    private GenericAdapter<Order> adapter;
    private Order order;
    private boolean isDelete=false;
    private float touchX1;
    public static final float MIN_DISTANCE = 100;

    //静态的方法参数
    public static OrdersFragment newInstance(Order order){
        OrdersFragment fragment=new OrdersFragment();
        Bundle args=new Bundle();
        args.putParcelable(ORDER,order);
        fragment.setArguments(args);
        return fragment;

    }
    //静态的方法传递
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            Order order=getArguments().getParcelable(ORDER);
            this.order=order;
        }
    }

        @Override
    protected void populate() {
        lv=find(R.id.main_lv_order);
            /** 无数据视图 **/
            View empty=find(R.id.order_tv_none);
            lv.setEmptyView(empty);
            orders=factory.get();
            adapter=new GenericAdapter<Order>(getActivity(),R.layout.order_item,orders) {
                @Override
                public void populate(ViewHolder viewHolder, Order order) {
                    String location=String.valueOf(CinemasFragment.getInstance()
                    .getById(order.getCinemaId().toString()));

                  viewHolder.setTextView(R.id.order_item_movie,order.getMovie())
                          .setTextView(R.id.order_item_location,location);

                    //region 删除
                    /** 侧滑删除 **/
                    Button btn=viewHolder.getView(R.id.order_item_btn);
                    btn.setOnClickListener(v -> new AlertDialog.Builder(getActivity())
                            .setTitle("删除确认")
                            .setMessage("要删除订单吗？")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确认",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isDelete = false;
                                    adapter.remove(order);
                                }
                            }).show());

                    int visible = isDelete?View.VISIBLE:View.GONE;
                    btn.setVisibility(visible);

                    viewHolder.getConvertView().setOnTouchListener(new ViewUtils.AbstractTouchHandler() {
                        @Override
                        public boolean handleTouch(MotionEvent event) {
                            slideToDelete(event,order,btn);
                            return true;
                        }
                    });
                    //endregion

                }
                private void slideToDelete(MotionEvent event, Order order, Button btn) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            touchX1=event.getX();
                            break;
                        case MotionEvent.ACTION_UP:
                            float touchX2=event.getX();
                            if (touchX1-touchX2 > MIN_DISTANCE){
                                if (!isDelete) {
                                    btn.setVisibility(View.VISIBLE);
                                    isDelete=true;
                                }
                            }else {
                                if(btn.isShown()){
                                    btn.setVisibility(View.GONE);
                                    isDelete=false;
                                }else {
                                    clickOrder(order);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
                private void clickOrder(Order order) {
                    Cinema cinema= CinemaFactory.getInstance().getById(order.getCinemaId().toString());
                    String content="["+order.getMovie()+"]"+order.getMovieTime()
                            +"\n"+cinema.toString()+"  票价为："+order.getPrice()+"元";
                    View view= LayoutInflater.from(getActivity()).inflate(R.layout.diglog_qrcode,null);
                    ImageView img=view.findViewById(R.id.dialog_qrcode_img);
                    img.setImageBitmap(AppUtils.createQRCodeBitmap(content,300,300));
                    new AlertDialog.Builder(getActivity())
                            .setView(view).show();
                }


                @Override
                public boolean persistInsert(Order order) {
                    return factory.addOrder(order);
                }

                @Override
                public boolean persistDelete(Order order) {
                    return factory.delete(order);
                }
            };
            lv.setAdapter(adapter);
            if (order!=null){
                save(order);
            }

    }
    public void save(Order order){
        adapter.add(order);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_orders;
    }

    @Override
    public void search(String kw) {

    }
}
