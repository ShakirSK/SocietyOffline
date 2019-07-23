package test.soc365.society365.maneger.MainReportTap;


import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;


/**
 * Created by Jerry on 3/17/2018.
 */

public class ReportRecyclerViewItemHolder extends RecyclerView.ViewHolder {

    private TextView carTitleText = null;

    private ImageView carImageView = null;

    public ReportRecyclerViewItemHolder(View itemView) {
        super(itemView);

        if(itemView != null)
        {
//
//            carTitleText = (TextView)itemView.findViewById(R.id.tvAnimalName);

         //   carImageView = (ImageView)itemView.findViewById(R.id.card_view_image);
        }
    }

    public TextView getCarTitleText() {
        return carTitleText;
    }

    public ImageView getCarImageView() {
        return carImageView;
    }
}