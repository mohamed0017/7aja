package com.haja.haja.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.haja.haja.R;
import com.haja.haja.Service.ApiService;
import com.haja.haja.Service.model.ProductImgs;
import com.haja.haja.View.ui.ProductDetails.ImageFullScreenActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<ProductImgs> images;

    public SliderAdapterExample(Context context, List<ProductImgs> imgs) {
        this.context = context;
        this.images = imgs;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        Picasso.get().load(ApiService.IMAGEBASEURL + images.get(position).getImg())
                .placeholder(context.getResources().getDrawable(R.drawable.placeholder))
                .error(context.getResources().getDrawable(R.drawable.placeholder)).into(viewHolder.imageViewBackground);

        viewHolder.imageViewBackground.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageFullScreenActivity.class);
            intent.putParcelableArrayListExtra("fullScreenImg", (ArrayList<? extends Parcelable>) images);
            context.startActivity(intent);
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return images.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.imgSlider);
            // textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;

        }
    }
}